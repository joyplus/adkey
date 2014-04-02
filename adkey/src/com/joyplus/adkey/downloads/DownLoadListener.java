package com.joyplus.adkey.downloads;

public interface DownLoadListener {

	public void Start(Download download);
	
	public void Finish(Download download);
	
	public void NoAD();
}
