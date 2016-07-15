package com.fastdev.core.session;

public interface SessionService {

	/**
	 * 检查Session是否存在
	 * @return true 存在 ，fasle 不存在
	 */
	boolean hasSession( String token );
	
	/**
	 * 创建Session
	 * @return
	 */
	Session createSession( String userName );
	
	/**
	 * 删除Session
	 * @return
	 */
	void deleteSession( String token );
	
	
}
