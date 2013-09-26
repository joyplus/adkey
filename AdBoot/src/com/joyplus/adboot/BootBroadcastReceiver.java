package com.joyplus.adboot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {

	private boolean Debug = true;
	// private String TAG = "BootBroadcastReceiver";
	private String TAG = "Jas";

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		if (Debug)
			Log.d(TAG, "BootBroadcastReceiver");
		AdBoot.getInstance().RegisterNetWorkReceiver();
	}

}
