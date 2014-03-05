package com.joyplus.admonitor.Application;

import java.util.List;

import com.joyplus.admonitor.AdMonitorManager;
import com.joyplus.admonitor.IMPRESSION;
import com.joyplus.admonitor.Monitor;
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
	
	
	//for even listener
	public static void AddCollertInfo(monitor info){
		if(!AdMonitorSDKManager.IsInited()) return;
		AppReportManager.getInstance().AddMonitor(info);
	}
	public static void AddCollertInfo(List<monitor> infos){
		if(!AdMonitorSDKManager.IsInited()) return;
		AppReportManager.getInstance().AddMonitor(infos);
	}
	
	
}
