package com.fastdev.balancer.dao;

import java.util.List;

import com.fastdev.balancer.entity.PartitionInfo;

public interface PartitionInfoDao {

	Integer save( PartitionInfo userPartition );
	
	List<PartitionInfo> findByMerchant( String merchant );
}
