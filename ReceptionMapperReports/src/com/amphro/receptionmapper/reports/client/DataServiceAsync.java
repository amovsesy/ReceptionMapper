package com.amphro.receptionmapper.reports.client;

import com.amphro.receptionmapper.reports.shared.Node;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>DataService</code>.
 */
public interface DataServiceAsync {
	void getNodes(AsyncCallback<Node[]> callback);
}