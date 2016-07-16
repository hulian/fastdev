package com.fastdev.core.util;

public class PackageNameUtil {

	public static String getDefaultPackageName(){
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		String className = stacks[stacks.length-1].getClassName();
		String defaultPackage = className.substring(0,className.lastIndexOf("."));
		return defaultPackage;
	}
}
