package com.joyplus.adkey.widget;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import android.content.Context;
import com.joyplus.adkey.AdRequest;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.RequestException;
import com.joyplus.adkey.RequestRichMediaAd;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.download.DisplayImgDownloader;
import com.joyplus.adkey.download.DownloaderSmallVideo;
import com.joyplus.adkey.video.RichMediaAd;

public class DownloadSmallVideoThread extends Thread {
	private String path = null;
	private Context context = null;
	private SerializeManager serializeManager = null;
	private AdRequest request = null;

	public DownloadSmallVideoThread(String path, Context context,
			AdRequest request) {
		this.path = path;
		this.context = context;
		this.request = request;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serializeManager = new SerializeManager();
		RequestNextAd(path);
		RichMediaAd nextResponse = (RichMediaAd) serializeManager
				.readSerializableData(path);
		if(nextResponse!=null){
			if (nextResponse.getType() == Const.INTERSTITIAL
					|| nextResponse.getType() == Const.INTERSTITIAL_TO_VIDEO) {
				// img
				RichMediaAd mResponse = nextResponse;
				if (mResponse != null && mResponse.getInterstitial() != null) {
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
							Log.i(Const.TAG, "download starting");
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
						DownloaderSmallVideo downloader = new DownloaderSmallVideo(
								pathVideo, context);
						String localfile = Const.DOWNLOAD_PATH + Util.VideoFileDir
								+ Const.DOWNLOADING_SMALLVIDEO;
						File tempFile = new File(localfile);
						if (!tempFile.exists()) {
							if (pathVideo.startsWith("http:")
									|| pathVideo.startsWith("https:")) {
								downloader.download();
								Log.i(Const.TAG, "download starting");
							}
						}
					}
				}
			}
		}
	}

	private void RequestNextAd(String pathTemp) {
		RequestRichMediaAd requestAd = new RequestRichMediaAd();
		RichMediaAd mResponse = null;
		try {
			mResponse = (RichMediaAd) serializeManager
					.readSerializableData(pathTemp);
			if (mResponse != null) {
				RichMediaAd nextResponse = requestAd.sendRequest(request);
				serializeManager.writeSerializableData(pathTemp, nextResponse);
			} else {
				mResponse = requestAd.sendRequest(request);
				serializeManager.writeSerializableData(pathTemp, mResponse);
			}
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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