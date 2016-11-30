package com.fastdev.balancer.service;

import com.fastdev.balancer.entity.UserPartition;

public interface AllocateService {
	
	UserPartition allocate( String userName , String merchant );
	
}
