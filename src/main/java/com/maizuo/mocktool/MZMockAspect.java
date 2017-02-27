package com.maizuo.mocktool;

import com.maizuo.api3.commons.util.LogUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @author qiyang
 * @ClassName: MZMockAspect
 * @Description: 注解AOP
 * @Email qiyang@maizuo.com
 * @date 2016/8/15 0015
 */
@Component
@Aspect
public class MZMockAspect {

    @Pointcut("@annotation(com.maizuo.mocktool.MZMock)")
    public void execute() {
    }

    @Before("execute() && @annotation(mzMock)")
    public void doBefore(MZMock mzMock) {
        LogUtils.debug("return mock data...");
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            // springmvc
//            PrintWriter out = response.getWriter();
            // springboot
            PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"), true);
            try {
                out.print(MZMockParser.parse(mzMock));
            } finally {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
