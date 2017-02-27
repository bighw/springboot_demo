package com.maizuo.mq.handler;

import com.maizuo.mq.MqBaseHandler;

/**
 * Created by harvey on 2017/1/13.
 * 测试用于处理从nsq消息通道获取到的信息
 */
public class MessageRecieveHandler extends MqBaseHandler {
    public MessageRecieveHandler(String host, int port, String topic, String channel) {
        super(host, port, topic, channel);
    }

    public MessageRecieveHandler(String topic, String channel) {
        super(topic, channel);
    }

    @Override
    public void handlerMessage(String s) {
        System.out.println(s);
    }
}
