package com.everbridge.wing.MqManage;

import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class MqConsumer
{
    private MqMethod mqMethod;
    private MqTask mqTask;

    private static Logger logger = LoggerFactory.getLogger(MqConsumer.class);


    public MqConsumer()  throws IOException
    {
        mqMethod = new MqMethod();
        mqMethod.connect();
    }

    public void init(MqBase.Scale scaleType)
    {
        mqTask = new MqTask();
        mqTask.setScaleType(scaleType);
    }


    /*
    */
    public MqConfig.Result consume() throws Exception
    {
        MqResource res = MqManager.getInstance().getConsumeMqResource(mqTask);
        if( res == null ){
            return MqConfig.Result.NoMessageToConsume;
        }

        while (true)
        {
            try{
                GetResponse response = mqMethod.pollingConsume( res.getQueueName() );

                if ( response == null )
                    return MqBase.Result.NoMessageToConsume;
            }
            catch (Exception e) {
                e.printStackTrace();
                return MqBase.Result.Fail;
            }
        }
    }

}

