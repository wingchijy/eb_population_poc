package MqManager;

import com.everbridge.wing.MqManage.MqConfig;
import com.everbridge.wing.MqManage.MqManager;
import com.everbridge.wing.MqManage.MqProducer;

public class TestMqProducer
{
    public static void main(String[] args)
    {
        try
        {
            MqConfig conf = MqConfig.getInstance();
            conf.load(args[0]);

            MqProducer producer = new MqProducer();
            for(int i=0; i<10; i++)
            {
                producer.publish(
                        "Notification1"+i,
                        100+i, 1000,  );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(1);
    }

}
