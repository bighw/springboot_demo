/**
 * 2017年1月17日 上午11:00:19  @author paul
 * <p>TODO</P>
 * @version TODO
 */
package com.maizuo.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maizuo.dao.CardDao;
import com.maizuo.redis.JobTokenRedis;
import com.maizuo.redis.handler.CardRedis;

/**
 * @ClassName:CardService
 * @Description:()
 * @Email:paul@hyx.com
 * @author paul  
 * @date 2017年1月17日 上午11:00:19 
 */
@Service
public class CardService {
	@Autowired
	CardDao cardDao;
	
	@Autowired
	JobTokenRedis jobTokenRedis;
	
	@Autowired
	CardRedis cardRedis;
	
	public Integer initCard(String batch, long begin, int count){
		String pattern="0000000000";
		long beginNo = begin;
		DecimalFormat df = new DecimalFormat(pattern);
		List<String> cardNoList = new ArrayList<String>();
		for(int i=0;i<count;i++){
			int random = (int) (Math.random()*10)+1;
			beginNo+=random;
			cardNoList.add(df.format(beginNo));
		}
		
		return cardDao.initCard(batch, cardNoList);
		
	}

	/**
	 * @Description:TODO(确定创建卡分发任务的线程的开始值)
	 * @param begin
	 * @return  参数
	 * long  返回类型
	 */
	public Long getBatchBeginNo(String batch,long begin) {
		long beginNo = cardRedis.incrCardSeqBy(10000L);
		if(beginNo<=begin+100000){
			return beginNo;
		}
		//返回null后不再发起线程，read方法结束
		jobTokenRedis.setTokenValue(batch, "status", "9");
		return null;
	}

	/**
	 * @Description:TODO(获取缓存内当前卡号序列开始值)
	 * @return  参数
	 * String  返回类型
	 */
	public String getCardSeqBegin() {
		return cardRedis.getCardSeqBegin();
	}
}
