package com.amphro.receptionmapper.reports.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.amphro.receptionmapper.reports.shared.Grid;
import com.amphro.receptionmapper.reports.shared.Node;
import com.amphro.receptionmapper.reports.shared.Pair;

public class DatabaseConnection {
	private Connection mConn = null;
	
	/**
	 * Initialize a DatabaseConnection and establish a connection
	 * to the database.
	 */
	public DatabaseConnection() {
		mConn = DatabaseConnection.getConnection();
	}
	
	/**
	 * Establish a new database connection after cleaning the 
	 * current connection, if there is one. 
	 */
	public void estabilishConnection() {
		cleanConnection();
		mConn = DatabaseConnection.getConnection();
	}
	
	/**
	 * Clear the current connection.
	 */
	public void cleanConnection() {
		if (mConn != null) {
			try {
				mConn.close();
			} catch (SQLException e) {
				System.err.println("Can not close connection.");
				e.printStackTrace();
			}
		} else {
			System.err.println("Connection is null!");
		}
	}
	
	/**
	 * Get a connection that uses the jdbc driver.
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
	
	/**
	 * Execute a query by creating a new statement from the current 
	 * connection
	 * 
	 * @param query Query to be executed
	 * @return An open ResultSet from the executed query. The statement 
	 * used to create this ResultSet is still open.
	 */
	public ResultSet executeQuery(String query) {
		ResultSet rs = null;
		
		try {
			Statement select = mConn.createStatement();
			
			rs = select.executeQuery(query);
		} catch (SQLException e) {
			System.err.println("Mysql Statement Error: " + query);
			e.printStackTrace();
		}
		
		return rs;
	}
	
	/**
	 * Get all nodes from the database
	 * 
	 * @return A list of all the nodes in the database.
	 */
	public List<Node> getAllNodes() {
		String query = "SELECT * FROM Nodes";
		return getNodes(query);
	}
	
	/**
	 * Get a certain amount of nodes from the database
	 * 
	 * @param limit The amount of nodes to retrieve. 
	 * @return A list containing limit, or less, amount of nodes. 
	 */
	public List<Node> getLimitNodes(Integer limit) {
		String query = "SELECT * FROM Nodes LIMIT " + limit;
		return getNodes(query);
	}
	
	public List<Node> getNodesAfterTime(String time) {
		String query = "SELECT * FROM Nodes WHERE time > '"+time+"' ORDER BY time DESC";
		return getNodes(query);
	}
	
	public List<Node> getNodesAfterTime(String time, Integer limit) {
		String query = "SELECT * FROM Nodes WHERE time > '"+time+"' ORDER BY time DESC LIMIT " + limit;
		return getNodes(query);
	}
	
	/**
	 * Get a list of node form a custom query. The custom query 
	 * MUST select all columns from Node.
	 * 
	 * @param query The custom query to be executed
	 * @return A list of Nodes from the custom query
	 */
	public List<Node> getNodes(String query) {
		List<Node> ret = new ArrayList<Node>();
		
		try {
			ResultSet result = executeQuery(query);
			
			while (result.next()) {
				ret.add(new Node(
						result.getInt(Node.NODE_ID),
						result.getFloat(Node.NODE_LATITUDE), 
						result.getFloat(Node.NODE_LONGITUDE), 
						result.getInt(Node.NODE_CLIENT), 
						result.getString(Node.NODE_NETWORK), 
						result.getInt(Node.NODE_SIGNAL_STRENGTH), 
						result.getDate(Node.NODE_TIME)));
			}
			
			result.getStatement().close();
			result.close();
		} catch (SQLException e) {
			System.err.println("Mysql Result Error: " + query);
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * Get all grids of a certain level from the database
	 * 
	 * @return A list of all the nodes in the database.
	 */
	public ArrayList<Grid> getAllGridsFromLevel(Integer level) {
		String query = "SELECT * FROM GridLevel" + level;
		return getGrids(query, level);
	}
	
	/**
	 * Get a certain amount of grids of a certain level from the 
	 * database
	 * 
	 * @param limit The amount of nodes to retrieve. 
	 * @return A list containing limit, or less, amount of nodes. 
	 */
	public ArrayList<Grid> getLimitGridsFromLevel(Integer limit, Integer level) {
		String query = "SELECT * FROM GridLevel" + level+ " LIMIT " + limit;
		return getGrids(query, level);
	}
	
	/**
	 * Get a list of nodes form a custom query. The custom query 
	 * MUST select all columns from GridLevelX.
	 * 
	 * @param query The custom query to be executed
	 * @param level The GridLevel the custom query is grabing from
	 * @return A list of Grids from the custom query
	 */
	public ArrayList<Grid> getGrids(String query, Integer level) {
		ArrayList<Grid> data = new ArrayList<Grid>();
		
		try {
			ResultSet result = executeQuery(query);

			while (result.next()) {
				data.add(new Grid(
						result.getInt(Grid.GRID_ID),
						level,
						result.getInt(Grid.GRID_X(level)),
						result.getInt(Grid.GRID_Y(level)),
						result.getFloat(Grid.GRID_AVGREC),
						result.getInt(Grid.GRID_NUMREC),
						result.getInt(Grid.GRID_CARRIER),
						result.getInt(Grid.GRID_AVGNET),
						result.getInt(Grid.GRID_NUMNET)));
			}

			result.getStatement().close();
			result.close();
		} catch (SQLException e) {
			System.err.println("Mysql Statement Error: " + query);
			e.printStackTrace();
		}
		return data;
	}
	
	
	/**
	 * Gets the Grids of a given level that are in a set of <X,Y> values.
	 * @param level The level to get the Grids from.
	 * @param xyVals The valid <X, Y> values to get for this grid.
	 * @return A list of Grids whose coordinates fall within the set xyVals.
	 */
	public ArrayList<Grid> getGrids(int level, List<Pair<Integer, Integer>> xyVals) {
		if(level < Grid.MIN_LEVEL || level > Grid.MAX_LEVEL)
			return null;
		if(xyVals.size() == 0)
			return new ArrayList<Grid>();
		
//System.out.println("xyVals.size()=" + xyVals.size());
		String query = "SELECT * FROM gridlevel" + level + " WHERE (";
		Iterator<Pair<Integer, Integer>> iter = xyVals.iterator();
		if(iter.hasNext()) {
			Pair<Integer, Integer> xy = iter.next();
			query += "(" + Grid.GRID_X(level) + " = " + xy.getLeft() + " AND " 
				+ Grid.GRID_Y(level) + " = " + xy.getRight() + ")";
			while(iter.hasNext()) {
				xy = iter.next();
				query += " OR (" + Grid.GRID_X(level) + " = " + xy.getLeft() 
					+ " AND " + Grid.GRID_Y(level) + " = " + xy.getRight() + ")";
			}
			query += ")";
		}
//System.out.println(query);
		return getGrids(query, level);
	}
}