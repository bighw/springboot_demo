package com.maizuo.batch.builder;


import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.repository.dao.MapExecutionContextDao;
import org.springframework.batch.core.repository.dao.MapJobExecutionDao;
import org.springframework.batch.core.repository.dao.MapJobInstanceDao;
import org.springframework.batch.core.repository.dao.MapStepExecutionDao;
import org.springframework.batch.core.repository.support.SimpleJobRepository;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.maizuo.batch.AbstractBatchJob;
import com.maizuo.batch.BaseBatchJob;


/**
 * 基于spring batch封装的批任务处理。
 * 特别说明:
 * 0.单例,静态内部类。同名的Job不会重复执行。
 * 1.使用内存方式In-memory Repository
 * 2.每次Job任务都是单个step,暂时不支持多step操作。
 */
public class BatchJobBuilder {

    private static JobBuilder builder;


    private static BatchJobBuilder mzBatchJobBuilder;

    private BatchJobBuilder() {}


    private static class JobBuilder {

        private SimpleJobRepository jobRepository;
        private JobBuilderFactory jobBuilderFactory;
        private StepBuilderFactory stepBuilderFactory;
        private Step step;
        private AbstractJob job;
        private SimpleJobLauncher jobLauncher;

        protected JobBuilder() {
            jobRepository = new SimpleJobRepository(new MapJobInstanceDao(), new MapJobExecutionDao(), new MapStepExecutionDao(), new MapExecutionContextDao());
            jobBuilderFactory = new JobBuilderFactory(jobRepository);
            stepBuilderFactory = new StepBuilderFactory(jobRepository, new ResourcelessTransactionManager());
            jobLauncher = new SimpleJobLauncher();
        }

        protected <I, O> JobBuilder buildStep(AbstractBatchJob<I, O> job, int chunkSize, String stepName) {

            step = stepBuilderFactory.get(stepName)
                    .<I, O>chunk(chunkSize)
                    .reader(job)
                    .processor(job)
                    .writer(job)
                    .build();

            return this;

        }


        protected JobBuilder buildJob(AbstractBatchJob mzjob, String jobName) {
            job = (AbstractJob) jobBuilderFactory.get(jobName)
                    .listener(mzjob)
                    .flow(step)
                    .end()
                    .build();


            return this;
        }

        protected void run() {
            jobLauncher.setJobRepository(jobRepository);
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
			executor.setMaxPoolSize(100);
			executor.setQueueCapacity(3);
			executor.initialize();
            jobLauncher.setTaskExecutor(executor);

            try {
                jobLauncher.run(job, new JobParameters());
            } catch (JobExecutionAlreadyRunningException e) {
                e.printStackTrace();
            } catch (JobRestartException e) {
                e.printStackTrace();
            } catch (JobInstanceAlreadyCompleteException e) {
                e.printStackTrace();
            } catch (JobParametersInvalidException e) {
                e.printStackTrace();
            }
        }

    }

    public static BatchJobBuilder getInstance() {
        if (mzBatchJobBuilder == null) {
            mzBatchJobBuilder = new BatchJobBuilder();
            builder = new JobBuilder();
        }

        return mzBatchJobBuilder;
    }

    public static void startJob(BaseBatchJob<?, ?> job) {
        builder.buildStep(job, job.chunkSize, job.stepName)
                .buildJob(job, job.jobName)
                .run();
    }


}
