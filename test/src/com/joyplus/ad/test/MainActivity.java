package com.joyplus.ad.test;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.joyplus.ad.html5.Html5DemoActivity;

public class MainActivity extends Activity{

	private static final String TAG = MainActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d(TAG, "data --> " + getIntent().getStringExtra("data"));
		try {
			handleData(getIntent().getStringExtra("data"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			finish();
		}
	}

	private void handleData(String data) throws Exception{
		JSONObject json = new JSONObject(data);
		int type = json.getInt("type");
		Intent intent = null;
		switch (type) {
		case 0:
			intent = new Intent(this, Html5DemoActivity.class); 
			intent.putExtra("url", json.getString("url"));
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(this, OpenActivity.class);
			intent.putExtra("url", json.getString("url"));
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(this, MovieListActivity.class);
			intent.putExtra("url", json.getString("url"));
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
}
