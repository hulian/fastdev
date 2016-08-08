package com.fastdev.core.util;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import com.fastdev.core.code.Delimiter;

public class TokenUtil {
	
	public static final String TOKEN_COOKIE_NMAE="TOKEN";
	public static final int TOKEN_COOKIE_EXPIRE_TIME=1000*60*60*6;
	
	public static String createRandomToken(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static String createDistributeToken( String userName , String partition , List<String> roles , Timestamp createTime , String key){
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(userName);
		sb.append(Delimiter.TOKEN);
		for( String role : roles ){
			sb.append(role).append(Delimiter.ROLES);
		}
		sb.append(Delimiter.TOKEN);
		sb.append(createTime.getTime());
		sb.append(Delimiter.TOKEN);
		sb.append(partition);
		sb.append(Delimiter.TOKEN);
		
		try {
			String token = AESUtil.encrypt(sb.toString(), key);
			return token;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
