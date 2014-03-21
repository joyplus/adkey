package com.joyplus.adkey.mini;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Timer;
import java.util.TimerTask;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.AdRequest;
import com.joyplus.adkey.BannerAd;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.RequestBannerAd;
import com.joyplus.adkey.RequestRichMediaAd;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.Util.TranslateAnimationType;
import com.joyplus.adkey.video.RichMediaAd;
import com.joyplus.adkey.widget.SerializeManager;

public class AdMini extends FrameLayout implements AdListener{

	public AdMini(Context context, AttributeSet attributes) {
		// TODO Auto-generated constructor stub
		super(context, attributes);
		mContext = context;
		if (attributes != null){
			int count = attributes.getAttributeCount();
			for (int i = 0; i < count; i++){
				String name = attributes.getAttributeName(i);
				if (name.equals("publisherId")){
					this.mPublisherId = attributes.getAttributeValue(i);
				} else if (name.equals("request_url")){
					this.requestURL = attributes.getAttributeValue(i);
				} else if (name.equals("animation")){
					this.animation = attributes.getAttributeBooleanValue(i,false);
				} else if (name.equals("includeLocation")){
					this.includeLocation = attributes.getAttributeBooleanValue(
							i, false);
				}
			}
		}
	}
	public AdMini(final Context context, final String publisherId,
			final boolean animation){
		this(context,Const.REQUESTURL,publisherId, false/*default false*/ ,animation,null);
	}
	public AdMini(final Context context, final String publisherId,
			final boolean animation,boolean cacheMode){
		this(context,Const.REQUESTURL,publisherId, false/*default false*/ ,animation,null);
		Util.CACHE_MODE = cacheMode;
	}
	public AdMini(final Context context, final String requestURL,
			final String publisherId, final boolean includeLocation,
			final boolean animation, final AdListener listener){
		super(context);
		this.requestURL = requestURL;
		mContext = context;
		mPublisherId = publisherId;
		this.includeLocation = includeLocation;
		this.animation = animation;
		mListener = listener;
		initialize(mContext);
	}
    public void SetAdListener(AdListener listener){
    	mListener = listener;
    }
	private void initialize(final Context context){
		Util.GetPackage(mContext);
		serializeManager = new SerializeManager();
		mUserAgent = Util.getDefaultUserAgentString(getContext());
		registerScreenStateBroadcastReceiver();
		this.locationManager        = null;
		this.telephonyPermission    = mContext.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE);
		this.isAccessFineLocation   = mContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
		this.isAccessCoarseLocation = mContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
		if (this.isAccessFineLocation == PackageManager.PERMISSION_GRANTED
				|| this.isAccessCoarseLocation == PackageManager.PERMISSION_GRANTED)
			this.locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
		mUniqueId1 = Util.getTelephonyDeviceId(mContext);
		mUniqueId2 = Util.getDeviceId(mContext);
		if ((mPublisherId == null) || (mPublisherId.length() == 0)){
			throw new IllegalArgumentException("User Id cannot be null or empty");
		}
		if ((mUniqueId2 == null) || (mUniqueId2.length() == 0)){
			throw new IllegalArgumentException("System Device Id cannot be null or empty");
		}
		this.MAC        = Util.GetMacAddress(mContext);
		mEnabled = (Util.getMemoryClass(getContext()) > 16);
	}
	private void registerScreenStateBroadcastReceiver(){
		mScreenStateReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent){
				if(!mIsInForeground)return;
				if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
						pause();
				} else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
						resume();
				}
			}
		};
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		mContext.registerReceiver(mScreenStateReceiver, filter);
	}
	@Override
	protected void onWindowVisibilityChanged(int visibility){
		super.onWindowVisibilityChanged(visibility);
		if (visibility == VISIBLE){
			mIsInForeground = true;
			resume();
		}else{
			mIsInForeground = false;
			pause();
		}
	}
	private void resume(){
		if(mResponse != null){
			LoadResponse();
		}else{
			RemoveAllUI();
			if(mIsInForeground)requestAd();//
		}
	}
	private void RemoveAllUI(){
        notifyAdListener(true, AD_CLOSED);
		if(mAdMiniView != null){
			this.removeView(mAdMiniView);
			mAdMiniView = null;
		}
		this.removeAllViews();
	}
	private void pause(){
		InitReloadTimer();
	}
	private void LoadResponse(){
		if(mResponse != null){
			if(mResponse.getType() == Const.INTERSTITIAL 
					|| mResponse.getType() == Const.VIDEO){
				notifyAdListener(true, AD_LOADSUCCESSED);
				ShowAd();
				return;
			}
		}
		mResponse = null;
		notifyAdListener(false,AD_NOADFIND);
	}
	private boolean ShowAd = false;
	private void ShowAd(){
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(ShowAd)return;
				ShowAd = true;
				RemoveAllUI();
				Util.CACHE_MODE = true;
				mAdMiniView = new AdMiniView(mContext, (RichMediaAd) mResponse, AdMini.this);
				mAdMiniView.SetAnimation(mTranslateAnimationType==null?
						TranslateAnimationType.UP:mTranslateAnimationType);
				AdMini.this.addView(mAdMiniView,new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						   LayoutParams.MATCH_PARENT, Gravity.CENTER));
				notifyAdListener(true, AD_SHOWN);
				ShowAd = false;
			}
		});
		
	}
	private void InitReloadTimer(){
		if (reloadTimer != null){
			reloadTimer.cancel();
			reloadTimer = null;
		}
		this.reloadTimer = new Timer();
	}
	private void startReloadTimer(RichMediaAd response){
		if (this.reloadTimer == null)return;
		final int refreshTime = 30 * 1000;
		this.reloadTimer.schedule(new TimerTask(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(mIsInForeground)requestAd();
			}
		}, refreshTime);
	}
	
	@Override
	protected void onAttachedToWindow(){
		super.onAttachedToWindow();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		mContext.registerReceiver(mScreenStateReceiver, filter);
	}
	
	@Override
	protected void onDetachedFromWindow(){
		super.onDetachedFromWindow();
		unregisterScreenStateBroadcastReceiver();
	}
	private void unregisterScreenStateBroadcastReceiver(){
		try{
			mContext.unregisterReceiver(mScreenStateReceiver);
		} catch (Exception IllegalArgumentException){
		}
	}
    /*this was copy from before, even it was very bad bad bad.*/
	//private static HashMap<Long, AdMini> sRunningAds = new HashMap<Long, AdMini>();
	private String      mPublisherId;
	private String      mUniqueId1;
	private String      mUniqueId2;
	private boolean     mIncludeLocation;
	private Context     mContext;
	private Thread      mRequestThread;
	private Handler     mHandler = new Handler();
	private AdRequest   mRequest = null;
	private boolean     animation;
	private AdListener  mListener;
	private boolean     mEnabled = true;
	private Ad          mResponse;//response for BannerAd or RichMediaAd
	private String      requestURL;
	private String      MAC ;
	private String      mUserAgent;
	private SerializeManager serializeManager = null;
	private boolean          includeLocation  = false;
	 
	private LocationManager locationManager;
	private int isAccessFineLocation;
	private int isAccessCoarseLocation;
	private int telephonyPermission;

	private BroadcastReceiver mScreenStateReceiver;
	protected boolean mIsInForeground;
	
	//for AD show
	//for binner
    //private BannerAdView mBannerView;
	private Timer        reloadTimer;
	private TimerTask    reloadTask;
	private AdMiniView   mAdMiniView;
	
	public void requestAd(){
		 if(mRequestThread != null)return;
		 mResponse = null;
		 mRequestThread  = new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String path = Const.DOWNLOAD_PATH + Util.VideoFileDir + "ad";
				File cacheDir = new File(Const.DOWNLOAD_PATH+Util.VideoFileDir);
				if (!cacheDir.exists())cacheDir.mkdirs();
				mResponse = getRichMediaAd();
				serializeManager.writeSerializableData(path,mResponse);
				LoadResponse();	
				mRequestThread = null;
			}
		 });
		 mRequestThread.setUncaughtExceptionHandler(new UncaughtExceptionHandler(){
			@Override
			public void uncaughtException(final Thread thread,
					final Throwable ex){
				mResponse = null;
				notifyAdListener(false,AD_NOADFIND);
				mRequestThread = null;
			}
		 });
		 mRequestThread.start();
	}
	
	//get richmedia ad
	private RichMediaAd getRichMediaAd(){
		RequestRichMediaAd requestAd = new RequestRichMediaAd();
		try{
			return requestAd.sendRequest(getRequest(AdRequest.VAD));
		} catch (final Throwable e){
		}
		return null;
	}
	//get banner ad
	private BannerAd getBannerAd(){
		RequestBannerAd requestAd = new RequestBannerAd();
		try{
			return requestAd.sendRequest(getRequest(AdRequest.BANNER));
		} catch (final Throwable e){
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
		mRequest.setConnectionType(Util.getConnectionType(getContext()));
		mRequest.setIpAddress(Util.getLocalIpAddress());
		mRequest.setTimestamp(System.currentTimeMillis());
		mRequest.setType(type);
		mRequest.setRequestURL(this.requestURL);
		return mRequest;
	}
	private Location getLocation(){
		if (this.locationManager != null){
			if (this.isAccessFineLocation == PackageManager.PERMISSION_GRANTED){
				final boolean isGpsEnabled = this.locationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER);
				if (isGpsEnabled)
					return this.locationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
			if (this.isAccessCoarseLocation == PackageManager.PERMISSION_GRANTED){
				final boolean isNetworkEnabled = this.locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
				if (isNetworkEnabled)
					return this.locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		}
		return null;
	}
	@Override
	public void adClicked() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void adClosed(Ad ad, boolean completed) {
		// TODO Auto-generated method stub
		InitReloadTimer();
		notifyAdListener(completed, AD_CLOSED);
		//RemoveAllUI();
		if(mIsInForeground)requestAd();
	}
	@Override
	public void adLoadSucceeded(Ad ad) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void adShown(Ad ad, boolean succeeded) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void noAdFound() {
		// TODO Auto-generated method stub
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////
	private final static int AD_MIN           = 0;
	private final static int AD_CLICKED       = 0;
	private final static int AD_CLOSED        = 1;
	private final static int AD_LOADSUCCESSED = 2;
	private final static int AD_SHOWN         = 3;
	private final static int AD_NOADFIND      = 4;
	private final static int AD_MAX           = 5;
	private void notifyAdListener(final boolean succeeded,final int num){
		if(mListener == null)return;
		if(num<AD_MIN || num>AD_MAX)return;
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				switch(num){
				case AD_CLICKED:mListener.adClicked();break;
				case AD_CLOSED: mListener.adClosed(mResponse, succeeded);break;
				case AD_LOADSUCCESSED:mListener.adLoadSucceeded(mResponse);break;
				case AD_SHOWN:mListener.adShown(mResponse, succeeded);break;
				case AD_NOADFIND:mListener.noAdFound();break;
				}
			}
		});
	}
	
	//for TranslateAnimation
	public void SetAnimation(TranslateAnimationType type){
		if(type != null){
			mTranslateAnimationType = type;
		}
	}
	private TranslateAnimationType mTranslateAnimationType = TranslateAnimationType.RANDOM;
}
