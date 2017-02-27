/**
 * 2017年1月17日 上午11:00:19  @author paul
 * <p>TODO</P>
 * @version TODO
 */
package com.maizuo.service;

import java.util.Map;

import com.maizuo.api3.commons.exception.MaizuoException;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.maizuo.batch.builder.BatchJobBuilder;
import com.maizuo.batch.handler.CreateCardJob;
import com.maizuo.dao.BatchJobDao;
import com.maizuo.data.entity.job.BatchJob;
import com.maizuo.redis.JobTokenRedis;

/**
 * @ClassName:BatchJobService
 * @Description:(批处理任务service)
 * @Email:paul@hyx.com
 * @author paul  
 * @date 2017年1月17日 上午11:00:19 
 */
@Service
public class BatchJobService {
	@Autowired
	BatchJobDao batchJobDao;
	
	@Autowired
	JobTokenRedis jobTokenRedis;
	
	@Autowired
	CreateCardJob createCardJob;
	

	/**
	 * @Description:TODO(创建一个批量生成卡的任务)
	 * @return  参数
	 * boolean  返回类型
	 */
	public boolean createCardJob() throws MaizuoException {
		String batch = batchJobDao.getNewBatchNo();
		if(null==batch){
			throw new MaizuoException(-1,"生成批次号失败");
		}
		if (jobTokenRedis.initToken(batch, 0)) {
			// 创建一个批处理创建卡的任务
			BatchJob job = new BatchJob();
			job.setJobName("创建卡" + batch);
			job.setType(1);
			job.setBatch(batch);
			return batchJobDao.createJob(job);
		} else {
			throw new MaizuoException(-1,"上一批次还未开始");
		}
	}

	/**
	 * @Description:TODO(启动批处理任务)
	 * @param jobName
	 * @return  参数
	 * boolean  返回类型
	 */
	public boolean startJob(String jobName) throws MaizuoException {
		BatchJob job = batchJobDao.getBatchJob(jobName);
    	if(null==job){
    		throw new MaizuoException(-1,"未找到此任务");
    	}
    	String status = jobTokenRedis.getTokenValue(String.valueOf(job.getBatch()), "status");
    	if(!StringUtils.equals(status, "0")){
    		throw new MaizuoException(-1,"不可重复启动");
    	}
    	//初始化任务参数
    	createCardJob.init(job.getBatch(), jobName, jobName);
        BatchJobBuilder.getInstance().startJob(createCardJob);
        return true;
	}

	/**
	 * @Description:TODO(终止批处理任务)
	 * @param jobName
	 * @return  参数
	 * boolean  返回类型
	 */
	public boolean stopJob(String jobName) throws MaizuoException {
		BatchJob job = batchJobDao.getBatchJob(jobName);
    	if(null==job){
    		throw new MaizuoException(-1,"未找到此任务");
    	}
    	String status = jobTokenRedis.getTokenValue(job.getBatch(), "status");
    	if(!StringUtils.equals(status, "1")){
    		throw new MaizuoException(-1,"任务还未开始或已完成");
    	}
    	//设置状态为 被终止
    	jobTokenRedis.setTokenValue(job.getBatch(), "status", "2");
    	//关掉开关，之后的业务不会再进行
        return jobTokenRedis.closeBatchJobSwitch(jobName);
	}

	/**
	 * @Description:TODO(获取批处理任务的状态)
	 * @param jobName
	 * @return  参数
	 * Map<String,String>  返回类型
	 */
	public Map<String, String> jobStatus(String jobName) throws MaizuoException {
		BatchJob job = batchJobDao.getBatchJob(jobName);
    	if(null==job){
    		throw new MaizuoException(-1,"未找到此任务");
    	}
        return jobTokenRedis.getTokenResult(job.getBatch());
	}
}
