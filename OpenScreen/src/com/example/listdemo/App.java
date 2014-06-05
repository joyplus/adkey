package com.example.listdemo;

import java.util.ArrayList;
import java.util.List;

import com.joyplus.adkey.AdKeySDKManager;
import com.joyplus.adkey.AdKeySDKManagerException;
import com.joyplus.konka.ADRequest;
import com.joyplus.konka.KonkaConfig;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class App extends Application {
    @Override
    public void onCreate() {
	    // TODO Auto-generated method stub
	    super.onCreate();
	    try {
			AdKeySDKManager.Init(getApplicationContext());
		} catch (AdKeySDKManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    KonkaConfig.Init(getApplicationContext());
	    Log.d("Jas","KonkaConfig-->"+KonkaConfig.ToString());
//	    mADRequest  = new ADRequest(getApplicationContext());
	    //start server
	    Intent intent = new Intent();
		intent.setClass(this, ScanServer.class);
		this.startService(intent);
    }
    
    private static List<String> mBlackList;
    static{
    	mBlackList = new ArrayList<String>();
    	mBlackList.add("com.konka.metrolauncher");
    	mBlackList.add("com.example.listdemo");
    }
    public  static boolean IsBlackList(String key){
    	return mBlackList.contains(key);
    }
}
