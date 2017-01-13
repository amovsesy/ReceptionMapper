package com.amphro.receptionmapper.reports.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.amphro.receptionmapper.reports.client.Login;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ReceptionMapperReports implements EntryPoint {
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final DataServiceAsync greetingService = GWT
			.create(DataService.class);

private final String id = "panelContainer";
	
	public static RootPanel root;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		root = RootPanel.get(id);
		Login.getLayout();
	}
}
