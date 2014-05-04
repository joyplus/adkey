package com.joyplus.adkey;

import java.io.File;

import com.joyplus.adkey.Monitorer.AdMonitorManager;
import com.joyplus.adkey.downloads.AdFileManager;
import com.joyplus.adkey.downloads.DownLoadManager;
import com.joyplus.adkey.mon.AppReportManager;

import android.content.Context;

/*Add by Jas@20140421 for review adkey*/
public class AdKeySDKManager {
   
	private static boolean Inited = false;
	public  static boolean IsInited(){
		return Inited;
	}
	public static void Init(Context context) throws AdKeySDKManagerException{
		if(Inited)return;
		if(context == null)throw new AdKeySDKManagerException("context is null !!!! ");
		new AdKeySDKManager(context);
	}
	
	private Context mContext;
	
	private AdKeySDKManager(Context context){
		mContext = context;
		InitResource();
		Inited = true;
	}
	private void InitResource() {
		// TODO Auto-generated method stub
		AdFileManager.Init(mContext.getApplicationContext());
		AdDeviceManager.Init(mContext.getApplicationContext());
		AdMonitorManager.Init(mContext.getApplicationContext());
		AppReportManager.Init(mContext.getApplicationContext());
		AdDeviceManager.Init(mContext.getApplicationContext());
		DownLoadManager.Init();
	}
	
	
	//////////////////////////////////////////////////////////////////
	//Interface for application. All Interface should be write here,for
	//unified interface
	//////////////////////////////////////////////////////////////////
	
	//for user set CUSTOMINFO
	public static void SetCUSTOMINFO(CUSTOMINFO info){
		AdDeviceManager.getInstance().SetCUSTOMINFO(info);
	}
	public static CUSTOMINFO GetCUSTOMINFO(){
		return AdDeviceManager.getInstance().GetCUSTOMINFO();
	}
    //for user set Cache.
	public static void SetFileBasePath(String path){
		AdFileManager.getInstance().SetBasePath(path);
	}
	public static File GetFileBasePath(){
		return AdFileManager.getInstance().GetBasePath();
	}
	public static boolean IsFileManagerUseable(){
		return AdFileManager.getInstance().UseAble();
	}
	
}
