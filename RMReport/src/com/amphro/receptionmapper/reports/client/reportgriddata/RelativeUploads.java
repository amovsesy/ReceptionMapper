package com.amphro.receptionmapper.reports.client.reportgriddata;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.amphro.receptionmapper.reports.client.DataServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class RelativeUploads extends GridData implements AsyncCallback<Float []>
{
	private final static int NUMBER_OF_COLS = 3;
	private List<List<String>> mFloatGrid;
	private ListGridField[] mColumns;
	private ListGridRecord[] mRecords;
	private List<Float> mFloats;
	private Date min;
	private Date max;
	private int interval;
	
	
	public RelativeUploads(Date min, Date max, int interval, DataServiceAsync dataservice)
	{
		this.min = min; this.max = max; this.interval = interval;
		dataservice.relativeUploads(min, max, interval, this);
		
		mFloatGrid = new ArrayList<List<String>>();
		
		mColumns = new ListGridField[NUMBER_OF_COLS];
		mColumns[0] = new ListGridField("Start Date", 50);
		mColumns[1] = new ListGridField("End Date", 50);
		mColumns[2] = new ListGridField("Average Uploads", 50);
		
		
	}

	@Override
	public ListGridField[] getColumnHeaders() {
		return mColumns;
	}

	public Object[] getRowObjects() {
		return mFloatGrid.toArray();
	}

	public ListGridRecord[] getRowObjectsByColumn() {
		return mRecords;
	}

	public String getTitle() {
		return "Relative Uploads";
	}

	public void onFailure(Throwable caught) {
		System.out.println("Relative Uploads: failed\n");
	}

	@Override
	public void onSuccess(Float[] result) {
			List<String> row;
			int i = 0;
			mFloats = new ArrayList<Float>();
			for (Float f : result) {
				mFloats.add(f);
				
				row = new ArrayList<String>();
				row.add(String.valueOf(min.getTime() + i * interval * 60 * 1000));
				row.add(String.valueOf(min.getTime() + (i + 1) * interval * 60 * 1000));
				row.add(String.valueOf(f));
				mFloatGrid.add(row);
				i++;
			}

			mRecords = new ListGridRecord[mFloats.size()];
			
			for (int index = 0; index < mFloats.size(); index++) {
				mRecords[index] = new ListGridRecord();
				mRecords[index].setAttribute("Start Date", min.getTime() + index * interval * 60 * 1000);
				mRecords[index].setAttribute("End Date", min.getTime() + (index + 1) * interval * 60 * 1000);
				mRecords[index].setAttribute("Average Uploads", mFloats.get(index));
			}

			finish();
		}
}
