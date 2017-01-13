package com.amphro.receptionmapper.reports.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	/**
	 * db conn
	 * 
	 * Make sure you add a reference library (external jar in build path) JDBC
	 * Connector - You will see I put it in
	 * /opt/gwt-linux/mysql-connector-java-5.0.8-bin.jar
	 * 
	 * @return Connection
	 */
	public static Connection getConnection() {
		Connection conn = null;
		String url = "jdbc:mysql://127.0.0.1:3306/";
		String db = "receptionmappertest";
		String driver = "com.mysql.jdbc.Driver";
		String user = "root";
		String pass = "";

		try {
			Class.forName(driver).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			conn = DriverManager.getConnection(url + db, user, pass);
		} catch (SQLException e) {
			System.err.println("Mysql Connection Error: ");
			e.printStackTrace();
		}
		
		return conn;
	}
}