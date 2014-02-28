package com.joyplus.adkey.mon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.common.internet.AjaxCallBack;
import com.common.internet.FastHttp;
import com.common.internet.ResponseEntity;
import com.joyplus.adkey.AdKeyConfig;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AppMonitorServer{
    
	 private Context mContext;
	 private List<monitor> mMonitorList;
	 private final static int  MAXSIZE = 100;
	 private Report mReport = null;
	
	 public  AppMonitorServer(Context context){
		 mContext     = context;
		 mMonitorList = new ArrayList<monitor>();
		 Checking     = false;
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
			 Log.d("Jas","report_third-->"+url);
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
			 Uri.Builder b = Uri.parse(AdKeyConfig.getInstance().getAppREQUESTURL()).buildUpon();
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
			 return b.build();
		 }
		 private Uri GetVcURL(){
			 Uri.Builder bc = Uri.parse(AdKeyConfig.getInstance().getVcREQUESTURL()).buildUpon();
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
			 return bc.build();
		 }
	 }
}
