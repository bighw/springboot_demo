/**
 * 2017年1月17日 上午10:19:11  @author paul
 * <p>TODO</P>
 * @version TODO
 */
package com.maizuo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.maizuo.data.entity.job.BatchJob;

/**
 * @ClassName:BatchJobDao
 * @Description:()
 * @Email:paul@hyx.com
 * @author paul  
 * @date 2017年1月18日 上午10:41:11 
 */
@Repository
public class BatchJobDao {

	@Autowired
    JdbcTemplate jdbcTemplateCard;
	
	/**
	 * 
	 * @Description:TODO(创建job)
	 * @param BatchJob
	 * @return  参数
	 * boolean  返回类型
	 */
	public boolean createJob(BatchJob job){
		String sql ="insert into t_job_info(job_name,type,batch) values (?,?,?)";
		Object[] args = {job.getJobName(),job.getType(),job.getBatch()};
		int row = jdbcTemplateCard.update(sql, args);
		return row>0;
	}

	/**
	 * @Description:TODO(根据jobName获得job)
	 * @return  参数
	 * long  返回类型
	 */
	public BatchJob getBatchJob(String jobName) {
		BatchJob job = new BatchJob();
		String sql = "select * from t_job_info where job_name = ? ";
		Object[] args = {jobName};
		SqlRowSet rs = jdbcTemplateCard.queryForRowSet(sql,args);
		if(rs.next()){
			job.setId(rs.getInt("id"));
			job.setJobName(rs.getString("job_name"));
			job.setType(rs.getInt("type"));
			job.setBatch(rs.getString("batch"));
			job.setStatus(rs.getInt("status"));
			return job;
		}
		return null;
	}

	/**
	 * @Description:TODO(获得卡业务批次号最大值+1)
	 * @return  参数
	 * int  返回类型
	 */
	public String getNewBatchNo() {
		String sql = "select ifnull(max(batch+1),0) as batch from t_job_info where type=1 ";
		SqlRowSet rs = jdbcTemplateCard.queryForRowSet(sql);
		if(rs.next()){
			//sql返回
			return String.valueOf((int)rs.getDouble("batch"));
		}
		return null;
	}
	
}
