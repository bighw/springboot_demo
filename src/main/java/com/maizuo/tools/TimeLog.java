package com.maizuo.tools;

/**
 * @author qiyang
 * @ClassName: TimeLog
 * @Description: 计时器工具类
 * @Email qiyang@maizuo.com
 * @date 2016/8/17 0017
 */
public class TimeLog {
    private long startTime;

    public TimeLog() {
        startTime = System.currentTimeMillis();
    }

    public String totalTime() {
        return String.valueOf(System.currentTimeMillis() - startTime);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }


}
