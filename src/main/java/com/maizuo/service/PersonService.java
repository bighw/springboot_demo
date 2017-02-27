package com.maizuo.service;

import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import com.maizuo.constants.RedisConstants;
import com.maizuo.data.entity.person.PersonProto;
import com.maizuo.data.response.Person;
import com.maizuo.redis.BaseRedis;
import com.maizuo.redis.ByteBaseRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author harvey
 * @ClassName PersonService
 * @Email harvey@maizuo.com
 * @create 2017/2/6 15:11
 * @Description: 用来处理和person 相关的业务
 */
@Service
public class PersonService {
    @Autowired
    ByteBaseRedis byteBaseRedis;

    @Autowired
    BaseRedis baseRedis;

    Gson gson = new Gson();
    /**
     * 将给定的person（protobuf生成的）对象，序列化，并写入redis中
     *
     * @param person
     */
    public boolean initPerson(PersonProto.Person person) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        person.writeTo(outputStream);
        return byteBaseRedis.set(RedisConstants.REDIS_PROTOBUF_PERSON, outputStream.toByteArray());
    }

    /**
     * 该方法用来读取缓存里面的person数据，并且反序列化返回
     * @return
     * @throws InvalidProtocolBufferException
     */
    public Person getPerson() throws InvalidProtocolBufferException {
        byte[] value = byteBaseRedis.get(RedisConstants.REDIS_PROTOBUF_PERSON);
        PersonProto.Person personSrc = PersonProto.Person.parseFrom(value);
        Person person = new Person();
        person.setAccount(personSrc.getAccount());
        person.setSalary(personSrc.getSalary());
        person.setPhone(personSrc.getPhone());
        person.setNickName(personSrc.getNickName());
        person.setId(personSrc.getId());
        person.setEmail(personSrc.getEmail());
        person.setCountry(personSrc.getCountry());
        person.setAddress(personSrc.getAddress());
        person.setAge(personSrc.getAge());
        person.setName(personSrc.getName());
        return person;
    }

    /**
     * 将给定的person（正常的）对象，序列化，并写入redis中
     * 用于和protobuf 对比
     * @param person
     */
    public boolean initPerson(Person person) {
        Gson gson = new Gson();
        String src = gson.toJson(person);
        return baseRedis.set(RedisConstants.REDIS_JSON_PERSON,src);
    }

    /**
     * 普通方式，用来和protobuf作对比用的
     * @return
     */
    public Person getJsonPerson() {
        String src = baseRedis.get(RedisConstants.REDIS_JSON_PERSON);
        return gson.fromJson(src, Person.class);
    }
}
