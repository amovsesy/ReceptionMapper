package com.amphro.receptionmapper.reports.client;

import java.sql.Date;
import java.util.ArrayList;

import com.amphro.receptionmapper.reports.shared.Grid;
import com.amphro.receptionmapper.reports.shared.Node;
import com.amphro.receptionmapper.reports.shared.Pair;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>DataService</code>.
 */
public interface DataServiceAsync {
	void getNodes(AsyncCallback<Node[]> callback);
	
	void getMostRecentUploads(Date maxDate, int maxNodes, AsyncCallback<Node[]> callback);

	void getGrids(int minLevel, int maxLevel,
			AsyncCallback<ArrayList<ArrayList<Grid>>> callback);
	
	/**
	 * Returns the fastest growing grids in the database with the given constraints.
	 * @param maxGridLevel The maximum grid level to be returned by the callback. Levels 1 
	 * through maxGridLevel will be returned.
	 * @param gridsPerLevel The number of grid coordinates <X,Y> per level to be returned.
	 * This determines the top N fastest growing grids to be returned.
	 * @param minTime The minimum Node time to allow.
	 * @param maxTime The maximum Node time to allow.
	 * @param callback The callback object containing the fastest growing grids.
	 */
	void fastestGrowingGrids(int maxGridLevel, int gridsPerLevel, Date minTime, Date maxTime,
			AsyncCallback<ArrayList<ArrayList<Pair<Integer, Grid>>>> callback);

	/**
	 * Returns the highest populated grids in the database with the given constraints.
	 * @param maxGridLevel The maximum grid level to be returned by the callback. Levels 1 
	 * through maxGridLevel will be returned.
	 * @param gridsPerLevel The number of grid coordinates <X,Y> per level to be returned.
	 * This determines the top N fastest growing grids to be returned.
	 * @param callback The callback object containing the highest populated grids.
	 */
	void highestPopulatedGrids(int maxGridLevel, int gridsPerLevel,
			AsyncCallback<ArrayList<ArrayList<Grid>>> callback);

	void relativeUploads(Date minDate, Date maxDate, int intervalInMins,
			AsyncCallback<Float[]> callback);
}