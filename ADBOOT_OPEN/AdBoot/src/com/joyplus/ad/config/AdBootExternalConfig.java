package com.joyplus.ad.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*Define by Jas@20131125
 * use to config adboot by custom*/
public class AdBootExternalConfig {
	
	private boolean      LoadOK = false;
	private Properties   props;
	private final static String ConfigFile = "/com/joyplus/ad/config/adconfig.properties"; 
	/*key for ConfigFile*/
	private final static String AdBootDebugEnable  = "AdBootDebugEnable";
	private final static String BaseURL            = "BaseURL";
	private final static String mAdBootBasePath    = "AdBootBasePath";
	
	private static AdBootExternalConfig mAdBootExternalConfig;
	public  static AdBootExternalConfig getInstance(){
		if(mAdBootExternalConfig == null){
			mAdBootExternalConfig = new AdBootExternalConfig();
		}
		return mAdBootExternalConfig;
	}	
	private AdBootExternalConfig(){
		Load();
	}
	
	public boolean GetDebugEnable(boolean defineValue){
		return GetBooleanConfig(AdBootDebugEnable,defineValue);
	}
	public String GetBasePath(String defineValue){
		return GetStringConfig(mAdBootBasePath,defineValue);
	}
	
	public String GetBaseURL(String defineValue){
		return GetStringConfig(BaseURL,defineValue);
	}
	
	private void Load(){   
        try {
        	InputStream is = this.getClass().getResourceAsStream(ConfigFile);
        	props = new Properties();
			props.load(is);
			LoadOK = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int GetIntConfig(String key,int defineValue){
		if(!LoadOK)return defineValue;
		try{
			int value = Integer.parseInt(GetStringConfig(key,Integer.toString(defineValue)));
			return value;
		}catch(NumberFormatException  e){			
		}
		return defineValue;
	}
	
	private boolean GetBooleanConfig(String key,boolean defineValue){
		if(!LoadOK)return defineValue;
		String value = GetStringConfig(key,Boolean.toString(defineValue));
		if(value == null)return defineValue;
		return Boolean.parseBoolean(value);
	}
	
	private String GetStringConfig(String key,String defineValue){
		if(!LoadOK)return defineValue;
		String value = props.getProperty(key);
		if(value == null)return defineValue;
		return value;
	}
}
