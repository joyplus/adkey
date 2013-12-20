package com.joyplus.adkey;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AdKeyConfig {
    
	private String     Joyplus_REQUESTURL = "http://advapitest.yue001.com/advapi/v1/mdrequest";//joyplus
	private String     Konka_REQUESTURL   = "http://advapikj.joyplus.tv/advapi/v1/mdrequest";//康佳
	private String     Runhe_REQUESTURL   = "http://advapi.joyplus.tv/advapi/v1/mdrequest";//润和
	
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
        	InputStream is = this.getClass().getResourceAsStream("/com/joyplus/adkeyConfig/adkeyconfig.properties");
            props = new Properties();
			props.load(is);
			LoadOK = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getREQUESTURL(){
		if(LoadOK){
			String url = props.getProperty("REQUESTURL");
			if(url != null)return url;
		}
		int custom = getCUSTOM();
		if(custom == CUSTOM.KONKA){
			return Konka_REQUESTURL;
		}else if(custom == CUSTOM.RUNHE){
			return Runhe_REQUESTURL;
		}else{
			return Joyplus_REQUESTURL;
		}
	}
	
    public int getCUSTOM(){
    	if(LoadOK){
			String custom = props.getProperty("CUSTOM");
			if(custom != null)return Integer.valueOf(custom);
		}
		return CUSTOM.JOYPLUS;
    }
}
