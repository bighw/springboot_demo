package com.maizuo.mq;

import com.hyx.monitor.NsqMonitorController;
import com.maizuo.constants.MQConstants;
import com.maizuo.mq.handler.LogSucMeeageHandler;
import com.maizuo.mq.handler.MessageRecieveHandler;

/**
 * @author harvey
 * @ClassName MqBaseHandler
 * @Email harvey@maizuo.com
 * @create 2017/1/20
 * @Description: NSQ监听控制器
 */
public class MqMonitor {

    /**
     * 在此处添加需要启动的NSQ监听服务，该方法将会随着系统的启动而被调用，支持多个监听服务
     */
    public static void init() {
        MqMonitor.getInstance()
                .start(new LogSucMeeageHandler(MQConstants.MQ_USER_LOGIN_TOPIC, MQConstants.MQ_USER_LOGIN_CHANNEL))
                .start(new MessageRecieveHandler(MQConstants.PUSH_TEST_TOPIC, MQConstants.PUSH_TEST_CHANNEL));
    }



    private static MqMonitor mqMonitor;

    /**
     * 启动单个NSQ监听服务
     *
     * @param mqBaseHandler
     * @return
     */
    private MqMonitor start(MqBaseHandler mqBaseHandler) {
        NsqMonitorController
                .getInstance()
                .setNsqHost(mqBaseHandler.getHost())
                .setNsqPort(mqBaseHandler.getPort())
                .setNsqTopic(mqBaseHandler.getTopic())
                .setNsqChannel(mqBaseHandler.getChannel())
                .setNsqMessageThread(mqBaseHandler)
                .start();
        return this;
    }

    private static MqMonitor getInstance() {
        synchronized (MqMonitor.class) {
            if (mqMonitor == null) {
                return new MqMonitor();
            }
            return mqMonitor;
        }
    }
}
