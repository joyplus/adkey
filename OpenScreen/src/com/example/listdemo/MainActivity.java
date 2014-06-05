package com.example.listdemo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.simple);
//		try{
//			WebView mWebView = (WebView) this.findViewById(R.id.webView1);
//			mWebView.getSettings().setJavaScriptEnabled(true);
//			mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//			mWebView.setBackgroundColor(Color.TRANSPARENT);
//			mWebView.setHorizontalScrollBarEnabled(false);
//			mWebView.setVerticalScrollBarEnabled(false);
//			String base = "file:///android_asset/";
//			String data = ("<html><center><img src=\""+getImage()+"\"></center></html>"); 
//			mWebView.loadDataWithBaseURL(base,data,"text/html","UTF-8","");
//			//mWebView.loadUrl(getImage());
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}
	private static int count = 0;
	private String getImage(){
		count = (++count)/2;
		switch(count){
		case 0:return "s1.gif";
		case 1:return "s2.gif";
		}
		return "s1.gif";
	}

}
