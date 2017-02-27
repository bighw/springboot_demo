package com.maizuo.web.controller;

import com.hyx.nsqpool.NsqClientProxy;
import com.maizuo.constants.MQConstants;
import com.maizuo.api3.commons.domain.Result;
import com.maizuo.constants.RouteConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by harvey on 2017/1/13.
 */
@Controller
@RequestMapping(value =  RouteConstants.PUSH_MESSAGE_ROUTH)
public class NsqPushController {
    @RequestMapping(value = "/{message}",method = RequestMethod.GET)
    @ResponseBody
    public Result pushMessage (@PathVariable String message) {
        if (StringUtils.isBlank(message)) {
            return new Result(1001, "", "推送信息为空");
        }
        NsqClientProxy.publishMessage(MQConstants.PUSH_TEST_TOPIC,message);
        return new Result(0, "", "推送成功");
    }
}
