package com.joyplus.ad.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import com.joyplus.ad.AdConfig;
import com.joyplus.ad.AdSDKFeature;
import com.joyplus.ad.config.Log;

import android.content.Context;

/*Define by Jas@20131125
 * this use to implementation Interface for AdBootFileManager*/
public class AdFileServer {
       
	 private  Context mContext;
	 private  Object  mObject    = new Object();
     private  boolean USEABLE    = false;//for this file server useable
	 private  File    BASEPATH ;
	 
	 public AdFileServer(Context context){
		 mContext = context;
		 InitResource(context);
	 }
	 
	 private void InitResource(Context context) { 
		 // TODO Auto-generated method stub
		 if(FileUtils.SDExist()){
			 BASEPATH = MkSDCarddir(context);
			 if(BASEPATH == null && AdSDKFeature.USE_EXTERNAL_SDCARD){
				 BASEPATH = MKDatadir(context);
			 }
		 }else if(AdSDKFeature.USE_EXTERNAL_SDCARD){
			 BASEPATH = MKDatadir(context);
		 }
		 if(BASEPATH == null) 
		       USEABLE  = false;
		 else USEABLE = true;
		 BASEPATH.mkdirs();
		 Log.d("USEABLE="+USEABLE+" BASEPATH="+BASEPATH.toString());
		 Log.d(" BASEPATH="+BASEPATH.toString()+" exist="+BASEPATH.exists()+" dir="+BASEPATH.isDirectory()
				 +" W="+BASEPATH.canWrite()+" R="+BASEPATH.canRead());
		 File sdkpath = context.getExternalFilesDir(null);
		 Log.d(" sdkpath="+sdkpath.toString()+" exist="+sdkpath.exists()+" dir="+sdkpath.isDirectory()
				 +" W="+sdkpath.canWrite()+" R="+sdkpath.canRead());
		 File sdkpaths = context.getFilesDir();
		 Log.d(" sdkpaths="+sdkpath.toString()+" exist="+sdkpath.exists()+" dir="+sdkpath.isDirectory()
				 +" W="+sdkpath.canWrite()+" R="+sdkpath.canRead());
	 }

	 private File MkSDCarddir(Context context){
		 if(!FileUtils.SDExist())return null;
		 File sdkpath = new File(context.getExternalFilesDir(null),AdConfig.GetBasePathName()+File.separator);
		 boolean result  = Mkdir(sdkpath);
		 if(result){//we should del ad resource which save in data/data/XXX		
			 File data = new File(context.getFilesDir(),AdConfig.GetBasePathName());
			 FileUtils.deleteFiles(data.getAbsolutePath());
		     return sdkpath;
		 }
		 return null;
	 }
	 
	 private File MKDatadir(Context context){
		 File  datapath = new File(context.getFilesDir(),AdConfig.GetBasePathName()+File.separator);
		 if( Mkdir(datapath))return datapath;
		 return null;
	 }
	 
	 private boolean Mkdir(File file){
		 if(file == null)return false;
		 if(!file.exists())file.mkdirs();
		 if(file.canRead() && file.canWrite())
			  return true;
		 return false;
	 }
	 
	 /*Interface for FileManager*/
	 public File GetBasePath(){ 
		 if(USEABLE&&!BASEPATH.exists()){
			 
		 }
		 return BASEPATH;
	 }
	 public boolean UseAble(){
	    	return USEABLE;
	 }
	 
	 public boolean writeSerializableData(String filename, Object o){	
		    if(!USEABLE)return false;
		    Log.d("Server writeSerializableData() name="+filename);
		    synchronized(mObject){
				try{
					File data = new File(GetBasePath(),filename);					
					FileOutputStream fop   = new FileOutputStream(data);
					ObjectOutputStream oos = new ObjectOutputStream(fop);
					oos.writeObject(o);
					oos.close();
					return true;
				} catch (FileNotFoundException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		}
		
		public Object readSerializableData(String filename){
			   if(!USEABLE)return null;
			   Log.d("Server readSerializableData() name="+filename);
			   synchronized(mObject){
					try{
						File              data = new File(GetBasePath(),filename);
						FileInputStream   fis  = new FileInputStream(data);
						ObjectInputStream ois  = new ObjectInputStream(fis);
						Object            file = (Object)ois.readObject();
						ois.close();
						return file;
					} catch (FileNotFoundException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (StreamCorruptedException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
			   }
		}
		
}
