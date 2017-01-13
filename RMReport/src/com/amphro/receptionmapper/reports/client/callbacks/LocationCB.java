package com.amphro.receptionmapper.reports.client.callbacks;

import com.amphro.receptionmapper.reports.shared.Node;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.user.client.ui.HTML;

public class LocationCB implements LocationCallback {
	
	private HTML mHtml = null;
	private Node mNode = null;
	
	public LocationCB(HTML html) {
		this.mHtml = html;
	}
	
	public LocationCB(Node node) {
		this.mNode = node;
	}
	
	public LocationCB(HTML html, Node node) {
		this.mHtml = html;
		this.mNode = node;
	}
	
	@Override
	public void onFailure(int statusCode) {
		String noAddress = "Couldn't get address.";
		
		setNodeAddress(noAddress, "", "");
		addHtml(noAddress);
	}

	@Override
	public void onSuccess(JsArray<Placemark> locations) {
		if (locations != null && locations.length() > 0) {
			Placemark pm = locations.get(0);
			String bestAddress = pm.getAddress() + " " + pm.getCountry();
			
			setNodeAddress(bestAddress, pm.getCity(), pm.getCountry());
			addHtml(bestAddress);
		}
	}
	
	private void setNodeAddress(String address, String city, String country) {
		if (mNode != null) {
			mNode.setAddress(address);
			mNode.setCity(city);
			mNode.setCountry(country);
		}
	}
	
	private void addHtml(String html) {
		if (mHtml != null) {
			mHtml.setHTML(mHtml.getHTML() + html);
		}
	}
}
