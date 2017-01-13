package com.amphro.receptionmapper.reports.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.Window;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class RMReportsIndex implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private final DataServiceAsync dataService = GWT.create(DataService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		Button sendButton = new Button("Generate Report", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.assign("index.html");
			}
		});
		
		ListBox reports = new ListBox();
		reports.addItem("Report1");
		reports.addItem("Report2");
		reports.addItem("Report3");
		
		reports.setVisibleItemCount(1);

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootLayoutPanel.get().clear();
		RootPanel.get("reportsListBoxContainer").add(reports);
		RootPanel.get("sendButtonContainer").add(sendButton);

		// Focus the cursor on the reports list box when the app loads
		reports.setFocus(true);
	}
}
