package com.joyplus.ad;

import java.io.File;
import java.util.ArrayList;

import com.joyplus.ad.config.Log;
import com.joyplus.ad.db.AdBootDao;
import com.joyplus.ad.db.AdBootImpressionInfo;
import com.joyplus.ad.db.AdBootTempDao;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AdBootServer extends Service{
   
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		StartCheckTemp();
		return super.onStartCommand(intent, flags, startId);
	}
	private synchronized void StartCheckTemp() {
		// TODO Auto-generated method stub
		Log.d("AdBootServer StartCheckTemp");
		AdBootTempDao Dao        = AdBootTempDao.getInstance(AdBootServer.this);
		AdBootDao     mAdBootDao = AdBootDao.getInstance(AdBootServer.this);
		if(Dao != null && mAdBootDao !=null){
			ArrayList<AdBootImpressionInfo> Info = Dao.GetAllTemp();
			//Dao.delAll();//remove it first.
			if(Info != null && Info.size()>0){
				Log.d("StartCheckTemp size="+Info.size());
				for(AdBootImpressionInfo info:Info){
					if(CheckAdBootImpressionInfo(info)){
						mAdBootDao.InsertOneInfo(info);
					}
					Dao.Remove(info.publisher_id);
				}
			}
		}else{
			android.util.Log.e("Jas","Temp:"+(Dao!=null)+","+(mAdBootDao!=null));
		}
	}
    
	private synchronized boolean CheckAdBootImpressionInfo(AdBootImpressionInfo info){
		if(info == null || !info.IsAviable())return false;
		return (CheckFile(info.FirstSource)||CheckFile(info.SecondSource)||CheckFile(info.ThirdSource));
	}
	
	private boolean CheckFile(String file){
		if(file== null || "".equals(file))return false;
		return (new File(file).exists());
	}
	
	
}
