package com.joyplus.admonitor.mon;

import java.util.ArrayList;
import java.util.List;

import com.joyplus.admonitor.Application.AdMonitorSDKManager;

import android.content.Context;

public class AppReportManager {
    
	   private Context mContext;
	   private static AppReportManager mAppReportManager;
	   private AppReportServer mAppMonitorServer;
	   public  static AppReportManager getInstance(){
		   return mAppReportManager;
	   }
	   public  void Init(Context context){
		   if(AdMonitorSDKManager.IsInited())return;
		   if(context == null)throw new IllegalArgumentException("context can't be null");
		   mAppReportManager = new AppReportManager(context);
	   }
	   private AppReportManager(Context context){
		   mContext = context.getApplicationContext();
		   mAppMonitorServer = new AppReportServer(mContext);
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
