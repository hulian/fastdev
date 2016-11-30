package com.ssc.auth.entity;

//分区表
public class PartitionInfo {

	//分区ID
	private Integer partitionId;
	
	//会员数量
	private Integer userCount;
	
	//所属代理商
	private String merchant;
	
	//加密密钥
	private String key;

	public Integer getPartitionId() {
		return partitionId;
	}

	public void setPartitionId(Integer partitionId) {
		this.partitionId = partitionId;
	}

	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	
}
