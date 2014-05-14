package com.joyplus.adkey;

import com.joyplus.adkey.widget.Log;

import android.content.Context;

public class AdDeviceManager {

	  private Context mContext;
	  private static AdDeviceManager mAdDeviceManager = null;
	  private CUSTOMINFO mCUSTOMINFO;
	  public  static void Init(Context context){
		   if(AdKeySDKManager.IsInited())return;
		   mAdDeviceManager = new AdDeviceManager(context);
	  }
	  public static AdDeviceManager getInstance(){
		  return mAdDeviceManager;
	  }
	  public static AdDeviceManager getInstance(Context context){
		  return mAdDeviceManager;//for Compatible before.
	  }
	  
	  private AdDeviceManager(Context context){
		  Log.d("AdDeviceManager create");
	      mContext = context.getApplicationContext();
	  }
	  public void SetCUSTOMINFO(CUSTOMINFO info){
		  if(info != null){
			  mCUSTOMINFO = info.CreateNew();
		  }else{
			  mCUSTOMINFO = null;
		  }
	  }
	  public CUSTOMINFO GetCUSTOMINFO(){
		  return mCUSTOMINFO;
	  }
}
