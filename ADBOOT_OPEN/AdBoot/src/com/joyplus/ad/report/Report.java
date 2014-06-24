package com.joyplus.ad.report;

import com.joyplus.ad.PublisherId;
import com.joyplus.ad.data.IMPRESSIONURL;

public class Report {
      
	private PublisherId   mId;
	private IMPRESSIONURL mURL; 
	private int           NUM = 1;
	private boolean       mReported = false;
	
	//private AdFileManager mAdFileManager;
	
	public void SetReported(){
		mReported = true;
	}
	public boolean IsReported(){
		return mReported;
	}
	public void SetNUM(int num){
		NUM = num;
	}
	public int GetNUM(){
		return NUM;
	}
	public boolean CanReported(){
		if(mURL == null || mURL.URL == null || "".equals(mURL.URL))return false;
		return ((NUM--)>0);
	}
	public boolean Check(){
		if(mURL == null || mURL.URL == null || "".equals(mURL.URL))return false;
		if(mId == null || !mId.CheckId())return false;
		return true;
	}
	
	public void SetIMPRESSIONURL(IMPRESSIONURL i){
		if(mURL != null)return;
		mURL = i;
	}
	public IMPRESSIONURL GetIMPRESSIONURL(){
		if(mURL == null)return new IMPRESSIONURL();
		return mURL;
	}
	
	public PublisherId GetPublisherId(){
		return mId;
	}
	public void SetPublisherId(PublisherId id){
		if(mId != null)return;
		mId = id;
		if(mId == null || !mId.CheckId())
			NUM = 0;
		//NUM = AdFileManager.getInstance().GetNum(mId);
	}
}
