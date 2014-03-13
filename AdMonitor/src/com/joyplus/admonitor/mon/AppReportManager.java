package com.joyplus.admonitor.mon;

import java.util.ArrayList;
import java.util.List;

import com.joyplus.admonitor.Application.AdMonitorSDKManager;

import android.content.Context;
import android.util.Log;

public class AppReportManager {
    
	   private Context mContext;
	   private static AppReportManager mAppReportManager;
	   private AppReportServer mAppMonitorServer;
	   public  static AppReportManager getInstance(){
		   return mAppReportManager;
	   }
	   public  static void Init(Context context){
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
	   
	    //for temp collect videoinfo
	    private VideoMonitior mVideoMonitior = null;
	    private long          mStartTime     = 0;
	    public  void SetCollectVideoStartInfo(String Prod_id, String Prod_name,long StartTime){
	    	   Log.d("Jas","SetCollectVideoStartInfo("+Prod_id+","+Prod_name+")");
	    	   mVideoMonitior = new VideoMonitior();
	    	   mVideoMonitior.SetProdId(Prod_id);
	    	   mVideoMonitior.SetProdName(Prod_name);
	    	   mVideoMonitior.SetStartTime(StartTime);
	    	   mStartTime = System.currentTimeMillis();
		}
	    public  void SetCollectVideoEndInfo(String Prod_id, String Prod_name){
	    	   Log.d("Jas","SetCollectVideoEndInfo("+Prod_id+","+Prod_name+")");
			   if(mVideoMonitior == null)return;
			   if(!(mVideoMonitior.GetProdId().equals(Prod_id)
					   ||(mVideoMonitior.GetProdName().equals(Prod_name))))return;
			   mVideoMonitior.SetContinueTime(System.currentTimeMillis() - mStartTime);
			   AppReportManager.getInstance().AddMonitor(mVideoMonitior.CreateNew());
			   mVideoMonitior = null;// for next start.
		}
}
