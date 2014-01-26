package com.joyplus.ad.report;

import java.util.ArrayList;
import java.util.List;
import com.joyplus.ad.AdSDKManager;
import com.joyplus.ad.AdSDKManagerException;
import android.content.Context;

public class AdReportManager {
	
	private Context mContext;
	private AdReportServer mAdReportServer;
	private static AdReportManager mInstance;
	public  static AdReportManager getInstance(){
		return mInstance;
	}
	public  static void Init(Context context) throws AdSDKManagerException{
		if(AdSDKManager.IsInited())return;
		if(context == null)throw new AdSDKManagerException("AdReportManager context is null !!!!!");
		mInstance = new AdReportManager(context);
	}
	private AdReportManager(Context context){
		mContext = context;
		mAdReportServer = new AdReportServer(context);
	}
	//interface for application
	public void AddReport(Report url){
		if(url == null)return;
		List<Report> urls = new ArrayList<Report>();
		urls.add(url);
		AddReport(urls);
	}
	public void AddReport(List<Report> url){
		if(url == null)return;
		mAdReportServer.AddReport(url);
	}   
	
	
	
}
