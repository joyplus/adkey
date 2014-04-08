package com.joyplus.ad.db;

import java.util.ArrayList;
import java.util.List;
import com.joyplus.ad.PublisherId;
import com.joyplus.ad.Monitor.AdMonitorManager;
import com.joyplus.ad.Monitor.Monitor;
import com.joyplus.ad.application.CUSTOMINFO;
import com.joyplus.ad.config.Log;
import com.joyplus.ad.data.IMPRESSIONURL;
import com.joyplus.ad.data.TRACKINGURL;
import com.joyplus.ad.report.AdReportManager;
import com.joyplus.ad.report.Report;

import android.content.Context;


public class AdBootThread {
	private AdBootDao     mAdBootDao ;
	private CUSTOMINFO    Info;
	private Context       mContext;
	public AdBootThread(Context context,CUSTOMINFO info){
		mContext = context;
		Info = info;
	}
	public void StartReport(){
		mAdBootDao = AdBootDao.getInstance(mContext);
		if(mAdBootDao != null){
			ArrayList<AdBootImpressionInfo> Info = mAdBootDao.GetAllReport();
			Log.d("StartReport-->"+(Info==null?"NULL":Info.size()));
			if(Info != null && Info.size()>0){
				for(AdBootImpressionInfo info:Info){
					Log.d("Report-->"+info.toString());
					Report(info);
					ReportThread(info);
				}
			}
			mAdBootDao.delAll();//make sure it work.
		}
	}
	private void ReportThread(AdBootImpressionInfo info) {
		// TODO Auto-generated method stub
		Monitor m = new Monitor();
    	if(Info != null ){
    		if(!("".equals(Info.GetDEVICEMOVEMENT()))){//dm
    			m.SetPM(Info.GetDEVICEMOVEMENT());
    		}else if(!("".equals(Info.GetDEVICEMUMBER()))){//ds
    			m.SetPM(Info.GetDEVICEMUMBER());
    		}
    		if(!("".equals(Info.GetMAC()))){//mac
    			m.SetMAC(Info.GetMAC());
    		}
    	}
    	List<TRACKINGURL> URL = new ArrayList<TRACKINGURL>();
    	TRACKINGURL admaster = GetTRACKINGURL(info.admaster, TRACKINGURL.TYPE.ADMASTER);
    	if(admaster != null) URL.add(admaster);
    	TRACKINGURL iresearch = GetTRACKINGURL(info.iresearch,TRACKINGURL.TYPE.IRESEARCH);
    	if(iresearch != null)URL.add(iresearch);
    	TRACKINGURL miaozhen = GetTRACKINGURL(info.miaozhen, TRACKINGURL.TYPE.MIAOZHEN);
    	if(miaozhen != null)URL.add(miaozhen);
    	TRACKINGURL nielsen  = GetTRACKINGURL(info.nielsen,  TRACKINGURL.TYPE.NIELSEN);
    	if(nielsen != null) URL.add(nielsen);
    	m.SetTRACKINGURL(URL);
    	if(URL.size()>0)AdMonitorManager.getInstance().AddMonitor(m);
	}
	private TRACKINGURL GetTRACKINGURL(String url,TRACKINGURL.TYPE Type){
		if(url == null || "".equals(url))return null;
		TRACKINGURL URL = new TRACKINGURL();
		URL.Type        = Type;
		URL.URL         = url;
		return URL;
	}
	private void Report(AdBootImpressionInfo info) {
		// TODO Auto-generated method stub
		if(info == null || !info.IsAviable())return;
		Report r = new Report();
	    r.SetPublisherId(new PublisherId(info.publisher_id));
	    IMPRESSIONURL URL = new IMPRESSIONURL();
	    URL.URL = info.mImpressionUrl;
	    r.SetIMPRESSIONURL(URL);
	    Log.d("Report-->"+r.GetNUM());
		AdReportManager.getInstance().AddReport(r);
	}
}
