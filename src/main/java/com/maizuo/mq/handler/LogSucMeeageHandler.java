package com.maizuo.mq.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maizuo.data.entity.user.User;
import com.maizuo.mq.MqBaseHandler;

import java.io.IOException;

/**
 * Created by harvey on 2017/1/16.
 * 用于处理监听登录topic，获取的消息
 * 简单打印
 */
public class LogSucMeeageHandler extends MqBaseHandler {
    public LogSucMeeageHandler(String host, int port, String topic, String channel) {
        super(host, port, topic, channel);
    }

    public LogSucMeeageHandler(String topic, String channel) {
        super(topic, channel);
    }

    @Override
    public void handlerMessage(String s) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            User user = mapper.readValue(s, User.class);
            System.out.println("从NSQ通道中获取到的登录成功消息：name ="+user.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
