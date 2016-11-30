package com.ssc.auth.dao;

import java.util.List;

import com.ssc.auth.entity.PartitionInfo;

public interface PartitionInfoDao {

	Integer save( PartitionInfo userPartition );
	
	List<PartitionInfo> findByMerchant( String merchant );
}
