package com.hgsoft.common.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;
import java.sql.*;

public class ApplicationContextUtils implements ApplicationContextAware {
	private static ApplicationContext applicationContext;
	private static DataSource dataSource;
	private static ApplicationContextUtils instance;
	
	
	private final static Class<ApplicationContextUtils> clazz = ApplicationContextUtils.class;
	private final static Log logger = LogFactory
			.getLog(ApplicationContextUtils.class);
	
	private ApplicationContextUtils()
	{
		
	}
	public static ApplicationContextUtils getInstance()
	{
		return instance;
	}

	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		
		ApplicationContextUtils.instance = this;
		
		ApplicationContextUtils.applicationContext = applicationContext;

		ApplicationContextUtils.dataSource = ApplicationContextUtils.applicationContext
				.getBean("dataSource", DataSource.class);

	}

	/**
	 * 获取Spring上下文对象
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取数据源
	 * 
	 * @return
	 */
	public static DataSource getDataSource() {
		return dataSource;
	}


	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			LogJob.writeLog(e, logger, clazz);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取Statement
	 * 
	 * @return
	 */
	public static Statement getStatement(Connection conn) {
		try {
			return conn.createStatement();
		} catch (SQLException e) {
			LogJob.writeLog(e, logger, clazz);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取PreparedStatement
	 * 
	 * @return
	 */
	public static PreparedStatement getPreparedStatement(Connection conn,
			String sql) {
		try {
			return conn.prepareStatement(sql);
		} catch (SQLException e) {
			LogJob.writeLog(e, logger, clazz);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 关闭ResultSet
	 * 
	 * @param conn
	 */
	public static void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();

			} catch (SQLException e) {
				LogJob.writeLog(e, logger, clazz);
				throw new RuntimeException(e);
			}

			resultSet = null;

		}
	}
	
	/**
	 * 关闭PreparedStatement
	 * 
	 * @param conn
	 */
	public static void closeCallableStatement(
			CallableStatement callableStatement) {
		if (callableStatement != null) {
			try {
				callableStatement.close();

			} catch (SQLException e) {
				LogJob.writeLog(e, logger, clazz);
				throw new RuntimeException(e);
			}
			callableStatement = null;
		}
	}

	/**
	 * 关闭PreparedStatement
	 * 
	 * @param conn
	 */
	public static void closePreparedStatement(
			PreparedStatement preparedStatement) {
		if (preparedStatement != null) {
			try {
				preparedStatement.close();

			} catch (SQLException e) {
				LogJob.writeLog(e, logger, clazz);
				throw new RuntimeException(e);
			}
			preparedStatement = null;
		}
	}

	/**
	 * 关闭statement
	 * 
	 * @param conn
	 */
	public static void closeStatement(Statement statement) {
		if (statement != null) {
			try {
				statement.close();

			} catch (SQLException e) {
				LogJob.writeLog(e, logger, clazz);
				throw new RuntimeException(e);
			}
			statement = null;
		}
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param conn
	 */
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();

			} catch (SQLException e) {
				LogJob.writeLog(e, logger, clazz);
				throw new RuntimeException(e);
			}
			conn = null;
		}
	}
}
