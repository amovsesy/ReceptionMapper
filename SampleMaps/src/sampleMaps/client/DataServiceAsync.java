package sampleMaps.client;

import java.util.List;

import sampleMaps.shared.ReceptionGridPoint;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataServiceAsync {
	void getCarriers(AsyncCallback<List<String>> callback);
	
	void getReceptionArea(double topLat, double topLong, double botLat, double botLong, int gridLevel, List<String> carriers,  AsyncCallback<List<ReceptionGridPoint>> callback);
}
