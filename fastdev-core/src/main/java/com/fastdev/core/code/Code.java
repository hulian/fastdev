package com.fastdev.core.code;

import java.util.HashMap;
import java.util.Map;

public enum Code {

	SUCCESS(1, "success"),
	UNKNOWN_ERROR(0,"unknown error"),
	HANDLER_EMPTY(-1,"handler is empty"),
	TOKEN_EMPTY(-2,"token is empty"),
	HANDLER_NOT_FOUND(-3,"handler not found"),
	SESSION_EMPTY(-4,"session empty"),
	ACCESS_DINIED(-5,"access dinied"), 
	SESSION_EXPIRED(-6,"session expired");
	
	private static final Map<Integer, CodeObject> cache = new HashMap<>();

	private final int code;
	private final String msg;

	Code(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	Code(int code, String msg,String param) {
		this.code = code;
		this.msg = msg+param;
	}

	public int getCode() {
		return this.code;
	}

	public String getMsg() {
		return this.msg;
	}
	
	public CodeObject toObject(){
		
		CodeObject codeObject = cache.get(code);
		
		if(codeObject==null){
			codeObject=new CodeObject(code, msg);
			cache.put(code, codeObject);
		}
		
		return codeObject;
	}
	
	public CodeObject toObject(String extraMsg){
		
		CodeObject codeObject = cache.get(code);
		
		if(codeObject==null){
			codeObject=new CodeObject(code, msg+":"+extraMsg);
			cache.put(code, codeObject);
		}
		
		return codeObject;
	}
	
	public static class CodeObject{
		
		private final int code;
		private final String msg;

		CodeObject(int code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		CodeObject(int code, String msg,String param) {
			this.code = code;
			this.msg = msg+param;
		}

		public int getCode() {
			return this.code;
		}

		public String getMsg() {
			return this.msg;
		}
	}
}
