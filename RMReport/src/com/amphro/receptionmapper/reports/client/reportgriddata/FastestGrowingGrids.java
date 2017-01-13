package com.amphro.receptionmapper.reports.client.reportgriddata;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import com.amphro.receptionmapper.reports.client.DataServiceAsync;
import com.amphro.receptionmapper.reports.shared.Grid;
import com.amphro.receptionmapper.reports.shared.Pair;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class FastestGrowingGrids extends GridData implements AsyncCallback<ArrayList<ArrayList<Pair<Integer, Grid>>>> {
	private final static int NUMBER_OF_COLS = 10;
	//private ArrayList<ArrayList<Grid>> mGrids;
	private List<List<String>> mGridGrid;
	private List<Grid> mGrids;
	private ListGridField[] mColumns;
	private ListGridRecord[] mRecords;
	
	/**
	 * Construct for a FastestGrowingGrids AsyncCallback.
	 * @param maxGridLevel The maximum grid level to be returned by the callback. Levels 1 
	 * through maxGridLevel will be returned.
	 * @param gridsPerLevel The number of grid coordinates <X,Y> per level to be returned.
	 * This determines the top N fastest growing grids to be returned.
	 * @param minTime The minimum Node time to allow.
	 * @param maxTime The maximum Node time to allow.
	 * @param dataService The data service that will be called for this object and return
	 * the data that this will display.
	 */
	public FastestGrowingGrids(int maxGridLevel, int gridsPerLevel, 
			Date minTime, Date maxTime,
			DataServiceAsync dataService) 
	{
		dataService.fastestGrowingGrids(maxGridLevel, gridsPerLevel, minTime, maxTime, this);
		
		mGridGrid = new ArrayList<List<String>>();
		mGrids = new ArrayList<Grid>();
		//mGrids = new ArrayList<ArrayList<Grid>>();
		
		mColumns = new ListGridField[NUMBER_OF_COLS];
		mColumns[0] = new ListGridField("Level", 45);
		mColumns[1] = new ListGridField("ID", 30);
		mColumns[2] = new ListGridField("Count", 75);
		mColumns[3] = new ListGridField("X", 55);
		mColumns[4] = new ListGridField("Y", 55);
		mColumns[5] = new ListGridField("Carrier", 55);
		mColumns[6] = new ListGridField("Average Net", 70);
		mColumns[7] = new ListGridField("Average Rec", 85);
		mColumns[8] = new ListGridField("Num Net", 55);
		mColumns[9] = new ListGridField("Num Rec", 55);
	}
	
	public ListGridField[] getColumnHeaders() {
		return mColumns;
	}

	public Object[] getRowObjects() {
		return mGrids.toArray();
	}

	public ListGridRecord[] getRowObjectsByColumn() {
		return mRecords;
	}

	public String getTitle() {
		return "Highest Populated Grids";
	}

	public void onFailure(Throwable caught) {
		System.out.println("Fastest Growing Grids: failed\n");
	}
	
	public void onSuccess(ArrayList<ArrayList<Pair<Integer, Grid>>> result) {
		if(result == null)
			return;
		
		//mGrids = result;
		int recordCount = 0;
		for(ArrayList<Pair<Integer, Grid>> gridList : result) {
			recordCount += gridList.size();
			System.out.print(gridList.size() + " + ");
		}
		mRecords = new ListGridRecord[recordCount];
		System.out.println("0 = " + recordCount);
		
		int recIndex = 0;
		int level = 1;
		for(ArrayList<Pair<Integer, Grid>> gridList : result) {
			for(Pair<Integer, Grid> countAndGrid : gridList) {
				Grid grid = countAndGrid.getRight();
				mGrids.add(grid);
				List<String> row = new ArrayList<String>();
				row.add(String.valueOf(level));
				row.add(String.valueOf(grid.getId()));
				row.add(String.valueOf(countAndGrid.getLeft()));
				row.add(String.valueOf(grid.getGridX()));
				row.add(String.valueOf(grid.getGridY()));
				row.add(String.valueOf(grid.getCarrier()));
				row.add(String.valueOf(grid.getAvgnet()));
				row.add(String.valueOf(grid.getAvgrec()));
				row.add(String.valueOf(grid.getNumnet()));
				row.add(String.valueOf(grid.getNumrec()));
				mGridGrid.add(row);

				mRecords[recIndex] = new ListGridRecord();
				mRecords[recIndex].setAttribute("Level", level);
				mRecords[recIndex].setAttribute("ID", grid.getId());
				mRecords[recIndex].setAttribute("Count", countAndGrid.getLeft());
				mRecords[recIndex].setAttribute("X", grid.getGridX());
				mRecords[recIndex].setAttribute("Y", grid.getGridY());
				mRecords[recIndex].setAttribute("Carrier", grid.getCarrier());
				mRecords[recIndex].setAttribute("Average Net", grid.getAvgnet());
				mRecords[recIndex].setAttribute("Average Rec", grid.getAvgrec());
				mRecords[recIndex].setAttribute("Num Net", grid.getNumnet());
				mRecords[recIndex].setAttribute("Num Rec", grid.getNumrec());
				
				recIndex++;
			}
			level++;
		}
		finish();
	}
}
