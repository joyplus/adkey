package com.joyplus.adkey.request;

import com.joyplus.adkey.AdRequest;
import com.joyplus.adkey.BannerAd;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.RequestBannerAd;
import com.joyplus.adkey.RequestRichMediaAd;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.video.RichMediaAd;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class Request {

	private String      mPublisherId;
	private String      mUniqueId1;
	private String      mUniqueId2;
//	private boolean     mIncludeLocation;
	private Context     mContext;
//	private Thread      mRequestThread;
//	private Handler     mHandler = new Handler();
//	private AdRequest   mRequest = null;
//	private boolean     animation;
//	private AdListener  mListener;
//	private boolean     mEnabled = true;
//	private Ad          mResponse;//response for BannerAd or RichMediaAd
	private String      requestURL;
	private String      MAC ;
	private String      mUserAgent;
//	private SerializeManager serializeManager = null;
	private boolean          includeLocation  = false;
	 
	private LocationManager locationManager;
	private int isAccessFineLocation;
	private int isAccessCoarseLocation;
//	private int telephonyPermission;
	
	 public Request(Context context, String publisherId){
		 mContext     = context;
		 mPublisherId = publisherId;
		 requestURL   = Const.REQUESTURL;
		 InitResource();
	 }

	private void InitResource() {
		// TODO Auto-generated method stub
		Util.PublisherId     = mPublisherId;
		Util.GetPackage(mContext);
		//mUserAgent = Util.getDefaultUserAgentString(mContext);
		mUserAgent = Util.buildUserAgent();
		locationManager        = null;
//		int telephonyPermission    = mContext.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE);
		isAccessFineLocation   = mContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
		isAccessCoarseLocation = mContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
		if (isAccessFineLocation == PackageManager.PERMISSION_GRANTED
				|| isAccessCoarseLocation == PackageManager.PERMISSION_GRANTED)
			locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
		mUniqueId1 = Util.getTelephonyDeviceId(mContext);
		mUniqueId2 = Util.getDeviceId(mContext);
		if ((mPublisherId == null) || (mPublisherId.length() == 0)){
			throw new IllegalArgumentException("User Id cannot be null or empty");
		}
		if ((mUniqueId2 == null) || (mUniqueId2.length() == 0)){
			throw new IllegalArgumentException("System Device Id cannot be null or empty");
		}
		MAC        = Util.GetMacAddress(mContext);
//		mEnabled = (Util.getMemoryClass(mContext) > 16);
	}
	
	//get richmedia ad 
	public RichMediaAd getRichMediaAd(boolean Report){
		RichMediaAd ad = getRichMediaAd();
		if(Report && ad != null){
			new Report(mContext).report(ad);
		}
		return ad;
	}
	public RichMediaAd getRichMediaAd(){
		RequestRichMediaAd requestAd = new RequestRichMediaAd();
		try{
			return requestAd.sendRequest(getRequest(AdRequest.VAD));
		} catch (final Throwable e){
			Log.d("Jas","getRichMediaAd fail...");
		}
		return null;
	}
	//get banner ad
	public BannerAd getBannerAd(boolean report){
		BannerAd ad = getBannerAd();
		if(report && ad != null){
			new Report(mContext).report(ad);
		}
		return ad;
	} 
	public BannerAd getBannerAd(){
		RequestBannerAd requestAd = new RequestBannerAd();
		try{
			return requestAd.sendRequest(getRequest(AdRequest.BANNER));
		} catch (final Throwable e){
			Log.d("Jas","getBannerAd fail...");
		}
		return null;
	}
	private AdRequest getRequest(int type){
		AdRequest mRequest = new AdRequest();
		mRequest.setDeviceId(mUniqueId1);
		mRequest.setDeviceId2(mUniqueId2);
		mRequest.setPublisherId(mPublisherId);
		mRequest.setUserAgent(mUserAgent);
		mRequest.setMACAddress(MAC);
		mRequest.setUserAgent2(Util.buildUserAgent());
		Location location = null;
		if(includeLocation)location = getLocation();
		if(location != null){
			mRequest.setLatitude(location.getLatitude());
			mRequest.setLongitude(location.getLongitude());
		}else{
			mRequest.setLatitude(0.0);
			mRequest.setLongitude(0.0);
		}
		mRequest.setConnectionType(Util.getConnectionType(mContext));
		mRequest.setIpAddress(Util.getLocalIpAddress());
		mRequest.setTimestamp(System.currentTimeMillis());
		mRequest.setType(type);
		mRequest.setRequestURL(requestURL);
		return mRequest;
	}
	private Location getLocation(){
		if (locationManager != null){
			if (isAccessFineLocation == PackageManager.PERMISSION_GRANTED){
				boolean isGpsEnabled = locationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER);
				if (isGpsEnabled)
					return locationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
			if (isAccessCoarseLocation == PackageManager.PERMISSION_GRANTED){
				boolean isNetworkEnabled = locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
				if (isNetworkEnabled)
					return locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		}
		return null;
	}
}
