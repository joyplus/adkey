package com.joyplus.adkey.downloads;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import com.joyplus.adkey.widget.Log;
import android.content.Context;
import android.text.TextUtils;

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
			 if(BASEPATH == null ){
				 BASEPATH = MKDatadir(context);
			 }
		 }
		 if(BASEPATH == null) 
		       USEABLE  = false;
		 else USEABLE = true;
		 if(USEABLE)BASEPATH.mkdirs();
		 
	 }

	 private File MkSDCarddir(Context context){
		 if(!FileUtils.SDExist())return null;
		 File sdkpath = new File(context.getExternalFilesDir(null),".adsdk"+File.separator);
		 boolean result  = Mkdir(sdkpath);
		 if(result){//we should del ad resource which save in data/data/XXX		
			 File data = new File(context.getFilesDir(),".adsdk");
			 FileUtils.deleteFiles(data.getAbsolutePath());
		     return sdkpath;
		 }
		 return null;
	 }
	  
	 private File MKDatadir(Context context){
		// File  datapath = new File(context.getFilesDir(),AdConfig.GetBasePathName()+File.separator);
		 File  datapath = context.getFilesDir();
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
	 public void SetBasePath(String path){
	    if(!(path == null || "".equals(path))){
	    	File m = new File(path);
		    if(Mkdir(m)){
		    	BASEPATH = m;
		    	USEABLE  = true;
		    	Log.d("SetBasePath success");
		    	return;
		    }
	    }
	    Log.d("SetBasePath fail !!!");
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
	 
	 public boolean writeSerializableData(String filename, Object o,String id){	
		    if(!USEABLE || TextUtils.isEmpty(id))return false;
		    //Log.d("Server writeSerializableData() name="+filename);
		    synchronized(mObject){
				try{
					File dir  = new File(GetBasePath(),id.trim());
					dir.mkdirs();
					File data = new File(dir,filename);
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(data));
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
		
		public Object readSerializableData(String filename,String id){
			   if(!USEABLE || TextUtils.isEmpty(id))return null;
			   //Log.d("Server readSerializableData() name="+filename);
			   synchronized(mObject){
					try{
						File              data = new File(GetBasePath(),id.trim()+File.separator+filename);
						ObjectInputStream ois  = new ObjectInputStream(new FileInputStream(data));
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
		public Object readSerializableData(String filename){
			synchronized(mObject){
				try{
					File              data = new File(filename);
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
//		//add for report
//	    private static final String  ADKEY_PUBLISHERID_CONFIG_XML = "adboot_config_xml";
//	    public void ReSetNum(PublisherId id){
//	    	synchronized(mObject){
//	    		if(id == null || !id.CheckId())return;
//		    	SharedPreferences sp = mContext.getSharedPreferences(ADKEY_PUBLISHERID_CONFIG_XML,Context.MODE_PRIVATE);
//	    		Editor editor = sp.edit();
//	    		editor.putInt(id.GetPublisherId(), 0);//must be 1
//	    		editor.commit();
//	    	}
//	    }
//	    public void AddReportNum(PublisherId id){
//	    	synchronized(mObject){
//	    		if(id == null || !id.CheckId())return;
//		    	SharedPreferences sp = mContext.getSharedPreferences(ADKEY_PUBLISHERID_CONFIG_XML,Context.MODE_PRIVATE);
//	    		int num = sp.getInt(id.GetPublisherId(), 0);//min is 0
//	    		Editor editor = sp.edit();
//	    		editor.putInt(id.GetPublisherId(), ++num);//make is 1 first
//	    		editor.commit();
//	    	}
//	    }
//	    public int GetNum(PublisherId id){
//	    	synchronized(mObject){
//	    		if(id == null || !id.CheckId())return 0;
//		    	SharedPreferences sp = mContext.getSharedPreferences(ADKEY_PUBLISHERID_CONFIG_XML,Context.MODE_PRIVATE);
//	    		return sp.getInt(id.GetPublisherId(), 0);
//	    	}
//	    }
}
