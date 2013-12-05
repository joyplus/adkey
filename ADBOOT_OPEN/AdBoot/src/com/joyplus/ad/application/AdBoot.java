package com.joyplus.ad.application;

import com.joyplus.ad.PublisherId;

public class AdBoot {
   
	  private CUSTOMINFO  mCustomInfo  = null;
	  private AdBootInfo  mAdBootInfo  = null; 
	  private PublisherId mPublisherId = null;
	  
	  public AdBoot(){
		  mCustomInfo  = null;
		  mAdBootInfo  = null;
		  mPublisherId = null;
	  }
	  
	  public AdBoot(CUSTOMINFO custom,AdBootInfo adboot,PublisherId id){
		  if(id == null) new IllegalArgumentException("User Id cannot be null or empty"); 
		  if(custom != null)mCustomInfo  = custom.CreateNew();
		  if(adboot != null)mAdBootInfo  = adboot.CreateNew();
		  mPublisherId = new PublisherId(id.GetPublisherId());
	  }
	  
	  public CUSTOMINFO GetCUSTOMINFO(){
		  return mCustomInfo;
	  }
	  
	  public AdBootInfo GetAdBootInfo(){
		  return mAdBootInfo;
	  }
	  
	  public PublisherId GetPublisherId(){
		  return mPublisherId;
	  }
}
