package com.joyplus.adkeydemo;

import android.content.Context;
import android.content.SharedPreferences;

public class PublisherIdManager {
    private Context mContext;
    private static PublisherIdManager mInstance;
    public  static void Init(Context context){
    	mInstance = new PublisherIdManager(context.getApplicationContext());
    }
    public  static PublisherIdManager GetInstance(){
    	return mInstance;
    }
	private PublisherIdManager(Context context){
		mContext = context;
		InitResource();
	}
	
	private void InitResource() {
		// TODO Auto-generated method stub
		AdViewPublicId       = Get("AdViewPublicId",AdViewPublicId);
		AfterPatchPublicId   = Get("AfterPatchPublicId",AfterPatchPublicId);
		FloatLayoutPublicId  = Get("FloatLayoutPublicId",FloatLayoutPublicId);
		InterstitialPublicId = Get("InterstitialPublicId",InterstitialPublicId);
		MiniInterPublicId    = Get("MiniInterPublicId",MiniInterPublicId);
		PatchPublicId        = Get("PatchPublicId",PatchPublicId);
		PatchMiddlePublicId  = Get("PatchMiddlePublicId",PatchMiddlePublicId);
	}
	
	public String AdViewPublicId      = "8f311026597575a5d4e2a6ec944f08bc";
	public String AfterPatchPublicId  = "a7fdba4e22f6be35b664eb88db55816e";
	public String FloatLayoutPublicId = "d286067f946dba267ca925e12369f050";
	public String InterstitialPublicId= "bc42d0e425d2bcbbe24b05118e59fc07";
	public String MiniInterPublicId   = "22697118e5b186d936dc00f3179496a1";
	public String PatchPublicId       = "68e9d5eaac266769b8110abfca7bcb5c";
	public String PatchMiddlePublicId = "8a4282b9981149ad1a4b0d2d81fe9bcb";
	
	
	
	
	public String Get(String key,String defaultV){
		SharedPreferences sp = mContext.getSharedPreferences("PUBLISHERID", Context.MODE_PRIVATE);
		return sp.getString(key, defaultV);
	}
	public void Set(String key,String defaultV){
		SharedPreferences sp = mContext.getSharedPreferences("PUBLISHERID", Context.MODE_PRIVATE);
		sp.edit().putString(key, defaultV).commit();
	}
	
}
