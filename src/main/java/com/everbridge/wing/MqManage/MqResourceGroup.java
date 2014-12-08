package com.everbridge.wing.MqManage;

import java.util.ArrayList;


public class MqResourceGroup
{
    private MqResourceParams params;
    private ArrayList<MqResource> resources = new ArrayList<MqResource>();
    private int currentHighPriorCount;


    public MqResourceGroup(MqResourceParams params){
        this.params = params;
    }


    public MqResourceParams getConfig() {
        return params;
    }

    public ArrayList<MqResource> getResources() {
        return resources;
    }

    public void addResource(MqResource resource) {
        resources.add(resource);
    }

    public int getCurrentHighPriorCount() {
        return currentHighPriorCount;
    }

    public boolean increaseHighPrior()
    {
        if( currentHighPriorCount >= params.getMaxHighPriorCount() )
            return false;

        currentHighPriorCount += 1;
        return true;
    }

}

