package com.joyplus.ad.Monitor;

import java.util.ArrayList;
import java.util.List;

import com.joyplus.ad.AdSDKManager;
import com.joyplus.ad.AdSDKManagerException;

import android.content.Context;

public class AdMonitorManager {
	
	private Context mContext;
	private static AdMonitorManager mAdMonitorManager;
	public  static void Init(Context context) throws AdSDKManagerException{
		  if(AdSDKManager.IsInited())return;
		  if(context == null)throw new AdSDKManagerException("AdMonitorManager context is null !!!!!");
		  mAdMonitorManager = new AdMonitorManager(context);
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
		if(url == null)return;
		List<Monitor> urls = new ArrayList<Monitor>();
		urls.add(url);
		AddMonitor(urls);
	}
	public void AddMonitor(List<Monitor> url){
		if(url == null)return;
	    mAdMonitorServer.AddMonitor(url);
	}   
}
