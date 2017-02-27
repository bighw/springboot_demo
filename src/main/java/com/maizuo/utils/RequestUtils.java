package com.maizuo.utils;

import com.maizuo.api3.commons.util.LogUtils;
import com.maizuo.api3.commons.util.StringUtils;
import com.maizuo.api3.commons.util.date.DateStyle;
import com.maizuo.api3.commons.util.date.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author qiyang
 * @ClassName: RequestUtils
 * @Description: 请求工具类
 * @Email qiyang@maizuo.com
 * @date 2016/8/15 0015
 */
public class RequestUtils {
    /**
     * 获取客户端IP(直连情况，中间没有类似nginx负载均衡代理)
     *
     * @param request
     * @return
     */
    public static String getReqIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.trim().length() > 15) {
            ip = ip.trim().split(",")[0];
        }
        return ip;
    }

    /**
     * 获取客户端登录IP(有类似nginx负载均衡代理的情况)
     *
     * @param request
     * @return
     */
    public static String getNewIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.trim().length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        LogUtils.info("获取到的ip的完整内容是: " + ip);
        if (ip != null) {
            String[] ips = ip.trim().split(",");
            //获取倒数第二个ip
            if (ips.length >= 2) {
                ip = ips[ips.length - 2];
            } else {
                ip = ips[0];
            }
        }
        return ip.trim();
    }


    /**
     * 生成唯一标识
     *
     * @return
     */
    public static String genRequestId() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(DateUtils.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS)).append(":")
                .append(Util.genRandomNum(10)).append("]");
        return sb.toString();
    }

    /**
     * 初始化请求唯一标识
     */
    public static void initRequestId() {
        Thread.currentThread().setName(genRequestId());
    }

    /**
     * 获取请求唯一标识
     *
     * @return
     */
    public static String getRequestId() {
        String requestId = Thread.currentThread().getName();
        if (StringUtils.isEmpty(requestId)) {
            requestId = genRequestId();
        }
        return requestId;
    }
}
