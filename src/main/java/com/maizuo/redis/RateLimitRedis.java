package com.maizuo.redis;

import com.maizuo.data.enums.ErrorCode;
import com.maizuo.api3.commons.exception.MaizuoException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author harvey
 * @ClassName RateLimitRedis
 * @Email harvey@maizuo.com
 * @create 2017/1/19-10:37
 * @Description: 用于操作频率限制的相关业务
 * 主要包含检查是否受限(check)，增加对象的操作次数(incr)，和删除key的功能(clear)
 * 包含三类场景:
 * 1. 固定时间段(fixTime),比如：每5分钟
 * 2. 连续时间段(seriallyTime),比如：在任意连续的5分钟内
 * 3. 时间类型(timeType),比如：在一天内，或者在一个小时内，或者在一分钟内
 */
@Component
public class RateLimitRedis {
    public static final int DAY_TYPE = 1;
    public static final int HOUR_TYPE = 2;
    public static final int MINUTE_TYPE = 3;
    private static final String START = "start";
    private static final String COUNT = "count";
    @Autowired
    private BaseRedis baseRedis;

    /**
     * 在指定的时间段，和操作次数来进行频率限制
     * 判断对象是否可以在设定的条件内继续操作
     *
     * @param key          操作对象的唯一标识
     * @param limiTimes    设定的限制次数
     * @param limitSeconds 设定的限制时间，单位为秒(s)
     * @return true: 在限制条件之内可以继续操作；false: 在限制条件之内无法继续操作
     */
    public boolean checkRateByFixTime(String key, int limiTimes, int limitSeconds) throws MaizuoException {
        if (StringUtils.isBlank(key)) {
            throw new MaizuoException(ErrorCode.ILLEGAL_PARAM.getCode(), ErrorCode.ILLEGAL_PARAM.getMsg());
        }
        // 不存在这个key
        if (!baseRedis.exists(key)) {
            return true;
        }
        String startTime = baseRedis.hget(key, START);
        if (StringUtils.isBlank(startTime) || !StringUtils.isNumeric(startTime)) {
            throw new MaizuoException(ErrorCode.REDIS_RETURN_ERROR.getCode(), ErrorCode.REDIS_RETURN_ERROR.getMsg());
        }
        // 当前时间，与这个时间段内的开始时间的差如果大于限制时间，则说明该限制已经过时
        if ((System.currentTimeMillis() - Long.parseLong(startTime)) > (limitSeconds * 1000)) {
            return true;
        } else {
            String count = baseRedis.hget(key, COUNT);
            if (StringUtils.isBlank(count) || !StringUtils.isNumeric(count)) {
                throw new MaizuoException(ErrorCode.REDIS_RETURN_ERROR.getCode(), ErrorCode.REDIS_RETURN_ERROR.getMsg());
            }
            /* 如果count的值和limitTimes想等，就证明对象已经操作了limitTimes次，已经达到了限制的数量，所以不可以加等号 */
            return limiTimes > Integer.parseInt(count);
        }
    }

    /**
     * 在指定的时间段，和操作次数来进行频率限制
     * 增加对象的操作次数
     *
     * @param key          操作对象的唯一标识
     * @param limitSeconds 设定的限制时间，单位为秒(s)
     * @return true: 增加成功；false: 增加失败
     */
    public boolean rateIncrByFixTime(String key, int limitSeconds) throws MaizuoException {
        long nowTime = System.currentTimeMillis();
        if (StringUtils.isBlank(key)) {
            throw new MaizuoException(ErrorCode.ILLEGAL_PARAM.getCode(), ErrorCode.ILLEGAL_PARAM.getMsg());
        }
        // 不存在这个key
        if (!baseRedis.exists(key)) {
            return (baseRedis.hset(key, START, nowTime + "") && baseRedis.hincrby(key, COUNT, 1) > 0 && baseRedis.expire(key, limitSeconds));
        }
        String startTime = baseRedis.hget(key, START);
        if (StringUtils.isBlank(startTime) || !StringUtils.isNumeric(startTime)) {
            throw new MaizuoException(ErrorCode.REDIS_RETURN_ERROR.getCode(), ErrorCode.REDIS_RETURN_ERROR.getMsg());
        }
        // 当前时间，与这个时间段内的开始时间的差如果大于限制时间，则说明该限制已经过时
        if ((nowTime - Long.parseLong(startTime)) > (limitSeconds * 1000)) {
            // 如果该限制已经过时，则删除原来的key，设置初始时间，操作次数增1，以及设置key的失效时间
            return (baseRedis.del(key) && baseRedis.hset(key, START, nowTime + "") && baseRedis.hincrby(key, COUNT, 1) > 0 && baseRedis.expire(key, limitSeconds));
        } else {
            // 操作次数增1，并且设置缓存key的失效时间
            return baseRedis.hincrby(key, COUNT, 1) > 0 && baseRedis.expire(key, limitSeconds);
        }
    }

