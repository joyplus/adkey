package com.joyplus.ad.download;

import com.joyplus.ad.download.Downloader.DownloaderState;

import android.webkit.URLUtil;

public class Download implements DownloaderListener{
      public enum STATE{
    	  IDLE , DOWNLOAD , FINISH
      }
      public String URL;
      
      public boolean WriteToTargetFile = true;
	  public String  TargetFile;
	  public String  LocalFile;
	  
	  public STATE  State = STATE.IDLE;
	  
	  
	  
	  public boolean Check(){
		  if(URLUtil.isHttpsUrl(URL)||URLUtil.isHttpUrl(URL))return false;
		  if(TargetFile == null || "".equals(TargetFile))return false;
		  if(LocalFile  == null || "".equals(LocalFile)) return false;
		  return true;
	  }



		@Override
		public void DownloaderStateChange(DownloaderState state) {
			// TODO Auto-generated method stub
			
		}
	
	
	
		@Override
		public void DownloaderProgress(int Dwonloaded, int TotleSize) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void DownloaderFinish() {
			// TODO Auto-generated method stub
			
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
		
	 
	  
}
