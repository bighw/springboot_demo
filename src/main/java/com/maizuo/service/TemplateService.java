package com.maizuo.service;

import com.maizuo.dao.TemplateDao;
import com.maizuo.data.entity.user.User;
import com.maizuo.api3.commons.exception.MaizuoException;
import com.maizuo.constants.Constants;
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
public class TemplateService {
    @Autowired
    TemplateDao templateDao;

    /**
     *
     * @param id
     * @return
     * @throws MaizuoException
     */
    public User queryUserById(int id) throws MaizuoException{

        User user = null;
        try {
           user = templateDao.queryUserById(id);
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
            userList = templateDao.searchUser(startTime, endTime, page, pageSize);
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
            total = templateDao.queryUserTotal(startTime, endTime, page, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MaizuoException(-1, "出错了，请重新请求了");
        }
        return total;
    }
}
