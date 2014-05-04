package com.joyplus.adkey.mon;

import java.util.ArrayList;
import java.util.List;

import com.joyplus.adkey.AdKeySDKManager;

import android.content.Context;

public class AppReportManager {
    
	   private Context mContext;
	   private static AppReportManager mAppReportManager;
	   public  static void Init(Context context){
		    if(AdKeySDKManager.IsInited())return;
		    mAppReportManager = new AppReportManager(context);
	   }
	   private AppMonitorServer mAppMonitorServer;
	   public  static AppReportManager getInstance(){
		   return mAppReportManager;
	   }
	   public  static AppReportManager getInstance(Context context){
		   return mAppReportManager;
	   }
	   private AppReportManager(Context context){
		   mContext = context.getApplicationContext();
		   mAppMonitorServer = new AppMonitorServer(mContext);
	   }
	   
	   public void AddMonitor(monitor m){
		   if(m == null)return;
		   List<monitor> k = new ArrayList<monitor>();
		   k.add(m);
		   AddMonitor(k);
	   }
	   
	   public void AddMonitor(List<monitor> m){
		   if(m == null || m.size()<=0)return;
		   mAppMonitorServer.AddMonitor(m);
	   }
	   
}
