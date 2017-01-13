package com.amphro.receptionmapper.node;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.amphro.receptionmapper.database.QueueDB;
import com.amphro.receptionmapper.database.Request;
import com.amphro.receptionmapper.location.Location;
import com.amphro.receptionmapper.phone.NetworkType;

public class NodeProvider extends ContentProvider {
	private final String RM_SERVER = "http://receptionmapper.com/services/";
	private final String RM_INSERT_SERVICE = "insertNode.php";
	private final String RM_GET_SERVICE = "getNodes.php";
	private final String PARAM_START = "?";
	private final String PARAM_EQUALS = "=";
	private final String PARAM_SEPERATOR = "&";
	
	/**
	 * Database of requests the user can view, add to, and remove from.
	 */
	protected QueueDB queueDB;
	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues nodeArg) {
		float lat = nodeArg.getAsFloat(Node.LATITUDE);
		float lon = nodeArg.getAsFloat(Node.LONGITUDE);
		NetworkType nt = NetworkType.valueOf(nodeArg.getAsString(Node.NETWORK_TYPE));
		String provider = nodeArg.getAsString(Node.CARRIER);
		String phone = nodeArg.getAsString(Node.PHONE);
		String manufac = nodeArg.getAsString(Node.MANUFACTURER);
		int sigStr = nodeArg.getAsInteger(Node.SIGNAL_STRENGTH);	
		
		try {
			String urlAsString = buildInsertURL(lat, lon, nt, provider, phone, manufac, sigStr);
			URL url = new URL(urlAsString);
			Scanner in = new Scanner(url.openStream());
		
			/* Change this later because we can't throw a toast inside the 
			 * content provide. Handle errors in here or figure out a 
			 * uri to return that will represent an error.
			 */
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
			
			Log.d("ReceptionMapper NodeProvider", response);
			//Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT);
			//toast.show();
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public boolean onCreate() {
		//Don't know if there is anything we need to do here...
		return false;
	}

	public float hashGridLoc(int x, int y) {
		return (((x*y)+(float)x/(float)y)%x)*(y%x);
	}
	public HashMap<String, Float> getGrids(float ullat, float ullon, float brlat, float brlon, int level) {
		HashMap<String, Float> ret = new HashMap<String, Float>();
		
		try {
			String urlAsString = buildGetURL(-90, -180, 90, 180, 5);
			URL url = new URL(urlAsString);
			Scanner in = new Scanner(url.openStream());
			String response;
			while (in.hasNextLine()) {
				response = in.nextLine();
				String s[] = response.split(",");
				if (s != null && s[0] != null && s[1] != null && s[2] != null) {
				Log.d("RECEPTIONMAPPER GET GRIDS", s[0] + ", " + s[1] + ", " + s[2]);
				ret.put(Location.makeKey(Integer.valueOf(s[0]), Integer.valueOf(s[1])), Float.valueOf(s[2]));
				}
			}
		} catch (Exception e) {
			Log.d("RECEPTIONMAPPER GET GRIDS EXCEPTION", "ok");
		}
		
		return ret;
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
			
			Log.d("ReceptionMapper NodeProvider", response);
			//Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT);
			//toast.show();
		} catch (Exception e) {
			
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
		Log.d("ReceptionMapper ContentProvider GET URI", ret);
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
	private String buildInsertURL(float lat, float lon, NetworkType nt, String carrier, String phone, String manufac, int sigStr) throws UnsupportedEncodingException {
		String ret = RM_SERVER + RM_INSERT_SERVICE + PARAM_START;
		ret += Node.LATITUDE + PARAM_EQUALS + encode(String.valueOf(lat));
		ret += PARAM_SEPERATOR + Node.LONGITUDE + PARAM_EQUALS + encode(String.valueOf(lon));
		ret += PARAM_SEPERATOR + Node.CARRIER + PARAM_EQUALS + encode(carrier);
		ret += PARAM_SEPERATOR + Node.PHONE + PARAM_EQUALS + encode(phone);
		ret += PARAM_SEPERATOR + Node.MANUFACTURER + PARAM_EQUALS + encode(manufac);
		ret += PARAM_SEPERATOR + Node.NETWORK_TYPE + PARAM_EQUALS + encode(nt.toString());
		ret += PARAM_SEPERATOR + Node.SIGNAL_STRENGTH + PARAM_EQUALS + encode(String.valueOf(sigStr));
		Log.d("ReceptionMapper ContentProvider INSERT URI", ret);
		return ret;
	}
	
	private String encode(String arg) throws UnsupportedEncodingException {
		return URLEncoder.encode(arg, "UTF-8");
	}

}
