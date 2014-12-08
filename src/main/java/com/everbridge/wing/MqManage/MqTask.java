package com.everbridge.wing.MqManage;

import java.util.ArrayList;

public class MqTask
{
    private String name;
    private String topic;
    private MqConfig.Priority priorType;
    private MqConfig.Scale scaleType;
    private ArrayList<byte[]> messages;


    public MqTask(String taskName, int priorValue,
                  int scaleValue,  ArrayList<Object> messages)
    {
        this.name = taskName;

        this.priorType = MqConfig.Priority.getPriority(priorValue);

        this.scaleType = MqConfig.Scale.getScale(scaleValue);

        this.topic = String.format("%s-%s",
                this.scaleType.getName(),
                this.priorType.getName() );

        setMessages(messages);
    }


    public MqConfig.Scale getScaleType() {
        return scaleType;
    }

    public MqConfig.Priority getPriorType() {
        return priorType;
    }

    public String getTopic(){
        return topic;
    }

    public String getRouteKey(){
        return String.format("%s-%s", topic, name);
    }


    public void setMessages(ArrayList<Object> messages){
        // serialize.
    }

    public ArrayList<byte[]> getMessages() {
        return messages;
    }
}

