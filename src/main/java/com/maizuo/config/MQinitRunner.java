package com.maizuo.config;

import com.maizuo.mq.MqMonitor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author harvey
 * @ClassName MQinitRunner
 * @Email harvey@maizuo.com
 * @create 2017/2/8 9:40
 * @Description:
 */
@Component
@Order(value = 5)
public class MQinitRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        MqMonitor.init();
    }
}
