package com.joyplus.adboot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.MalformedURLException;
import java.net.URL;
import com.joyplus.adkey.AdRequest;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.RequestRichMediaAd;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.download.Downloader;
import com.joyplus.adkey.download.ImpressionThread;
import com.joyplus.adkey.video.ResourceManager;
import com.joyplus.adkey.video.RichMediaAd;
import com.joyplus.adkey.video.VideoData;
import com.joyplus.adkey.widget.Log;
import com.joyplus.adkey.widget.SerializeManager;

import android.content.Context;
import android.location.Location;

/*Add by Jas@20130714 for Advert during boot up machine
 * This file was relase to SDK 
 * So don't change this file by youself*/
public class AdBootScreenManager {

	private boolean Debug = true;
	// private String TAG = "AdBootScreenManager";
	private String TAG = "Jas";
	private boolean mIncludeLocation = false;
	private String mRequestURL = null;
	private SerializeManager serializeManager;

	private Context mContext;
	private String PUBLISHERID = null;
	private String mUniqueId1;
	private String mUniqueId2;
	private String mUserAgent;
	private boolean mEnabled = false;

	private AdRequest mAdRequest = null;
	private Thread mRequestThread;
	private RichMediaAd mRichMediaAd; // respone media

	private AdBootScreenListener mListener;

	public void setListener(AdBootScreenListener listener) {
		mListener = listener;
	}

	// this should be the same as it in BootAnimation.cpp
	private static final String DefaultFILEPATH = "/data/joyplus/bootanimation.mp4";
	
	private  String DefaultAD = null;
	private  String PATH = null;


	public AdBootScreenManager(Context ctx, final String publisherId,
			final boolean cacheMode) {
		this.PUBLISHERID = publisherId;
		this.PATH = Const.DOWNLOAD_PATH+ctx.getPackageName()+"/"+PUBLISHERID+"/";
		mContext = ctx;
		Util.CACHE_MODE = cacheMode;
		InitResource();
	}
    
	// Interface for user to Manager the resource.
	public void UpdateAdvert() {
		// resave advert file first.
		ResaveCacheLoaded();
		requestAd();
		if (!WaitAd(20000)) {
			notifyAdClose();
			return;
		}
		// now we can download mp4 file and report count.
		if(CheckAd(mRichMediaAd)){
			ReportCount();
			DownloadFile();
		}
	}

	// Interface Report count
	private void ReportCount() {
		new Thread(new ReportCountRunnable()).start();
	}

	// Tnterface Download file
	private void DownloadFile() {
		new Thread(new DownloadFileRunnable()).start();
	}

