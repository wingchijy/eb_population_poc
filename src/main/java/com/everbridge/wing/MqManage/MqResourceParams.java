package com.everbridge.wing.MqManage;


public class MqResourceParams
{
    private MqConfig.Scale scale;
    private String exchangeName;
    private String queuePrefix;

    private int maxQueueCount;
    private int maxHighPriorCount;
    private int maxMsgCountInQueue;
    private int maxUnbindTimeout;


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

    public int getMaxQueueCount() {
        return maxQueueCount;
    }

    public void setMaxQueueCount(int maxQueueCount) {
        this.maxQueueCount = maxQueueCount;
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

}

