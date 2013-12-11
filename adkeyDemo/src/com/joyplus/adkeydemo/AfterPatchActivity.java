package com.joyplus.adkeydemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.joyplus.adkey.Ad;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.AdPatchManager;
import com.joyplus.adkey.widget.Log;

public class AfterPatchActivity extends Activity implements AdListener{

	private FrameLayout layout;
	private AdPatchManager mManager    = null;
	private String publisherId         = "5c82ff3b88e03a7f14eefa3965fade7e";//要显示广告的publisherId
	private boolean cacheMode          = true;//该广告加载时是否用本地缓存
	
	private Spinner   mFirst;
	private Spinner   mSecond;
	private final   static int MOVIE   = 1;
	private final   static int TV      = 2;
	private final   static int ZONGYI  = 3;
	private final   static int DONGMAN = 4;
	private String  VC         = "401";
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patch);
		layout = (FrameLayout) findViewById(R.id.adsdkContent);
		InitFirst();
	}
	private void InitSecond() {
		// TODO Auto-generated method stub
		mSecond = (Spinner) this.findViewById(R.id.spinner2);
		String[] m ;
		if(mFirst.getSelectedItemPosition()==(MOVIE-1)){
			m = VideoType.GetVideo_MOVIE();
		}else if(mFirst.getSelectedItemPosition()==(TV-1)){
			m = VideoType.GetVideo_TV();
		}else if(mFirst.getSelectedItemPosition()== (ZONGYI-1)){
			m = VideoType.GetVideo_ZONGYI();
		}else if(mFirst.getSelectedItemPosition()== (DONGMAN-1)){
			m = VideoType.GetVideo_DONGMAN();
		}else{
			mFirst.setSelection(0);
			return;
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);
		mSecond.setAdapter(adapter);
		mSecond.setSelection(0);
	}
	public void onClickShowPatch(View v){
		GetVC();
		//Toast.makeText(this, VC, Toast.LENGTH_SHORT).show();
		if(mManager!=null)
			mManager.release();
		mManager = new AdPatchManager(this,publisherId,cacheMode,layout);
		mManager.SetVC(VC);
		mManager.setListener(this);
		mManager.requestAd();
	}
	private void GetVC(){
		int vc = (int) ((mFirst.getSelectedItemId()+1)*100);
		vc +=(mSecond.getSelectedItemPosition()+1);
		if(VideoType.GetType(vc) != null){
			VC = Integer.toString(vc);
		}
	}
	private void InitFirst(){
		mFirst = (Spinner) this.findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,VideoType.GetVideoItems());
		mFirst.setAdapter(adapter);
		mFirst.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				InitSecond();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				mFirst.setSelection(0);
			}
		});
		mFirst.setSelection(0);
	}
	
	@Override
	public void adClicked()
	{
		// TODO Auto-generated method stub
		Log.d("Jas","adClicked");
	}

	@Override
	public void adClosed(Ad ad, boolean completed)
	{
		// TODO Auto-generated method stub
		Log.d("Jas","adClosed");
	}

	@Override
	public void adLoadSucceeded(Ad ad)
	{
		// TODO Auto-generated method stub
		Log.d("Jas","adLoadSucceeded");
		if (mManager != null && mManager.isAdLoaded())
			mManager.showAd();
	}

	@Override
	public void adShown(Ad ad, boolean succeeded)
	{
		// TODO Auto-generated method stub
		Log.d("Jas","adLoadSucceeded");
	}

	@Override
	public void noAdFound()
	{
		// TODO Auto-generated method stub
		Toast.makeText(AfterPatchActivity.this, "No ad found!", Toast.LENGTH_LONG)
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
	
	private void CreateDialog(){
		
	}
}