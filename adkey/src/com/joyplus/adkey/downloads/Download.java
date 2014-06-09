package com.joyplus.adkey.downloads;

import com.joyplus.adkey.downloads.Downloader.DownloaderState;
import android.webkit.URLUtil;

public class Download implements DownloaderListener,DownLoadListener{
      public enum STATE{
    	  IDLE , DOWNLOAD , FINISH
      }
      public String URL;
      
      public boolean WriteToTargetFile = true;
	  public String  TargetFile;
	  public String  LocalFile;
	  
	  public STATE  State = STATE.IDLE;
	  
	  public Download(){
	  }
	  public Download(Download down){
		  if(down != null){
			  URL = down.URL;
			  WriteToTargetFile = down.WriteToTargetFile;
			  TargetFile        = down.TargetFile;
			  LocalFile         = down.LocalFile;
			  State             = down.State;
		  }
	  }
	  public Download CreateNew(){
		  return new Download(this);
	  }
	  private DownLoadListener mDownLoadLinstener;
	  public  void SetDownLoadListener(DownLoadListener downloadListener){
		  mDownLoadLinstener = downloadListener;
	  }
	  public boolean Check(){
		  if(URLUtil.isHttpsUrl(URL)||URLUtil.isHttpUrl(URL))return false;
		  if(TargetFile == null || "".equals(TargetFile))return false;
		  if(LocalFile  == null || "".equals(LocalFile)) return false;
		  return true;
	  }



		@Override
		public void DownloaderStateChange(DownloaderState state) {
			// TODO Auto-generated method stub
			if(DownloaderState.INIT == state){
				if(mDownLoadLinstener != null){
					mDownLoadLinstener.Start(this);
				}
			}
		}
	
	
	
		@Override
		public void DownloaderProgress(int Dwonloaded, int TotleSize) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void DownloaderFinish() {
			// TODO Auto-generated method stub
			if(mDownLoadLinstener != null){
				mDownLoadLinstener.Finish(this);
			}
		}



		@Override
		public String toString() {
			// TODO Auto-generated method stub
			StringBuffer ap = new StringBuffer();
			ap.append("Download={")
			  .append(" URL="+URL)
			  .append(" ,WriteToTargetFile="+WriteToTargetFile)
			  .append(" ,TargetFile="+TargetFile)
			  .append(" ,LocalFile="+LocalFile)
			  .append(" ,State="+State.toString())
			  .append(" }");			
			return ap.toString();
		}
		@Override
		public void Start(Download download) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void Finish(Download download) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void NoAD() {
			// TODO Auto-generated method stub
			
		}
		
	 
	  
}
