package com.joyplus.adkey.widget;

import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;

import com.joyplus.adkey.Const;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.download.DisplayImgDownloader;
import com.joyplus.adkey.download.Downloader;
import com.joyplus.adkey.video.RichMediaAd;

public class DownloadVideoThread extends Thread {
	private String path = null;
	private Context context = null;

	public DownloadVideoThread(String path, Context context) {
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
		RichMediaAd nextResponse = (RichMediaAd) serializeManager
				.readSerializableData(path);
		if(nextResponse!=null)
		{
			if (nextResponse.getType() == Const.INTERSTITIAL
					|| nextResponse.getType() == Const.INTERSTITIAL_TO_VIDEO) {
				// img
				RichMediaAd mResponse = nextResponse;
				if (mResponse != null && mResponse.getInterstitial() != null) {
					if(!(mResponse.GetCreative_res_url()==null||"".equals(mResponse.GetCreative_res_url()))){
						String displayImagePath = mResponse.GetCreative_res_url();
						generateExtensionName(displayImagePath,".jpg");
						DisplayImgDownloader downloader = new DisplayImgDownloader(
								displayImagePath, context);
						if (displayImagePath.startsWith("http:")
								|| displayImagePath.startsWith("https:")) {
							downloader.download();
							Log.i(Const.TAG, "download starting");
						}
					}else{
						String textData = mResponse.getInterstitial().interstitialMarkup;
						if (textData != null) {
							int startInd = textData.indexOf("<img") + 10;
							int endInd = textData.indexOf(">", startInd) - 1;
							String displayImagePath = textData.substring(startInd,
									endInd);
							generateExtensionName(displayImagePath, ".jpg");
							DisplayImgDownloader downloader = new DisplayImgDownloader(
									displayImagePath, context);
							if (displayImagePath.startsWith("http:")
									|| displayImagePath.startsWith("https:")) {
								downloader.download();
								Log.i(Const.TAG, "jas download starting");
							}
						}
					}
				}
			} else if (nextResponse.getType() == Const.VIDEO
					|| nextResponse.getType() == Const.VIDEO_TO_INTERSTITIAL) {
				// video
				if (nextResponse != null && nextResponse.getVideo() != null) {
					String pathVideo = nextResponse.getVideo().getVideoUrl();
					if (Util.CACHE_MODE) {
						generateExtensionName(pathVideo, ".mp4");
						Downloader downloader = new Downloader(pathVideo, context);
						if (pathVideo.startsWith("http:")
								|| pathVideo.startsWith("https:")) {
							downloader.download();
							Log.i(Const.TAG, "jas video download starting");
						}
					}
				}
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