package com.maizuo.web.controller;

import com.hyx.zookeeper.MaizuoLogUtil;
import com.maizuo.api3.commons.domain.HttpResult;
import com.maizuo.api3.commons.util.HttpUtils;
import com.maizuo.api3.commons.util.JsonUtils;
import com.maizuo.api3.commons.util.LogUtils;
import com.maizuo.api3.commons.util.StringUtils;
import com.maizuo.api3.commons.util.date.DateStyle;
import com.maizuo.api3.commons.util.date.DateUtils;
import com.maizuo.api3.moviebase.client.MovieClient;
import com.maizuo.api3.moviebase.protocol.LongListMv;
import com.maizuo.constants.RouteConstants;
import com.maizuo.data.entity.user.User;
import com.maizuo.data.enums.ErrorCode;
import com.maizuo.api3.commons.domain.Result;
import com.maizuo.api3.commons.exception.MaizuoException;
import com.maizuo.mocktool.MZMock;
import com.maizuo.redis.handler.TemplateRedis;
import com.maizuo.service.TemplateService;
import com.maizuo.tools.TimeLog;
import com.maizuo.constants.Constants;
import com.maizuo.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Set;

/**
 * @author qiyang
 * @ClassName: TemplateController
 * @Description: 控制器模板
 * @Email qiyang@maizuo.com
 * @date 2016/8/17 0017
 */
@RestController
@RequestMapping(value =  RouteConstants.TEST_ROUTH)
public class TemplateController {
    @Autowired
    TemplateService templateService;
    @Autowired
    TemplateRedis templateRedis;
    @Autowired
    MovieClient movieClient;

    /**
     * mock注解示例
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @MZMock("mock_hello")
    public Result hello(@RequestParam String data) {
        String loghead = RequestUtils.getRequestId() + "hello测试:";
        String logInterface = "test_hello";
        TimeLog timeLog = new TimeLog();
        LogUtils.info(loghead + "postbody:" + data);
        if (false) {
            LogUtils.info(loghead + "response:" + "无效请求");
            MaizuoLogUtil.writeLog(Constants.SYSTEMID, Constants.SYSTEMID, data, logInterface, "1", "", timeLog.totalTime(), "1");
            return new Result(ErrorCode.INVALID_REQUEST.getCode(), "", ErrorCode.INVALID_REQUEST.getMsg());
        }
        MaizuoLogUtil.writeLog(Constants.SYSTEMID, Constants.SYSTEMID, data, logInterface, "0", "", timeLog.totalTime(), "0");
        return new Result(ErrorCode.SUCCESS.getCode(), data, ErrorCode.SUCCESS.getMsg());
    }

    /**
     * 常用操作使用示例
     *
     * @return
     */
    @RequestMapping("/test")
    public Result test() {
        System.out.println(Constants.SYSTEMID);
        System.out.println(Constants.SYSTEMNAME);
        //数据库查询
//        List<String> list = templateService.queryLast10User();
//        System.out.println(JsonUtils.toJSON(list));
       User user = null;
        try {
            user = templateService.queryUserById(10);
        } catch (MaizuoException e) {
            e.printStackTrace();
        }
        System.out.println(JsonUtils.toJSON(user));

        //缓存查询
        String cacheData = templateRedis.testGet("test_test");
        System.out.println(cacheData);

        //字符串非空引用和非空字符串校验
        String s = null;
        if (StringUtils.isEmpty(s)) {
            System.out.println("s is empty");
        }

        //集合非空校验
        Set<String> set = null;
        if (CollectionUtils.isEmpty(set)) {
            System.out.println("set is a empty collection");
        }

        //时间转换
        String datatime = "2016-08-17 19:09:21";
        Date date = DateUtils.StringToDate(datatime, DateStyle.YYYY_MM_DD_HH_MM_SS);
        System.out.println(date);
        long l_time = DateUtils.DateToMilli(date);
        System.out.println(l_time);

        LongListMv listMv = movieClient.queryCinemaIdsByCity(10);
        System.out.println("[moviebase]cityfilms:" + listMv.data);

        return new Result(ErrorCode.SUCCESS.getCode(), user, ErrorCode.SUCCESS.getMsg());
    }

    @RequestMapping("/halls/{cinemaId}")
    public Result httpTest(@PathVariable String cinemaId) {
        HttpResult httpResult = HttpUtils.doGet("http://bcnew.maizuo.com/api/cinema/halls?cinemaId=" + cinemaId);
        return JsonUtils.jsonToBean(httpResult.getBody(), Result.class);
    }

    @RequestMapping(value = "/testPostJson", method = RequestMethod.POST)
    public Result httpPostJson(@RequestBody User user){
        return new Result(ErrorCode.SUCCESS.getCode(), user, ErrorCode.SUCCESS.getMsg());
    }

    @RequestMapping(value = "/testPost", method = RequestMethod.POST)
    public Result httpPost(@RequestParam String name, @RequestParam String password){
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        return new Result(ErrorCode.SUCCESS.getCode(), user, ErrorCode.SUCCESS.getMsg());
    }
}
