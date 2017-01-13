package com.amphro.receptionmapper.reports.client.reportgriddata;

import java.util.ArrayList;
import java.util.List;
import com.amphro.receptionmapper.reports.client.DataServiceAsync;
import com.amphro.receptionmapper.reports.shared.Grid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class HighestPopulatedGrids extends GridData implements AsyncCallback<ArrayList<ArrayList<Grid>>> {
	private final static int NUMBER_OF_COLS = 9;
	//private ArrayList<ArrayList<Grid>> mGrids;
	private List<List<String>> mGridGrid;
	private List<Grid> mGrids;
	private ListGridField[] mColumns;
	private ListGridRecord[] mRecords;
	
	/**
	 * Constructor for the HighestPopulatedGrids AsyncCallback.
	 * @param maxGridLevel The maximum grid level to be returned by the callback. Levels 1 
	 * through maxGridLevel will be returned.
	 * @param gridsPerLevel The number of grid coordinates <X,Y> per level to be returned.
	 * This determines the top N fastest growing grids to be returned.
	 * @param dataService The data service that will be called for this object and return
	 * the data that this will display.
	 */
	public HighestPopulatedGrids(int maxGridLevel, int gridsPerLevel,
			DataServiceAsync dataService) 
	{
		dataService.highestPopulatedGrids(maxGridLevel, gridsPerLevel, this);
		mGrids = new ArrayList<Grid>();
		mGridGrid = new ArrayList<List<String>>();
		//mGrids = new ArrayList<ArrayList<Grid>>();
		
		mColumns = new ListGridField[NUMBER_OF_COLS];
		mColumns[0] = new ListGridField("Level", 45);
		mColumns[1] = new ListGridField("ID", 30);
		mColumns[2] = new ListGridField("X", 55);
		mColumns[3] = new ListGridField("Y", 55);
		mColumns[4] = new ListGridField("Carrier", 55);
		mColumns[5] = new ListGridField("Average Net", 70);
		mColumns[6] = new ListGridField("Average Rec", 85);
		mColumns[7] = new ListGridField("Num Net", 55);
		mColumns[8] = new ListGridField("Num Rec", 55);
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
		System.out.println("Highest Populated Grids: failed\n");
	}
	
	public void onSuccess(ArrayList<ArrayList<Grid>> result) {
		if(result == null)
			return;
		
		//mGrids = result;
		int recordCount = 0;
		for(ArrayList<Grid> gridList : result) {
			recordCount += gridList.size();
		}
		mRecords = new ListGridRecord[recordCount];

		int recIndex = 0;
		int level = 1;
		
		for(ArrayList<Grid> gridList : result) {
			for(Grid grid : gridList) {
				mGrids.add(grid);
				List<String> row = new ArrayList<String>();
				row.add(String.valueOf(level));
				row.add(String.valueOf(grid.getId()));
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
