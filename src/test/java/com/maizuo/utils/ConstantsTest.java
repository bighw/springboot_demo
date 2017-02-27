package com.maizuo.utils;

import com.maizuo.common.SimpleBaseTest;
import com.maizuo.constants.Constants;
import org.junit.Test;

/**
 * @author qiyang
 * @ClassName: ConstantsTest
 * @Description: 测试系统常量
 * @Email qiyang@maizuo.com
 * @date 2016/8/25 0025
 */
public class ConstantsTest extends SimpleBaseTest {
    @Test
    public void printConstant() {
        System.out.println(Constants.SYSTEMID);
        System.out.println(Constants.SYSTEMNAME);
    }
}
