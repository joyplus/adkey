package com.joyplus.adkey.download;

import com.joyplus.adkey.download.AdBootDownloader.AdBootDownloaderState;

public interface AdBootDownloaderListener {
  
	   void AdBootDownloaderStateChange(AdBootDownloaderState state);
	   
	   void AdBootDownloaderProgress(int Dwonloaded,int TotleSize);
	   
}
