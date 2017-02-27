package com.maizuo.constants;

/**
 * @author rose
 * @ClassName: RedisConstants
 * @Email rose@maizuo.com
 * @create 2017/1/13-10:52
 * @Description: REDIS的KEY常量备案文件
 */
public class RedisConstants {

    //用户基础信息缓存KEY前缀
    public static final String REDIS_USER_INFO_PREFIX  = "test:user:info:";

    //用户白名单缓存KEY
    public static final String REDIS_USER_WHITELIST_KEY = "test:user:whitelist";

    //用户黑名单缓存KEY
    public static final String REDIS_USER_BLACKLIST_KEY = "test:user:blacklist";


    //用户登录频率限制KEY前缀
    public static final String REDIS_USER_LOGIN_RATE = "mzrate:test:login:";

    // 批处理开关的缓存
    public static final String REDIS_BATCHJOB_SWITCH = "test:card:jobswitch";
    
    //卡号开始序列值
    public static final String REDIS_CARD_CURRENT_BEGIN = "test:card:currentbegin";
    
    //令牌head
	public static final String TOKEN_HEAD = "test:token:";


    //令牌head
    public static final String REDIS_GETLOCK_ORDER = "test:getlock:order:";

    //protobuf 测试用的缓存key
    public static final String REDIS_PROTOBUF_PERSON = "test:protobuf:person";

    //json 测试用的缓存key
    public static final String REDIS_JSON_PERSON = "test:json:person";

}
