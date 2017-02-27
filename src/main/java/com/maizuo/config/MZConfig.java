package com.maizuo.config;

import com.hyx.zookeeper.MaizuoConfig;
import com.hyx.zookeeper.ZooKeeperConfiguration;
import com.maizuo.constants.Constants;

/**
 * @author qiyang
 * @ClassName: MZConfig
 * @Description: 读取配置系统配置
 * @Email qiyang@maizuo.com
 * @date 2016/8/15 0015
 */
public class MZConfig {
    private static MaizuoConfig maizuoConfig = null;

    private MZConfig() {
    }

    public static ZooKeeperConfiguration getInstance() {

        System.out.println("sytemName:"+Constants.SYSTEMNAME);
        if (maizuoConfig == null) {
            synchronized (MZConfig.class) {
                if (maizuoConfig == null) {
                    maizuoConfig = MaizuoConfig.getInstance(Constants.SYSTEMNAME);
                }
            }

        }
        return maizuoConfig.getConfig();
    }


    public static int getInt(String name){
        return getInstance().getInt(name);
    }

    public static String getString(String name){
        return getInstance().getString(name);
    }

    public static long getLong(String name){
       return getInstance().getLong(name);
    }





}
