package com.ssc.auth.entity;

//会员分区信息
public class UserPartition {
	
	//用户ID
	private Integer userId;

	//用户名
	private String userName;
	
	//密码
	private String password;
	
	//所属代理商
	private String merchant;
	
	//分区ID
	private Integer partitionId;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public Integer getPartitionId() {
		return partitionId;
	}

	public void setPartitionId(Integer partitionId) {
		this.partitionId = partitionId;
	}

}
