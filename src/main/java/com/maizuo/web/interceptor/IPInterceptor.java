package com.maizuo.web.interceptor;

import com.hyx.zookeeper.MaizuoLogUtil;
import com.maizuo.api3.commons.util.JsonUtils;
import com.maizuo.api3.commons.util.LogUtils;
import com.maizuo.api3.commons.domain.Result;
import com.maizuo.data.enums.ErrorCode;
import com.maizuo.constants.Constants;
import com.maizuo.config.MZConfig;
import com.maizuo.utils.RequestUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qiyang
 * @ClassName: IPInterceptor
 * @Description: IP拦截器
 * @Email qiyang@maizuo.com
 * @date 2016/8/25 0025
 */
public class IPInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //请求唯一标识设置
        RequestUtils.initRequestId();

        LogUtils.info(request.getRequestURI());
        //从配置系统获取内部IP白名单
        String allowIPList = MZConfig.getString("MAIZUO_INNER_IPS");
        //获取客户端真实ip
        String ip = RequestUtils.getReqIp(request);
        //检查请求IP是否在配置中
        for (String allowIP : allowIPList.split(",")) {
            if (allowIP.trim().equals(ip)) {
                return true;
            }
        }
        //非法IP记录现场到日志系统，记录URL和请求IP
        Map<String, String> map = new HashMap<>();
        map.put("url", "" + request.getRequestURL());
        map.put("ip", "" + ip);

        MaizuoLogUtil.writeLog(Constants.SYSTEMID, Constants.SYSTEMID, JsonUtils.toJSON(map), "bcnew:ip_exception", "1", "非法IP请求", 0, 1);

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter pw = response.getWriter();
        Result rs = new Result(ErrorCode.ILLEGAL_IP.getCode(), "", ErrorCode.ILLEGAL_IP.getMsg());
        pw.print(JsonUtils.toJSON(rs));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

