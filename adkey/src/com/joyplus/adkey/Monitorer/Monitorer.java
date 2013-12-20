package com.joyplus.adkey.Monitorer;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

import com.joyplus.adkey.Const;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.Monitorer.TRACKINGURL.TYPE;
import com.joyplus.adkey.widget.Log;
import com.miaozhen.mzmonitor.MZMonitor;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class Monitorer {
	  private Context         mContext;
      private Monitor         mMonitor;
      private MonitorListener mMonitorListener = null;
      private MonitorerState  mMonitorerState;
      private Object mObject = new Object();
      public  MonitorerState  getMonitorerState(){
    	   return mMonitorerState;
      }
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
	  public Monitorer(Context context){
		  mContext        = context;
		  mMonitorerState = MonitorerState.IDLE;
	  }
	  public void StartMonitor(Monitor m) {
		  // TODO Auto-generated method stub
		  if(mMonitor!= null)return;
		  mMonitor = m;
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
					mMonitorThread = new MonitorThread(mTRACKINGURL);
					mMonitorThread.start();
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
	  private class MonitorThread extends Thread{
		  private TRACKINGURL MonitorURL = null;
		  private MonitorThread(TRACKINGURL url){
			  MonitorURL = url;
			  
		  }
		  private void Finish(){
			  Message msg = Message.obtain(mHandler, MSG_FINISH_MONITOR);
	          msg.sendToTarget();
			  //mHandler.sendEmptyMessage(MSG_FINISH_MONITOR);
		  }
		  @Override
		  public void run() {
			  // TODO Auto-generated method stub
			  super.run();
			  if(MonitorURL == null 
					  || MonitorURL.URL == null
					  || "".equals(MonitorURL.URL)){
				  Finish();
			  }
			  Log.d("Jas","Monitorer = "+MonitorURL.toString());
			  if(AdSDKFeature.MONITOR_MIAOZHEN && TYPE.MIAOZHEN==MonitorURL.Type){
				  if(mContext != null){
					  MZMonitor.retryCachedRequests(mContext);
					  MZMonitor.adTrack(mContext, MonitorURL.URL);
				  }
				  Finish();
			  }else if((AdSDKFeature.MONITOR_IRESEARCH && TYPE.IRESEARCH==MonitorURL.Type)
					  ||(AdSDKFeature.MONITOR_ADMASTER && TYPE.ADMASTER==MonitorURL.Type)
					  ||((AdSDKFeature.MONITOR_NIELSEN && TYPE.NIELSEN==MonitorURL.Type))){
				  report(MonitorURL.URL);
			  }else{
				  Finish();
			  }
		  }
		  
		  private void report(String url){
			    DefaultHttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setSoTimeout(client.getParams(),
						Const.SOCKET_TIMEOUT);
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						Const.CONNECTION_TIMEOUT);
				HttpProtocolParams.setUserAgent(client.getParams(),
						Util.buildUserAgent()); 
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
				} finally{
					Finish();
				}
		  }
	  }
	  
}
