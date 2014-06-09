package com.joyplus.adkey.Monitorer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.joyplus.adkey.AdDeviceManager;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.Monitorer.TRACKINGURL.TYPE;

public class Monitor {
      public final static String REPLACE_MAC  = "%mac%";
      public final static String REPLACE_DM   = "%dm%";
	  public final static String REPLACE_IP   = "%ip%";
	  public final static String REPLACE_EX   = "%ex%";
	  public final static String REPLACE_UA   = "%UA%";
	  public final static String REPLACE_TS   = "%TS%";
	  
	  private List<TRACKINGURL>  mTrackingUrl = new ArrayList<TRACKINGURL>();
	  
	  private String             MAC          = "";//mac ,mac ,null
	  private String             PM           = "";//ds ,dm ,null
	  private String             IP           = "";//ip ,ip ,null
	  private String             EX           = "";//ex ,ex ,null
	  private String             UA           = "";//UA ,UA ,null
	  private String             TS           = "";//TS ,TS ,null
	  
	  public boolean CheckMonitor(){
		  if(mTrackingUrl == null || mTrackingUrl.size()<=0)return false;
		  for(TRACKINGURL url :mTrackingUrl){
			  if((AdSDKFeature.MONITOR_MIAOZHEN && TYPE.MIAOZHEN == url.Type)
						|| (AdSDKFeature.MONITOR_MIAOZHEN && TYPE.IRESEARCH == url.Type)
						|| (AdSDKFeature.MONITOR_MIAOZHEN && TYPE.ADMASTER == url.Type)
	                    || (AdSDKFeature.MONITOR_MIAOZHEN && TYPE.NIELSEN == url.Type)
	                    || TYPE.JOYPLUS == url.Type){
				    return true;
			  }
		  }
		  return false;
	  }
	  
	  public void SetPM(String pm){
		  PM = pm;
	  }
	  public String GetPM(){
		  if(PM == null || "".equals(PM))return "";
		  return PM;  
	  }
      
	  public void SetMAC(String mac){
		  MAC = mac;
	  }
	  public String GetMAC(){
		  if(MAC == null || "".equals(MAC))return "";
		  return MD5Util.GetMD5Code(MAC);
	  }
	  
	  public void SetIP(String ip){
		  IP = ip;
	  }
	  public String GetIP(){
		  if(IP == null || "".equals(IP))return "";
		  return IP;  
	  }
	  
	  public void SetEX(String ex){
		  EX = ex;
	  }
	  public String GetEX(){
		  if(EX == null || "".equals(EX))return "";
		  return EX;  
	  }
	  
	  public void SetUA(String ua){
		  UA = ua;
	  }
	  public String GetUA(){
		  if(UA == null || "".equals(UA))return "";
		  return UA;  
	  }
	  
	  public void SetTS(String ts){
		  TS = ts;
	  }
	  public String GetTS(){
		  if(TS == null || "".equals(TS))return "";
		  return TS;  
	  }
	  
