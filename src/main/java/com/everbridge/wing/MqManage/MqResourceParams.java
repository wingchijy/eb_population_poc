package com.everbridge.wing.MqManage;


public class MqResourceParams
{
    private MqConfig.Scale scale;
    private String exchangeName;
    private String queuePrefix;

    private int queueCount;
    private int maxHighPriorCount;
    private int maxMsgCountInQueue;
    private int maxUnbindTimeout;
    private int maxConsumersOneHighQueue;
    private int maxConsumersOneNormalQueue;


    public MqConfig.Scale getScale() {
        return scale;
    }

    public void setScale(MqConfig.Scale scale) {
        this.scale = scale;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getQueuePrefix() {
        return queuePrefix;
    }

    public void setQueuePrefix(String queuePrefix) {
        this.queuePrefix = queuePrefix;
    }

    public int getQueueCount() {
        return queueCount;
    }

    public void setQueueCount(int queueCount) {
        this.queueCount = queueCount;
    }

    public int getMaxHighPriorCount() {
        return maxHighPriorCount;
    }

    public void setMaxHighPriorCount(int maxHighPriorCount) {
        this.maxHighPriorCount = maxHighPriorCount;
    }

    public int getMaxMsgCountInQueue() {
        return maxMsgCountInQueue;
    }

    public void setMaxMsgCountInQueue(int maxMsgCountInQueue) {
        this.maxMsgCountInQueue = maxMsgCountInQueue;
    }

    public int getMaxUnbindTimeout() {
        return maxUnbindTimeout;
    }

    public void setMaxUnbindTimeout(int maxUnbindTimeout) {
        this.maxUnbindTimeout = maxUnbindTimeout;
    }


    public int getMaxConsumersOneHighQueue() {
        return maxConsumersOneHighQueue;
    }

    public void setMaxConsumersOneHighQueue(int maxConsumersOneHighQueue) {
        this.maxConsumersOneHighQueue = maxConsumersOneHighQueue;
    }

    public int getMaxConsumersOneNormalQueue() {
        return maxConsumersOneNormalQueue;
    }

    public void setMaxConsumersOneNormalQueue(int maxConsumersOneNormalQueue) {
        this.maxConsumersOneNormalQueue = maxConsumersOneNormalQueue;
    }
}

