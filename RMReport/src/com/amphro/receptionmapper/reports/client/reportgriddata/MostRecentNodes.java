package com.amphro.receptionmapper.reports.client.reportgriddata;

import java.util.ArrayList;
import java.util.List;

import com.amphro.receptionmapper.reports.client.DataServiceAsync;
import com.amphro.receptionmapper.reports.shared.Node;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class MostRecentNodes extends GridData implements AsyncCallback<Node[]> {
	final static int NUMBER_OF_COLS = 4;
	
	private List<Node> mNodes;
	private List<List<String>> mNodeGrid;
	private ListGridField[] mColumns;
	private ListGridRecord[] mRecords;
	
	public MostRecentNodes(DataServiceAsync dataService) {
		dataService.getMostRecentUploads(null, 10, this);
		
		mNodeGrid = new ArrayList<List<String>>();
		mNodes = new ArrayList<Node>();
		mColumns = new ListGridField[NUMBER_OF_COLS];
		
		mColumns[0] = new ListGridField("ID", 35);
		mColumns[1] = new ListGridField("Latitude", 60);
		mColumns[2] = new ListGridField("Longitude", 60);
		mColumns[3] = new ListGridField("Client", 35); 
	}
	
	@Override
	public ListGridField[] getColumnHeaders() {
		return mColumns;
	}

	@Override
	public Object[] getRowObjects() {
		return mNodes.toArray();
	}

	@Override
	public ListGridRecord[] getRowObjectsByColumn() {
		return mRecords;
	}

	@Override
	public String getTitle() {
		return "Most Recent Nodes";
	}
	
	@Override
	public void onFailure(Throwable caught) {
			
	}

	@Override
	public void onSuccess(Node[] result) {
		List<String> row;
		
		for (Node node : result) {
			mNodes.add(node);
			
			row = new ArrayList<String>();
			row.add(String.valueOf(node.getId()));
			row.add(String.valueOf(node.getLatitude()));
			row.add(String.valueOf(node.getLongitude()));
			row.add(String.valueOf(node.getClient()));
			mNodeGrid.add(row);
		}

		mRecords = new ListGridRecord[mNodes.size()];
		
		for (int index = 0; index < mNodes.size(); index++) {
			mRecords[index] = new ListGridRecord();
			mRecords[index].setAttribute("ID", mNodes.get(index).getId());
			mRecords[index].setAttribute("Latitude", mNodes.get(index).getLatitude());
			mRecords[index].setAttribute("Longitude", mNodes.get(index).getLongitude());
			mRecords[index].setAttribute("Client", mNodes.get(index).getClient());
		}

		finish();
	}
}
