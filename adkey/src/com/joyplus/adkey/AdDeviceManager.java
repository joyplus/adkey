package com.joyplus.adkey;

import android.content.Context;

public class AdDeviceManager {

	  private Context mContext;
	  private static AdDeviceManager mAdDeviceManager;
	  
	  public static AdDeviceManager getInstance(Context context){
		  if(mAdDeviceManager == null){
			  mAdDeviceManager = new AdDeviceManager(context);
		  }
		  return mAdDeviceManager;
	  }
	  private AdDeviceManager(Context context){
		  if(context != null){
			  mContext = context.getApplicationContext();
		  }
	  }
	 private String      mOS                   = "";//os
	 private String      mOSV                  = "";//osv
	 private int         mDSS                  = 0;//dss
	 private String      mDSR                  = "";//dsr
	 public  void SetOS(String os){
		 mOS = os;
	 }
	 public String GetOS(){
		 return mOS;
	 }
	 public void SetOSVersion(String osv){
		 mOSV = osv;
	 }
	 public String GetOSVersion(){
		 return mOSV;
	 }
	 public void SetDeviceScreenSize(int size){
		 mDSS = size;
	 }
	 public int GetDeviceScreenSize(){
		 return mDSS>0?mDSS:0;
	 }
	 public void SetDeviceScreenResolution(int Width,int Height){
		 if(Width<0 || Height<0)return;
		 mDSR = Width+"X"+Height;
	 }
     public String GetDeviceScreenResolution(){
    	 if(mDSR == null || "".equals(mDSR))return null;
    	 return mDSR;
     } 
}
