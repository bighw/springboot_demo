package com.maizuo.data.enums;

import org.apache.log4j.Level;

/**
 * @author qiyang
 * @ClassName: LogLevelEnum
 * @Description: 日志级别
 * @Email qiyang@maizuo.com
 * @date 2016/8/1 0001
 */
public enum LogLevelEnum {
    ALL(Level.ALL),
    DEBUG(Level.DEBUG),
    INFO(Level.INFO),
    WARN(Level.WARN),
    ERROR(Level.ERROR),
    FATAL(Level.FATAL),
    OFF(Level.OFF);

    Level level;

    LogLevelEnum(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    public static Level getLevel(String name) {
        try {
            Level level = LogLevelEnum.valueOf(name.toUpperCase()).getLevel();
            return level;
        } catch (Exception e) {
            return LogLevelEnum.INFO.getLevel();
        }
    }
}
