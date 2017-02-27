package com.maizuo.data.request;

/**
 * @author gavin
 * @ClassName:LoginForm
 * @Description:(登录表单实体)
 * @Email:gavin@hyx.com
 * @date 2017/1/20 17:29
 */
public class LoginRequest {

    private String name;       //用户名
    private String password;   //密码
    private byte sex;          //性别  0:女性(默认)  1:男性
    private String nickName;   //微信昵称
    private int type;          //是否需要记住密码 0:不需要(默认)  1:需要
    private String picCode;    //图形验证码


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte getSex() {
        return sex;
    }

    public void setSex(byte sex) {
        this.sex = sex;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPicCode() {
        return picCode;
    }

    public void setPicCode(String picCode) {
        this.picCode = picCode;
    }
}
