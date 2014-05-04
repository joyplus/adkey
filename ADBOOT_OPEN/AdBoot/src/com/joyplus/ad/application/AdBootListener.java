package com.joyplus.ad.application;

public interface AdBootListener {

	public void NoAd();
	
	public void Finish();
	
	public void NoAn();
	
	public void DownLoadProgress(String TargetFile,long complete,long Totle);
}
