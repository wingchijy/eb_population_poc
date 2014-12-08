package com.everbridge.wing.MqManage;

import com.rabbitmq.client.AMQP;

import java.util.HashSet;

public class MqRouteKey
{
    private String key;
    private long startTime;

    public MqRouteKey(String key) {
        this.key = key;
        startTime = System.currentTimeMillis();
    }

    public void setTime() {
        startTime = System.currentTimeMillis();
    }

    public  boolean isTimeout() {
        long nowTime = System.currentTimeMillis();

        return  (nowTime - startTime) / 1000  > MqBase.MQ_UNBIND_TIMEOUT;
     }

}

