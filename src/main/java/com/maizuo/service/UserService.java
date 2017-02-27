package com.maizuo.service;

import com.maizuo.constants.Constants;
import com.maizuo.dao.UserDao;
import com.maizuo.data.entity.user.User;
import com.maizuo.api3.commons.exception.MaizuoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qiyang
 * @ClassName: TemplateService
 * @Description: 模板
 * @Email qiyang@maizuo.com
 * @date 2016/8/17 0017
 */
@Service
public class UserService {
    @Autowired
    UserDao userDao;

    /**
     * 根据用户ID获取用户基本信息
     * @param id
     * @return
     * @throws MaizuoException
     */
    public User queryUserById(int id) throws MaizuoException{
        if(id<=0) {
            throw new MaizuoException(1005, "用户Id为空");
        }
        if(id==2005){
            throw new MaizuoException(1014, "请求太频繁");
        }
        User user = null;
        try {
           user = userDao.queryUserById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MaizuoException(-1, "出错了，请重新请求了");
        }
        if(user==null){
            throw new MaizuoException(1100001, "用户不存在");
        }
         return user;

    }

    public List<User> searchUser(String startTime, String endTime, int page, int pageSize)
            throws MaizuoException{
        if(pageSize> Constants.MAX_PAGE_SIZE){
            throw new MaizuoException(1015, "单页数量过大");
        }

        List<User> userList = null;
        try {
            userList = userDao.searchUser(startTime, endTime, page, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MaizuoException(-1, "出错了，请重新请求了");
        }
        return userList;
    }

    public int queryUserTotal(String startTime, String endTime, int page, int pageSize)
            throws MaizuoException{
        int total = 0;
        try{
            total = userDao.queryUserTotal(startTime, endTime, page, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MaizuoException(-1, "出错了，请重新请求了");
        }
        return total;
    }
}
