package com.maizuo.constants;

/**
 * @author rose
 * @ClassName: MQConfig
 * @Email rose@maizuo.com
 * @create 2017/1/16-14:13
 * @Description: 消息主题和通道备案文件
 */
public class MQConstants {

    //用户登录成功信息topic
    public static final String MQ_USER_LOGIN_TOPIC  = "MAIZUO_USER_LOGIN_SUC";
    // topic test
    public static final String PUSH_TEST_TOPIC = "THIS_GAVINTEST";

    // 用户登录成功，nsq监听的channel
    public static final String MQ_USER_LOGIN_CHANNEL = "localTest";
    // nsq测试用的 channel
    public static final String PUSH_TEST_CHANNEL = "localTest";
}
