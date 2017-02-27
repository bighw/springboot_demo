package com.maizuo.config;

import com.maizuo.constants.RouteConstants;
import com.maizuo.web.interceptor.IPInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author qiyang
 * @ClassName: WebMvcConfig
 * @Description: WEBMVC配置
 * @Email qiyang@maizuo.com
 * @date 2016/8/25 0025
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加IP拦截器以及对应的拦截路由地址
      // registry.addInterceptor(new IPInterceptor()).addPathPatterns(RouteConstants.USER_ROUTE+RouteConstants.ROUTE_SUFFIX);

    }

}
