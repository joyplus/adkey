package com.joyplus.adkey;

import java.io.File;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import com.joyplus.adkey.video.ResourceManager;
import com.joyplus.adkey.video.RichMediaAd;
import com.joyplus.adkey.video.RichMediaView;
import com.joyplus.adkey.video.TrackerService;
import com.joyplus.adkey.widget.Log;
import com.joyplus.adkey.widget.SerializeManager;

public class AdPatchManager {
  
	private static HashMap<Long, AdPatchManager> sRunningAds = new HashMap<Long, AdPatchManager>();
	private FrameLayout layout;
	private String mPublisherId;
	private String mUniqueId1;
	private String mUniqueId2;
	private boolean mIncludeLocation;
	private static Context mContext;
	private Thread mRequestThread;
	private Handler mHandler;
	private AdRequest mRequest = null;
	private AdListener mListener;
	private boolean mEnabled = true;
	private RichMediaAd mResponse;
	private String requestURL;
    //add by Jas
	private String MAC;
	//end add by Jas
	private String mUserAgent;
	private SerializeManager serializeManager = null;
	
	
	public static AdPatchManager getAdManager(RichMediaAd ad) {
		AdPatchManager adManager = sRunningAds.remove(ad.getTimestamp());
		return adManager;
	}

	public static void closeRunningAd(RichMediaAd ad, boolean result) {
		AdPatchManager adManager = sRunningAds.remove(ad.getTimestamp());
		adManager.notifyAdClose(ad, result);
	}

	public void release() {
		TrackerService.release();
		ResourceManager.cancel();
	}
	/*
	 * @author yyc
	 */
	public AdPatchManager(Context ctx, final String publisherId,final boolean cacheMode)
			throws IllegalArgumentException {
		AdPatchManager.setmContext(ctx);
		Util.PublisherId = publisherId;
		this.requestURL = Const.REQUESTURL;
		this.mPublisherId = publisherId;
		this.mIncludeLocation = true;
		this.mRequestThread = null;
		this.mHandler = new Handler();
		Util.CACHE_MODE = cacheMode;
		initialize();
	}
	
	/*
	 * test
	 */
	public AdPatchManager(Context ctx, final String publisherId,final boolean cacheMode,View v)
			throws IllegalArgumentException {
		AdPatchManager.setmContext(ctx);
		mContext = ctx;
		Util.PublisherId = publisherId;
		this.requestURL = Const.REQUESTURL;
		this.mPublisherId = publisherId;
		this.mIncludeLocation = true;
		this.mRequestThread = null;
		this.mHandler = new Handler();
		Util.CACHE_MODE = cacheMode;
		layout = (FrameLayout)v;
		initialize();
	}
	
	public AdPatchManager(Context ctx, final String requestURL, final String publisherId,
			final boolean includeLocation)
					throws IllegalArgumentException {
		Util.PublisherId = publisherId;
		AdPatchManager.setmContext(ctx);
		this.requestURL = requestURL;
		this.mPublisherId = publisherId;
		this.mIncludeLocation = includeLocation;
		this.mRequestThread = null;
		this.mHandler = new Handler();
		initialize();
	}

	public void setListener(AdListener listener) {
		this.mListener = listener;
	}
	
