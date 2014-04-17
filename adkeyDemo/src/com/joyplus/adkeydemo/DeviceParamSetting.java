package com.joyplus.adkeydemo;

import java.util.List;

import com.joyplus.adkey.AdDeviceManager;
import com.joyplus.adkey.AdKeyConfig;
import com.joyplus.adkey.CUSTOMINFO;
import com.joyplus.adkey.Debug.AdKeyDebugFeature;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class DeviceParamSetting extends Activity implements OnClickListener{
    private EditText   mDM;
    private EditText   mDS;
    private EditText   mMAC;
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
		InitPublisher();
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
			SavePublisherId();
			finish();
			break;
		case R.id.button2:
			finish();
			break;
		}
	}
	
	
	private EditText Banner;
	private EditText AfterPatch; 
    private EditText FloatLayout;
    private EditText Interstitial;
    private EditText MiniInter;
    private EditText Patch;
    private EditText PatchMiddle;
    
    private void InitPublisher(){
    	PublisherIdManager mM = PublisherIdManager.GetInstance();
    	Banner = (EditText) this.findViewById(R.id.Banner);
    	Banner.setText(mM.AdViewPublicId);
    	
    	AfterPatch = (EditText) findViewById(R.id.AfterPatch);
    	AfterPatch.setText(mM.AfterPatchPublicId);
    	
    	FloatLayout = (EditText) findViewById(R.id.FloatLayout);
    	FloatLayout.setText(mM.FloatLayoutPublicId);
    	
    	Interstitial = (EditText) findViewById(R.id.Interstitial);
    	Interstitial.setText(mM.InterstitialPublicId);
    	
    	MiniInter = (EditText) findViewById(R.id.MiniInter);
    	MiniInter.setText(mM.MiniInterPublicId);
    	
    	Patch = (EditText) findViewById(R.id.Patch);
    	Patch.setText(mM.PatchPublicId);
    	
    	PatchMiddle = (EditText) findViewById(R.id.PatchMiddle);
    	PatchMiddle.setText(mM.PatchMiddlePublicId);
    	
    	InitRequest();
    }
	
    private void SavePublisherId(){
    	PublisherIdManager mM = PublisherIdManager.GetInstance();
    	mM.Set("AdViewPublicId", Banner.getText().toString().trim());
    	mM.Set("AfterPatchPublicId", AfterPatch.getText().toString().trim());
    	mM.Set("FloatLayoutPublicId", FloatLayout.getText().toString().trim());
    	mM.Set("InterstitialPublicId", Interstitial.getText().toString().trim());
    	mM.Set("MiniInterPublicId", MiniInter.getText().toString().trim());
    	mM.Set("PatchPublicId", Patch.getText().toString().trim());
    	mM.Set("PatchMiddlePublicId", PatchMiddle.getText().toString().trim());
    	
    	SaveRequest();
    }
	
	
    
    
    private void InitRequest(){
    	Spinner request = (Spinner) findViewById(R.id.request);
    	List<String> requestUrl = AdKeyConfig.getInstance().GetDebugBaseUrl();
    	if(requestUrl == null || requestUrl.size()<=0){
    		request.setVisibility(View.GONE);
    		return;
    	}
    	PublisherIdManager mM = PublisherIdManager.GetInstance();
    	request.setVisibility(View.VISIBLE);
    	String[] URLS = new String[requestUrl.size()];
    	int i = 0;
    	for(String url : requestUrl){
    		URLS[i++] = url;
    	}
    	AdKeyConfig.getInstance().SetDEMOTEST(mM.Get("REQUESTURL", AdKeyConfig.getInstance().getREQUESTURL()));
    	request.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,URLS));
    	request.setSelection(requestUrl.indexOf(mM.Get("REQUESTURL", AdKeyConfig.getInstance().getREQUESTURL())));
    }
    
    private void SaveRequest(){
    	Spinner request = (Spinner) findViewById(R.id.request);
    	if(request.getVisibility() != View.VISIBLE)return;
    	List<String> requestUrl = AdKeyConfig.getInstance().GetDebugBaseUrl();
    	PublisherIdManager mM = PublisherIdManager.GetInstance();
    	mM.Set("REQUESTURL", requestUrl.get(request.getSelectedItemPosition()));
    }
    
    
}
