package com.maizuo.redis;

import com.google.protobuf.InvalidProtocolBufferException;
import com.maizuo.common.SimpleBaseTest;
import com.maizuo.data.entity.person.PersonProto;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author harvey
 * @ClassName ByteBaseRedisTest
 * @Email harvey@maizuo.com
 * @create 2017/2/5 17:27
 * @Description:
 */
@ActiveProfiles("local")
public class ByteBaseRedisTest extends SimpleBaseTest {
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
    private final static int n = 90000;

    @Autowired
    ByteBaseRedis byteBaseRedis;


    @Test
    public void setTest() {
        String key = "protobuf:test";
        PersonProto.Person person = PersonProto.Person.newBuilder().setAge(26).setName("harvey").setSalary(100000).build();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            person.writeTo(outputStream);
            System.out.println(Arrays.toString(outputStream.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(byteBaseRedis.set(key, outputStream.toByteArray()));
    }

    @Test
    public void getTest() {
        String key = "protobuf:test";
        byte[] value = byteBaseRedis.get(key);
        try {
            PersonProto.Person person1 = PersonProto.Person.parseFrom(value);
            System.out.println(person1.getName());
            Assert.assertNotNull(person1.getName());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于测试json 序列化与反序列化
     */
//    @Test
//    public void jsonTest() {
//        Person person = new Person();
//        person.setName(name);
//        person.setAge(age);
//        person.setAccount(account);
//        person.setAddress(address);
//        person.setCountry(country);
//        person.setEmail(email);
//        person.setId(id);
//        person.setNickName(nickName);
//        person.setPhone(phone);
//        person.setSalary(salary);
//        Gson gson = new Gson();
//        long start = System.currentTimeMillis();
//        for (int i = 0; i <= n; i++) {
//            String src = gson.toJson(person);
//            Person person1 = gson.fromJson(src, Person.class);
//        }
//        System.out.println("共花费时间：" + (System.currentTimeMillis() - start) + "ms");
//    }
//
//    /**
//     * 用于测试protobuf 的序列化与反序列化的能力
//     */
//    @Test
//    public void protoTest() {
//        PersonProto.Person person = PersonProto.Person.newBuilder()
//                .setAge(age)
//                .setName(name)
//                .setSalary(salary)
//                .setPhone(phone)
//                .setNickName(nickName)
//                .setId(id)
//                .setEmail(email)
//                .setCountry(country)
//                .setAddress(address)
//                .setAccount(account)
//                .build();
//        long start = System.currentTimeMillis();
//        for (int i = 0; i <= n; i++) {
//            try {
//                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                person.writeTo(outputStream);
//                PersonProto.Person person1 = PersonProto.Person.parseFrom(outputStream.toByteArray());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("共花费时间：" + (System.currentTimeMillis() - start) + "ms");
//    }
}
