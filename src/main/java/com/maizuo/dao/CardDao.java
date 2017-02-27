/**
 * 2017年1月17日 上午10:19:11  @author paul
 * <p>TODO</P>
 * @version TODO
 */
package com.maizuo.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @ClassName:CardDao
 * @Description:(批量更新数据的范例)
 * @Email:paul@hyx.com
 * @author paul  
 * @date 2017年1月17日 上午10:19:11 
 */
@Repository
public class CardDao {

	@Autowired
    JdbcTemplate jdbcTemplateCard;
	
	/**
	 * 
	 * @Description:TODO(创建空白卡)
	 * @param cardNoList
	 * @param batch
	 * @return  参数
	 * int  返回类型
	 */
	public int initCard(String batch, List<String> cardNoList) throws DataAccessException{
		StringBuffer sql = new StringBuffer("insert into t_card_info(batch_no,card_no,password) values ");
		List<Object> params = new ArrayList<Object>();
		int index =0;
		for(String cardNo:cardNoList){
			if(index!=0) {
				sql.append(",");
			}
			sql.append("(?,?,?)");
			params.add(batch);
			params.add(cardNo);
			params.add("111111");
			index ++;
		}
		int row = jdbcTemplateCard.update(sql.toString(),params.toArray());
		return row;
	}

	/**
	 *
	 * @Description:TODO(创建空白卡,关注rewriteBatchedStatements配置和JDBC驱动程序的版本)
	 * @param cardNoList
	 * @param batch
	 * @return  参数
	 * int  返回类型
	 */
	public int[] batchAddCard(final String batch, final List<String> cardNoList) throws DataAccessException{
		StringBuffer sql = new StringBuffer("insert into t_card_info(batch_no,card_no,password) values(?,?,?)");

		int row[] = jdbcTemplateCard.batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				String cardNo = cardNoList.get(i);
				ps.setString(1,batch);
				ps.setString(2,cardNo);
				ps.setString(3,"111111");
			}

			@Override
			public int getBatchSize() {
				return cardNoList.size();
			}
		});
		return row;
	}



}
