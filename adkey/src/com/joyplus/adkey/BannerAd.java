package com.joyplus.adkey;

import java.util.ArrayList;
import java.util.List;
import com.joyplus.adkey.Monitorer.AdSDKFeature;
import com.joyplus.adkey.Monitorer.TRACKINGURL;
import com.joyplus.adkey.Monitorer.TRACKINGURL.TYPE;
import com.joyplus.adkey.data.ClickType;
import com.joyplus.adkey.request.Click;

public class BannerAd implements Ad {

	public static final String WEB = "web";
	public static final String OTHER = "other";

	private static final long serialVersionUID = 3271938798582141269L;
	private int type;
	private int bannerWidth;
	private int bannerHeight;
	private String text;
	private int skipOverlay = 0;
	private String imageUrl;
	private ClickType clickType;
	private String clickUrl;
	private String urlType;
	private int refresh;
	private boolean scale;
	private boolean skipPreflight;
	//add by Jas
	private String mCreative_res_url;
	private String mCreative_res_hash;
	public  void SetCreative_res_url(String url){
		mCreative_res_url = url;
	}
	public  String GetCreative_res_url(){
		return mCreative_res_url;
	}
	public  void SetCreative_res_hash(String url){
		mCreative_res_hash = url;
	}
	public  String GetCreative_res_hash(){
		return mCreative_res_hash;
	}
	private Click mClick = null;
	public  void SetClick(Click cl){
		mClick = (cl==null?null:cl.CreateNew());
	}
	public  Click GetClick(){
		return mClick;
	}
	//end add by Jas
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
				  ||(AdSDKFeature.MONITOR_MIAOZHEN && TYPE.MIAOZHEN==TrackingUrl.Type)
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

	public int getBannerHeight() {
		return this.bannerHeight;
	}

	public int getBannerWidth() {
		return this.bannerWidth;
	}

	public ClickType getClickType() {
		return this.clickType;
	}

	public String getClickUrl() {
		return this.clickUrl;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public int getRefresh() {
		return this.refresh;
	}

	public String getText() {
		return this.text;
	}

	@Override
	public int getType() {
		return this.type;
	}

	public String getUrlType() {
		return this.urlType;
	}

	public boolean isScale() {
		return this.scale;
	}

	public boolean isSkipPreflight() {
		return this.skipPreflight;
	}

	public void setBannerHeight(final int bannerHeight) {
		this.bannerHeight = bannerHeight;
	}

	public void setBannerWidth(final int bannerWidth) {
		this.bannerWidth = bannerWidth;
	}

	public void setClickType(final ClickType clickType) {
		this.clickType = clickType;
	}

	public void setClickUrl(final String clickUrl) {
		this.clickUrl = clickUrl;
	}

	public void setImageUrl(final String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setRefresh(final int refresh) {
		this.refresh = refresh;
	}

	public void setScale(final boolean scale) {
		this.scale = scale;
	}

	public void setSkipPreflight(final boolean skipPreflight) {
		this.skipPreflight = skipPreflight;
	}

	public void setText(final String text) {
		this.text = text;
	}

	@Override
	public void setType(final int adType) {
		this.type = adType;
	}

	public void setUrlType(final String urlType) {
		this.urlType = urlType;
	}

	@Override
	public String toString() {
		StringBuffer ap = new StringBuffer();
		for(TRACKINGURL url:this.getmTrackingUrl()){
			ap.append(" ,TRACKINGURL{"+url.toString()+"}");
		}
		return "Response [refresh=" + this.refresh + ", type=" + this.type
				+ ", bannerWidth=" + this.bannerWidth + ", bannerHeight="
				+ this.bannerHeight + ", text=" + this.text + ", imageUrl="
				+ this.imageUrl + ", clickType=" + this.clickType
				+ ", clickUrl=" + this.clickUrl + ", urlType=" + this.urlType
				+ ", scale=" + this.scale + ", skipPreflight="
				+ this.skipPreflight 
				+",Creative_res_url="+mCreative_res_url
				+",Creative_res_hash="+mCreative_res_hash
				+",mClick="+(mClick==null?"":mClick.toString())
				+ ap.toString()+ " ]";
	}

	public int getSkipOverlay() {
		return skipOverlay;
	}

	public void setSkipOverlay(int skipOverlay) {
		this.skipOverlay = skipOverlay;
	}

}
