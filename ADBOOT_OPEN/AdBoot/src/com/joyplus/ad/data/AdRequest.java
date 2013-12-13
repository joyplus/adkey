package com.joyplus.ad.data;

import android.net.Uri;

import com.joyplus.ad.AdConfig;
import com.joyplus.ad.AdManager.AD;
import com.joyplus.ad.AdMode;
import com.joyplus.ad.AdRequestManager;
import com.joyplus.ad.PhoneManager;
import com.joyplus.ad.application.AdBoot;
import com.joyplus.ad.config.Log;

public class AdRequest {
      
	  private AdMode mAdMode = null; 
	  private AdBoot mAdBoot;
	  private String listAds;//don't know it mean.
	  private final static boolean Debug = false;
	  public AdRequest(AdMode ad){
		  mAdMode = ad;
	  }
	  public AdRequest(AdMode ad,AdBoot adboot){
		  mAdMode = ad;
		  mAdBoot = adboot;
	  }
	  public void setListAds(final String listAds) {
			this.listAds = listAds;
	  }
	  public String getListAds() {
			if (this.listAds != null)
				return this.listAds;
			else
				return "";
	  }
	  @Override
	  public String toString() {
		 return this.toUri().toString();
	  }
	  
	  public Uri toUri() { 
			final Uri.Builder b = Uri.parse(AdConfig.GetBaseURL()).buildUpon();
			if(Debug)Log.d("URI="+AdConfig.GetBaseURL());
			if(Debug)if(mAdMode == null)Log.d("++++++++++++");
			if(Debug)if(mAdMode != null)Log.d("AD="+mAdMode.GetAD().toString());
			if(Debug)if(mAdMode == null || mAdMode.GetAD() == AD.UNKNOW)return b.build();
			b.appendQueryParameter("rt", AdRequestManager.REQUEST_TYPE_ANDROID);
			if(Debug)Log.d("rt="+AdRequestManager.REQUEST_TYPE_ANDROID);
			b.appendQueryParameter("v",  PhoneManager.getInstance().GetAndroidVersion());
			if(Debug)Log.d("v="+PhoneManager.getInstance().GetAndroidVersion());
			//b.appendQueryParameter("i",  PhoneManager.getInstance().GetMac());
			//if(Debug)Log.d("i="+PhoneManager.getInstance().GetIp());
			//if(Debug)Log.d("mac="+PhoneManager.getInstance().GetMac());
			if(mAdBoot == null || mAdBoot.GetCUSTOMINFO() == null || mAdBoot.GetCUSTOMINFO().GetMAC() == null
					|| "".equals(mAdBoot.GetCUSTOMINFO().GetMAC())){
				b.appendQueryParameter("i", "");
			}else{
				b.appendQueryParameter("i", MD5Util.GetMD5Code(mAdBoot.GetCUSTOMINFO().GetMAC()));
				if(Debug)Log.d("i="+MD5Util.GetMD5Code(mAdBoot.GetCUSTOMINFO().GetMAC()));
			}
			b.appendQueryParameter("u",  PhoneManager.getInstance().GetUA1());
			if(Debug)Log.d("u="+PhoneManager.getInstance().GetUA1());
			b.appendQueryParameter("u2", PhoneManager.getInstance().GetUA2());
			if(Debug)Log.d("u2="+PhoneManager.getInstance().GetUA2());
			b.appendQueryParameter("s",  mAdMode.GetPublisherId().GetPublisherId());
			if(Debug)Log.d("s="+mAdMode.GetPublisherId().GetPublisherId());
			b.appendQueryParameter("o",  PhoneManager.getInstance().GetDeviceId1());
			if(Debug)Log.d("o="+PhoneManager.getInstance().GetDeviceId1());
			b.appendQueryParameter("o2", PhoneManager.getInstance().GetDeviceId2());
			if(Debug)Log.d("o2="+PhoneManager.getInstance().GetDeviceId2());
			b.appendQueryParameter("t",  Long.toString(System.currentTimeMillis()));
			if(Debug)Log.d("t="+Long.toString(System.currentTimeMillis()));
			b.appendQueryParameter("connection_type", PhoneManager.getInstance().GetConnectType().toString());
			if(Debug)Log.d("connection_type="+PhoneManager.getInstance().GetConnectType().toString());
			b.appendQueryParameter("listads", this.getListAds());
			if(Debug)Log.d("listads="+this.getListAds());
			switch(mAdMode.GetAD()){
			case BINNER:
				b.appendQueryParameter("c.mraid", "1");
				b.appendQueryParameter("sdk","banner");
				break;
			case VAD:
				b.appendQueryParameter("c.mraid", "0");
				b.appendQueryParameter("sdk","vad");
				break;
			case ADBOOT:
				b.appendQueryParameter("sdk", "open");
				if(Debug)Log.d("sdk="+"open");
				if(mAdBoot == null || mAdBoot.GetCUSTOMINFO() == null || mAdBoot.GetCUSTOMINFO().GetDEVICEMUMBER() == null){
					b.appendQueryParameter("ds", "");
				}else{
					b.appendQueryParameter("ds", mAdBoot.GetCUSTOMINFO().GetDEVICEMUMBER());
					if(Debug)Log.d("ds="+mAdBoot.GetCUSTOMINFO().GetDEVICEMUMBER());
				}
				if(mAdBoot == null || mAdBoot.GetCUSTOMINFO() == null || mAdBoot.GetCUSTOMINFO().GetSN() == null){
					b.appendQueryParameter("sn", "");
				}else{
					b.appendQueryParameter("sn", mAdBoot.GetCUSTOMINFO().GetSN());
					if(Debug)Log.d("sn="+mAdBoot.GetCUSTOMINFO().GetSN());
				}
				if(mAdBoot == null || mAdBoot.GetCUSTOMINFO() == null || mAdBoot.GetCUSTOMINFO().GetDEVICETYPE() == null){
					b.appendQueryParameter("dt", "");
				}else{
					b.appendQueryParameter("dt", Integer.toString(mAdBoot.GetCUSTOMINFO().GetDEVICETYPE().toInt()));
					if(Debug)Log.d("dt="+Integer.toString(mAdBoot.GetCUSTOMINFO().GetDEVICETYPE().toInt()));
				}
				if(mAdBoot == null || mAdBoot.GetCUSTOMINFO() == null || mAdBoot.GetCUSTOMINFO().GetUSEMODE() == null){
					b.appendQueryParameter("up", "");
				}else{
					b.appendQueryParameter("up", Integer.toString(mAdBoot.GetCUSTOMINFO().GetUSEMODE().toInt()));
					if(Debug)Log.d("up="+Integer.toString(mAdBoot.GetCUSTOMINFO().GetUSEMODE().toInt()));
				}
				if(mAdBoot == null || mAdBoot.GetCUSTOMINFO() == null || mAdBoot.GetCUSTOMINFO().GetLICENSEPROVIDER() == null){
					b.appendQueryParameter("lp", "");
				}else{
					b.appendQueryParameter("lp", Integer.toString(mAdBoot.GetCUSTOMINFO().GetLICENSEPROVIDER().toInt()));
					if(Debug)Log.d("lp="+Integer.toString(mAdBoot.GetCUSTOMINFO().GetLICENSEPROVIDER().toInt()));
				}
				if(mAdBoot == null || mAdBoot.GetCUSTOMINFO() == null || mAdBoot.GetCUSTOMINFO().GetDEVICEMOVEMENT() == null){
					b.appendQueryParameter("dm", "");
				}else{
					b.appendQueryParameter("dm", mAdBoot.GetCUSTOMINFO().GetDEVICEMOVEMENT());
					if(Debug)Log.d("dm="+mAdBoot.GetCUSTOMINFO().GetDEVICEMOVEMENT());
				}
				if(mAdBoot == null || mAdBoot.GetCUSTOMINFO() == null || mAdBoot.GetCUSTOMINFO().GetBRAND() == null){
					b.appendQueryParameter("b", "");
				}else{
					b.appendQueryParameter("b", mAdBoot.GetCUSTOMINFO().GetBRAND().toString());
					if(Debug)Log.d("b="+mAdBoot.GetCUSTOMINFO().GetBRAND().toString());
				}
				if(mAdBoot == null || mAdBoot.GetCUSTOMINFO() == null ){
					b.appendQueryParameter("ot", "");
				}else{
					b.appendQueryParameter("ot", Integer.toString(mAdBoot.GetCUSTOMINFO().GetLastBootUpCount()));
					if(Debug)Log.d("ot="+Integer.toString(mAdBoot.GetCUSTOMINFO().GetLastBootUpCount()));
				}
				break;
			case ADPLACE:
				break;
			}
			b.appendQueryParameter("u_wv", PhoneManager.getInstance().GetUA1());
			if(Debug)Log.d("u_wv="+PhoneManager.getInstance().GetUA1());
			return b.build();
		}
}
