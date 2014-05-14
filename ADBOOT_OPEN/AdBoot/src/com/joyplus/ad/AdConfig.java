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
	//add by Jas@20140430 for admaster config
	private final  static  String     mDebugBaseURL_admaster = "http://advapi.yue001.com/advapi/config/admaster";
	private final  static  String     mURL_KONKA_admaster    = "http://advapikj.joyplus.tv/advapi/config/admaster";//¿µ¼Ñ
	private final  static  String     mURL_JOYPLUS_admaster  = "http://advapi.joyplus.tv/advapi/config/admaster";
    private static  String mBaseURL_admaster = "";
  //end add by Jas
    private final  static  int    DEFAULT_MAX   = 5;
    private static int     MAXSIZE              = DEFAULT_MAX;
    private static boolean COPYALWAYS           = true;
	public  static void Init(Context context){
		if(AdSDKManager.IsInited())return;
		if(AdSDKFeature.DEBUG){//advtest
			mBaseURL    = (new Des()).strDec(mDebugBaseURL, GetCompany(), "", "");
			mBaseURL_admaster = mDebugBaseURL_admaster;
		}else{
			if(AdSDKManager.GetCustomType() == CUSTOMTYPE.KONKA){
			   mBaseURL = (new Des()).strDec(mURL_KONKA, GetCompany(), "", "");
			   mBaseURL_admaster = mURL_KONKA_admaster;
			}else if(AdSDKManager.GetCustomType() == CUSTOMTYPE.HAIER){
			   mBaseURL = (new Des()).strDec(mURL_JOYPLUS, GetCompany(), "", "");
			   mBaseURL_admaster = mURL_JOYPLUS_admaster;
			}else{//default is AdManager
			   mBaseURL = (new Des()).strDec(mURL_JOYPLUS, GetCompany(), "", "");
			   mBaseURL_admaster = mDebugBaseURL_admaster;
			}
		}
		if(AdSDKFeature.EXTERNAL_CONFIG){
			mAdBootDebugEnable  = AdBootExternalConfig.getInstance(context).GetDebugEnable(mAdBootDebugEnable);
			mBaseURL            = AdBootExternalConfig.getInstance(context).GetBaseURL(mBaseURL);
			mAdBootBasePath     = AdBootExternalConfig.getInstance(context).GetBasePath(mAdBootBasePath);
			MAXSIZE             = AdBootExternalConfig.getInstance(context).GetMAXSIZE(DEFAULT_MAX);
			COPYALWAYS          = AdBootExternalConfig.getInstance(context).GetCOPYALWAYS(COPYALWAYS);
		}
	}
    
	private AdConfig(){}//for can't be new by others.
	public static String ToString(){
		return ("AdBootConfig {"
				+" ,mAdBootDebugEnable="+GetDebugEnable()
				+" ,mAdBootBasePath="+GetBasePath()
				+" ,mAdBootBasePathName="+GetBasePathName()
				+" ,mBaseURL="+GetBaseURL()
				+" ,mCompany="+GetCompany()
				+" ,mBaseURL_admaster="+GetBaseURLAdmaster()
				+" ,MAXSIZE="+GetMaxSize()
				+" ,COPYALWAYS="+GetCOPYALWAYS()
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
	public static String GetBaseURLAdmaster(){
		return mBaseURL_admaster;
	}
	public static String GetCompany(){
		return mCompany;
	}
	public static int GetMaxSize(){
		return MAXSIZE;
	}
	public static boolean GetCOPYALWAYS(){
		return COPYALWAYS;
	}
}
