package com.joyplus.ad;

import com.joyplus.ad.AdManager.AD;

public abstract class AdMode implements Ad{
	  private   AD          mAD;
	  protected PublisherId mPublisherId; 
	  public AdMode(AD ad){
		  if(!AdSDKManager.IsInited())
			  throw new IllegalArgumentException("AdSDKManager don't Init first !!!!");
		  mAD = ad;
	  }
	  
	  public AD GetAD(){
		  return mAD;
	  }
	  	  
	  public PublisherId GetPublisherId(){
		  return mPublisherId;
	  }
}
