package com.maizuo.testdemo;

import com.maizuo.api3.commons.util.JsonUtils;
import com.maizuo.common.MockMvcBaseTest;
import com.maizuo.data.entity.user.User;
import com.maizuo.data.response.LoginResponse;
import com.maizuo.service.LoginService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author gavin
 * @ClassName:SimpleTestDemo
 * @Description:(一些常规的测试)
 * @Email:gavin@hyx.com
 * @date 2017/1/19 14:33
 */
public class SimpleTestDemo extends MockMvcBaseTest {

    @Autowired
    private LoginService loginService;


    @Test
    public void test() throws Exception{
        LoginResponse user = loginService.login("harvey", "123456", "123456");
        System.out.println("Service return ==> \n" + JsonUtils.toJSON(user));
    }
}
