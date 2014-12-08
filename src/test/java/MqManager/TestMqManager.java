package MqManager;

import com.everbridge.wing.MqManage.MqConfig;
import com.everbridge.wing.MqManage.MqManager;

public class TestMqManager
{
    public static void main(String[] args)
    {
        try
        {
            MqConfig conf = MqConfig.getInstance();
            conf.load(args[0]);

            MqManager mgr = new MqManager();
            mgr.init();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(1);
    }

}
