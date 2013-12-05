package com.joyplus.ad.config;

import com.joyplus.ad.AdConfig;

/*Define by Jas@20131125
 * for Debug this environment*/
public final class Log {
	private static final int LOG_FAIL = -1;
	private static final String TAG   = "AdBootSDK"; 
	
	public static int i(String msg){
		return i(TAG,msg);
	}
    public static int i(String tag, String msg) {
    	if (!AdConfig.GetDebugEnable())return LOG_FAIL;    		
        return android.util.Log.i(tag, msg);
    }
    
    public static int i(String msg,Throwable tr){
		return i(TAG,msg,tr);
	}
    public static int i(String tag, String msg, Throwable tr) {
    	if (!AdConfig.GetDebugEnable())return LOG_FAIL; 
        return android.util.Log.i(tag, msg, tr);
    }
    
    public static int d(String msg){
		return d(TAG,msg);
	}
    public static int d(String tag, String msg) {
    	if (!AdConfig.GetDebugEnable())return LOG_FAIL; 
    	return android.util.Log.d(tag, msg);
    }
    
    public static int d(String msg,Throwable tr){
		return d(TAG,msg,tr);
	}
    public static int d(String tag, String msg, Throwable tr) {
    	if (!AdConfig.GetDebugEnable())return LOG_FAIL; 
    	return android.util.Log.d(tag, msg, tr);
    }
    
    public static int e(String msg){
		return e(TAG,msg);
	}
    public static int e(String tag, String msg) {
    	if (!AdConfig.GetDebugEnable())return LOG_FAIL; 
    	return android.util.Log.e(tag, msg);
    }
	
    public static int e(String msg,Throwable tr){
		return e(TAG,msg,tr);
	}
    public static int e(String tag, String msg, Throwable tr) {
    	if (!AdConfig.GetDebugEnable())return LOG_FAIL; 
    	return android.util.Log.e(tag, msg, tr);
    }
    
    public static int w(String msg){
		return w(TAG,msg);
	}
    public static int w(String tag, String msg, Throwable tr) {
    	if (!AdConfig.GetDebugEnable())return LOG_FAIL; 
    	return android.util.Log.w(tag, msg, tr);
    }
    
    public static int w(String msg,Throwable tr){
		return w(TAG,msg,tr);
	}
    public static int w(String tag, String msg) {
    	if (!AdConfig.GetDebugEnable())return LOG_FAIL; 
    	return android.util.Log.w(tag, msg);
    }
}