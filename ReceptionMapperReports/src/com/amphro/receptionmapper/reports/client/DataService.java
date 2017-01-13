package com.amphro.receptionmapper.reports.client;

import com.amphro.receptionmapper.reports.shared.Node;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("dataService")
public interface DataService extends RemoteService {
	Node[] getNodes();
}