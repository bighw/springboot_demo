package com.maizuo.batch.handler;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maizuo.batch.BaseBatchJob;
import com.maizuo.redis.JobTokenRedis;
import com.maizuo.service.CardService;


/**
 * @author rose
 * @ClassName: CreateCardJob
 * @Email rose@maizuo.com
 * @create 2017/1/19-18:02
 * @Description: desc
 */
@Component
public class CreateCardJob extends BaseBatchJob<Long, FutureTask<Integer>> {
	
	@Autowired
	private CardService cardService;
	
	@Autowired
	private JobTokenRedis jobTokenRedis;
	
	private String batch;
	private long begin;
	long start;
    long end;
    
//	public CreateCardJob(String batch, String jobName, String stepName, CardService cardService, JobTokenRedis jobTokenRedis) {
//		this.batch = batch;
//        this.jobName = jobName;
//        this.stepName = stepName;
//		this.begin = Long.valueOf(jobTokenRedis.getCardSeqBegin());
//		this.cardService = cardService;
//		this.jobTokenRedis = jobTokenRedis;
//	}

    /**
     * 初始化参数
     */
    public void init(String batch, String jobName,String stepName){
	    this.batch = batch;
	    this.jobName = jobName;
	    this.stepName = stepName;
	    this.begin = Long.valueOf(cardService.getCardSeqBegin());
    }

    /**
     * job执行前做的准备工作。super中负责启动线程执行器
     *
     * @param jobExecution
     */
    public void beforeJob(JobExecution jobExecution) {
		//初始化任务执行器。
        super.beforeJob(jobExecution);
        jobTokenRedis.setTokenValue(batch, "total", "10");
    	jobTokenRedis.setTokenValue(batch, "status", "1");
        start = System.currentTimeMillis();
        System.out.println("before Job time:" + start);
    }


    /**
     * job完成后调用
     *
     * @param jobExecution
     */
    public void afterJob(JobExecution jobExecution) {
        super.afterJob(jobExecution);
        end = System.currentTimeMillis();
        System.out.println("after Job time: " + end);
        System.out.println("take time:" + (end - start));
    }

	/**
	 * 通过BaseJob中的多线程执行器执行耗时任务。
	 * @param item
	 * @return
	 * @throws Exception
     */
    public FutureTask<Integer> process(final Long item) throws Exception {
		String flag = jobTokenRedis.getBatchJobSwitch(this.jobName);
		if (StringUtils.isNotBlank(flag) && "1".equals(flag)) {
			return null;
		}
        FutureTask<Integer> task = new FutureTask<>(
                new Callable<Integer>() {
                    public Integer call() throws Exception {
						System.out.println("batch:" + batch + ":process");
						
						Thread.sleep((long) (Math.random()*20000));
						
						return cardService.initCard(batch, item, 1000);
					}
                });

        executor.submit(task);
        return task;
    }


	/**
	 * 批任务读取数据的入口。
     * 注意,当返回null的时候,任务后续将不再执行读取任务。
     *
     * @return
	 * @throws Exception
     */
    public Long read() throws Exception {
		String flag = jobTokenRedis.getBatchJobSwitch(this.jobName);
		if (StringUtils.isNotBlank(flag) && "1".equals(flag)) {
			return null;
		}
		
		Thread.sleep((long) (Math.random()*3000));
		
		return cardService.getBatchBeginNo(batch,begin);
	}

    @Override
    public void write(List<? extends FutureTask<Integer>> items) throws Exception {
        LinkedList<FutureTask<Integer>> linklist = new LinkedList<FutureTask<Integer>>(
                items);

        FutureTask<Integer> future;

        while ((future = linklist.poll()) != null) {
            if (future.isDone()) {
                if (!future.isCancelled() || future.isDone()) {
                	Integer p = future.get();
                	if(p>0){
                		jobTokenRedis.setTokenValue(batch, "increment", "1");
                	}
				}
            } else {
                linklist.addLast(future);
            }
        }
    }
}
