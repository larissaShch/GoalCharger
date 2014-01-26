package org.larissashch.portfolio.goalchatge.persistence.repository.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReusableConnection implements Connection {
	private Connection connection;
	private AtomicBoolean busy;
	private AtomicBoolean closed;
	private String errorMessage;
	
	public ReusableConnection(Connection connection) {
		this.connection = connection;
		this.busy = new AtomicBoolean(true);
		this.closed = new AtomicBoolean(false);
		errorMessage = "Connection is unavailable.";
	}

	public void setBusy(boolean busy) {
		this.busy.set(busy);
	}

	public boolean isBusy() {
		return busy.get();
	}

	private Connection getConnection(){
		return connection;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ReusableConnection) {
			return this.connection.equals(((ReusableConnection) obj).getConnection());
		}
		if (obj instanceof Connection) {
			return this.connection.equals(obj);
		}
		return false;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {

		return connection.isWrapperFor(iface);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {

		return connection.unwrap(iface);
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		connection.abort(executor);

	}

	@Override
	public void clearWarnings() throws SQLException {
		connection.clearWarnings();

	}

	@Override
	public void close() throws SQLException {
		if (!isBusy()) {
			throw new SQLException(errorMessage);
		}

		if(!connection.isClosed() && this.closed.get()){
			connection.close();
		}
		
		this.closed.set(true);

	}

	@Override
	public void commit() throws SQLException {

		connection.commit();

	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {

		return this.connection.createArrayOf(typeName, elements);
	}

	@Override
	public Blob createBlob() throws SQLException {

		return this.connection.createBlob();
	}

	@Override
	public Clob createClob() throws SQLException {

		return this.connection.createClob();
	}

	@Override
	public NClob createNClob() throws SQLException {

		return this.connection.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {

		return this.connection.createSQLXML();
	}

	@Override
	public Statement createStatement() throws SQLException {

		return this.connection.createStatement();
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {

		return this.connection.createStatement(resultSetType,
				resultSetConcurrency);
	}

	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {

		return this.connection.createStatement(resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {

		return this.connection.createStruct(typeName, attributes);
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return this.connection.getAutoCommit();
	}

	@Override
	public String getCatalog() throws SQLException {

		return this.connection.getCatalog();
	}

	@Override
	public Properties getClientInfo() throws SQLException {

		return this.connection.getClientInfo();
	}

	@Override
	public String getClientInfo(String name) throws SQLException {

		return this.connection.getClientInfo(name);
	}

	@Override
	public int getHoldability() throws SQLException {

		return this.connection.getHoldability();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {

		return this.connection.getMetaData();
	}

	@Override
	public int getNetworkTimeout() throws SQLException {

		return this.connection.getNetworkTimeout();
	}

	@Override
	public String getSchema() throws SQLException {

		return this.connection.getSchema();
	}

	@Override
	public int getTransactionIsolation() throws SQLException {

		return this.connection.getTransactionIsolation();
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {

		return this.connection.getTypeMap();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {

		return this.connection.getWarnings();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return this.closed.get();
	}

	@Override
	public boolean isReadOnly() throws SQLException {

		return this.connection.isReadOnly();
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {

		return this.connection.isValid(timeout);
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {

		return this.connection.nativeSQL(sql);
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {

		return this.connection.prepareCall(sql);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {


		return this.connection.prepareCall(sql, resultSetType,
				resultSetConcurrency);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {

		return this.connection.prepareCall(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {

		return this.connection.prepareStatement(sql);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {

		return this.connection.prepareStatement(sql, autoGeneratedKeys);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {

		return this.connection.prepareStatement(sql, columnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {

		return this.connection.prepareStatement(sql, columnNames);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {

		return this.connection.prepareStatement(sql, resultSetType,
				resultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {

		return this.connection.prepareStatement(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {

		this.connection.releaseSavepoint(savepoint);

	}

	@Override
	public void rollback() throws SQLException {

		this.connection.rollback();

	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {

		this.connection.rollback(savepoint);

	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {

		this.connection.setAutoCommit(autoCommit);

	}

	@Override
	public void setCatalog(String catalog) throws SQLException {

		this.connection.setCatalog(catalog);

	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {

		this.connection.setClientInfo(properties);

	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {

		this.connection.setClientInfo(name, value);

	}

	@Override
	public void setHoldability(int holdability) throws SQLException {

		this.connection.setHoldability(holdability);

	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds)
			throws SQLException {

		this.connection.setNetworkTimeout(executor, milliseconds);

	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {

		this.connection.setReadOnly(readOnly);

	}

	@Override
	public Savepoint setSavepoint() throws SQLException {

		return this.connection.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {

		return this.connection.setSavepoint(name);
	}

	@Override
	public void setSchema(String schema) throws SQLException {

		this.connection.setSchema(schema);

	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {

		this.connection.setTransactionIsolation(level);

	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {

		this.connection.setTypeMap(map);

	}

}
