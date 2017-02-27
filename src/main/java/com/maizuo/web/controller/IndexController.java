package com.maizuo.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rose
 * @ClassName: IndexController
 * @Email rose@maizuo.com
 * @create 2017/1/11-17:27
 * @Description: 首页欢迎语
 */
@RestController
public class IndexController {


    /**
     *
     * @return
     */

    @RequestMapping(value = "/")
    public String index() {
        return "Welcome to home";
    }
}
