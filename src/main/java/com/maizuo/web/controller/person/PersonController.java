package com.maizuo.web.controller.person;

import com.google.protobuf.InvalidProtocolBufferException;
import com.maizuo.api3.commons.domain.Result;
import com.maizuo.constants.RouteConstants;
import com.maizuo.data.response.Person;
import com.maizuo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author harvey
 * @ClassName PersonController
 * @Email harvey@maizuo.com
 * @create 2017/2/6 15:28
 * @Description: person 协议控制器
 */
@Controller
@RequestMapping(value = RouteConstants.PERSON_ROUTH)
public class PersonController {
    /**
     * protobuf 模式
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/proto",method = RequestMethod.GET)
    public Result getPerson() {
        Person person = null;
        try {
            person = personService.getPerson();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return new Result(500, "服务异常");
        }
        return new Result(0, person, "success");
    }
    @Autowired
    private PersonService personService;

    /**
     * 普通json模式，用于和proto模式进行对比
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/json",method = RequestMethod.GET)
    public Result getJsonPerson() {
        Person person = null;
        person = personService.getJsonPerson();
        return new Result(0, person, "success");
    }
}
