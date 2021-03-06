package com.sovereignty.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil {

	// These are to be configured and NEVER stored in the code.
	// once you retrieve this code, you can update
	public final static String rdsMySqlDatabaseUrl = "cms-database-1.cqesujasrspi.us-east-1.rds.amazonaws.com";
	public final static String dbUsername = "admin";
	public final static String dbPassword = "cs509cms";
		
	public final static String jdbcTag = "jdbc:mysql://";
	public final static String rdsMySqlDatabasePort = "3306";
	public final static String multiQueries = "?allowMultiQueries=true";
	   
	public final static String dbName = "innodb";    // default created from MySQL WorkBench

	// pooled across all usages.
	static Connection connection;
 
	/**
	 * Singleton access to DB connection to share resources effectively across multiple accesses.
	 */
	protected static Connection connect() throws Exception {
		if (connection != null) { return connection; }
		
		try {
			//System.out.println("start connecting......");
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(
					jdbcTag + rdsMySqlDatabaseUrl + ":" + rdsMySqlDatabasePort + "/" + dbName + multiQueries,
					dbUsername,
					dbPassword);
			System.out.println("Database has been connected successfully.");
			return connection;
		} catch (Exception ex) {
			System.out.println("Problem connecting to DB");
			throw new Exception("Database connection failed");
		}
	}
}
