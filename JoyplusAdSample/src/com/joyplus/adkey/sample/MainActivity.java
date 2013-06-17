package com.joyplus.adkey.sample;


import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.AdManager;
import com.joyplus.adkey.banner.AdView;
import com.joyplus.adkey.example.R;
import com.joyplus.adkey.mraid.MraidView;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
// implements AdListener 
public class MainActivity extends Activity
{
//	private RelativeLayout layout;
//	private AdView mAdView;
//	private AdManager mManager;
	
	public void onClickShowBanner(View view) {
//		if (mAdView != null) {
//			removeBanner();
//		}
//		mAdView = new AdView(this, "http://adv.yue001.com/md.request.php",
//		"038ec9c3d97315b24be739b204f0ea07", true, true);
//		mAdView.setAdListener(this);
//		layout.addView(mAdView);
		Intent intent = new Intent(MainActivity.this,AdViewActivity.class);
		startActivity(intent);
	}

//	private void removeBanner(){
//		if(mAdView!=null){
//			
//			layout.removeView(mAdView);
//			mAdView = null;
//		}
//	}

	public void onClickShowVideoInterstitial(View v) {
//		mManager.requestAd();
		Intent intent = new Intent(MainActivity.this,InterstitialActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		layout = (RelativeLayout) findViewById(R.id.adsdkContent);
//		mManager = new AdManager(this, "http://adv.yue001.com/md.request.php",
//				"038ec9c3d97315b24be739b204f0ea07", true);
//		mManager.setListener(this);
	}

//	@Override
//	public void adClicked()
//	{
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void adClosed(Ad ad, boolean completed)
//	{
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void adLoadSucceeded(Ad ad)
//	{
//		// TODO Auto-generated method stub
//		if (mManager != null && mManager.isAdLoaded())
//			mManager.showAd();
//	}
//
//	@Override
//	public void adShown(Ad ad, boolean succeeded)
//	{
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void noAdFound()
//	{
//		// TODO Auto-generated method stub
//		Toast.makeText(MainActivity.this, "No ad found!", Toast.LENGTH_LONG)
//		.show();
//	}
//	
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		mManager.release();
//		if(mAdView!=null)
//			mAdView.release();
//	}
}
