package com.maizuo.utils;

import com.maizuo.api3.commons.util.LogUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

/**
 * @author qiyang
 * @ClassName: Util
 * @Description: 常用工具类
 * @Email qiyang@maizuo.com
 * @date 2016/8/15 0015
 */
public class Util {

    /**
     * 获取指定区间不重复的n个数字
     *
     * @param min
     * @param max
     * @param n
     * @return
     */
    public static int[] getRandomNoRepeat(int min, int max, int n) {
        if (n <= 0) {
            return new int[0];
        }
        if (n > max - min + 1) {
            n = max - min + 1;
        }
        int[] arr = new int[max - min + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        for (int i = 0; i < n; i++) {
            int index = (int) (Math.random() * arr.length);
            int tmp = arr[i];
            arr[i] = arr[index];
            arr[index] = tmp;
        }
        int[] result = new int[n];
        for (int i = 0; i < result.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    private static Logger logger = Logger.getLogger(LogUtils.Default_Logger);

    public static void logExceptionStack(Exception e) {
        logger.error("exception==stack==", e);
    }

    /**
     * 随机数字串
     *
     * @param length
     * @return
     */
    public static String genRandomNum(int length) {
        String random = RandomStringUtils.randomNumeric(length);
        return random;
    }

    /**
     * 随机大小字母+数字串
     *
     * @param length
     * @return
     */
    public static String genCharNumRandom(int length) {
        String random = RandomStringUtils.randomAlphanumeric(length);
        return random;
    }

    /**
     * 随机小写字母+数字串
     *
     * @param length
     * @return
     */
    public static String genLowerCharNumRandom(int length) {
        String random = RandomStringUtils.randomAlphanumeric(length).toLowerCase();
        return random;
    }

    /**
     * 随机小写字母串
     *
     * @param length
     * @return
     */
    public static String genLowerCharRandom(int length) {
        String random = RandomStringUtils.randomAlphabetic(length).toLowerCase();
        return random;
    }

    /**
     * 随机大写字母串
     *
     * @param length
     * @return
     */
    public static String genUpperCharRandom(int length) {
        String random = RandomStringUtils.randomAlphabetic(length).toUpperCase();
        return random;
    }

}
