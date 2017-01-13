package com.amphro.receptionmapper.reports.client.callbacks;

import java.util.ArrayList;
import java.util.List;

import com.amphro.receptionmapper.reports.shared.Node;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;

public class NodeAsyncCB implements AsyncCallback<Node[]> {
	
	private HTML mHtml = null;
	private DialogBox mBox = null;
	
	private List<Node> mNodes;
	
	public NodeAsyncCB(List<Node> nodes) {
		this.mNodes = nodes;
	}
	
	public NodeAsyncCB(HTML html, DialogBox box) {
		this.mHtml = html;
		this.mBox = box;
		this.mNodes = new ArrayList<Node>();
	}
	
	public void onFailure(Throwable caught) {
		// Show the RPC error message to the user
		displayHtml(caught.getMessage());
		showBox();
	}

	public void onSuccess(Node[] results) {
		final Node firstNode = results[0];
		
		Geocoder gc = new Geocoder();
		LatLng latLon;
		
		if (mHtml != null) {
			latLon = LatLng.newInstance(firstNode.getLatitude(), firstNode.getLongitude());
			gc.getLocations(latLon, new LocationCB(mHtml));
		}
		
		for (Node node : results) {
			latLon = LatLng.newInstance(node.getLatitude(), node.getLongitude());
			gc.getLocations(latLon, new LocationCB(node));
			mNodes.add(node);
		}
		
		displayHtml(firstNode.toString());
		showBox();
	}
	
	private void displayHtml(String html) {
		if (mHtml != null) {
			mHtml.setText(html);
		}
	}
	
	private void showBox() {
		if (mBox != null) {
			mBox.center();
			mBox.show();
		}
	}
}
