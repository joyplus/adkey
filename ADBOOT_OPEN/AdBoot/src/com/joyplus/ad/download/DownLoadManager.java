package com.joyplus.ad.download;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.joyplus.ad.AdFileManager;
import com.joyplus.ad.config.Log;
import com.joyplus.ad.data.FileUtils;
import com.joyplus.ad.download.Download.STATE;
import com.joyplus.ad.download.Downloader.DownloaderState;

import android.os.Handler;
import android.os.Message;

public class DownLoadManager {
       //private Context        mContext;
	   private List<Download> mDownload;
	   private Downloader     mDownloader     = null;
	   
	   private Download       mCrrentDownload = null;
	   
	   private final   static int  MSG_DOWNLOAD_IDLE   = 1;
	   private final   static int  MSG_DOWNLOAD_START  = 2;
	   
	   
	   private Object mObject = new Object();
	   
	   
	   public  static void Init(){
		   if(mInstance == null){
			   mInstance  = new DownLoadManager();   
		   }
	   }
	   private static DownLoadManager mInstance;
	   public  synchronized static DownLoadManager getInstance(){		   
		   return mInstance;
	   }
	   
	   private DownLoadManager(){
		   mDownload  = new ArrayList<Download>();
	   }
	   
	   public  void AddDownload(Download download){
		   if(download != null){
			   if(!(mDownload.contains(download))){
				   Log.d("AddDownload "+download.toString());
				   mDownload.add(download);
				   mHandler.sendEmptyMessage(MSG_DOWNLOAD_IDLE);
			   }
		   }
	   }
	   
	   public boolean RemoveDownload(Download download){
		   if(download.State == STATE.DOWNLOAD || download==null)return false;
		   return mDownload.remove(download);
	   }
	   
	   private  Handler mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				synchronized (mObject) {
					switch(msg.what){
					case MSG_DOWNLOAD_IDLE:
						if(mDownloader != null)return;
						if(mDownload.size()<=0)return;	
						Iterator<Download> it = mDownload.iterator();
						while(it.hasNext()){
							mCrrentDownload = it.next();
							if(mCrrentDownload.State == STATE.FINISH)
								it.remove();
							else {
								mHandler.sendEmptyMessage(MSG_DOWNLOAD_START);
								break;
							}
					    }
						break;
					case MSG_DOWNLOAD_START:
						if(mDownloader != null)return;
						if(mCrrentDownload.State == STATE.DOWNLOAD){
							return;
						}
						RemoveDownload(mCrrentDownload);
						mCrrentDownload.State = STATE.DOWNLOAD;
						mDownloader     = new Downloader(mCrrentDownload.URL,mHandler);
						mDownloader.SetDownloaderListener(new DownloadBackCall());		
						mDownloader.download();
						break;
					}
				}
			}		   
	   };
	   	   
	   private class DownloadBackCall implements DownloaderListener{
            private boolean Check(){
            	if(mCrrentDownload == null)return false;
            	return true;
            }
            private void Downloadfinish(){
            	//Log.d("DownloadManager Downloadfinish +++++ ");
            	mDownloader = null;
            	if(mCrrentDownload!=null)RemoveDownload(mCrrentDownload);
            	mHandler.sendEmptyMessage(MSG_DOWNLOAD_IDLE);
            }
			@Override
			public void DownloaderStateChange(DownloaderState state) {
				// TODO Auto-generated method stub
				//Log.d("DownloadManager DownloaderStateChange "+state.toString());
				if(!Check()){Downloadfinish();return;}
				mCrrentDownload.DownloaderStateChange(state);
			}
	
			@Override
			public void DownloaderProgress(int Dwonloaded, int TotleSize) {
				// TODO Auto-generated method stub
				if(!Check()){Downloadfinish();return;}
				mCrrentDownload.DownloaderProgress(Dwonloaded, TotleSize);
			}

			@Override
			public void DownloaderFinish() {
				// TODO Auto-generated method stub
				Log.d("DownloadManager DownloaderFinish ");
				if(!Check()){Downloadfinish();return;}
				File file = new File(AdFileManager.getInstance().GetBasePath(),Downloader.DOWNLOAD_FINISH);
			    if(file.exists()){
			    	if(!(mCrrentDownload.LocalFile != null || "".equals(mCrrentDownload.LocalFile))){
			    		FileUtils.copyFile(file, new File(mCrrentDownload.LocalFile));
			    	}
			    	if(mCrrentDownload.WriteToTargetFile){
			    		FileUtils.copyFile(file, new File(mCrrentDownload.TargetFile));
			    	}
			    }
			    mCrrentDownload.DownloaderFinish();
			    Downloadfinish();
			}
	   }
	   
}