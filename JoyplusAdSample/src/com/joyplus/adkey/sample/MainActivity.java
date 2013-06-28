package com.joyplus.adkey.sample;

import com.joyplus.adkey.example.R;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity
{
	
	public void onClickShowBanner(View view) {
		Intent intent = new Intent(MainActivity.this,AdViewActivity.class);
		startActivity(intent);
	}

	public void onClickShowVideoInterstitial(View v) {
		Intent intent = new Intent(MainActivity.this,InterstitialActivity.class);
		startActivity(intent);
	}
	
	public void onClickSmallWindow(View v){
		Intent intent = new Intent(MainActivity.this,SmallActivity.class);
		startActivity(intent);
	}
	
	public void onClickScreenSaver(View v){
		Intent intent = new Intent(MainActivity.this,ScreenSaverActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
}
