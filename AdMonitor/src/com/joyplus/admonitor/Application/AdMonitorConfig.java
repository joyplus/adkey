package com.joyplus.admonitor.Application;


import com.joyplus.admonitor.Config.AdMonitorExternalConfig;
import com.joyplus.admonitor.Config.Des;

import android.content.Context;

/*Config of this AdBoot
 * define by Jas@20140102*/
public class AdMonitorConfig {
	
    private static boolean mAdBootDebugLogEnable   = false;
    
    private final  static  String mCompany         = "joyplus.adkey";
    private final  static  String mBaseURL         = "D5313CEAA0E07E98E875117C601ABF26D0248FA1ED3572ACAE7D7F3BBCD40861110294B2C80228A23BBB1702421FFB63C0857980F9D4EE9174A9ABD4EDFBE4C14585B0F8379DFDEFB14531E5B7C4FC0FFE41A3942912F60A";
    private final  static  String mDebugBaseURL    = "D5313CEAA0E07E98E875117C601ABF26D0248FA1ED3572AC5F31E91461BF151ADDD7BE9F4CAD1C0BA299B9BAC9EF1545C0857980F9D4EE9174A9ABD4EDFBE4C14585B0F8379DFDEFB14531E5B7C4FC0FFE41A3942912F60A";
    //add by Jas@20140305 for app and vc collect
    private final  static  String mAppBaseURL      = "D5313CEAA0E07E98E875117C601ABF26D0248FA1ED3572ACAE7D7F3BBCD40861110294B2C80228A23BBB1702421FFB63C0857980F9D4EE9174A9ABD4EDFBE4C14585B0F8379DFDEF708A5EDF6E008A9C0375DE907C2BB3EA";
    private final  static  String mVcBaceURL       = "D5313CEAA0E07E98E875117C601ABF26D0248FA1ED3572ACAE7D7F3BBCD40861110294B2C80228A23BBB1702421FFB63C0857980F9D4EE9174A9ABD4EDFBE4C14585B0F8379DFDEF16AF2741D452255050C107AA4488E854";
    private final  static  String mDebugAppBaseURL = "D5313CEAA0E07E98E875117C601ABF26D0248FA1ED3572AC5F31E91461BF151ADDD7BE9F4CAD1C0BA299B9BAC9EF1545C0857980F9D4EE9174A9ABD4EDFBE4C14585B0F8379DFDEF708A5EDF6E008A9C0375DE907C2BB3EA";
    private final  static  String mDebugVcBaseURL  = "D5313CEAA0E07E98E875117C601ABF26D0248FA1ED3572AC5F31E91461BF151ADDD7BE9F4CAD1C0BA299B9BAC9EF1545C0857980F9D4EE9174A9ABD4EDFBE4C14585B0F8379DFDEF16AF2741D452255050C107AA4488E854";
    //add by Jas@20140430 for admaster config
  	private final  static  String     mDebugBaseURL_admaster = "http://advapi.yue001.com/advapi/config/admaster";
  	private final  static  String     mURL_KONKA_admaster    = "http://advapikj.joyplus.tv/advapi/config/admaster";//����
  	private final  static  String     mBaseURL_admaster      = "http://advapi.joyplus.tv/advapi/config/admaster";
    private static  String mURL_admaster = "";
    //end add by Jas
    private static String  mURL;
    private static String  mAppURL;
    private static String  mVcURL;
    
	public  static void Init(Context context){
		if(AdMonitorSDKManager.IsInited())return;
		Des mDes = new Des();
		if(AdMonitorSDKFeature.DEBUG){
			mURL    = mDes.strDec(mDebugBaseURL, GetCompany(), "", "");
			mAppURL = mDes.strDec(mDebugAppBaseURL, GetCompany(), "", "");
			mVcURL  = mDes.strDec(mDebugVcBaseURL, GetCompany(), "", "");
			mURL_admaster = mDebugBaseURL_admaster;
		}else{
			mURL    = mDes.strDec(mBaseURL, GetCompany(), "", "");
		    mAppURL = mDes.strDec(mAppBaseURL, GetCompany(), "", "");
		    mVcURL  = mDes.strDec(mVcBaceURL, GetCompany(), "", "");
			mURL_admaster = mBaseURL_admaster;
		}
		if(AdMonitorSDKFeature.EXTERNAL_CONFIG){
			mAdBootDebugLogEnable  = AdMonitorExternalConfig.getInstance().GetDebugEnable(mAdBootDebugLogEnable);
			mURL                   = AdMonitorExternalConfig.getInstance().GetBaseURL(mURL);
			mAppURL                = AdMonitorExternalConfig.getInstance().GetAppURL(mAppURL);
			mVcURL                 = AdMonitorExternalConfig.getInstance().GetVcURL(mVcURL);
		}
	}
    
	private AdMonitorConfig(){}//for can't be new by others.
	public static String ToString(){
		return ("AdBootConfig {"
				+" ,mAdBootDebugEnable="+GetDebugEnable()
				+" ,mURL="+GetBaseURL()
				+" ,mCompany="+GetCompany()
				+" ,mAppURL="+GetAppURL()
				+" ,mURL_admaster="+GetBaseURLAdmaster()
				+" ,mVcURL="+GetVcURL()
				+" }");
	}
	/*Interface of Application*/
	public static boolean GetDebugEnable(){ 
		return mAdBootDebugLogEnable;
	}
	
	public static String GetBaseURL(){
		return mURL;
	}
	public static String GetBaseURLAdmaster(){
		return mURL_admaster;
	}
	public static String GetAppURL(){
		return mAppURL;
	}
	public static String GetVcURL(){
		return mVcURL;
	}
	public static String GetCompany(){
		return mCompany;
	}
	
}
