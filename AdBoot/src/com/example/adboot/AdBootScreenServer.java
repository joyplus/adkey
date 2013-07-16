package com.example.adboot;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AdBootScreenServer extends Service{
    
	private boolean Debug = true;
	private String  TAG   = "AdBootScreenServer";
	
	private AdBootScreenManager mManager;
    private String PUBLISHERID = "3156ccf24a9f8024c34ac0c60e78952f";
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
   
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(Debug)Log.d(TAG,"onStartCommand");
		InitResource();
		return super.onStartCommand(intent, flags, startId);
	}

	private void InitResource() {
		// TODO Auto-generated method stub
		try {
			mManager = new AdBootScreenManager(AdBoot.getInstance().getApplicationContext(),PUBLISHERID,true);
			mManager.setListener(new Listener());
			mManager.UpdateAdvert();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.stopSelf(); // don't support AdBootScreen,it will shut down.
		}
	}
	
    private class Listener implements AdBootScreenListener{

		@Override
		public void Closed() {
			// TODO Auto-generated method stub
			if(Debug)Log.d(TAG,"Closed");
			AdBootScreenServer.this.stopSelf(); //Ad get fail,it will shut down.
		}

		@Override
		public void adLoadSucceeded() {
			// TODO Auto-generated method stub
			if(Debug)Log.d(TAG,"adLoadSucceeded");
		}

		@Override
		public void noAdFound() {
			// TODO Auto-generated method stub
			if(Debug)Log.d(TAG,"noAdFound");
		}
    }
}
