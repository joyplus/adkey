package com.joyplus.admonitor.mon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.common.internet.AjaxCallBack;
import com.common.internet.FastHttp;
import com.common.internet.ResponseEntity;
import com.joyplus.admonitor.Application.AdMonitorConfig;
import com.joyplus.admonitor.Application.AdMonitorSDKManager;
import com.joyplus.admonitor.Application.CUSTOMINFO;
import com.joyplus.admonitor.Application.MD5Util;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

public class AppReportServer{
    
	 private Context mContext;
	 private List<monitor> mMonitorList;
	 private final static int  MAXSIZE = 100;
	 private Report mReport = null;
	 //private AdDeviceManager mAdDeviceManager;
	 public  AppReportServer(Context context){
		 mContext     = context;
		 mMonitorList = new ArrayList<monitor>();
		 Checking     = false;
		 //mAdDeviceManager = AdDeviceManager.getInstance(mContext);
	 }
	 
	 public void AddMonitor(List<monitor> urls){
		 if(urls == null)return;
		 for(monitor mo :urls){
			 if(!mo.IsAviable())continue;
			 AddMonitor(mo);
		 }
	 }
	 private void AddMonitor(monitor url){
		 if(mMonitorList.size()>=MAXSIZE)return;
		 mMonitorList.add(url);
		 mHandler.sendEmptyMessage(MSG_CHECK_MONITOR);
	 }
	 
