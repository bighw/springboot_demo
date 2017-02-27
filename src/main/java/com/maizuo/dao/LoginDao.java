package com.maizuo.dao;

import com.maizuo.data.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

/**
 * @author rose
 * @ClassName: LoginDao
 * @Email rose@maizuo.com
 * @create 2017/1/16-19:48
 * @Description: 登录业务操作数据库代码示例，涉及密码不可逆情况下的安全问题
 */
@Repository
public class LoginDao {
    @Autowired
    JdbcTemplate jdbcTemplateUser;



    public User login(String name,String password) throws DataAccessException {



        String sql = "select id,name,sex from t_user_info where name=? AND password=? ";
        SqlRowSet rs = jdbcTemplateUser.queryForRowSet(sql, new Object[]{name,password});
        if (rs != null && rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            // user.setSex(Byte.parseByte(rs.getString("sex")));
            return user;
        }
        return null;
    }


}
