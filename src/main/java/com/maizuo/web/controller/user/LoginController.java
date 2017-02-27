package com.maizuo.web.controller.user;

import com.maizuo.api3.commons.util.MaizuoLogUtils;
import com.maizuo.constants.Constants;
import com.maizuo.constants.RouteConstants;
import com.maizuo.data.entity.user.User;
import com.maizuo.data.enums.ErrorCode;
import com.maizuo.api3.commons.domain.Result;
import com.maizuo.api3.commons.exception.MaizuoException;
import com.maizuo.data.request.LoginRequest;
import com.maizuo.data.response.LoginResponse;
import com.maizuo.data.response.LoginV2Response;
import com.maizuo.service.LoginService;
import com.maizuo.tools.TimeLog;
import com.maizuo.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author rose
 * @ClassName: LoginController
 * @Email rose@maizuo.com
 * @create 2017/1/13-11:45
 * @Description: 模板
 */

@RestController
@RequestMapping(value =  RouteConstants.USER_ROUTE)
public class LoginController {

    @Autowired
    LoginService loginService;

    /**
     * 示例: 多参数接收普通表单提交
     * @param userName    用户名
     * @param password    密码
     * @param picCode     图形验证码
     * @param type         登录类型  测试整型数据自动绑定
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    //@MZMock("mock_api_user_login")
    public Result login(String userName, String password, String picCode, @RequestParam(required = false, defaultValue = "0") int type) throws Exception {
        String loghead = RequestUtils.getRequestId() + "hello测试:";
        String logInterface = "test_api_user_login";
        TimeLog timeLog = new TimeLog();

        LoginResponse loginResponse = null;
        try {
            loginResponse = loginService.login(userName,password,picCode);
        } catch (MaizuoException e) {
            return new Result(e.getStatus(), e.getMsg());
        }
        if(null == loginResponse){
            return new Result(ErrorCode.EMPTY_LIST.getCode(), "暂无数据~");
        }
        MaizuoLogUtils.writeLog(Constants.SYSTEMID, Constants.SYSTEMID,userName+"", logInterface, "0", "","0", timeLog.totalTime(), "0");
        return new Result(ErrorCode.SUCCESS.getCode(), loginResponse, ErrorCode.SUCCESS.getMsg());
    }

    /**
     * 示例: 实体Bean 接收表单提交，支付普通表单GET,POST请求
     * @param loginRequest
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login/v2", method = RequestMethod.POST)
    //@MZMock("mock_api_user_login")
    public Result login2(LoginRequest loginRequest) throws Exception {
        String loghead = RequestUtils.getRequestId() + "hello测试:";
        String logInterface = "test_api_user_login";
        TimeLog timeLog = new TimeLog();

        LoginV2Response loginV2Response = null;
        try {
            loginV2Response = loginService.login(loginRequest);
        } catch (MaizuoException e) {
            return new Result(e.getStatus(), e.getMsg());
        }
        if(null == loginV2Response){
            return new Result(ErrorCode.EMPTY_LIST.getCode(), "暂无数据~");
        }
        MaizuoLogUtils.writeLog(Constants.SYSTEMID, Constants.SYSTEMID, loginRequest.getName() +"", logInterface, "0", "","0", timeLog.totalTime(), "0");
        return new Result(ErrorCode.SUCCESS.getCode(), loginV2Response, ErrorCode.SUCCESS.getMsg());
    }

    /**
     * 示例: 实体Bean 接收POST Json 参数
     * @param loginRequest
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login/v3", method = RequestMethod.POST)
    //@MZMock("mock_api_user_login")
    public Result login3(@RequestBody LoginRequest loginRequest) throws Exception {
        String loghead = RequestUtils.getRequestId() + "hello测试:";
        String logInterface = "test_api_user_login";
        TimeLog timeLog = new TimeLog();

        LoginV2Response loginV2Response = null;
        try {
            loginV2Response = loginService.login(loginRequest);
        } catch (MaizuoException e) {
            return new Result(e.getStatus(), e.getMsg());
        }
        if(null == loginV2Response){
            return new Result(ErrorCode.EMPTY_LIST.getCode(), "暂无数据~");
        }
        MaizuoLogUtils.writeLog(Constants.SYSTEMID, Constants.SYSTEMID, loginRequest.getName() +"", logInterface, "0", "","0", timeLog.totalTime(), "0");
        return new Result(ErrorCode.SUCCESS.getCode(), loginV2Response, ErrorCode.SUCCESS.getMsg());
    }

}
