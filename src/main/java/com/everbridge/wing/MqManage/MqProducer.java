package com.everbridge.wing.MqManage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;


public class MqProducer
{
    private MqMethod mqMethod;

    private static Logger logger = LoggerFactory.getLogger(MqProducer.class);


    public MqProducer()  throws IOException
    {
        mqMethod = new MqMethod();
        mqMethod.connect();
    }


    /*
        1.set task properties.
        2.get mq-resource.
        3.send message.
    */
    public MqConfig.Result publish( String taskName, int taskPrior,
                         int taskScale, ArrayList<Object> messages ) throws Exception
    {
        if( messages.size() == 0 )
            return MqConfig.Result.NoMessageToSend;

        MqTask task = new MqTask( taskName, taskPrior, taskScale, messages );

        MqResource res = MqManager.getInstance().getMqResource(task);
        if( res == null ){
            return MqConfig.Result.NoResourceAvailable;
        }

        for( int i=0; i<task.getMessages().size(); i++ )
        {
            mqMethod.publish(
                res.getExchangeName(),
                String.format( "%s.%d", task.getRouteKey(), i),
                task.getMessages().get(i) );
        }

        return MqConfig.Result.Success;
    }

}

