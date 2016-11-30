package com.fastdev.balancer.dao;

import com.fastdev.balancer.entity.UserPartition;

public interface UserPartitionDao {

	Integer save( UserPartition userPartition );
	
	UserPartition findByUserName( String userName );
}
