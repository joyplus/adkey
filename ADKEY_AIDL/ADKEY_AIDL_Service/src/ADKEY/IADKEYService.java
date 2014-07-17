package ADKEY;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdKeySDKManager;
import com.joyplus.adkey.AdKeySDKManagerException;
import com.joyplus.adkey.downloads.AdFileManager;
import com.joyplus.adkey.request.Report;
import com.joyplus.adkey.request.Request;

public class IADKEYService extends Service{
	private final static  boolean DEBUG = true;
	private final static  String TAG = "Jas";
	private AdFileManager    mAdFileManager = null;
	private Report           mReport        = null;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d(TAG,"onCreate");
		super.onCreate();
		try {
			AdKeySDKManager.Init(IADKEYService.this);
		} catch (AdKeySDKManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG,"onBind");
		return new ADKEYAIDL();
	}
	
	@SuppressLint("NewApi")
	private class ADKEYAIDL extends ADKEY.IADKEY.Stub{
		@Override
		public AD getNetWorkAD(String publisherId, String TYPE) throws RemoteException {
			// TODO Auto-generated method stub
			Log.i(TAG, "ADKEYService getNetWorkAD "+Thread.currentThread().getName()
					+","+Thread.currentThread().getId()+","+Thread.currentThread().toString());
			if(DEBUG)Log.d(TAG,"getNetWorkAD("+publisherId+","+TYPE+")");
			if(!TextUtils.isEmpty(publisherId)){
				return ADKEYTransform.ThransformAD(getNetWorkAd(publisherId, TYPE));
			}
			throw new RemoteException("publisherId or type is unAviable !!!!");
		}

		@Override
		public AD getLocalAD(String publisherId) throws RemoteException {
			// TODO Auto-generated method stub
			if(DEBUG)Log.d(TAG,"getLocalAD("+publisherId+")");
			if(!TextUtils.isEmpty(publisherId)){
				return ADKEYTransform.ThransformAD(getLocalAd(publisherId));
			}
			throw new RemoteException("publisherId or type is unAviable !!!!");
		}

		@Override
		public void ReportShow(String publisherId) throws RemoteException {
			// TODO Auto-generated method stub
			if(DEBUG)Log.d(TAG,"ReportShow("+publisherId+")");
			if(!TextUtils.isEmpty(publisherId)){
				getReport().report(getLocalAd(publisherId));
				return;
			}
			throw new RemoteException("publisherId or type is unAviable !!!!");
		}

		@Override
		public void ReportClick(String publisherId) throws RemoteException {
			// TODO Auto-generated method stub
			if(DEBUG)Log.d(TAG,"ReportClick("+publisherId+")");
			if(!TextUtils.isEmpty(publisherId)){
				getReport().reportClick(getLocalAd(publisherId));
				return;
			}
			throw new RemoteException("publisherId or type is unAviable !!!!");
		}
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////
	///  Joyplus ADKEY support interface
	//////////////////////////////////////////////////////////////////////////////////////////////////
	private Ad getLocalAd(String publisherId){
		if(TextUtils.isEmpty(publisherId))return null;
		return (Ad) getAdFileManager().readSerializableData("ad", publisherId);
	}
	private Ad getNetWorkAd(String publisherId,String TYPE){
		if(TextUtils.equals(ADModel.AD_BANNER, TYPE)){
			return new Request(IADKEYService.this, publisherId).getBannerAd();
		}else if(TextUtils.equals(ADModel.AD_RICHMEDIA, TYPE)){
			return new Request(IADKEYService.this, publisherId).getRichMediaAd();
		}
		return null;
	}
	private synchronized AdFileManager getAdFileManager(){
		if(mAdFileManager == null){
			mAdFileManager = AdFileManager.getInstance();
		}
		return mAdFileManager;
	}
	private synchronized Report getReport(){
		if(mReport == null){
			mReport = new Report(IADKEYService.this);
		}
		return mReport;
	}
	private final static String URL = "http://advapi.yue001.com/advapi/v1/mdrequest?rt=android_app&v=4.1.6&u=Mozilla%2F5.0%20(Linux%3B%20U%3B%20Android%204.1.2%3B%20zh-cn%3B%20AMLOGIC8726MX%20Build%2FV420R270C040B0813CS)%20AppleWebKit%2F533.1%20(KHTML%2C%20like%20Gecko)%20Version%2F4.0%20Mobile%20Safari%2F533.1&u2=Mozilla%2F5.0%20(Linux%3B%20U%3B%20Android%204.1.2%3B%20zh-cn%3B%20AMLOGIC8726MX%20Build%2FV420R270C040B0813CS)%20AppleWebKit%2F533.1%20(KHTML%2C%20like%20Gecko)%20Version%2F4.0%20Mobile%20Safari%2F533.1&s=d286067f946dba267ca925e12369f050&o=&o2=8207839f1d12cadd&t=1405068683736&connection_type=UNKNOWN&listads=&c.mraid=1&sdk=banner&u_wv=Mozilla%2F5.0%20(Linux%3B%20U%3B%20Android%204.1.2%3B%20zh-cn%3B%20AMLOGIC8726MX%20Build%2FV420R270C040B0813CS)%20AppleWebKit%2F533.1%20(KHTML%2C%20like%20Gecko)%20Version%2F4.0%20Mobile%20Safari%2F533.1&i=a3f89430e39476726d2544bb06e771d4&ds=AMLOGIC8726MX";
}
