package com.maizuo.kit.goods.request;

import com.maizuo.api3.commons.domain.MaizuoRequest;
import com.maizuo.api3.commons.util.date.DateStyle;
import org.apache.http.client.utils.DateUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rose
 * @ClassName: GoodsInfoRequest
 * @Email rose@maizuo.com
 * @create 2017/1/16-11:45
 * @Description: 商品详情信息请求类
 */
public class GoodsInfoRequest implements MaizuoRequest {


    //商品ID
    private int goodsId;
    //渠道id
    private int channelId;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public Map<String, Object> getTextParams() {
        Map<String,Object>   map = new HashMap<String,Object>();
        map.put("goodsId",goodsId);
        map.put("channelId",channelId);
        String timestamp = DateUtils.formatDate(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS.getValue());
        map.put("datetime", timestamp);

        return map;
    }

    @Override
    public String getRequestBody() {
        return null;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }


}
