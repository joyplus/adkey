package com.joyplus.ad.application;

import com.joyplus.ad.AdSDKManager;

import android.os.Parcel;
import android.os.Parcelable;

public class CUSTOMINFO implements Parcelable{
     public enum DEVICETYPE{
    	 TV     (1),     	 
    	 TVBOX  (2);
    	 private int TYPE;
    	 DEVICETYPE(int type){
    		 TYPE = type;
    	 }
    	 public int toInt(){
    		 return TYPE;
    	 }
     }
     
	 public enum LICENSEPROVIDER{
		 WASU   (0), 
		 CNTV   (1);
		 private int LICENSE;
		 LICENSEPROVIDER(int license){
			 LICENSE = license;
		 }
		 public int toInt(){
			 return LICENSE;
		 }
	 }
	 
	 public enum USEMODE{
		 HOME   (0),
		 MARKET (1);
		 private int MODE;
		 USEMODE(int mode){
			 MODE = mode;
		 }
		 public int toInt(){
			 return MODE;
		 }
	 }
	 
	 
	 
	 
	 private String      mDEVICEMUMBER        = ""; //ds
	 private String      mSN                  = ""; //sn
	 private DEVICETYPE  mDEVICETYPE          = DEVICETYPE.TV;//dt
	 private USEMODE     mUSEMODE             = USEMODE.HOME;//up
	 private LICENSEPROVIDER mLICENSEPROVIDER = LICENSEPROVIDER.WASU;//lp
	 private String      mDEVICEMOVEMENT      = "";//dm
	 private String      mBRAND               = "";//d
	 private int         mLastBootUpCount     = 0;//ot
	 
	 public  CUSTOMINFO(){
		 if(!AdSDKManager.IsInited())
			  throw new IllegalArgumentException("AdSDKManager don't Init first !!!!");			 
		 mBRAND = AdSDKManager.getInstance().GetCustomType().toString();
	 }
	 public  CUSTOMINFO(CUSTOMINFO info){
		 if(info != null){
			 mDEVICEMUMBER     = info.mDEVICEMUMBER;
			 mSN               = info.mSN;
			 mDEVICETYPE       = info.mDEVICETYPE;
			 mUSEMODE          = info.mUSEMODE;
			 mLICENSEPROVIDER  = info.mLICENSEPROVIDER;
			 mDEVICEMOVEMENT   = info.mDEVICEMOVEMENT;
			 mBRAND            = info.mBRAND;
			 mLastBootUpCount  = info.mLastBootUpCount;
		 }
	 }
	 public CUSTOMINFO CreateNew(){
		 return new CUSTOMINFO(this);
	 }
	 
	public void SetDEVICEMUMBER(String m){
		mDEVICEMUMBER = m;
	}
	public String GetDEVICEMUMBER(){
		return mDEVICEMUMBER ;
	}
	
	public void SetSN(String m){
		mSN = m;
	}
	public String GetSN(){
		return mSN;
	}
	
	public void SetDEVICETYPE(DEVICETYPE m){
		mDEVICETYPE = m;
	}
	public DEVICETYPE GetDEVICETYPE(){
		return mDEVICETYPE;
	}
	
	public void SetUSEMODE(USEMODE m){
		mUSEMODE = m;
	}
	public USEMODE GetUSEMODE(){
		return mUSEMODE;
	}
	
	public void SetLICENSEPROVIDER(LICENSEPROVIDER m){
		mLICENSEPROVIDER = m;
	}
	public LICENSEPROVIDER GetLICENSEPROVIDER(){
		return mLICENSEPROVIDER;
	}
	
	public void SetDEVICEMOVEMENT(String m){
		mDEVICEMOVEMENT = m;
	}
	public String GetDEVICEMOVEMENT(){
		return mDEVICEMOVEMENT;
	}
	
	public String GetBRAND(){
		return mBRAND;
	}
	
	public void SetLastBootUpCount(int m){
		mLastBootUpCount = m;
	}
	public int GetLastBootUpCount(){
		return mLastBootUpCount; 
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(mDEVICEMUMBER);
		dest.writeString(mSN);
		dest.writeInt(mDEVICETYPE.toInt());
		dest.writeInt(mUSEMODE.toInt());
		dest.writeInt(mLICENSEPROVIDER.toInt());
		dest.writeString(mDEVICEMOVEMENT);
		dest.writeString(mBRAND);
		dest.writeInt(mLastBootUpCount);
	}
	 
	 
}
