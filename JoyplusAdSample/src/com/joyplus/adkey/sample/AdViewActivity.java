package com.joyplus.adkey.sample;

import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.banner.AdView;
import com.joyplus.adkey.example.R;
import com.joyplus.adkey.widget.Log;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class AdViewActivity extends Activity implements AdListener
{
	private RelativeLayout layout;
	private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adview);
		layout = (RelativeLayout) findViewById(R.id.adsdkContent);
	}
	

	public void onClickShowBanner(View view) {
		if (mAdView != null) {
			removeBanner();
		}
		mAdView = new AdView(this, "http://adv.yue001.com/md.request.php",
		"152c965b2381d2ee18bdf3e729ccccf6", true, true);
		mAdView.setAdListener(this);
		layout.addView(mAdView);
	}

	private void removeBanner(){
		if(mAdView!=null){
			layout.removeView(mAdView);
			mAdView = null;
		}
	}
	
	@Override
	public void adClicked()
	{
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"AdViewActivity--->adClicked");
	}

	@Override
	public void adClosed(Ad ad, boolean completed)
	{
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"AdViewActivity--->adClosed");
	}

	@Override
	public void adLoadSucceeded(Ad ad)
	{
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"AdViewActivity--->adLoadSucceeded");
	}

	@Override
	public void adShown(Ad ad, boolean succeeded)
	{
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"AdViewActivity--->adShown");
	}

	@Override
	public void noAdFound()
	{
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"AdViewActivity--->noAdFound");
	}
	
}
