package com.maizuo.service;


import com.maizuo.api3.commons.util.codec.Md5;
import com.maizuo.config.MZConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyx.nsqpool.NsqClientProxy;

import com.maizuo.constants.MQConstants;
import com.maizuo.constants.RedisConstants;
import com.maizuo.dao.LoginDao;
import com.maizuo.data.entity.user.User;
import com.maizuo.data.enums.ErrorCode;
import com.maizuo.api3.commons.exception.MaizuoException;
import com.maizuo.data.request.LoginRequest;
import com.maizuo.data.response.LoginResponse;
import com.maizuo.data.response.LoginV2Response;
import com.maizuo.redis.RateLimitRedis;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rose
 * @ClassName: LoginService
 * @Description: serverice模板，建议入参为三个以上采用对象传入
 * @Email rose@maizuo.com
 * @date 2016/8/17 0017
 */
@Service
public class LoginService {
    @Autowired
    LoginDao loginDao;
    @Autowired
    RateLimitRedis rateLimitRedis;

    /**
     * 示例: 多字段入参 登录方法
     * @param name   用户名
     * @param password  密码
     * @param picCode    图形验证码
     * @return
     * @throws Exception
     */
    public LoginResponse login(String name,String password, String picCode) throws Exception{

        if(StringUtils.isBlank(name)) {
            throw new MaizuoException(ErrorCode.ILLEGAL_PARAM.getCode(), "用户名不能为空");
        }
        if(StringUtils.isBlank(password)) {
            throw new MaizuoException(ErrorCode.ILLEGAL_PARAM.getCode(), "密码不能为空");
        }
        if(StringUtils.isBlank(picCode) || !"123456".equals(picCode)){
            throw new MaizuoException(ErrorCode.FAIL_INVALID_IMAGE.getCode(), "图形验证码错误");
        }
        if (!rateLimitRedis.checkRateBySeriallyTime(RedisConstants.REDIS_USER_LOGIN_RATE+name,5,60)) {
            throw new MaizuoException(ErrorCode.REQUEST_FREQUENTLY.getCode(), ErrorCode.REQUEST_FREQUENTLY.getMsg());
        }
        //得到加盐后的密码
        String ciphertext = this.getCiphertext(name, password);
        User user = null;
        try {
            //调用DAO层根据账号密码获取用户信息
            user = loginDao.login(name, ciphertext);

        } catch (Exception e) {
            e.printStackTrace();
            throw new MaizuoException(-1, "出错了，请重新请求了");
        }
        //用户名或密码错误
        if(user==null){
            rateLimitRedis.rateIncrBySeriallyTime(RedisConstants.REDIS_USER_LOGIN_RATE+name,5,60);
            throw new MaizuoException(1100001, "账号或密码错误");
        }
        // 登录成功之后，写入mq 对应的topic
        ObjectMapper mapper = new ObjectMapper();
        NsqClientProxy.publishMessage(MQConstants.MQ_USER_LOGIN_TOPIC,mapper.writeValueAsString(user) );
        rateLimitRedis.clearRate(RedisConstants.REDIS_USER_LOGIN_RATE+name);

        //组装目标数据
        LoginResponse loginResponse = new LoginResponse();
        BeanUtils.copyProperties(loginResponse,user);
        return loginResponse;


    }



    /**
     * 示例: 入参 表单的 LoginForm对象的 JavaBean
     * @param loginRequest   用户登录表单实体类
     * @return
     * @throws Exception
     */
    public LoginV2Response login(LoginRequest loginRequest) throws Exception{
        String name = loginRequest.getName();
        String password = loginRequest.getPassword();
        if(StringUtils.isBlank(name)) {
            throw new MaizuoException(ErrorCode.ILLEGAL_PARAM.getCode(), "用户名不能为空");
        }
        if(StringUtils.isBlank(password)) {
            throw new MaizuoException(ErrorCode.ILLEGAL_PARAM.getCode(), "密码不能为空");
        }

        if(StringUtils.isBlank(loginRequest.getPicCode()) || !"123456".equals(loginRequest.getPicCode())){
            throw new MaizuoException(ErrorCode.FAIL_INVALID_IMAGE.getCode(), "图形验证码错误");
        }
        if (!rateLimitRedis.checkRateBySeriallyTime(RedisConstants.REDIS_USER_LOGIN_RATE+name,5,60)) {
            throw new MaizuoException(ErrorCode.REQUEST_FREQUENTLY.getCode(), ErrorCode.REQUEST_FREQUENTLY.getMsg());
        }
        //得到加盐后的密码
        String ciphertext = this.getCiphertext(name, password);
        User user2 = null;
        try {
            //调用DAO层根据账号密码获取用户信息
            user2 = loginDao.login(name, ciphertext);

        } catch (Exception e) {
            e.printStackTrace();
            throw new MaizuoException(-1, "出错了，请重新请求了");
        }
        //用户名或密码错误
        if(user2 == null){
            rateLimitRedis.rateIncrBySeriallyTime(RedisConstants.REDIS_USER_LOGIN_RATE+name,5,60);
            throw new MaizuoException(1100001, "账号或密码错误");
        }
        // 登录成功之后，写入mq 对应的topic
        ObjectMapper mapper = new ObjectMapper();
        NsqClientProxy.publishMessage(MQConstants.MQ_USER_LOGIN_TOPIC,mapper.writeValueAsString(user2) );
        rateLimitRedis.clearRate(RedisConstants.REDIS_USER_LOGIN_RATE+name);

        //组装目标数据
        LoginV2Response loginV2Response = new LoginV2Response();
        BeanUtils.copyProperties(loginV2Response,user2);;
        return loginV2Response;

    }


    /**
     *  登录密码加盐处理
     * @param name     用户名
     * @param password  密码
     * @return      加盐处理后的密文
     */
    private String getCiphertext(String name,String password){
        //密码加盐
        //前端传过来的密码值为明文密码的Md5值，需要转换成加盐后的密文
        //规则如下：
        //第一步：将用户名MD5拼接密码再拼接加盐秘钥的Md5值
        //第二步  将拼接后的字符串再一次MD5
        //例如用户名  hotdog    密码  0b5029d1515b4ff68c52ca56af6c6795   加盐秘钥  ktysi74_@k
        //加密方式如下：  Md5.getVal_UTF8(Md5.getVal_UTF8("hotdog")+"0b5029d1515b4ff68c52ca56af6c6795"+Md5.getVal_UTF8("ktysi74_@k"));
        return  Md5.getVal_UTF8(Md5.getVal_UTF8(name)+password+Md5.getVal_UTF8(MZConfig.getString("USER_LOGIN_CONFUSED_KEY")));
    }

}
