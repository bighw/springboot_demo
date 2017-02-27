package com.maizuo.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maizuo.constants.RouteConstants;
import com.maizuo.api3.commons.domain.Result;
import com.maizuo.api3.commons.exception.MaizuoException;
import com.maizuo.redis.JobTokenRedis;
import com.maizuo.service.BatchJobService;
import com.maizuo.service.CardService;

/**
 * Created by paul on 2017/1/18.
 */
@Controller
@RequestMapping(value = RouteConstants.BATCH_JOB_ROUTH)
public class BatchJobController {
    
    @Autowired
    private CardService cardService;
    
    @Autowired
    private JobTokenRedis jobTokenRedis;
    
    @Autowired
    private BatchJobService batchJobService;
    
    
	@ResponseBody
    @RequestMapping(value = "/{jobName}",method = RequestMethod.POST)
    public Result startJob (@PathVariable String jobName) {
        try{
        	boolean isStarted = batchJobService.startJob(jobName);
        	if(isStarted){
        		return new Result(0,"启动任务成功");
        	}else{
        		return new Result(-1,"系统异常");
        	}
        }catch(MaizuoException e){
        	e.printStackTrace();
        	return new Result(e.getStatus(),e.getMsg());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{jobName}",method = RequestMethod.DELETE)
    public Result stopJob (@PathVariable String jobName) {
    	try{
        	boolean isStoped = batchJobService.stopJob(jobName);
        	if(isStoped){
        		return new Result(0,"终止任务成功");
        	}else{
        		return new Result(-1,"系统异常");
        	}
        }catch(MaizuoException e){
        	e.printStackTrace();
        	return new Result(e.getStatus(),e.getMsg());
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/{jobName}",method = RequestMethod.GET)
    public Result jobStatus (@PathVariable String jobName) {
    	try{
    		 Map<String,String> map = batchJobService.jobStatus(jobName);
    		 return new Result(0, map, "success");
        }catch(MaizuoException e){
        	e.printStackTrace();
        	return new Result(e.getStatus(),e.getMsg());
        }
    }
}
