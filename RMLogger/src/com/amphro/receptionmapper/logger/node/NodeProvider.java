package com.amphro.receptionmapper.logger.node;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.amphro.receptionmapper.logger.Logger;
import com.amphro.receptionmapper.logger.database.QueueDB;
import com.amphro.receptionmapper.logger.database.Request;

public class NodeProvider extends ContentProvider {
	private final String RM_SERVER = "http://receptionmapper.com/services/";
	private final String RM_INSERT_SERVICE = "InsertNode.php";
	private final String RM_GET_SERVICE = "getNodes.php";
	private final String PARAM_START = "?";
	private final String LIST_PARAM = "list";
	private final String PARAM_EQUALS = "=";
	private final String PARAM_SEPERATOR = "&";

	private QueueDB queueDB;
	
	public NodeProvider(Context t){
		queueDB = new QueueDB(t);
		queueDB.open();
	}
	
	public void closeDB(){
		queueDB.close();
	}
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		//Delete is not supported. The server will take care of removing Nodes 
		//from the database.
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		return null;
	}
	
	@Override
	public Uri insert(Uri arg0, ContentValues nodeArg) {
		float lat = nodeArg.getAsFloat(Node.LATITUDE);
		float lon = nodeArg.getAsFloat(Node.LONGITUDE);
		String network = nodeArg.getAsString(Node.NETWORK_TYPE);
		String provider = nodeArg.getAsString(Node.CARRIER);
		String phone = nodeArg.getAsString(Node.PHONE);
		String manufac = nodeArg.getAsString(Node.MANUFACTURER);
		String user = nodeArg.getAsString(Node.USER);
		int sigStr = nodeArg.getAsInteger(Node.SIGNAL_STRENGTH);
		int gsm = nodeArg.getAsInteger(Node.GSM);
		int gsmError = nodeArg.getAsInteger(Node.GSMERROR);
		int cdma = nodeArg.getAsInteger(Node.CDMA);
		int cdmaError = nodeArg.getAsInteger(Node.CDMAERROR);
		int evdo = nodeArg.getAsInteger(Node.EVDO);
		int evdoError = nodeArg.getAsInteger(Node.EVDOERROR);
		
		String deviceID = nodeArg.getAsString(Node.DEVICEID);
		String phoneType = nodeArg.getAsString(Node.PHONETYPE);
		int networkInt = nodeArg.getAsInteger(Node.NETWORK_TYPE_INT);
		String response;
		String urlAsString = "";
		boolean sent = false;
		
		try {
			urlAsString = buildInsertURL(lat, lon, user, network, provider, phone, phoneType, deviceID, networkInt, manufac, sigStr, gsm, gsmError, cdma, cdmaError, evdo, evdoError);
			
			if(Logger.uploadNumber == 4){
				Logger.uploadNumber = 0;
				
				URL url = buildBatchUploadString();
				Scanner in = new Scanner(url.openStream());
				response = in.nextLine();
				
				if (response.toUpperCase().contains("SUCCEEDED")) {
					sent = true;
					queueDB.removeAllRequest();
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (urlAsString.length() == 0 && !sent) {
				queueDB.insertRequest(urlAsString);
			}
		}
		
		return null;
	}

	@Override
	public boolean onCreate() {
		//Don't know if there is anything we need to do here...
		return false;
	}
	
	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4) {
		
		try {
			String urlAsString = buildGetURL(-90, -180, 90, 180, 5);
			URL url = new URL(urlAsString);
			Scanner in = new Scanner(url.openStream());
		
			//Cursor<NodeProvider> ret = new Cursor<NodeProvider>();

			String response = in.nextLine();//"Insert Succeeded!";
			
			if (response.toUpperCase().contains("SUCCEEDED")) {
				Cursor c = queueDB.getAllRequests();
				ArrayList<Long> indexesToRemove = new ArrayList<Long>();
				
				while(c != null && c.moveToNext()){
					Request r = QueueDB.getRequestFromCursor(c);
					
					if(r != null){
						url = new URL(r.getRequest());
						in = new Scanner(url.openStream());
						
						String res = in.nextLine();
						
						if(res.toUpperCase().contains("SUCCEEDED")){
							indexesToRemove.add(r.getID());
						} else {
							break;
						}
					}
				}
				
				for(int i=0; i < indexesToRemove.size(); i++){
					queueDB.removeRequest(indexesToRemove.get(i));
				}
			} else {
				response = "Insert failed. Sorry.";
				queueDB.insertRequest(urlAsString);
			}
			
			log("Response: " + response);
		} catch (Exception e) {
			log("Error: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		/* Updated is not supported. Once a node is inserted, it should never be modified.
		 * If this changes, the server should handle removing and updated nodes. 
		 * For example, if there is a node already in a specific location for a provider, 
		 * then the server can update that row with the new network type and signal strength.
		*/
		return 0;
	}
	
	private static String UPPER_LEFT_LATITUDE = "ullat";
	private static String UPPER_LEFT_LONGITUDE = "ullon";
	private static String BOTTOM_RIGHT_LATITUDE = "brlat";
	private static String BOTTOM_RIGHT_LONGITUDE = "brlon";
	private static String LEVEL = "level";
	
	private String buildGetURL(float ullat, float ullon, float brlat, float brlon, int level) throws UnsupportedEncodingException {
		String ret = RM_SERVER + RM_GET_SERVICE + PARAM_START;
		ret += UPPER_LEFT_LATITUDE + PARAM_EQUALS + encode(String.valueOf(ullat));
		ret += PARAM_SEPERATOR + UPPER_LEFT_LONGITUDE + PARAM_EQUALS + encode(String.valueOf(ullon));
		ret += PARAM_SEPERATOR + BOTTOM_RIGHT_LATITUDE + PARAM_EQUALS + encode(String.valueOf(brlat));
		ret += PARAM_SEPERATOR + BOTTOM_RIGHT_LONGITUDE + PARAM_EQUALS + encode(String.valueOf(brlon));
		ret += PARAM_SEPERATOR + LEVEL + PARAM_EQUALS + encode(String.valueOf(level));
		log("GET URI: " + ret);
		return ret;
	}
	
	/**
	 * Builds a URL to insert a Node on http://www.receptionmapper.com
	 * This method assumes all parameters have already been checked.
	 * 
	 * @param lat The latitude
	 * @param lon The Longitude
	 * @param nt The network type
	 * @param p The service provider
	 * @param sigStr The signal strength
	 * @return A valid URL, otherwise an exception is thrown
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException 
	 */
	private String buildInsertURL(float lat, float lon, String user, String nt, String carrier, String phone, String phoneType, String deviceID, int networkInt, String manufac, int sigStr, int gsm, int gsmError, int cdma, int cdmaError, int evdo, int evdoError) throws UnsupportedEncodingException {
		String ret = Node.LATITUDE + PARAM_EQUALS + encode(String.valueOf(lat));
		ret += PARAM_SEPERATOR + Node.LONGITUDE + PARAM_EQUALS + encode(String.valueOf(lon));
		ret += PARAM_SEPERATOR + Node.CARRIER + PARAM_EQUALS + encode(carrier);
		ret += PARAM_SEPERATOR + Node.PHONE + PARAM_EQUALS + encode(phone);
		ret += PARAM_SEPERATOR + Node.PHONETYPE + PARAM_EQUALS + encode(phoneType);
		ret += PARAM_SEPERATOR + Node.DEVICEID + PARAM_EQUALS + encode(deviceID);
		ret += PARAM_SEPERATOR + Node.NETWORK_TYPE_INT + PARAM_EQUALS + networkInt;
		ret += PARAM_SEPERATOR + Node.MANUFACTURER + PARAM_EQUALS + encode(manufac);
		ret += PARAM_SEPERATOR + Node.NETWORK_TYPE + PARAM_EQUALS + encode(nt);
		ret += PARAM_SEPERATOR + Node.USER + PARAM_EQUALS + encode(user);
		ret += PARAM_SEPERATOR + Node.SIGNAL_STRENGTH + PARAM_EQUALS + encode(String.valueOf(sigStr));
		ret += PARAM_SEPERATOR + Node.GSM + PARAM_EQUALS + encode(String.valueOf(gsm));
		ret += PARAM_SEPERATOR + Node.GSMERROR + PARAM_EQUALS + encode(String.valueOf(gsmError));
		ret += PARAM_SEPERATOR + Node.CDMA + PARAM_EQUALS + encode(String.valueOf(cdma));
		ret += PARAM_SEPERATOR + Node.CDMAERROR + PARAM_EQUALS + encode(String.valueOf(cdmaError));
		ret += PARAM_SEPERATOR + Node.EVDO + PARAM_EQUALS + encode(String.valueOf(evdo));
		ret += PARAM_SEPERATOR + Node.EVDOERROR + PARAM_EQUALS + encode(String.valueOf(evdoError));
		log("INSERT URI: " + ret);
		return ret;
	}
	
	private URL buildBatchUploadString() {
		HashMap<String, String> nodes = new HashMap<String, String>();
		Cursor c = queueDB.getAllRequests();
		String key;
		
		int count = 1;
		
		while(c != null && c.moveToNext()){
			Request r = QueueDB.getRequestFromCursor(c);
			
			if(r != null){
				key = "node" + count;
				count++;
				nodes.put(key, r.getRequest());
			}
		}
		
		JSONObject jsonList = new JSONObject();
		String url = RM_SERVER + RM_INSERT_SERVICE + PARAM_START + LIST_PARAM + PARAM_EQUALS;
		
		try {
			jsonList.put("nodes", nodes);
			url += URLEncoder.encode(jsonList.toString(),"UTF-8");
			return new URL(url);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String encode(String arg) throws UnsupportedEncodingException {
		return URLEncoder.encode(arg, "UTF-8");
	}
	
	private final String DEBUG_TAG = "Node Provider";
	
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
