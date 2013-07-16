package com.example.adboot;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

/*Add by Jas@20130712
 * It's a demo for use using SDK */
public class AdBoot extends Application{
   
	  private boolean Debug = true;
	  private String  TAG   = "AdBoot";
	  
	  private NetworkReceiver mNetworkReceiver;
	  private static AdBoot mAdBoot = null;
	  public  static AdBoot getInstance(){
		  return mAdBoot;
	  }
	  @Override
	  public void onCreate() {
	    	// TODO Auto-generated method stub
		    if(Debug)Log.d(TAG,"AdBoot onCreate()");
	    	super.onCreate();
			mAdBoot = this;
	  }
	
	  public void RegisterNetWorkReceiver(){
		   if(Debug)Log.d(TAG,"RegisterNetWorkReceiver()");
		   if(mNetworkReceiver == null){
			   mNetworkReceiver = new NetworkReceiver();
		   }
		   this.getApplicationContext().registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
      }
	  
	  public void UnRegisterNetWorkReceiver(){
	       if(Debug)Log.d("TAG","UnRegisterNetWorkReceiver()");
	       if(mNetworkReceiver == null){
	    		return;
	       }
	       this.getApplicationContext().unregisterReceiver(mNetworkReceiver);
	  }
	
}
