package com.maizuo.redis.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maizuo.constants.RedisConstants;
import com.maizuo.redis.BaseRedis;

/**
 * 卡业务redis相关
 * @author paul
 *
 */
@Service
public class CardRedis {

	@Autowired
	private BaseRedis baseRedis;

	/**
	 * 获得卡号开始序列
	 * @return
	 */
	public String getCardSeqBegin(){
		return baseRedis.get(RedisConstants.REDIS_CARD_CURRENT_BEGIN);
	}

	/**
	 * 指定增量卡号开始序列
	 */
	public long incrCardSeqBy(long incr) {
		return baseRedis.incrBy(RedisConstants.REDIS_CARD_CURRENT_BEGIN,incr);
	}
}
