package com.example.adboot;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class NetworkReceiver extends BroadcastReceiver{
    
	private boolean Debug = true;
	private String  TAG   = "NetworkReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().endsWith(ConnectivityManager.CONNECTIVITY_ACTION)){
			if(!intent.getBooleanExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO, false)){
				if(Debug)Log.d(TAG,"NetworkReceiver network is connectivity");
				AdBoot.getInstance().UnRegisterNetWorkReceiver();
				Intent mIntent = new Intent(AdBoot.getInstance().getApplicationContext(),AdBootScreenServer.class); 
				mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
	    		         | Intent.FLAG_ACTIVITY_NEW_TASK
	    		         | Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            context.startService(mIntent); 	
			}
		}
	}
    
}
