package com.joyplus.admonitor.Application;


import com.joyplus.admonitor.Config.AdMonitorExternalConfig;
import com.joyplus.admonitor.Config.Des;

import android.content.Context;

/*Config of this AdBoot
 * define by Jas@20140102*/
public class AdMonitorConfig {
	
    private static boolean mAdBootDebugEnable   = true;
    
    private final  static  String mCompany      = "joyplus.adkey";
    private final  static  String mDebugBaseURL = "D5313CEAA0E07E98E875117C601ABF26D0248FA1ED3572AC5F31E91461BF151ADDD7BE9F4CAD1C0BA299B9BAC9EF1545C0857980F9D4EE9174A9ABD4EDFBE4C14585B0F8379DFDEFB14531E5B7C4FC0FFE41A3942912F60A";
    private static String  mBaseURL;

	public  static void Init(Context context){
		if(AdMonitorSDKManager.IsInited())return;
		if(AdMonitorSDKFeature.DEBUG){
			mBaseURL = (new Des()).strDec(mDebugBaseURL, GetCompany(), "", "");
		}
		if(AdMonitorSDKFeature.EXTERNAL_CONFIG){
			mAdBootDebugEnable  = AdMonitorExternalConfig.getInstance().GetDebugEnable(mAdBootDebugEnable);
			mBaseURL            = AdMonitorExternalConfig.getInstance().GetBaseURL(mBaseURL);
		}
	}
    
	private AdMonitorConfig(){}//for can't be new by others.
	public static String ToString(){
		return ("AdBootConfig {"
				+" ,mAdBootDebugEnable="+GetDebugEnable()
				+" ,mBaseURL="+GetBaseURL()
				+" ,mCompany="+GetCompany()
				+" }");
	}
	/*Interface of Application*/
	public static boolean GetDebugEnable(){ 
		return mAdBootDebugEnable;
	}
	
	public static String GetBaseURL(){
		return mBaseURL;
	}
	public static String GetCompany(){
		return mCompany;
	}
	
}