	public void requestAd() {
		Log.i(Const.TAG,"AdManager--->requestAd");
		if (!mEnabled) {
			return;
		}
		if (mRequestThread == null) {
			mResponse = null;
			mRequestThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (ResourceManager.isDownloading()) {
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
						}
					}
					try {
						RequestRichMediaAd requestAd = new RequestRichMediaAd();
						AdRequest request = getRequest();
						String path = Const.DOWNLOAD_PATH + Util.VideoFileDir
								+ "ad";
						File cacheDir = new File(Const.DOWNLOAD_PATH+Util.VideoFileDir);
						if (!cacheDir.exists())
							cacheDir.mkdirs();
						
						mResponse = (RichMediaAd) serializeManager
								.readSerializableData(path);
						if (mResponse != null)
						{
							handleRequest();
							RichMediaAd nextResponse = requestAd
									.sendRequest(request);
							serializeManager.writeSerializableData(path,
									nextResponse);
						} else
						{
							notifyNoAdFound();
							mResponse = requestAd.sendRequest(request);
							serializeManager.writeSerializableData(path,
									mResponse);
						}
						
					} catch (Throwable t) {
						String path = Const.DOWNLOAD_PATH + Util.VideoFileDir
								+ "ad";
						mResponse = (RichMediaAd) serializeManager
								.readSerializableData(path);
						if (mResponse != null)
						{
							handleRequest();
						} else{
							mResponse = new RichMediaAd();
							mResponse.setType(Const.AD_FAILED);
							if (mListener != null) {
								t.printStackTrace();
								
								mHandler.post(new Runnable() {
									
									@Override
									public void run() {
										notifyNoAdFound();
										
									}
								});
							}
						}
					}
					mRequestThread = null;
				}
			});
			mRequestThread
			.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

				@Override
				public void uncaughtException(Thread thread,
						Throwable ex) {
					mResponse = new RichMediaAd();
					mResponse.setType(Const.AD_FAILED);
					mRequestThread = null;
				}
			});
			mRequestThread.start();
		}
	}
	
	private void handleRequest(){
		if (mResponse.getVideo() != null
				&& android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.FROYO)
		{
			notifyNoAdFound();
		} else if (mResponse.getType() == Const.VIDEO_TO_INTERSTITIAL
				|| mResponse.getType() == Const.INTERSTITIAL_TO_VIDEO
				|| mResponse.getType() == Const.VIDEO
				|| mResponse.getType() == Const.INTERSTITIAL)
		{
			if (mListener != null)
			{
				mHandler.post(new Runnable()
				{
					
					@Override
					public void run()
					{
						mListener.adLoadSucceeded(mResponse);
					}
				});
			}
		} else if (mResponse.getType() == Const.NO_AD)
		{
			if (mListener != null)
			{
				mHandler.post(new Runnable()
				{
					
					@Override
					public void run()
					{
						notifyNoAdFound();
					}
				});
			}
		} else
		{
			if (mListener != null)
			{
				mHandler.post(new Runnable()
				{
					
					@Override
					public void run()
					{
						notifyNoAdFound();
					}
				});
			}
		}
	}
	
	public void setRequestURL(String requestURL){
		this.requestURL = requestURL;
	}

	public void requestAd(final InputStream xml) {
		if (!mEnabled) {
			return;
		}
		if (mRequestThread == null) {
			mResponse = null;
			mRequestThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (ResourceManager.isDownloading()) {
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
						}
					}
					try {
						RequestRichMediaAd requestAd = new RequestRichMediaAd(xml);
						AdRequest request = getRequest();
						mResponse = requestAd.sendRequest(request);
						if (mResponse.getType() != Const.NO_AD) {
							if (mListener != null) {
								mHandler.post(new Runnable() {

									@Override
									public void run() {
										mListener.adLoadSucceeded(mResponse);
									}
								});
							}
						} else {
							if (mListener != null) {
								mHandler.post(new Runnable() {

									@Override
									public void run() {
										notifyNoAdFound();
									}
								});
							}
						}
					} catch (Throwable t) {
						mResponse = new RichMediaAd();
						mResponse.setType(Const.AD_FAILED);
						if (mListener != null) {

							mHandler.post(new Runnable() {

								@Override
								public void run() {
									notifyNoAdFound();

								}
							});
						}
					}
					mRequestThread = null;
				}
			});
			mRequestThread
			.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

				@Override
				public void uncaughtException(Thread thread,
						Throwable ex) {
					mResponse = new RichMediaAd();
					mResponse.setType(Const.AD_FAILED);
					mRequestThread = null;
				}
			});
			mRequestThread.start();
		}
	}

	public boolean isAdLoaded() {
		return (mResponse != null);
	}
	
	public void requestAdAndShow(long timeout) {
		AdListener l = mListener;

		mListener = null;
		requestAd();
		long now = System.currentTimeMillis();
		long timeoutTime = now + timeout;
		while ((!isAdLoaded()) && (now < timeoutTime)) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			now = System.currentTimeMillis();
		}
		mListener = l;
		showAd();
	}

	public boolean isCacheLoaded(){
		File file = new File(Const.DOWNLOAD_PATH+Util.VideoFileDir);
		if(file.exists()){
			String[] temp = file.list();
			for(int i = 0;i<temp.length;i++)
			{
				if(temp[i].contains(Const.DOWNLOAD_PLAY_FILE))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public void showAd() {
		if ((mResponse == null)
				|| (mResponse.getType() == Const.NO_AD)
				|| (mResponse.getType() == Const.AD_FAILED)) {
			notifyAdShown(mResponse, false);
			return;
		}
		AdRequest request = getRequest();
		new RichMediaView(mContext,mResponse,layout,request);
	}
	
	private void initialize() throws IllegalArgumentException {
		/*
		 * init Util.VideoFileDir
		 */
		Util.GetPackage(mContext);
		serializeManager = new SerializeManager();
		mUserAgent = Util.getDefaultUserAgentString(getContext());
		this.mUniqueId1 = Util.getTelephonyDeviceId(getContext());
		this.mUniqueId2 = Util.getDeviceId(getContext());
		//add by Jas
		this.MAC        = Util.GetMacAddress(getContext());
		//end add by Jas
		if ((mPublisherId == null) || (mPublisherId.length() == 0)) {
			throw new IllegalArgumentException(
					"User Id cannot be null or empty");
		}
		if ((mUniqueId2 == null) || (mUniqueId2.length() == 0)) {
			throw new IllegalArgumentException(
					"System Device Id cannot be null or empty");
		}
		mEnabled = (Util.getMemoryClass(getContext()) > 16);
		Util.initializeAnimations(getContext());

	}

	private void notifyNoAdFound() {
		if (mListener != null) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					mListener.noAdFound();
				}
			});
		}
		this.mResponse = null;
	}

	private void notifyAdShown(final RichMediaAd ad, final boolean ok) {
		if (mListener != null) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					mListener.adShown(ad, ok);
				}
			});
		}
		this.mResponse = null;
	}

	private void notifyAdClose(final RichMediaAd ad, final boolean ok) {
		if (mListener != null) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					mListener.adClosed(ad, ok);
				}
			});
		}
	}

	private AdRequest getRequest() {
		if (mRequest == null) {
			mRequest = new AdRequest();
			mRequest.setDeviceId(mUniqueId1);
			mRequest.setDeviceId2(mUniqueId2);
			mRequest.setPublisherId(mPublisherId);
			mRequest.setUserAgent(mUserAgent);
			//add by Jas
			mRequest.setMACAddress(MAC);
			//end add by Jas
			mRequest.setUserAgent2(Util.buildUserAgent());
		}
		Location location = null;
		if (this.mIncludeLocation) {
			location = Util.getLocation(getContext());
		}
		if (location != null) {
			mRequest.setLatitude(location.getLatitude());
			mRequest.setLongitude(location.getLongitude());
		} else {
			mRequest.setLatitude(0.0);
			mRequest.setLongitude(0.0);
		}
		mRequest.setConnectionType(Util.getConnectionType(getContext()));
		mRequest.setIpAddress(Util.getLocalIpAddress());
		mRequest.setTimestamp(System.currentTimeMillis());

		mRequest.setType(AdRequest.PATCH);
		mRequest.setPatchVC(VC);
		mRequest.setRequestURL(this.requestURL);
		return mRequest;
	}

	private Context getContext() {
		return getmContext();
	}

	private static Context getmContext() {
		return mContext;
	}

	private static void setmContext(Context mContext) {
		AdPatchManager.mContext = mContext;
	}
	//add by Jas
	private String VC = "";
	public void SetVC(String vc){
		VC = vc;
	}
}
