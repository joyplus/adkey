package com.joyplus.adkey.sample;

import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.AdSmallManager;
import com.joyplus.adkey.example.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class SmallActivity extends Activity implements AdListener
{
	private FrameLayout layout;
	private AdSmallManager mManager = null;
	private String publisherId = "dabb5c604c9dd34e4ee519c9c2fdcd52";//要显示广告的publisherId
	private boolean cacheMode = true;//该广告加载时是否用本地缓存
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.small);
		layout = (FrameLayout) findViewById(R.id.adsdkContent);
		
		if(mManager!=null)
			mManager.release();
		
		mManager = new AdSmallManager(this,publisherId,cacheMode,layout);
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
		Toast.makeText(SmallActivity.this, "No ad found!", Toast.LENGTH_LONG)
			.show();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mManager!=null)
			mManager.release();
	}
	
	public void OnClose(View v){
		if(mManager!=null)
			mManager.release();
//		finish();
	}
	
}
