package com.maizuo.web.controller;
import com.maizuo.data.entity.Page;
import com.maizuo.api3.commons.domain.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rose
 * @ClassName: BaseController
 * @Email rose@maizuo.com
 * @create 2017/1/10-16:40
 * @Description: 基础控制器，提供公共方法
 */
public class BaseController {

    public Result returnOk(){
        return new Result(0,"ok");
    }

    public Result returnPageingOK(int total,int current,String keyName,List objs){
        Map<String,Object>  map = new HashMap<String,Object>();
        map.put("page",new Page(total,current));
        map.put(keyName,objs);
        return new Result(0,map,"ok");
    }
}
