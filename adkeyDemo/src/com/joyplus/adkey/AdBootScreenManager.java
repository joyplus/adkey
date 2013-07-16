package com.joyplus.adkey;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import com.joyplus.adkey.download.Downloader;
import com.joyplus.adkey.download.ImpressionThread;
import com.joyplus.adkey.video.ResourceManager;
import com.joyplus.adkey.video.RichMediaAd;
import com.joyplus.adkey.video.VideoData;
import com.joyplus.adkey.widget.Log;
import com.joyplus.adkey.widget.SerializeManager;

import android.content.Context;
import android.location.Location;

public class AdBootScreenManager {
   
	public static final boolean FEATURE_ADBOOTSCREEN = true;
	private boolean Debug = true;
	private String  TAG = "AdBootScreen";
	
	private boolean mIncludeLocation = false;
	private String  mRequestURL = null;
	private SerializeManager serializeManager;
	
	private Context mContext;
	private String  PUBLISHERID = null;
	private String  mUniqueId1;
	private String  mUniqueId2;
	private String  mUserAgent;
	private boolean mEnabled = false;
	
	private AdRequest   mAdRequest = null;
	private Thread      mRequestThread;
    private RichMediaAd mRichMediaAd; // respone media
	
    
	private AdBootScreenListener mListener;
	public void setListener(AdBootScreenListener listener){
		 if(!FEATURE_ADBOOTSCREEN) return;
	     mListener = listener;
	}
	
	//this should be the same as it in BootAnimation.cpp
	private static final String PATH            = "/data/joy/";
	private static final String DefaultFILEPATH = PATH+"advertBootScreen.mp4";
    private static final String DefaultAD       = PATH+"adBootScreen";
    
    //private static final String PATH            = Const.DOWNLOAD_PATH + Util.VideoFileDir;
    //private static final String DefaultFILEPATH = PATH+"advertBootScreen.mp4";
    //private static final String DefaultAD       = PATH+"adBootScreen";
    
	public AdBootScreenManager(Context ctx, final String publisherId,
			final boolean cacheMode) throws Exception{
		 if(!FEATURE_ADBOOTSCREEN) throw new Exception("This Version don't support Advert boot Screen.");
		 this.PUBLISHERID = publisherId;
		 mContext = ctx;
		 Util.CACHE_MODE = cacheMode;
		 InitResource();
    }
	
	//Interface for user to Manager the resource.
	public void UpdateAdvert(){
		if(!FEATURE_ADBOOTSCREEN)return;
		 //resave advert file first.
		 ResaveCacheLoaded();
		 requestAd();
		 if(!WaitAd(20000)){
			 notifyAdClose();
			 return;
		 }
		 //now we can download mp4 file and report count.
		 ReportCount();		
		 DownloadFile();
	}
	
