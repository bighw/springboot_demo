package com.maizuo.data.response;

/**
 * @author rose
 * @ClassName: LoginResponse
 * @Email rose@maizuo.com
 * @create 2017/2/5-17:59
 * @Description: 模板
 */
public class LoginV2Response {
    private int id;
    private String name;
    private byte sex;
    private String nickName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
