package com.joyplus.ad;


import com.joyplus.ad.Monitor.AdMonitorManager;
import com.joyplus.ad.download.DownLoadManager;
import com.joyplus.ad.report.AdReportManager;

import android.content.Context;


/* Define by Jas@20131125
 * AdBoot manager for init environment and manager resource.
 * */

public class AdSDKManager {
	  
	  private Context mContext;
	  private static  boolean     mInited       = false;         //flog of this SDK init finish,and can use.
	  private static  CUSTOMTYPE  mCustom       = CUSTOMTYPE.JOYPLUS; //flog for this SDK who use.
	  
	  
	  
      private static AdSDKManager mAdBootSDKManager;
      public  static AdSDKManager getInstance(){
    	  return mAdBootSDKManager;
      }
      public  static void Init(Context context) throws AdSDKManagerException{
    	  if(IsInited()||!AdSDKFeature.CUSTOM_CONFIG)return;//inited or can't be init by custom.
		  if(context == null)throw new AdSDKManagerException("AdBootSDKManager context is null !!!!!");
		  mCustom        = CUSTOMTYPE.JOYPLUS;
		  mAdBootSDKManager = new AdSDKManager(context.getApplicationContext());
      }
	  public  static void Init(Context context,CUSTOMTYPE custom) throws AdSDKManagerException{
		  if(IsInited())return;
		  if(context == null)throw new AdSDKManagerException("AdBootSDKManager context is null !!!!!");
		  mCustom           = custom;
		  mAdBootSDKManager = new AdSDKManager(context.getApplicationContext());
	  }	  
	  
	  private AdSDKManager(Context context){
		  mContext = context;
		  InitResource();
	  }
	  public String toString(){
		  StringBuffer ap = new StringBuffer();
		  ap.append("AdBootSDKManager{")
		    .append(" ,mInited="+mInited)
		    .append(" ,mCustom="+mCustom)
		    .append(" }");
		  return ap.toString();
	  }
	  private void InitResource() {
		  // TODO Auto-generated method stub		  
		  try {
			  AdConfig.Init(mContext);
			  PhoneManager.Init(mContext);
			  AdFileManager.Init(mContext); 
			  AdManager.Init(mContext);
			  DownLoadManager.Init();
			  AdMonitorManager.Init(mContext);
			  AdReportManager.Init(mContext);
			  SetSDKInited();
		  } catch (AdSDKManagerException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }		  
	  }
	  private void SetSDKInited(){
		  mInited = true;
	  }
	  /*Interface for this SDK use*/
	  public static boolean IsInited(){
		  return mInited;
	  }
	  /*Define by Jas@20131126
	   * Custom type:  defalut Joyplus */
	  public enum CUSTOMTYPE{//
		  CHANGHONG    (0,"CHANGHONG"),
		  OUBAOLI      (1,"OUBAOLI"),
		  KONKA        (2,"KONKA"),
		  SKYWORTH     (4,"Skyworth"),
		  HAIER        (5,"Haier"),
		  SONY         (6,"SONY"),
		  PANASONIC    (7,"Panasonic"),
		  TCL          (8,"TCL"),
		  HISENSE      (9,"Hisense"),
		  JOYPLUS      (10,"Joyplus");
		  int CUSTOM;
		  String BRAND;
		  CUSTOMTYPE(int Custom,String Brand){
			  CUSTOM = Custom;
			  BRAND  = Brand;
		  }
		  public int toInt(){
			  return CUSTOM;
		  }
		  public String toString(){
			  return BRAND;
		  }
	  }
	  public static CUSTOMTYPE GetCustomType(){
		  return mCustom;
	  }
}
