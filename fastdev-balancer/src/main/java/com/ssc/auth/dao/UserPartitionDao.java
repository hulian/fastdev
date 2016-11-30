package com.ssc.auth.dao;

import com.ssc.auth.entity.UserPartition;

public interface UserPartitionDao {

	Integer save( UserPartition userPartition );
	
	UserPartition findByUserName( String userName );
}
