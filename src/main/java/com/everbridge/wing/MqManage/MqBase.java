package com.everbridge.wing.MqManage;


import org.apache.commons.configuration.XMLConfiguration;


public class MqBase
{
    protected static XMLConfiguration xmlConfig;

    public static String[] MQ_HOSTS;
    public static boolean MQ_DURABLE;
    public static String  MQ_EXCHANGE_TYPE;
    public static boolean MQ_CONSUME_ACK;
    public static int MQ_UNBIND_TIMEOUT;

    public static int HIGH_PRIOR_THRESHOLD;
    public static int BIG_SCALE_THRESHOLD;
    public static int SMALL_SCALE_THRESHOLD;


    public enum Priority
    {
        HIGH("high"), NORMAL("normal");

        private String name;

        private Priority(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Priority getPriority(int value)
        {
            if( value >= MqBase.HIGH_PRIOR_THRESHOLD ) {
                return Priority.HIGH;
            }
            return Priority.NORMAL;
        }
    }


    public enum Scale
    {
        BIG("big"), MIDDLE("middle"), SMALL("small");

        private String name;

        private Scale(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Scale getScale(int value)
        {
            if( value >= MqBase.BIG_SCALE_THRESHOLD ) {
                return Scale.BIG;
            }
            else if( value <= MqBase.SMALL_SCALE_THRESHOLD ) {
                return Scale.SMALL;
            }
            else {
                return Scale.MIDDLE;
            }
        }

        public static Scale getScale(String value)
        {
            if( "big".equals(value) ) {
                return Scale.BIG;
            }
            else if( "small".equals(value) ) {
                return Scale.SMALL;
            }
            else {
                return Scale.MIDDLE;
            }
        }
    }


    public enum Result
    {
        Success,
        Fail,
        NoResourceAvailable,
        NoMessageToSend,
        NoMessageToConsume,
//        BindButQueueFull
    }


    public interface MqProducer{
        void publish();
    }

}

