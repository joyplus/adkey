package com.joyplus.admonitor.collect;

import java.util.ArrayList;
import java.util.List;

import com.joyplus.admonitor.Application.AdMonitorSDKException;
import com.joyplus.admonitor.Application.AdMonitorSDKManager;
import com.joyplus.admonitor.mon.AppMonitior;
import com.joyplus.admonitor.mon.AppReportManager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class CollectManager {

	   private Context mContext;
	   private static boolean Debug = true;
	   private static CollectManager mCollectManager;
	   public  static CollectManager getInstance(){
		   return mCollectManager;
	   }
	   public static void Init(Context context) throws AdMonitorSDKException{
		   if(AdMonitorSDKManager.IsInited())return;
		   if(context == null)throw new AdMonitorSDKException("AdMonitorManager context is null !!!!!");
		   mCollectManager = new CollectManager(context.getApplicationContext());
	   }
	   private CollectManager(Context context) throws AdMonitorSDKException{
		   mContext = context;
		   Register(true);
		   InitAppName();  
	   }
	   private AppListener mAppListener = new AppListener();
	   @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	   private void Register(boolean register){
		   Log.d("Jas","Register("+register+")");
		   if(register){
			   if(mContext instanceof Activity){
				   ((Activity)mContext).getApplication().registerActivityLifecycleCallbacks(mAppListener);
			   }else if(mContext instanceof Application){
				   ((Application)mContext).registerActivityLifecycleCallbacks(mAppListener);
			   }
		   }else{
			   if(mContext instanceof Activity){
				   ((Activity)mContext).getApplication().registerActivityLifecycleCallbacks(mAppListener);
			   }else if(mContext instanceof Application){
				   ((Application)mContext).registerActivityLifecycleCallbacks(mAppListener);
			   }
		   }
	   }
	   //for collect app info auto flag.
	   private static boolean AppCollectAuto = true;
	   public void SetAppAutoCollect(boolean auto){
		   AppCollectAuto = auto;
	   }
	   public  boolean GetAppAutoCollect(){
		   return AppCollectAuto;
	   }
	   //for info
	   private final static int MSG_AUTO_REPORTAPP = 0;
	   private Handler mHandler = new Handler(){
		  @Override
		  public void handleMessage(Message msg) {
			  // TODO Auto-generated method stub
			  super.handleMessage(msg);
			  if(Debug)Log.d("Jas","CollectManager handleMessage "+msg.what);
			  switch(msg.what){
			  case MSG_AUTO_REPORTAPP:
				  long start = GetAppTime(true);
				  long con   = GetAppTime(false)-start;
				  if(con>0 && start>0){
					  if(GetAppAutoCollect()){//now we can report it by ourself.
						  AppMonitior m = new AppMonitior();
						  m.SetAppName(mAppName);
						  m.SetAppPackageName(mPackageName);
						  m.SetStartTime(start);
						  m.SetContinueTime(con);
						  AppReportManager.getInstance().AddMonitor(m);
					  }else{
						  if(Debug)Log.d("Jas","Auto report was closed !!!");
					  }
				  }else{
					  if(Debug)Log.d("Jas","UnUse Time  start="+start+" ,con="+con);
				  }
				  SetAppStartTime();
				  break;
			  }
		  }
	   };
	   //for app collect 
	   private class AppListener implements ActivityLifecycleCallbacks{
	        private List<String> ActivityName = new ArrayList<String>();
			@Override
			public void onActivityCreated(Activity activity,
					Bundle savedInstanceState) {
				// TODO Auto-generated method stub
				if(Debug)Log.d("Jas","onActivityCreated-->"+activity.getLocalClassName());
				LogSize();
				if(ActivityName.size()<=0){
					mHandler.removeMessages(MSG_AUTO_REPORTAPP);
					mHandler.sendEmptyMessage(MSG_AUTO_REPORTAPP);
				}
				if(!ActivityName.contains(activity.getLocalClassName()))
				    ActivityName.add(activity.getLocalClassName());
			}
			@Override
			public void onActivityDestroyed(Activity activity) {
				// TODO Auto-generated method stub
				if(Debug)Log.d("Jas","onActivityDestroyed-->"+activity.getLocalClassName());
				LogSize();
				ActivityName.remove(activity.getLocalClassName());
				if(ActivityName.size()<=0){
					//now we can judge this application has finish
					SetAppEndTime();
					Register(false);
					if(Debug)Log.d("Jas","StartTime="+GetAppTime(true)+",endTime="+GetAppTime(false));
				}
				LogSize();
			}
			@Override
			public void onActivityPaused(Activity activity) {
				// TODO Auto-generated method stub
				if(Debug)Log.d("Jas","onActivityPaused-->"+activity.getLocalClassName());
				LogSize();
			}
			@Override
			public void onActivityResumed(Activity activity) {
				// TODO Auto-generated method stub
				if(Debug)Log.d("Jas","onActivityResumed-->"+activity.getLocalClassName());
				LogSize();
			}
			@Override
			public void onActivitySaveInstanceState(Activity activity,
					Bundle outState) {
				// TODO Auto-generated method stub
				if(Debug)Log.d("Jas","onActivitySaveInstanceState-->"+activity.getLocalClassName());
				LogSize();
			}
			@Override
			public void onActivityStarted(Activity activity) {
				// TODO Auto-generated method stub
				if(Debug)Log.d("Jas","onActivityStarted-->"+activity.getLocalClassName());
				LogSize();
			}
			@Override
			public void onActivityStopped(Activity activity) {
				// TODO Auto-generated method stub
				if(Debug)Log.d("Jas","onActivityStopped-->"+activity.getLocalClassName());
				LogSize();
			}
			private void LogSize(){
				if(Debug){
					Log.d("Jas","ActivityNameList size="+ActivityName.size());
				}
			}
	   }
	   
	   private void SetAppStartTime(){
		   if(Debug)Log.d("Jas","SetAppStartTime");
		   SharedPreferences sh = mContext.getSharedPreferences("AdMonitor", Context.MODE_PRIVATE);
		   long time = System.currentTimeMillis();
		   sh.edit().putLong("appcollectstart", time).commit();
		   sh.edit().putLong("appcollectend", time).commit();
	   }
	   private void SetAppEndTime(){ 
		   if(Debug)Log.d("Jas","SetAppEndTime");
		   SharedPreferences sh = mContext.getSharedPreferences("AdMonitor", Context.MODE_PRIVATE);
		   long time = System.currentTimeMillis();
		   sh.edit().putLong("appcollectend", time).commit();
	   }
	   private long GetAppTime(boolean start){
		   if(Debug)Log.d("Jas","GetAppTime("+start+")");
		   SharedPreferences sh = mContext.getSharedPreferences("AdMonitor", Context.MODE_PRIVATE);
		   if(start){
			   return sh.getLong("appcollectstart", -1);
		   }else
			   return sh.getLong("appcollectend", -1); 
	   }
	   private String mAppName     = "";
	   private String mPackageName = "";
	   private void InitAppName(){
		   ApplicationInfo info = mContext.getApplicationInfo();
		   mPackageName = info.packageName;
		   mAppName     = (String) mContext.getPackageManager().getApplicationLabel(info);
	   }
}
