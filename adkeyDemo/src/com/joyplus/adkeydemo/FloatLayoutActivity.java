package com.joyplus.adkeydemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.floatlayout.FloatLayout;
import com.joyplus.adkey.widget.Log;

public class FloatLayoutActivity extends Activity implements AdListener{

	private RelativeLayout layout;
    private FloatLayout mFloatLayout;
    private PublisherIdManager mM;
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mM = PublisherIdManager.GetInstance();
		setContentView(R.layout.mini);
		layout = (RelativeLayout) findViewById(R.id.adsdkContent);
	}
	
	public void onClickShowMini(View view) {
		if(mFloatLayout != null){
			mFloatLayout.Stop();
			mFloatLayout = null;
		}
		mFloatLayout = new FloatLayout(this, mM.FloatLayoutPublicId,layout,300,300);
		mFloatLayout.SetAdListener(this);
		mFloatLayout.requestAd();
	}

	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(mFloatLayout != null){
			mFloatLayout.Stop();
			mFloatLayout = null;
		}
	}

	@Override
	public void adClicked(){
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"FloatLayoutActivity--->adClicked");
	}

	@Override
	public void adClosed(Ad ad, boolean completed){
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"FloatLayoutActivity--->adClosed");
	}

	@Override
	public void adLoadSucceeded(Ad ad){
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"FloatLayoutActivity--->adLoadSucceeded");
	}

	@Override
	public void adShown(Ad ad, boolean succeeded){
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"FloatLayoutActivity--->adShown");
	}

	@Override
	public void noAdFound(){
		// TODO Auto-generated method stub
		Log.i(Const.TAG,"FloatLayoutActivity--->noAdFound");
	}
}
