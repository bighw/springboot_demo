package com.maizuo.service;

import com.maizuo.common.MockMvcBaseTest;
import com.maizuo.data.entity.user.User;
import com.maizuo.data.response.LoginResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.BDDMockito.given;

/**
 * @author gavin
 * @ClassName:MockServiceTest
 * @Description:(示例: 使用@MockBean需要模拟Service对数据打桩)
 * @Email:gavin@hyx.com
 * @date 2017/1/18 12:34
 */
public class SimpleMockLoginServiceTest extends MockMvcBaseTest {

    //使用Mock的Service对象
    @MockBean
    private LoginService loginService;

    @Before
    public void setup() throws Exception {

        LoginResponse user1 = new LoginResponse();
        user1.setName("harvey");
        user1.setSex((byte) 0);
        user1.setNickName("黄老司机");

        //模拟Service对应的方法入参及返参,对数据打桩
        given(this.loginService.login("harvey", "123456", "123456"))

                .willReturn(user1);

        LoginResponse user2 = new LoginResponse();
        user2.setName("gavin");
        user2.setSex((byte) 1);
        user2.setNickName("陌路人丁");
        given(this.loginService.login("gavin", "123", "123456"))
                .willReturn(user2);
    }

    @Test
    public void test() throws Exception{
        String result = this.getByUrl("/api/user/login?userName=harvey&password=123456");
        System.out.println("GET return ==> \n" + result);
    }

}
