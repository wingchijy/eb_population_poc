package com.everbridge.wing.MqManage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class MqManager
{
    private static MqManager mqManager;
    private static MqMethod mqMethod;
    private Map<String, MqResourceGroup> groupMap;

    private static Logger logger = LoggerFactory.getLogger(MqManager.class);


    public static MqManager getInstance() throws Exception
    {
        if (mqManager == null){
            mqManager = new MqManager();
        }
        return mqManager;
    }


    public void  init() throws Exception
    {
        mqMethod = new MqMethod();
        mqMethod.connect();

        groupMap = new HashMap<String, MqResourceGroup>();

        createResourceGroup();
    }

    /*
        Create resource group.
    */
    public synchronized void createResourceGroup() throws Exception
    {
        for(MqResourceParams params: MqConfig.getInstance().getResourceParamsGroup() )
        {
            MqResourceGroup group = new MqResourceGroup(params);

            System.out.println("----------"+params.getExchangeName());

            mqMethod.createExchange(params.getExchangeName());

            for( int i=0; i<params.getQueueCount(); i++ )
            {
                MqResource res = new MqResource();
                res.init(mqMethod, params, i);

                group.addResource(res);
            }

            groupMap.put(params.getScale().getName(), group);
        }
    }


    /*
        Get Mq-Resource.
     */
    public synchronized MqResource getMqResource(MqTask mqTask) throws Exception
    {
        MqResourceGroup group = groupMap.get( mqTask.getScaleType().getName() );
        if ( group == null )
            return  null;

        MqResource res = isBoundResource(group, mqTask.getRouteKey());
        if( res != null ) {
            if ( res.isFull( mqTask.getMessages().size() ) )
                return null;
            else
                return res;
        }

        res = hasFreeResource(group, mqTask);
        if( res != null ){
            return res;
        }
//        logger.info( String.format(
//                "key=(%s), resources isn't exists. ", routeKey) );

        res = isReusableResource(group, mqTask );
        if( res != null ){
            return res;
        }

        return null;
    }


    public synchronized MqResource isBoundResource(MqResourceGroup group, String routeKey)
    {
        for( MqResource res: group.getResources() ){
            if ( res.isBoundKey(routeKey) )
                return res;
        }
        return null;
    }

    public synchronized MqResource hasFreeResource(MqResourceGroup group, MqTask mqTask) throws Exception
    {
        for( MqResource res: group.getResources() ){
            if( res.isFree() && group.increaseHighPrior() )
            {
                res.setPriorType(mqTask.getPriorType());
                res.bindQueue(mqTask.getRouteKey());
                return res;
            }
        }
        return null;
    }


    public synchronized MqResource isReusableResource(MqResourceGroup group, MqTask mqTask)
    {
        for( MqResource res: group.getResources() ){
            if ( res.isReusable(mqTask.getPriorType(),mqTask.getMessages().size()) )
                return res;
        }
        return null;
    }


    /*
        Unbind Mq-Resource's exchange&queue relation.
     */
    public synchronized MqConfig.Result unbindMqResource(MqTask mqTask) throws Exception
    {
        MqResourceGroup group = groupMap.get( mqTask.getScaleType().getName() );
        if ( group == null )
            return  MqConfig.Result.Fail;

        for( MqResource res: group.getResources() ){
            res.unbindKey( mqTask.getRouteKey() );
        }

        return MqConfig.Result.Success;
    }


    /*
        Get Mq-Resource's to consume by polling mode.
     */
    public synchronized MqResource getConsumeMqResource(MqTask mqTask)
    {
        MqResourceGroup group = groupMap.get( mqTask.getScaleType().getName() );
        if ( group == null )
            return  null;

        MqResource res = findConsumeResource(group, mqTask);
        if( res != null ) {
            return res;
        }

        return null;
    }

    public synchronized MqResource findConsumeResource(MqResourceGroup group, MqTask mqTask)
    {
        ArrayList<MqResource> resList = group.getResources();
        SortConsumePrior comparator=new SortConsumePrior();
        Collections.sort(resList, comparator);

        for( MqResource res: resList ){
            if ( res.isConsumersEnough() )
                return res;
        }

        return null;
    }

    public class SortConsumePrior implements Comparator
    {
        public int compare(Object arg0, Object arg1) {
            MqResource res0 = (MqResource)arg0;
            MqResource res1 = (MqResource)arg1;

            // compare queue priority.
            if( res0.getPriorType() != res1.getPriorType() ){
                if( res0.getPriorType() == MqBase.Priority.HIGH )
                    return 1;
                else
                    return -1;
            }
            // compare queue message count.
            else {
                if( res0.getQueueMessageCount() > res1.getQueueMessageCount() )
                    return 1;
                else if( res0.getQueueMessageCount() == res1.getQueueMessageCount() )
                    return 0;
                else
                    return -1;
            }
        }
    }

}

