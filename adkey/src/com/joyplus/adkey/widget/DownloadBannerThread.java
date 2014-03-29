package com.joyplus.adkey.widget;

import java.net.MalformedURLException;
import java.net.URL;
import com.joyplus.adkey.BannerAd;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.download.DisplayImgDownloader;

import android.content.Context;

public class DownloadBannerThread extends Thread{

	private String path = null;
	private Context context = null;

	public DownloadBannerThread(String path, Context context) {
		this.path = path;
		this.context = context;
	}
	
	@Override
	public void run(){
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SerializeManager serializeManager = new SerializeManager();
		BannerAd nextResponse = (BannerAd) serializeManager.readSerializableData(path);
		if(nextResponse!=null){
			String displayImagePath = nextResponse.GetCreative_res_url();
			if(displayImagePath==null || "".equals(displayImagePath))return;
			generateExtensionName(displayImagePath,".jpg");
			DisplayImgDownloader downloader = new DisplayImgDownloader(
					displayImagePath, context);
			if (displayImagePath.startsWith("http:")
					|| displayImagePath.startsWith("https:")) {
				downloader.download();
				Log.i("Jas", "download starting");
			}
		}
			
	}
	
	
	private void generateExtensionName(String path, String defaultType) {
		URL url = null;
		try {
			url = new URL(path);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (url != null) {
			Util.ExternalName = "." + Util.getExtensionName(url.getPath());
		} else {
			Util.ExternalName = defaultType;
		}
	}
}
