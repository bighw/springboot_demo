/**
 * 2017年1月17日 上午10:10:48  @author paul
 * <p>TODO</P>
 * @version TODO
 */
package com.maizuo.data.entity.card;

/**
 * @ClassName:Card
 * @Description:()
 * @Email:paul@hyx.com
 * @author paul  
 * @date 2017年1月17日 上午10:10:48 
 */
public class Card {

	private int id;
	private int batchNo;
	private String cardNo;
	private String password;
	private String createAt;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(int batchNo) {
		this.batchNo = batchNo;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCreateAt() {
		return createAt;
	}
	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}
}
