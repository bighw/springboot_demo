package com.maizuo.redis;

import com.hyx.zookeeper.MaizuoLogUtil;
import com.maizuo.api3.commons.util.JsonUtils;
import com.maizuo.api3.commons.util.LogUtils;
import com.maizuo.constants.Constants;
import com.maizuo.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author qiyang
 * @ClassName: BaseRedis
 * @Description: redis基类,请勿随便修改，需征得系统负责人同意
 * @Email qiyang@maizuo.com
 * @date 2016/8/15 0015
 */
@Component
public class BaseRedis {
    @Autowired
    private StringRedisTemplate redisTemplate;

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

    /**
     * 查询一个key是否存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        boolean flag = false;
        try {
            flag = redisTemplate.hasKey(key);
        } catch (Exception e) {
            redisException(e);
        }
        return flag;
    }

    /**
     * 设置一个key的过期的秒数
     *
     * @param key
     * @param time
     * @return
     */
    public boolean expire(final String key, final int time) {
        //永不失效，不用设置
        if (time == -1) {
            return true;
        }
        boolean flag = false;
        try {
            flag = redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
                    if (!exists(key)) {
                        return false;
                    }
                    boolean doflag;
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    doflag = conn.expire(bkey, time);
                    return doflag;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return flag;
    }

    /**
     * 判断给定域是否存在于哈希集中
     *
     * @param key
     * @return
     */
    public boolean hexists(final String key, final String field) {
        boolean flag = false;
        try {
            flag = redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
                    boolean doflag = false;
                    if (exists(key)) {
                        byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                        byte[] bfield = redisTemplate.getStringSerializer().serialize(field);
                        doflag = conn.hExists(bkey, bfield);
                    }

                    return doflag;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return flag;
    }

    /**
     * 删除一个key
     *
     * @param key
     * @return
     */
    public boolean del(String key) {
        boolean flag = false;
        try {
            if (exists(key)) {
                redisTemplate.delete(key);
                flag = true;
            }
        } catch (Exception e) {
            redisException(e);
        }
        return flag;
    }

    /**
     * 获取key的值
     *
     * @param key
     */
    public String get(final String key) {
        String result = null;
        try {
            result = redisTemplate.execute(new RedisCallback<String>() {
                public String doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    String str = null;
                    if (exists(key)) {
                        byte[] bvalue = conn.get(bkey);
                        if (bvalue != null && bvalue.length > 0) {
                            str = redisTemplate.getStringSerializer().deserialize(bvalue);
                        }
                    }
                    return str;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * string设置
     *
     * @param key
     */
    public boolean set(final String key, final String value) {
        boolean flag = false;
        try {
            flag = redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
                    boolean doflag;
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    byte[] bvalue = redisTemplate.getStringSerializer().serialize(value);
                    conn.set(bkey, bvalue);
                    doflag = true;
                    return doflag;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return flag;
    }

    /**
     * 设置不存在string
     *
     * @param key
     */
    public boolean setNx(final String key) {
        boolean flag = false;
        try {
            flag = redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
                    boolean doflag;
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    String now = String.valueOf(System.currentTimeMillis());
                    byte[] bvalue = redisTemplate.getStringSerializer().serialize(now);
                    doflag = conn.setNX(bkey, bvalue);
                    return doflag;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return flag;
    }


    /**
     * 删除HashMap指定键值
     *
     * @param key
     * @return
     */
    public boolean hdel(final String key, final String field) {
        boolean flag = false;
        try {
            flag = redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
                    boolean doflag = false;
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    byte[] bfield = redisTemplate.getStringSerializer().serialize(field);
                    if (exists(key)) {
                        conn.hDel(bkey, bfield);
                        doflag = true;
                    }
                    return doflag;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return flag;
    }

    /**
     * hash长度
     *
     * @param key
     * @return
     */
    public long hlen(final String key) {
        long len = 0;
        try {
            len = redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    long len = conn.hLen(bkey);
                    return len;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return len;
    }

    /**
     * 添加map键值对
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean hset(final String key, final String field, final String value) {
        boolean flag = false;
        try {
            flag = redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
                    boolean doflag;
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    byte[] bfield = redisTemplate.getStringSerializer().serialize(field);
                    byte[] bvalue = redisTemplate.getStringSerializer().serialize(value);
                    doflag = conn.hSet(bkey, bfield, bvalue);
                    return doflag;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return flag;
    }

    /**
     * 为哈希表 key 中的域 field 的值加上增量 increment 。
     * HINCRBY
     *
     * @param key
     * @param field
     * @param incre
     * @return
     */
    public Long hincrby(final String key, final String field, final long incre) {
        Long flag = 0L;
        try {
            flag = redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    byte[] bfield = redisTemplate.getStringSerializer().serialize(field);
                    return conn.hIncrBy(bkey, bfield, incre);
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return flag;
    }

    /**
     * 获取hash里面指定字段的值
     *
     * @param key
     * @param field
     */
    public String hget(final String key, final String field) {
        String result = null;
        try {
            result = redisTemplate.execute(new RedisCallback<String>() {
                public String doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    byte[] bfield = redisTemplate.getStringSerializer().serialize(field);
                    String str = null;
                    if (exists(key)) {
                        byte[] bvalue = conn.hGet(bkey, bfield);
                        if (bvalue != null && bvalue.length > 0) {
                            str = redisTemplate.getStringSerializer().deserialize(bvalue);
                        }
                    }
                    return str;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 批量获取hash里面指定字段的值
     *
     * @param key
     */
    public List<String> hmget(final String key, final List<String> fields) {
        List<String> result = null;
        try {
            result = redisTemplate.execute(new RedisCallback<List<String>>() {
                public List<String> doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    byte[][] bfieldArr = new byte[fields.size()][];
                    for (int i = 0; i < fields.size(); i++) {
                        bfieldArr[i] = redisTemplate.getStringSerializer().serialize(fields.get(i));
                    }
                    List<String> strList = null;
                    if (exists(key)) {
                        List<byte[]> valueList = conn.hMGet(bkey, bfieldArr);
                        if (valueList != null && valueList.size() > 0) {
                            strList = new ArrayList<String>();
                            for (byte[] value : valueList) {
                                if (value == null) {
                                    continue;
                                }
                                strList.add(redisTemplate.getStringSerializer().deserialize(value));
                            }
                        }
                    }
                    return strList;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 哈希:根据fileds获取value,按顺序返回结果,不存在的filed返回空字符
     *
     * @param key
     * @param fields
     * @return
     */
    public List<String> hmget2(final String key, final List<String> fields) {
        List<String> result = null;
        try {
            result = redisTemplate.execute(new RedisCallback<List<String>>() {
                public List<String> doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    byte[][] bfieldArr = new byte[fields.size()][];
                    for (int i = 0; i < fields.size(); i++) {
                        bfieldArr[i] = redisTemplate.getStringSerializer().serialize(fields.get(i));
                    }
                    List<String> strList = new ArrayList<String>();
                    if (exists(key)) {
                        List<byte[]> valueList = conn.hMGet(bkey, bfieldArr);
                        if (valueList != null && valueList.size() > 0) {
                            strList = new ArrayList<String>();
                            for (byte[] value : valueList) {
                                if (value == null) {
                                    strList.add("");
                                } else {
                                    strList.add(redisTemplate.getStringSerializer().deserialize(value));
                                }

                            }
                        }
                    }
                    return strList;
                }
            });
        } catch (Exception e) {
            Util.logExceptionStack(e);
            redisException(e);
        }
        return result;
    }

    /**
     * 设置hash字段值
     *
     * @param key
     */
    public void hmset(final String key, final Map<String, String> map) {
        try {
            redisTemplate.execute(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection conn) throws DataAccessException {

                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    Map<byte[], byte[]> bmap = new HashMap<byte[], byte[]>();
                    for (String mapkey : map.keySet()) {
                        byte[] bmapkey = redisTemplate.getStringSerializer().serialize(mapkey);
                        byte[] bmapvalue = redisTemplate.getStringSerializer().serialize(map.get(mapkey));
                        bmap.put(bmapkey, bmapvalue);
                    }
                    conn.hMSet(bkey, bmap);
                    return null;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
    }

    /**
     * 获取HashMap中所有键集合
     *
     * @param key
     * @return
     */
    public Set<String> hkeys(final String key) {
        Set<String> result = null;
        try {
            result = redisTemplate.execute(new RedisCallback<Set<String>>() {
                public Set<String> doInRedis(RedisConnection conn) throws DataAccessException {
                    Set<String> strSet = null;
                    if (exists(key)) {
                        byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                        Set<byte[]> bmapkyes = conn.hKeys(bkey);
                        if (bmapkyes != null && bmapkyes.size() > 0) {
                            strSet = new HashSet<String>();
                            for (byte[] bmapkey : bmapkyes) {
                                if (bmapkey == null) {
                                    continue;
                                }
                                strSet.add(redisTemplate.getStringSerializer().deserialize(bmapkey));
                            }
                        }
                    }
                    return strSet;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 获得hash的所有值
     *
     * @param key
     */
    public List<String> hvals(final String key) {
        List<String> result = null;
        try {
            result = redisTemplate.execute(new RedisCallback<List<String>>() {
                public List<String> doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    List<String> strList = null;
                    if (exists(key)) {
                        List<byte[]> valueList = conn.hVals(bkey);
                        if (valueList != null && valueList.size() > 0) {
                            strList = new ArrayList<String>();
                            for (byte[] value : valueList) {
                                if (value == null) {
                                    continue;
                                }
                                strList.add(redisTemplate.getStringSerializer().deserialize(value));
                            }
                        }
                    }
                    return strList;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 获取HashMap所有键值对
     *
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(final String key) {
        Map<String, String> result = null;
        try {
            result = redisTemplate.execute(new RedisCallback<Map<String, String>>() {
                public Map<String, String> doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    Map<String, String> strMap = null;
                    if (exists(key)) {
                        Map<byte[], byte[]> bmap = conn.hGetAll(bkey);
                        if (bmap != null && bmap.size() > 0) {
                            strMap = new HashMap<String, String>();
                            for (byte[] key : bmap.keySet()) {
                                String mapkey = redisTemplate.getStringSerializer().deserialize(key);
                                String mapvalue = redisTemplate.getStringSerializer().deserialize(bmap.get(key));
                                strMap.put(mapkey, mapvalue);
                            }
                        }
                    }
                    return strMap;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 左侧进入队列
     *
     * @param key
     * @return
     */
    public boolean lPush(final String key, final String value) {
        boolean result = false;
        try {
            result = redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    byte[] bvalue = redisTemplate.getStringSerializer().serialize(value);
                    long index = conn.lPush(bkey, bvalue);
                    if (index > 0) {
                        return true;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 右侧进入队列
     *
     * @param key
     * @return
     */
    public boolean rPush(final String key, final String value) {
        boolean result = false;
        try {
            result = redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    byte[] bvalue = redisTemplate.getStringSerializer().serialize(value);
                    long index = conn.rPush(bkey, bvalue);
                    if (index > 0) {
                        return true;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 左侧出队列
     *
     * @param key
     * @return
     */
    public String lPop(final String key) {
        String result = null;
        try {
            result = redisTemplate.execute(new RedisCallback<String>() {
                public String doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    String result = null;
                    if (exists(key)) {
                        byte[] bvalue = conn.lPop(bkey);
                        if (bvalue != null && bvalue.length > 0) {
                            result = redisTemplate.getStringSerializer().deserialize(bvalue);
                        }
                    }
                    return result;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 右侧出队列
     *
     * @param key
     * @return
     */
    public String rPop(final String key) {
        String result = null;
        try {
            result = redisTemplate.execute(new RedisCallback<String>() {
                public String doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    String result = null;
                    if (exists(key)) {
                        byte[] bvalue = conn.rPop(bkey);
                        if (bvalue != null && bvalue.length > 0) {
                            result = redisTemplate.getStringSerializer().deserialize(bvalue);
                        }
                    }
                    return result;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 队列长度
     *
     * @param key
     * @return
     */
    public int lLen(final String key) {
        int result = 0;
        try {
            result = redisTemplate.execute(new RedisCallback<Integer>() {
                public Integer doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    int result = 0;
                    if (exists(key)) {
                        result = Integer.valueOf(String.valueOf(conn.lLen(bkey)));
                    }
                    return result;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 获取队列指定范围列表
     *
     * @param key
     * @return
     */
    public List<String> lRange(final String key, final int start, final int stop) {
        List<String> result = new ArrayList<String>();
        try {
            result = redisTemplate.execute(new RedisCallback<List<String>>() {
                public List<String> doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    List<String> strList = null;
                    if (exists(key)) {
                        List<byte[]> valueList = conn.lRange(bkey, start, stop);
                        if (valueList != null && valueList.size() > 0) {
                            strList = new ArrayList<String>();
                            for (byte[] value : valueList) {
                                if (value == null) {
                                    continue;
                                }
                                strList.add(redisTemplate.getStringSerializer().deserialize(value));
                            }
                        }
                    }
                    return strList;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 循环队列pop
     *
     * @param key
     * @return
     */
    public String rPopLPush(final String key) {
        String result = null;
        try {
            result = redisTemplate.execute(new RedisCallback<String>() {
                public String doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    String result = null;
                    if (exists(key)) {
                        byte[] value = conn.rPopLPush(bkey, bkey);
                        result = redisTemplate.getStringSerializer().deserialize(value);
                    }
                    return result;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 移除列表中指定值所有元素
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lRem(final String key, final String value) {
        boolean result = false;
        try {
            result = redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    byte[] bvalue = redisTemplate.getStringSerializer().serialize(value);
                    Boolean result = false;
                    if (exists(key)) {
                        long index = conn.lRem(bkey, 0, bvalue);
                        if (index > 0) {
                            result = true;
                        }
                    }
                    return result;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 移除列表头开始中指定个数值元素
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lLRem(final String key, final String value, final int n) {
        boolean result = false;
        try {
            result = redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    byte[] bvalue = redisTemplate.getStringSerializer().serialize(value);
                    Boolean result = false;
                    if (exists(key)) {
                        long index = conn.lRem(bkey, n, bvalue);
                        if (index > 0) {
                            result = true;
                        }
                    }
                    return result;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 移除列表尾开始中指定个数值元素
     *
     * @param key
     * @param value
     * @return
     */
    public boolean lRRem(final String key, final String value, final int n) {
        boolean result = false;
        try {
            result = redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    byte[] bvalue = redisTemplate.getStringSerializer().serialize(value);
                    Boolean result = false;
                    if (exists(key)) {
                        long index = conn.lRem(bkey, 0 - n, bvalue);
                        if (index > 0) {
                            result = true;
                        }
                    }
                    return result;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 添加有序集合
     *
     * @param key
     * @param score
     * @param member
     * @return
     */
    public boolean zadd(final String key, final double score, final String member) {
        boolean result = false;
        try {
            result = redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    byte[] bmember = redisTemplate.getStringSerializer().serialize(member);
                    Boolean result;
                    result = conn.zAdd(bkey, score, bmember);
                    return result;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 自增
     *
     * @param key
     * @return
     */
    public long incr(final String key) {
        Long result = 0l;
        try {
            result = redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    Long result;
                    result = conn.incr(bkey);
                    return result;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }
    
    /**
     * 自增指定增量
     *
     * @param key
     * @return
     */
    public long incrBy(final String key, final long incre) {
        Long result = 0l;
        try {
            result = redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    Long result;
                    result = conn.incrBy(bkey,incre);
                    return result;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 移除有序集合元素
     *
     * @param key
     * @param member
     * @return
     */
    public boolean zrem(final String key, final String member) {
        boolean result = false;
        try {
            result = redisTemplate.execute(new RedisCallback<Boolean>() {
                public Boolean doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    byte[] bmember = redisTemplate.getStringSerializer().serialize(member);
                    conn.zRem(bkey, bmember);
                    return true;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 返回有序集合key的某一段:
     *
     * @param key
     * @param begin
     * @param end
     * @return
     */
    public List<String> zRange(final String key, final long begin, final long end) {


        final String inkey = key;
        List<String> result = null;
        try {
            return result = redisTemplate.execute(new RedisCallback<List<String>>() {
                public List<String> doInRedis(RedisConnection conn) throws DataAccessException {
                    List<String> dataResult = new ArrayList<String>();
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(inkey);

                    Set<byte[]> data = conn.zRange(bkey, begin, end);

                    if (data != null && data.size() > 0) {
                        for (byte[] bs : data) {
                            String str = redisTemplate.getStringSerializer().deserialize(bs);
                            dataResult.add(str);
                        }
                    }
                    return dataResult;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 返回member在有序集合中的排号
     *
     * @param key
     * @param member
     * @return
     */
    public long zRank(final String key, final String member) {
        final String inkey = key;
        long result = 0;
        try {
            return result = redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection conn) throws DataAccessException {
                    List<String> dataResult = new ArrayList<String>();
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(inkey);
                    byte[] bmember = redisTemplate.getStringSerializer().serialize(member);
                    return conn.zRank(bkey, bmember);
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 有序集合成员分数自增
     *
     * @param key
     * @param increment
     * @param member
     * @return
     */
    public double zincrby(final String key, final double increment, final String member) {
        double result = -1;
        try {
            result = redisTemplate.execute(new RedisCallback<Double>() {
                public Double doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    byte[] bmember = redisTemplate.getStringSerializer().serialize(member);
                    double result;
                    result = conn.zIncrBy(bkey, increment, bmember);
                    return result;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 获取有序集合中指定分数范围的成员集合
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<String> zrangeByScore(final String key, final double min, final double max) {
        Set<String> result = null;
        try {
            result = redisTemplate.execute(new RedisCallback<Set<String>>() {
                public Set<String> doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    if (!exists(key)) {
                        return null;
                    }
                    Set<byte[]> byteSet = conn.zRangeByScore(bkey, min, max);
                    if (byteSet == null || byteSet.size() == 0) {
                        return null;
                    }
                    Set<String> result = new HashSet<String>();
                    for (byte[] bArr : byteSet) {
                        result.add(redisTemplate.getStringSerializer().deserialize(bArr));
                    }
                    return result;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 删除有序集合中指定分数范围成员
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    public long zremRangeByScore(final String key, final double min, final double max) {
        long result = 0l;
        try {
            result = redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    if (!exists(key)) {
                        return null;
                    }
                    long result;
                    result = conn.zRemRangeByScore(bkey, min, max);
                    return result;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    public List<String> zRevRange(final String key, final long start, final long end) {
        List<String> result = null;
        try {
            result = redisTemplate.execute(new RedisCallback<List<String>>() {
                public List<String> doInRedis(RedisConnection conn) throws DataAccessException {
                    List<String> dataResult = new ArrayList<String>();
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);

                    Set<byte[]> data = conn.zRevRange(bkey, start, end);

                    if (data != null && data.size() > 0) {
                        for (byte[] bs : data) {
                            String str = redisTemplate.getStringSerializer().deserialize(bs);
                            dataResult.add(str);
                        }
                    }
                    return dataResult;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 分数从小到大排序
     *
     * @param key
     * @param max
     * @param min
     * @param offset
     * @param num
     * @return
     */
    public List<Map<String, Object>> zRangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int num) {
        List<Map<String, Object>> result = null;
        try {
            result = redisTemplate.execute(new RedisCallback<List<Map<String, Object>>>() {
                public List<Map<String, Object>> doInRedis(RedisConnection conn) throws DataAccessException {
                    List<Map<String, Object>> dataResult = new ArrayList<Map<String, Object>>();
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);

                    Set<RedisZSetCommands.Tuple> data = conn.zRangeByScoreWithScores(bkey, min, max, offset, num);

                    if (data != null && data.size() > 0) {
                        for (RedisZSetCommands.Tuple tuple : data) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            byte[] bValue = tuple.getValue();
                            Double score = tuple.getScore();
                            String value = redisTemplate.getStringSerializer().deserialize(bValue);
                            map.put("value", value);
                            map.put("score", score.toString());
                            dataResult.add(map);
                        }
                    }
                    return dataResult;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    public Double zScore(final String key, final String member) {
        Double result = 0d;
        try {
            result = redisTemplate.execute(new RedisCallback<Double>() {
                public Double doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);
                    byte[] bmember = redisTemplate.getStringSerializer().serialize(member);
                    Double score = conn.zScore(bkey, bmember);
                    return score;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 分数从大到小排序
     *
     * @param key
     * @param max
     * @param min
     * @param offset
     * @param num
     * @return
     */
    public List<Map<String, Object>> zRevRangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int num) {
        List<Map<String, Object>> result = Collections.EMPTY_LIST;
        try {
            result = redisTemplate.execute(new RedisCallback<List<Map<String, Object>>>() {
                public List<Map<String, Object>> doInRedis(RedisConnection conn) throws DataAccessException {
                    List<Map<String, Object>> dataResult = new ArrayList<Map<String, Object>>();
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(key);

                    Set<RedisZSetCommands.Tuple> data = conn.zRevRangeByScoreWithScores(bkey, min, max, offset, num);

                    if (data != null && data.size() > 0) {
                        for (RedisZSetCommands.Tuple tuple : data) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            byte[] bValue = tuple.getValue();
                            Double score = tuple.getScore();
                            String value = redisTemplate.getStringSerializer().deserialize(bValue);
                            map.put("value", value);
                            map.put("score", score.toString());
                            dataResult.add(map);
                        }
                    }
                    return dataResult;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }

    /**
     * 按顺序批量查询string。返回顺序与入参顺序一致,不存在的key则返回空字符串
     *
     * @param keys
     * @return
     */
    public List<String> mget(List<String> keys) {
        final List<String> inkeys = keys;
        List<String> result = null;
        try {
            result = redisTemplate.execute(new RedisCallback<List<String>>() {
                public List<String> doInRedis(RedisConnection conn) throws DataAccessException {
                    List<String> strList = null;
                    byte[][] bfieldArr = new byte[inkeys.size()][];
                    for (int i = 0; i < inkeys.size(); i++) {
                        bfieldArr[i] = redisTemplate.getStringSerializer().serialize(inkeys.get(i));
                    }
                    List<byte[]> valueList = conn.mGet(bfieldArr);
                    if (valueList != null && valueList.size() > 0) {
                        strList = new ArrayList<String>();
                        for (byte[] value : valueList) {
                            if (value == null) {
                                strList.add("");
                            } else {
                                strList.add(redisTemplate.getStringSerializer().deserialize(value));
                            }

                        }
                    }

                    return strList;
                }
            });
        } catch (Exception e) {
            redisException(e);
        }
        return result;
    }


    private static String LRUPREKEY = "LRUHASH:";
    private static String KEYFIELDSEPARATER = "->";

    /**
     * 添加map键值对(LRU算法管理)
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean lru_hset(String sysId, String key, String field, String value) {
        boolean flag1 = zadd(LRUPREKEY + sysId, Double.valueOf(System.currentTimeMillis()), key + KEYFIELDSEPARATER + field);
        boolean flag2 = hset(key, field, value);
        if (flag1 && flag2) {
            return true;
        }
        hdel(key, field);
        hdel(LRUPREKEY + sysId, key + KEYFIELDSEPARATER + field);
        return false;
    }

    /**
     * 设置hash字段值(LRU算法管理)
     *
     * @param key
     */
    public void lru_hmset(String sysId, String key, Map<String, String> map) {
        for (String field : map.keySet()) {
            zadd(LRUPREKEY + sysId, Double.valueOf(System.currentTimeMillis()), key + KEYFIELDSEPARATER + field);
        }
        hmset(key, map);
    }

    /**
     * 返回有序集合长度:
     *
     * @param key
     * @return
     */
    public Long zCard(final String key) {
        final String inkey = key;
        try {
            return redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection conn) throws DataAccessException {

                    return conn.zCard(redisTemplate.getStringSerializer().serialize(inkey));
                }
            });
        } catch (Exception e) {
            redisException(e);
            return -1L;
        }
    }

    /**
     * 清理早于指定日期前的缓存
     *
     * @param day
     */
    public void lru_oldclean(String sysId, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, 0 - day);
        long time = c.getTimeInMillis();
        Set<String> set = zrangeByScore(LRUPREKEY + sysId, 0, time);
        for (String key : set) {
            String hashKey = key.split(KEYFIELDSEPARATER)[0];
            String hashField = key.split(KEYFIELDSEPARATER)[1];
            hdel(hashKey, hashField);
        }
        zremRangeByScore(LRUPREKEY + sysId, 0, time);
    }

    /**
     * 获取redis锁
     *
     * @param uniqueKey 唯一键
     * @param validTime 有效时间
     * @return
     */
    @Transactional
    public boolean getLock(final String uniqueKey, final int validTime) {
        boolean flag = false;
        if (exists(uniqueKey)) {
            return flag;
        }
        try {
            RedisCallback<List<Object>> callback = new RedisCallback<List<Object>>() {
                public List<Object> doInRedis(RedisConnection conn) throws DataAccessException {
                    byte[] bkey = redisTemplate.getStringSerializer().serialize(uniqueKey);
                    String now = String.valueOf(System.currentTimeMillis());
                    byte[] bvalue = redisTemplate.getStringSerializer().serialize(now);
                    conn.openPipeline();
                    conn.setNX(bkey, bvalue);
                    conn.expire(bkey, validTime);
                    return conn.closePipeline();
                }
            };
            List<Object> results = redisTemplate.execute(callback);
            if (results != null && Boolean.valueOf(results.get(0).toString())) {
                flag = true;
            }
        } catch (Exception e) {
            redisException(e);
        }

        return flag;
    }

    /**
     * 释放占用锁
     *
     * @param uniqueKey
     */
    public void releaseLock(String uniqueKey) {
        if (exists(uniqueKey)) {
            del(uniqueKey);
        }
    }
}
