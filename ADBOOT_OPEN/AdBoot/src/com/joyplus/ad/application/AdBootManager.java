package com.joyplus.ad.application;


import java.io.File;

import com.joyplus.ad.AdBootDownloadManager;
import com.joyplus.ad.AdFileManager;
import com.joyplus.ad.AdListener;
import com.joyplus.ad.AdMode;
import com.joyplus.ad.AdManager.AD;
import com.joyplus.ad.Monitor.AdMonitorManager;
import com.joyplus.ad.Monitor.Monitor;
import com.joyplus.ad.PublisherId;
import com.joyplus.ad.config.Log;
import com.joyplus.ad.data.ADBOOT;
import com.joyplus.ad.data.AdBootRequest;
import com.joyplus.ad.data.RequestException;
import com.joyplus.ad.download.ImpressionThread;
import com.miaozhen.mzmonitor.MZMonitor;

import android.content.Context;

public class AdBootManager extends AdMode{
     
	private Context       mContext;
	private AdBoot        mAdBoot;//
	private AdBootRequest mAdBootRequest;
	private AdBootDownloadManager mDownloadManager;
	private AdListener    mAdListener;
	
	public void SetAdListener(AdListener adlistener){
		mAdListener = adlistener;
	}
	
	private AdBootManager(){
		super(AD.ADBOOT);
	}//can't instance this by no param.
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
	}
	
	
	@Override
	public void RequestAD() {
		// TODO Auto-generated method stub
		if(mAdBootRequest == null){
			new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					mAdBootRequest = new AdBootRequest(AdBootManager.this,mAdBoot);
					ADBOOT mADBOOT = null;
					try {
						mADBOOT = mAdBootRequest.sendRequest();
					} catch (RequestException e) {
						// TODO Auto-generated catch block
						mADBOOT = null;
						e.printStackTrace();
					} 
					if(mDownloadManager != null && mADBOOT != null){ 
						if(mADBOOT.video != null && mADBOOT.video.impressionurl!=null){
							new ImpressionThread(mContext,mADBOOT.video.impressionurl.URL,
									mPublisherId.GetPublisherId(),AD.ADBOOT).start();
						}
			            if(mADBOOT.video != null && mADBOOT.video.trackingurl != null){
			            	Monitor m = new Monitor();
			            	if(mAdBoot != null && mAdBoot.GetCUSTOMINFO() != null){
			            		if(!("".equals(mAdBoot.GetCUSTOMINFO().GetDEVICEMUMBER()))){//ds
			            			m.SetPM(mAdBoot.GetCUSTOMINFO().GetDEVICEMUMBER());
			            		}else if(!("".equals(mAdBoot.GetCUSTOMINFO().GetDEVICEMOVEMENT()))){//dm
			            			m.SetPM(mAdBoot.GetCUSTOMINFO().GetDEVICEMOVEMENT());
			            		}
			            		if(!("".equals(mAdBoot.GetCUSTOMINFO().GetMAC()))){//mac
			            			m.SetMAC(mAdBoot.GetCUSTOMINFO().GetMAC());
			            		}
			            	}
			            	m.SetTRACKINGURL(mADBOOT.video.trackingurl);
			            	AdMonitorManager.getInstance().AddMonitor(m);//start monitor
			            }
						mDownloadManager.UpdateADBOOT(mADBOOT, mAdBootRequest.GetFileName(), mPublisherId);
					}
					mAdBootRequest = null;
			}}.start();			
		}
	}
	
	public AdBoot GetAdBoot(){
		return mAdBoot;
	}
	
}
