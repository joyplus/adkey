package com.joyplus.ad.download;

import com.joyplus.ad.download.Downloader.DownloaderState;

public interface DownloaderListener {
  
	   void DownloaderStateChange(DownloaderState state);
	   
	   void DownloaderProgress(int Dwonloaded,int TotleSize);
	   
	   void DownloaderFinish();
}
