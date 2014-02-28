package com.joyplus.adkey.mon;

public class AppMonitior implements monitor{

	
	private  String mAppName = "";//an
	private  String mAppPackageName = "";//apn
	private  double mAppStartTime   = 0 ;//asti
	private  double mAppContinueTime= 0 ;//acti
	
	public  void  SetAppName(String name){
		mAppName = name;
	}
	public  String GetAppName(){
		if(mAppName == null)return "";
		return mAppName;
	} 
	
	public  void SetAppPackageName(String name){
		mAppPackageName = name;
	}
	public  String GetAppPackageName(){
		if(mAppPackageName == null)return "";
		return mAppPackageName;
	}
	
	public  void SetStartTime(double time){
		mAppStartTime = time;
	}
	public  double GetStartTime(){
		return mAppStartTime;
	}
	
	public  void SetContinueTime(double time){
		mAppContinueTime = time;
	}
	public  double GetContinueTime(){
		return mAppContinueTime;
	}
	
	@Override
	public TYPE getType() {
		// TODO Auto-generated method stub
		return monitor.TYPE.APP;
	}
	@Override
	public boolean IsAviable() {
		// TODO Auto-generated method stub
		//there we should judge this is useable
		//eg: continue time should be non-f.
		return true;
	}
}
