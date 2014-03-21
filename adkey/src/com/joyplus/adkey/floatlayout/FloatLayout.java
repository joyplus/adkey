package com.joyplus.adkey.floatlayout;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Timer;
import java.util.TimerTask;
import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.AdRequest;
import com.joyplus.adkey.BannerAd;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.RequestBannerAd;
import com.joyplus.adkey.RequestRichMediaAd;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.Util.TranslateAnimationType;
import com.joyplus.adkey.video.ResourceManager;
import com.joyplus.adkey.video.RichMediaAd;
import com.joyplus.adkey.widget.DownloadBannerThread;
import com.joyplus.adkey.widget.DownloadVideoThread;
import com.joyplus.adkey.widget.SerializeManager;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class FloatLayout implements AdListener{
        
	    //private final static int FloatLayout_default = 200;
	    private int  FloatLayout_Width     = 0;
	    private int  FloatLayout_Height    = 0;
	    private View FloatLayout_root      = null;
	    private final static boolean Debug = true;
	    private PopupWindow      mPopupWindow      = null;
	    private FloatLayoutView  mFloatLayoutView  = null;
	    private FloatLayoutView2 mFloatLayoutView2 = null;
	    
		public FloatLayout( Context context, String publisherId,View root, int width,int height){  
			this(context,publisherId,root,width,height,null);
		}
		public FloatLayout( Context context,String publisherId,View root,int width,int height,AdListener listener){  
			this(context,Const.REQUESTURL,publisherId,false,true,listener);
			FloatLayout_root   = root;
			FloatLayout_Width  = width;
			FloatLayout_Height = height;
			
		}
		private FloatLayout(final Context context, final String requestURL,
				final String publisherId, final boolean includeLocation,
				final boolean animation, final AdListener listener){
			this.requestURL      = requestURL;
			mContext             = context;
			mPublisherId         = publisherId;
			this.includeLocation = includeLocation;
			this.animation       = animation;
			mListener            = listener;
			initialize(mContext);
		}
	    public void SetAdListener(AdListener listener){
	    	mListener = listener;
	    }
		private void initialize(final Context context){
			Util.GetPackage(mContext);
			serializeManager = new SerializeManager();
			mUserAgent = Util.getDefaultUserAgentString(mContext);
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
			mEnabled = (Util.getMemoryClass(mContext) > 16);
		}
		
		
		private void LoadResponse(){
			if(mResponse != null){
				if(Debug)Log.d("Jas","LoadResponse mResponse-->"+mResponse.toString());
				if(mResponse instanceof RichMediaAd){
					if(mResponse.getType() == Const.INTERSTITIAL){
						//notifyAdListener(true, AD_LOADSUCCESSED);
						ShowMediaAd((RichMediaAd) mResponse);
						return;
					}
				}else if(mResponse instanceof BannerAd){
			        String url = (((BannerAd)mResponse).GetCreative_res_url());
					if(!(url==null || "".equals(url))){
						ShowBannerAd((BannerAd)mResponse);
						return;
					}
				}
			}
			if(Debug)Log.d("Jas","LoadResponse mResponse null !!!!");
			mResponse = null;
			notifyAdListener(false,AD_NOADFIND);
		}
		private void ShowBannerAd(final BannerAd mResponse2) {
			// TODO Auto-generated method stub
			mHandler.post(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(ShowAd || FloatLayout_root ==null || mResponse2 == null)return;
					ShowAd = true;
					Stop(mResponse2);
					int[] location = new int[2];  
					FloatLayout_root.getLocationOnScreen(location); 
					mPopupWindow.showAtLocation(FloatLayout_root, Gravity.NO_GRAVITY, 
							location[0]+FloatLayout_root.getWidth(), location[1]+FloatLayout_root.getHeight());
					mFloatLayoutView2.SetAnimation(mTranslateAnimationType);
					mFloatLayoutView2.InitResource();//now AD Show.
					ShowAd = false;
					startDismissTimer(mResponse2);
					startReloadTimer(mResponse2);
				}
			});
		}
		private boolean ShowAd = false;
		private void ShowMediaAd(final RichMediaAd response){
			mHandler.post(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(ShowAd || FloatLayout_root ==null || response == null)return;
					ShowAd = true;
					Stop(response);
					int[] location = new int[2];  
					FloatLayout_root.getLocationOnScreen(location); 
					mPopupWindow.showAtLocation(FloatLayout_root, Gravity.NO_GRAVITY, 
							location[0]+FloatLayout_root.getWidth(), location[1]+FloatLayout_root.getHeight());
					mFloatLayoutView.SetAnimation(mTranslateAnimationType);
					mFloatLayoutView.InitResource();//now AD Show.
					ShowAd = false;
					startDismissTimer(response);
					startReloadTimer(response);
				}
			});
		}
		
		
		private void InitPop(Ad Response){
			if(mPopupWindow != null){
				mPopupWindow.dismiss();
				mFloatLayoutView  = null;
				mFloatLayoutView2 = null;
				mPopupWindow      = null;
			}
			if(Response == null)return;
			if(Response instanceof RichMediaAd){
				mFloatLayoutView = new FloatLayoutView(mContext,(RichMediaAd)mResponse,FloatLayout.this);
				if(FloatLayout_Height <=0 || FloatLayout_Width<=0 || FloatLayout_root == null){
					mPopupWindow = new PopupWindow(mFloatLayoutView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,false);
				}else{
					mPopupWindow = new PopupWindow(mFloatLayoutView, FloatLayout_Width, FloatLayout_Height,false);
				}
			}else if(Response instanceof BannerAd){
				mFloatLayoutView2 = new FloatLayoutView2(mContext,(BannerAd)mResponse,FloatLayout.this);
				if(FloatLayout_Height <=0 || FloatLayout_Width<=0 || FloatLayout_root == null){
					mPopupWindow = new PopupWindow(mFloatLayoutView2,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,false);
				}else{
					mPopupWindow = new PopupWindow(mFloatLayoutView2, FloatLayout_Width, FloatLayout_Height,false);
				}
			}
		}
		private void InitDismissTimer(){
			if (DismissTimer != null){
				DismissTimer.cancel();
				DismissTimer = null;
			}
			this.DismissTimer = new Timer();
		}
		
		private void startDismissTimer(final Ad response){
			if (DismissTimer == null)return;
			final int refreshTime = 10*1000;
			DismissTimer.schedule(new TimerTask(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mHandler.post(new Runnable(){
						@Override
						public void run() {
							// TODO Auto-generated method stub
							InitPop(null);//don't need to init view.
					}});
				}
			}, refreshTime);
		}
		private void InitReloadTimer(){
			if (reloadTimer != null){
				reloadTimer.cancel();
				reloadTimer = null;
			}
			this.reloadTimer = new Timer();
		}
		
		private void startReloadTimer(Ad response){
			if (this.reloadTimer == null)return;
			final int refreshTime = 20*1000;
			this.reloadTimer.schedule(new TimerTask(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					requestAd();
				}
			}, refreshTime);
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

		//private BroadcastReceiver mScreenStateReceiver;
		protected boolean mIsInForeground;
		
		//for AD show
		//for binner
	    //private BannerAdView mBannerView;
		private Timer        reloadTimer;
		private TimerTask    reloadTask;
		private Timer        DismissTimer;
		private TimerTask    DismissTask;
		//private AdMiniView   mAdMiniView;
		public   void Stop(){
			Stop(null);
		}
		private  void Stop(Ad response){
			InitDismissTimer();//remove dismiss
			InitReloadTimer();//remove timetesk
			InitPop(response);//remove pop
		};
		public void requestAd(){
			 if(mRequestThread != null)return;
			 mResponse       = null;
			 mRequestThread  = new Thread(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (ResourceManager.isDownloading()){
						try{
							Thread.sleep(200);
						} catch (InterruptedException e){
						}
					}
					String path = Const.DOWNLOAD_PATH + Util.VideoFileDir + "adfloatlayout";
					File cacheDir = new File(Const.DOWNLOAD_PATH+Util.VideoFileDir);
					if (!cacheDir.exists())cacheDir.mkdirs();
					if(MediaOrBanner){//media
						mResponse = (RichMediaAd) serializeManager.readSerializableData(path);
						LoadResponse();
						serializeManager.writeSerializableData(path,getRichMediaAd());
						new DownloadVideoThread(path,mContext).start();//download resource.
					}else{//binner
						mResponse = (BannerAd) serializeManager.readSerializableData(path);
						LoadResponse();
						serializeManager.writeSerializableData(path,getBannerAd());
						new DownloadBannerThread(path,mContext).start();//download resource.
					}
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
				if(Debug)Log.d("Jas","getBannerAd fail...");
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
		
		
		private boolean MediaOrBanner = false;//Media -- true  Banner -- false
		
}
