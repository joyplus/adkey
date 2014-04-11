package com.joyplus.adkey.monitor;

import java.io.File;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import com.joyplus.adkey.AdRequest;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.RequestRichMediaAd;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.download.ImpressionThread;
import com.joyplus.adkey.video.ResourceManager;
import com.joyplus.adkey.video.RichMediaAd;
import com.joyplus.adkey.video.TrackerService;
import com.joyplus.adkey.widget.Log;
import com.joyplus.adkey.widget.SerializeManager;
import com.miaozhen.mzmonitor.MZMonitor;

public class ADKEYMonitor
{
	private static HashMap<Long, ADKEYMonitor> sRunningAds = new HashMap<Long, ADKEYMonitor>();
	private String mPublisherId;
	private String mUniqueId1;
	private String mUniqueId2;
	private boolean mIncludeLocation;
	private static Context mContext;
	private Thread mRequestThread;
	private AdRequest mRequest = null;
	private boolean mEnabled = true;
	private RichMediaAd mResponse;
	private String requestURL;
	
	private String mUserAgent;
	
	private SerializeManager serializeManager = null;
	
	public static ADKEYMonitor getAdManager(RichMediaAd ad)
	{
		ADKEYMonitor adManager = sRunningAds.remove(ad.getTimestamp());
		return adManager;
	}
	
	public static void closeRunningAd(RichMediaAd ad, boolean result)
	{
		ADKEYMonitor adManager = sRunningAds.remove(ad.getTimestamp());
		adManager.notifyAdClose(ad, result);
	}
	
	public void release()
	{
		TrackerService.release();
		ResourceManager.cancel();
		
	}
	
	/*
	 * @author yyc
	 */
	public ADKEYMonitor(Context ctx, final String publisherId) throws IllegalArgumentException
	{
		ADKEYMonitor.setmContext(ctx);
		Util.PublisherId = publisherId;
		this.requestURL = Const.REQUESTURL;
		this.mPublisherId = publisherId;
		this.mIncludeLocation = true;
		this.mRequestThread = null;
		initialize();
	}
	
	public ADKEYMonitor(Context ctx, final String requestURL,
			final String publisherId, final boolean includeLocation)
			throws IllegalArgumentException
	{
		Util.PublisherId = publisherId;
		ADKEYMonitor.setmContext(ctx);
		this.requestURL = requestURL;
		this.mPublisherId = publisherId;
		this.mIncludeLocation = includeLocation;
		this.mRequestThread = null;
		initialize();
	}
	
	public void trackAd(){
		requestAd();
	}
	
