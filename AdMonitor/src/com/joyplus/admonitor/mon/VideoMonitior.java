package com.joyplus.admonitor.mon;

public class VideoMonitior implements monitor{

	private String mProd_id      = "";//cpi
	private String mProd_name    = "";//cpn
	private long   mStartTime    = 0 ;//csti
	private long   mContinueTime = 0 ;//ccti
	
	public  void SetProdId(String id){
		mProd_id = id;
	}
	public  String GetProdId(){
		if(mProd_id == null)return "";
		return mProd_id;
	}
	
	public  void SetProdName(String name){
		mProd_name = name;
	}
	public  String GetProdName(){
		if(mProd_name == null)return "";
		return mProd_name;
	}
	
	public  void SetStartTime(long time){
		mStartTime = time;
	}
	public  long GetStartTime(){
		return mStartTime;
	}
	
	public  void SetContinueTime(long time){
		mContinueTime = time;
	}
	public  long GetContinueTime(){
		return mContinueTime;
	}
	@Override
	public TYPE getType() {
		// TODO Auto-generated method stub
		return monitor.TYPE.VIDEO;
	}
	@Override
	public boolean IsAviable() {
		// TODO Auto-generated method stub
		//there we should judge this is useable
		//eg: continue time should be non-f.
		if(mProd_id==null || "".equals(mProd_id)
			|| mProd_name==null || "".equals(mProd_name))return false;
		return true;
	}
	public VideoMonitior(){
		//for nothing to do.
	}
	public VideoMonitior(VideoMonitior info){
		if(info != null){
			mProd_id      = info.mProd_id;
			mProd_name    = info.mProd_name;
			mStartTime    = info.mStartTime;
			mContinueTime = info.mContinueTime;
		}
	}
	public VideoMonitior CreateNew(){
		return new VideoMonitior(this);
	}
}
