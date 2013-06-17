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
	private RelativeLayout layout = null;
	private AdManager mManager = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interstitial);
		layout = (RelativeLayout) findViewById(R.id.adsdkContent);
	}
	
	public void onClickShowVideoOnline(View v){
		if(mManager!=null)
			mManager.release();
		
		mManager = new AdManager(this, "http://adv.yue001.com/md.request.php",
				"038ec9c3d97315b24be739b204f0ea07", true, false);
		mManager.setListener(this);
		mManager.requestAd();
	}
	
	public void onClickShowVideoCache(View v){
		if(mManager!=null)
			mManager.release();
		
		mManager = new AdManager(this, "http://adv.yue001.com/md.request.php",
				"038ec9c3d97315b24be739b204f0ea07", true, true);
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
