package com.joyplus.admonitor;

import java.util.ArrayList;
import java.util.List;

import com.joyplus.admonitor.Application.AdMonitorSDKException;
import com.joyplus.admonitor.Application.AdMonitorSDKManager;


import android.content.Context;

public class AdMonitorManager {
	
	private Context mContext;
	private Object mObject = new Object();
	
	private static AdMonitorManager mAdMonitorManager;
	public  static void Init(Context context) throws AdMonitorSDKException  {
		  if(AdMonitorSDKManager.IsInited())return;
		  if(context == null)throw new AdMonitorSDKException("AdMonitorManager context is null !!!!!");
		  mAdMonitorManager = new AdMonitorManager(context.getApplicationContext());
	}
	public static AdMonitorManager getInstance(){
	      return mAdMonitorManager;
	}
	private AdMonitorManager(Context context){
		  mContext = context;
		  mAdMonitorServer = new AdMonitorServer(mContext);
	}
	private AdMonitorServer mAdMonitorServer;
	
	//Interface for Application
	public void AddMonitor(Monitor url){
		synchronized (mObject) {
			if(url == null)return;
			List<Monitor> urls = new ArrayList<Monitor>();
			urls.add(url);
			mAdMonitorServer.AddMonitor(urls);//don't use interface fallow.
		}
	}
	public synchronized void AddMonitor(List<Monitor> url){
		synchronized (mObject) {
			if(url == null)return;
		    mAdMonitorServer.AddMonitor(url);
		}
	} 
	
}
