package com.everbridge.wing.MqManage;

import java.util.ArrayList;

public class MqTask
{
    private String name;
    private String topic;
    private MqConfig.Priority priorType;
    private MqConfig.Scale scaleType;
    private ArrayList<byte[]> messages;


    public void init(String taskName, int priorValue,
                  int scaleValue,  ArrayList<Object> messages) throws Exception
    {
        this.name = taskName;

        setPriorType(priorValue);

        setScaleType(scaleValue);

        this.topic = String.format("%s-%s",
                this.scaleType.getName(),
                this.priorType.getName() );

        if(messages != null)
            setMessages(messages);
    }


    public MqConfig.Scale getScaleType() {
        return scaleType;
    }

    public void setScaleType(int scaleValue) {
        this.scaleType = MqConfig.Scale.getScale(scaleValue);
    }

    public void setScaleType(MqConfig.Scale type) {
        this.scaleType = type;
    }

    public MqConfig.Priority getPriorType() {
        return priorType;
    }

    public void setPriorType(int priorValue) {
        this.priorType = MqConfig.Priority.getPriority(priorValue);
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

