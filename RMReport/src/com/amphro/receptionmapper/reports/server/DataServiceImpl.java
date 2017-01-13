package com.amphro.receptionmapper.reports.server;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
	
	private DatabaseConnection mDbConn = null;
	
	public DatabaseConnection getConn() {
		if (mDbConn == null) {
			mDbConn = new DatabaseConnection();
		}
		return mDbConn;
	}
	
	public Node[] getNodes() {
		List<Node> ret = getConn().getLimitNodes(10);

		Node[] n = new Node[ret.size()];

		for (int i = 0; i < ret.size(); i++) {
			n[i] = ret.get(i);
		}
		
		return n;
	}
	
	public Node[] getMostRecentUploads(Date maxDate, int maxNodes)
	{
		
		List<Node> ret = getConn().getNodesAfterTime(
				new Timestamp(maxDate == null ? 0 :maxDate.getTime()).toString(), maxNodes);

		Node[] n = new Node[ret.size()];

		for (int i = 0; i < ret.size(); i++) {
			n[i] = ret.get(i);
		}
		return n;
	}
	
	/**
	 * Should be that list.get(0) is actually grid level 1... get(1) is 2, etc.
	 */
	public ArrayList<ArrayList<Grid>> getGrids(int minLevel, int maxLevel) {
		if(minLevel < Grid.MIN_LEVEL || maxLevel > Grid.MAX_LEVEL || minLevel > maxLevel)
			return null;
		
		ArrayList<ArrayList<Grid>> data = new ArrayList<ArrayList<Grid>>();
		
		for(int level = maxLevel; level < minLevel; level++)  {
			data.add(getConn().getAllGridsFromLevel(level));
		}
		
		return data;
	}

	/**
	 * Returns the fastest growing grids in the database with the given constraints.
	 * @param maxGridLevel The maximum grid level to be returned by the callback. Levels 1 
	 * through maxGridLevel will be returned.
	 * @param gridsPerLevel The number of grid coordinates <X,Y> per level to be returned.
	 * This determines the top N fastest growing grids to be returned.
	 * @param minTime The minimum Node time to allow.
	 * @param maxTime The maximum Node time to allow.
	 * @return The lists of the fastest growing grids, one per grid level required in order 1..N.
	 */
	public ArrayList<ArrayList<Pair<Integer, Grid>>> fastestGrowingGrids(int maxGridLevel, int gridsPerLevel, Date minTime, Date maxTime)
	{
		if(maxGridLevel < Grid.MIN_LEVEL || maxGridLevel > Grid.MAX_LEVEL || gridsPerLevel <= 0)
			return null;

		//use nodes valid within input minTime/maxTime
		String query = "SELECT * FROM Nodes WHERE " + Node.NODE_TIME + " BETWEEN '"+ minTime + "' AND '" + maxTime + "' ORDER BY time ASC";
		List<Node> nodeList = getConn().getNodes(query);

		//output = List of <Node Count, Grid Data>
		//node count = number of nodes for the given Grid <Level, X, Y> within the timeframe
		ArrayList<ArrayList<Pair<Integer, Grid>>> output = new ArrayList<ArrayList<Pair<Integer, Grid>>>();
		
		//append top N valid gridLevel results
		for(int level = Grid.MIN_LEVEL; level <= maxGridLevel; level++) {
			HashMap<Pair<Integer, Integer>, Integer> grids 
				= new HashMap<Pair<Integer, Integer>, Integer>();
			for(int i = 0; i < nodeList.size(); i++) {
				Pair<Integer, Integer> xy = new Pair<Integer, Integer>();
				Node node = nodeList.get(i);
				xy.setLeft(GSSHelper.toGSSx(level, node.getLongitude()));
				xy.setRight(GSSHelper.toGSSy(level, node.getLatitude()));
				Integer count = grids.get(xy);
				if(count != null)
					grids.put(xy, count + 1); //replaces in HashMap - intentional
				else
					grids.put(xy, 1); //<xy, count>
			}
			//sort <gridX, gridY> by count and keep top N
			List<Entry<Pair<Integer, Integer>, Integer>> list
				= new ArrayList<Entry<Pair<Integer, Integer>, Integer>>();
			Iterator<Entry<Pair<Integer, Integer>, Integer>> iter 
				= grids.entrySet().iterator();
			while(iter.hasNext()) {
				list.add(iter.next());
			}
			//sort by count
			Collections.sort(list, new Comparator<Entry<Pair<Integer, Integer>, Integer>>() {
					public int compare(Entry<Pair<Integer, Integer>, Integer> val0,
							Entry<Pair<Integer, Integer>, Integer> val1) {
						return val0.getValue() - val1.getValue();
					}
				});
			//remove unnecessary grid coordinates
//System.out.println("level=" + level);
//System.out.println("list.size()=" + list.size());
			list = list.subList(0, Math.min(gridsPerLevel, list.size()));
//System.out.println(" list.size()=" + list.size());
			ArrayList<Pair<Integer, Grid>> levelVals = new ArrayList<Pair<Integer, Grid>>();		
			List<Pair<Integer, Integer>> xyVals = new ArrayList<Pair<Integer, Integer>>();
			for(Entry<Pair<Integer, Integer>, Integer> e : list) {
				xyVals.add(e.getKey());
			}
//System.out.println("xyVals.size()=" + xyVals.size());
			//get grids with specific coordinates
			ArrayList<Grid> gridVals = getConn().getGrids(level, xyVals);
//System.out.println("gridVals.size()=" + gridVals.size());
			for(Grid g : gridVals) {
				Pair<Integer, Integer> xy 
					= new Pair<Integer, Integer>(g.getGridX(), g.getGridY());
				levelVals.add(new Pair<Integer, Grid>(grids.get(xy), g));
			}
//System.out.println("levelVals.size()=" + levelVals.size());
			output.add(levelVals);
		}
		return output;
	}

	public ArrayList<ArrayList<Grid>> highestPopulatedGrids(int maxGridLevel, int gridsPerLevel)
	{
		if(maxGridLevel < Grid.MIN_LEVEL || maxGridLevel > Grid.MAX_LEVEL || gridsPerLevel <= 0)
			return null;
		String query = "SELECT * FROM gridlevel";
		String query2 = " ORDER BY numrec DESC LIMIT 0, ";
		ArrayList<ArrayList<Grid>> data = new ArrayList<ArrayList<Grid>>();
		DatabaseConnection conn = getConn();
		for(int level = 1; level <= maxGridLevel; level++)  {
			ArrayList<Grid> levelData = conn.getGrids(query + level + query2 + gridsPerLevel, level);
			if(levelData == null)
				return null;
			data.add(levelData);
		}
		return data;
	}

	public Float[] relativeUploads(Date minDate, Date maxDate, int intervalInMins)
	{
		ArrayList<Float> ret = new ArrayList<Float>();
		String query = "SELECT * FROM Nodes order BY time asc";
		List<Node> nodeList = getConn().getNodes(query);
		
		Date topInt = new Date(minDate.getTime());
		int nodeIndex = 0;
		int numNodes = 0;
		do {
			topInt = new Date(topInt.getTime() + intervalInMins * 60000);
			while(nodeIndex < nodeList.size() && nodeList.get(nodeIndex).getTime().before(topInt))
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