package com.joyplus.ad;


import com.joyplus.ad.config.Log;
import com.joyplus.ad.download.ImpressionThread;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AdBootServer extends Service{
   
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("onStartCommand");
		new ImpressionThread(this.getApplicationContext()).start();
		return super.onStartCommand(intent, flags, startId);
	}

	
	
	
}
