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
        MQ_UNBIND_TIMEOUT = xmlConfig.getInt("MqParams/unbindTimeout");

        logger.info( String.format("Mq-params:[hostCount=%d | durable=%s | " +
                        "exchangeType=%s | consumeAck=%s | unbindTimeout=%d]",
                MQ_HOSTS.length, MQ_DURABLE, MQ_EXCHANGE_TYPE,
                MQ_CONSUME_ACK, MQ_UNBIND_TIMEOUT) );


        HIGH_PRIOR_THRESHOLD = xmlConfig.getInt( "MqParams/highPriorThreshold" );
        BIG_SCALE_THRESHOLD = xmlConfig.getInt( "MqParams/bigScaleThreshold" );
        SMALL_SCALE_THRESHOLD = xmlConfig.getInt( "MqParams/smallScaleThreshold" );

        logger.info( String.format("Mq-params:[highPriorThreshold=%d | " +
                        "bigScaleThreshold=%d | smallScaleThreshold=%d]",
                HIGH_PRIOR_THRESHOLD, BIG_SCALE_THRESHOLD, SMALL_SCALE_THRESHOLD) );
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

        params.setQueueCount(xmlConfig.getInt(String.format(
                "MqResourceParams/resourceGroup[name = '%s']/queueCount", name)));

        params.setMaxHighPriorCount(xmlConfig.getInt(String.format(
                "MqResourceParams/resourceGroup[name = '%s']/maxHighPriorCount", name)));

        params.setMaxMsgCountInQueue(xmlConfig.getInt(String.format(
                "MqResourceParams/resourceGroup[name = '%s']/maxMsgCountInQueue", name)));

        params.setMaxConsumersOneHighQueue(xmlConfig.getInt(String.format(
                "MqResourceParams/resourceGroup[name = '%s']/maxConsumersOneHighQueue", name)));

        params.setMaxConsumersOneNormalQueue(xmlConfig.getInt(String.format(
                "MqResourceParams/resourceGroup[name = '%s']/maxConsumersOneNormalQueue", name)));

        logger.info( String.format("ResourceParams:[scale=%s | exchangeName=%s | queueName=%s | queueCount=%d | " +
                        "maxHighPriorCount=%s | maxMsgCountInQueue=%d  | maxConsumersOneHighQueue=%d | maxConsumersOneNormalQueue=%d]",
                params.getScale().getName(), params.getExchangeName(), params.getQueuePrefix(), params.getQueueCount(),
                params.getMaxHighPriorCount(), params.getMaxMsgCountInQueue(), params.getMaxConsumersOneHighQueue(),
                params.getMaxConsumersOneNormalQueue()) );

        resourceParamsGroup.add(params);
    }


    public ArrayList<MqResourceParams> getResourceParamsGroup() {
        return resourceParamsGroup;
    }
}

