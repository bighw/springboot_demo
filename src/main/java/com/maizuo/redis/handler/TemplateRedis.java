package com.maizuo.redis.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maizuo.redis.BaseRedis;

/**
 * @author qiyang
 * @ClassName: TemplateRedis
 * @Description: 模板
 * @Email qiyang@maizuo.com
 * @date 2016/8/17 0017
 */
@Service
public class TemplateRedis {
    @Autowired
    BaseRedis baseRedis;

    public String testGet(String key) {
        String value = baseRedis.get(key);
        return value;
    }
}
