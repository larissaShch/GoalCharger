package org.larissashch.portfolio.goalchatge.persistence.repository.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReusableConnectionPool implements ConnectionPool {

	final private int maxSize;
	private List<ReusableConnection> connections;
	final private String url;
	private AtomicBoolean closed;

	public ReusableConnectionPool(String url, int maxSize) {
		if (maxSize < 0){
			throw new RuntimeException("Pool size cannot be less than 0.");
		}
		if (url == null){
			throw new RuntimeException("Url can't be empty.");
		}

		this.url = url;
		this.maxSize = maxSize;
		closed = new AtomicBoolean(false);
	}
	public void getInfo(){
		System.out.println("Connections in the pool: "+connections.size());
		int i=0;
		for(ReusableConnection connection : connections){
			if(connection.isBusy()){
				i++;
			}
		}
		System.out.println("Busy connections: "+i);
		
		i=0;
		for(ReusableConnection connection : connections){
			try {
				if(connection.isClosed()){
					i++;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Closed connections: "+i);
		
	}
	@Override
	public ReusableConnection getConnection() {
		boolean allConnectionsBusy;
		if (closed.get()) {
			throw new RuntimeException("Connection pool is closed.");
		}
		if (connections == null) {
			connections = new ArrayList<>();
		}
		int index = 0;
		for (ReusableConnection connection : connections) {
			index++;
			synchronized (connection) {
				if (connection != null && !connection.isBusy()) {
					connection.setBusy(true);
					allConnectionsBusy = false;
					
					
					System.out.println("Free connection "+index+" from pool!");
					
					return connection;
				}
			}
		}
		synchronized (connections) {
			if (connections.size() < maxSize) {

				Connection connection;
				try {
					connection = DriverManager.getConnection(url);
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(
							"Error in creation of new connection.");
				}
				ReusableConnection reusableConnection = new ReusableConnection(
						connection);
				connections.add(reusableConnection);
				
				index++;
				System.out.println("New connection "+index+" added to pool!");
				
				return reusableConnection;
			}
		}
		allConnectionsBusy = true;
		while (allConnectionsBusy) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		 

			//this.getInfo();
			System.out.println("All connections are busy!");
			return getConnection();
		}
		return null;
	}

	@Override
	public void releaseConnection(Connection connection) {
		synchronized (connections) {
			for (ReusableConnection connection2 : connections) {
				if (connection2.equals(connection)) {
					connection2.setBusy(false);
				}
			}
		}
		
		//this.getInfo();
	}

	@Override
	public void close() {
		this.getInfo();
		
		closed.set(true);
		for (ReusableConnection connection : connections) {
			if (!connection.isBusy()) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException("Error in closing connections.");
				}
			}else{
				closed.set(false);
			}
		}
		
		while (!closed.get()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.close();
		}
		
		for (ReusableConnection connection : connections) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("Error in closing connections.");
			}
		}
		this.getInfo();
		connections.removeAll(connections);
		System.out.println("Reusable connections in the pool: "+connections.size()+". Connection pool is closed.");

	}

}