	private class DownloadFileRunnable implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			RichMediaAd tempAd = (RichMediaAd) serializeManager
					.readSerializableData(DefaultAD);
			if (tempAd != null) {
				VideoData video = tempAd.getVideo();
				if (Util.CACHE_MODE && video != null) {
					String Download_path = video.getVideoUrl();
					Log.d("Jas", "Download_path=" + Download_path);
					URL url = null;
					try {
						url = new URL(Download_path);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						Log.d(TAG, "download fail Url fail");
						e.printStackTrace();
					}
					if (url != null) {
						Util.ExternalName = "."
								+ Util.getExtensionName(url.getPath());
					} else {
						Util.ExternalName = ".mp4";
					}
					Downloader downloader = new Downloader(Download_path,
							mContext);
					if (Download_path.startsWith("http:")
							|| Download_path.startsWith("https:")) {
						downloader.download();
						if (Debug)
							Log.i(TAG, "download starting");
					}
				} else {
					if (Debug)
						Log.i(TAG, "download fail video url fail");
					notifyAdNofound();
				}
			} else {
				if (Debug)
					Log.i(TAG, "download fail ad null");
				notifyAdNofound();
			}
		}
	}

	private class ReportCountRunnable implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (Debug)
				Log.d(TAG, "ReportCountRunnable run()");
			new ImpressionThread(mContext, mRichMediaAd.getmImpressionUrl(),
					PUBLISHERID, Util.AD_TYPE.FULL_SCREEN_VIDEO).start();
		}
	}

	/* Wait for Ad return form Server,Time out : faslse , Result : true. */
	private boolean WaitAd(int Time) {
		// waiting for Ad result.
		long now = System.currentTimeMillis();
		long timeoutTime = now + Time;// 20s
		while ((mRichMediaAd == null) && (now < timeoutTime)) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			now = System.currentTimeMillis();
		}
		if (mRichMediaAd == null) {// no Ad can use
			notifyAdClose();
			return false;
		}
		return true;
	}

	/* request Ad from Server */
	private void requestAd() {
		requestAd(null);
	}

	private void requestAd(final InputStream xml) {
		if (Debug)
			Log.i(TAG, "AdBootScreenManager requestAd mEnabled=" + mEnabled);
		if (!mEnabled)
			return;
		if (mRequestThread == null) {
			mRichMediaAd = null;
			mRequestThread = new Thread(new Request(xml));
			mRequestThread
					.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
						@Override
						public void uncaughtException(Thread thread,
								Throwable ex) {
							mRichMediaAd = new RichMediaAd();
							mRichMediaAd.setType(Const.AD_FAILED);
							mRequestThread = null;
						}
					});
			mRequestThread.start();
		}
	}

	/* get Ad from Server */
	private class Request implements Runnable {
		private InputStream XML = null;
		private RequestRichMediaAd requestAd;

		public Request(InputStream xml) {
			XML = xml;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (ResourceManager.isDownloading()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
				}
			}
			try {
				if (XML == null) {
					requestAd = new RequestRichMediaAd();
				} else {
					requestAd = new RequestRichMediaAd(XML);
				}
				AdRequest request = getRequest();
				File cacheDir = new File(PATH);
				if (!cacheDir.exists())
					cacheDir.mkdirs();
				mRichMediaAd = (RichMediaAd) serializeManager
						.readSerializableData(DefaultAD);
				RichMediaAd nextResponse = requestAd.sendRequest(request);
				if(Debug)Log.d(TAG,"Request over mRichMediaAd()");
				serializeManager.writeSerializableData(DefaultAD, nextResponse);
				if (mRichMediaAd == null) {
					mRichMediaAd = nextResponse;
				}
				if(Debug)Log.d(TAG,"Request over mRichMediaAd="+mRichMediaAd.toString());
			} catch (Throwable t) {
				Log.e("Jas","Request error ");
				mRichMediaAd = (RichMediaAd) serializeManager
						.readSerializableData(DefaultAD);
			} finally {
				notifyResponse();
			}
		}
	}

	private void notifyResponse() {
		if (mRichMediaAd == null) {
			if (Debug)
				Log.d(TAG, "notifyResponse() mReponse ==null ");
			mRichMediaAd = new RichMediaAd();
			mRichMediaAd.setType(Const.AD_FAILED);
		}
		Log.d("Jas", "notifyResponse() type=" + mRichMediaAd.getType());
		if (mRichMediaAd.getType() == Const.VIDEO_TO_INTERSTITIAL
				|| mRichMediaAd.getType() == Const.INTERSTITIAL_TO_VIDEO
				|| mRichMediaAd.getType() == Const.VIDEO
				|| mRichMediaAd.getType() == Const.INTERSTITIAL) {
			notifyAdLoadSuccessed();
		} else {
			notifyAdNofound();
		}
	}

	// get the AdRequest
	private AdRequest getRequest() {
		if (mAdRequest == null) {
			mAdRequest = new AdRequest();
			mAdRequest.setDeviceId(mUniqueId1);
			mAdRequest.setDeviceId2(mUniqueId2);
			mAdRequest.setPublisherId(PUBLISHERID);
			mAdRequest.setUserAgent(mUserAgent);
			mAdRequest.setUserAgent2(Util.buildUserAgent());
		}
		Location location = null;
		if (this.mIncludeLocation) {
			location = Util.getLocation(mContext);
		}
		if (location != null) {
			mAdRequest.setLatitude(location.getLatitude());
			mAdRequest.setLongitude(location.getLongitude());
		} else {
			mAdRequest.setLatitude(0.0);
			mAdRequest.setLongitude(0.0);
		}
		mAdRequest.setConnectionType(Util.getConnectionType(mContext));
		mAdRequest.setIpAddress(Util.getLocalIpAddress());
		mAdRequest.setTimestamp(System.currentTimeMillis());

		mAdRequest.setType(AdRequest.VAD);
		mAdRequest.setRequestURL(this.mRequestURL);
		return mAdRequest;
	}
    /*Remove file when ad close
     * */
	private boolean CheckAd(RichMediaAd ad){
		if(ad == null)return false;
		if(ad.getType() == Const.NO_AD){
			File adfile = new File(DefaultFILEPATH);
			if(adfile.exists())adfile.delete();
			File file = new File(PATH);
			if (file.exists()) {
				String[] temp = file.list();
				if (temp != null) {
					for (int i = 0; i < temp.length; i++) {
						new File(PATH + temp[i]).delete();
					}
				}
			}
			return true;
		}
		return false;
	}
	/*
	 * Resave advert file to default dir,make sure its same as BootAnimation.cpp
	 * support.
	 */
	private boolean ResaveCacheLoaded() {
		// TODO Auto-generated method stub
		if (Debug)
			Log.d(TAG, "ResaveCacheLoaded()");
		File file = new File(PATH);
		if (file.exists()) {
			String[] temp = file.list();
			if (temp != null) {
				for (int i = 0; i < temp.length; i++) {
					if (temp[i].contains(Const.DOWNLOAD_PLAY_FILE)) {
						if(copyFile(new File(PATH + temp[i]), new File(
								DefaultFILEPATH)))Chmod();
						return true;
					}
				}
			}
		}
		return false;
	}
   
    private boolean Chmod() {
        boolean resault = true;
        try {
           Process p = Runtime.getRuntime().exec("chmod 777 /data/joyplus/bootanimation.mp4");
           int status = p.waitFor();
           if (status == 0) {
              resault = true;
           } else {
              resault = false;
           }
        } catch (IOException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        return resault;
    }
	private boolean copyFile(File srcFile, File dstFile) {
		if (Debug)
			Log.d(TAG, "copyFile()");
		try {
			InputStream in = new FileInputStream(srcFile);
			if (dstFile.exists())
				dstFile.delete();
			OutputStream out = new FileOutputStream(dstFile);
			try {
				int cnt;
				byte[] buf = new byte[4096];
				while ((cnt = in.read(buf)) >= 0) {
					out.write(buf, 0, cnt);
				}
			} finally {
				out.close();
				in.close();
			}
			if (Debug)
				Log.d(TAG, "copyFile() success");
			return true;
		} catch (IOException e) {
			if (Debug)
				Log.d(TAG, "copyFile() fail");
			return false;
		}
	}

	private void InitResource() {
		// TODO Auto-generated method stub
		if (Debug)
			Log.d(TAG, "InitResource()");
		this.mIncludeLocation = true;
		this.mRequestURL = Const.REQUESTURL;
		Util.GetPackage(mContext);
		serializeManager = new SerializeManager();
		mUserAgent = Util.getDefaultUserAgentString(mContext);
		this.mUniqueId1 = Util.getTelephonyDeviceId(mContext);
		this.mUniqueId2 = Util.getDeviceId(mContext);
		if ((PUBLISHERID == null) || (PUBLISHERID.length() == 0)) {
			throw new IllegalArgumentException(
					"User Id cannot be null or empty");
		}
		if ((mUniqueId2 == null) || (mUniqueId2.length() == 0)) {
			throw new IllegalArgumentException(
					"System Device Id cannot be null or empty");
		}
		mEnabled = (Util.getMemoryClass(mContext) > 16);
		Util.initializeAnimations(mContext);
	}

	private synchronized void notifyAdLoadSuccessed() {
		if (mListener == null)
			return;
		new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mListener.adLoadSucceeded();
			}
		}.run();
	}

	private synchronized void notifyAdNofound() {
		if (mListener == null)
			return;
		new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mListener.noAdFound();
			}
		}.run();
	}

	private synchronized void notifyAdClose() {
		if (mListener == null)
			return;
		new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mListener.Closed();
			}
		}.run();
	}

}
