package com.joyplus.adkey;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.joyplus.adkey.Debug.AdKeyDebugFeature;

public class AdKeyConfig {
    
	private String     Joyplus_REQUESTURL = "http://advapi.yue001.com/advapi/v1/mdrequest";//joyplus
	private String     Konka_REQUESTURL   = "http://advapikj.joyplus.tv/advapi/v1/mdrequest";//康佳
	private String     Runhe_REQUESTURL   = "http://advapi.joyplus.tv/advapi/v1/mdrequest";//润和
	private String     Haier_REQUESTURL   = "http://advapi.joyplus.tv/advapi/v1/mdrequest";//海尔
	//add by Jas@20140227
	private String     Joyplus_app_REQUESTURL = "http://advapi.joyplus.tv/advapi/v1/mdapplog";//for app 
	private String     Joyplus_vc_REQUESTURL  = "http://advapi.joyplus.tv/advapi/v1/mdvclog";//for video
	//end add by Jas
	private boolean    LoadOK = false;
	private Properties props;
	private static AdKeyConfig mAdKeyConfig;
	public  static AdKeyConfig getInstance(){
		if(mAdKeyConfig == null){
			mAdKeyConfig = new AdKeyConfig();
		}
		return mAdKeyConfig;
	}
	public AdKeyConfig(){
		Load();
	}
	
	private void Load(){   
        try {
        	InputStream is = this.getClass().getResourceAsStream("/com/joyplus/Config/adkeyconfig.properties");
            props = new Properties();
			props.load(is);
			LoadOK = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e){
			// TODO Auto-generated catch block
			android.util.Log.i("AdKey","External Config no find !!!!");
		}
	}
	
	public String getREQUESTURL(){
		//add by Jas@20140415 for DEMO Test.
		if(AdKeyDebugFeature.DEBUG)return DEMO_Test;
		if(LoadOK){
			String url = props.getProperty("REQUESTURL");
			if(url != null)return url;
		}
		int custom = getCUSTOM();
		if(custom == CUSTOM.KONKA){
			return Konka_REQUESTURL;
		}else if(custom == CUSTOM.RUNHE){
			return Runhe_REQUESTURL;
		}else if(custom == CUSTOM.HAIER){
			return Haier_REQUESTURL;
		}else{
			return Joyplus_REQUESTURL;
		}
	}
	public String getAppREQUESTURL(){
		if(LoadOK){
			String custom = props.getProperty("AppURL");
			if(custom != null)return custom;
		}
		return Joyplus_app_REQUESTURL;
	}
	public String getVcREQUESTURL(){
		if(LoadOK){
			String custom = props.getProperty("VcURL");
			if(custom != null)return custom;
		}
		return Joyplus_vc_REQUESTURL;
	}
    public int getCUSTOM(){
    	if(LoadOK){
			String custom = props.getProperty("CUSTOM");
			if(custom != null)return Integer.valueOf(custom);
		}
		return CUSTOM.JOYPLUS;
    }
    
    ///////////////////////////////////////////////////////
    //for Joyplus Debug Demo test 
    private String DEMO_Test = Joyplus_REQUESTURL;
    public List<String> GetDebugBaseUrl(){
    	if(!AdKeyDebugFeature.DEBUG)return null;
    	List<String> url = new ArrayList<String>();
    	url.add(Joyplus_REQUESTURL);
    	url.add(Konka_REQUESTURL);
    	url.add(Runhe_REQUESTURL);
    	url.add(Haier_REQUESTURL);
    	return url;
    }
    public void SetDEMOTEST(String url){
    	if(!AdKeyDebugFeature.DEBUG)return;
    	DEMO_Test = url;
    }
    public String GetDEMOTEST(){
    	if(!AdKeyDebugFeature.DEBUG)return null;
    	return DEMO_Test;
    }
}
