package com.joyplus.ad;

import android.content.Context;


public class AdManager {
      
	private Context mContext;
	
	private static AdManager mAdManager;
	public  static void Init(Context context) throws AdSDKManagerException{
		  if(AdSDKManager.IsInited())return;
		  if(context == null)throw new AdSDKManagerException("AdManager context is null !!!!!");
		  mAdManager = new AdManager(context);
	}
	public static AdManager getInstance(){
	      return mAdManager;
	}
    private AdManager(Context context){
  	      mContext = context;
  	     
    }
    
    /*Interface for Application*/
	public enum AD{
		  UNKNOW , ADBOOT , ADPLACE , BINNER , VAD 
	}
	  
	public enum ADTYPE{
		  FAILED                (-1),
		  IMAGE                 (0),
		  TEXT                  (1),
		  NO_AD                 (2),
		  VIDEO_TO_INTERSTITIAL (3),
		  INTERSTITIAL_TO_VIDEO (4),
		  VIDEO                 (5),
		  INTERSTITIAL          (6),
		  MRAID                 (7);		  
		  private int TYPE;
		  ADTYPE(int type){
			  TYPE = type;
		  }
		  public int toInt(){
			  return TYPE;
		  }
	}
	
	
	
	
	
}
