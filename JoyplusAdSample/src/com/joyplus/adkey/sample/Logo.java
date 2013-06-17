package com.joyplus.adkey.sample;

import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.AdManager;
import com.joyplus.adkey.banner.AdView;
import com.joyplus.adkey.example.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Logo extends Activity implements AdListener{
	private RelativeLayout layout;
	private AdView mAdView;
	private AdManager mManager;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示标题
		setContentView(R.layout.logo);// 显示welcom.xml
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏显示
		
		layout = (RelativeLayout) findViewById(R.id.adsdkContent);
		
		final Intent intent = new Intent(Logo.this, MainActivity.class);// AndroidMainScreen为主界面
		startActivity(intent);
		Logo.this.finish();
//		mManager = new AdManager(this, "http://adv.yue001.com/md.request.php",
//				"038ec9c3d97315b24be739b204f0ea07", true);
		
//		mManager.setListener(this);
//		mManager.requestAd();
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
		Toast.makeText(Logo.this, "广告点击事件", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void adClosed(Ad ad, boolean completed)
	{
		// TODO Auto-generated method stub
		Toast.makeText(Logo.this, "关闭了", Toast.LENGTH_SHORT).show();
		final Intent intent = new Intent(Logo.this, MainActivity.class);// AndroidMainScreen为主界面
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
		Toast.makeText(Logo.this, "广告显示事件", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void noAdFound()
	{
		// TODO Auto-generated method stub
		Toast.makeText(Logo.this, "No ad found!", Toast.LENGTH_LONG).show();
		
		Intent intent = new Intent(Logo.this, MainActivity.class);// AndroidMainScreen为主界面
		startActivity(intent);
		finish();
	}
	
	
}