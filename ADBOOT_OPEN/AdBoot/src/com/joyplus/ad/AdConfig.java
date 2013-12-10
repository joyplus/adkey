package com.joyplus.ad;

import com.joyplus.ad.config.AdBootExternalConfig;
import com.joyplus.ad.data.Des;

import android.content.Context;

/*Config of this AdBoot
 * define by Jas@20131125*/
public class AdConfig {
	
    private static boolean mAdBootDebugEnable   = false;
    
    private static String  mAdBootBasePath      = "";
    private static String  mAdBootBasePathName  = ".adsdk";
    private final  static  String mCompany      = "joyplus.adkey";
    private final  static  String mDebugBaseURL = "D5313CEAA0E07E98E875117C601ABF26D0248FA1ED3572AC18167485A54977E991C375FC60B1EA25DDD7BE9F4CAD1C0BA299B9BAC9EF1545C0857980F9D4EE9174A9ABD4EDFBE4C14585B0F8379DFDEFB14531E5B7C4FC0FFE41A3942912F60A";
    private final  static  String mUseBaseURL   = "D5313CEAA0E07E98E875117C601ABF26D0248FA1ED3572AC14A3CB11726A66904709C8F629FDB48C887DBE3AC49F4EDD0B3856DDE8B29AD8D0248FA1ED3572ACAB0CF9C0615443C8FE7A9A05FF18704BC1C23F62DF2D1CCC17DFA7815949350B";
    private static String  mBaseURL;

	public  static void Init(Context context){
		if(AdSDKManager.IsInited())return;
		if(AdSDKFeature.DEBUG){
			mBaseURL = (new Des()).strDec(mDebugBaseURL, GetCompany(), "", "");
		}else{ 
			mBaseURL = (new Des()).strDec(mUseBaseURL, GetCompany(), "", "");
		}
		if(AdSDKFeature.EXTERNAL_CONFIG){
			mAdBootDebugEnable  = AdBootExternalConfig.getInstance().GetDebugEnable(mAdBootDebugEnable);
			mBaseURL            = AdBootExternalConfig.getInstance().GetBaseURL(mBaseURL);
			mAdBootBasePath     = AdBootExternalConfig.getInstance().GetBasePath(mAdBootBasePath);
		}
	}
    
	private AdConfig(){}//for can't be new by others.
	public static String ToString(){
		return ("AdBootConfig {"
				+" ,mAdBootDebugEnable="+GetDebugEnable()
				+" ,mAdBootBasePathName="+GetBasePathName()
				+" ,mBaseURL="+GetBaseURL()
				+" ,mCompany="+GetCompany()
				+" }");
	}
	/*Interface of Application*/
	public static boolean GetDebugEnable(){ 
		return mAdBootDebugEnable;
	}
	public static String GetBasePath(){
		return mAdBootBasePath;
	}
	public static String GetBasePathName(){
		return mAdBootBasePathName;
	}
	public static String GetBaseURL(){
		return mBaseURL;
	}
	public static String GetCompany(){
		return mCompany;
	}
	
}
