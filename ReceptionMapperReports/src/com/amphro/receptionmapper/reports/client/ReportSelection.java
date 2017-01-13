package com.amphro.receptionmapper.reports.client;

import com.amphro.receptionmapper.reports.shared.Node;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

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
		
		h1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		h1.setSpacing(5);
		h2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		h2.setSpacing(5);
		h3.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		h3.setSpacing(5);
		
		final ListBox reportsList = new ListBox(false);
		reportsList.addItem("Most Recept Nodes");
		reportsList.addItem("Fastest Growing Grids");
		reportsList.addItem("Highest Populated Grids");
		reportsList.addItem("Uploads for Time of Day");
		reportsList.addItem("Growth");
		reportsList.addItem("Number of Users");
		reportsList.addItem("Signal Strength Change");
		reportsList.addItem("Network Strength Change");
		
		final Label desc = new Label("This is a description of the Most Recept Nodes");
		
		reportsList.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				switch(reportsList.getSelectedIndex()){
					case 0:
						desc.setText("This is a description of the Most Recept Nodes");
						break;
					case 1:
						desc.setText("This is a description of the Fastest Growing Grids");
						break;
					case 2:
						desc.setText("This is a description of the Highest Populated Grids");
						break;
					case 3:
						desc.setText("This is a description of the Uploads for Time of Day");
						break;
					case 4:
						desc.setText("This is a description of the Growth");
						break;
					case 5:
						desc.setText("This is a description of the Number of Users");
						break;
					case 6:
						desc.setText("This is a description of the Signal Strength Change");
						break;
					case 7:
						desc.setText("This is a description of the Network Strength Change");
						break;
				}
			}
		});
		
		Button generate = new Button("Generate Report");
		generate.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
			}
		});
		
		h1.add(reportsList);
		h3.add(desc);
		h2.add(generate);
		
		vpanel.add(h1);
		vpanel.add(h3);
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
				dataService.getNodes(
						new AsyncCallback<Node[]>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								resultText.setText("Remote Procedure Call - Failure");
								
								result.center();
								result.show();
							}

							public void onSuccess(Node[] results) {
								resultText.setHTML(results[0].toString());
								
								result.center();
								result.show();
							}
						});
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
