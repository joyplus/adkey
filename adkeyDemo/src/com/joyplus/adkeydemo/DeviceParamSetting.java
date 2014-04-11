package com.joyplus.adkeydemo;

import com.joyplus.adkey.AdDeviceManager;
import com.joyplus.adkey.CUSTOMINFO;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class DeviceParamSetting extends Activity implements OnClickListener{
    private EditText mDM;
    private EditText mDS;
    private EditText mMAC;
    private CUSTOMINFO mCUSTOMINFO;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.devicesettingparam);
		InitResource();
	}

	private void InitResource() {
		// TODO Auto-generated method stub
		mDM  = (EditText) findViewById(R.id.dm);
		mDS  = (EditText) findViewById(R.id.ds);
		mMAC = (EditText) findViewById(R.id.i);
		mCUSTOMINFO = AdDeviceManager.getInstance(this).GetCUSTOMINFO();
		if(mCUSTOMINFO == null){
			mCUSTOMINFO = new CUSTOMINFO();
		}else{
			mCUSTOMINFO = mCUSTOMINFO.CreateNew();
		}
		mDM.setText(mCUSTOMINFO.GetDEVICEMOVEMENT());
		mDS.setText(mCUSTOMINFO.GetDEVICEMUMBER());
		mMAC.setText(mCUSTOMINFO.GetMAC());
		findViewById(R.id.button1).setOnClickListener(this);
		findViewById(R.id.button2).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.button1:
			mCUSTOMINFO.SetDEVICEMOVEMENT(mDM.getText().toString().trim());
			mCUSTOMINFO.SetDEVICEMUMBER(mDS.getText().toString().trim());
			mCUSTOMINFO.SetMAC(mMAC.getText().toString().trim());
			AdDeviceManager.getInstance(DeviceParamSetting.this).SetCUSTOMINFO(mCUSTOMINFO);
			finish();
			break;
		case R.id.button2:
			finish();
			break;
		}
	}
    
	
	
	
}
