package com.joyplus.ad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.webkit.URLUtil;
import com.joyplus.ad.Monitor.Escape;
import com.joyplus.ad.application.AdBoot;
import com.joyplus.ad.application.AdBootInfo;
import com.joyplus.ad.application.AdBootManager;
import com.joyplus.ad.config.Log;
import com.joyplus.ad.data.ADBOOT;
import com.joyplus.ad.data.AdHash;
import com.joyplus.ad.data.CODE;
import com.joyplus.ad.data.FileUtils;
import com.joyplus.ad.data.TRACKINGURL;
import com.joyplus.ad.db.AdBootImpressionInfo;
import com.joyplus.ad.db.AdBootTempDao;
import com.joyplus.ad.download.DownLoadListener;
import com.joyplus.ad.download.DownLoadManager;
import com.joyplus.ad.download.Download;

public class AdBootDownloadManager implements DownLoadListener{
     
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
		   //AdFileManager.getInstance().ReSetNum(id);
		   if(mCurrentADBOOT == null)return;
		   if(mCurrentADBOOT.code != null && CODE.AD_NO.equals(mCurrentADBOOT.code.VALUE)){
			   if(mAdBootInfo.CheckFirstImageUsable())
				   FileUtils.deleteFile(mAdBootInfo.GetFirstSource());
			   if(mAdBootInfo.CheckSecondImageUsable())
			       FileUtils.deleteFile(mAdBootInfo.GetSecondSource());
			   if(mAdBootInfo.CheckBootAnimationZipUsable())
			       FileUtils.deleteFile(mAdBootInfo.GetThirdSource());
			   if(mDownLoadListener != null){// for notify no ad
					mDownLoadListener.NoAD();
			   }
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
		   //add by Jas for report
		   if(mCurrentADBOOT != null && mCurrentADBOOT.video != null){
			   AdBootImpressionInfo Info = new AdBootImpressionInfo();
			   Info.publisher_id         = id.GetPublisherId();
			   if(mCurrentADBOOT.video.impressionurl!=null){
			       Info.mImpressionUrl   = GetURL(mCurrentADBOOT.video.impressionurl.URL);
		       }
			   if(mAdBootInfo != null){
				   Info.FirstSource  = mAdBootInfo.GetFirstSource();
				   Info.SecondSource = mAdBootInfo.GetSecondSource();
				   Info.ThirdSource  = mAdBootInfo.GetThirdSource();
			   }
			   if(mCurrentADBOOT.video.trackingurl != null){
				   for(TRACKINGURL url : mCurrentADBOOT.video.trackingurl){
					   if(TRACKINGURL.TYPE.MIAOZHEN == url.Type){
						   Info.miaozhen  = url.URL;
					   }else if(TRACKINGURL.TYPE.ADMASTER == url.Type){
						   Info.admaster  = url.URL;
					   }else if(TRACKINGURL.TYPE.IRESEARCH == url.Type){
						   Info.iresearch = GetURL(url.URL);
					   }else if(TRACKINGURL.TYPE.NIELSEN == url.Type){
						   Info.nielsen   = GetURL(url.URL);
					   }
				   }
			   }
			   Info.Count = 0;
			   AdBootTempDao.getInstance(mContext).InsertOneInfo(Info);
		   }
		   //end add 
		   if(mDownload != null && mDownload.size() <=0){
			   if(mDownLoadListener != null){
					mDownLoadListener.Finish(null);
			   }
		   }
	   }
	   private String GetURL(String url){
		  if(url==null || "".equals(url))return url;
		  String ua = PhoneManager.getInstance().GetUA1();
		  if(ua ==null || "".equals(ua))ua = PhoneManager.getInstance().GetUA2();
		  if(ua ==null || "".equals(ua)){
			  url=url.replaceAll("%UA%", "");
		  }else{
			  url=url.replaceAll("%UA%", Escape.escape(ua));
		  }
		  url=url.replaceAll("%TS%", ""+System.currentTimeMillis());
		  return url;
	   }
	   private void CheckDownLoadFirst(){
		   if(mAdBootInfo == null)return;
		   if(mAdBootInfo.CheckFirstImageUsable() ){
			   File first = new File(mLocalAdBootInfo.GetFirstSource());
			   if(IsFirstSame() && first.exists()){//URL same and localfile is exists.
				   if(IsFirstHashSame()){
					   Log.d("Hash check success "+mAdBootInfo.GetFirstSource()+" then going to copy file to Target file !!!");
					   File TargetFirst = new File(mAdBootInfo.GetFirstSource());
					   if(!AdConfig.GetCOPYALWAYS()){
						   long t = TargetFirst.length();
						   if(t>0 && t== first.length()){
							   FileUtils.Chmod(TargetFirst);
							   return;
						   }
					   }
					   if(FileUtils.copyFile(first, TargetFirst)){
						   FileUtils.Chmod(TargetFirst);
					   }
				   }else{
					   Log.d("Hash check fail  "+mAdBootInfo.GetFirstSource()+" then going to download file to Target file !!!");
					   DownloadFirst();
				   }
			   }else if((mCurrentADBOOT.video.creative != null)
						  && (URLUtil.isHttpsUrl(mCurrentADBOOT.video.creative.URL)||URLUtil.isHttpUrl(mCurrentADBOOT.video.creative.URL))){
				   if(first.exists())first.delete();//remove it first
				   DownloadFirst();
			   }else{//now server not get
				   if(first.exists())first.delete();//remove it first
				   FileUtils.deleteFile(mAdBootInfo.GetFirstSource());
			   }
		   }
	   }
	   private void CheckDownLoadSecond(){
           if(mAdBootInfo == null)return;
		   if(mAdBootInfo.CheckSecondImageUsable()){
			   File second = new File(mLocalAdBootInfo.GetSecondSource());
			   if(IsSecondSame() && second.exists()){
				   if(IsSecondHashSame()){
					   Log.d("Hash check success "+mAdBootInfo.GetSecondSource()+" then going to copy file to Target file !!!");
					   File TagetSecond = new File(mAdBootInfo.GetSecondSource());
					   if(!AdConfig.GetCOPYALWAYS()){
						   long t = TagetSecond.length();
						   if(t>0 && t== second.length()){
							   FileUtils.Chmod(TagetSecond);
							   return;
						   }
					   }
					   if(FileUtils.copyFile(second, TagetSecond)){
						   FileUtils.Chmod(TagetSecond);
					   }
				   }else{
					   Log.d("Hash check fail  "+mAdBootInfo.GetSecondSource()+" then going to download file to Target file !!!");
					   DownloadSecond();
				   }
			   }else if((mCurrentADBOOT.video.creative2 != null)
						  && (URLUtil.isHttpsUrl(mCurrentADBOOT.video.creative2.URL)||URLUtil.isHttpUrl(mCurrentADBOOT.video.creative2.URL))){
				   if(second.exists())second.delete();//remove it first
				   DownloadSecond();
			   }else{//now server not get
				   if(second.exists())second.delete();//remove it first
				   FileUtils.deleteFile(mAdBootInfo.GetSecondSource());
			   }
		   }
	   }
	   private void CheckDownLoadZIP(){
		   if(mAdBootInfo == null)return;
		   if(mAdBootInfo.CheckBootAnimationZipUsable()){
			   File zip = new File(mLocalAdBootInfo.GetThirdSource());
			   if(IsBootAnimationSame() && zip.exists()){
				   if(IsBootAnimationHashSame()){
					   Log.d("Hash check success "+mAdBootInfo.GetThirdSource()+" then going to copy file to Target file !!!");
					   File TargetThird = new File(mAdBootInfo.GetThirdSource());
					   if(!AdConfig.GetCOPYALWAYS()){
						   long t = TargetThird.length();
						   if(t>0 && t== zip.length()){
							   FileUtils.Chmod(TargetThird);
							   return;
						   }
					   }
					   if(FileUtils.copyFile(zip, TargetThird)){
						   FileUtils.Chmod(TargetThird);
					   }
				   }else{
					   Log.d("Hash check fail  "+mAdBootInfo.GetThirdSource()+" then going to download file to Target file !!!");
					   DownloadBootAnimation();
				   }
			   }else if((mCurrentADBOOT.video.creative3 != null)
						  && (URLUtil.isHttpsUrl(mCurrentADBOOT.video.creative3.URL)||URLUtil.isHttpUrl(mCurrentADBOOT.video.creative3.URL))){
				   if(zip.exists())zip.delete();//remove it first
				   DownloadBootAnimation();
			   }else{//now server not get
				   if(zip.exists())zip.delete();//remove it first
				   FileUtils.deleteFile(mAdBootInfo.GetThirdSource());
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
           mDownload = new ArrayList<Download>();
	   }
	   
	   private boolean IsBootAnimationSame(){
		   if(!(mLastADBOOT!=null && mCurrentADBOOT!=null))
			   return false;
		   if(!(mLastADBOOT.video!=null && mCurrentADBOOT.video!=null))
			   return false;
		   if(!(mLastADBOOT.video.creative3!=null && mCurrentADBOOT.video.creative3!=null))
			   return false; 
		   String mLastbootanimation = mLastADBOOT.video.creative3.URL;
		   String mbootanimation = mCurrentADBOOT.video.creative3.URL;
		   if(mLastbootanimation ==null || mbootanimation == null)return false;
		   if("".equals(mbootanimation)&&"".equals(mLastbootanimation))return false;
		   return (mLastbootanimation.equals(mbootanimation));
	   }
	   private boolean IsBootAnimationHashSame(){
		   if(mCurrentADBOOT==null||mCurrentADBOOT.video==null||mCurrentADBOOT.video.creative3==null)return false;
		   String filehash = AdHash.getFileHash(mLocalAdBootInfo.GetThirdSource());
		   if(filehash==null||"".equals(filehash.trim()))return false;//file no exsits
		   if(filehash.equals(mCurrentADBOOT.video.creative3.Hash))return true;
		   return false;
	   }
	   private void DownloadBootAnimation(){
		   Download zipdownload   = new Download();
		   zipdownload.URL        = mCurrentADBOOT.video.creative3.URL;
		   zipdownload.LocalFile  = mLocalAdBootInfo.GetThirdSource();
		   zipdownload.TargetFile = mAdBootInfo.GetThirdSource();
		   zipdownload.SetDownLoadListener(this);
		   mDownload.add(zipdownload);
		   DownLoadManager.getInstance().AddDownload(zipdownload);
	   }
	   private boolean IsFirstSame(){
		   if(!(mLastADBOOT!=null && mCurrentADBOOT!=null))
			   return false;
		   if(!(mLastADBOOT.video!=null && mCurrentADBOOT.video!=null))
			   return false;
		   if(!(mLastADBOOT.video.creative!=null && mCurrentADBOOT.video.creative!=null))
			   return false;
		   String mLastbootanimation = mLastADBOOT.video.creative.URL;
		   String mbootanimation = mCurrentADBOOT.video.creative.URL;
		   if(mLastbootanimation ==null || mbootanimation == null)return false;
		   if("".equals(mbootanimation)&&"".equals(mLastbootanimation))return false;
		   return (mLastbootanimation.equals(mbootanimation));
	   }
	   private boolean IsFirstHashSame(){
		   if(mCurrentADBOOT==null||mCurrentADBOOT.video==null||mCurrentADBOOT.video.creative==null)return false;
		   String filehash = AdHash.getFileHash(mLocalAdBootInfo.GetFirstSource());
		   if(filehash==null||"".equals(filehash.trim()))return false;//file no exsits
		   if(filehash.equals(mCurrentADBOOT.video.creative.Hash))return true;
		   return false;
	   }
	   private void DownloadFirst(){
		   Download firstdownload   = new Download();
		   firstdownload.URL        = mCurrentADBOOT.video.creative.URL;
		   firstdownload.LocalFile  = mLocalAdBootInfo.GetFirstSource();
		   firstdownload.TargetFile = mAdBootInfo.GetFirstSource();
		   firstdownload.SetDownLoadListener(this);
		   mDownload.add(firstdownload);
		   DownLoadManager.getInstance().AddDownload(firstdownload);
	   }
	   private boolean IsSecondSame(){
		   if(!(mLastADBOOT!=null && mCurrentADBOOT!=null))
			   return false;
		   if(!(mLastADBOOT.video!=null && mCurrentADBOOT.video!=null))
			   return false;
		   if(!(mLastADBOOT.video.creative2!=null && mCurrentADBOOT.video.creative2!=null))
			   return false;
		   String mLastbootanimation = mLastADBOOT.video.creative2.URL;
		   String mbootanimation = mCurrentADBOOT.video.creative2.URL;
		   if(mLastbootanimation ==null || mbootanimation == null)return false;
		   if("".equals(mbootanimation)&&"".equals(mLastbootanimation))return false;
		   return (mLastbootanimation.equals(mbootanimation));
	   }
	   private boolean IsSecondHashSame(){
		   if(mCurrentADBOOT==null||mCurrentADBOOT.video==null||mCurrentADBOOT.video.creative2==null)return false;
		   String filehash = AdHash.getFileHash(mAdBootInfo.GetSecondSource());
		   if(filehash==null||"".equals(filehash.trim()))return false;//file no exsits
		   if(filehash.equals(mCurrentADBOOT.video.creative2.Hash))return true;
		   return false;
	   }
	   private void DownloadSecond(){
		   Download seconddownload   = new Download();
	       seconddownload.URL        = mCurrentADBOOT.video.creative2.URL;
	       seconddownload.LocalFile  = mLocalAdBootInfo.GetSecondSource();
	       seconddownload.TargetFile = mAdBootInfo.GetSecondSource();
	       seconddownload.SetDownLoadListener(this);
	       mDownload.add(seconddownload);
		   DownLoadManager.getInstance().AddDownload(seconddownload);
	   }
	    // Interface Report count
//		private void ReportCount() {
//			new Thread(new ReportCountRunnable()).start();
//		}
//		private class ReportCountRunnable implements Runnable {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				new ImpressionThread(mContext, mCurrentADBOOT.video.impressionurl.URL,
//						mPublisherId.GetPublisherId(), AdManager.AD.ADBOOT).start();
//			}
//		}
	   
		//for Listener
		private List<Download> mDownload;

		@Override
		public void Start(Download download) {
			// TODO Auto-generated method stub
			if(mDownload != null && mDownload.size()>0){
				if(mDownload.contains(download) && mDownLoadListener != null){
					mDownLoadListener.Start(download);
				}
			}
		}

		@Override
		public void Finish(Download download) {
			// TODO Auto-generated method stub
			if(mDownload != null && mDownload.size()>0){
				mDownload.remove(download);
				if(mDownload.size()<=0 && mDownLoadListener != null){
					mDownLoadListener.Finish(download);
				}
			}
		}
     
		private DownLoadListener mDownLoadListener;
		public void SetDownLoadListener(DownLoadListener Listener){
			   mDownLoadListener = Listener;
		}

		@Override
		public void NoAD() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void DownLoading(String targetFile, long complete, long totle) {
			// TODO Auto-generated method stub
			if(mDownLoadListener != null){
				mDownLoadListener.DownLoading(targetFile, complete, totle);
			}
		}
}
