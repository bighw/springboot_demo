package com.maizuo.web.controller;

import com.maizuo.common.MockMvcBaseTest;
import com.maizuo.data.entity.user.User;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author gavin
 * @ClassName:SimpleTest
 * @Description:(示例：MockMvc测试rest接口)
 * @Email:gavin@hyx.com
 * @date 2017/1/17 14:42
 */
public class SimpleMockMvcTest extends MockMvcBaseTest {


    /**
     * 测试GET接口示例
     * @throws Exception
     */
    @Test
    public void getByUrl() throws Exception {
        String result = this.getByUrl("/api/user/login?userName=harvey&password=123456");
//        String result = this.getByUrl("/test");
//        String result = this.getRequestByUrl("/halls/496");
        System.out.println("GET return ==> \n" + result);
    }

    /**
     * 测试表单POST接口示例
     * @throws Exception
     */
    @Test
    public void postByQuery() throws Exception {
        MultiValueMap<String, String> params =  new LinkedMultiValueMap<String, String>();
        params.add("name", "gavin");
        params.add("password", "gavin520");
        String result = this.postByQuery("/api/test/testPost", params);
        System.out.println("POST return ==> \n" + result);
    }

    /**
     * 测试POST json接口示例
     * @throws Exception
     */
    @Test
    public void postByJson() throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", 12);
        json.put("name", "gavin");
        json.put("password", "gavin520");
        String result = this.postByJosn("/api/test/testPostJson", json);
        System.out.println("POST JSON return ==> \n" + result);
    }

    /**
     * 测试POST Bean接口示例[其实也是POST JSON的一种格式]
     * @throws Exception
     */
    @Test
    public void postByBean() throws Exception {
        User user = new User();
        user.setName("gavin");
        user.setPassword("gavin520");
        String result = this.postByBean("/api/test/testPostJson", user);
        System.out.println("POST Bean return ==> \n" + result);
    }
}
