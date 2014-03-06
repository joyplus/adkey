package com.joyplus.admonitor.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*Define by Jas@20131125
 * use to config adboot by custom*/
public class AdMonitorExternalConfig {
	
	private boolean      LoadOK = false;
	private Properties   props;
	private final static String ConfigFile = "/com/joyplus/Config/admonitorconfig.properties"; 
	/*key for ConfigFile*/
	private final static String AdMonitorDebugEnable  = "AdMonitorDebugEnable";
	private final static String BaseURL               = "BaseURL";
	private final static String AppURL                = "AppURL";
	private final static String VcURL                 = "VcURL";
	private static AdMonitorExternalConfig mAdBootExternalConfig;
	public  static AdMonitorExternalConfig getInstance(){
		if(mAdBootExternalConfig == null){
			mAdBootExternalConfig = new AdMonitorExternalConfig();
		}
		return mAdBootExternalConfig;
	}	
	private AdMonitorExternalConfig(){
		Load();
	}
	
	public boolean GetDebugEnable(boolean defineValue){
		return GetBooleanConfig(AdMonitorDebugEnable,defineValue);
	}
	
	
	public String GetBaseURL(String defineValue){
		return GetStringConfig(BaseURL,defineValue);
	}
	public String GetAppURL(String defineValue) {
		// TODO Auto-generated method stub
		return GetStringConfig(AppURL,defineValue);
	}
	public String GetVcURL(String defineValue) {
		// TODO Auto-generated method stub
		return GetStringConfig(VcURL,defineValue);
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
		} catch (NullPointerException e){
			// TODO Auto-generated catch block
			android.util.Log.i("AdMonitorSDK","External Config no find !!!!");
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
