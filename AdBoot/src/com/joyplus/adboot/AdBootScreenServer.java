package com.joyplus.adboot;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AdBootScreenServer extends Service {

	private boolean Debug = false;
	private String TAG = "Jas";
	
	private AdBootScreenManager mManager;
	
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
		if (Debug)Log.d(TAG, "onStartCommand");
		InitResource();
		return super.onStartCommand(intent, flags, startId);
	}

	private void InitResource() {
		// TODO Auto-generated method stub
		if(Debug)Log.d(TAG,"InitResource()");
		try {
			mManager = new AdBootScreenManager(AdBoot.getInstance()
					.getApplicationContext(), getPUBLISHERID(), true);
			mManager.setListener(new Listener());
			mManager.UpdateAdvert();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getPUBLISHERID() {
		// TODO Auto-generated method stub
		AdPublisherIdManager  mAdPublisherIdManager = new AdPublisherIdManager(this);
		String PUBLISHERID    = mAdPublisherIdManager.getPublisherId();
		String newPUBLISHERID = mAdPublisherIdManager.UpdatePublisherId(AdPublisherIdManager.UMENGPARAMS.BOOT_ADV);
		if(newPUBLISHERID != null && !newPUBLISHERID.equals("")){
			PUBLISHERID = newPUBLISHERID;
		}
		Log.i(TAG," PUBLISHERID="+PUBLISHERID+" newPUBLISHERID="+newPUBLISHERID);
		return PUBLISHERID;
	}

	private class Listener implements AdBootScreenListener {

		@Override
		public void Closed() {
			// TODO Auto-generated method stub
			if (Debug)
				Log.d(TAG, "Closed");
			AdBootScreenServer.this.stopSelf(); // Ad get fail,it will shut
												// down.
		}

		@Override
		public void adLoadSucceeded() {
			// TODO Auto-generated method stub
			if (Debug)
				Log.d(TAG, "adLoadSucceeded");
		}

		@Override
		public void noAdFound() {
			// TODO Auto-generated method stub
			if (Debug)
				Log.d(TAG, "noAdFound");
		}
	}
}