	  public void SetTRACKINGURL(List<TRACKINGURL> urls){
		  mTrackingUrl = new ArrayList<TRACKINGURL>();
		  if(urls != null && urls.size()>0){
			  for(TRACKINGURL url :urls){
				  if((AdSDKFeature.MONITOR_MIAOZHEN && TYPE.MIAOZHEN == url.Type)
							|| (AdSDKFeature.MONITOR_MIAOZHEN && TYPE.IRESEARCH == url.Type)
							|| (AdSDKFeature.MONITOR_MIAOZHEN && TYPE.ADMASTER == url.Type)
		                    || (AdSDKFeature.MONITOR_MIAOZHEN && TYPE.NIELSEN == url.Type)
		                    || TYPE.JOYPLUS == url.Type){
					    if(!(url.URL == null || "".equals(url.URL))){
					    	mTrackingUrl.add(url);//now we can sure it useable.
					    }
				  }
			  }
		  }
	  }
	  public List<TRACKINGURL> GetTRACKINGURL(){
		  if(mTrackingUrl == null)
			  return new ArrayList<TRACKINGURL>();
		  return mTrackingUrl;
	  }
	  //
	  public TRACKINGURL GetFirstTRACKINGURL(){
		  if(mTrackingUrl == null || mTrackingUrl.size()<=0)
			  return null;
		  Iterator<TRACKINGURL> iterator = mTrackingUrl.iterator();
		  while(iterator.hasNext()){
			  TRACKINGURL url = iterator.next();
			  if((AdSDKFeature.MONITOR_MIAOZHEN && TYPE.MIAOZHEN == url.Type)
						|| (AdSDKFeature.MONITOR_MIAOZHEN && TYPE.IRESEARCH == url.Type)
						|| (AdSDKFeature.MONITOR_MIAOZHEN && TYPE.ADMASTER == url.Type)
	                    || (AdSDKFeature.MONITOR_MIAOZHEN && TYPE.NIELSEN == url.Type)
	                    || TYPE.JOYPLUS == url.Type){
				  if(url.URL == null || "".equals(url.URL) || url.Monitored){
					  iterator.remove();
					  continue;
				  }
				  if((TYPE.IRESEARCH == url.Type)
						 // ||(TYPE.ADMASTER == url.Type)
						  ||(TYPE.NIELSEN == url.Type)
						  || TYPE.JOYPLUS == url.Type){//we shoule replace url first.
					  AdDeviceManager mDevice = AdDeviceManager.getInstance(null);
					  if(MAC == null || "".equals(MAC)){
						  if(mDevice != null && mDevice.GetCUSTOMINFO() != null){
							  String mac = mDevice.GetCUSTOMINFO().GetMAC();
							  if(!(mac == null || "".equals(mac))){
								  url.URL=url.URL.replaceAll(REPLACE_MAC, MD5Util.GetMD5Code(mac.toUpperCase()));
							  }else{
								  url.URL=url.URL.replaceAll(REPLACE_MAC, "");
							  }
						  }else{
						      url.URL=url.URL.replaceAll(REPLACE_MAC, "");
						  }
					  }else{ 
						  url.URL=url.URL.replaceAll(REPLACE_MAC, MD5Util.GetMD5Code(MAC.toUpperCase()));
					  }
					  if(PM == null || "".equals(PM)){
						  if(mDevice != null && mDevice.GetCUSTOMINFO() != null){
							  String dm = mDevice.GetCUSTOMINFO().GetDEVICEMOVEMENT();
							  if(!(dm == null || "".equals(dm))){
								  url.URL=url.URL.replaceAll(REPLACE_MAC, dm);
							  }else{
								  url.URL=url.URL.replaceAll(REPLACE_MAC, "");
							  }
						  }else{
							  url.URL=url.URL.replaceAll(REPLACE_DM, "");
						  }
					  }else{ 
						  url.URL=url.URL.replaceAll(REPLACE_DM, PM);
					  }
					  if(IP == null || "".equals(IP)){
						  url.URL=url.URL.replaceAll(REPLACE_IP, "");
					  }else{ 
						  url.URL=url.URL.replaceAll(REPLACE_IP, IP);
					  }
					  if(EX == null || "".equals(EX)){
						  url.URL=url.URL.replaceAll(REPLACE_EX, "");
					  }else{ 
						  url.URL=url.URL.replaceAll(REPLACE_EX, EX);
					  }
					  
					  if(UA == null || "".equals(UA)){
						  String ua = Util.buildUserAgent();
						  if(ua ==null || "".equals(ua)){
							  url.URL=url.URL.replaceAll(REPLACE_UA, "");
						  }else{
							  url.URL=url.URL.replaceAll(REPLACE_UA, Escape.escape(ua));
						  }
					  }else{
						  url.URL=url.URL.replaceAll(REPLACE_UA, UA);
					  }
					  if(TS == null || "".equals(TS)){
						  url.URL=url.URL.replaceAll(REPLACE_TS, (""+System.currentTimeMillis()));
					  }else{
						  url.URL=url.URL.replaceAll(REPLACE_TS, TS);
					  }
				  }
				  return url;
			  }
		  }
		  return null;
	  }
	  public boolean RemoveFirstTRACKINGURL(TRACKINGURL url){
		  if(mTrackingUrl == null || mTrackingUrl.size()<=0)
			  return true;
		  return mTrackingUrl.remove(url);
	  }
}
