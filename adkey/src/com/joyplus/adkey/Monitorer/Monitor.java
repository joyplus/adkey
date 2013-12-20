package com.joyplus.adkey.Monitorer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.joyplus.adkey.Monitorer.TRACKINGURL.TYPE;

public class Monitor {
      public final static String REPLACE_MAC  = "%mac%";
      public final static String REPLACE_DM   = "%dm%";
	   
	  private List<TRACKINGURL>  mTrackingUrl = new ArrayList<TRACKINGURL>();
	  
	  private String             MAC          = "";//mac , mac , null
	  private String             PM           = "";//ds . dm . null
	  
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
						  ||(TYPE.ADMASTER == url.Type)
						  ||(TYPE.NIELSEN == url.Type)){//we shoule replace url first.
					  if(MAC == null || "".equals(MAC)){
						  url.URL.replace(REPLACE_MAC, "");
					  }else{ 
						  url.URL.replace(REPLACE_MAC, MD5Util.GetMD5Code(MAC));
					  }
					  if(PM == null || "".equals(PM)){
						  url.URL.replace(REPLACE_DM, "");
					  }else{ 
						  url.URL.replace(REPLACE_DM, PM);
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
