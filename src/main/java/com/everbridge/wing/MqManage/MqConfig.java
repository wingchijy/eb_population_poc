package com.everbridge.wing.MqManage;


import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class MqConfig extends MqBase
{
    private static MqConfig mqConfig;
    private ArrayList<MqResourceParams> resourceParamsGroup = new ArrayList<MqResourceParams>();

    private static Logger logger = LoggerFactory.getLogger(MqConfig.class);


    public static MqConfig getInstance() throws Exception
    {
        if (mqConfig == null){
            mqConfig = new MqConfig();
        }
        return mqConfig;
    }


    public void load(String configFile) throws Exception
    {
        xmlConfig = new XMLConfiguration(configFile);
        xmlConfig.setExpressionEngine( new XPathExpressionEngine() );

        loadMqParams();

        loadResourceParams("bigGroup");
        loadResourceParams("middleGroup");
        loadResourceParams("smallGroup");
    }


    public void loadMqParams() throws Exception
    {
        MQ_HOSTS = xmlConfig.getStringArray( "MqParams/hosts/host" );
        MQ_DURABLE = xmlConfig.getBoolean("MqParams/durable");
        MQ_EXCHANGE_TYPE = xmlConfig.getString("MqParams/exchangeType");
        MQ_CONSUME_ACK = xmlConfig.getBoolean("MqParams/consumeAck");

        logger.info( String.format("MqParams:[hostCount=%s|durable=%s|exchangeType=%s|ack=%s]",
                MQ_HOSTS.length, MQ_DURABLE, MQ_EXCHANGE_TYPE, MQ_CONSUME_ACK) );
    }

    public void loadResourceParams(String name) throws Exception
    {
        MqResourceParams params = new MqResourceParams();

        String scale =xmlConfig.getString(String.format(
                "MqResourceParams/resourceGroup[name = '%s']/scale", name));
        params.setScale( Scale.getScale(scale) );

        params.setExchangeName(xmlConfig.getString(String.format(
                "MqResourceParams/resourceGroup[name = '%s']/exchangeName", name)));

        params.setQueuePrefix(xmlConfig.getString(String.format(
                "MqResourceParams/resourceGroup[name = '%s']/queuePrefix", name)));

        params.setMaxQueueCount(xmlConfig.getInt(String.format(
                "MqResourceParams/resourceGroup[name = '%s']/maxQueueCount", name)));

        params.setMaxHighPriorCount(xmlConfig.getInt(String.format(
                "MqResourceParams/resourceGroup[name = '%s']/maxHighPriorCount", name)));

        params.setMaxMsgCountInQueue(xmlConfig.getInt(String.format(
                "MqResourceParams/resourceGroup[name = '%s']/maxMsgCountInQueue", name)));

        params.setMaxUnbindTimeout(xmlConfig.getInt(String.format(
                "MqResourceParams/resourceGroup[name = '%s']/maxUnbindTimeout", name)));

        resourceParamsGroup.add(params);
    }


    public ArrayList<MqResourceParams> getResourceParamsGroup() {
        return resourceParamsGroup;
    }
}

