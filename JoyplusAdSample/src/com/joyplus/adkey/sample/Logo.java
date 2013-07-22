package com.joyplus.adkey.sample;

import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.AdManager;
import com.joyplus.adkey.example.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Logo extends Activity implements AdListener{
	private AdManager mManager;
//	private String publisherId = "03f9d5d1ead2ac506a91f2c0f5c21d46";//瑕佹樉绀哄箍鍛婄殑publisherId
	private String publisherId = "4083958c3dc3360dafe7287ae997070c";
	private boolean cacheMode = true;//璇ュ箍鍛婂姞杞芥椂鏄惁鐢ㄦ湰鍦扮紦瀛�
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 涓嶆樉绀烘爣棰�
		setContentView(R.layout.logo);// 鏄剧ずwelcom.xml
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 璁剧疆鍏ㄥ睆鏄剧ず
		
		
//		final Intent intent = new Intent(Logo.this, MainActivity.class);// AndroidMainScreen涓轰富鐣岄潰
//		startActivity(intent);
//		Logo.this.finish();
		mManager = new AdManager(this,publisherId,cacheMode);
		mManager.setListener(this);
		mManager.requestAd();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mManager!=null)
			mManager.release();
	}

	@Override
	public void adClicked()
	{
		// TODO Auto-generated method stub
		Toast.makeText(Logo.this, "adClicked", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void adClosed(Ad ad, boolean completed)
	{
		// TODO Auto-generated method stub
		Toast.makeText(Logo.this,"adClosed", Toast.LENGTH_SHORT).show();
		final Intent intent = new Intent(Logo.this, MainActivity.class);// AndroidMainScreen涓轰富鐣岄潰
		startActivity(intent);
		Logo.this.finish();
	}

	@Override
	public void adLoadSucceeded(Ad ad)
	{
		// TODO Auto-generated method stub
		if (mManager != null && mManager.isAdLoaded())
			mManager.showAd();
	}

	@Override
	public void adShown(Ad ad, boolean succeeded)
	{
		// TODO Auto-generated method stub
		Toast.makeText(Logo.this, "adShown", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void noAdFound()
	{
		// TODO Auto-generated method stub
		Toast.makeText(Logo.this, "No ad found!", Toast.LENGTH_LONG).show();
		
		Intent intent = new Intent(Logo.this, MainActivity.class);// AndroidMainScreen涓轰富鐣岄潰
		startActivity(intent);
		finish();
	}
	
	// 杩斿洖閿�
		@Override
		public boolean dispatchKeyEvent(KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& event.getRepeatCount() == 0) {
					if(mManager!=null)
						mManager.release();
				}
			}
			return super.dispatchKeyEvent(event);
		}
}