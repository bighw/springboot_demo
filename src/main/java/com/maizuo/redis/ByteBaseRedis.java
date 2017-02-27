package com.maizuo.redis;

import com.hyx.zookeeper.MaizuoLogUtil;
import com.maizuo.api3.commons.util.JsonUtils;
import com.maizuo.api3.commons.util.LogUtils;
import com.maizuo.constants.Constants;
import com.maizuo.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author harvey
 * @ClassName ByteBaseRedis
 * @Email harvey@maizuo.com
 * @create 2017/2/5 16:29
 * @Description:
 */
@Component
public class ByteBaseRedis {
    @Autowired
    private RedisTemplate redisTemplate;

    private String CHARSET_NAME = "UTF8";

    /**
     * 异常处理
     *
     * @param e
     */
    private void redisException(Exception e) {
        if (null == e) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("exception_msg", "" + e.toString());
        LogUtils.error("==========Redis Exception==========" + e.toString());
        Util.logExceptionStack(e);
        MaizuoLogUtil.writeLog(Constants.SYSTEMID, Constants.SYSTEMID, "", "redis_exception", JsonUtils.toJSON(map), "redis异常", 0, 1);
    }

    public boolean set(final String key, final byte[] value) {
        boolean flag = false;
        try {
            flag = (boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
                    try {
                        byte[] bkey = key.getBytes(CHARSET_NAME);
                        conn.set(bkey, value);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return flag;
    }

    public byte[] get(final String key) {
        byte[] value = new byte[0];
        try {
            value = (byte[]) redisTemplate.execute(new RedisCallback<byte[]>() {
                public byte[] doInRedis(RedisConnection conn) throws DataAccessException {
                    try {
                        byte[] bkey = key.getBytes(CHARSET_NAME);
                        return  conn.get(bkey);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return new byte[0];
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return value;
    }

}
