package com.everbridge.wing.MqManage;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;


public class MqMethod
{
    protected Connection connection;
    protected Channel channel;

    private static Logger logger = LoggerFactory.getLogger(MqMethod.class);


    /*
       Connect.
    */
    public void connect() throws IOException
    {
        ConnectionFactory factory = new ConnectionFactory();

        if( MqConfig.MQ_HOSTS.length == 1 )
        {
            String host = MqConfig.MQ_HOSTS[0];

            if( host.indexOf(":") > 0 )
            {
                factory.setHost( host.split(":")[0].trim() );
                factory.setPort(Integer.parseInt(host.split(":")[1].trim()));

                logger.info( String.format("Connect host: [host=%s | port=%s]",
                        host.split(":")[0], host.split(":")[1]));
            }
            else{
                factory.setHost( host.trim() );
            }

            connection = factory.newConnection();
        }
        else if( MqConfig.MQ_HOSTS.length > 1 )
        {
            Address[] addresses = new Address[MqConfig.MQ_HOSTS.length];

            for(int i=0; i<MqConfig.MQ_HOSTS.length; i++)
            {
                String host = MqConfig.MQ_HOSTS[i];

                if( host.indexOf(":") > 0 ){
                    addresses[i] = new Address(
                            host.split(":")[0].trim(),
                            Integer.parseInt( host.split(":")[1].trim()) );
                }
                else{
                    addresses[i] = new Address( host.trim() );
                }

                logger.info( String.format("Connect host: [host=%s | port=%s]",
                        host.split(":")[0], host.split(":")[1]));
            }

            connection = factory.newConnection(addresses);
        }
    }



    /*
       Exchange.
    */
    public AMQP.Exchange.DeclareOk createExchange(String exchangeName) throws IOException
    {
        return channel.exchangeDeclare(
                exchangeName,
                MqConfig.MQ_EXCHANGE_TYPE,
                true);
    }


    /*
       Queue.
    */
    public AMQP.Queue.DeclareOk createQueue(String queueName) throws Exception
    {
//        Map<String, Object> args = null;
//        args = new HashMap<String, Object>();
//        args.put("x-ha-policy", "all");

        return channel.queueDeclare(
                queueName,
                MqConfig.MQ_DURABLE,
                false,
                false,
                null );
    }


    public AMQP.Queue.BindOk exchangeBindQueue(String exchangeName, String queueName, String routingKey) throws Exception
    {
        return channel.queueBind(
                queueName,
                exchangeName,
                routingKey );
    }


    /*
        Produce.
    */
    public void publish(String exchangeName, String routingKey, byte[] message) throws IOException
    {
        channel.basicPublish(
                exchangeName,
                routingKey,
                MessageProperties.BASIC,
                message);
    }


    /*
        Consume.
        noAck = true，不需要回复，接收到消息后，queue上的消息就会清除
        noAck = false，需要回复，接收到消息后，queue上的消息不会被清除，
          直到调用channel.basicAck(deliveryTag, false); queue上的消息才会被清除
          而且，在当前连接断开以前，其它客户端将不能收到此queue上的消息.
    */
    public GetResponse pollingConsume(String queueName) throws Exception
    {
        GetResponse response = channel.basicGet(
                queueName,
                MqConfig.MQ_CONSUME_ACK);

        if(response == null)
            return null;

        channel.basicAck(
                response.getEnvelope().getDeliveryTag(),
                MqConfig.MQ_CONSUME_ACK );

        return response;
    }
}

