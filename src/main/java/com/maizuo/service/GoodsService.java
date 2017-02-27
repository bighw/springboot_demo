package com.maizuo.service;

import com.maizuo.kit.goods.response.GoodsInfoResponse;
import com.maizuo.api3.commons.exception.MaizuoException;
import com.maizuo.kit.goods.request.GoodsInfoRequest;
import com.maizuo.kit.goods.GoodsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rose
 * @ClassName: GoodsService
 * @Email rose@maizuo.com
 * @create 2017/1/16-12:31
 * @Description: 商品业务类
 */
@Service
public class GoodsService {
    @Autowired
    GoodsClient goodsClient;

    public GoodsInfoResponse queryGoodsInfo(int goodsId, int channleId) throws MaizuoException{

        GoodsInfoRequest goodsInfoRequest = new GoodsInfoRequest();
        goodsInfoRequest.setChannelId(channleId);
        goodsInfoRequest.setGoodsId(goodsId);

        return goodsClient.queryGoodsInfo(goodsInfoRequest);

    }

}
