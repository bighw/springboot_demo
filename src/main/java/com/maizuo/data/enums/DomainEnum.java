package com.maizuo.data.enums;

import com.maizuo.config.MZConfig;
import com.maizuo.api3.commons.util.LogUtils;
import com.maizuo.api3.commons.util.StringUtils;

/**
 * @author qiyang
 * @ClassName: DomainEnum
 * @Description: 域名枚举
 * @Email qiyang@maizuo.com
 * @date 2016/8/9 0009
 */
public enum DomainEnum {
    IM_DOMAIN("http://im.maizuo.com"),
    SMS_DOMAIN("http://sms.maizuo.com"),
    NEWOC_IN_DOMAIN("http://innewoc.maizuo.com"),
    SEAT_IN_DOMAIN("http://inseat.maizuo.com"),
    SENSITIVE_DOMAIN("http://sensitive.maizuo.com"),
    GOODS_DOMAIN("http://goods.maizuo.com"),
    ACTIVE_DOMAIN("http://active.maizuo.com"),
    PUSH_DOMAIN("http://push.maizuo.com");

    String host;

    DomainEnum(String host) {
        this.host = host;
    }

    public String getHost() {
        String host = MZConfig.getInstance().getString(this.name());
        if (StringUtils.isEmpty(host)) {
            LogUtils.debug("===use local host===" + this.host);
            host = this.host;
        }
        return host;
    }

}
