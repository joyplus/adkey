package com.joyplus.admonitor.Application;

import java.util.List;

import com.joyplus.admonitor.AdMonitorManager;
import com.joyplus.admonitor.IMPRESSION;
import com.joyplus.admonitor.Monitor;
import com.joyplus.admonitor.collect.CollectManager;
import com.joyplus.admonitor.mon.AppReportManager;
import com.joyplus.admonitor.mon.monitor;

//define by Jas@20140303
public class AdMonitor {
    //for base config
	public static void SetCUSTOMINFO(CUSTOMINFO info){
		if(!AdMonitorSDKManager.IsInited()) return;
		AdMonitorSDKManager.getInstance().SetCUSTOMINFO(info);
	}
	
	//for report monitor
	public static void AddMonitor(String url){// its a shortcut only for Joyplus report.
		if(!AdMonitorSDKManager.IsInited()) return;
		if(url == null || "".equals(url))return;
		IMPRESSION impression = new IMPRESSION();
		impression.mImpressionURL = url;
		Monitor  m = new Monitor();
		m.AddIMPRESSION(impression);
		AddMonitor(new Monitor());
	}
	public static void AddMonitor(Monitor url){
		if(!AdMonitorSDKManager.IsInited()) return;
		AdMonitorManager.getInstance().AddMonitor(url);
	}
	public static void AddMonitor(List<Monitor> url){
		if(!AdMonitorSDKManager.IsInited()) return;
		AdMonitorManager.getInstance().AddMonitor(url);
	}
	
	//for even listener this can add VideoMonitor and AppMonitor
	public static void AddCollectInfo(monitor info){
		if(!AdMonitorSDKManager.IsInited()) return;
		AppReportManager.getInstance().AddMonitor(info);
	}
	public static void AddCollectInfo(List<monitor> infos){
		if(!AdMonitorSDKManager.IsInited()) return;
		AppReportManager.getInstance().AddMonitor(infos);
	}
	//for collect app info auto
	public static void SetCollectAppAuto(boolean auto){
		if(!AdMonitorSDKManager.IsInited()) return;
		CollectManager.getInstance().SetAppAutoCollect(auto);
	}
	
	//for collect video info auto
	public static void SetCollectVideoStartInfo(String Prod_id, String Prod_name,long StartTime){
		if(!AdMonitorSDKManager.IsInited()) return;
		AppReportManager.getInstance().SetCollectVideoStartInfo(Prod_id, Prod_name, StartTime);
	}
    public static void SetCollectVideoEndInfo(String Prod_id, String Prod_name){
    	if(!AdMonitorSDKManager.IsInited()) return;
    	AppReportManager.getInstance().SetCollectVideoEndInfo(Prod_id, Prod_name);
	}
}
