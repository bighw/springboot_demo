package com.maizuo.mq;

import com.hyx.monitor.NsqMessageThread;
import com.maizuo.config.MZConfig;

/**
 * @author harvey
 * @ClassName MqBaseHandler
 * @Email harvey@maizuo.com
 * @create 2017/1/20
 * @Description: NSQ监听服务的基础类，新增的NSQ监听服务需要细节该类
 * 需要继承该类的构造方法和实现 handlerMessage 方法
 */
public abstract class MqBaseHandler extends NsqMessageThread {
    private String host;
    private int port;
    private String topic;
    private String channel;

    public MqBaseHandler(String host, int port, String topic, String channel) {
        this.host = host;
        this.port = port;
        this.topic = topic;
        this.channel = channel;
    }

    public MqBaseHandler(String topic, String channel) {
        this(MZConfig.getInstance().getString("NSQ_HOST"), MZConfig.getInstance().getInt("NSQ_PORT_TCP"), topic, channel);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
