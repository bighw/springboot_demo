package com.maizuo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import javax.servlet.annotation.WebFilter;

/**
 * @author qiyang
 * @className Application
 * @description springboot启动类
 * @email qiyang@maizuo.com
 * @date 2016/9/5 0005 14:37
 */
@SpringBootApplication
@WebFilter
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        // 项目启动后需要马上启用的服务列表
        // 用来启动MQ(nsq)的监听服务
//        MqMonitor.init();
    }
}
