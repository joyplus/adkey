package com.joyplus.adkey;

import android.content.Context;

public class AdDeviceManager {

	  private Context mContext;
	  private static AdDeviceManager mAdDeviceManager = null;
	  private CUSTOMINFO mCUSTOMINFO;
	  
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
