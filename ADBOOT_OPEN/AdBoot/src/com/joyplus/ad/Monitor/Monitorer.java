package com.joyplus.ad.Monitor;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import com.common.internet.AjaxCallBack;
import com.common.internet.FastHttp;
import com.common.internet.ResponseEntity;
import com.joyplus.ad.AdSDKFeature;
import com.joyplus.ad.HttpManager;
import com.joyplus.ad.PhoneManager;
import com.joyplus.ad.config.Log;
import com.joyplus.ad.data.TRACKINGURL;
import com.joyplus.ad.data.TRACKINGURL.TYPE;
import com.miaozhen.mzmonitor.MZMonitor;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class Monitorer {
	  private Context         mContext;
      private Monitor         mMonitor;
      private MonitorListener mMonitorListener = null;
      private MonitorerState  mMonitorerState;
      private Object mObject = new Object();
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
		  Log.d("Monitorer = "+mMonitor.GetTRACKINGURL().size());
	  }
	  public void StartMonitor() {
		  // TODO Auto-generated method stub
		  if(mMonitor == null || mMonitor.GetFirstTRACKINGURL() == null){
			  SetMonitorerState(MonitorerState.FINISH);
			  return;
		  }
		  SetMonitorerState(MonitorerState.START);
		  mHandler.sendEmptyMessage(MSG_CHECK_MONITOR);
	  }
	  //monitor
	  private final static int  MSG_CHECK_MONITOR  = 1;
	  private final static int  MSG_START_MONITOR  = 2;
	  private final static int  MSG_FINISH_MONITOR = 3;
	  
	  private MonitorThread mMonitorThread = null;
	  private TRACKINGURL   mTRACKINGURL   = null;
	  private Handler mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what){
				case MSG_CHECK_MONITOR:
					if(mMonitorThread != null)break;
					mTRACKINGURL = mMonitor.GetFirstTRACKINGURL();
					if(mTRACKINGURL == null){
						SetMonitorerState(MonitorerState.FINISH);
						break;
					}
					SetMonitorerState(MonitorerState.NEW);
					mHandler.sendEmptyMessage(MSG_START_MONITOR);
					break;
				case MSG_START_MONITOR:
					SetMonitorerState(MonitorerState.START);
					mMonitorThread = new MonitorThread(mTRACKINGURL,mMonitor.GetNUM());
					mMonitorThread.report();
					break;
				case MSG_FINISH_MONITOR:
					SetMonitorerState(MonitorerState.OVER);
					mMonitor.GetFirstTRACKINGURL().Monitored = true;
					mMonitor.RemoveFirstTRACKINGURL(mTRACKINGURL);
					mMonitorThread = null;
					mTRACKINGURL   = null;
					mHandler.sendEmptyMessage(MSG_CHECK_MONITOR);
					break;
				}
			}
	  };
	  
	  
	  //monitor start
	  private class MonitorThread{
		  private TRACKINGURL MonitorURL = null;
		  private int NUM  = 1;
		  private MonitorThread(TRACKINGURL url,int num){
			  MonitorURL = url;
			  NUM = num;
		  }
		  private void Finish(){
			  Log.d("Monitorer finish = "+MonitorURL.toString());
			  mHandler.sendEmptyMessage(MSG_FINISH_MONITOR);
		  }
		  private void Finish_OneTime(){
			  if(NUM<=0)Finish();
			    else Report_OneTime();
		  }
		  public void report() {
			  // TODO Auto-generated method stub
			  if(MonitorURL == null 
					  || MonitorURL.URL == null
					  || "".equals(MonitorURL.URL)
					  || NUM<=0){
				  Finish();
			  }
			  NUM=(NUM>5)?5:NUM;
			  Report_OneTime();
		  }
		  private void Report_OneTime(){
			  Log.d("Report_OneTime NUM="+NUM);
			  if((NUM--)<=0){Finish_OneTime();return;}
			  Log.d("NUM="+NUM+" Monitorer = "+MonitorURL.toString());
			  if(AdSDKFeature.MONITOR_MIAOZHEN && TYPE.MIAOZHEN==MonitorURL.Type){
				  if(mContext != null){
					  MZMonitor.retryCachedRequests(mContext);
					  MZMonitor.adTrack(mContext, MonitorURL.URL);
				  }
				  Finish_OneTime();
			  }else if((AdSDKFeature.MONITOR_IRESEARCH && TYPE.IRESEARCH==MonitorURL.Type)
					  ||(AdSDKFeature.MONITOR_ADMASTER && TYPE.ADMASTER==MonitorURL.Type)
					  ||((AdSDKFeature.MONITOR_NIELSEN && TYPE.NIELSEN==MonitorURL.Type))){
				  report_third(MonitorURL.URL);
			  }else{
				  Finish_OneTime();
			  }
		  }
		  private void report_post(String url){
			    Log.d("report=="+url);
			    DefaultHttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setSoTimeout(client.getParams(),
						HttpManager.SOCKET_TIMEOUT);
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						HttpManager.CONNECTION_TIMEOUT);
				HttpProtocolParams.setUserAgent(client.getParams(),
						PhoneManager.getInstance().GetUA1()); 
				HttpPost post = new HttpPost(url);
				HttpResponse response;
				try {
					response = client.execute(post);
					//int responseCode = response.getStatusLine().getStatusCode();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					Finish();
				}
		  }
		private void report_third(String url){
			FastHttp.ajaxGet(url, new AjaxCallBack() {
				@Override
				public void callBack(ResponseEntity arg0) {
					Finish_OneTime();
				}
				@Override
				public boolean stop() {
					// TODO Auto-generated method stub
					return false;
				}}
			);
		} 
		private void report_get(String url){
			    Log.d("report=="+url);
			    url=URLEncoder.encode(url);
			    DefaultHttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setSoTimeout(client.getParams(),
						HttpManager.SOCKET_TIMEOUT);
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						HttpManager.CONNECTION_TIMEOUT);
				HttpProtocolParams.setUserAgent(client.getParams(),
						PhoneManager.getInstance().GetUA1()); 
				try {
					HttpGet get = new HttpGet(url);
					HttpResponse response;
					response = client.execute(get);
					//int responseCode = response.getStatusLine().getStatusCode();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e){
					e.printStackTrace();
				} catch (IllegalStateException e){
					e.printStackTrace();
				}finally{
					Finish();
				}
		  }
	  }
	  
}
