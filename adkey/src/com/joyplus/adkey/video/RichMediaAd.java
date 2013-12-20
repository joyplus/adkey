package com.joyplus.adkey.video;

import java.util.ArrayList;
import java.util.List;

import com.joyplus.adkey.Ad;
import com.joyplus.adkey.Monitorer.AdSDKFeature;
import com.joyplus.adkey.Monitorer.TRACKINGURL;
import com.joyplus.adkey.Monitorer.TRACKINGURL.TYPE;


public class RichMediaAd implements Ad {

	private static final long serialVersionUID          = 6443573739926220979L;

	public final static int   ANIMATION_FADE_IN         = 1;
	public final static int   ANIMATION_SLIDE_IN_TOP    = 2;
	public final static int   ANIMATION_SLIDE_IN_BOTTOM = 3;
	public final static int   ANIMATION_SLIDE_IN_LEFT   = 4;
	public final static int   ANIMATION_SLIDE_IN_RIGHT  = 5;
	public final static int   ANIMATION_FLIP_IN         = 6;
	public static final int   ANIMATION_NONE            = 0;

	private int               type;
	private int               animation;
	private VideoData         video;
	private InterstitialData  interstitial;
	private long              timestamp;
	
	private String mImpressionUrl;
	private List<TRACKINGURL> mTrackingUrl;
	
	public List<TRACKINGURL> getmTrackingUrl()
	{
		if(mTrackingUrl == null){
			mTrackingUrl = new ArrayList<TRACKINGURL>(); 
		}
		return mTrackingUrl;
	}

	public void setmTrackingUrl(TRACKINGURL TrackingUrl)
	{
		if(TrackingUrl == null)return;
		if((AdSDKFeature.MONITOR_IRESEARCH && TYPE.IRESEARCH==TrackingUrl.Type)
				  ||(AdSDKFeature.MONITOR_ADMASTER && TYPE.ADMASTER==TrackingUrl.Type)
				  ||((AdSDKFeature.MONITOR_NIELSEN && TYPE.NIELSEN==TrackingUrl.Type))){
			if(TrackingUrl.URL==null || "".equals(TrackingUrl.URL))return;
			if(mTrackingUrl == null){
				mTrackingUrl = new ArrayList<TRACKINGURL>();
			}
			mTrackingUrl.add(TrackingUrl);
		}
	}

	public String getmImpressionUrl()
	{
		return mImpressionUrl;
	}

	public void setmImpressionUrl(String mImpressionUrl)
	{
		this.mImpressionUrl = mImpressionUrl;
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public void setType(int adType) {
		this.type = adType;
	}

	public int getAnimation() {
		return animation;
	}

	public void setAnimation(int animation) {
		this.animation = animation;
	}

	public VideoData getVideo() {
		return video;
	}

	public void setVideo(VideoData video) {
		this.video = video;
	}

	public InterstitialData getInterstitial() {
		return interstitial;
	}

	public void setInterstitial(InterstitialData interstitial) {
		this.interstitial = interstitial;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "RichMediaAD [timestamp=" + timestamp + ", type=" + type
				+ ", animation=" + animation + ", video=" + video
				+ ", interstitial=" + interstitial + "]";
	}

}
