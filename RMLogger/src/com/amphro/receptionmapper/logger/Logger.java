package com.amphro.receptionmapper.logger;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.amphro.receptionmapper.logger.location.RMLocationManager;
import com.amphro.receptionmapper.logger.node.Node;
import com.amphro.receptionmapper.logger.node.NodeUploader;
import com.amphro.receptionmapper.logger.phone.PhoneData;

public class Logger extends Activity {
	/* The tag for logging debug messages */
	private final static String DEBUG_TAG = "ReceptionMapperLogger";
	private final static String LOG_NAME = "Logger";
	private TextView mTotalUploadView;
	
	private int mCount;

	/*Interval time in milliseconds between executions of task*/
	final int repeatInterval = 60 *1000;
	public static int uploadNumber = 0;
	
	private static RMLocationManager mManager;
	private static NodeUploader mUploader;
	private static PhoneData mPhoneData;
	
	private ListView mNodeList;
	//private UploadTask mUpload;
	private User mUser;
	private ArrayList<String> mNodes;
	private SharedPreferences mPref;
	
	private final static String COUNT = "count";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTotalUploadView = (TextView)findViewById(R.id.TextView02);
        mNodeList = (ListView)findViewById(R.id.nodeList);
        
        mPref = getPreferences(MODE_PRIVATE);
        
        if (mUser == null) {
        	mUser = new User(mPref, Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID));
        }
        
        mNodes = new ArrayList<String>();
        
        mCount = mUser.getNodeCount();
        
        if (mCount == 0) {
        	mCount = mPref.getInt(COUNT, 0);
        } else {
        	mPref.edit().putInt(COUNT, mCount).commit();
        }
        
        if (mUser.hasUsername()) {
        	setText();
        	startLogging();
        } else {
        	dialog = new UserDialog(this, mUser);
        	dialog.show();
        }
        
       
    }
	
	public void setText() {
		mTotalUploadView.setText(mUser.getUsername() + " has " + mCount + " uploads.");
	}
	
	private Handler mHandler = new Handler();
	
	public void startLogging() {
		if (!isLogging()) {
			mPref.edit().putBoolean(LOGGING, true).commit();
			
			mManager = new RMLocationManager(this);
			mUploader = new NodeUploader(this);
			mPhoneData = new PhoneData(this);
	      
			mHandler.removeCallbacks(mUploadTask);
			mHandler.postDelayed(mUploadTask, repeatInterval);
	      
			log("Logging...");
		}
	}
	
	public void stopLogging() {
		if (mHandler != null) {
			mHandler.removeCallbacks(mUploadTask);
		}
		if (mManager != null) {
			mManager.destroyManager();
		}
		if (mPhoneData != null) {
			mPhoneData.destroyPhoneData();
		}
		mPref.edit().putBoolean(LOGGING, false).commit();
		log("Stoped logging");
	}
	
	public boolean isLogging() {
		return mPref.getBoolean(LOGGING, false);
	}
	
	private UserDialog dialog;
	
	private Runnable mUploadTask = new Runnable() {
		@Override
		public void run() {
			log("Handler is called");
			if (mManager.hasNewLocation()) {
				log("Uploading node...");
				uploadNumber++;
				
				Node n = new Node(mManager.getLocation(), mUser, mPhoneData);
				int uploaded = mUploader.uploadNode(n);
				if (uploaded > 0) {
					mNodes.add("Success: " + n);
					mCount += uploaded;
					setText();
					log("Success: Count - " + mCount + "   List Count - " + mNodes.size());
				} else {
					mNodes.add("Failed");
				}
				//setListView();
			}
			
			mHandler.postAtTime(this, SystemClock.uptimeMillis()+(repeatInterval/4));
		}
	};
	
	private void setListView() {
		mNodeList.setAdapter(new ArrayAdapter<Object>(this, R.id.TextView01, mNodes.toArray()));
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (dialog != null && dialog.isShowing()) {
			dialog.setUsername(savedInstanceState.getString("usernameEditBox"));
		}
		mCount = mPref.getInt(COUNT, 0);
		setText();
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (dialog != null && dialog.isShowing()) {
			outState.putString("usernameEditBox", dialog.getUsername());
		}
		mPref.edit().putInt(COUNT, mCount).commit();
		
		super.onSaveInstanceState(outState);
	}

	private final String LOGGING = "logging";
	
	@Override
	protected void onDestroy() {
		stopLogging();
		super.onDestroy();
	}

	private String getName() {
		return Logger.class.getName();
	}
	
	private void log(String message) {
		log(LOG_NAME, message);
	}
	
	public static void log(String name, String message) {
		Log.d(DEBUG_TAG, name + ": " + message);
	}
}