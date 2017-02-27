package com.maizuo.task;

import com.maizuo.api3.commons.util.LogUtils;
import com.maizuo.data.entity.person.PersonProto;
import com.maizuo.data.response.Person;
import com.maizuo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author harvey
 * @ClassName InitPersonTask
 * @Email harvey@maizuo.com
 * @create 2017/2/6 15:08
 * @Description: person 对象的初始化
 */
@Component
@Configurable
@EnableScheduling
public class InitPersonTask {
    private final static String name = "ZhangSan";
    private final static int age = 25;
    private final static double salary = 100000.00;
    private final static String country = "CN";
    private final static String email = "zhangsan@hyx.com";
    private final static String phone = "12341234124";
    private final static String address = "广东省南山区";
    private final static int id = 1;
    private final static String nickName = "xiaosan";
    private final static long account = 333223423421234L;

    @Autowired
    private PersonService personService;

    /**
     * 每一个小时初始化一下person对象，并且将对象序列化后写入redis
     */
    @Scheduled(fixedRate = 60*60*1000)
    @Async
    public void initPerson() {
        // protobuf
        PersonProto.Person person = PersonProto.Person.newBuilder()
                .setAge(age)
                .setName(name)
                .setSalary(salary)
                .setPhone(phone)
                .setNickName(nickName)
                .setId(id)
                .setEmail(email)
                .setCountry(country)
                .setAddress(address)
                .setAccount(account)
                .build();
        try {
            if (!personService.initPerson(person)) {
                LogUtils.info("初始化person对象任务执行失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // json
        Person person1 = new Person();
        person1.setAge(age);
        person1.setName(name);
        person1.setSalary(salary);
        person1.setPhone(phone);
        person1.setNickName(nickName);
        person1.setId(id);
        person1.setEmail(email);
        person1.setCountry(country);
        person1.setAddress(address);
        person1.setAccount(account);
        if (!personService.initPerson(person1)) {
            LogUtils.info("初始化person对象任务执行失败");
        }
    }
}
