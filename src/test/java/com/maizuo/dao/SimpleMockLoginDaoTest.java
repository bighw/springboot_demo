package com.maizuo.dao;

import com.maizuo.api3.commons.util.JsonUtils;
import com.maizuo.common.SimpleBaseTest;
import com.maizuo.data.entity.user.User;
import com.maizuo.data.response.LoginResponse;
import com.maizuo.service.LoginService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.BDDMockito.given;

/**
 * @author gavin
 * @ClassName:SimpleMockServiceTest
 * @Description:(示例: 使用@MockBean需要模拟Dao对数据打桩)
 * @Email:gavin@hyx.com
 * @date 2017/1/18 16:43
 */
public class SimpleMockLoginDaoTest extends SimpleBaseTest {

    //声明需要模拟的Dao
    @MockBean
    private LoginDao loginDao;

    @Autowired
    private LoginService loginService;

    @Before
    public void setUp() {
        User user2 = new User();
        user2.setName("gavin");
        user2.setPassword("123");
        user2.setId(2);
        user2.setSex((byte) 1);
        user2.setNickName("陌路人丁");
        //模拟Dao的方法入参及返参,对数据打桩
        given(this.loginDao.login("gavin", "123"))
                .willReturn(user2);
    }

    @Test
    public void test() throws Exception{
        LoginResponse user = loginService.login("gavin","123","123456");
        System.out.println("return User Object ==> \n" + JsonUtils.toJSON(user));
    }
}
