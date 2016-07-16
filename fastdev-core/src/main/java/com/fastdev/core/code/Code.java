package com.fastdev.core.code;

public enum Code {

	UNKNOWN_ERROR(0,"unknown error"),
	SUCCESS(1, "success"),
	HANDLER_EMPTY(2,"handler is empty"),
	TOKEN_EMPTY(3,"token is empty"),
	HANDLER_NOT_FOUND(4,"handler not found"),
	SESSION_EMPTY(5,"session empty"),
	ACCESS_DINIED(6,"access dinied");

	private final int id;
	private final String msg;

	Code(int id, String msg) {
		this.id = id;
		this.msg = msg;
	}
	
	Code(int id, String msg,String param) {
		this.id = id;
		this.msg = msg+param;
	}

	public int getId() {
		return this.id;
	}

	public String getMsg() {
		return this.msg;
	}
	
	public String getIdAndMsg() {
		return this.id+","+this.msg;
	}
	
	public String getIdAndMsg( String extraMsg) {
		return this.id+","+this.msg+","+extraMsg;
	}
}
