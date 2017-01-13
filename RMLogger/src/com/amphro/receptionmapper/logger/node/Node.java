package com.amphro.receptionmapper.logger.node;

import android.location.Location;

import com.amphro.receptionmapper.logger.User;
import com.amphro.receptionmapper.logger.phone.NetworkType;
import com.amphro.receptionmapper.logger.phone.PhoneData;

/**
 * Represents an entry node from the reception mapper database.
 * 
 * @author Thomas Dvornik
 *
 */
public class Node {
	/**
	 * Do not change these values. They are what the server uses. 
	 * Ideally we would pull the names via a service from 
	 * Receptionmapper.com. We can add that later. 
	 */
	public final static String LATITUDE = "latitude";
	public final static String LONGITUDE = "longitude";
	public final static String CARRIER = "carrier";
	public final static String NETWORK_TYPE = "network";
	public final static String SIGNAL_STRENGTH = "signalstrength";
	public final static String PHONE = "phone";
	public final static String MANUFACTURER = "manufacturer";
	public final static String USER = "user";
	public final static String GSM = "gsm";
	public final static String GSMERROR = "gsmerror";
	public final static String CDMA = "cdma";
	public final static String CDMAERROR = "cdmaerror";
	public final static String EVDO = "evdo";
	public final static String EVDOERROR = "evdoerror";
	public final static String PHONETYPE = "phonetype";
	public final static String DEVICEID = "deviceid";
	public final static String NETWORK_TYPE_INT = "networkint";
	
	private Location mLocation;
	
	private String mCarrier;
	private Integer mSignalStrength;
	private Integer mGsm;
	private Integer mGsmError;
	private Integer mCdma;
	private Integer mCdmaError;
	private Integer mEvdo;
	private Integer mEvdoError;
	private NetworkType mType;
	private int mTypeInt;
	private String mPhoneType;
	private String mDeviceID;
	private String mManufacturer;
	private String mPhone;
	private User mUser;
	
	public Node(Location location, User user, PhoneData phoneData) {
		mLocation = location;
		mCarrier = phoneData.getCurrentProvider();
		mSignalStrength = phoneData.getSignalStrength();
		mGsm = phoneData.getGsm();
		mGsmError = phoneData.getGsmError();
		mCdma = phoneData.getCdma();
		mCdmaError = phoneData.getCdmaError();
		mEvdo = phoneData.getEvdo();
		mEvdoError = phoneData.getEvdoError();
		mType = phoneData.getNetworkType();
		mTypeInt = phoneData.getNetworkTypeInt();
		mPhoneType = phoneData.getPhoneType();
		mDeviceID = phoneData.getDeviceID();
		mManufacturer = phoneData.getManufacturer();
		mPhone = phoneData.getPhone();
		mUser = user;
	}
	
	public String getUsername() {
		return mUser.getUsername();
	}
	public String getPhoneType() {
		return mPhoneType;
	}
	public String getDeviceID() {
		return mDeviceID;
	}

	public double getLatitude() {
		return mLocation.getLatitude();
	}

	public double getLongitude() {
		return mLocation.getLongitude();
	}

	public String getCarrier() {
		return mCarrier;
	}

	public Integer getSignalStrength() {
		return mSignalStrength;
	}

	public String getNetworkType() {
		return mType.name();
	}
	public Integer getNetworkTypeInt() {
		return mTypeInt;
	}

	public String getManufacturer() {
		return mManufacturer;
	}
	
	public String getPhone() {
		return mPhone;
	}
	
	public Integer getGsm() {
		return mGsm;
	}
	public Integer getGsmError() {
		return mGsmError;
	}
	public Integer getCdma() {
		return mCdma;
	}
	public Integer getCdmaError() {
		return mCdmaError;
	}
	public Integer getEvdo() {
		return mEvdo;
	}
	public Integer getEvdoError() {
		return mEvdoError;
	}
	
	@Override
	public String toString() {
		return "Location: " + mLocation.toString() + 
			"\nCarrier: " + mCarrier +
			"\nSignalStrength: " + mSignalStrength +
			"\nNetwork Type: " + mType +
			"\nManufacturer: " + mManufacturer +
			"\nGSM: " + mGsm +
			"\nGSM Error: " + mGsmError +
			"\nCDMA: " + mCdma +
			"\nCDMA Error: " + mCdmaError +
			"\nEVDO: " + mEvdo +
			"\nEVDO Error: " + mEvdoError;
	}
}
