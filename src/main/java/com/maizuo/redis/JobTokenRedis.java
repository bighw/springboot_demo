package com.maizuo.redis;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maizuo.constants.RedisConstants;

/**
 * 令牌redis工具类
 * @author paul
 *
 */
@Service
public class JobTokenRedis {

	@Autowired
	private BaseRedis baseRedis;

	public static final int EXPIRE_TIME = 60*60*24;//过期时间 单位:s

	/**
	 * 生成令牌 
	 * time:令牌过期时间(单位:s)  null不设置  0默认1天
	 * @param token
	 * @return
	 */
	public boolean initToken(String token,Integer time){
		String key = RedisConstants.TOKEN_HEAD+token;
		//判断此业务动作是否已在执行
		if(!baseRedis.exists(key)){
			baseRedis.hset(key, "total", "0");
			baseRedis.hset(key, "increment", "0");
			baseRedis.hset(key, "status", "0");
			if(time==0){
				baseRedis.expire(key, EXPIRE_TIME);
			}else if(null!=time){
				baseRedis.expire(key, time);
			}
			return true;
		}else{
			//进行中的业务...
			return false;
		}
	}

	/**
	 * <pre>
	 * 更新令牌状态  key为increment时累加该值，其他key为直接设置值
	 * 令牌内的keys应为  ：
	 * 	total       总执行次数
	 * 	increment   当前执行进度数
	 * 	status      状态（-1 失败 0等待 1执行中 2被终止 9完成）
	 * </pre>
	 * @param token
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setTokenValue(String token, String key, String value){
		if(StringUtils.equals(key, "increment")){
			return baseRedis.hincrby(RedisConstants.TOKEN_HEAD+token, key, Long.valueOf(value))>0;
		}else{
			return baseRedis.hset(RedisConstants.TOKEN_HEAD+token, key, value);
		}
	}

	/**
	 * 查询令牌内的key值
	 * @param token
	 * @param key
	 * @return
	 */
	public String getTokenValue(String token, String key){
		return StringUtils.equals(baseRedis.hget(RedisConstants.TOKEN_HEAD+token, key), "null")?null:baseRedis.hget(RedisConstants.TOKEN_HEAD+token, key);
	}


	/**
	 * <pre>
	 * 根据令牌查看对应的结果信息
	 *  令牌内的keys应为  ：
	 * 	total       总执行次数
	 * 	increment   当前执行进度数
	 * 	status      状态（-1 失败 0等待 1执行中 2被终止 9完成）
	 * </pre>
	 * @param token
	 * @return
	 */
	public Map<String,String> getTokenResult(String token){
		return baseRedis.hgetAll(RedisConstants.TOKEN_HEAD+token);
	}
	
	/**
	 * 打开批处理开关
	 * @param jobName
	 * @param key
	 * @return
	 */
	public boolean openBatchJobSwitch(String jobName){
		return baseRedis.hset(RedisConstants.REDIS_BATCHJOB_SWITCH, jobName, "0");
	}
	
	/**
	 * 关闭批处理开关
	 * @param jobName
	 * @return
	 */
	public boolean closeBatchJobSwitch(String jobName){
		return baseRedis.hset(RedisConstants.REDIS_BATCHJOB_SWITCH, jobName, "1");
	}
	
	/**
	 * 获取批处理开关状态
	 * @param jobName
	 * @return
	 */
	public String getBatchJobSwitch(String jobName){
		return baseRedis.hget(RedisConstants.REDIS_BATCHJOB_SWITCH, jobName);
	}
}
