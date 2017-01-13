package com.amphro.receptionmapper.reports.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public class ReportDisplay {
	private static VerticalPanel vpanel;
	private static ListGrid dataGrid;
	private static Object[] allData;
	
	private static void setUp(){
		
		if(vpanel == null)
			vpanel = new VerticalPanel();
		
		vpanel.clear();
		
		HorizontalPanel h1 = new HorizontalPanel();
		HorizontalPanel h2 = new HorizontalPanel();
		HorizontalPanel h3 = new HorizontalPanel();
		
		h1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		h1.setSpacing(5);
		h2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		h2.setSpacing(5);
		h3.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		h3.setSpacing(5);
		
		Button back = new Button("Back");
		
		back.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				RMReport.root.clear();
				RMReport.root.add(ReportSelection.getLayout());
			}
		});
		
		dataGrid.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				SC.confirm(allData[event.getRecordNum()].toString(), new BooleanCallback(){

					@Override
					public void execute(Boolean value) {
						
					}
				});
			}
		});
		
		h1.add(dataGrid);
		h3.add(back);
		
		vpanel.add(h1);
		vpanel.add(h3);
	}
	
	public static void setUpGrid(ListGridField[] fields, ListGridRecord[] rows, Object[] allInfo, String title){
		if(dataGrid != null)
			dataGrid = null;
		
		dataGrid = new ListGrid();
		dataGrid.setFields(fields);
		dataGrid.setData(rows);

        dataGrid.setTitle(title);
		dataGrid.resizeTo(600, 800);
		allData = allInfo;
		
		RMReport.root.clear();
		RMReport.root.add(ReportDisplay.getLayout());
	}
	
	public static Widget getLayout(){
		setUp();
		
		return vpanel;
	}
}