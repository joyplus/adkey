package com.joyplus.adkey;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AdKeyConfig {
    
	//private String     REQUESTURL = "http://adv.yue001.com/md.request.php";
	//private String     REQUESTURL = "http://adkey.joyplus.tv/md.request.php";
	private String       REQUESTURL = "http://advapitest.yue001.com/advapi/v1/mdrequest";
	
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
        	InputStream is = this.getClass().getResourceAsStream("/com/joyplus/adkey/adkeyconfig.properties");
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
		return REQUESTURL;
	}
    
}
