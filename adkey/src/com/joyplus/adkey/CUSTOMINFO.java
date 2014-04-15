package com.joyplus.adkey;

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
	 //srceen type
	 public enum SCREEN{
		 S_UNKNOW(""),
		 S_SD    ("001"),//����   1028*720
		 S_HD    ("002"),//����   1920*1080
		 S_4K    ("003");//4K2K 3840*2160
		 private String Screen;
		 SCREEN(String screen){
			 Screen = screen;
		 }
		 public String toString(){
			 return Screen;
		 }
	 }
	 //zip or video back from server.
	 public enum SOURCETYPE{
		 IMAGE ("1"),
		 ZIP   ("4"),
		 VIDEO ("2"),
		 MRAID ("3"),
		 ZIPANDVIDEO ("5");
		 private String SourceType;
		 SOURCETYPE(String s) {
			// TODO Auto-generated constructor stub
			 SourceType = s;
		 }
		 public String toString(){
			 return SourceType;
		 }
	 }
	 public enum CUSTOMTYPE{//
		  CHANGHONG    (0,"CHANGHONG"),
		  OUBAOLI      (1,"OUBAOLI"),
		  KONKA        (2,"KONKA"),
		  SKYWORTH     (4,"Skyworth"),
		  HAIER        (5,"Haier"),
		  SONY         (6,"SONY"),
		  PANASONIC    (7,"Panasonic"),
		  TCL          (8,"TCL"),
		  HISENSE      (9,"Hisense"),
		  JOYPLUS      (10,"Joyplus");
		  int CUSTOM;
		  String BRAND;
		  CUSTOMTYPE(int Custom,String Brand){
			  CUSTOM = Custom;
			  BRAND  = Brand;
		  }
		  public int toInt(){
			  return CUSTOM;
		  }
		  public String toString(){
			  return BRAND;
		  }
	  }
	 private String      mDEVICEMUMBER        = ""; //ds
	 private String      mSN                  = ""; //sn
	 private DEVICETYPE  mDEVICETYPE          = DEVICETYPE.TV;//dt
	 private USEMODE     mUSEMODE             = USEMODE.HOME;//up
	 private LICENSEPROVIDER mLICENSEPROVIDER = LICENSEPROVIDER.WASU;//lp
	 private String      mDEVICEMOVEMENT      = "";//dm
	 private String      mBRAND               = "";//b
	 private int         mLastBootUpCount     = 0;//ot
	 private String      mMAC                 = "";//i
	 private SCREEN      mSCREEN              = SCREEN.S_UNKNOW;//screen
	 private SOURCETYPE  mSOURCETYPE          = null;//mt    don't know what to use.
	 //add by Jas@20140227
	 private String      mOS                  = "";//os
	 private String      mOSV                 = "";//osv
	 private int         mDSS                 = 0;//dss
	 private String      mDSR                 = "";//dsr
	 public  void SetOS(String os){
		 mOS = os;
	 }
	 public String GetOS(){
		 return mOS;
	 }
	 public void SetOSVersion(String osv){
		 mOSV = osv;
	 }
	 public String GetOSVersion(){
		 return mOSV;
	 }
	 public void SetDeviceScreenSize(int size){
		 mDSS = size;
	 }
	 public int GetDeviceScreenSize(){
		 return mDSS>0?mDSS:0;
	 }
	 public void SetDeviceScreenResolution(int Width,int Height){
		 if(Width<0 || Height<0)return;
		 mDSR = Width+"X"+Height;
	 }
     public String GetDeviceScreenResolution(){
    	 if(mDSR == null || "".equals(mDSR))return null;
    	 return mDSR;
     } 
     //end add by Jas
	 public  CUSTOMINFO(){
		 mBRAND = CUSTOMTYPE.JOYPLUS.toString();
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
			 mMAC              = info.mMAC;
			 mSCREEN           = info.mSCREEN;
			 mSOURCETYPE       = info.mSOURCETYPE;
			 //add by Jas@20140227
			 mOS               = info.mOS;
			 mOSV              = info.mOSV;
			 mDSS              = info.mDSS;
			 mDSR              = info.mDSR;
			 //end add by Jas
		 }else{
			 mBRAND = CUSTOMTYPE.JOYPLUS.toString();
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
	public void SetMAC(String mac){
		mMAC  = mac; 
	}
	public String GetMAC(){
		return mMAC;
	}
	public void SetSCREEN(SCREEN s){
		mSCREEN = s;
	}
	public SCREEN GetSCREEN(){
		return mSCREEN;
	}
	public void SetSOURCETYPE(SOURCETYPE s){
		mSOURCETYPE = s;
	}
	public SOURCETYPE GetSOURCETYPE(){
		return mSOURCETYPE;
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
		dest.writeString(mMAC);
		if(mSCREEN != null)
		dest.writeString(mSCREEN.toString());
		if(mSOURCETYPE != null)
		dest.writeString(mSOURCETYPE.toString());
		//add by Jas@20140227
		dest.writeString(mOS);
		dest.writeString(mOSV);
		dest.writeInt(mDSS);
		dest.writeString(mDSR);
		//end add by Jas
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer ap = new StringBuffer();
		ap.append(" CUSTOMINFO={")
		  .append(" mDEVICEMUMBER="+mDEVICEMUMBER)
		  .append(" ,mSN="+mSN)
		  .append(" ,mDEVICETYPE="+mDEVICETYPE.toInt())
		  .append(" ,mUSEMODE="+mUSEMODE.toInt())
		  .append(" ,mLICENSEPROVIDER="+mLICENSEPROVIDER.toInt())
		  .append(" ,mDEVICEMOVEMENT="+mDEVICEMOVEMENT)
		  .append(" ,mBRAND="+mBRAND)
		  .append(" ,mLastBootUpCount="+mLastBootUpCount)
		  .append(" ,mMAC="+mMAC)
		  .append(" ,mSCREEN="+((mSCREEN==null)?"null":mSCREEN.toString()))
		  .append(" ,mSOURCETYPE="+((mSOURCETYPE==null)?"null":mSOURCETYPE.toString()))
		  //add by Jas@20140227
		  .append(" ,mOS="+mOS)
		  .append(" ,mOSV="+mOSV)
		  .append(" ,mDSS="+mDSS)
		  .append(" ,mDSR="+mDSR)
		  //end add by Jas
		  .append(" }");
		return ap.toString();
	}
	 
	 
}
