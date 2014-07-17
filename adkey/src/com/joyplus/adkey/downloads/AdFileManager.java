package com.joyplus.adkey.downloads;

import java.io.File;
import android.content.Context;

public class AdFileManager {
       
	private Context mContext;
    private AdFileServer mAdBootFileServer;
    
	private static AdFileManager mAdBootFileManager;
	public  static void Init(Context context){
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
    public String toString(){
    	StringBuffer ap = new StringBuffer();
    	ap.append("AdFileManager{")
    	  .append(",UseAble="+UseAble())
    	  .append(",GetBasePath="+GetBasePath())
    	  .append("}");
    	return ap.toString();
    }
    public void SetBasePath(String path){
    	mAdBootFileServer.SetBasePath(path);
    }
    public synchronized boolean writeSerializableData(String fileName, Object o,String id){
    	//Log.d("writeSerializableData() name="+fileName+" UseAble="+UseAble()+" GetBasePath()="+GetBasePath());
    	return mAdBootFileServer.writeSerializableData(fileName, o ,id);
    }
    
    public synchronized Object readSerializableData(String fileName,String id){
    	//Log.d("readSerializableData() name="+fileName +" UseAble="+UseAble()+" GetBasePath()="+GetBasePath());
    	return mAdBootFileServer.readSerializableData(fileName,id);
    }
    
    public synchronized Object readSerializableData(String fileName){
    	if(!(UseAble()||GetBasePath()==null))return null;//no sapce to read file .    	
    	if(fileName==null || "".equals(fileName))return null;//nothing to be return
    	return mAdBootFileServer.readSerializableData(fileName);
    }
//    //for report count.
//    public synchronized void ReSetNum(PublisherId id){
//    	mAdBootFileServer.ReSetNum(id);
//    }
//    public synchronized void AddReportNum(PublisherId id){
//    	mAdBootFileServer.AddReportNum(id);
//    }
//    public synchronized int GetNum(PublisherId id){
//    	return mAdBootFileServer.GetNum(id);
//    }
    
//    private Handler mHandler = new Handler(){};
//    private void DelUnUseFile(){
//	    if(!AdSDKManager.IsInited())return;
//	    LocationDao mLocationDao = new LocationDao(mContext);
//		if(mLocationDao == null)return;
//    	ArrayList<LocationInfo> mInfos = mLocationDao.GetAlllocationInfo();
//    	File[] m = AdFileManager.getInstance().GetBasePath().listFiles();
//    	if(m != null && m.length>0){
//    		for(File location : m){
//    			if(location.exists() && location.isDirectory()){
//    				if(!IsAvailable(mInfos,location)){//remove unuse res dir.
//    					FileUtils.deleteFiles(location.toString());
//    				}
//    			}
//    		}
//    	}
//	  }
//		
//      private boolean IsAvailable(ArrayList<LocationInfo> info,File file){
//    	if(info == null || info.size() <= 0 || file == null)return false;
//    	for(LocationInfo in : info){
//    		if(!in.IsAvailable())continue;
//    		File location = new File(AdFileManager.getInstance().GetBasePath(),in.PublisherId.GetPublisherId());
//    		if(file.toString().equals(location.toString())){
//    			return true;
//    		}
//    	}
//    	return false;
//     }
}
