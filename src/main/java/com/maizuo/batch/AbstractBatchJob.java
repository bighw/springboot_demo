package com.maizuo.batch;


import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;

import java.util.List;


public abstract class AbstractBatchJob<I,O> implements ItemWriter<O>,ItemReader<I>,ItemProcessor<I,O>,JobExecutionListener {


    public abstract void beforeJob(JobExecution jobExecution) ;
    public abstract void afterJob(JobExecution jobExecution);
    public abstract O process(I item) throws Exception;
    public abstract I read() throws Exception;
    public abstract void write(List<? extends O> items) throws Exception;




}
