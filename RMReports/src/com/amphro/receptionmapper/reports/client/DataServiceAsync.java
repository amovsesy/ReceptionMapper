package com.amphro.receptionmapper.reports.client;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import com.amphro.receptionmapper.reports.shared.Grid;
import com.amphro.receptionmapper.reports.shared.Node;
import com.amphro.receptionmapper.reports.shared.Pair;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>DataService</code>.
 */
public interface DataServiceAsync {
	void getNodes(AsyncCallback<Node[]> callback);

	void getMostRecentUploads(Date maxDate, AsyncCallback<Node[]> callback);

	void getGrids(int minLevel, int maxLevel,
			AsyncCallback<ArrayList<ArrayList<Grid>>> callback);
	
	void getGrids(int level, List<Pair<Integer, Integer>> xyVals,
			AsyncCallback<ArrayList<Grid>> callback);
	
	void fastestGrowingGrids(int maxGridLevel, int gridsPerLevel, Date minTime, Date maxTime,
			AsyncCallback<ArrayList<ArrayList<Pair<Integer,Grid>>>> callback);

	void highestPopulatedGrids(int maxGridLevel, int gridsPerLevel,
			AsyncCallback<ArrayList<ArrayList<Grid>>> callback);

	void relativeUploads(Date minDate, Date maxDate, int intervalInMins,
			AsyncCallback<Float[]> callback);
}
