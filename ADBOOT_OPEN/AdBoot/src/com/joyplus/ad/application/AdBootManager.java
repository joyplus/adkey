package com.joyplus.ad.application;

import java.lang.Thread.UncaughtExceptionHandler;
import com.joyplus.ad.AdBootDownloadManager;
import com.joyplus.ad.AdConfig;
import com.joyplus.ad.AdMode;
import com.joyplus.ad.AdManager.AD;
import com.joyplus.ad.PublisherId;
import com.joyplus.ad.data.ADBOOT;
import com.joyplus.ad.data.AdBootRequest;
import com.joyplus.ad.data.RequestException;
import com.joyplus.ad.db.AdBootImpressionInfo;
import com.joyplus.ad.db.AdBootTempDao;
import com.joyplus.ad.db.AdBootThread;
import com.joyplus.ad.download.DownLoadListener;
import com.joyplus.ad.download.Download;
import android.content.Context;

public class AdBootManager extends AdMode{
     
	private Context       mContext;
	private AdBoot        mAdBoot;//
	private AdBootRequest mAdBootRequest;
	private Thread        mAdBootRequestThread;
	private AdBootDownloadManager mDownloadManager;
	private final static  int TIME = 10;
	
	private AdBootManager(){
		super(AD.ADBOOT);
	}
	//can't instance this by no param.
	public AdBootManager(Context context,AdBoot info){	
		super(AD.ADBOOT);
		if(info == null)   throw new IllegalArgumentException("AdBoot cannot be null or empty");
		if(context == null)throw new IllegalArgumentException("Context cannot be null or empty");
		mContext       = context;
		mAdBoot        = new AdBoot(info.GetCUSTOMINFO(),info.GetAdBootInfo(),info.GetPublisherId());
		mPublisherId   = new PublisherId(mAdBoot.GetPublisherId().GetPublisherId());
		if(mAdBoot.GetCUSTOMINFO()==null||mAdBoot.GetCUSTOMINFO().GetDEVICEMOVEMENT() == null || "".equals(mAdBoot.GetCUSTOMINFO().GetDEVICEMOVEMENT()))
			throw new IllegalArgumentException("dm cannot be null or empty");
		mDownloadManager = new AdBootDownloadManager(mContext,this,mAdBoot);
		mDownloadManager.SetDownLoadListener(new AdListsnerer());
	}
	
	
	@Override
	public void RequestAD() {
		// TODO Auto-generated method stub
		if(mAdBootRequestThread == null){
			mAdBootRequestThread = new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					Request();// for request ad.
					mAdBootRequestThread = null;
				}
			};
			mAdBootRequestThread.setUncaughtExceptionHandler(new UncaughtExceptionHandler(){
				@Override
				public void uncaughtException(Thread thread, Throwable ex) {
					// TODO Auto-generated method stub
					mAdBootRequest       = null;
					mAdBootRequestThread = null;
					if(mAdBootListener != null){//now we can know its fail.
						mAdBootListener.NoAn();
					}
				}}
			);
			mAdBootRequestThread.start();
		}
	}
	private void Request(){
		if(mAdBootRequest != null)return;
//		AddReportNUM();//add report count.
		if(!CheckRequestAble()){//can't request AD now.so we can return.
		    mAdBootRequest = null;
		    return;
		}
		mAdBootRequest = new AdBootRequest(AdBootManager.this,mAdBoot);
		ADBOOT mADBOOT = null;
		int Count = TIME;
		while((Count--)>0){
			try {
				Thread.sleep(500);
				mADBOOT = null;
				mADBOOT = mAdBootRequest.sendRequest();
				break;
			} catch (RequestException e) {
				// TODO Auto-generated catch block
				mADBOOT = null;
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				mADBOOT = null;
				e.printStackTrace();
			}
		}
		if(mDownloadManager != null && mADBOOT != null){ 
			//Report();//new we can sure network is OK ,so report first.
			AdBootTempDao.getInstance(mContext).Remove(mPublisherId.GetPublisherId());//remove it first.
			new AdBootThread(mContext, ((mAdBoot != null && mAdBoot.GetCUSTOMINFO() != null)?mAdBoot.GetCUSTOMINFO():null)).StartReport();
			mDownloadManager.UpdateADBOOT(mADBOOT, mAdBootRequest.GetFileName(), mPublisherId);
		}else{
			if(mAdBootListener != null){
				mAdBootListener.NoAn();
			}
		}
		mAdBootRequest = null;
	}
