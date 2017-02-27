package com.maizuo.web.controller.goods;

import com.hyx.zookeeper.MaizuoLogUtil;
import com.maizuo.constants.Constants;
import com.maizuo.constants.RouteConstants;
import com.maizuo.kit.goods.response.GoodsInfoResponse;
import com.maizuo.data.enums.ErrorCode;
import com.maizuo.service.GoodsService;
import com.maizuo.api3.commons.domain.Result;
import com.maizuo.api3.commons.exception.MaizuoException;
import com.maizuo.tools.TimeLog;
import com.maizuo.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rose
 * @ClassName: GoodsController
 * @Email rose@maizuo.com
 * @create 2017/1/16-13:32
 * @Description: 从内部其他系统获取商品详情数据的demo
 */
@RestController
@RequestMapping(value =  RouteConstants.GOODS_ROUTE)
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    //@MZMock("mock_api_goods_info")
    public Result info(@RequestParam int goodsId, @RequestParam int channleId) {
        String loghead = RequestUtils.getRequestId() + "hello测试:";
        String logInterface = "test_api_goods_info";
        TimeLog timeLog = new TimeLog();

        GoodsInfoResponse goodsInfo = null;
        try {
            goodsInfo = goodsService.queryGoodsInfo(goodsId, channleId);
        } catch (MaizuoException e) {
            e.printStackTrace();
            return new Result(e.getStatus(), e.getMsg());
        }
        MaizuoLogUtil.writeLog(Constants.SYSTEMID, Constants.SYSTEMID, goodsId+"", logInterface, "0", "", timeLog.totalTime(), "0");
        return new Result(ErrorCode.SUCCESS.getCode(), goodsInfo, ErrorCode.SUCCESS.getMsg());
    }
}
