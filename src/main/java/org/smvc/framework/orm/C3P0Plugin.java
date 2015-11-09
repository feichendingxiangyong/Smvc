package org.smvc.framework.orm;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0Plugin implements IDataSourcePlugin {
	private ComboPooledDataSource ds = null;
	private String jdbcUrl;
	private String user;
	private String password;
	private String driverClass = "com.mysql.jdbc.Driver";
	private int maxPoolSize = 100;
	private int minPoolSize = 10;
	private int initialPoolSize = 10;
	private int maxIdleTime = 20;
	private int acquireIncrement = 2;

	public C3P0Plugin(String jdbcUrl, String user, String password,
			String driverClass, int maxPoolSize, int minPoolSize,
			int initialPoolSize, int maxIdleTime, int acquireIncrement) {

		this.jdbcUrl = jdbcUrl;
		this.user = user;
		this.password = password;
		this.maxPoolSize = maxPoolSize;
		this.minPoolSize = minPoolSize;
		this.initialPoolSize = initialPoolSize;
		this.maxIdleTime = maxIdleTime;
		this.acquireIncrement = acquireIncrement;
		this.driverClass = driverClass;

		ds = new ComboPooledDataSource();
		try {
			ds.setDriverClass(this.driverClass);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		ds.setJdbcUrl(this.jdbcUrl);
		ds.setUser(this.user);
		ds.setPassword(this.password);
		ds.setMaxPoolSize(this.maxPoolSize);
		ds.setMinPoolSize(this.minPoolSize);
		ds.setInitialPoolSize(this.initialPoolSize);
		ds.setMaxIdleTime(this.maxIdleTime);
		ds.setAcquireIncrement(this.acquireIncrement);

	}

	public synchronized DataSource getDataSource() {
		return ds;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getMinPoolSize() {
		return minPoolSize;
	}

	public void setMinPoolSize(int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	public int getInitialPoolSize() {
		return initialPoolSize;
	}

	public void setInitialPoolSize(int initialPoolSize) {
		this.initialPoolSize = initialPoolSize;
	}

	public int getMaxIdleTime() {
		return maxIdleTime;
	}

	public void setMaxIdleTime(int maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}

	public int getAcquireIncrement() {
		return acquireIncrement;
	}

	public void setAcquireIncrement(int acquireIncrement) {
		this.acquireIncrement = acquireIncrement;
	}

	public void load() {

	}

}