//	//judge custom local file. we can add report num,when it exist.
//	private void AddReportNUM() {
//		// TODO Auto-generated method stub
//		if(CustomAdFileExist()){
//			AdFileManager.getInstance().AddReportNum(mPublisherId);
//		}
//	}
//    private boolean CustomAdFileExist(){
//    	if(mAdBoot == null || mAdBoot.GetAdBootInfo() == null)return false;
//    	if(CustomAdFileExist(mAdBoot.GetAdBootInfo().GetFirstSource())
//    			|| CustomAdFileExist(mAdBoot.GetAdBootInfo().GetSecondSource())
//    			|| CustomAdFileExist(mAdBoot.GetAdBootInfo().GetThirdSource())){
//    		return true;
//    	}
//    	return false;
//    }
//    private boolean CustomAdFileExist(String file){
//    	if(file ==null || "".equals(file))return false;
//    	return (new File(file)).exists();
//    }
//	public AdBoot GetAdBoot(){
//		return mAdBoot;
//	}
	
//	private void Report(){
//		ADBOOT last = (ADBOOT) AdFileManager.getInstance()
//				.readSerializableData(mAdBootRequest.GetFileName(),mPublisherId);
//		ThirdReport(last);//third listener
//		if(last != null && last.video != null && last.video.impressionurl != null){
//			//report to joyplus
//			Report r = new Report();
//		    r.SetPublisherId(mPublisherId);
//		    r.SetIMPRESSIONURL(last.video.impressionurl);
//			AdReportManager.getInstance().AddReport(r);
//		}
//		AdFileManager.getInstance().ReSetNum(mPublisherId);//it reported.
//	}
//	private void ThirdReport(ADBOOT last){
//		if(last != null && last.video != null && last.video.trackingurl != null){
//			Monitor m = new Monitor();
//        	if(mAdBoot != null && mAdBoot.GetCUSTOMINFO() != null){
//        		if(!("".equals(mAdBoot.GetCUSTOMINFO().GetDEVICEMOVEMENT()))){//dm
//        			m.SetPM(mAdBoot.GetCUSTOMINFO().GetDEVICEMOVEMENT());
//        		}else if(!("".equals(mAdBoot.GetCUSTOMINFO().GetDEVICEMUMBER()))){//ds
//        			m.SetPM(mAdBoot.GetCUSTOMINFO().GetDEVICEMUMBER());
//        		}
//        		if(!("".equals(mAdBoot.GetCUSTOMINFO().GetMAC()))){//mac
//        			m.SetMAC(mAdBoot.GetCUSTOMINFO().GetMAC());
//        		}
//        	}
//        	m.SetTRACKINGURL(last.video.trackingurl);
//        	m.SetNUM(AdFileManager.getInstance().GetNum(mPublisherId));
//        	AdMonitorManager.getInstance().AddMonitor(m);
//        }
//	}
	private boolean CheckRequestAble(){
		if(AdConfig.GetREQUESTALWAYS())return true;
		AdBootImpressionInfo info = AdBootTempDao.getInstance(mContext).GetOne(mPublisherId.GetPublisherId());
		if(info!=null && info.Count <1){
			if(mAdBootListener != null){//last AD no show.so we return finish this request.
				mAdBootListener.Finish();
			}
			return false;
		}
		return true;
	}
	private AdBootListener mAdBootListener;
	public  void SetAdBootListener(AdBootListener listener){
		mAdBootListener = listener;
	}
	
	private class AdListsnerer implements DownLoadListener{
		@Override
		public void Start(Download download) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void Finish(Download download) {
			// TODO Auto-generated method stub
			if(mAdBootListener != null){
				mAdBootListener.Finish();
			}
		}
		@Override
		public void NoAD() {
			// TODO Auto-generated method stub
			if(mAdBootListener != null){
				mAdBootListener.NoAd();
			}
		}
		@Override
		public void DownLoading(String targetFile,long complete, long totle) {
			// TODO Auto-generated method stub
			if(mAdBootListener != null){
				mAdBootListener.DownLoadProgress(targetFile, complete, totle);
			}
		}
	}
	
}
