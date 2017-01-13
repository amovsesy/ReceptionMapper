package com.amphro.receptionmapper.reports.client;

import java.sql.Date;

import com.amphro.receptionmapper.reports.client.callbacks.NodeAsyncCB;
import com.amphro.receptionmapper.reports.client.reportgriddata.FastestGrowingGrids;
import com.amphro.receptionmapper.reports.client.reportgriddata.HighestPopulatedGrids;
import com.amphro.receptionmapper.reports.client.reportgriddata.MostRecentNodes;
import com.amphro.receptionmapper.reports.client.reportgriddata.RelativeUploads;
import com.amphro.receptionmapper.reports.shared.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class ReportSelection {
	private static VerticalPanel vpanel;
	private final static DataServiceAsync dataService = GWT.create(DataService.class);
	
	private static void setUp(){
		
		if(vpanel == null)
			vpanel = new VerticalPanel();
		
		vpanel.clear();
		
		HorizontalPanel h1 = new HorizontalPanel();
		HorizontalPanel h2 = new HorizontalPanel();
		HorizontalPanel h3 = new HorizontalPanel();
		
		VerticalPanel options = new VerticalPanel();
		HorizontalPanel o1 = new HorizontalPanel();
		HorizontalPanel o2 = new HorizontalPanel();
		HorizontalPanel o3 = new HorizontalPanel();
		HorizontalPanel o4 = new HorizontalPanel();
		
		h1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		h1.setSpacing(5);
		h2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		h2.setSpacing(5);
		h3.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		h3.setSpacing(5);
		
		options.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		options.setSpacing(5);
		o1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		o1.setSpacing(5);
		o2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		o2.setSpacing(5);
		o3.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		o3.setSpacing(5);
		o4.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		o4.setSpacing(5);
		
		final ListBox reportsList = new ListBox(false);
		reportsList.addItem("Most Recent Nodes");
		reportsList.addItem("Fastest Growing Grids");
		reportsList.addItem("Highest Populated Grids");
		reportsList.addItem("Uploads for Time of Day");
		//reportsList.addItem("Growth");
		//reportsList.addItem("Number of Users");
		//reportsList.addItem("Signal Strength Change");
		//reportsList.addItem("Network Strength Change");
		
		final Label desc = new Label("This is a description of the Most Recept Nodes");
		
		final Label o1Label = new Label("maxNodes");
		final TextBox o1Box = new TextBox();
		o1Box.setText("10");
		final Label o2Label = new Label("o2");
		final TextBox o2Box = new TextBox();
		o2Box.setText("15");
		final Label o3Label = new Label("maxDate");
		final DateBox o3Box = new DateBox();
		o3Box.setValue(new Date(System.currentTimeMillis()), true);
		final Label o4Label = new Label("o4");
		final DateBox o4Box = new DateBox();
		o4Box.setValue(new Date(System.currentTimeMillis()), true);
		
		o1Label.setVisible(true);
		o1Box.setVisible(true);
		o2Label.setVisible(false);
		o2Box.setVisible(false);
		o3Label.setVisible(true);
		o3Box.setVisible(true);
		o4Label.setVisible(false);
		o4Box.setVisible(false);
		
		reportsList.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				switch(reportsList.getSelectedIndex()){
					case 0:
						desc.setText("This is a description of the Most Recept Nodes");
						o1Label.setText("maxNodes");
						o1Box.setText("10");
						o3Label.setText("maxDate");
						o3Box.setValue(new Date(System.currentTimeMillis()));
						o1Label.setVisible(true);
						o1Box.setVisible(true);
						o2Label.setVisible(false);
						o2Box.setVisible(false);
						o3Label.setVisible(true);
						o3Box.setVisible(true);
						o4Label.setVisible(false);
						o4Box.setVisible(false);
						break;
					case 1:
						desc.setText("This is a description of the Fastest Growing Grids");
						o1Label.setText("maxGridLevel");
						o1Box.setText("5");
						o2Label.setText("gridsPerLevel");
						o2Box.setText("15");
						o3Label.setText("minTime");
						o3Box.setValue(new Date(0));
						o4Label.setText("maxTime");
						o4Box.setValue(new Date(System.currentTimeMillis()));
						o1Label.setVisible(true);
						o1Box.setVisible(true);
						o2Label.setVisible(true);
						o2Box.setVisible(true);
						o3Label.setVisible(true);
						o3Box.setVisible(true);
						o4Label.setVisible(true);
						o4Box.setVisible(true);
						break;
					case 2:
						desc.setText("This is a description of the Highest Populated Grids");
						o1Label.setText("maxGridLevel");
						o1Box.setText("5");
						o2Label.setText("gridsPerLevel");
						o2Box.setText("15");
						o1Label.setVisible(true);
						o1Box.setVisible(true);
						o2Label.setVisible(true);
						o2Box.setVisible(true);
						o3Label.setVisible(false);
						o3Box.setVisible(false);
						o4Label.setVisible(false);
						o4Box.setVisible(false);
						break;
					case 3:
						desc.setText("This is a description of the Uploads for Time of Day");
						o1Label.setText("interval(mins)");
						o1Box.setText("1");
						o3Label.setText("minDate");
						o3Box.setValue(new Date(0));
						o4Label.setText("maxDate");
						o4Box.setValue(new Date(System.currentTimeMillis()));
						o1Label.setVisible(true);
						o1Box.setVisible(true);
						o2Label.setVisible(false);
						o2Box.setVisible(false);
						o3Label.setVisible(true);
						o3Box.setVisible(true);
						o4Label.setVisible(true);
						o4Box.setVisible(true);
						break;
					case 4:
						desc.setText("This is a description of the Growth");
						o1Label.setVisible(false);
						o1Box.setVisible(false);
						o2Label.setVisible(false);
						o2Box.setVisible(false);
						o3Label.setVisible(false);
						o3Box.setVisible(false);
						o4Label.setVisible(false);
						o4Box.setVisible(false);
						break;
					case 5:
						desc.setText("This is a description of the Number of Users");
						o1Label.setVisible(false);
						o1Box.setVisible(false);
						o2Label.setVisible(false);
						o2Box.setVisible(false);
						o3Label.setVisible(false);
						o3Box.setVisible(false);
						o4Label.setVisible(false);
						o4Box.setVisible(false);
						break;
					case 6:
						desc.setText("This is a description of the Signal Strength Change");
						o1Label.setVisible(false);
						o1Box.setVisible(false);
						o2Label.setVisible(false);
						o2Box.setVisible(false);
						o3Label.setVisible(false);
						o3Box.setVisible(false);
						o4Label.setVisible(false);
						o4Box.setVisible(false);
						break;
					case 7:
						desc.setText("This is a description of the Network Strength Change");
						o1Label.setVisible(false);
						o1Box.setVisible(false);
						o2Label.setVisible(false);
						o2Box.setVisible(false);
						o3Label.setVisible(false);
						o3Box.setVisible(false);
						o4Label.setVisible(false);
						o4Box.setVisible(false);
						break;
				}
			}
		});
		
		Button generate = new Button("Generate Report");
		generate.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {				
				switch(reportsList.getSelectedIndex()){
					case 0:		
						new MostRecentNodes(dataService);
						break;
					case 1:
						new FastestGrowingGrids(Integer.parseInt(o1Box.getText()), Integer.parseInt(o2Box.getText()), 
								new Date(o3Box.getValue().getTime()), new Date(o4Box.getValue().getTime()), dataService);
						break;
					case 2:
						new HighestPopulatedGrids(Integer.parseInt(o1Box.getText()), Integer.parseInt(o2Box.getText()), dataService);
						break;
					case 3:
						new RelativeUploads(new Date(o3Box.getValue().getTime()), new Date(o4Box.getValue().getTime()), Integer.parseInt(o1Box.getText()), dataService);
						break;
					case 4:
						break;
					case 5:
						break;
					case 6:
						break;
					case 7:
						break;
				}
				DOM.setInnerHTML(RootPanel.get(Constants.LOADER_DIV).getElement(), Constants.LOADING_REPORT);
				//create a loader here while waiting for 
				//report to generate
			}
		});
		
		h1.add(reportsList);
		h3.add(desc);
		options.add(o1);
		o1.add(o1Label);
		o1.add(o1Box);
		options.add(o2);
		o2.add(o2Label);
		o2.add(o2Box);
		options.add(o3);
		o3.add(o3Label);
		o3.add(o3Box);
		options.add(o4);
		o4.add(o4Label);
		o4.add(o4Box);
		h2.add(generate);
		
		vpanel.add(h1);
		vpanel.add(h3);
		vpanel.add(options);
		vpanel.add(h2);
		
		// ************* TESTING *****************
		//testing section to be removed on deployment
		//this is for Wes and Nate to test their services
		//
		//
		final DialogBox result = new DialogBox();
		final VerticalPanel vp = new VerticalPanel();
		final HTML resultText = new HTML();
		Button testButton = new Button("testing button");
		
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
		result.hide();
		
		testButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				//change the text of the resultText label in the onFailure and
				//onSuccess methods
				//Also change the service and method you call
				//
				dataService.getNodes(new NodeAsyncCB(resultText, result));
			}
		});

		vpanel.add(testButton);
		//
		//
		//end of testing section
		
	}
	
	public static Widget getLayout(){
		setUp();
		
		return vpanel;
	}
}