    /**
     * 在给定连续的时间段，和给定的操作次数来进行频率限制
     * 比如：当对象操作的次数超过了限制的次数，最新的一次请求将会把第一次操作挤掉，第二次操作的时间成为比对的基准
     * 判断对象是否可以在设定的条件内继续操作
     *
     * @param key          操作对象的唯一标识
     * @param limitTimes   设定的限制次数
     * @param limitSeconds 设定的限制时间，单位为秒(s)
     * @return true: 在限制条件之内可以继续操作；false: 在限制条件之内无法继续操作
     */
    public boolean checkRateBySeriallyTime(String key, int limitTimes, int limitSeconds) throws MaizuoException {
        long nowTime = System.currentTimeMillis();
        if (StringUtils.isBlank(key)) {
            throw new MaizuoException(ErrorCode.ILLEGAL_PARAM.getCode(), ErrorCode.ILLEGAL_PARAM.getMsg());
        }
        // 不存在这个key
        if (!baseRedis.exists(key)) {
            return true;
        }
        int len = baseRedis.lLen(key);
        // 列表的数量代表操作的数量，操作数量小于限制数量，可以通过
        if (len < limitTimes) {
            return true;
        }
        // 取出队列最开始的操作时间，并删除
        String startTime = baseRedis.lPop(key);
        if (StringUtils.isBlank(startTime) || !StringUtils.isNumeric(startTime)) {
            throw new MaizuoException(ErrorCode.REDIS_RETURN_ERROR.getCode(), ErrorCode.REDIS_RETURN_ERROR.getMsg());
        }
        /*
        向队列的尾端添加当前时间，并且设置key的失效时间
        同时比较：当前时间与最早操作时间的差 和 设置的限制时间 的大小
        */
        return (baseRedis.rPush(key, nowTime + "") && baseRedis.expire(key, limitSeconds) && (nowTime - Long.parseLong(startTime)) > (limitSeconds * 1000));
    }

    /**
     * 在给定连续的时间段，和给定的操作次数来进行频率限制
     * 比如：当对象操作的次数超过了限制的次数，最新的一次请求将会把第一次操作挤掉，第二次操作的时间成为比对的基准
     * 增加对象的操作次数
     *
     * @param key       操作对象的唯一标识
     * @param limiTimes 设定的限制次数
     * @return true: 增加成功；false: 增加失败
     */
    public boolean rateIncrBySeriallyTime(String key, int limiTimes, int limitSeconds) throws MaizuoException {
        long nowTime = System.currentTimeMillis();
        if (StringUtils.isBlank(key)) {
            throw new MaizuoException(ErrorCode.ILLEGAL_PARAM.getCode(), ErrorCode.ILLEGAL_PARAM.getMsg());
        }
        // 不存在这个key
        if (!baseRedis.exists(key)) {
            // 把当前操作时间写入队列
            return baseRedis.rPush(key, nowTime + "") && baseRedis.expire(key, limitSeconds);
        }
        int len = baseRedis.lLen(key);
        // 操作次数小于限制的次数
        if (len < limiTimes) {
            // 把当前操作时间写入队列
            return baseRedis.rPush(key, nowTime + "") && baseRedis.expire(key, limitSeconds);
        }
        // 操作次数大于限制次数，弹出最早的操作时间，写入当前操作时间，并设置key的失效时间
        baseRedis.lPop(key);
        return baseRedis.rPush(key, nowTime + "") && baseRedis.expire(key, limitSeconds);
    }

