package com.joyplus.adkey.sample;

import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.banner.AdViewScreenSaver;
import com.joyplus.adkey.example.R;
import com.joyplus.adkey.widget.Log;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ScreenSaverActivity extends Activity implements AdListener
{
	private RelativeLayout layout;
	private AdViewScreenSaver mAdView;
	private String publisherId = "b7a59bdb75c2e6341e6dfac70b68b709";//要显示广告的publisherId
	private boolean animation = true;//该广告加载时是否用动画效果
	private ImageView image = null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adviewscreensaver);
		layout = (RelativeLayout) findViewById(R.id.adsdkContent);
		image = (ImageView)findViewById(R.id.image);
		
		if (mAdView != null) {
			removeBanner();
		}
		mAdView = new AdViewScreenSaver(this, publisherId,animation);
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
		Log.i(Const.TAG,"ScreenSaverActivity--->adClicked");
	}

	@Override
	public void adClosed(Ad ad, boolean completed)
	{
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"ScreenSaverActivity--->adClosed");
	}

	@Override
	public void adLoadSucceeded(Ad ad)
	{
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"ScreenSaverActivity--->adLoadSucceeded");
		image.setVisibility(View.GONE);
	}

	@Override
	public void adShown(Ad ad, boolean succeeded)
	{
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"ScreenSaverActivity--->adShown");
		image.setVisibility(View.GONE);
	}

	@Override
	public void noAdFound()
	{
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"ScreenSaverActivity--->noAdFound");
	}
}
