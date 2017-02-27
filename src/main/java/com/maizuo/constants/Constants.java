package com.maizuo.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author qiyang
 * @className Constants
 * @description 系统常量
 * @email qiyang@maizuo.com
 * @date 2016/9/2 0002 11:10
 */

@Component
@ConfigurationProperties(prefix = "system.constants")
public class Constants {
    //全局系统ID
    public static String SYSTEMID;
    //系统名称
    public static String SYSTEMNAME;

    public static void setSYSTEMID(String SYSTEMID) {
        Constants.SYSTEMID = SYSTEMID;
    }

    public static void setSYSTEMNAME(String SYSTEMNAME) {
        Constants.SYSTEMNAME = SYSTEMNAME;
    }

    public static final int MAX_PAGE_SIZE = 5000;


}
