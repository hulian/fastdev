package com.fastdev.balancer.service;

import com.fastdev.balancer.entity.UserPartition;

public interface RegisterService {
	
	boolean isUserExisted( String userName );

	UserPartition allocate( String userName , String merchant );
	
}
