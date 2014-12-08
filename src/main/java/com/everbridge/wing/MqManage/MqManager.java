package com.everbridge.wing.MqManage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


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


    public void init() throws Exception
    {
        mqMethod = new MqMethod();
        groupMap = new HashMap<String, MqResourceGroup>();

        createResourceGroup();
    }

    /*
        Create resource group.
    */
    public void createResourceGroup() throws Exception
    {
        for(MqResourceParams params: MqConfig.getInstance().getResourceParamsGroup() )
        {
            MqResourceGroup group = new MqResourceGroup(params);

            mqMethod.createExchange(params.getExchangeName());

            for( int i=0; i<params.getMaxQueueCount(); i++ )
            {
                MqResource res = new MqResource();
                res.init(mqMethod, params, i);

                group.getResources().add(res);
            }

            groupMap.put(params.getScale().getName(), group);
        }
    }


    /*
        Get Mq-Resource.
     */
    public MqResource getMqResource(MqTask mqTask) throws Exception
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

    public MqResource isBoundResource(MqResourceGroup group, String routeKey)
    {
        for( MqResource res: group.getResources() ){
            if ( res.getRouteKeySet().contains(routeKey) )
                return res;
        }
        return null;
    }

    public MqResource hasFreeResource(MqResourceGroup group, MqTask mqTask) throws Exception
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


    public MqResource isReusableResource(MqResourceGroup group, MqTask mqTask)
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


    /*
        Get Mq-Resource's to consume by polling mode.
     */
    public MqResource getConsumeMqResource()
    {

        return null;
    }

}

