package com.joyplus.ad;

import java.io.File;

import android.content.Context;
import android.webkit.URLUtil;

import com.joyplus.ad.application.AdBoot;
import com.joyplus.ad.application.AdBootInfo;
import com.joyplus.ad.application.AdBootManager;
import com.joyplus.ad.config.Log;
import com.joyplus.ad.data.ADBOOT;
import com.joyplus.ad.data.CODE;
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
       
	   public void UpdateADBOOT(ADBOOT adboot,String name,PublisherId id){
		   Log.d("AdBootDownloadManager UpdateADBOOT("+(adboot!=null)+" ,"+name+")" +" "+(mAdBootInfo!=null));
		   if(name == null || "".equals(name))return;
		   if(mAdBootInfo == null  
			   || (!(mAdBootInfo.CheckFirstImageUsable() || mAdBootInfo.CheckSecondImageUsable() || mAdBootInfo.CheckBootAnimationZipUsable())))
			   return;
		   mLastADBOOT    = (ADBOOT) AdFileManager.getInstance().readSerializableData(name,id);
		   mCurrentADBOOT = adboot;
		   AdFileManager.getInstance().writeSerializableData(name, mCurrentADBOOT,id);
		   if(mCurrentADBOOT == null)return;
		   if(mCurrentADBOOT.code != null && CODE.AD_NO.equals(mCurrentADBOOT.code)){
			   if(mAdBootInfo.CheckFirstImageUsable())
				   FileUtils.deleteFile(mAdBootInfo.GetFirstSource());
			   if(mAdBootInfo.CheckSecondImageUsable())
			       FileUtils.deleteFile(mAdBootInfo.GetSecondSource());
			   if(mAdBootInfo.CheckBootAnimationZipUsable())
			       FileUtils.deleteFile(mAdBootInfo.GetThirdSource());
			   return;
		   }
		   
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
			   File first = new File(AdFileManager.getInstance().GetBasePath(),mLocalAdBootInfo.GetFirstSource());
			   if(IsFirstSame() && first.exists()){
				   FileUtils.copyFile(first, new File(mAdBootInfo.GetFirstSource()));
			   }else if((mCurrentADBOOT.video.creative != null)
						  && (URLUtil.isHttpsUrl(mCurrentADBOOT.video.creative.URL)||URLUtil.isHttpUrl(mCurrentADBOOT.video.creative.URL))){
				   Download firstdownload   = new Download();
				   firstdownload.URL        = mCurrentADBOOT.video.creative.URL;
				   firstdownload.LocalFile  = mLocalAdBootInfo.GetFirstSource();
				   firstdownload.TargetFile = mAdBootInfo.GetFirstSource();
				   DownLoadManager.getInstance().AddDownload(firstdownload);
			   }
		   }
	   }
	   private void CheckDownLoadSecond(){
           if(mAdBootInfo == null)return;
		   if(mAdBootInfo.CheckSecondImageUsable()){
			   File second = new File(AdFileManager.getInstance().GetBasePath(),mLocalAdBootInfo.GetSecondSource());
			   if(IsSecondSame() && second.exists()){
				   FileUtils.copyFile(second, new File(mAdBootInfo.GetSecondSource()));
			   }else if((mCurrentADBOOT.video.creative2 != null)
						  && (URLUtil.isHttpsUrl(mCurrentADBOOT.video.creative2.URL)||URLUtil.isHttpUrl(mCurrentADBOOT.video.creative2.URL))){
				   Download seconddownload   = new Download();
			       seconddownload.URL        = mCurrentADBOOT.video.creative2.URL;
			       seconddownload.LocalFile  = mLocalAdBootInfo.GetSecondSource();
			       seconddownload.TargetFile = mAdBootInfo.GetSecondSource();
				   DownLoadManager.getInstance().AddDownload(seconddownload);
			   }
		   }
	   }
	   private void CheckDownLoadZIP(){
		   if(mAdBootInfo == null)return;
		   if(mAdBootInfo.CheckBootAnimationZipUsable()){
			   File zip = new File(AdFileManager.getInstance().GetBasePath(),mLocalAdBootInfo.GetThirdSource());
			   if(IsBootAnimationSame() && zip.exists()){
				   FileUtils.copyFile(zip, new File(mAdBootInfo.GetThirdSource()));
			   }else if((mCurrentADBOOT.video.creative3 != null)
						  && (URLUtil.isHttpsUrl(mCurrentADBOOT.video.creative3.URL)||URLUtil.isHttpUrl(mCurrentADBOOT.video.creative3.URL))){
				   Download zipdownload   = new Download();
				   zipdownload.URL        = mCurrentADBOOT.video.creative3.URL;
				   zipdownload.LocalFile  = mLocalAdBootInfo.GetThirdSource();
				   zipdownload.TargetFile = mAdBootInfo.GetThirdSource();
				   DownLoadManager.getInstance().AddDownload(zipdownload);
			   }
		   }
	   }
	   private void InitResource() {
		   // TODO Auto-generated method stub
		   mLocalAdBootInfo = new AdBootInfo();
		   (new File(AdFileManager.getInstance().GetBasePath().toString()+File.separator+mPublisherId.GetPublisherId().toString()+File.separator)).mkdirs();
		   mLocalAdBootInfo.SetThirdSource(AdFileManager.getInstance().GetBasePath().toString()+File.separator+mPublisherId.GetPublisherId().toString()+File.separator+"AdBootManager_bootanimation");
		   mLocalAdBootInfo.SetFirstSource(AdFileManager.getInstance().GetBasePath().toString()+File.separator+mPublisherId.GetPublisherId().toString()+File.separator+"AdBootManager_first");
		   mLocalAdBootInfo.SetSecondSource(AdFileManager.getInstance().GetBasePath().toString()+File.separator+mPublisherId.GetPublisherId().toString()+File.separator+"AdBootManager_second");
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
