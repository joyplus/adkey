package com.joyplus.adboot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class NetworkReceiver extends BroadcastReceiver {

	private boolean Debug = true;
	// private String TAG = "NetworkReceiver";
	private String TAG = "Jas";
	private Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction()
				.endsWith(ConnectivityManager.CONNECTIVITY_ACTION)) {
			if (!intent.getBooleanExtra(
					ConnectivityManager.EXTRA_OTHER_NETWORK_INFO, false)) {
				if (Debug)
					Log.d(TAG, "NetworkReceiver network is connectivity");
				mContext = context;
				try {
					Thread.sleep(1000 * 10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					CheckNetWork();
				}
			}
		}
	}

	private void CheckNetWork() {
		// TODO Auto-generated method stub
		if (AdBoot.getInstance().CheckNetWork()) {
			if (Debug)
				Log.d("Jas", "CheckNetWork() NetWork is Available");
			AdBoot.getInstance().UnRegisterNetWorkReceiver();
			Intent mIntent = new Intent(AdBoot.getInstance()
					.getApplicationContext(), AdBootScreenServer.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
					| Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mContext.startService(mIntent);
		}
	}

}
