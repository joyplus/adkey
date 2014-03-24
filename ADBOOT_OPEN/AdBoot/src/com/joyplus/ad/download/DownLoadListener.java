package com.joyplus.ad.download;

public interface DownLoadListener {

	public void Start(Download download);
	
	public void Finish(Download download);
	
	public void NoAD();
}
