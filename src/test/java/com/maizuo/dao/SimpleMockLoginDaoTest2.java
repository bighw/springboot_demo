package com.maizuo.dao;

import com.maizuo.api3.commons.util.JsonUtils;
import com.maizuo.common.SimpleBaseTest;
import com.maizuo.data.entity.user.User;
import com.maizuo.service.TemplateService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author gavin
 * @ClassName:SimpleMockLoginServiceTest2
 * @Description:(示例: 测试Dao的第二种方式)
 * @Email:gavin@hyx.com
 * @date 2017/1/19 12:02
 * 本示例中@Mock @Spy 都是用于注入 @InjectMocks 中的
 */
public class SimpleMockLoginDaoTest2 extends SimpleBaseTest {
    //创建一个实例
    @InjectMocks
    private TemplateService templateService;

    //创建一个Mock
    //使用@Mock生成的类，所有方法都不是真实的方法，而且返回值都是NULL，需要做Mock打桩
    @Mock
    private TemplateDao templateDao;

    @Before
    public void setUp(){

        //初始化,将@Mock、@Spy等对象注入 @InjectMocks对象中
        MockitoAnnotations.initMocks(this);

        //这个是不会走的，因为下面的代码块包含了这个代码块
        User user1 = new User();
        user1.setName("harvey");
        user1.setPassword("123456");
        user1.setId(1);
        user1.setSex((byte) 0);
        user1.setNickName("黄老司机");
        //如果Service里面调用多个Dao或者Redis等都需要逐一的做Mock打桩
        //及需逐一的配上when(Dao/Redis被调用的函数).thenReturn(期望值)
        when(templateDao.queryUserById(1)).thenReturn(user1);

        User user2 = new User();
        user2.setName("gavin");
        user2.setPassword("123");
        user2.setId(2);
        user2.setSex((byte) 1);
        user2.setNickName("陌路人丁");
        when(templateDao.queryUserById(any(Integer.class))).thenReturn(user2);
    }

    @Test
    public void test() throws Exception{
        User user = templateService.queryUserById(1);
        System.out.println("Service return ==> \n" + JsonUtils.toJSON(user));

    }
}
