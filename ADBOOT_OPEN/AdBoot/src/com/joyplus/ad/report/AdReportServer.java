package com.joyplus.ad.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.common.internet.AjaxCallBack;
import com.common.internet.FastHttp;
import com.common.internet.ResponseEntity;
import com.joyplus.ad.FASTTEST;
import com.joyplus.ad.config.Log;
import android.content.Context;
import android.os.Handler;
import android.os.Message;


public class AdReportServer {

	 private Context mContext;
	 private List<Report> mReportList;
	 private final static int  MAXSIZE = 100;
	 public AdReportServer(Context context){
		 mContext = context;
		 mReportList = new ArrayList<Report>();
	 }
	 
	 public void AddReport(List<Report> urls) {
		// TODO Auto-generated method stub
		if(urls == null)return;
		for(final Report url : urls){
			if(!url.Check())continue;
			AddReport(url);
		}
	 }
	 private void AddReport(Report url){
		 if(mReportList.size()>=MAXSIZE)return;
		 mReportList.add(url);
		 mHandler.sendEmptyMessage(MSG_CHECK_REPORT);
	 }
	 //msg
	 private final static int MSG_CHECK_REPORT   = 1;
	 private final static int MSG_START_REPORT   = 2;
	 private final static int MSG_ING_REPORT     = 3;
	 private final static int MSG_FINISH_REPORT  = 4;
	 private Report           mReport    = null;
	 private boolean          Checking   = false;
	 private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_CHECK_REPORT:
				if(Checking)break;
				Checking = true;
				if(mReport != null || mReportList.isEmpty()){
					Checking = false;
					break;
				}
				Iterator<Report> k = mReportList.iterator();
				while(k.hasNext()){
					mReport = k.next();
					k.remove();
					if(mReport.IsReported() || !mReport.Check()){
						mReport = null;
						continue;
					}
					break;
				}
				if(mReport != null)
					mHandler.sendEmptyMessage(MSG_START_REPORT);
				Checking = false;
				break;
			case MSG_START_REPORT:
				if(mReport == null ||mReport.IsReported()||mReport.GetNUM()<=0){
					mHandler.sendEmptyMessage(MSG_FINISH_REPORT);
					return;
				}
			case MSG_ING_REPORT:
				if(mReport.CanReported()){
					report_third(mReport.GetIMPRESSIONURL().URL);
				}else{
					mHandler.sendEmptyMessage(MSG_FINISH_REPORT);
				}
				break;
			case MSG_FINISH_REPORT:
				mReport.SetReported();
				mReportList.remove(mReport);
				mReport    = null;
				mHandler.sendEmptyMessage(MSG_CHECK_REPORT);
				break;
			}
		} 
	};
	
	private void report_third(String url){
		Log.d("AdReport = "+url);
		FASTTEST.REPORT++;//for test
		FastHttp.ajaxGet(url, new AjaxCallBack() {
			@Override
			public void callBack(ResponseEntity arg0) {
				mHandler.sendEmptyMessage(MSG_ING_REPORT);
			}
			@Override
			public boolean stop() {
				// TODO Auto-generated method stub
				return false;
			}}
		);
	} 
	 
	 
	 
}
