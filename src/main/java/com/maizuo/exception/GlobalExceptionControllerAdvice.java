package com.maizuo.exception;

import com.maizuo.api3.commons.util.JsonUtils;
import com.maizuo.api3.commons.util.LogUtils;
import com.maizuo.api3.commons.util.MaizuoLogUtils;
import com.maizuo.data.enums.ErrorCode;
import com.maizuo.constants.Constants;
import com.maizuo.api3.commons.domain.Result;
import com.maizuo.api3.commons.exception.MaizuoException;
import com.maizuo.utils.Util;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qiyang
 * @ClassName: GlobalExceptionControllerAdvice
 * @Description: 全局异常处理
 * @Email qiyang@maizuo.com
 * @date 2016/8/17 0017
 */
@ControllerAdvice
public class GlobalExceptionControllerAdvice {

    @ExceptionHandler
    public void resolve500(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {

        if (exception != null) {
            exception.printStackTrace();
        }

        //客户端中断异常不处理
        if (exception instanceof ClientAbortException) {
            return;

        } else {
            //非客户端中断异常的处理，记录现场到日志系统
            Map<String, String> map = new HashMap<>();
            map.put("url", "" + request.getRequestURL());
            map.put("exception_msg", "" + exception.toString());
            LogUtils.error("exception==url==" + request.getRequestURL());
            Util.logExceptionStack(exception);
            MaizuoLogUtils.writeLog(Constants.SYSTEMID, Constants.SYSTEMID, "",
                    Constants.SYSTEMNAME+":global:exception", JsonUtils.toJSON(map), "包场中心异常","1","0", "1");
        }
        Result rs = null;
        //自定义业务异常的处理，直接返回状态码和文案
        if(exception instanceof MaizuoException){
            MaizuoException e = (MaizuoException)exception;
            rs =  new Result(e.getStatus(), e.getMsg());
        } else if(exception instanceof BindException){
            //表单绑定对象失败，参数格式错误
            rs =  new Result(ErrorCode.ILLEGAL_PARAM.getCode(), "参数缺少或参数格式错误");
        }  else if(exception instanceof MethodArgumentTypeMismatchException){
            //单个参数格式错误
            rs =  new Result(ErrorCode.ILLEGAL_PARAM.getCode(), "参数缺少或参数格式错误");
        }else{
            //其他未业务系统未捕获的异常统一返回异常状态码500
            rs =  new Result(ErrorCode.EXCEPTION.getCode(), ErrorCode.EXCEPTION.getMsg());
        }

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        //响应流回写异常码和文案
        PrintWriter pw = response.getWriter();
        pw.print(JsonUtils.toJSON(rs));
        return;
    }
}