	 private final static int MSG_CHECK_MONITOR   = 1;
	 private final static int MSG_START_MONITOR   = 2;
	 private final static int MSG_FINISH_MONITOR  = 3;
	 private boolean Checking = false;
	 private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_CHECK_MONITOR:
				if(Checking)return;
				Checking = true;
				if(mReport != null||mMonitorList.isEmpty()){
					Checking = false;
					break;
				}
				Iterator<monitor> k = mMonitorList.iterator();
				while(k.hasNext()){
					monitor s = k.next();
					k.remove();
					if(!s.IsAviable())continue;
					mReport = new Report(s);
					break;
				}
				if(mReport != null){
					mHandler.sendEmptyMessage(MSG_START_MONITOR);
				}
				Checking = false;
				break;
			case MSG_START_MONITOR:
				if(mReport == null){
					mHandler.sendEmptyMessage(MSG_FINISH_MONITOR);
					break;
				}
				mReport.StartMonitor();
				break;
			case MSG_FINISH_MONITOR:
				mReport = null;
				mHandler.sendEmptyMessage(MSG_CHECK_MONITOR);
				break;
			}
		}
	 };
	 
	 private class Report{
		 
		 private monitor mMonitor;
		 public  Report(monitor m){
			 mMonitor = m;
		 }
		 
		 public void StartMonitor(){
			 if(mMonitor == null || !mMonitor.IsAviable()){
				 Finish();
				 return;
			 }
			 if(mMonitor instanceof AppMonitior){
				 report_third(GetAppURL().toString());
			 }else if(mMonitor instanceof VideoMonitior){
				 report_third(GetVcURL().toString());
			 }
			 Finish();
		 }
		 
		 private void Finish(){
			 mHandler.sendEmptyMessage(MSG_FINISH_MONITOR);
		 }
		 
		 private void report_third(String url){
			 //Log.d("Jas","report_third-->"+url);
				FastHttp.ajaxGet(url, new AjaxCallBack() {
					@Override
					public void callBack(ResponseEntity arg0) {
						Finish();
					}
					@Override
					public boolean stop() {
						// TODO Auto-generated method stub
						return false;
					}}
				);
	     } 

		 private Uri GetAppURL(){
			 Uri.Builder b = Uri.parse(AdMonitorConfig.GetAppURL()).buildUpon();
			 AppMonitior m = (AppMonitior) mMonitor;
			 if(m.GetAppName() == null || "".equals(m.GetAppName())){
				 b.appendQueryParameter("an", "");
			 }else{
				 b.appendQueryParameter("an", m.GetAppName());
			 }
			 if(m.GetAppPackageName() == null || "".equals(m.GetAppPackageName())){
				 b.appendQueryParameter("apn", "");
			 }else{
				 b.appendQueryParameter("apn", m.GetAppPackageName());
			 }
			 b.appendQueryParameter("asti", ""+m.GetStartTime());
			 b.appendQueryParameter("acti", ""+m.GetContinueTime());
			 if(AdMonitorSDKManager.IsInited() && AdMonitorSDKManager.getInstance().GetCUSTOMINFO()!=null){
				    CUSTOMINFO info = AdMonitorSDKManager.getInstance().GetCUSTOMINFO();
					if(info.GetDEVICEMUMBER() == null){
						b.appendQueryParameter("ds", "");
					}else{
						b.appendQueryParameter("ds", info.GetDEVICEMUMBER());
					}
					if(info.GetSN() == null){
						b.appendQueryParameter("sn", "");
					}else{
						b.appendQueryParameter("sn", info.GetSN());
					}
					if(info.GetDEVICETYPE() == null){
						b.appendQueryParameter("dt", "");
					}else{
						b.appendQueryParameter("dt", Integer.toString(info.GetDEVICETYPE().toInt()));
					}
					if(info.GetUSEMODE() == null){
						b.appendQueryParameter("up", "");
					}else{
						b.appendQueryParameter("up", Integer.toString(info.GetUSEMODE().toInt()));
					}
					if(info.GetLICENSEPROVIDER() == null){
						b.appendQueryParameter("lp", "");
					}else{
						b.appendQueryParameter("lp", Integer.toString(info.GetLICENSEPROVIDER().toInt()));
					}
					if(info.GetDEVICEMOVEMENT() == null){
						b.appendQueryParameter("dm", "");
					}else{
						b.appendQueryParameter("dm", info.GetDEVICEMOVEMENT());
					}
					if(info.GetBRAND() == null){
						b.appendQueryParameter("b", "");
					}else{
						b.appendQueryParameter("b", info.GetBRAND().toString());
					}
					b.appendQueryParameter("ot", Integer.toString(info.GetLastBootUpCount()));
					if(info.GetSCREEN() == null){
						b.appendQueryParameter("screen", "");
					}else{
						b.appendQueryParameter("screen", info.GetSCREEN().toString());
					}
					if(info.GetSOURCETYPE() == null){
						b.appendQueryParameter("mt", "");
					}else{
						b.appendQueryParameter("mt", info.GetSOURCETYPE().toString());
					}
					if(info.GetOS()==null){
						b.appendQueryParameter("os", "");
					}else{
						b.appendQueryParameter("os", info.GetOS());
					}
					if(info.GetOSVersion()==null){
						b.appendQueryParameter("osv", "");
					}else{
						b.appendQueryParameter("osv", info.GetOSVersion());
					}
					b.appendQueryParameter("dss", Integer.toString(info.GetDeviceScreenSize()));
					if(info.GetDeviceScreenResolution()==null){
						b.appendQueryParameter("dsr", "");
					}else{
						b.appendQueryParameter("dsr", info.GetDeviceScreenResolution());
					}
					if(info.GetMAC()==null || "".equals(info.GetMAC())){
						b.appendQueryParameter("i", "");
					}else{
						b.appendQueryParameter("i", MD5Util.GetMD5Code(info.GetMAC().toUpperCase()));
					}
			 }
			 return b.build();
		 }
		 private Uri GetVcURL(){
			 Uri.Builder bc = Uri.parse(AdMonitorConfig.GetVcURL()).buildUpon();
			 VideoMonitior m = (VideoMonitior) mMonitor;
			 if(m.GetProdId() == null || "".equals(m.GetProdId())){
				 bc.appendQueryParameter("cpi", "");
			 }else{
				 bc.appendQueryParameter("cpi", m.GetProdId());
			 }
			 if(m.GetProdName() == null || "".equals(m.GetProdName())){
				 bc.appendQueryParameter("cpn", "");
			 }else{
				 bc.appendQueryParameter("cpn", m.GetProdName());
			 }
			 bc.appendQueryParameter("csti", ""+m.GetStartTime());
			 bc.appendQueryParameter("ccti", ""+m.GetContinueTime());
			 if(AdMonitorSDKManager.IsInited() && AdMonitorSDKManager.getInstance().GetCUSTOMINFO()!=null){
				    CUSTOMINFO info = AdMonitorSDKManager.getInstance().GetCUSTOMINFO();
					if(info.GetDEVICEMUMBER() == null){
						bc.appendQueryParameter("ds", "");
					}else{
						bc.appendQueryParameter("ds", info.GetDEVICEMUMBER());
					}
					if(info.GetSN() == null){
						bc.appendQueryParameter("sn", "");
					}else{
						bc.appendQueryParameter("sn", info.GetSN());
					}
					if(info.GetDEVICETYPE() == null){
						bc.appendQueryParameter("dt", "");
					}else{
						bc.appendQueryParameter("dt", Integer.toString(info.GetDEVICETYPE().toInt()));
					}
					if(info.GetUSEMODE() == null){
						bc.appendQueryParameter("up", "");
					}else{
						bc.appendQueryParameter("up", Integer.toString(info.GetUSEMODE().toInt()));
					}
					if(info.GetLICENSEPROVIDER() == null){
						bc.appendQueryParameter("lp", "");
					}else{
						bc.appendQueryParameter("lp", Integer.toString(info.GetLICENSEPROVIDER().toInt()));
					}
					if(info.GetDEVICEMOVEMENT() == null){
						bc.appendQueryParameter("dm", "");
					}else{
						bc.appendQueryParameter("dm", info.GetDEVICEMOVEMENT());
					}
					if(info.GetBRAND() == null){
						bc.appendQueryParameter("b", "");
					}else{
						bc.appendQueryParameter("b", info.GetBRAND().toString());
					}
					bc.appendQueryParameter("ot", Integer.toString(info.GetLastBootUpCount()));
					if(info.GetSCREEN() == null){
						bc.appendQueryParameter("screen", "");
					}else{
						bc.appendQueryParameter("screen", info.GetSCREEN().toString());
					}
					if(info.GetSOURCETYPE() == null){
						bc.appendQueryParameter("mt", "");
					}else{
						bc.appendQueryParameter("mt", info.GetSOURCETYPE().toString());
					}
					if(info.GetOS()==null){
						bc.appendQueryParameter("os", "");
					}else{
						bc.appendQueryParameter("os", info.GetOS());
					}
					if(info.GetOSVersion()==null){
						bc.appendQueryParameter("osv", "");
					}else{
						bc.appendQueryParameter("osv", info.GetOSVersion());
					}
					bc.appendQueryParameter("dss", Integer.toString(info.GetDeviceScreenSize()));
					if(info.GetDeviceScreenResolution()==null){
						bc.appendQueryParameter("dsr", "");
					}else{
						bc.appendQueryParameter("dsr", info.GetDeviceScreenResolution());
					}
					if(info.GetMAC()==null || "".equals(info.GetMAC())){
						bc.appendQueryParameter("i", "");
					}else{
						bc.appendQueryParameter("i", MD5Util.GetMD5Code(info.GetMAC().toUpperCase()));
					}
			 }
			 return bc.build();
		 }
	 }
	 
}
