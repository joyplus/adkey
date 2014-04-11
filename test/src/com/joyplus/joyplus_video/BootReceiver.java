package com.joyplus.joyplus_video;

import com.joyplus.request.ADRequest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		ADRequest r = ADRequest.GetInstance();
		if(r!=null){
			r.request();
		}else{
			Log.d("Jas","BootReceiver ADRequest is null ");
		}
	}

}
