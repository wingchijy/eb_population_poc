package com.everbridge.wing.MqManage;

import com.rabbitmq.client.AMQP;
import java.util.HashSet;

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
    private HashSet<String> routeKeySet = new HashSet<String>();


    public void init(MqMethod mqMethod, MqResourceParams params, int no) throws Exception
    {
        this.mqMethod = mqMethod;
        this.queueParams = params;

        exchangeName = params.getExchangeName();
        queueName = String.format( "%s-%s", params.getQueuePrefix(), no);

        scaleType = queueParams.getScale();

        // create queue.
        queue = mqMethod.createQueue(queueName);
    }


    public void bindQueue(String bindKey) throws Exception
    {
        mqMethod.exchangeBindQueue(exchangeName, queueName, bindKey);

        routeKeySet.add(bindKey);
    }


    public synchronized boolean isFull(int addCount )
    {
        return queue.getMessageCount() + addCount
                > queueParams.getMaxMsgCountInQueue() ;
    }

    public synchronized boolean isFree(){
        return queue.getMessageCount() == 0;
    }


    public synchronized boolean isReusable(MqConfig.Priority prior, int addCount)
    {
        if( priorType != prior )
            return false;

        return queue.getMessageCount() +addCount <
                queueParams.getMaxMsgCountInQueue();
    }


    public void setPriorType(MqConfig.Priority prior){
        priorType = prior;
    }

    public HashSet getRouteKeySet(){
        return routeKeySet;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public String getQueueName() {
        return queueName;
    }
}

