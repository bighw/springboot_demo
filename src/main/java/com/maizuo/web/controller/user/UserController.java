package com.maizuo.web.controller.user;

import com.maizuo.api3.commons.util.LogUtils;
import com.maizuo.api3.commons.util.MaizuoLogUtils;
import com.maizuo.constants.RouteConstants;
import com.maizuo.web.controller.BaseController;
import com.maizuo.data.entity.user.User;
import com.maizuo.data.enums.ErrorCode;
import com.maizuo.api3.commons.domain.Result;
import com.maizuo.api3.commons.exception.MaizuoException;
import com.maizuo.service.UserService;
import com.maizuo.tools.TimeLog;
import com.maizuo.constants.Constants;
import com.maizuo.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value =  RouteConstants.USER_ROUTE)

/**
 * @author rose
 * @ClassName: UserController
 * @Email rose@maizuo.com
 * @create 2017/1/10-11:57
 * @Description: 从数据库获取用户数据，操作数据库，业务异常处理，分页范例
 */
public class UserController extends BaseController{
    @Autowired
    UserService userService;

    /**
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    //@MZMock("mock_api_user_query")
    public Result queryById(@RequestParam int userId) {
        String loghead = RequestUtils.getRequestId() + "hello测试:";
        String logInterface = "test_api_user_query";
        TimeLog timeLog = new TimeLog();
        if (userId<=0) {
            LogUtils.info(loghead + "response:" + "无效请求");
            MaizuoLogUtils.writeLog(Constants.SYSTEMID, Constants.SYSTEMID, userId+"", logInterface, "1", "","0", timeLog.totalTime(), "1");
            return new Result(ErrorCode.INVALID_REQUEST.getCode(), "", ErrorCode.INVALID_REQUEST.getMsg());
        }
        User user = null;
        try {
            user = userService.queryUserById(userId);
        } catch (MaizuoException e) {
            e.printStackTrace();
            return new Result(e.getStatus(), e.getMsg());
        }
        MaizuoLogUtils.writeLog(Constants.SYSTEMID, Constants.SYSTEMID, userId+"", logInterface, "0", "","0", timeLog.totalTime(), "0");
        return new Result(ErrorCode.SUCCESS.getCode(), user, ErrorCode.SUCCESS.getMsg());
    }

    /**
     *
     * @param startTime
     * @param endTime
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    //@MZMock("mock_api_user_query")
    public Result search(@RequestParam String startTime,@RequestParam String endTime,
                         @RequestParam int page,@RequestParam int pageSize) {
        String loghead = RequestUtils.getRequestId() + "hello测试:";
        String logInterface = "test_api_user_search";
        TimeLog timeLog = new TimeLog();
        if(pageSize> Constants.MAX_PAGE_SIZE){
            return new Result(1015, "单页数量过大");
        }
        int total =0;
        List<User> users = null;

        try {
             total = userService.queryUserTotal(startTime, endTime, page, pageSize);
             users = userService.searchUser(startTime, endTime, page, pageSize);
        } catch (MaizuoException e) {
            e.printStackTrace();
            return new Result(e.getStatus(), e.getMsg());
        }
        MaizuoLogUtils.writeLog(Constants.SYSTEMID, Constants.SYSTEMID, "", logInterface, "0", "", "0",timeLog.totalTime(), "0");
        return this.returnPageingOK(total,page,"users",users);
    }


}
