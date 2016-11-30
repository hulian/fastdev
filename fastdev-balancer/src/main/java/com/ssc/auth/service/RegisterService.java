package com.ssc.auth.service;

import com.ssc.auth.entity.UserPartition;

public interface RegisterService {
	
	boolean isUserExisted( String userName );

	UserPartition allocate( String userName , String merchant );
	
}
