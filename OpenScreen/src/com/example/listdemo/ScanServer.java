package com.example.listdemo;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.joyplus.konka.ADRequest;
import wei.mark.standout.StandOutWindow;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class ScanServer extends Service{
	private App mApp;
	private ActivityManager mActivityManager;
	private String mTopActivityName = "";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d("Jas","ScanServer onCreate");
		mActivityManager = (ActivityManager) this.getSystemService("activity");
		mApp = (App) this.getApplication();
		mADRequest = new ADRequest(this);
		mADRequest.request();
		//BroadcastReceiver re = new re();
		this.registerReceiver(new re(), new IntentFilter("android.intent.action.MAIN"));
	}
	private ADRequest mADRequest= null;
    public synchronized void Request(){
    	mADRequest.request();
    }
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
    private synchronized void StartTimer(){
    	if(mTimer!=null){
			mTimer.cancel();
			mTimer = null;
		}
		mTimer = new Timer();
		mTimer.schedule(new ScranTack(), 0L, 500L);
    }
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		StartTimer();
		return super.onStartCommand(intent, flags, startId);
	}
	private Timer mTimer;
    private class ScranTack extends TimerTask{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			ComponentName name = mActivityManager.getRunningTasks(1).get(0).topActivity;
			String topPackage = name.getPackageName();
			boolean same = IsSame(topPackage);
			//Log.d("Jas","ScranTack -->topPackage="+topPackage+",mTopActivityName="+mTopActivityName+",show="+show);
			if(!same && !App.IsBlackList(topPackage)){
				//mHandler.sendEmptyMessage(0);
				List<Drawable> ad = ADRequest.getPicturesDrawble(0);
				if(ad!=null && ad.size()>0)Show();
				else mADRequest.request();
			}
			mTopActivityName = topPackage;
			//StartTimer();
		}
    }
    private boolean IsSame(String topPackage){
    	if(!(topPackage == null || "".equals(topPackage))){
			if(!(mTopActivityName == null||"".equals(mTopActivityName))){
				if(mTopActivityName.equals(topPackage)){
					return true;
				}
			}
			return false;
		}
    	return true;
    }
    
    private static boolean show = false;
    private void Show(){
    	if(SimpleWindow.SHOW)return;
    	SimpleWindow.SHOW = false;
    	StandOutWindow.closeAll(this, SimpleWindow.class);
    	StandOutWindow.show(this, SimpleWindow.class, StandOutWindow.DEFAULT_ID);
    	mADRequest.request();
    }
    private Timer ShowTimer;
    
    private Handler mHandler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		dialog();
    	};
    };
    protected void dialog() {
    AlertDialog.Builder builder = new Builder(ScanServer.this);
    builder.setMessage("server");
    builder.setTitle("server");
    builder.setNegativeButton("no", new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	});
    builder.create().show();
    }
    private class re extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.d("Jas","re-->"+intent.getAction());
		}
    }
}
