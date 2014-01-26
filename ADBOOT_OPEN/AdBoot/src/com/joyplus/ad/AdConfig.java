package com.joyplus.ad;

import com.joyplus.ad.AdSDKManager.CUSTOMTYPE;
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
    private final  static  String mDebugBaseURL = "D5313CEAA0E07E98E875117C601ABF26D0248FA1ED3572AC5F31E91461BF151ADDD7BE9F4CAD1C0BA299B9BAC9EF1545C0857980F9D4EE9174A9ABD4EDFBE4C14585B0F8379DFDEFB14531E5B7C4FC0FFE41A3942912F60A";
    private final  static  String mURL_KONKA    = "D5313CEAA0E07E98E875117C601ABF26D0248FA1ED3572AC14A3CB11726A66904709C8F629FDB48C887DBE3AC49F4EDD0B3856DDE8B29AD8D0248FA1ED3572ACAB0CF9C0615443C8FE7A9A05FF18704BC1C23F62DF2D1CCC17DFA7815949350B";
    private final  static  String mURL_JOYPLUS  = "D5313CEAA0E07E98E875117C601ABF26D0248FA1ED3572ACAE7D7F3BBCD40861110294B2C80228A23BBB1702421FFB63C0857980F9D4EE9174A9ABD4EDFBE4C14585B0F8379DFDEFB14531E5B7C4FC0FFE41A3942912F60A";
    private static String  mBaseURL;

	public  static void Init(Context context){
		if(AdSDKManager.IsInited())return;
		if(AdSDKFeature.DEBUG){//advtest
			mBaseURL = (new Des()).strDec(mDebugBaseURL, GetCompany(), "", "");
		}else{
			if(AdSDKManager.GetCustomType() == CUSTOMTYPE.KONKA){
			   mBaseURL = (new Des()).strDec(mURL_KONKA, GetCompany(), "", "");
			}else if(AdSDKManager.GetCustomType() == CUSTOMTYPE.HAIER){
			   mBaseURL = (new Des()).strDec(mURL_JOYPLUS, GetCompany(), "", "");
			}else{//default is AdManager
			   mBaseURL = (new Des()).strDec(mURL_JOYPLUS, GetCompany(), "", "");
			}
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
