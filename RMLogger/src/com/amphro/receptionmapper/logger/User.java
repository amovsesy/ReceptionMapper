package com.amphro.receptionmapper.logger;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.SharedPreferences;

public class User {
	private final String RM_SERVER = "http://receptionmapper.com/services/";
	private final String RM_INSERT_SERVICE = "InsertUser.php";
	private final String RM_GET_USERNAME = "GetUserName.php";
	private final String RM_GET_NODE_COUNT = "GetUserNodeCount.php";
	private final String PARAM_START = "?";
	private final String PARAM_EQUALS = "=";
	private final String PARAM_SEPERATOR = "&";
	
	private final String USER_NAME = "username";
	private final String PHONE_ID = "phoneid";
	
	private SharedPreferences mPref;
	private String username;
	private String mId;
	
	public User(SharedPreferences pref, String phoneID) {
		mPref = pref;
		mId = phoneID;
	}
	
	public String getUsername() {
		if (username == null) {
			username = mPref.getString(USER_NAME, null);
			log(username);
			
			if (username == null) {
				String urlAsString;
				try {
					urlAsString = buildGetUsernameURL(mId);
					URL url = new URL(urlAsString);
					Scanner in = new Scanner(url.openStream());
					
					String response = in.nextLine();
					
					if (response != null && !response.trim().equals("")) {
						username = response;
					}
					log("Response: " + response);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		return username;
	}
	
	private int mCount = 0;
	
	public int getNodeCount() {
		if (mCount == 0) {
			String user = getUsername();
		
			mCount = 0;
			if (user != null) {
				String urlAsString;
				try {
					urlAsString = buildGetNodeCountURL(user);
					URL url = new URL(urlAsString);
					Scanner in = new Scanner(url.openStream());
					
					String response = in.nextLine();
					
					if (response != null && !response.trim().equals("")) {
						mCount = Integer.valueOf(response);
					}
					log("Response: " + response);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		return mCount;
	}
	
	public boolean hasUsername() {
		return getUsername() != null ? true : false;
	}
	
	public enum UserState {
		USER_EXIST("The username already exist"),
		PHONEID_EXIST("The phone id has already been registered with a username"),
		USER_CREATED("The user has been created"),
		ERROR("The user was not created. Please make sure you have an internet connection and" +
				"try again.");
		
		private String mMessage;
		
		UserState(String message) {
			mMessage = message;
		}
		
		public String getMessage() {
			return mMessage;
		}
	}
	
	public UserState insertUser(String userName, String phoneId) {
		String urlAsString, response;
		
		if (hasUsername()) {
			return UserState.USER_EXIST;
		}
		
		try {
			urlAsString = buildInsertURL(userName, phoneId);
			URL url = new URL(urlAsString);
			Scanner in = new Scanner(url.openStream());
			
			response = in.nextLine();
			log("Response: " + response);
			
			Matcher usernameMatcher = Pattern.compile("<username exist=\"([a-z]*)\" />").matcher(response);
			if (usernameMatcher.find() && usernameMatcher.group(1).equals("true")) {
				return UserState.USER_EXIST;
			}
			
			Matcher phoneMatcher = Pattern.compile("<phoneid exist=\"([a-z]*)\" />").matcher(response);
			if (phoneMatcher.find() && phoneMatcher.group(1).equals("true")) {
					return UserState.PHONEID_EXIST;
			}
			
			Matcher userMatcher = Pattern.compile("<user created=\"([a-z]*)\" />").matcher(response);
			if (userMatcher.find() && userMatcher.group(1).equals("true")) {
					mPref.edit().putString(USER_NAME, userName).commit();
					log(userName);
					return UserState.USER_CREATED;
			}
		} catch (Exception e) {
			log("Error: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		return UserState.ERROR;
		
	}
	private String buildInsertURL(String userName, String phoneId) throws UnsupportedEncodingException {
		String ret = RM_SERVER + RM_INSERT_SERVICE + PARAM_START;
		ret += USER_NAME + PARAM_EQUALS + URLEncoder.encode(String.valueOf(userName));
		ret += PARAM_SEPERATOR + PHONE_ID + PARAM_EQUALS + URLEncoder.encode(String.valueOf(phoneId));
		log("INSERT URI: " + ret);
		return ret;
	}
	private String buildGetUsernameURL(String phoneId) throws UnsupportedEncodingException {
		String ret = RM_SERVER + RM_GET_USERNAME + PARAM_START;
		ret += PHONE_ID + PARAM_EQUALS + URLEncoder.encode(String.valueOf(phoneId));
		log("INSERT URI: " + ret);
		return ret;
	}
	private String buildGetNodeCountURL(String username) throws UnsupportedEncodingException {
		String ret = RM_SERVER + RM_GET_NODE_COUNT + PARAM_START;
		ret += USER_NAME + PARAM_EQUALS + URLEncoder.encode(String.valueOf(username));
		log("INSERT URI: " + ret);
		return ret;
	}
	
	private static final String DEBUG_TAG = "User";
	
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
