package com.joyplus.ad;

import java.io.File;

import com.joyplus.ad.config.Log;
import com.joyplus.ad.data.AdFileServer;

import android.content.Context;

public class AdFileManager {
       
	private Context mContext;
    private AdFileServer mAdBootFileServer;
    
	private static AdFileManager mAdBootFileManager;
	public  static void Init(Context context) throws AdSDKManagerException{
		  if(AdSDKManager.IsInited())return;
		  if(context == null)throw new AdSDKManagerException("AdBootFileManager context is null !!!!!");
		  mAdBootFileManager = new AdFileManager(context);
	}
	public static AdFileManager getInstance(){
	      return mAdBootFileManager;
	}
    private AdFileManager(Context context){
  	      mContext = context;
  	      mAdBootFileServer = new AdFileServer(mContext);
    }
    
    /*Interface for Application*/
    public boolean UseAble(){
    	return mAdBootFileServer.UseAble();
    }
    
    public File GetBasePath(){
    	return mAdBootFileServer.GetBasePath();
    }
    
    public synchronized boolean writeSerializableData(String fileName, Object o){
    	Log.d("writeSerializableData() name="+fileName+" UseAble="+UseAble()+" GetBasePath()="+GetBasePath());
    	if(!(UseAble()||GetBasePath()==null))return false;//when no space to Save file we should return fail.     	
    	if(fileName==null  
    			|| "".equals(fileName))  
    		return false;//we should return when null to be read.
    	return mAdBootFileServer.writeSerializableData(fileName, o);
    }
    
    public synchronized Object readSerializableData(String fileName){
    	Log.d("readSerializableData() name="+fileName +" UseAble="+UseAble()+" GetBasePath()="+GetBasePath());
    	if(!(UseAble()||GetBasePath()==null))return null;//no sapce to read file .    	
    	if(fileName==null || "".equals(fileName))return null;//nothing to be return
    	return mAdBootFileServer.readSerializableData(fileName);
    }
}
