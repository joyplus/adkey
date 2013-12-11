package com.joyplus.adkeydemo;

import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.banner.AdView;
import com.joyplus.adkey.widget.Log;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class PatchMiddleActivity extends Activity implements AdListener
{
	private RelativeLayout layout;
	private AdView mAdView;
	private String publisherId = "af24620a7bf84a87923ec3491f11f004";//瑕佹樉绀哄箍鍛婄殑publisherId
	private boolean animation = true;//璇ュ箍鍛婂姞杞芥椂鏄惁鐢ㄥ姩鐢绘晥鏋�

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patchmiddle);
		layout = (RelativeLayout) findViewById(R.id.adsdkContent);
	}
	

	public void onClickShowPatchMiddle(View view) {
		if (mAdView != null) {
			removeBanner();
		}
		mAdView = new AdView(this, publisherId,animation);
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
