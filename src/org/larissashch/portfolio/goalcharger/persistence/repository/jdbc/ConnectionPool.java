package org.larissashch.portfolio.goalcharger.persistence.repository.jdbc;

import java.sql.Connection;

public interface ConnectionPool {

	Connection getConnection();
	void releaseConnection(Connection connection);
	void close();

}