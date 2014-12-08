package com.everbridge.wing.MqManage;

import com.rabbitmq.client.AMQP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MqResource
{
    private String name;
    private String exchangeName;
    private String queueName;

    private MqConfig.Scale scaleType;
    private MqConfig.Priority priorType;
    private MqMethod mqMethod;
    private MqResourceParams queueParams;
    private AMQP.Queue.DeclareOk queue;
    private HashMap<String, MqRouteKey> routeKeyMap = new HashMap<String, MqRouteKey>();

    private static Logger logger = LoggerFactory.getLogger(MqResource.class);


    public synchronized void init(MqMethod mqMethod, MqResourceParams params, int no) throws Exception
    {
        this.mqMethod = mqMethod;
        this.queueParams = params;

        exchangeName = params.getExchangeName();
        queueName = String.format( "%s-%s", params.getQueuePrefix(), no);

        scaleType = queueParams.getScale();

        // create queue.
        queue = mqMethod.createQueue(queueName);

        logger.info(String.format("MqResource-init: exchangeName=%s | queueName=%s | scaleType=%s ",
                exchangeName, queueName, scaleType.getName()));
    }


    public synchronized void bindQueue(String bindKey) throws Exception
    {
        mqMethod.exchangeBindQueue(exchangeName, queueName, bindKey);

        routeKeyMap.put(bindKey, new MqRouteKey(bindKey));
    }


    public synchronized void unbindKey(String bindKey) throws Exception
    {
        mqMethod.unbindQueue(exchangeName, queueName, bindKey);

        routeKeyMap.remove(bindKey);
    }

    public synchronized void clearBind() throws Exception
    {
        for(Map.Entry entry : routeKeyMap.entrySet() )
        {
            String bindKey = (String)entry.getValue();
            MqRouteKey route = (MqRouteKey)entry.getValue();

            if( route.isTimeout() )
                this.unbindKey(bindKey);
        }
    }


    public synchronized boolean isFull(int addCount )
    {
        return queue.getMessageCount() + addCount > queueParams.getMaxMsgCountInQueue() ;
    }

    public synchronized boolean isFree(){
        return queue.getMessageCount() == 0;
    }


    public synchronized boolean isBoundKey(String bindKey){
        if( routeKeyMap.get(bindKey) == null)
            return false;
        return true;
    }


    public synchronized boolean isReusable(MqConfig.Priority prior, int addCount)
    {
        if( priorType != prior )
            return false;

        return queue.getMessageCount() +addCount < queueParams.getMaxMsgCountInQueue();
    }


    public synchronized boolean isConsumersEnough()
    {
        if( priorType == MqBase.Priority.HIGH ) {
            return queue.getConsumerCount() < queueParams.getMaxConsumersOneHighQueue();
        }
        else if( priorType == MqBase.Priority.NORMAL ) {
            return queue.getConsumerCount() < queueParams.getMaxConsumersOneNormalQueue();
        }
        else
            return false;
    }


    public void setPriorType(MqConfig.Priority prior){
        priorType = prior;
    }

    public MqConfig.Priority getPriorType() {
        return priorType;
    }


    public String getExchangeName() {
        return exchangeName;
    }

    public String getQueueName() {
        return queueName;
    }

    public int getQueueMessageCount(){
        return queue.getMessageCount();
    }
}

