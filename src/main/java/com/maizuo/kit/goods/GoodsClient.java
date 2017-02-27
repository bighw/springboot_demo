package com.maizuo.kit.goods;

import com.maizuo.api3.commons.exception.MaizuoException;
import com.maizuo.kit.goods.response.GoodsInfoResponse;
import com.maizuo.kit.goods.request.GoodsInfoRequest;
import org.springframework.stereotype.Component;

/**
 * @author rose
 * @ClassName: GoodsClient
 * @Email rose@maizuo.com
 * @create 2017/1/16-11:14
 * @Description: 无SDK的情况下调用第三方服务
 */
@Component
public class GoodsClient {

    public GoodsInfoResponse queryGoodsInfo(GoodsInfoRequest goodsInfoRequest) throws MaizuoException {

//        根据商品和渠道Id从商品系统获取商品详情信息
//        本范例调用代码因为涉及其他系统，故将调用和解析直接注释，采用hardcode模式访问数据
//        Map<String,Object> params = goodsInfoRequest.getTextParams();
//        String url = MZConfig.getString("Goods_DOMAIN_IN") + "/api/goods/info";
//        HttpResult httpResult = HttpUtils.doGet(url,params,1500,1500);
        GoodsInfoResponse goodsInfo = new GoodsInfoResponse();
        goodsInfo.setId(1);
        goodsInfo.setName("保利影院3D电子票");
        goodsInfo.setPrice(5000);
        goodsInfo.setIcon("https://pic.maizuo.com/usr/100003495/fbf7fc6a76fa9c696edcedfe856089ba.jpg");
        goodsInfo.setDesc("商品描述");
        return goodsInfo;
    }
}
