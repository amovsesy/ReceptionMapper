package com.amphro.receptionmapper.reports.client;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import com.amphro.receptionmapper.reports.shared.Grid;
import com.amphro.receptionmapper.reports.shared.Node;
import com.amphro.receptionmapper.reports.shared.Pair;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("dataService")
public interface DataService extends RemoteService {
	Node[] getNodes();

	//NOTE: How many nodes per page to display etc can be
	//handled by the client as dynamic page generation, and 
	//should not be handled in the data request.
	Node[] getMostRecentUploads(Date maxDate);
	
	ArrayList<ArrayList<Grid>> getGrids(int minLevel, int maxLevel);
	
	ArrayList<Grid> getGrids(int level, List<Pair<Integer, Integer>> xyVals);

	ArrayList<ArrayList<Pair<Integer,Grid>>> fastestGrowingGrids(int maxGridLevel, int gridsPerLevel, Date minTime, Date maxTime);

	ArrayList<ArrayList<Grid>> highestPopulatedGrids(int maxGridLevel, int gridsPerLevel);

	Float[] relativeUploads(Date minDate, Date maxDate, int intervalInMins);
}
