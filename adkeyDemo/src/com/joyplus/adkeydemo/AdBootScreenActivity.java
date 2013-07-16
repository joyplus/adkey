package com.joyplus.adkeydemo;

import java.io.File;

import com.joyplus.adkey.AdBootScreenListener;
import com.joyplus.adkey.AdBootScreenManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;


public class AdBootScreenActivity extends Activity{
    

	//private String PUBLISHERID = "3156ccf24a9f8024c34ac0c60e78952f";
	private String PUBLISHERID  =  "c23a1ed44ef1b264a935d4c3a1c3232b";
	private TextView mView ;
	private AdBootScreenManager mManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 涓嶆樉绀烘爣棰�
		setContentView(R.layout.adbootscreen);// 鏄剧ずwelcom.xml
		InitSource();
	}
	private void InitSource() {
		// TODO Auto-generated method stub
		InitManager();
	}

	private void InitManager() {
		// TODO Auto-generated method stub
		try {
			mManager = new AdBootScreenManager(AdBootScreenActivity.this,PUBLISHERID,true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		} 
		mManager.setListener(new Listener());
		mManager.UpdateAdvert();
	} 
	
	private class Listener implements AdBootScreenListener{

		@Override
		public void Closed() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void ReportFail() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void ReportSuccessed() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void adLoadSucceeded() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void noAdFound() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
