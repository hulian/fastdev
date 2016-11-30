package com.fastdev.core.transaction;

import java.sql.Connection;

public interface TransactionManager {

	Connection getConnection();
	void start();
	void commit();
	void rollback();
	void stop();
	void doInTransaction( InTransaction runable );
	void autoConnection(InTransaction runable);
}
