package com.maizuo.web.controller.card;

import com.maizuo.api3.commons.domain.Result;
import com.maizuo.api3.commons.exception.MaizuoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.maizuo.constants.RouteConstants;
import com.maizuo.service.BatchJobService;

/**
 * @author paul
 * @ClassName: CardController
 * @Email paul@maizuo.com
 * @create 2017/1/17-16:17
 * @Description: 卡相关业务controller
 */
@RestController
@RequestMapping(value = RouteConstants.CARD_ROUTH)
public class CardController {

    @Autowired
    BatchJobService batchJobService;

	@RequestMapping(method = RequestMethod.POST)
	public Result createCard() {
		try{
			boolean isCreated = batchJobService.createCardJob();
			if(isCreated){
				return new Result(0,"success");
			}else{
				return new Result(-1,"系统异常");
			}
		}catch(MaizuoException e){
			e.printStackTrace();
			return new Result(e.getStatus(),e.getMsg());
		}
	}
}
