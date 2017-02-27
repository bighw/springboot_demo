package com.maizuo.data.entity;

import com.maizuo.api3.commons.mapping.ApiField;

/**
 * @author rose
 * @ClassName: ClientHeader
 * @Email rose@maizuo.com
 * @create 2017/1/16-21:34
 * @Description: 模板
 */
public class ClientHeader {
    @ApiField("i")
    private String clientIp;

    @ApiField("c")
    private int cityId;


    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
