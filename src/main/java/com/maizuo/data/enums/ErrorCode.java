package com.maizuo.data.enums;

/**
 * @author qiyang
 * @ClassName: ErrorCode
 * @Description: 错误码
 * @Email qiyang@maizuo.com
 * @date 2016/8/16 0016
 */
public enum ErrorCode {
    SUCCESS(0, "success"),
    SYSTEM_BUSY(-1, "系统繁忙"),
    UNDEFINED(404, "资源不存在"),
    EXCEPTION(500, "系统异常"),
    ILLEGAL_IP(1001, "非法IP"),
    NORIGHT(1002, "权限不够"),
    SIGN_ERROR(1003, "签名串错误"),
    DECODE_ERROR(1004, "解密错误"),
    ILLEGAL_PARAM(1005, "请求参数非法"),
    INVALID_REQUEST(1006, "请求已失效"),
    DATA_PARSE_ERROR(1007, "数据解析异常"),
    ILLEGAL_FILE_FORMAT(1008, "非法文件格式"),
    INVALID_FILE_SIZE(1009, "无效文件大小"),
    FILE_NOT_NULL(1010, "文件内容不能为空"),
    ILLEGAL_URL(1011, "不合法URL"),
    FILE_UPLOAD_FAIL(1012, "文件上传失败"),
    CAN_NOT_REQUEST(1013, "接口暂时无法访问"),
    REQUEST_FREQUENTLY(1014, "请求太过频繁"),
    CACHE_UPDATE_FAIL(2001, "同步更新缓存失败"),
    EMPTY_LIST(6003, "空白的列表"),
    FAIL_INVALID_IMAGE(6006, "图形验证码错误"),
    FAIL_MESSAGE_CODE(6101, "短信验证码错误"),
    INVALID_MESSAGE_CODE(6102, "短信验证码失效"),

    REDIS_RETURN_ERROR(6103, "redis返回值有误");

    int code;
    String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