    /**
     * 删除频率限制对象的缓存数据,一般用于限制对象正确操作之后
     *
     * @param key 操作对象的唯一标识
     * @return true: 删除成功 ; false：删除失败
     * @throws MaizuoException 卖座定义的异常
     */
    public boolean clearRate(String key) throws MaizuoException {
        if (StringUtils.isBlank(key)) {
            throw new MaizuoException(ErrorCode.ILLEGAL_PARAM.getCode(), ErrorCode.ILLEGAL_PARAM.getMsg());
        }
        return (!baseRedis.exists(key)) || baseRedis.del(key);
    }

    /**
     * 删除频率限制对象的缓存数据,一般用于限制对象正确操作之后
     *
     * @param key   key
     * @param field field
     * @return true: 删除成功 ; false：删除失败
     * @throws MaizuoException 卖座定义的异常
     */
    public boolean clearRate(String key, String field) throws MaizuoException {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) {
            throw new MaizuoException(ErrorCode.ILLEGAL_PARAM.getCode(), ErrorCode.ILLEGAL_PARAM.getMsg());
        }
        return (!baseRedis.exists(key)) || !baseRedis.hexists(key, field) || baseRedis.hdel(key, field);
    }

    /**
     * 查看指定的对象在约定的时间单位（如天，小时，分等），是否可以继续操作
     * 比如：限制单个ip一天内的请求次数
     *
     * @param type       1: 天 ； 2：小时；3：分钟
     * @param key        业务类型，例如如果是IP类可以传IP
     * @param field      操作对象的唯一标识
     * @param limitTimes 限制次数
     * @return true : 可以继续操作；false : 不可以继续操作
     */
    public boolean checkRateByTimeType(int type, String key, String field, int limitTimes) throws MaizuoException {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) {
            throw new MaizuoException(ErrorCode.ILLEGAL_PARAM.getCode(), ErrorCode.ILLEGAL_PARAM.getMsg());
        }
        String timeType = this.formatTimeByType(type);
        /*
        根据时间类型给key加后缀：
        1:天的后缀是 20160119
        2:小时的后缀是 2016011911
        3:分钟的后缀是 201601191150
        */
        key = this.checkKeyFormat(key) + timeType;
        return this.checkRateByOnlyTimes(key, field, limitTimes);
    }

    /**
     * 增加指定的对象在约定的时间单位（如天，小时，分等）的操作次数
     * 比如：限制单个ip一天内的请求次数
     *
     * @param type  1: 天 ； 2：小时；3：分钟
     * @param key   业务类型，例如如果是IP类可以传IP
     * @param field 操作对象的唯一标识
     * @return true : 增加成功；false : 增加失败
     */
    public boolean rateIncrByTimeType(int type, String key, String field) throws MaizuoException {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) {
            throw new MaizuoException(ErrorCode.ILLEGAL_PARAM.getCode(), ErrorCode.ILLEGAL_PARAM.getMsg());
        }
        String timeType = this.formatTimeByType(type);
        /*
        根据时间类型给key加后缀：
        1:天的后缀是 20160119
        2:小时的后缀是 2016011911
        3:分钟的后缀是 201601191150
        */
        key = this.checkKeyFormat(key) + timeType;
        return this.incrRateByOnlyTimes(key, field, this.remainTimeByType(type));
    }

    /**
     * 根据指定的时间类型（1: 天 ； 2：小时；3：分钟）
     * 删除频率限制对象的缓存数据,一般用于限制对象正确操作之后
     *
     * @param type  1: 天 ； 2：小时；3：分钟
     * @param key   key
     * @param field field
     * @return true: 删除成功 ; false：删除失败
     * @throws MaizuoException 卖座定义的异常
     */
    public boolean clearRateByTimeType(int type, String key, String field) throws MaizuoException {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) {
            throw new MaizuoException(ErrorCode.ILLEGAL_PARAM.getCode(), ErrorCode.ILLEGAL_PARAM.getMsg());
        }
        key = this.checkKeyFormat(key) + this.formatTimeByType(type);
        return this.clearRate(key, field);
    }

    /**
     * 按类型格式化当前时间类型，作为位hash key 的后缀
     *
     * @param type 1: 天 ； 2：小时；3：分钟
     * @return 根据时间类型，格式化后的时间
     */
    private String formatTimeByType(int type) throws MaizuoException {
        SimpleDateFormat sdf;
        switch (type) {
            case DAY_TYPE:
                sdf = new SimpleDateFormat("yyyyMMdd");
                break;
            case HOUR_TYPE:
                sdf = new SimpleDateFormat("yyyyMMddHH");
                break;
            case MINUTE_TYPE:
                sdf = new SimpleDateFormat("yyyyMMddHHmm");
                break;
            default:
                throw new MaizuoException(ErrorCode.ILLEGAL_PARAM.getCode(), ErrorCode.ILLEGAL_PARAM.getMsg());
        }
        return sdf.format(new Date());
    }

    /**
     * 按时间类型计算当前时间距离失效的剩余时间
     *
     * @param type 1: 天 ； 2：小时；3：分钟
     * @return 计算后的时间，单位秒(s)
     */
    private int remainTimeByType(int type) throws MaizuoException {
        long nowTime = System.currentTimeMillis() / 1000;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf;
        switch (type) {
            case DAY_TYPE:
                calendar.add(Calendar.DATE, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                break;
            case HOUR_TYPE:
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                break;
            case MINUTE_TYPE:
                calendar.add(Calendar.MINUTE, 1);
                calendar.set(Calendar.SECOND, 0);
                break;
            default:
                throw new MaizuoException(ErrorCode.ILLEGAL_PARAM.getCode(), ErrorCode.ILLEGAL_PARAM.getMsg());
        }
        long latestTime = calendar.getTime().getTime() / 1000;
        return (int) (latestTime - nowTime);
    }

    /**
     * 忽略时间，只针对次数进行频率限制
     * 判断对象是否可以继续操作
     *
     * @param key        key
     * @param field      对象唯一标识
     * @param limitTimes 限制次数
     * @return true:可以继续操作 false:已经达到频率限制的条件，不可以继续操作
     * @throws MaizuoException 卖座自定义异常
     */
    private boolean checkRateByOnlyTimes(String key, String field, int limitTimes) throws MaizuoException {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) {
            throw new MaizuoException(ErrorCode.ILLEGAL_PARAM.getCode(), ErrorCode.ILLEGAL_PARAM.getMsg());
        }
        // 不存在这个key或者这个hash里没有这个field
        if (!baseRedis.exists(key) || !baseRedis.hexists(key, field)) {
            return true;
        }
        String count = baseRedis.hget(key, field);
        if (StringUtils.isBlank(count) || !StringUtils.isNumeric(count)) {
            throw new MaizuoException(ErrorCode.REDIS_RETURN_ERROR.getCode(), ErrorCode.REDIS_RETURN_ERROR.getMsg());
        }
        return limitTimes > Integer.parseInt(count);
    }

    /**
     * 忽略时间，只针对次数进行频率限制
     * 增加操作次数
     *
     * @param key          key
     * @param field        操作对象的唯一标识
     * @param expirSeconds 失效时间
     * @return true: 操作成功 false: 操作失败
     * @throws MaizuoException 卖座自定义异常
     */
    private boolean incrRateByOnlyTimes(String key, String field, int expirSeconds) throws MaizuoException {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) {
            throw new MaizuoException(ErrorCode.ILLEGAL_PARAM.getCode(), ErrorCode.ILLEGAL_PARAM.getMsg());
        }
        // 指定的key，field 原子增1,并且设定key的失效时间
        return (baseRedis.hincrby(key, field, 1L) > 0 && baseRedis.expire(key, expirSeconds));
    }

    /**
     * 增加key 的“:”后缀
     *
     * @param key
     * @return
     */
    private String checkKeyFormat(String key) {
        if (key.endsWith(":")) {
            return key;
        } else {
            return key + ":";
        }
    }

}
