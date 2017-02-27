package com.maizuo.task;

import com.maizuo.api3.commons.util.LogUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author qiyang
 * @ClassName: TemplateTask
 * @Description: 模板定时器
 * @Email qiyang@maizuo.com
 * @date 2016/8/15 0015
 */
@Component
@Configurable
@EnableScheduling
public class TemplateTask {
    @Scheduled(cron = "0 0/30 15-22 * * ?")
    @Async
    public void task() {
        LogUtils.info(System.currentTimeMillis()+"");
    }
}
