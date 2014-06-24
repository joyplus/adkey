package com.joyplus.adkey;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import com.joyplus.adkey.Monitorer.MD5Util;
import android.net.Uri;
import android.os.Build;

public class AdRequest {

	private String userAgent;
	private String userAgent2;
	private String headers;
	private String deviceId;
	private String listAds;
	private String requestURL;
	private String protocolVersion;
	private String publisherId;
	private double longitude = 0.0;
	private double latitude = 0.0;
	private static final String REQUEST_TYPE_ANDROID = "android_app";
	private int type = -1;

	public static final int BANNER = 0;
	public static final int VAD = 1;
    
	private String ipAddress;

	private String deviceId2;

	private String connectionType;

	private long timestamp;
    //add by Jas
	public static final int PATCH = 2;
	private String PATCHVC  = "";
	private String PATCHVID = "";
	public String getPatchVC() {
		return PATCHVC;
	}

	public void setPatchVC(String vc) {
		PATCHVC = vc;
	}
	private String MacAddress;
	public String getMACAddress() {
		if (this.ipAddress == null)
			return "";
		return this.ipAddress;
	}
	public void setMACAddress(final String macAddress) {
		this.MacAddress = macAddress;
	}
	//end add by Jas
	public String getAndroidVersion() {
		return Build.VERSION.RELEASE;
	}

	public String getConnectionType() {
		return this.connectionType;
	}

	public String getDeviceId() {
		if (this.deviceId == null)
			return "";
		return this.deviceId;
	}

	public String getDeviceId2() {
		return this.deviceId2;
	}

	public String getDeviceMode() {
		return Build.MODEL;
	}

	public String getHeaders() {
		if (this.headers == null)
			return "";
		return this.headers;
	}

