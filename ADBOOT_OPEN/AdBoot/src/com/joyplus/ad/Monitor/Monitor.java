package com.joyplus.ad.Monitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.joyplus.ad.AdSDKFeature;
import com.joyplus.ad.data.MD5Util;
import com.joyplus.ad.data.TRACKINGURL;
import com.joyplus.ad.data.TRACKINGURL.TYPE;

public class Monitor {
	  private int   NUM  = 1;
      public final static String REPLACE_MAC  = "%mac%";
      public final static String REPLACE_DM   = "%dm%";
      public final static String REPLACE_IP   = "%ip%";
	  public final static String REPLACE_EX   = "%ex%";
	  
	  private List<TRACKINGURL>  mTrackingUrl = new ArrayList<TRACKINGURL>();
	  
	  private String             MAC          = "";//mac , mac , null
	  private String             PM           = "";//ds . dm . null
	  private String             IP           = "";//ip
	  private String             EX           = "";//ex
	  
	  public boolean CheckMonitor(){
		  if(mTrackingUrl == null || mTrackingUrl.size()<=0)return false;
		  for(TRACKINGURL url :mTrackingUrl){
			  if((AdSDKFeature.MONITOR_MIAOZHEN && TYPE.MIAOZHEN == url.Type)
						|| (AdSDKFeature.MONITOR_MIAOZHEN && TYPE.IRESEARCH == url.Type)
						|| (AdSDKFeature.MONITOR_MIAOZHEN && TYPE.ADMASTER == url.Type)
	                    || (AdSDKFeature.MONITOR_MIAOZHEN && TYPE.NIELSEN == url.Type)){
				    return true;
			  }
		  }
		  return false;
	  }
	  public void SetNUM(int u){
		  NUM = u;
	  }
	  public int GetNUM(){
		  if(NUM<=1)return 1;
		  else if(NUM>5)return 5;
		  else return NUM;
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
	  public void SetTRACKINGURL(List<TRACKINGURL> urls){
		  mTrackingUrl = new ArrayList<TRACKINGURL>();
		  if(urls != null && urls.size()>0){
			  for(TRACKINGURL url :urls){
				  if((AdSDKFeature.MONITOR_MIAOZHEN && TYPE.MIAOZHEN == url.Type)
							|| (AdSDKFeature.MONITOR_MIAOZHEN && TYPE.IRESEARCH == url.Type)
							|| (AdSDKFeature.MONITOR_MIAOZHEN && TYPE.ADMASTER == url.Type)
		                    || (AdSDKFeature.MONITOR_MIAOZHEN && TYPE.NIELSEN == url.Type)){
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
	                    || (AdSDKFeature.MONITOR_MIAOZHEN && TYPE.NIELSEN == url.Type)){
				  if(url.URL == null || "".equals(url.URL) || url.Monitored){
					  iterator.remove();
					  continue;
				  }
				  if((TYPE.IRESEARCH == url.Type)
						  //||(TYPE.ADMASTER == url.Type)
						  ||(TYPE.NIELSEN == url.Type)){//we shoule replace url first.
					  if(MAC == null || "".equals(MAC)){
						  url.URL=Replace(url.URL,REPLACE_MAC,"");
					  }else{ 
						  url.URL=Replace(url.URL,REPLACE_MAC,MD5Util.GetMD5Code(MAC.toUpperCase()));
					  }
					  if(PM == null || "".equals(PM)){
						  url.URL=Replace(url.URL,REPLACE_DM,"");
					  }else{ 
						  url.URL=Replace(url.URL,REPLACE_DM,PM);
					  }
					  if(IP == null || "".equals(IP)){
						  url.URL=Replace(url.URL,REPLACE_IP,"");
					  }else{ 
						  url.URL=Replace(url.URL,REPLACE_IP,IP);
					  }
					  if(EX == null || "".equals(EX)){
						  url.URL=Replace(url.URL,REPLACE_EX,"");
					  }else{ 
						  url.URL=Replace(url.URL,REPLACE_EX,EX);
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
	  private String Replace(String s,String d,String di){
		  String result = s;
		  try{
			  result = s.replaceAll(d, di);
		  }catch(NullPointerException e){
			  e.printStackTrace();
			  result = s;
		  }
		  return result;
	  }
}
