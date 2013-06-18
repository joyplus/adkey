package com.joyplus.adkey.sample;

import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.AdManager;
import com.joyplus.adkey.example.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class InterstitialActivity extends Activity implements AdListener
{
	private AdManager mManager = null;
	private String publisherId = "038ec9c3d97315b24be739b204f0ea07";//要显示广告的publisherId
	private boolean cacheMode = false;//该广告加载时是否用本地缓存
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interstitial);
	}
	
	public void onClickShowVideoOnline(View v){
		if(mManager!=null)
			mManager.release();
		
		mManager = new AdManager(this,publisherId,cacheMode);
		mManager.setListener(this);
		mManager.requestAd();
	}
	
	public void onClickShowVideoCache(View v){
		if(mManager!=null)
			mManager.release();
		if(!cacheMode)
			cacheMode = true;
		mManager = new AdManager(this,publisherId,cacheMode);
		mManager.setListener(this);
		mManager.requestAd();
	}
	
	@Override
	public void adClicked()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adClosed(Ad ad, boolean completed)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adLoadSucceeded(Ad ad)
	{
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (mManager != null && mManager.isAdLoaded())
			mManager.showAd();
	}

	@Override
	public void adShown(Ad ad, boolean succeeded)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void noAdFound()
	{
		// TODO Auto-generated method stub
		Toast.makeText(InterstitialActivity.this, "No ad found!", Toast.LENGTH_LONG)
			.show();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mManager!=null)
			mManager.release();
	}
}
