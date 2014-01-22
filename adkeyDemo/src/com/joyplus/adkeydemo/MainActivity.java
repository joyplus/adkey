package com.joyplus.adkeydemo;


import com.joyplus.adkey.mraid.MraidActivity;

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
	
	public void onClickShowPatchVideo(View v){
		Intent intent = new Intent(MainActivity.this,PatchActivity.class);
		startActivity(intent);
	}
	
	public void onClickShowAfterPatchVideo(View v){
		Intent intent = new Intent(MainActivity.this,AfterPatchActivity.class);
		startActivity(intent);
	}
	
	public void onClickShowMiddlePatchVideo(View v){
		Intent intent = new Intent(MainActivity.this,PatchMiddleActivity.class);
		startActivity(intent);
	}
	
	public void onClickShowMini(View v){
		Intent intent = new Intent(MainActivity.this,MiniInterstitialActivity.class);
		startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		Intent intent = new Intent(MainActivity.this,MraidActivity.class);
//		startActivity(intent);
	}
}
