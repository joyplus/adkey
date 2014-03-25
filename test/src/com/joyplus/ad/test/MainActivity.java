package com.joyplus.ad.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.joyplus.ad.html5.Html5DemoActivity;

public class MainActivity extends Activity implements OnClickListener{

	private static final String TAG = MainActivity.class.getSimpleName();
	
	private static final String html5BaseUrl = "http://new.transtrouvere.com/html5/";
	private static final String nativeBaseUrl = "http://advapi.yue001.com/advapi/v1/topic/list?bid=zino";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button btn1 = (Button) findViewById(R.id.button1);
		Button btn2 = (Button) findViewById(R.id.button2);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (v.getId()) {
		case R.id.button1:
			intent = new Intent(this, Html5DemoActivity.class); 
			intent.putExtra("url", html5BaseUrl);
			startActivity(intent);
			break;
		case R.id.button2:
			intent = new Intent(this, GroupListActivity.class);
			intent.putExtra("url", nativeBaseUrl);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	
}
