package com.joyplus.ad.Monitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import cn.com.mma.mobile.tracking.api.Countly;
import com.joyplus.ad.Monitor.Monitorer.MonitorerState;
import com.joyplus.ad.config.Log;
import com.joyplus.ad.data.TRACKINGURL;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class AdMonitorServer implements MonitorListener{
    
	 private Context mContext;
	 private List<Monitor> mMonitorList;
	 private final static int  MAXSIZE = 100;
	 //add by Jas for admaster
     private final static String CONFIG_URL = "http://admaster.mobi/sdkconfig.xml";
	 public AdMonitorServer(Context context){
		 mContext = context;
		 mMonitorList = new ArrayList<Monitor>();
		 //add by Jas for admaster
		 Countly.sharedInstance().init(mContext, CONFIG_URL);
	 }
	 public void AddMonitor(List<Monitor> urls) {
		// TODO Auto-generated method stub
		if(urls == null)return;
		for(final Monitor url : urls){
			if(!url.CheckMonitor())continue;
			AddMonitor(url);
		}
	 }
	 private void AddMonitor(Monitor url){
		 if(mMonitorList.size()>=MAXSIZE)return;
		 mMonitorList.add(url);
		 mHandler.sendEmptyMessage(MSG_CHECK_MONITOR);
	 }
	 //msg
	 private final static int MSG_CHECK_MONITOR   = 1;
	 private final static int MSG_START_MONITOR   = 2;
	 private final static int MSG_FINISH_MONITOR  = 3;
	 private Monitorer        mMonitorer = null;
	 private Monitor          mMonitor   = null;
	 private boolean          Checking   = false;
	 private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_CHECK_MONITOR:
				if(Checking)break;
				Checking = true;
				if(mMonitorer != null || mMonitorList.isEmpty()){
					Checking = false;
					break;
				}
				Log.d("mMonitorList size = "+mMonitorList.size());
				Iterator<Monitor> k = mMonitorList.iterator();
				while(k.hasNext()){
					mMonitor = k.next();
					k.remove();
					if(mMonitor.GetFirstTRACKINGURL() == null){
						mMonitor = null;
						continue;
					}
					if(mMonitor != null)break;
				}
				if(mMonitor != null)
					mHandler.sendEmptyMessage(MSG_START_MONITOR);
				Checking = false;
				break;
			case MSG_START_MONITOR:
				if(mMonitorer != null || mMonitor == null){
					mHandler.sendEmptyMessage(MSG_CHECK_MONITOR);
					break;
				}
				mMonitorer = new Monitorer(mContext,mMonitor);
				mMonitorer.SetMonitorListener(AdMonitorServer.this);
				mMonitorer.StartMonitor();
				break;
			case MSG_FINISH_MONITOR:
				mMonitorList.remove(mMonitor);
				mMonitor   = null;
				mMonitorer = null;
				mHandler.sendEmptyMessage(MSG_CHECK_MONITOR);
				break;
			}
		} 
	};
	@Override
	public void MonitorerStateChange(MonitorerState state, Monitor m) {
		// TODO Auto-generated method stub
		if(state == MonitorerState.FINISH){
			mHandler.sendEmptyMessage(MSG_FINISH_MONITOR);
		}
	}
	@Override
	public void MonitorStateChange(MonitorerState state, Monitor m,
			TRACKINGURL url) {
		// TODO Auto-generated method stub
		
	}
	 
}
