package org.smvc.framework.orm;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;
import org.smvc.framework.orm.annotation.GeneratedKeys;
import org.smvc.framework.orm.annotation.MyBeanProcessor;
import org.smvc.framework.orm.annotation.Parameter;
import org.smvc.framework.orm.annotation.SqlBuilder;
import org.smvc.framework.orm.annotation.SqlHolder;
import org.smvc.framework.util.ClassUtil;

/**
 * Base dao
 * 
 */
public class DbPlugin<T> {
	private final static Logger LOGGER = Logger.getLogger(DbPlugin.class);
	private QueryRunner runner;
	
	//data souce plugin
	private IDataSourcePlugin dsPlugin;
	
	//data source
	protected DataSource ds;

	/**
	 * Default creator
	 * @param dsPlugin
	 */
	public DbPlugin(IDataSourcePlugin dsPlugin)
	{
		this.dsPlugin = dsPlugin;
		this.ds = this.dsPlugin.getDataSource();
		runner = new QueryRunner(this.ds);
	}

	private final static ScalarHandler scaleHandler = new ScalarHandler() {
		@Override
		public Object handle(ResultSet rs) throws SQLException {
			Object obj = super.handle(rs);
			if (obj instanceof BigInteger)
				return ((BigInteger) obj).longValue();

			return obj;
		}
	};

	/**
	 * 
	 * Use handler of MyBeanProcessor
	 * 
	 * @see MyBeanProcessor
	 * @param clazz
	 * @return
	 */
	private BeanListHandler<T> getBeanListHandler(Class<T> clazz) {
		return new BeanListHandler<T>(clazz, new BasicRowProcessor(
				new MyBeanProcessor()));
	}

	/**
	 * Use handler of MyBeanProcessor
	 * 
	 * @see MyBeanProcessor
	 * @param clazz
	 * @return
	 */
	private BeanHandler<T> getBeanHandler(Class<T> clazz) {
		return new BeanHandler<T>(clazz, new BasicRowProcessor(
				new MyBeanProcessor()));
	}

	/**
	 * Get one connection
	 * 
	 * @return
	 */
	public Connection getConn() {
		try {
			return ds.getConnection();
		} catch (Exception e) {
			LOGGER.error("Get db connecttion failed!", e);
			return null;
		}
	}

	/**
	 * print sql
	 * 
	 * @param sql
	 */
	private static void showSql(String sql) {
		if (true) {
			LOGGER.debug(sql);
		}
	}

