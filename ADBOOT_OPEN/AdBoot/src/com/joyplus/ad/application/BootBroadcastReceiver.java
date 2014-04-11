package com.joyplus.ad.application;

import com.joyplus.ad.AdBootServer;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class BootBroadcastReceiver extends BroadcastReceiver {

	private Context mContext;
    
	@Override
	public void onReceive(Context arg0, Intent intent) {
		// TODO Auto-generated method stub
		mContext      = arg0;
		String action = intent.getAction();
		if("android.intent.action.BOOT_COMPLETED".equals(action)){
			mContext.registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
		}else if("android.net.conn.CONNECTIVITY_CHANGE".equals(action)){
			if (!intent.getBooleanExtra(
					ConnectivityManager.EXTRA_OTHER_NETWORK_INFO, false)) {
				try {
					Thread.sleep(1000 * 10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					if(isNetworkAvailable(mContext)){
						mContext.unregisterReceiver(this);
						Intent mIntent = new Intent(mContext, AdBootServer.class);
						mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
								| Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						mContext.startService(mIntent);
					}
				}
			}
		}
	}
	
	public boolean isNetworkAvailable(Context ctx) {
		int networkStatePermission = ctx
				.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);
		if (networkStatePermission == PackageManager.PERMISSION_GRANTED) {
			ConnectivityManager mConnectivity = (ConnectivityManager) ctx
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = mConnectivity.getActiveNetworkInfo();
			if (info == null) {return false;}
			int netType = info.getType();
			if ((netType == ConnectivityManager.TYPE_WIFI)
					|| (netType == ConnectivityManager.TYPE_MOBILE)) {
				return info.isConnected();
			} else {
				return false;
			}
		} else {
			return true;
		}
	}


}
