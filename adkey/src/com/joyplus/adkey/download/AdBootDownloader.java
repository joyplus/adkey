package com.joyplus.adkey.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.Util;

public class AdBootDownloader {
	
	private String urlstr;   //download Url.
	private String localfile;//localfile it was in SDCard or data/data/
	
	private int fileSize = 0;
	private int compeleteSize = 0;
	
	private Context context;
	    
    public enum AdBootDownloaderState{
    	IDLE , INIT , DOWNLOADING , STOP , FAILED , SUCCESS
    }
    private Object mObject = new Object();
    private AdBootDownloaderState    mSTATE;
    private AdBootDownloaderListener mAdBootDownloaderListener;
    public  void SetAdBootDownloaderListener(AdBootDownloaderListener listener){
    	synchronized (mObject) {
    		mAdBootDownloaderListener = listener;
 		}
    }
    private void SetAdBootDownloaderState(AdBootDownloaderState state){    	
    		mSTATE = state;
        	NotifyAdBootDownloaderState(mSTATE);	
    }
    private void NotifyAdBootDownloaderState(final AdBootDownloaderState state){    	
    	 if(mAdBootDownloaderListener != null){
    		 new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					synchronized (mObject) {
					   mAdBootDownloaderListener.AdBootDownloaderStateChange(state);
					}
				}
    		 }.run();
    	 }
    }
    
    private void NotifyAdBootDownloaderProgress(final int CompeleteSize,final int TotleSize){
    	if(mAdBootDownloaderListener != null){
	   		 new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						synchronized (mObject){
							mAdBootDownloaderListener.AdBootDownloaderProgress(CompeleteSize, TotleSize);
						}
					}
	   		 }.run();
   	    }
    }
	public AdBootDownloader(String urlstr, Context context) {
		this.urlstr = urlstr;
		this.context = context;
		File cacheDir = new File(Const.DOWNLOAD_PATH+Util.VideoFileDir);
		if (!cacheDir.exists())cacheDir.mkdirs();
		SetAdBootDownloaderState(AdBootDownloaderState.IDLE);
	}


	public void download() {
		if(mSTATE == AdBootDownloaderState.IDLE ||
		   mSTATE == AdBootDownloaderState.STOP){
				new MyThread().start();
		}
	}
    
	private class MyThread extends Thread {		
		@Override
		public void run() {
			localfile = Const.DOWNLOAD_PATH+Util.VideoFileDir+Const.DOWNLOADING_FILE;			
			HttpURLConnection connection = null;
			RandomAccessFile randomAccessFile = null;
			InputStream inputstream = null;
			try {
				SetAdBootDownloaderState(AdBootDownloaderState.INIT);
				URL url = new URL(urlstr);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(60*1000);
				connection.setRequestMethod("GET");
				fileSize = connection.getContentLength();
				randomAccessFile = new RandomAccessFile(localfile, "rwd");
				inputstream = connection.getInputStream();
				byte[] buffer = new byte[1024 * 50];
				int length = -1;
				while ((length = inputstream.read(buffer)) != -1) {
					SetAdBootDownloaderState(AdBootDownloaderState.DOWNLOADING);
					randomAccessFile.write(buffer, 0, length);
					compeleteSize += length;
					NotifyAdBootDownloaderProgress(compeleteSize,fileSize);
					if (compeleteSize == fileSize) {
						File file = new File(Const.DOWNLOAD_PATH+Util.VideoFileDir+Const.DOWNLOAD_READY_FILE);
						if(file.exists())file.delete();
						randomAccessFile.close();	
						File filetemp = new File(Const.DOWNLOAD_PATH+Util.VideoFileDir+Const.DOWNLOADING_FILE);
						if(filetemp.exists()){
							File filedone = new File(Const.DOWNLOAD_PATH+Util.VideoFileDir+Const.DOWNLOAD_PLAY_FILE+Util.ExternalName);
							if(filedone.exists())filedone.delete();
							filetemp.renameTo(filedone);
							SetAdBootDownloaderState(AdBootDownloaderState.SUCCESS);
						}
					}
					if (mSTATE == AdBootDownloaderState.STOP) {
						SetAdBootDownloaderState(AdBootDownloaderState.FAILED);
						return;
					}
				}
			} catch (Exception e) {	
				SetAdBootDownloaderState(AdBootDownloaderState.FAILED);
				e.printStackTrace();				
			} finally {				
				if(connection!=null)
					connection.disconnect();
				SetAdBootDownloaderState(AdBootDownloaderState.STOP);
			}
		}
	}

	public void stop() {
		SetAdBootDownloaderState(AdBootDownloaderState.STOP);
	}

	public AdBootDownloaderState getstate() {
		return mSTATE;
	}
	
}
