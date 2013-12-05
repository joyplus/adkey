package com.joyplus.ad;

import java.io.File;

import android.content.Context;
import android.webkit.URLUtil;

import com.joyplus.ad.application.AdBoot;
import com.joyplus.ad.application.AdBootInfo;
import com.joyplus.ad.application.AdBootManager;
import com.joyplus.ad.config.Log;
import com.joyplus.ad.data.ADBOOT;
import com.joyplus.ad.data.FileUtils;
import com.joyplus.ad.download.DownLoadManager;
import com.joyplus.ad.download.Download;
import com.joyplus.ad.download.ImpressionThread;

public class AdBootDownloadManager {
     
	   private AdBoot     mAdBoot;  
	   private Context    mContext;
	   
	   private AdBootInfo mAdBootInfo;     //file use want to save 
	   private AdBootInfo mLocalAdBootInfo;//file save in local
	   
	   private ADBOOT     mLastADBOOT;     //last adbootresponse
	   private ADBOOT     mCurrentADBOOT;  //current adbootresponse
	   
	   private PublisherId mPublisherId;
	   
	   public AdBootDownloadManager(Context context,AdBootManager adbootmanager,AdBoot info ){
		   if(context==null || adbootmanager == null)throw new IllegalArgumentException("AdBootDownloadManager only be instance by AdBootManager!!!");
		   mContext       =  context;
		   mAdBoot        =  info;
		   mAdBootInfo    =  mAdBoot.GetAdBootInfo();
		   mPublisherId   =  new PublisherId(mAdBoot.GetPublisherId().GetPublisherId());
		   InitResource();
	   }
       
	   public void UpdateADBOOT(ADBOOT adboot,String name){
		   Log.d("AdBootDownloadManager UpdateADBOOT("+(adboot!=null)+" ,"+name+")" +" "+(mAdBootInfo!=null));
		   if(name == null || "".equals(name))return;
		   if(mAdBootInfo == null  
			   || (!(mAdBootInfo.CheckFirstImageUsable() || mAdBootInfo.CheckSecondImageUsable() || mAdBootInfo.CheckBootAnimationZipUsable())))
			   return;
		   mLastADBOOT    = (ADBOOT) AdFileManager.getInstance().readSerializableData(name);
		   mCurrentADBOOT = adboot;
		   AdFileManager.getInstance().writeSerializableData(name, mCurrentADBOOT);
		   if(mCurrentADBOOT == null || mCurrentADBOOT.video == null)return;
		   
		   if(mCurrentADBOOT != null)Log.d("mCurrentADBOOT="+mCurrentADBOOT.toString());
			else Log.d("mCurrentADBOOT == null");
		   if(mLastADBOOT != null)Log.d("mLastADBOOT="+mLastADBOOT.toString());
			else Log.d("mLastADBOOT == null");
		    
		   CheckDownLoadFirst();
		   CheckDownLoadSecond();
		   CheckDownLoadZIP();		   
	   }
	   private void CheckDownLoadFirst(){
		   if(mAdBootInfo == null)return;
		   if(mAdBootInfo.CheckFirstImageUsable() ){
			   File first = new File(AdFileManager.getInstance().GetBasePath(),mLocalAdBootInfo.GetFirstImage());
			   if(IsFirstSame() && first.exists()){
				   FileUtils.copyFile(first, new File(mAdBootInfo.GetFirstImage()));
			   }else if((mCurrentADBOOT.video.creative != null)
						  && (URLUtil.isHttpsUrl(mCurrentADBOOT.video.creative.URL)||URLUtil.isHttpUrl(mCurrentADBOOT.video.creative.URL))){
				   Download firstdownload   = new Download();
				   firstdownload.URL        = mCurrentADBOOT.video.creative.URL;
				   firstdownload.LocalFile  = mLocalAdBootInfo.GetFirstImage();
				   firstdownload.TargetFile = mAdBootInfo.GetFirstImage();
				   DownLoadManager.getInstance().AddDownload(firstdownload);
			   }
		   }
	   }
	   private void CheckDownLoadSecond(){
           if(mAdBootInfo == null)return;
		   if(mAdBootInfo.CheckSecondImageUsable()){
			   File second = new File(AdFileManager.getInstance().GetBasePath(),mLocalAdBootInfo.GetSecondImage());
			   if(IsSecondSame() && second.exists()){
				   FileUtils.copyFile(second, new File(mAdBootInfo.GetSecondImage()));
			   }else if((mCurrentADBOOT.video.creative2 != null)
						  && (URLUtil.isHttpsUrl(mCurrentADBOOT.video.creative2.URL)||URLUtil.isHttpUrl(mCurrentADBOOT.video.creative2.URL))){
				   Download seconddownload   = new Download();
			       seconddownload.URL        = mCurrentADBOOT.video.creative2.URL;
			       seconddownload.LocalFile  = mLocalAdBootInfo.GetSecondImage();
			       seconddownload.TargetFile = mAdBootInfo.GetSecondImage();
				   DownLoadManager.getInstance().AddDownload(seconddownload);
			   }
		   }
	   }
	   private void CheckDownLoadZIP(){
		   if(mAdBootInfo == null)return;
		   if(mAdBootInfo.CheckBootAnimationZipUsable()){
			   File zip = new File(AdFileManager.getInstance().GetBasePath(),mLocalAdBootInfo.GetBootAnimationZip());
			   if(IsBootAnimationSame() && zip.exists()){
				   FileUtils.copyFile(zip, new File(mAdBootInfo.GetBootAnimationZip()));
			   }else if((mCurrentADBOOT.video.creative3 != null)
						  && (URLUtil.isHttpsUrl(mCurrentADBOOT.video.creative3.URL)||URLUtil.isHttpUrl(mCurrentADBOOT.video.creative3.URL))){
				   Download zipdownload   = new Download();
				   zipdownload.URL        = mCurrentADBOOT.video.creative3.URL;
				   zipdownload.LocalFile  = mLocalAdBootInfo.GetBootAnimationZip();
				   zipdownload.TargetFile = mAdBootInfo.GetBootAnimationZip();
				   DownLoadManager.getInstance().AddDownload(zipdownload);
			   }
		   }
	   }
	   private void InitResource() {
		   // TODO Auto-generated method stub
		   mLocalAdBootInfo = new AdBootInfo();
		   mLocalAdBootInfo.SetBootAnimationZip(AdFileManager.getInstance().GetBasePath().toString()+"AdBootManager_bootanimation");
		   mLocalAdBootInfo.SetFirstImage(AdFileManager.getInstance().GetBasePath().toString()+"AdBootManager_first");
		   mLocalAdBootInfo.SetSecondImage(AdFileManager.getInstance().GetBasePath().toString()+"AdBootManager_seconde");
	   }
	   
