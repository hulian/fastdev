package com.fastdev.core.util;

public class StringUtil {

	public static boolean isEmpty( String s ){
		if( s==null || "".equals(s.trim()) ){
			return true;
		}
		return false;
	}
	
	public static boolean isNotEmpty( String s ){
		return !isEmpty(s);
	}
}