	private static void showParam(Object[] params) {
		StringBuilder sb = new StringBuilder("parameter:");
		if (params.length == 0) {
			sb.append("no parameters.");
		} else {
			for (Object obj : params) {
				sb.append(obj).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		LOGGER.debug(sb.toString());
	}

	/**
	 * 
	 * query list
	 * 
	 * @param sql
	 * @param clazz
	 * @param params
	 * @return
	 */
	public List<T> queryList(Class<T> clazz, Parameter param) {
		SqlHolder holder = SqlBuilder.buildQuery(clazz, param);
		return this.queryList(holder.getSql(), clazz, holder.getParams());
	}

	/**
	 * 
	 * query all of rows with returning list
	 * 
	 * @param sql
	 * @param clazz
	 * @param params
	 * @return
	 */
	public List<T> queryAllList(Class<T> clazz) {
		return this.queryList(clazz, null);
	}

	/**
	 * 
	 * query rows with returning list
	 * 
	 * @param sql
	 * @param clazz
	 *            target class
	 * @param params
	 *            param array
	 * @return
	 */
	private List<T> queryList(String sql, Class<T> clazz, Object... params) {
		showSql(sql);
		try {
			return (List<T>) runner.query(sql, getBeanListHandler(clazz),
					params);
		} catch (SQLException e) {
			LOGGER.debug("query failed.", e);
			return new ArrayList<T>();
		}
	}
	
	/**
	 * 
	 * query rows with returning list
	 * 
	 * @param sql
	 * @param clazz
	 *            target class
	 * @param params
	 *            param array
	 * @return
	 */
	public List<T> queryListWithSql(String sql, Class<T> clazz, Object... params) {
		showSql(sql);
		showParam(params);
		try {
			return (List<T>) runner.query(sql, getBeanListHandler(clazz),
					params);
		} catch (SQLException e) {
			LOGGER.debug("query failed.", e);
			return new ArrayList<T>();
		}
	}

	/**
	 * query one object
	 * 
	 * @param sql
	 * @param clazz
	 * @param params
	 * @return
	 */
	public T query(Class<T> clazz, Parameter param) {
		SqlHolder holder = SqlBuilder.buildQuery(clazz, param);
		showSql(holder.getSql());
		try {
			return (T) runner.query(holder.getSql(), getBeanHandler(clazz),
					holder.getParams());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * query one object
	 * 
	 * @param sql
	 * @param clazz
	 * @param params
	 * @return
	 */
	public T queryWithSql(String sql, Class<T> clazz, Object... params) {
		showSql(sql);
		showParam(params);
		try {
			return (T) runner.query(sql, getBeanHandler(clazz),
					params);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * query long
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public Long queryLong(String sql, Object... params) {
		showSql(sql);
		try {
			Number n = (Number) runner.query(sql, scaleHandler, params);
			return n.longValue();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Save one object
	 * 
	 * @param obj
	 * @return
	 */
	public int save(Object obj) {
		SqlHolder holder = SqlBuilder.buildInsert(obj);
		return this.update(this.getConn(), holder.getSql(),
				holder.getParams());
	}

	/**
	 * Save a list of objects
	 * 
	 * @param obj
	 * @return
	 */
	public <T> int[] saveList(List<T> objs) {
		SqlHolder holder = SqlBuilder.buildInsertList(objs);
		Object[][] params = new Object[holder.getParams().length][];
		int index = 0;
		for (Object item : holder.getParams()) {
			params[index++] = (Object[]) item;
		}
		return this.insertList(this.getConn(), holder.getSql(), params);
	}

	public long saveWithGeneratedKeys(Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		SqlHolder holder = SqlBuilder.buildInsert(obj);

		// generated primary key
		Long generatedKey = this.insert(this.getConn(), holder.getSql(),
				holder.getParams());

		// set key value to object
		Class<?> clazz = obj.getClass();
		Field[] fields = ClassUtil.getAnnotatedDeclaredFields(clazz,
				GeneratedKeys.class, true);

		if (fields.length > 0) {
			// forced access
			fields[0].setAccessible(true);

			// set value
			fields[0].set(obj, generatedKey);

		}

		return generatedKey;
	}

	public int delete(Class<T> clazz, String where) {
		SqlHolder holder = SqlBuilder.buildDelete(clazz, where);
		return this.update(this.getConn(), holder.getSql(),
				holder.getParams());
	}

	public int delete(Class<?> clazz, Parameter param) {
		SqlHolder holder = SqlBuilder.buildDelete(clazz, param);
		return this.update(this.getConn(), holder.getSql(),
				holder.getParams());
	}

	public <T> int deleteAll(Class<T> clazz) {
		return delete(clazz, null);
	}

	public int update(Object obj, String where) {
		SqlHolder holder = SqlBuilder.buildUpdate(obj, where);
		return this.update(this.getConn(), holder.getSql(),
				holder.getParams());
	}

	public int update(Object obj, Parameter param) {
		SqlHolder holder = SqlBuilder.buildUpdate(obj, param);
		return this.update(this.getConn(), holder.getSql(),
				holder.getParams());
	}

	/**
	 * execute INSERT/UPDATE/DELETE
	 * 
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 */
	private int update(Connection conn, String sql, Object... params) {
		showSql(sql);
		try {

			return runner.update(conn, sql, params);
		} catch (SQLException e) {
			LOGGER.debug("update failed!", e);
			return 0;
		}
	}

	/**
	 * execute INSERT
	 * 
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 */
	private Long insert(Connection conn, String sql, Object... params) {
		showSql(sql);
		try {
			return runner.insert(conn, sql, new ScalarHandler<Long>(), params);
		} catch (SQLException e) {
			LOGGER.debug("update failed!", e);
			return new Long(0);
		}
	}

	/**
	 * execute INSERT
	 * 
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 */
	private int[] insertList(Connection conn, String sql, Object[][] params) {
		showSql(sql);
		try {
			return runner.batch(conn, sql, params);
		} catch (SQLException e) {
			LOGGER.debug("update failed!", e);
			return new int[0];
		}
	}

	/**
	 * 
	 * query list
	 * 
	 * @param sql
	 * @return Map<String, Object>
	 */
	public List<Map<String, Object>> queryList(String sql) {
		showSql(sql);
		try {
			List<Map<String, Object>> results = (List<Map<String, Object>>) runner
					.query(sql, new MapListHandler());
			return results;
		} catch (SQLException e) {
			LOGGER.error("query failed!", e);
			return new ArrayList<Map<String, Object>>();
		}
	}

}
