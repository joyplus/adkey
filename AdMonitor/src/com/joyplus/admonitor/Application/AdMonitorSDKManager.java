package com.joyplus.admonitor.Application;

import com.joyplus.admonitor.AdMonitorManager;
import com.joyplus.admonitor.phone.PhoneManager;

import android.content.Context;


/* Define by Jas@20131125
 * AdBoot manager for init environment and manager resource.
 * */

public class AdMonitorSDKManager {
	  
	  private Context mContext;
	  private CustomInfo mCustomInfo;
	  private static  boolean     mInited       = false;         //flog of this SDK init finish,and can use.
	  
      private static AdMonitorSDKManager mAdMonitorSDKManager;
      public  static AdMonitorSDKManager getInstance(){
    	  return mAdMonitorSDKManager;
      }
      
	  public  static void Init(Context context) throws AdMonitorSDKException{
		  if(IsInited())return;
		  if(context == null)throw new AdMonitorSDKException("AdMonitorSDKManager context is null !!!!!");
		  mAdMonitorSDKManager = new AdMonitorSDKManager(context.getApplicationContext());
	  }	  
	  
	  private AdMonitorSDKManager(Context context){
		  mContext = context;
		  InitResource();
	  }
	  public String toString(){
		  StringBuffer ap = new StringBuffer();
		  ap.append("AdMonitorSDKManager{")
		    .append(" ,mInited="+mInited)
		    .append(" }");
		  return ap.toString();
	  }
	  private void InitResource() {
		  // TODO Auto-generated method stub		  
		  try {
		    AdMonitorConfig.Init(mContext);
			AdMonitorManager.Init(mContext);
			PhoneManager.Init(mContext);
			SetSDKInited();
    	  } catch (AdMonitorSDKException e) {
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
	  
	  
	  public void SetCustomInfo(CustomInfo info){
		  mCustomInfo = info;
	  }
	  public CustomInfo GetCustomInfo(){
		  if(mCustomInfo == null)return new CustomInfo();
		  return mCustomInfo;
	  }
}
