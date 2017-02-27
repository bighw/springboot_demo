package com.maizuo.testdemo;

import com.maizuo.common.MockMvcBaseTest;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author gavin
 * @ClassName:TestForm
 * @Description:()
 * @Email:gavin@hyx.com
 * @date 2017/1/21 23:00
 */
public class TestForm extends MockMvcBaseTest {

    /**
     * 示例: 测试Post Json
     * @throws Exception
     */
    @Test
    public void  testPostJson() throws Exception{
        JSONObject json = new JSONObject();

        json.put("name","gavin");
        json.put("password","gavin520");
        json.put("sex",(byte)1);
        json.put("nickName","陌路人丁");
        json.put("type",1);

        String result = this.postByJosn("/api/user/login/v3",json);
        System.out.println("fanhuile: " + result);

    }


    /**
     * 示例: 测试用实体Bean接收 Post 表单提交
     * @throws Exception
     */
    @Test
    public void  testPostQuery() throws Exception{
        MultiValueMap<String, String> params =  new LinkedMultiValueMap<String, String>();
        params.add("name","gavin");
        params.add("password","ss20");
        params.add("nickName", "XX");
        String result = this.postByQuery("/api/user/login/v2",params);
        System.out.println("fanhuile: " + result);

    }
}
