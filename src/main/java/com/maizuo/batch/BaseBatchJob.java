package com.maizuo.batch;

import com.maizuo.api3.commons.util.LogUtils;
import org.springframework.batch.core.JobExecution;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Aaron on 16/8/18.
 * 批任务基础类.需要批任务处理的时候继承本类并重写本类中的方法
 * 特别说明:
 * 0.如果不需要多线程任务,则所有的read-process-write将会串行执行
 * 1.jobName和stepName是Job的唯一标示,对同名的jobName和stepName是不会重复执行的,防止多次执行同一个任务。
 * 2.建议chunk和pool大小保持一致。在子类中改写所需要的线程池大小。
 * 3.如有需要其他的多线程任务执行器,自行创建。否则请在子类中使用super.beforeJob和afterJob初始化默认执行器。
 */
public class BaseBatchJob<I, O> extends AbstractBatchJob<I, O> {
    public int DEFAULT_POOL_SIZE = 10;
    public int DEFAULT_CHUNK_SIZE = 10;


    public ExecutorService executor;
    //默认step中的执行线程是10个。
    public int poolSize=DEFAULT_POOL_SIZE;
    //默认Job中的chunk块是10个。
    public int chunkSize=DEFAULT_CHUNK_SIZE;


    public String jobName;
    public String stepName;


    @Override
    public I read() throws Exception {
        return null;
    }

    /**
     * 批任务读取数据的入口。
     * 注意,当返回null的时候,任务后续将不再执行读取任务。
     *
     * @return
     * @throws Exception
     */
    public O process(I item) throws Exception {
        return null;
    }

    @Override
    public void write(List<? extends O> items) throws Exception {

    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        //初始化默认的多线程任务执行器
        if (poolSize == 0) {
            poolSize = DEFAULT_POOL_SIZE;
        }
        executor = Executors.newFixedThreadPool(poolSize);

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        //关闭默认的多线程任务执行器
		System.out.println("关闭线程池");
		LogUtils.info("关闭线程池");
		executor.shutdown();
    }
}
