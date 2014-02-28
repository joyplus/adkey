package com.joyplus.admonitor;

import com.common.internet.AjaxCallBack;
import com.common.internet.FastHttp;
import com.common.internet.ResponseEntity;
import com.joyplus.admonitor.Application.AdMonitorSDKFeature;
import com.joyplus.admonitor.Config.Log;
import com.joyplus.admonitor.data.ImpressionType;
import com.miaozhen.mzmonitor.MZMonitor;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class Monitorer {
	  private Context         mContext;
      private Monitor         mMonitor;
      private MonitorListener mMonitorListener = null;
      private MonitorerState  mMonitorerState;
      private Object mObject  = new Object();
      public  void SetMonitorListener(MonitorListener l){
    	  synchronized(mObject){
    		  mMonitorListener = l;
    	  }
      }
      private void SetMonitorerState(MonitorerState state){
    		  mMonitorerState = state;
    		  NotifyStateChange();
      }
      private void NotifyStateChange(){
    	  if(mMonitorListener == null)return;
    	  new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					synchronized (mObject) {
						mMonitorListener.MonitorerStateChange(mMonitorerState, mMonitor);
					}
				}
  		 }.run();
      }
      public  enum MonitorerState{
    	  IDLE , START , NEW , MONITOR , OVER , FINISH
      }
      
	  public Monitorer(Context context,Monitor m){
		  mContext        = context;
		  mMonitor        = m;
		  mMonitorerState = MonitorerState.IDLE;
		  Log.d("Monitorer = "+mMonitor.GetIMPRESSION().size());
	  }
	  public void StartMonitor() {
		  // TODO Auto-generated method stub
		  SetMonitorerState(MonitorerState.START);
		  mHandler.sendEmptyMessage(MSG_CHECK_MONITOR);
	  }
	  //monitor
	  private final static int  MSG_CHECK_MONITOR  = 1;
	  private final static int  MSG_START_MONITOR  = 2;
	  private final static int  MSG_FINISH_MONITOR = 3;
	  
	  private MonitorThread mMonitorThread = null;
	  private IMPRESSION    mIMPRESSION    = null;
	  
	  private Handler mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what){
				case MSG_CHECK_MONITOR:
					if(mMonitorThread != null)break;
					mIMPRESSION = mMonitor.GetFirstIMPRESSION();
					if(mIMPRESSION == null){
						mMonitor = null;
						SetMonitorerState(MonitorerState.FINISH);
						break;
					}
					SetMonitorerState(MonitorerState.NEW);
					mHandler.sendEmptyMessage(MSG_START_MONITOR);
					break;
				case MSG_START_MONITOR:
					SetMonitorerState(MonitorerState.START);
					mMonitorThread = new MonitorThread(mIMPRESSION);
					mMonitorThread.report();
					break;
				case MSG_FINISH_MONITOR:
					SetMonitorerState(MonitorerState.OVER);
					mMonitor.GetFirstIMPRESSION().mMonitored = true;
					mMonitor.RemoveFirstTRACKINGURL(mIMPRESSION);
					mMonitorThread = null;
					mIMPRESSION    = null;
					mHandler.sendEmptyMessage(MSG_CHECK_MONITOR);
					break;
				}
			}
	  };
	  
	  
	  //monitor start
	  private class MonitorThread{
		  private IMPRESSION MonitorURL = null;
		  private MonitorThread(IMPRESSION url){
			  MonitorURL = url;
		  }
		  private void Finish(){
			  Log.d("Monitorer finish = "+MonitorURL.toString());
			  mHandler.sendEmptyMessage(MSG_FINISH_MONITOR);
		  }
		  public void report() {
			  // TODO Auto-generated method stub
			  if(MonitorURL == null 
					  || MonitorURL.mImpressionURL == null
					  || "".equals(MonitorURL.mImpressionURL)){
				  Finish();
			  }
			  Log.d("Monitorer = "+MonitorURL.toString());
			  if(AdMonitorSDKFeature.MIAOZHEN && ImpressionType.miaozhen==MonitorURL.mImpressionType){
				  if(mContext != null){
					  MZMonitor.retryCachedRequests(mContext);
					  MZMonitor.adTrack(mContext, MonitorURL.mImpressionURL);
				  }
				  Finish();
			  }else if(ImpressionType.Joyplus==MonitorURL.mImpressionType){
				  report_third(MonitorURL.mImpressionURL);
			  }else{
				  Finish();
			  }
		  }
		  
		private void report_third(String url){
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

	  }
	  
}
