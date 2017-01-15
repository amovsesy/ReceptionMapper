package sampleMaps.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
	private static Connection _conn = null;
	private final static String URL = "jdbc:mysql://localhost:3306/receptionmapper";
	private final static String USER = "root";
	private final static String PASSWORD = "";
	
	private MySQLConnection(){
	}
	
	private static void setupConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			_conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			System.out.println("SQL EXCEPTION");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("CLASS NOT FOUND EXCEPTION");
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		if(_conn == null){
			setupConnection();
		}
		
		return _conn;
	}
}
