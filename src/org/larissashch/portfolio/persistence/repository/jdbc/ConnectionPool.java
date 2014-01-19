package org.larissashch.portfolio.persistence.repository.jdbc;

import java.sql.Connection;

public interface ConnectionPool {

	Connection getConnection();
	void releaseConnection(Connection connection);
	void close();

}