package com.joyplus.adkey.downloads;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import com.joyplus.adkey.widget.Log;
import android.os.Handler;
import android.webkit.URLUtil;

public class Downloader {
	
	private String urlstr;   //download Url.
	private String localfile;//localfile it was in SDCard or data/data/
	private Handler mHandler;
	
	private boolean Running = false;
	
	private int fileSize = 0;
	private int compeleteSize = 0;
	
	//private Context context;
	public  final static String DOWNLOAD_DOWNLOAD = "download_downloading";
	public  final static String DOWNLOAD_FINISH   = "download_finish";
	
    public enum DownloaderState{
    	IDLE , INIT , DOWNLOADING , STOP , FAILED , SUCCESS
    }
    private Object mObject = new Object();
    private DownloaderState    mSTATE;
    private DownloaderListener mDownloaderListener;
    public  void SetDownloaderListener(DownloaderListener listener){
    	synchronized (mObject) {
    		mDownloaderListener = listener;
 		}
    }
    private void SetDownloaderState(DownloaderState state){    	
    		mSTATE = state;
        	NotifyDownloaderState(mSTATE);	
    }
    private void NotifyDownloaderState(final DownloaderState state){    	
    	 if(mDownloaderListener != null){
    		 new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					synchronized (mObject) {
					   mDownloaderListener.DownloaderStateChange(state);
					}
				}
    		 }.run();
    	 }
    }
    
    private void NotifyDownloaderFinish(){    	
	   	 if(mDownloaderListener != null){
	   		 new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						synchronized (mObject) {
						   mDownloaderListener.DownloaderFinish();
						}
					}
	   		 }.run();
	   	 }
   }
    
    private void NotifyDownloaderProgress(final int CompeleteSize,final int TotleSize){
    	if(mDownloaderListener != null){
	   		 new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						synchronized (mObject){
							mDownloaderListener.DownloaderProgress(CompeleteSize, TotleSize);
						}
					}
	   		 }.run();
   	    }
    }
	public Downloader(String urlstr,Handler handler) {
		this.urlstr  = urlstr;
		//this.context = context;
		SetDownloaderState(DownloaderState.IDLE);
		Running       = false;
		mHandler      = handler;
	}


	public void download() {
		if(mSTATE == DownloaderState.IDLE ||
		   mSTATE == DownloaderState.STOP){
				new MyThread().start();
		}
	}
    
	private class MyThread extends Thread {		
		@Override
		public void run() {
			if(!((URLUtil.isHttpsUrl(urlstr)||URLUtil.isHttpUrl(urlstr))
			     || AdFileManager.getInstance().UseAble())){
				Log.d("urlstr-->"+urlstr
						+",urlstr="+(URLUtil.isHttpsUrl(urlstr)||URLUtil.isHttpUrl(urlstr))
						+",UseAble="+AdFileManager.getInstance().UseAble());
				NotifyDownloaderState(DownloaderState.FAILED);
				NotifyDownloaderFinish();
				return;
			}
			if(Running) return;
			Running    = true;
			localfile  = (new File(AdFileManager.getInstance().GetBasePath(),DOWNLOAD_DOWNLOAD)).toString();			
			HttpURLConnection connection = null;
			RandomAccessFile randomAccessFile = null;
			InputStream inputstream = null;
			try {
				SetDownloaderState(DownloaderState.INIT);
				URL url = new URL(urlstr);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(60*1000);
				connection.setRequestMethod("GET");
				fileSize = connection.getContentLength();
				randomAccessFile = new RandomAccessFile(localfile, "rwd");
				inputstream = connection.getInputStream();
				byte[] buffer = new byte[1024 * 50];
				int length = -1;
				while (Running &&((length = inputstream.read(buffer)) != -1)) {
					SetDownloaderState(DownloaderState.DOWNLOADING);
					randomAccessFile.write(buffer, 0, length);
					compeleteSize += length;
					NotifyDownloaderProgress(compeleteSize,fileSize);
					if (compeleteSize == fileSize) {
						randomAccessFile.close();	
						File filetemp = new File(localfile);
						if(filetemp.exists()){
							File filedone = new File(AdFileManager.getInstance().GetBasePath(),DOWNLOAD_FINISH);
							if(filedone.exists())filedone.delete();
							filetemp.renameTo(filedone);
							FileUtils.Chmod(filedone);
							SetDownloaderState(DownloaderState.SUCCESS);
						}
					}
					if (mSTATE == DownloaderState.STOP) {
						SetDownloaderState(DownloaderState.FAILED);
						return;
					}
				}
			} catch (Exception e) {	
				Log.d("Exception-->"+e.toString());
				SetDownloaderState(DownloaderState.FAILED);
				e.printStackTrace();				
			} finally {		
				Running = false;
				if(connection!=null)
					connection.disconnect();
				SetDownloaderState(DownloaderState.STOP);
				NotifyDownloaderFinish();
			}
		}
	}

	public void stop() {
		SetDownloaderState(DownloaderState.STOP);
	}

	public DownloaderState getstate() {
		return mSTATE;
	}
	
}
