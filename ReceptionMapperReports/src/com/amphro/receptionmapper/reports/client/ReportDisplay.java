package com.amphro.receptionmapper.reports.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ReportDisplay {
	private static VerticalPanel vpanel;
	private static Grid dataGrid;
	private static Object[][] allData;
	
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
		
		final TextBox row = new TextBox();
		row.setWidth("10");
		
		final Label rowLabel = new Label("Enter Row:");
		
		Button getMore = new Button("Get More Information");
		
		getMore.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				final DialogBox result = new DialogBox();
				VerticalPanel vp = new VerticalPanel();
				Label resultText = new Label();
				
				resultText.setText(allData[Integer.parseInt(row.getText())].toString());
				
				final Button closeButton = new Button("Close");
				closeButton.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						result.hide();
					}
				});
				
				vp.add(resultText);
				vp.add(closeButton);
				result.add(vp);
				result.center();
				result.show();
			}
		});
		
		h1.add(dataGrid);
		h2.add(rowLabel);
		h2.add(row);
		h2.add(getMore);
	}
	
	public static void setUpGrid(String[] cols, Object[][] rows, Object[][] allInfo){
		dataGrid = new Grid(rows.length, cols.length);
		allData = allInfo;
		
		for(int i=0; i < dataGrid.getRowCount(); i++){
			for(int j=0; j < dataGrid.getColumnCount(); j++){
				if(i==0) {
					Label header = new Label(cols[j]);
					dataGrid.setWidget(i, j, header);
				} else {
					Label rowData = new Label(rows[i][j].toString());
					dataGrid.setWidget(i, j, rowData);
				}
			}
		}
	}
	
	public static Widget getLayout(){
		setUp();
		
		return vpanel;
	}
}
