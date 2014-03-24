package com.joyplus.ad.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.joyplus.ad.html5.Html5DemoActivity;

public class MainActivity extends Activity implements OnClickListener{

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
		switch (v.getId()) {
		case R.id.button1:
			startActivity(new Intent(this, Html5DemoActivity.class));
			break;
		case R.id.button2:
			startActivity(new Intent(this, GroupListActivity.class));
			break;
		default:
			break;
		}
	}
	
	
}
