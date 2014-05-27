package com.joyplus.adkeydemo;

import static com.joyplus.adkey.Const.RESPONSE_ENCODING;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.joyplus.adkey.AdKeySDKManager;
import com.joyplus.adkey.AdKeySDKManagerException;
import com.joyplus.adkey.BannerAd;
import com.joyplus.adkey.RequestBannerAd;
import com.joyplus.adkey.RequestException;
import com.joyplus.adkey.video.ResponseHandler;
import com.joyplus.adkey.video.RichMediaAd;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;

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
	public void onClickShowFloatLayout(View v){
		Intent intent = new Intent(MainActivity.this,FloatLayoutActivity.class);
		startActivity(intent);
	}
	
	public void onClickDeviceParam(View v){
		Intent intent = new Intent(MainActivity.this,DeviceParamSetting.class);
		startActivityForResult(intent, 0);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		try {
			AdKeySDKManager.Init(this.getApplicationContext());
		} catch (AdKeySDKManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		setContentView(R.layout.activity_main);
		PublisherIdManager.Init(MainActivity.this.getApplicationContext());
		//for Init Device state
//		CUSTOMINFO info = new CUSTOMINFO();
//		info.SetDEVICEMOVEMENT("JOYPLUS_TEST");
//		info.SetDEVICEMUMBER("JOYPLUS_TEST");
//		info.SetMAC("01:02:03:04:05:06");
//		AdDeviceManager.getInstance(this).SetCUSTOMINFO(info);
		Test();
	}
	
	
	private void Test(){
		try {
			AssetManager am = this.getAssets();
			RequestBannerAd AD = new RequestBannerAd();
			BannerAd ad = AD.parse(am.open("banner.xml"), false);
			Log.d("Jas","BannerAd-->"+ad==null?"null":ad.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("Jas","IOException-->"+e.toString());
			e.printStackTrace();
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			Log.d("Jas","IOException-->"+e.toString());
			e.printStackTrace();
		}
	}
}
