/**
 * 2017年1月18日 上午10:43:39  @author paul
 * <p>TODO</P>
 * @version TODO
 */
package com.maizuo.data.entity.job;

/**
 * @ClassName:BatchJob
 * @Description:()
 * @Email:paul@hyx.com
 * @author paul  
 * @date 2017年1月18日 上午10:43:39 
 */
public class BatchJob {

	private int id;
	private String jobName;
	private int type;
	private String batch;
	private int status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
