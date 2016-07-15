package com.fastdev.core.handler;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class Handler implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6090576030921722549L;

	private String path;
	
	private String[] rolesAllowed;
	
	private Callable<?> handler;
	
	public Handler(String path,String[] rolesAllowed,Callable<?> handler) {
		this.path=path;
		this.rolesAllowed=rolesAllowed;
		this.handler=handler;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String[] getRolesAllowed() {
		return rolesAllowed;
	}

	public void setRolesAllowed(String[] rolesAllowed) {
		this.rolesAllowed = rolesAllowed;
	}

	public Callable<?> getHandler() {
		return handler;
	}

	public void setHandler(Callable<?> handler) {
		this.handler = handler;
	}
	
	
}
