package com.joyplus.admonitor.mon;

public class AppMonitior implements monitor{

	
	private  String mAppName         = "";//an
	private  String mAppPackageName  = "";//apn
	private  long   mAppStartTime    = 0 ;//asti
	private  long   mAppContinueTime = 0 ;//acti
	
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
	
	public  void SetStartTime(long time){
		mAppStartTime = time;
	}
	public  long GetStartTime(){
		return mAppStartTime;
	}
	
	public  void SetContinueTime(long time){
		mAppContinueTime = time;
	}
	public  long GetContinueTime(){
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
		if(mAppName==null || "".equals(mAppName)
				|| mAppPackageName==null || "".equals(mAppPackageName))return false;
		return true;
	}
	
	public AppMonitior(){
		//for nothing to do.
	}
	public AppMonitior(AppMonitior info){
		if(info != null){
			mAppName         = info.mAppName;
			mAppPackageName  = info.mAppPackageName;
			mAppStartTime    = info.mAppStartTime;
			mAppContinueTime = info.mAppContinueTime;
		}
	}
	public AppMonitior CreateNew(){
		return new AppMonitior(this);
	}
}