	private void requestAd()
	{
		Log.i(Const.TAG, "AdManager--->requestAd");
		if (!mEnabled)
		{
			return;
		}
		if (mRequestThread == null)
		{
			mResponse = null;
			mRequestThread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					while (ResourceManager.isDownloading())
					{
						try
						{
							Thread.sleep(200);
						} catch (InterruptedException e)
						{
						}
					}
					try
					{
						RequestRichMediaAd requestAd = new RequestRichMediaAd();
						AdRequest request = getRequest();
						// if hasn't net,the writeSerializableData function
						// doesn't called
						String path = Const.DOWNLOAD_PATH + Util.VideoFileDir
								+ "ad";
						File cacheDir = new File(Const.DOWNLOAD_PATH+Util.VideoFileDir);
						if (!cacheDir.exists())
							cacheDir.mkdirs();
						
						mResponse = (RichMediaAd) serializeManager
								.readSerializableData(path);
						if (mResponse != null)
						{
							RichMediaAd nextResponse = requestAd
									.sendRequest(request);
							serializeManager.writeSerializableData(path,
									nextResponse);
						} else
						{
							
							mResponse = requestAd.sendRequest(request);
							serializeManager.writeSerializableData(path,
									mResponse);
						}
						handleRequest();
					} catch (Throwable t)
					{
						String path = Const.DOWNLOAD_PATH + Util.VideoFileDir
								+ "ad";
						mResponse = (RichMediaAd) serializeManager
								.readSerializableData(path);
						if (mResponse != null)
						{
							handleRequest();
						} else
						{
							mResponse = new RichMediaAd();
							mResponse.setType(Const.AD_FAILED);
						}
					}
					mRequestThread = null;
				}
			});
			mRequestThread
					.setUncaughtExceptionHandler(new UncaughtExceptionHandler()
					{
						
						@Override
						public void uncaughtException(Thread thread,
								Throwable ex)
						{
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
			if(mResponse.getmImpressionUrl()!=null)
			{
				new ImpressionThread(mContext,mResponse.getmImpressionUrl(),Util.PublisherId,Util.AD_TYPE.BANNER_IMAGE).start();
			}
			if(mResponse.getmTrackingUrl()!=null)
			{
				MZMonitor.retryCachedRequests(mContext);
				MZMonitor.adTrack(mContext, mResponse.getmTrackingUrl());
			}
		} else if (mResponse.getType() == Const.NO_AD)
		{
			mResponse = null;
		} else
		{
			mResponse = null;
		}
		release();
	}
	
	private void setRequestURL(String requestURL)
	{
		this.requestURL = requestURL;
	}
	
	private void requestAd(final InputStream xml)
	{
		if (!mEnabled)
		{
			return;
		}
		if (mRequestThread == null)
		{
			mResponse = null;
			mRequestThread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					while (ResourceManager.isDownloading())
					{
						try
						{
							Thread.sleep(200);
						} catch (InterruptedException e)
						{
						}
					}
					try
					{
						RequestRichMediaAd requestAd = new RequestRichMediaAd(
								xml);
						AdRequest request = getRequest();
						mResponse = requestAd.sendRequest(request);
						if (mResponse.getType() != Const.NO_AD)
						{
							
						} else
						{
							
						}
					} catch (Throwable t)
					{
						mResponse = new RichMediaAd();
						mResponse.setType(Const.AD_FAILED);
						
					}
					mRequestThread = null;
				}
			});
			mRequestThread
					.setUncaughtExceptionHandler(new UncaughtExceptionHandler()
					{
						
						@Override
						public void uncaughtException(Thread thread,
								Throwable ex)
						{
							mResponse = new RichMediaAd();
							mResponse.setType(Const.AD_FAILED);
							mRequestThread = null;
						}
					});
			mRequestThread.start();
		}
	}
	
	public boolean isAdLoaded()
	{
		return (mResponse != null);
	}
	
	private void initialize() throws IllegalArgumentException
	{
		Util.GetPackage(mContext);
		serializeManager = new SerializeManager();
		mUserAgent = Util.getDefaultUserAgentString(getContext());
		this.mUniqueId1 = Util.getTelephonyDeviceId(getContext());
		this.mUniqueId2 = Util.getDeviceId(getContext());
		if ((mPublisherId == null) || (mPublisherId.length() == 0))
		{
			throw new IllegalArgumentException(
					"User Id cannot be null or empty");
		}
		if ((mUniqueId2 == null) || (mUniqueId2.length() == 0))
		{
			throw new IllegalArgumentException(
					"System Device Id cannot be null or empty");
		}
		mEnabled = (Util.getMemoryClass(getContext()) > 16);
		Util.initializeAnimations(getContext());
		
	}
	
	private void notifyNoAdFound()
	{
		this.mResponse = null;
	}
	
	private void notifyAdShown(final RichMediaAd ad, final boolean ok)
	{
		this.mResponse = null;
	}
	
	private void notifyAdClose(final RichMediaAd ad, final boolean ok)
	{
		
	}
	
	private AdRequest getRequest()
	{
		if (mRequest == null)
		{
			mRequest = new AdRequest();
			mRequest.setDeviceId(mUniqueId1);
			mRequest.setDeviceId2(mUniqueId2);
			mRequest.setPublisherId(mPublisherId);
			mRequest.setUserAgent(mUserAgent);
			mRequest.setUserAgent2(Util.buildUserAgent());
		}
		Location location = null;
		if (this.mIncludeLocation)
		{
			location = Util.getLocation(getContext());
		}
		if (location != null)
		{
			mRequest.setLatitude(location.getLatitude());
			mRequest.setLongitude(location.getLongitude());
		} else
		{
			mRequest.setLatitude(0.0);
			mRequest.setLongitude(0.0);
		}
		mRequest.setConnectionType(Util.getConnectionType(getContext()));
		mRequest.setIpAddress(Util.getLocalIpAddress());
		mRequest.setTimestamp(System.currentTimeMillis());
		
		mRequest.setType(AdRequest.VAD);
		mRequest.setRequestURL(this.requestURL);
		return mRequest;
	}
	
	private Context getContext()
	{
		return getmContext();
	}
	
	private static Context getmContext()
	{
		return mContext;
	}
	
	private static void setmContext(Context mContext)
	{
		ADKEYMonitor.mContext = mContext;
	}
	
}
