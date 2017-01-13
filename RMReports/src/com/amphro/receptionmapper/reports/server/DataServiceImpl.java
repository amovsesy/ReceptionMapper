package com.amphro.receptionmapper.reports.server;

import java.sql.Connection;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.amphro.receptionmapper.location.GSS;
import com.amphro.receptionmapper.reports.client.DataService;
import com.amphro.receptionmapper.reports.shared.Grid;
import com.amphro.receptionmapper.reports.shared.Node;
import com.amphro.receptionmapper.reports.shared.Pair;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class DataServiceImpl extends RemoteServiceServlet implements DataService
{
	// Necessary in order to convert timestamp to comparable data
	static Timestamp dataTime = new Timestamp(1);

	public Node[] getNodes()
	{
		String query = "SELECT * FROM Nodes LIMIT 10";
		List<Node> ret = new ArrayList<Node>();

		try
		{
			Connection conn = DatabaseConnection.getConnection();
			Statement select = conn.createStatement();
			ResultSet result = select.executeQuery(query);

			while (result.next())
			{
				Node node = new Node(result.getFloat(Node.NODE_LATITUDE), result.getFloat(Node.NODE_LONGITUDE), result.getInt(Node.NODE_CLIENT), result.getString(Node.NODE_NETWORK), result
						.getInt(Node.NODE_SIGNAL_STRENGTH), result.getDate(Node.NODE_TIME));
				ret.add(node);
				System.out.println(node);
			}
			select.close();
			result.close();
			conn.close();
		} catch (SQLException e)
		{
			System.err.println("Mysql Statement Error: " + query);
			e.printStackTrace();
		}

		Node[] n = new Node[ret.size()];

		for (int i = 0; i < ret.size(); i++)
		{
			n[i] = ret.get(i);
		}

		return n;
	}

	public Node[] getMostRecentUploads(Date maxDate)
	{
		String query = "SELECT * FROM Nodes";
		List<Node> ret = new ArrayList<Node>();
		try
		{
			Connection conn = DatabaseConnection.getConnection();
			Statement select = conn.createStatement();
			ResultSet result = select.executeQuery(query);

			while (result.next())
			{
				Node node = new Node(result.getFloat(Node.NODE_LATITUDE), result.getFloat(Node.NODE_LONGITUDE), result.getInt(Node.NODE_CLIENT), result.getString(Node.NODE_NETWORK), result
						.getInt(Node.NODE_SIGNAL_STRENGTH), result.getDate(Node.NODE_TIME));
				ret.add(node);
				System.out.println(node);
			}
			select.close();
			result.close();
			conn.close();
		} catch (SQLException e)
		{
			System.err.println("Mysql Statement Error: " + query);
			e.printStackTrace();
		}

		Node[] n = new Node[ret.size()];

		for (int i = 0, j = 0; i < ret.size(); i++)
		{
			dataTime.valueOf(ret.get(i).NODE_TIME);
			if (!dataTime.after(maxDate))
			{
				n[j] = ret.get(i);
				j++;
			}
		}
		return n;
	}
	
	/**
	 * Should be that list.get(0) is actually grid level 1... get(1) is 2, etc.
	 */
	public ArrayList<ArrayList<Grid>> getGrids(int minLevel, int maxLevel) {
		if(minLevel < Grid.MIN_LEVEL || maxLevel > Grid.MAX_LEVEL || minLevel > maxLevel)
			return null;
		String query = "SELECT * FROM gridlevel";
		ArrayList<ArrayList<Grid>> data = new ArrayList<ArrayList<Grid>>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			Statement select = conn.createStatement();
			for(int level = maxLevel; level < minLevel; level++)  {
				ResultSet result = select.executeQuery(query + level);
				ArrayList<Grid> levelData = new ArrayList<Grid>();
				while (result.next()) {
					float avgnet = result.getInt(Grid.GRID_AVGNET);
					float avgrec = result.getFloat(Grid.GRID_AVGREC);
					int carrier = result.getInt(Grid.GRID_CARRIER);
					int gridX = result.getInt(Grid.GRID_X);
					int gridY = result.getInt(Grid.GRID_Y);
					int id = result.getInt(Grid.GRID_ID);
					int numnet = result.getInt(Grid.GRID_NUMNET);
					int numrec = result.getInt(Grid.GRID_NUMREC);
					Grid grid = new Grid(id, level, gridX, gridY, avgrec, numrec,
							carrier, avgnet, numnet);
					levelData.add(grid);
				}
				data.add(levelData);
			}
			return data;
		} catch (SQLException e) {
			System.err.println("Mysql Statement Error: " + query);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the Grids of a given level that are in a set of <X,Y> values.
	 */
	public ArrayList<Grid> getGrids(int level, List<Pair<Integer, Integer>> xyVals) {
		if(level < Grid.MIN_LEVEL || level > Grid.MAX_LEVEL)
			return null;

		String query = "SELECT * FROM gridlevel" + level + " WHERE (";
		Iterator<Pair<Integer, Integer>> iter = xyVals.iterator();
		if(iter.hasNext()) {
			query += Grid.GRID_X + " = " + iter.next().getLeft() + " ";
			while(iter.hasNext()) {
				query += "OR " + Grid.GRID_X + " = " + iter.next().getLeft() + " ";
			}
			query += ")";
		}
		try {
			Connection conn = DatabaseConnection.getConnection();
			Statement select = conn.createStatement();
			ResultSet result = select.executeQuery(query);
			ArrayList<Grid> output = new ArrayList<Grid>();
			while (result.next()) {
				float avgnet = result.getInt(Grid.GRID_AVGNET);
				float avgrec = result.getFloat(Grid.GRID_AVGREC);
				int carrier = result.getInt(Grid.GRID_CARRIER);
				int gridX = result.getInt(Grid.GRID_X);
				int gridY = result.getInt(Grid.GRID_Y);
				int id = result.getInt(Grid.GRID_ID);
				int numnet = result.getInt(Grid.GRID_NUMNET);
				int numrec = result.getInt(Grid.GRID_NUMREC);
				Grid grid = new Grid(id, level, gridX, gridY, avgrec, numrec,
						carrier, avgnet, numnet);
				output.add(grid);
			}
			return output;
		} catch (SQLException e) {
			System.err.println("Mysql Statement Error: " + query);
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<ArrayList<Pair<Integer,Grid>>> fastestGrowingGrids(int maxGridLevel, int gridsPerLevel, Date minTime, Date maxTime)
	{	
		if(maxGridLevel < Grid.MIN_LEVEL || maxGridLevel > Grid.MAX_LEVEL)
			return null;

		//get valid nodes
		String query = "SELECT * FROM Nodes WHERE " + Node.NODE_TIME + " BETWEEN "
			+ minTime + " " + maxTime + " ORDER BY time ASC";
		ArrayList<Node> nodeList = new ArrayList<Node>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			Statement select = conn.createStatement();
			ResultSet result = select.executeQuery(query);
			while(result.next()) {
				Node node = new Node(
					result.getFloat(Node.NODE_LATITUDE), 
					result.getFloat(Node.NODE_LONGITUDE), 
					result.getInt(Node.NODE_CLIENT), 
					result.getString(Node.NODE_NETWORK), 
					result.getInt(Node.NODE_SIGNAL_STRENGTH), 
					result.getDate(Node.NODE_TIME));
				nodeList.add(node);
			}
		} catch (SQLException e) {
			System.err.println("Mysql Statement Error: " + query);
			e.printStackTrace();
			return null;
		}

		ArrayList<ArrayList<Pair<Integer,Grid>>> output = new ArrayList<ArrayList<Pair<Integer,Grid>>>();
		
		//append top N valid gridLevel1 results
		if(maxGridLevel >= 1) {
			HashMap<Pair<Integer, Integer>, Integer> grids 
				= new HashMap<Pair<Integer, Integer>, Integer>();
			
			//determine count for each <gridX, gridY>
			for(int i = 0; i < nodeList.size(); i++) {
				Pair<Integer, Integer> xy = new Pair<Integer, Integer>();
				Node node = nodeList.get(i);
				xy.setLeft(GSS.toGSS1x(node.getLongitude()));
				xy.setRight(GSS.toGSS1y(node.getLatitude()));
				Integer count = grids.get(xy);
				if(count != null)
					grids.put(xy, count + 1); //replaces
				else
					grids.put(xy, 1);
			}
			//sort <gridX, gridY> by count and keep top N
			List<Entry<Pair<Integer, Integer>, Integer>> list
				= new ArrayList<Entry<Pair<Integer, Integer>, Integer>>();
			Iterator<Entry<Pair<Integer, Integer>, Integer>> iter 
				= grids.entrySet().iterator();
			while(iter.hasNext()) {
				list.add(iter.next());
			}
			Collections.sort(list, new Comparator<Entry<Pair<Integer, Integer>, Integer>>() {
					public int compare(Entry<Pair<Integer, Integer>, Integer> arg0,
							Entry<Pair<Integer, Integer>, Integer> arg1) {
						return arg0.getValue() - arg1.getValue();
					}
				});
			list = list.subList(0, gridsPerLevel);
			ArrayList<Pair<Integer, Grid>> level1Vals = new ArrayList<Pair<Integer, Grid>>();		
			List<Pair<Integer, Integer>> xyVals = new ArrayList<Pair<Integer, Integer>>();
			for(Entry<Pair<Integer, Integer>, Integer> e : list) {
				xyVals.add(e.getKey());
			}
			ArrayList<Grid> gridVals = getGrids(1, xyVals);
			for(Grid g : gridVals) {
				Pair<Integer, Integer> xy 
					= new Pair<Integer, Integer>(g.getGridX(), g.getGridY());
				level1Vals.add(new Pair<Integer, Grid>(grids.get(xy), g));
			}
			output.add(level1Vals);
		}
		if(maxGridLevel >= 2) {
			HashMap<Pair<Integer, Integer>, Integer> grids 
				= new HashMap<Pair<Integer, Integer>, Integer>();
		
			//determine count for each <gridX, gridY>
			for(int i = 0; i < nodeList.size(); i++) {
				Pair<Integer, Integer> xy = new Pair<Integer, Integer>();
				Node node = nodeList.get(i);
				xy.setLeft(GSS.toGSS2x(node.getLongitude()));
				xy.setRight(GSS.toGSS2y(node.getLatitude()));
				Integer count = grids.get(xy);
				if(count != null)
					grids.put(xy, count + 1); //replaces
				else
					grids.put(xy, 1);
			}
			//sort <gridX, gridY> by count and keep top N
			List<Entry<Pair<Integer, Integer>, Integer>> list
				= new ArrayList<Entry<Pair<Integer, Integer>, Integer>>();
			Iterator<Entry<Pair<Integer, Integer>, Integer>> iter 
				= grids.entrySet().iterator();
			while(iter.hasNext()) {
				list.add(iter.next());
			}
			Collections.sort(list, new Comparator<Entry<Pair<Integer, Integer>, Integer>>() {
					public int compare(Entry<Pair<Integer, Integer>, Integer> arg0,
							Entry<Pair<Integer, Integer>, Integer> arg1) {
						return arg0.getValue() - arg1.getValue();
					}
				});
			list = list.subList(0, gridsPerLevel);
			ArrayList<Pair<Integer, Grid>> level2Vals = new ArrayList<Pair<Integer, Grid>>();		
			List<Pair<Integer, Integer>> xyVals = new ArrayList<Pair<Integer, Integer>>();
			for(Entry<Pair<Integer, Integer>, Integer> e : list) {
				xyVals.add(e.getKey());
			}
			ArrayList<Grid> gridVals = getGrids(2, xyVals);
			for(Grid g : gridVals) {
				Pair<Integer, Integer> xy 
					= new Pair<Integer, Integer>(g.getGridX(), g.getGridY());
				level2Vals.add(new Pair<Integer, Grid>(grids.get(xy), g));
			}
			output.add(level2Vals);
		}
		if(maxGridLevel >= 3) {
			HashMap<Pair<Integer, Integer>, Integer> grids 
				= new HashMap<Pair<Integer, Integer>, Integer>();
		
			//determine count for each <gridX, gridY>
			for(int i = 0; i < nodeList.size(); i++) {
				Pair<Integer, Integer> xy = new Pair<Integer, Integer>();
				Node node = nodeList.get(i);
				xy.setLeft(GSS.toGSS3x(node.getLongitude()));
				xy.setRight(GSS.toGSS3y(node.getLatitude()));
				Integer count = grids.get(xy);
				if(count != null)
					grids.put(xy, count + 1); //replaces
				else
					grids.put(xy, 1);
			}
			//sort <gridX, gridY> by count and keep top N
			List<Entry<Pair<Integer, Integer>, Integer>> list
				= new ArrayList<Entry<Pair<Integer, Integer>, Integer>>();
			Iterator<Entry<Pair<Integer, Integer>, Integer>> iter 
				= grids.entrySet().iterator();
			while(iter.hasNext()) {
				list.add(iter.next());
			}
			Collections.sort(list, new Comparator<Entry<Pair<Integer, Integer>, Integer>>() {
					public int compare(Entry<Pair<Integer, Integer>, Integer> arg0,
							Entry<Pair<Integer, Integer>, Integer> arg1) {
						return arg0.getValue() - arg1.getValue();
					}
				});
			list = list.subList(0, gridsPerLevel);
			ArrayList<Pair<Integer, Grid>> level3Vals = new ArrayList<Pair<Integer, Grid>>();		
			List<Pair<Integer, Integer>> xyVals = new ArrayList<Pair<Integer, Integer>>();
			for(Entry<Pair<Integer, Integer>, Integer> e : list) {
				xyVals.add(e.getKey());
			}
			ArrayList<Grid> gridVals = getGrids(3, xyVals);
			for(Grid g : gridVals) {
				Pair<Integer, Integer> xy 
					= new Pair<Integer, Integer>(g.getGridX(), g.getGridY());
				level3Vals.add(new Pair<Integer, Grid>(grids.get(xy), g));
			}
			output.add(level3Vals);
		}
		if(maxGridLevel >= 4) {
			HashMap<Pair<Integer, Integer>, Integer> grids 
				= new HashMap<Pair<Integer, Integer>, Integer>();
		
			//determine count for each <gridX, gridY>
			for(int i = 0; i < nodeList.size(); i++) {
				Pair<Integer, Integer> xy = new Pair<Integer, Integer>();
				Node node = nodeList.get(i);
				xy.setLeft(GSS.toGSS4x(node.getLongitude()));
				xy.setRight(GSS.toGSS4y(node.getLatitude()));
				Integer count = grids.get(xy);
				if(count != null)
					grids.put(xy, count + 1); //replaces
				else
					grids.put(xy, 1);
			}
			//sort <gridX, gridY> by count and keep top N
			List<Entry<Pair<Integer, Integer>, Integer>> list
				= new ArrayList<Entry<Pair<Integer, Integer>, Integer>>();
			Iterator<Entry<Pair<Integer, Integer>, Integer>> iter 
				= grids.entrySet().iterator();
			while(iter.hasNext()) {
				list.add(iter.next());
			}
			Collections.sort(list, new Comparator<Entry<Pair<Integer, Integer>, Integer>>() {
					public int compare(Entry<Pair<Integer, Integer>, Integer> arg0,
							Entry<Pair<Integer, Integer>, Integer> arg1) {
						return arg0.getValue() - arg1.getValue();
					}
				});
			list = list.subList(0, gridsPerLevel);
			ArrayList<Pair<Integer, Grid>> level4Vals = new ArrayList<Pair<Integer, Grid>>();		
			List<Pair<Integer, Integer>> xyVals = new ArrayList<Pair<Integer, Integer>>();
			for(Entry<Pair<Integer, Integer>, Integer> e : list) {
				xyVals.add(e.getKey());
			}
			ArrayList<Grid> gridVals = getGrids(4, xyVals);
			for(Grid g : gridVals) {
				Pair<Integer, Integer> xy 
					= new Pair<Integer, Integer>(g.getGridX(), g.getGridY());
				level4Vals.add(new Pair<Integer, Grid>(grids.get(xy), g));
			}
			output.add(level4Vals);
		}
		if(maxGridLevel >= 4) {
			HashMap<Pair<Integer, Integer>, Integer> grids 
				= new HashMap<Pair<Integer, Integer>, Integer>();
		
			//determine count for each <gridX, gridY>
			for(int i = 0; i < nodeList.size(); i++) {
				Pair<Integer, Integer> xy = new Pair<Integer, Integer>();
				Node node = nodeList.get(i);
				xy.setLeft(GSS.toGSS5x(node.getLongitude()));
				xy.setRight(GSS.toGSS5y(node.getLatitude()));
				Integer count = grids.get(xy);
				if(count != null)
					grids.put(xy, count + 1); //replaces
				else
					grids.put(xy, 1);
			}
			//sort <gridX, gridY> by count and keep top N
			List<Entry<Pair<Integer, Integer>, Integer>> list
				= new ArrayList<Entry<Pair<Integer, Integer>, Integer>>();
			Iterator<Entry<Pair<Integer, Integer>, Integer>> iter 
				= grids.entrySet().iterator();
			while(iter.hasNext()) {
				list.add(iter.next());
			}
			Collections.sort(list, new Comparator<Entry<Pair<Integer, Integer>, Integer>>() {
					public int compare(Entry<Pair<Integer, Integer>, Integer> arg0,
							Entry<Pair<Integer, Integer>, Integer> arg1) {
						return arg0.getValue() - arg1.getValue();
					}
				});
			list = list.subList(0, gridsPerLevel);
			ArrayList<Pair<Integer, Grid>> level5Vals = new ArrayList<Pair<Integer, Grid>>();		
			List<Pair<Integer, Integer>> xyVals = new ArrayList<Pair<Integer, Integer>>();
			for(Entry<Pair<Integer, Integer>, Integer> e : list) {
				xyVals.add(e.getKey());
			}
			ArrayList<Grid> gridVals = getGrids(5, xyVals);
			for(Grid g : gridVals) {
				Pair<Integer, Integer> xy 
					= new Pair<Integer, Integer>(g.getGridX(), g.getGridY());
				level5Vals.add(new Pair<Integer, Grid>(grids.get(xy), g));
			}
			output.add(level5Vals);
		}
		return output;
	}

	public ArrayList<ArrayList<Grid>> highestPopulatedGrids(int maxGridLevel, int gridsPerLevel)
	{
		if(maxGridLevel < Grid.MIN_LEVEL || maxGridLevel > Grid.MAX_LEVEL)
			return null;
		String query = "SELECT * FROM gridlevel";
		String query2 = " ORDER BY numrec DESC";
		ArrayList<ArrayList<Grid>> data = new ArrayList<ArrayList<Grid>>();
		try {
			Connection conn = DatabaseConnection.getConnection();
			Statement select = conn.createStatement();
			for(int level = 1; level < maxGridLevel; level++)  {
				ResultSet result = select.executeQuery(query + level + query2);
				ArrayList<Grid> levelData = new ArrayList<Grid>();
				int count = 0;
				while (result.next() && count < gridsPerLevel) {
					float avgnet = result.getInt(Grid.GRID_AVGNET);
					float avgrec = result.getFloat(Grid.GRID_AVGREC);
					int carrier = result.getInt(Grid.GRID_CARRIER);
					int gridX = result.getInt(Grid.GRID_X);
					int gridY = result.getInt(Grid.GRID_Y);
					int id = result.getInt(Grid.GRID_ID);
					int numnet = result.getInt(Grid.GRID_NUMNET);
					int numrec = result.getInt(Grid.GRID_NUMREC);
					Grid grid = new Grid(id, level, gridX, gridY, avgrec, numrec,
							carrier, avgnet, numnet);
					levelData.add(grid);
					count++;
				}
				data.add(levelData);
			}
			return data;
		} catch (SQLException e) {
			System.err.println("Mysql Statement Error: " + query);
			e.printStackTrace();
		}
		return null;
	}

	public Float[] relativeUploads(Date minDate, Date maxDate, int intervalInMins)
	{
		ArrayList<Float> ret = new ArrayList<Float>();
		String query = "SELECT * FROM Nodes order BY time asc";
		List<Node> nodeList = new ArrayList<Node>();
		try
		{
			Connection conn = DatabaseConnection.getConnection();
			Statement select = conn.createStatement();
			ResultSet result = select.executeQuery(query);

			while (result.next())
			{
				Node node = new Node(result.getFloat(Node.NODE_LATITUDE), result.getFloat(Node.NODE_LONGITUDE), result.getInt(Node.NODE_CLIENT), result.getString(Node.NODE_NETWORK), result
						.getInt(Node.NODE_SIGNAL_STRENGTH), result.getDate(Node.NODE_TIME));
				dataTime.valueOf(node.NODE_TIME);
				if (dataTime.before(maxDate) && dataTime.after(minDate))
					nodeList.add(node);

				System.out.println(node);
			}
			select.close();
			result.close();
			conn.close();
		} catch (SQLException e)
		{
			System.err.println("Mysql Statement Error: " + query);
			e.printStackTrace();
		}
		Date topInt = new Date(minDate.getTime());
		int nodeIndex = 0;
		int numNodes = 0;
		do
		{
			topInt = new Date(topInt.getTime() + intervalInMins * 60000);
			while(nodeList.get(nodeIndex).getTime().before(topInt))
			{
				numNodes++;
				nodeIndex++;
			}
			ret.add((float)numNodes);
			numNodes = 0;
		} while (topInt.before(maxDate));
		for(int i = 0; i < ret.size(); i++)
			ret.set(i, ret.get(i)/nodeIndex);
		
		Float[] returnVal = new Float[ret.size()];

		for (int i = 0; i < ret.size(); i++)
		{
			returnVal[i] = ret.get(i);
		}
		
		return returnVal;
	}
}
