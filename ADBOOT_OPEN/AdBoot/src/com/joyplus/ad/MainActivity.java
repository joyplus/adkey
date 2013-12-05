package com.joyplus.ad;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.joyplus.ad.AdSDKManager.CUSTOMTYPE;
import com.joyplus.ad.R;
import com.joyplus.ad.application.AdBoot;
import com.joyplus.ad.application.AdBootInfo;
import com.joyplus.ad.application.AdBootManager;
import com.joyplus.ad.application.CUSTOMINFO;
import com.joyplus.ad.config.Log;
import com.joyplus.ad.data.ADBOOT;
import com.joyplus.ad.data.AdBootRequest;
import com.joyplus.ad.data.AdBootResponseHandler;
import com.joyplus.ad.data.RequestException;

import android.os.Bundle; 
import android.app.Activity;
import android.view.Menu; 
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private AdBoot mAdBoot;
	private AdBootManager mManager;
	private TextView      mTextView;
	private Button        mButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			AdSDKManager.Init(this,CUSTOMTYPE.CHANGHONG);
		} catch (AdSDKManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InitResource();
//		File kk = new File("/mnt/sdcard/demo.xml");
//		if(kk.exists()){
//			InputStream in = null;
//			ADBOOT      mADBOOT = null;
//			try {
//				in = new BufferedInputStream(new FileInputStream(kk));				
//				ADBOOT ad = Parser(in);
//				mADBOOT = ad;
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (RequestException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} finally {
//				if (in != null) {        
//					try {
//						in.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}      
//			    } 
//			}
//			if(mADBOOT != null)Log.d("Jas"," "+mADBOOT.toString());
//			else Log.d("Jas","mADBOOT == null !!! ");
//		}else Log.d("Jas","File no exit");
		
	}
//    private ADBOOT Parser(InputStream inputStream) throws RequestException{
//    	if(inputStream == null)return null;
//    	Log.d("Jas","55555");
//		try {
//			SAXParserFactory spf = SAXParserFactory.newInstance();
//			SAXParser sp = spf.newSAXParser();
//			XMLReader xr = sp.getXMLReader();
//			AdBootResponseHandler AdBootHandler = new AdBootResponseHandler();
//			xr.setContentHandler(AdBootHandler);
//			InputSource src = new InputSource(inputStream);
//			src.setEncoding(HttpManager.RESPONSE_ENCODING);
//			xr.parse(src);
//			return AdBootHandler.getADBOOT();
//		} catch (Exception e) { 
//			throw new RequestException("Cannot parse Response:"
//					+ e.getMessage(), e);
//		}
//    }
	private void InitResource() {
		// TODO Auto-generated method stub
		PublisherId id          = new PublisherId("0d017aa913b9e100e77bbf9401826ae3");
		AdBootInfo  mAdBootInfo = new AdBootInfo();
		CUSTOMINFO  mCUSTOMINFO = new CUSTOMINFO();
		mAdBootInfo.SetFirstImage("./mnt/sdcard/textfirstImageAAAA.jpg");
		mAdBootInfo.SetSecondImage("./mnt/sdcard/textSecondImageAAAA.jpg");
		mAdBootInfo.SetBootAnimationZip("./mnt/sdcard/textbootanimationAAAA.zip");
		mCUSTOMINFO.SetDEVICEMUMBER("AMLOGIC8726MX");
		
		mAdBoot  = new AdBoot(mCUSTOMINFO,mAdBootInfo,id);
		mManager = new AdBootManager(this,mAdBoot);
		Log.d(" "+mManager.GetAD().toString());
		mButton   = (Button) this.findViewById(R.id.button);
		mTextView = (TextView) this.findViewById(R.id.textView);
		mButton.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mManager.RequestAD();
			}
		});
		
		
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
