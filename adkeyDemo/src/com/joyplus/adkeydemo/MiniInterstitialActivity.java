package com.joyplus.adkeydemo;

import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.AdManager;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.mini.AdMini;
import com.joyplus.adkey.widget.Log;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MiniInterstitialActivity extends Activity implements AdListener{

	private RelativeLayout layout;
	private String publisherId = "ec85b4cb90ec83bcf8097e207d2ea15a";
	private boolean animation = true;
    private AdMini mAdMini;
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mini);
		layout = (RelativeLayout) findViewById(R.id.adsdkContent);
	}
	
	public void onClickShowMini(View view) {
		removeMiniAd();
		mAdMini = new AdMini(this, publisherId,animation);
		mAdMini.SetAdListener(this);
		layout.addView(mAdMini);
	}

	private void removeMiniAd(){
		if(mAdMini!=null){
			layout.removeView(mAdMini);
			mAdMini = null;
		}
	}
	
	@Override
	public void adClicked(){
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"MiniInterstitialActivity--->adClicked");
	}

	@Override
	public void adClosed(Ad ad, boolean completed){
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"MiniInterstitialActivity--->adClosed");
	}

	@Override
	public void adLoadSucceeded(Ad ad){
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"MiniInterstitialActivity--->adLoadSucceeded");
	}

	@Override
	public void adShown(Ad ad, boolean succeeded){
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"MiniInterstitialActivity--->adShown");
	}

	@Override
	public void noAdFound(){
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"MiniInterstitialActivity--->noAdFound");
	}
}
