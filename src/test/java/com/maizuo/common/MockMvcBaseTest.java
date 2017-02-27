package com.maizuo.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author gavin
 * @ClassName:MockMvcBaseTest
 * @Description:(使用MockMvc测试接口路由的基类)
 * @Email:gavin@hyx.com
 * @date 2017/1/18 11:55
 *
 * 该类会加载 application-local下的配置
 * 并执行初始化容器
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
public class MockMvcBaseTest {
    @Autowired
    private WebApplicationContext context;

    public MockMvc mvc;

    @Before
    public void setUp() {
        //初始化 MockMvc 对象
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    /**
     * 测试普通GET请求
     * @param url
     * @return String类型的返回参数
     * @throws Exception
     */
    public String getByUrl(String url) throws Exception {
        if(StringUtils.isBlank(url)){
            return "";
        }
        MvcResult resBody = this.mvc.perform(
                get(url))
                .andExpect(status().isOk())
                .andReturn();
        return resBody.getResponse().getContentAsString();
    }

    /**
     * 测试POST_Json请求
     * @param url
     * @param json
     * @return
     * @throws Exception
     */
    public String postByJosn(String url, JSONObject json) throws Exception{
        if(StringUtils.isBlank(url) || json.length() == 0){
            return "";
        }
        MvcResult resBody = this.mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json.toString().getBytes())).andReturn();
        return resBody.getResponse().getContentAsString();
    }

    /**
     * 测试POST_Bean请求
     * @param url
     * @param user
     * @return
     * @throws Exception
     */
    public String postByBean(String url, Object user) throws Exception{
        if(StringUtils.isBlank(url) || null == user){
            return "";
        }
        ObjectMapper mapper = new ObjectMapper();
        MvcResult resBody = this.mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(mapper.writeValueAsBytes(user)))
                .andReturn();
        return resBody.getResponse().getContentAsString();
    }

    /**
     * 测试普通表单POST请求
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public String postByQuery(String url, MultiValueMap<String, String> params) throws Exception{
        if(params.size() == 0){
            return "";
        }
        MvcResult resBody = this.mvc.perform(post(url).params(params)).andReturn();
        return resBody.getResponse().getContentAsString();
    }
}
