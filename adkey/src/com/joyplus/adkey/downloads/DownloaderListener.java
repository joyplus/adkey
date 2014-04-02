package com.joyplus.adkey.downloads;

import com.joyplus.adkey.downloads.Downloader.DownloaderState;

public interface DownloaderListener {
  
	   void DownloaderStateChange(DownloaderState state);
	   
	   void DownloaderProgress(int Dwonloaded,int TotleSize);
	   
	   void DownloaderFinish();
}
