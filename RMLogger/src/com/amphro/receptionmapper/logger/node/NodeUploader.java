package com.amphro.receptionmapper.logger.node;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.amphro.receptionmapper.logger.Logger;

/**
 * Node Uploader uploads nodes to the server. If the server is not
 * accessible, then the nodes are inserted into the database to be
 * uploaded later.
 * 
 * @author Thomas Dvornik
 *
 */
public class NodeUploader {
	/* The tag for logging debug messages */
	private final static String DEBUG_TAG = "Node Uploader";

	private NodeProvider mProvider;
	
	public NodeUploader(Context context) {
		mProvider = new NodeProvider(context);
	}
	
	/**
	 * Upload data to ReceptionMapper.com
	 * @param node Node to be uploaded
	 */
	public int uploadNode(Node node) {
		ContentValues cv = new ContentValues();
		cv.put(Node.LATITUDE, node.getLatitude());
		cv.put(Node.LONGITUDE, node.getLongitude());
		cv.put(Node.NETWORK_TYPE, node.getNetworkType());
		cv.put(Node.CARRIER, node.getCarrier());
		cv.put(Node.SIGNAL_STRENGTH, node.getSignalStrength());
		cv.put(Node.PHONE, node.getPhone());
		cv.put(Node.MANUFACTURER, node.getManufacturer());
		cv.put(Node.USER, node.getUsername());
		cv.put(Node.GSM, node.getGsm());
		cv.put(Node.GSMERROR, node.getGsmError());
		cv.put(Node.CDMA, node.getCdma());
		cv.put(Node.CDMAERROR, node.getCdmaError());
		cv.put(Node.EVDO, node.getEvdo());
		cv.put(Node.EVDOERROR, node.getEvdoError());
		
		cv.put(Node.PHONETYPE, node.getPhoneType());
		cv.put(Node.DEVICEID, node.getDeviceID());
		cv.put(Node.NETWORK_TYPE_INT, node.getNetworkTypeInt());
		Uri uri = mProvider.insert(null, cv);
		
		//log("Uploaded Node: " + node);
		int uploaded = uri != null ? Integer.valueOf(uri.toString()) : 0;
		log ("Uploaded: " + uploaded);
		return uploaded;
	}

	/**
	 * Log a debug message using the ReceptionMapperService tag
	 * 
	 * @param message
	 *            The message to display
	 */
	private void log(String message) {
		Logger.log(DEBUG_TAG, message);
	}
}
