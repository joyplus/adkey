package com.joyplus.adkey.mon;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class AppReportManager {
    
	   private Context mContext;
	   private static AppReportManager mAppReportManager;
	   private AppMonitorServer mAppMonitorServer;
	   public  static AppReportManager getInstance(Context context){
		   if(mAppReportManager == null){
			   if(context == null)throw new IllegalArgumentException("context can't be null");
			   mAppReportManager = new AppReportManager(context);
		   }
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
