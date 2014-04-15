package com.joyplus.adkey.request;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.joyplus.adkey.Ad;
import com.joyplus.adkey.BannerAd;
import com.joyplus.adkey.Monitorer.AdMonitorManager;
import com.joyplus.adkey.Monitorer.TRACKINGURL;
import com.joyplus.adkey.Monitorer.TRACKINGURL.TYPE;
import com.joyplus.adkey.video.RichMediaAd;

public class Report {
	
	 private Context mContext;
	 public  Report (Context context){
		 mContext = context;
	 }
     public  void report(Ad ad){
    	 if(ad == null)return;
    	 if(ad instanceof BannerAd){
    		 reportBanner((BannerAd) ad);
    	 }else if(ad instanceof RichMediaAd){
    		 reportMediaAd((RichMediaAd) ad);
    	 }
     }

	 private  void reportMediaAd(RichMediaAd ad) {
		// TODO Auto-generated method stub
		if(ad == null)return;
		List<TRACKINGURL> urls = ad.getmTrackingUrl();
		if(urls == null) urls = new ArrayList<TRACKINGURL>();
		TRACKINGURL joyplus = GetTRACKINGURL(ad.getmImpressionUrl(), TYPE.JOYPLUS);
		if(joyplus != null)urls.add(joyplus);
		if(urls != null && urls.size()>0){
			AdMonitorManager.getInstance(mContext).AddTRACKINGURL(urls);
		}
	 }
     
	 private  void reportBanner(BannerAd ad) {
		// TODO Auto-generated method stub
		if(ad == null)return;
		List<TRACKINGURL> urls = ad.getmTrackingUrl();
		if(urls == null) urls = new ArrayList<TRACKINGURL>();
		TRACKINGURL joyplus = GetTRACKINGURL(ad.getmImpressionUrl(), TYPE.JOYPLUS);
		if(joyplus != null)urls.add(joyplus);
		if(urls != null && urls.size()>0){
			AdMonitorManager.getInstance(mContext).AddTRACKINGURL(urls);
		}
	 }
	 
	 private TRACKINGURL GetTRACKINGURL(String url , TYPE type){
		 if(url == null || "".equals(url))return null;
		 TRACKINGURL URL = new TRACKINGURL();
		 URL.URL         = url;
		 URL.Type        = type;
		 return URL;
	 }
	 
}
