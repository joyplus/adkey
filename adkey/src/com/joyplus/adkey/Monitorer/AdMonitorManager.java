package com.joyplus.adkey.Monitorer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import com.joyplus.adkey.Util;
import android.content.Context;

public class AdMonitorManager {
	
	private Context mContext;
	private static AdMonitorManager mAdMonitorManager;
	public  static AdMonitorManager getInstance(Context context){
		  if(mAdMonitorManager == null){
			  if(context == null)throw new IllegalArgumentException("context can't be null");
			  mAdMonitorManager = new AdMonitorManager(context.getApplicationContext());
		  }
	      return mAdMonitorManager;
	}
	private AdMonitorManager(Context context){
		  mContext = context;
		  mAdMonitorServer = new AdMonitorServer(mContext);
	}
	private AdMonitorServer mAdMonitorServer;
	
	//Interface for Application
	public void AddTRACKINGURL(TRACKINGURL url){
		if(url == null)return;
		List<TRACKINGURL> urls = new ArrayList<TRACKINGURL>();
		urls.add(url);
		AddTRACKINGURL(urls);
	}
	public void AddTRACKINGURL(List<TRACKINGURL> urls){
		if(urls == null)return;
		AddMonitor(CreateMonitor(urls));
	}
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
	private Monitor CreateMonitor(List<TRACKINGURL> urls){
		if(urls == null || urls.size()<=0)return null; 
    	Monitor m = new Monitor();
		m.SetTRACKINGURL(urls);
		String mac = Util.GetMacAddress(mContext);
		if(mac == null || "".equals(mac)){
			m.SetMAC("");
		}else{
			m.SetMAC(mac);
		}
		String device_name = "V8";
		try {
			device_name = URLEncoder.encode(Util.GetDeviceName(), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(device_name == null || "".equals(device_name)){
			m.SetPM("");
		}else{
			m.SetPM(device_name);
		}
		return m;
	}
}
