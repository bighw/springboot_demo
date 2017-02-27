package com.maizuo.web.controller;

import com.maizuo.api3.commons.domain.Result;
import com.maizuo.data.enums.ErrorCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiyang
 * @ClassName: ErrorController
 * @Description: 错误请求控制器
 * @Email qiyang@maizuo.com
 * @date 2016/8/17 0017
 */
@RestController
public class ErrorController {
    @RequestMapping("/404")
    public Result go404() {
        return new Result(ErrorCode.UNDEFINED.getCode(), "", ErrorCode.UNDEFINED.getMsg());
    }
}