	public String getIpAddress() {
		if (this.ipAddress == null)
			return "";
		return this.ipAddress;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public String getListAds() {
		if (this.listAds != null)
			return this.listAds;
		else
			return "";
	}

	public double getLongitude() {
		return this.longitude;
	}

	public String getProtocolVersion() {
		if (this.protocolVersion == null)
			return Const.VERSION;
		else
			return this.protocolVersion;
	}

	public String getPublisherId() {
		if (this.publisherId == null)
			return "";
		return this.publisherId;
	}

	public String getRequestType() {
		return AdRequest.REQUEST_TYPE_ANDROID;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public String getUserAgent() {
		if (this.userAgent == null)
			return "";
		return this.userAgent;
	}

	public String getUserAgent2() {
		if (this.userAgent2 == null)
			return "";
		return this.userAgent2;
	}

	public void setConnectionType(final String connectionType) {
		this.connectionType = connectionType;
	}

	public void setDeviceId(final String deviceId) {
		this.deviceId = deviceId;
	}

	public void setDeviceId2(final String deviceId2) {
		this.deviceId2 = deviceId2;
	}

	public void setHeaders(final String headers) {
		this.headers = headers;
	}

	public void setIpAddress(final String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setLatitude(final double latitude) {
		this.latitude = latitude;
	}

	public void setListAds(final String listAds) {
		this.listAds = listAds;
	}

	public void setLongitude(final double longitude) {
		this.longitude = longitude;
	}

	public void setProtocolVersion(final String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public void setPublisherId(final String publisherId) {
		this.publisherId = publisherId;
	}

	public void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}

	public void setUserAgent(final String userAgent) {
		this.userAgent = userAgent;
	}

	public void setUserAgent2(final String userAgent) {
		this.userAgent2 = userAgent;
	}
    
	public void setPatchVC(){
		
	}
	@Override
	public String toString() {

		return this.toUri().toString();
	}

	public Uri toUri() {
		final Uri.Builder b = Uri.parse(this.getRequestURL()).buildUpon();
		b.appendQueryParameter("rt", this.getRequestType());
		b.appendQueryParameter("v", this.getProtocolVersion());
		b.appendQueryParameter("u", this.getUserAgent());
		b.appendQueryParameter("u2", this.getUserAgent2());
		b.appendQueryParameter("s", this.getPublisherId());
		b.appendQueryParameter("o", this.getDeviceId());
		b.appendQueryParameter("o2", this.getDeviceId2());
		b.appendQueryParameter("t", Long.toString(this.getTimestamp()));
		b.appendQueryParameter("connection_type", this.getConnectionType());
		b.appendQueryParameter("listads", this.getListAds());
		switch(getType()){
		case BANNER:
			b.appendQueryParameter("c.mraid", "1");
			b.appendQueryParameter("sdk","banner");
			break;
		case VAD:
			b.appendQueryParameter("c.mraid", "0");
			b.appendQueryParameter("sdk","vad");
			break;
		case PATCH:
			b.appendQueryParameter("vc", PATCHVC);
			//b.appendQueryParameter("vid", VID);
			break;
		}
		b.appendQueryParameter("u_wv", this.getUserAgent());
		//add by Jas@20140227
		AdDeviceManager mDevice = AdDeviceManager.getInstance(null);
		if(mDevice != null && mDevice.GetCUSTOMINFO() != null){
			CUSTOMINFO info = mDevice.GetCUSTOMINFO();
			if(info.GetDEVICEMUMBER() == null){//we should get ds by-self.
				String device_name = "V8";
				try {
					device_name = URLEncoder.encode(Util.GetDeviceName(), "utf-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				b.appendQueryParameter("ds", device_name);
			}else{
				b.appendQueryParameter("ds", info.GetDEVICEMUMBER());
			}
			if(info.GetSN() == null){
				b.appendQueryParameter("sn", "");
			}else{
				b.appendQueryParameter("sn", info.GetSN());
			}
			if(info.GetDEVICETYPE() == null){
				b.appendQueryParameter("dt", "");
			}else{
				b.appendQueryParameter("dt", Integer.toString(info.GetDEVICETYPE().toInt()));
			}
			if(info.GetUSEMODE() == null){ 
				b.appendQueryParameter("up", "");
			}else{
				b.appendQueryParameter("up", Integer.toString(info.GetUSEMODE().toInt()));
			}
			if(info.GetLICENSEPROVIDER() == null){
				b.appendQueryParameter("lp", "");
			}else{
				b.appendQueryParameter("lp", Integer.toString(info.GetLICENSEPROVIDER().toInt()));
			}
			if(info.GetDEVICEMOVEMENT() == null){
				b.appendQueryParameter("dm", "");
			}else{
				b.appendQueryParameter("dm", info.GetDEVICEMOVEMENT());
			}
			if(info.GetBRAND() == null){
				b.appendQueryParameter("b", "");
			}else{
				b.appendQueryParameter("b", info.GetBRAND().toString());
			}
			b.appendQueryParameter("ot", Integer.toString(info.GetLastBootUpCount()));
			if(info.GetSCREEN() == null){
				b.appendQueryParameter("screen", "");
			}else{
				b.appendQueryParameter("screen", info.GetSCREEN().toString());
			}
			if(info.GetSOURCETYPE() == null){
				b.appendQueryParameter("mt", "");
			}else{
				b.appendQueryParameter("mt", info.GetSOURCETYPE().toString());
			}
			if(info.GetOS()==null){
				b.appendQueryParameter("os", "");
			}else{
				b.appendQueryParameter("os", info.GetOS());
			}
			if(info.GetOSVersion()==null){
				b.appendQueryParameter("osv", "");
			}else{
				b.appendQueryParameter("osv", info.GetOSVersion());
			}
			b.appendQueryParameter("dss", Integer.toString(info.GetDeviceScreenSize()));
			if(info.GetDeviceScreenResolution()==null){
				b.appendQueryParameter("dsr", "");
			}else{
				b.appendQueryParameter("dsr", info.GetDeviceScreenResolution());
			}
			if(info.GetMAC()==null || "".equals(info.GetMAC())){
				if(MacAddress == null || "".equals(MacAddress)){
					b.appendQueryParameter("i", "");
				}else{
					b.appendQueryParameter("i", MD5Util.GetMD5Code(MacAddress.toUpperCase()));
				}
			}else{
				b.appendQueryParameter("i", MD5Util.GetMD5Code(info.GetMAC().toUpperCase()));
			}
		}else{//now we should get mac and ds by-self.
			if(MacAddress == null || "".equals(MacAddress)){
				b.appendQueryParameter("i", "");
			}else{
				b.appendQueryParameter("i", MD5Util.GetMD5Code(MacAddress.toUpperCase()));
			}
			String device_name = "V8";
			try {
				device_name = URLEncoder.encode(Util.GetDeviceName(), "utf-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			b.appendQueryParameter("ds", device_name);
		}
		//end add by Jas
		return b.build();
	}

	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}