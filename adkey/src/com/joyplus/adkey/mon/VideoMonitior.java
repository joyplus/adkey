package com.joyplus.adkey.mon;

public class VideoMonitior implements monitor{

	private String mProd_id      = "";//cpi
	private String mProd_name    = "";//cpn
	private double mStartTime    = 0 ;//csti
	private double mContinueTime = 0;//ccti
	
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
	
	public  void SetStartTime(double time){
		mStartTime = time;
	}
	public  double GetStartTime(){
		return mStartTime;
	}
	
	public  void SetContinueTime(double time){
		mContinueTime = time;
	}
	public  double GetContinueTime(){
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
		return true;
	}
}
