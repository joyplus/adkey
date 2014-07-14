package ADKEY;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

 
public class AdKeyManager {
	private Context  mContext;
    private IADKEY   mADKEY;  
    private AdkeyServiceConnection mAdKeyConnection;
	private class AdkeyServiceConnection implements ServiceConnection{
		@Override 
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i("Jas", "AdkeyServiceConnection onServiceConnected");
			mADKEY = IADKEY.Stub.asInterface(service);
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i("Jas", "AdkeyServiceConnection onServiceDisconnected");
			mADKEY = null; 
		}
	}
    
	public AdKeyManager(Context context){
		mContext = context.getApplicationContext();
		mAdKeyConnection = new AdkeyServiceConnection();
		bindService();
	}
	
	public void bindService(){
		mContext.bindService(new Intent("JOYPLUS.AIDL.ADKEYSERVICE"), mAdKeyConnection, Context.BIND_AUTO_CREATE);
	} 
	public void unBindService(){ 
		mContext.unbindService(mAdKeyConnection);
	} 
	public AD getNetWorkAD(String publisherId,String type){
		try {
			if(mADKEY!=null && !TextUtils.isEmpty(publisherId))
				return mADKEY.getNetWorkAD(publisherId, type);
		} catch (RemoteException e) {
		}
		return null;
	}
	
	public AD getLocalAD(String publisherId){
		try {
			if(mADKEY!=null && !TextUtils.isEmpty(publisherId))
				return mADKEY.getLocalAD(publisherId);
		} catch (RemoteException e) {
		}
		return null;
	}
	
	public void reportAdShow(String publisherId){
		try {
			if(mADKEY!=null && !TextUtils.isEmpty(publisherId))mADKEY.ReportShow(publisherId);
		} catch (RemoteException e) {
		}
	}
	public void reportAdClick(String publisherId){
		try {
			if(mADKEY!=null && !TextUtils.isEmpty(publisherId))mADKEY.ReportClick(publisherId);
		} catch (RemoteException e) {
		}
	}
	public boolean IsADKEYServiceConnected(){
		return mADKEY!=null;
	}
}