	   private boolean IsBootAnimationSame(){
		   if(!(mLastADBOOT!=null && mCurrentADBOOT!=null))
			   return false;
		   if(!(mLastADBOOT.video!=null && mCurrentADBOOT.video!=null))
			   return false;
		   if(!(mLastADBOOT.video.creative3!=null && mCurrentADBOOT.video.creative3!=null))
			   return false;
		   String mLastbootanimation = mLastADBOOT.video.creative3.URL;
		   if(mLastbootanimation!=null)
			   mLastbootanimation = mLastbootanimation.substring(mLastbootanimation.lastIndexOf('/') + 1);
		   else mLastbootanimation = "";
		   String mbootanimation = mCurrentADBOOT.video.creative3.URL;
		   if(mbootanimation!=null)
			   mbootanimation = mbootanimation.substring(mbootanimation.lastIndexOf('/') + 1);
		   else mbootanimation = "";
		   return (mLastbootanimation.equals(mbootanimation));
	   }
	   
	   private boolean IsFirstSame(){
		   if(!(mLastADBOOT!=null && mCurrentADBOOT!=null))
			   return false;
		   if(!(mLastADBOOT.video!=null && mCurrentADBOOT.video!=null))
			   return false;
		   if(!(mLastADBOOT.video.creative!=null && mCurrentADBOOT.video.creative!=null))
			   return false;
		   String mLastbootanimation = mLastADBOOT.video.creative.URL;
		   if(mLastbootanimation!=null)
			   mLastbootanimation = mLastbootanimation.substring(mLastbootanimation.lastIndexOf('/') + 1);
		   else mLastbootanimation = "";
		   String mbootanimation = mCurrentADBOOT.video.creative.URL;
		   if(mbootanimation!=null)
			   mbootanimation = mbootanimation.substring(mbootanimation.lastIndexOf('/') + 1);
		   else mbootanimation = "";
		   return (mLastbootanimation.equals(mbootanimation));
	   }
	   
	   private boolean IsSecondSame(){
		   if(!(mLastADBOOT!=null && mCurrentADBOOT!=null))
			   return false;
		   if(!(mLastADBOOT.video!=null && mCurrentADBOOT.video!=null))
			   return false;
		   if(!(mLastADBOOT.video.creative2!=null && mCurrentADBOOT.video.creative2!=null))
			   return false;
		   String mLastbootanimation = mLastADBOOT.video.creative2.URL;
		   if(mLastbootanimation!=null)
			   mLastbootanimation = mLastbootanimation.substring(mLastbootanimation.lastIndexOf('/') + 1);
		   else mLastbootanimation = "";
		   String mbootanimation = mCurrentADBOOT.video.creative2.URL;
		   if(mbootanimation!=null)
			   mbootanimation = mbootanimation.substring(mbootanimation.lastIndexOf('/') + 1);
		   else mbootanimation = "";
		   return (mLastbootanimation.equals(mbootanimation));
	   }
	    // Interface Report count
		private void ReportCount() {
			new Thread(new ReportCountRunnable()).start();
		}
		private class ReportCountRunnable implements Runnable {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				new ImpressionThread(mContext, mCurrentADBOOT.video.impressionurl.URL,
						mPublisherId.GetPublisherId(), AdManager.AD.ADBOOT).start();
			}
		}
	   
	   
}
