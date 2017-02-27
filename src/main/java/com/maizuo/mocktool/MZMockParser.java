package com.maizuo.mocktool;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.maizuo.api3.commons.util.StringUtils;
import com.maizuo.api3.commons.domain.Result;
import com.maizuo.config.MZConfig;

/**
 * @author qiyang
 * @ClassName: MockParser
 * @Description: 注解解析
 * @Email qiyang@maizuo.com
 * @date 2016/8/15 0015
 */
public class MZMockParser {
    public static String parse(MZMock mzMock) {
        String data = MZConfig.getInstance().getString(mzMock.value());
        if (StringUtils.isEmpty(data)) {
            data = "";
            return new Result(0, data, "success").toString();
        }
        try {
            JsonElement json = new JsonParser().parse(data);
            return new Result(0, json, "success").toString();
        } catch (Exception e) {
            //直接返回字符串
            return new Result(0, data, "success").toString();
        }
    }
}
