package com.everbridge.wing.MqManage;

import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;


public class MqConsumer
{
    private MqMethod mqMethod;

    private static Logger logger = LoggerFactory.getLogger(MqConsumer.class);


    public MqConsumer()  throws IOException
    {
        mqMethod = new MqMethod();
        mqMethod.connect();
    }


    /*
    */
    public MqConfig.Result consume() throws Exception
    {
        MqResource res = MqManager.getInstance().getConsumeMqResource();
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

