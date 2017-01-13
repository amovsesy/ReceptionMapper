package com.amphro.receptionmapper.reports.client;

import com.amphro.receptionmapper.reports.shared.Constants;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class RMReport implements EntryPoint {
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
		DOM.setInnerHTML(RootPanel.get(Constants.LOADER_DIV).getElement(), "");
	}
}
