package com.amphro.receptionmapper.reports.client.reportgriddata;

import com.amphro.receptionmapper.reports.client.ReportDisplay;
import com.amphro.receptionmapper.reports.shared.Constants;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public abstract class GridData {
	/**
	 * This is a method that will return the column headers
	 *   to the specific report
	 */
	public abstract ListGridField[] getColumnHeaders();
	
	/**
	 *  Returns the row as specific columns to be displayed in
	 *    the grid.  Not all data needs to be displayed.  Should
	 *    have the same number of columns as the length of the 
	 *    String[] that getColumnHeaders returns 
	 */
	public abstract ListGridRecord[] getRowObjectsByColumn();
	
	/**
	 * The actual object for each row. Objects must have an 
	 * overwritten toString method to display the data.
	 */
	public abstract Object[] getRowObjects();
	
	public abstract String getTitle();
	
	public void finish() {
		ReportDisplay.setUpGrid(getColumnHeaders(), 
				getRowObjectsByColumn(), 
				getRowObjects(),
				getTitle());
		
		//add code to remove the loader
		DOM.setInnerHTML(RootPanel.get(Constants.LOADER_DIV).getElement(), "");
	}
}