	//Interface Report count
	private void ReportCount(){
		new Thread(new ReportCountRunnable()).start();
	}
	//Tnterface Download file
	private void DownloadFile(){
		new Thread(new DownloadFileRunnable()).start();
	}
	
	
	private class DownloadFileRunnable implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			RichMediaAd tempAd = (RichMediaAd) serializeManager.readSerializableData(DefaultAD);
			if (tempAd != null){
				VideoData video = tempAd.getVideo();
				if (Util.CACHE_MODE && video != null){
					String Download_path = video.getVideoUrl();
					URL url = null;	
					try{
						url = new URL(Download_path);
					} catch (MalformedURLException e){ 
						// TODO Auto-generated catch block
						Log.d(TAG,"download fail");
						e.printStackTrace();
					}
					if (url != null){
						Util.ExternalName = "."+ Util.getExtensionName(url.getPath());
					} else{
						Util.ExternalName = ".mp4";
					}
					Downloader downloader = new Downloader(Download_path, mContext);
					if (Download_path.startsWith("http:")
					    || Download_path.startsWith("https:")){
							downloader.download();
							Log.i(TAG, "download starting");
					}
				}else{
					Log.i(TAG, "download fail aaaa"); 
					notifyAdNofound();
				}
			}else{
				Log.i(TAG, "download fail");
				notifyAdNofound();
			}
		}
	}
	
	private class ReportCountRunnable implements Runnable{     
        @Override 
		public void run() { 
			// TODO Auto-generated method stub
        	Log.d(TAG, "ReportCountRunnable run()");
			new ImpressionThread(mContext,mRichMediaAd.getmImpressionUrl(),PUBLISHERID,Util.AD_TYPE.FULL_SCREEN_VIDEO).start();	
		} 
	}  
	
	/*Wait for Ad return form Server,Time out : faslse , Result : true.*/
	private boolean WaitAd(int Time){
		//waiting for Ad result.
		 long now = System.currentTimeMillis();
		 long timeoutTime = now + Time;//20s
		 while ((mRichMediaAd == null) && (now < timeoutTime)){
			try	{
				Thread.sleep(200);
			} catch (InterruptedException e){
			}
			now = System.currentTimeMillis();
		 }
		 if(mRichMediaAd == null){//no Ad can use
			 if(Debug)Log.d(TAG," UpdateAdvertFile() mResponse == null");
			 notifyAdClose();
			 return false;
		 }
		 return true;
	}
	
	 /*request Ad from Server*/
	 private void requestAd(){
		  if(!FEATURE_ADBOOTSCREEN)return;
		  requestAd(null);
	 }
	  
	  private void requestAd(final InputStream xml){
          if(!FEATURE_ADBOOTSCREEN)return;
			Log.i(TAG, "AdBootScreenManager--->requestAd mEnabled="+mEnabled);
			if (!mEnabled)return;
			if (mRequestThread == null){
				mRichMediaAd = null;
				mRequestThread = new Thread(new Request(xml));				
				mRequestThread.setUncaughtExceptionHandler(
					new UncaughtExceptionHandler(){
							@Override
							public void uncaughtException(Thread thread,
									Throwable ex){
								mRichMediaAd = new RichMediaAd();
								mRichMediaAd.setType(Const.AD_FAILED);
								mRequestThread = null;
							}
				});
				mRequestThread.start();
			}
	 }
	  
	  /* get Ad from Server*/
	  private class Request implements Runnable{
			private InputStream XML = null;
			private RequestRichMediaAd requestAd;
			public Request(InputStream xml){
				XML = xml;
			}
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (ResourceManager.isDownloading()){
					try{
						Thread.sleep(200);
					} catch (InterruptedException e){
					}
				}
				try	{
					if(XML == null){
					    requestAd = new RequestRichMediaAd();
					}else {
						requestAd = new RequestRichMediaAd(XML);
					}
					AdRequest request = getRequest();
					File cacheDir = new File(PATH);
					if (!cacheDir.exists())cacheDir.mkdirs();
					mRichMediaAd = (RichMediaAd) serializeManager.readSerializableData(DefaultAD);
					RichMediaAd nextResponse = requestAd.sendRequest(request);
					serializeManager.writeSerializableData(DefaultAD,nextResponse);
					if (mRichMediaAd == null){ 
						mRichMediaAd = nextResponse;
					}
				} catch (Throwable t){
					mRichMediaAd = (RichMediaAd) serializeManager.readSerializableData(DefaultAD);
				}finally{
					notifyResponse();
				}
			}
		}
		
		private void notifyResponse(){
			if(mRichMediaAd == null ){
				if(Debug)Log.d(TAG,"notifyResponse() mReponse ==null ");
				mRichMediaAd = new RichMediaAd();
				mRichMediaAd.setType(Const.AD_FAILED);
			}
			if(mRichMediaAd.getType() == Const.VIDEO_TO_INTERSTITIAL
					|| mRichMediaAd.getType() == Const.INTERSTITIAL_TO_VIDEO
					|| mRichMediaAd.getType() == Const.VIDEO
					|| mRichMediaAd.getType() == Const.INTERSTITIAL){
				notifyAdLoadSuccessed();
			}else{
				notifyAdNofound();
			}
		}
		//get the AdRequest 
		private AdRequest getRequest(){
			if (mAdRequest == null){
				mAdRequest = new AdRequest();
				mAdRequest.setDeviceId(mUniqueId1);
				mAdRequest.setDeviceId2(mUniqueId2);
				mAdRequest.setPublisherId(PUBLISHERID);
				mAdRequest.setUserAgent(mUserAgent);
				mAdRequest.setUserAgent2(Util.buildUserAgent());
			}
			Location location = null;
			if (this.mIncludeLocation){
				location = Util.getLocation(mContext);
			}
			if (location != null){
				mAdRequest.setLatitude(location.getLatitude());
				mAdRequest.setLongitude(location.getLongitude());
			} else{
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
	
	/*Resave advert file to default dir,make sure its same as BootAnimation.cpp support.*/
	private boolean ResaveCacheLoaded() {
		// TODO Auto-generated method stub
		if(Debug)Log.d(TAG, "ResaveCacheLoaded()");
		File file = new File(PATH);
		if (file.exists()){
			String[] temp = file.list();
			if(temp != null){
				for (int i = 0; i < temp.length; i++){
					if (temp[i].contains(Const.DOWNLOAD_PLAY_FILE)){
						copyFile(new File(PATH+temp[i]),new File(DefaultFILEPATH));
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean copyFile(File srcFile, File dstFile) {
		if(Debug)Log.d(TAG, "copyFile()");
        try {
            InputStream in = new FileInputStream(srcFile);
            if (dstFile.exists()) {
                dstFile.delete();
            }
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
            if(Debug)Log.d(TAG, "copyFile() success");
            return true;
        } catch (IOException e) {
        	if(Debug)Log.d(TAG, "copyFile() fail");
            return false;
        }
    }
	
	private void InitResource() {
			// TODO Auto-generated method stub
			if(Debug)Log.d(TAG,"InitResource()");
		    this.mIncludeLocation = true;
		    this.mRequestURL = Const.REQUESTURL;
		    Util.GetPackage(mContext);
			serializeManager = new SerializeManager();
			mUserAgent = Util.getDefaultUserAgentString(mContext);
			this.mUniqueId1 = Util.getTelephonyDeviceId(mContext);
			this.mUniqueId2 = Util.getDeviceId(mContext);
			if (( PUBLISHERID == null) || (PUBLISHERID.length() == 0)){
				throw new IllegalArgumentException("User Id cannot be null or empty");
			}
			if ((mUniqueId2 == null) || (mUniqueId2.length() == 0)){
				throw new IllegalArgumentException("System Device Id cannot be null or empty");
			}
			mEnabled = (Util.getMemoryClass(mContext) > 16);
			Util.initializeAnimations(mContext);
	}
	
	private synchronized void notifyAdLoadSuccessed(){
		if(mListener == null) return ;
		new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mListener.adLoadSucceeded();
			}
		}.run();
	}
	
	private synchronized void notifyAdNofound(){
		if(mListener == null) return ;
		new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mListener.noAdFound();
			}
		}.run();
	}
	
	private synchronized void notifyAdReportFail(){
		if(mListener == null) return ;
		new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mListener.ReportFail();
			}
		}.run();
	}
	
	private synchronized void notifyAdReportSuccess(){
		if(mListener == null) return ;
		new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mListener.ReportSuccessed();
			}
		}.run();
	}

	private synchronized void notifyAdClose(){
		if(mListener == null) return ;
		new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mListener.Closed();
			}
		}.run();
	}

}
