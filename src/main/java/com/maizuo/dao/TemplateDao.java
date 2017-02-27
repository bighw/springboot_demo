package com.maizuo.dao;

import com.maizuo.data.entity.user.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author qiyang
 * @ClassName: TestDao
 * @Description: 测试dao
 * @Email qiyang@maizuo.com
 * @date 2016/8/17 0017
 */
@Repository
public class TemplateDao {
    @Autowired
    JdbcTemplate jdbcTemplateUser;

    @Autowired
    JdbcTemplate jdbcTemplateCard;

    public User queryUserById(int id) throws DataAccessException {
        String sql = "select id,name,sex from t_user_info where id=?";
        SqlRowSet rs = jdbcTemplateUser.queryForRowSet(sql, new Object[]{id});
        if (rs != null && rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            // user.setSex(Byte.parseByte(rs.getString("sex")));
            return user;
        }
        return null;
    }

    public List<User> searchUser(String startTime, String endTime, int page, int pageSize) throws DataAccessException {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select id,name from t_user_info");
        if (StringUtils.isNotBlank(startTime) || StringUtils.isNotBlank(endTime)) {
            sqlBuf.append(" where 1=1 ");
        }
        List<Object> parmas = new ArrayList<Object>();
        if (StringUtils.isNotBlank(startTime)) {
            sqlBuf.append("and create_at>= ? ");
            parmas.add(startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            sqlBuf.append("and create_at<= ? ");
            parmas.add(endTime);
        }
        if (page != 0) {
            sqlBuf.append(" limit ?,? ");
            parmas.add(page - 1);
            parmas.add(pageSize);
        }
        String sql = sqlBuf.toString();
        SqlRowSet rs = jdbcTemplateUser.queryForRowSet(sql, parmas.toArray());
        List<User> list = new ArrayList<User>();
        while (rs != null & rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            list.add(user);
        }
        return list;
    }

    public int queryUserTotal(String startTime, String endTime, int page, int pageSize) throws DataAccessException {
        StringBuffer sqlBuf = new StringBuffer();
        sqlBuf.append("select count(Id) from t_user_info");
        if (StringUtils.isNotBlank(startTime) || StringUtils.isNotBlank(endTime)) {
            sqlBuf.append(" where 1=1 ");
        }
        List<Object> parmas = new ArrayList<Object>();
        if (StringUtils.isNotBlank(startTime)) {
            sqlBuf.append("and create_at>= ? ");
            parmas.add(startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            sqlBuf.append("and create_at<= ? ");
            parmas.add(endTime);
        }
        if (page != 0) {
            sqlBuf.append(" limit ?,? ");
            parmas.add(page - 1);
            parmas.add(pageSize);
        }
        String sql = sqlBuf.toString();
        SqlRowSet rs = jdbcTemplateUser.queryForRowSet(sql, parmas.toArray());
        while (rs != null & rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }
}
