package com.joyplus.ad.data;

import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.Locale;
import java.util.UUID;

import com.joyplus.ad.PhoneManager.CONNECTION_TYPE;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class PhoneService {
	private static final String PREFS_DEVICE_ID    = "device_id";
	private static final String USER_AGENT_PATTERN = "Mozilla/5.0 (Linux; U; Android %1$s; %2$s; %3$s Build/%4$s) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";   
	
	public CONNECTION_TYPE getConnectionType(Context context) {
		int networkStatePermission = context
				.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);
		if (networkStatePermission == PackageManager.PERMISSION_GRANTED) {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info == null) {
				return CONNECTION_TYPE.UNKNOWN;
			}
			int netType = info.getType();
			int netSubtype = info.getSubtype();
			if (netType == ConnectivityManager.TYPE_WIFI) {
				return CONNECTION_TYPE.WIFI;
			} else if (netType == 6) {
				return CONNECTION_TYPE.WIMAX;
			} else if (netType == ConnectivityManager.TYPE_MOBILE) {
				switch (netSubtype) {
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					return CONNECTION_TYPE.MOBILE_1xRTT;
				case TelephonyManager.NETWORK_TYPE_CDMA:
					return CONNECTION_TYPE.MOBILE_CDMA;
				case TelephonyManager.NETWORK_TYPE_EDGE:
					return CONNECTION_TYPE.MOBILE_EDGE;
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					return CONNECTION_TYPE.MOBILE_EVDO_0;
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					return CONNECTION_TYPE.MOBILE_EVDO_A;
				case TelephonyManager.NETWORK_TYPE_GPRS:
					return CONNECTION_TYPE.MOBILE_GPRS;
				case TelephonyManager.NETWORK_TYPE_UMTS:
					return CONNECTION_TYPE.MOBILE_UMTS;
				case 14:
					return CONNECTION_TYPE.MOBILE_EHRPD;
				case 12:
					return CONNECTION_TYPE.MOBILE_EVDO_B;
				case 8:
					return CONNECTION_TYPE.MOBILE_HSDPA;
				case 10:
					return CONNECTION_TYPE.MOBILE_HSPA;
				case 15:
					return CONNECTION_TYPE.MOBILE_HSPAP;
				case 9:
					return CONNECTION_TYPE.MOBILE_HSUPA;
				case 11:
					return CONNECTION_TYPE.MOBILE_IDEN;
				case 13:
					return CONNECTION_TYPE.MOBILE_LTE;
				default:
					return CONNECTION_TYPE.UNKNOWN;
				}
			} else {
				return CONNECTION_TYPE.UNKNOWN;
			}
		} else {
			return CONNECTION_TYPE.UNKNOWN;
		}
	}
	
	
	public String getDeviceId(Context context) {
  		String androidId = Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);
  		if ((androidId == null) || (androidId.equals("9774d56d682e549c"))
  				|| (androidId.equals("0000000000000000"))) {
  			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
  			androidId = prefs.getString(PREFS_DEVICE_ID, null);
  			if (androidId == null) {
  				try {
  					String uuid = UUID.randomUUID().toString();
  					MessageDigest digest = MessageDigest.getInstance("MD5");
  					digest.update(uuid.getBytes(), 0, uuid.length());
  					androidId = String.format("%032X",new Object[] { 
  							new BigInteger(1, digest.digest()) }).substring(0, 16);
  				} catch (Exception e) {
  					androidId = "9774d56d682e549c";
  				}
  				prefs.edit().putString(PREFS_DEVICE_ID, androidId).commit();
  			}
  		}
  		return androidId;
	 }
	
	
      public String getTelephonyDeviceId(Context context) {
    	    String DeviceID = "";
	  		int telephonyPermission = context
	  				.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE);	
	  		if (telephonyPermission == PackageManager.PERMISSION_GRANTED) {
	  			TelephonyManager tm = (TelephonyManager) context
	  					.getSystemService(Context.TELEPHONY_SERVICE);
	  			DeviceID = tm.getDeviceId();
	  		}
	  		if(DeviceID == null)DeviceID="";
	  		return DeviceID;
	  }
  
      public String getDefaultUserAgentString(Context context) {
	  		try {
	  			Constructor<WebSettings> constructor = WebSettings.class
	  					.getDeclaredConstructor(Context.class, WebView.class);
	  			constructor.setAccessible(true);
	  			try {
	  				WebSettings settings = constructor.newInstance(context, null);
	  				return settings.getUserAgentString();
	  			} finally {
	  				constructor.setAccessible(false);
	  	        }
	  		} catch (Exception e) {
	  			return new WebView(context).getSettings().getUserAgentString();
	  		}
	  }
      
      
      public String buildUserAgent() {
	  		String androidVersion = Build.VERSION.RELEASE;
	  		String model = Build.MODEL;
	  		String androidBuild = Build.ID;
	  		final Locale l = Locale.getDefault();
	  		final String language = l.getLanguage();
	  		String locale = "en";
	  		if (language != null) {
	  			locale = language.toLowerCase();
	  			final String country = l.getCountry();
	  			if (country != null) {
	  				locale += "-" + country.toLowerCase();
	  			}
	  		}	
	  		String userAgent = String.format(USER_AGENT_PATTERN,
	  				androidVersion, locale, model, androidBuild);
	  		return userAgent;
	 }
      
      public String getLocalIpAddress() {
  		try {
  			for (Enumeration<NetworkInterface> en = NetworkInterface
  					.getNetworkInterfaces(); en.hasMoreElements();) {
  				NetworkInterface intf = en.nextElement();
  				for (Enumeration<InetAddress> enumIpAddr = intf
  						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
  					InetAddress inetAddress = enumIpAddr.nextElement();
  					if (!inetAddress.isLoopbackAddress()) {
  						return inetAddress.getHostAddress().toString();
  					}
  				}
  			}
  		} catch (SocketException ex) {
  		}
  		return null;
  	}
    
    public String getMacAddress(Context context){
		String macAddress = "";
		WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wifiMgr ? null : wifiMgr
				.getConnectionInfo());
		if (info != null) {
			macAddress = info.getMacAddress();
		}
		return macAddress;
    }  
      
}
