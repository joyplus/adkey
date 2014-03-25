package com.joyplus.ad.html5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.HttpAuthHandler;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.joyplus.ad.test.R;
import com.joyplus.ad.test.util.Log;

public class Html5DemoActivity extends Activity {
    
	private RelativeLayout mRoot;
	private ProgressBar mProgressBar;
	private WebView     mWebView;
	private String mBaseUrl = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBaseUrl = getIntent().getStringExtra("url");
		setContentView(R.layout.activity_html5demo);
		mRoot = (RelativeLayout) this.findViewById(R.id.root);
		mProgressBar = (ProgressBar) this.findViewById(R.id.progressBar1); 
		mProgressBar.setMax(100);
		InitWebView(this);
		mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");
//		mWebView.loadUrl("file:///android_asset/index.html");
		mWebView.loadUrl(mBaseUrl);
		
		
	}
	public static final String HIDE_BORDER = "<style>* { -webkit-tap-highlight-color: rgba(0,0,0,0);} img {width:100%;height:100%}</style>";
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.action_settings:
			
			break;
		}
		return super.onOptionsItemSelected(item); 
	}
   
	 
	private void SetProgess(final int newProgress){
		Html5DemoActivity.this.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(newProgress<=0 || newProgress>=100){
					mProgressBar.setVisibility(View.GONE);
				}else{
					mProgressBar.setProgress(newProgress);
					mProgressBar.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	private void InitWebView(Context context){
		mWebView = new WebView(context);
		InitWebViewConfig();
		LinearLayout.LayoutParams params = new 
				LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		params.gravity = Gravity.CENTER;
		mWebView.setLayoutParams(params);
		mRoot.addView(mWebView, params);
	}

	private void InitWebViewConfig() {
		// TODO Auto-generated method stub
		mWebView.setFocusable(true);
		mWebView.requestFocus();
		//mWebView.setFocusableInTouchMode(false);
		mWebView.setBackgroundColor(Color.TRANSPARENT);
		mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
	    mWebView.getSettings().setSupportZoom(true);
	    mWebView.getSettings().supportMultipleWindows();
	    mWebView.getSettings().setJavaScriptEnabled(true);
	    mWebView.clearView();
	    //mWebView.setc
	    mWebView.setWebChromeClient(new CustomWebChromeClient());
	    mWebView.setWebViewClient(new WebViewClient());
	}
	
	class CustomWebViewClient extends WebViewClient{

		@Override
		public void doUpdateVisitedHistory(WebView view, String url,
				boolean isReload) {
			// TODO Auto-generated method stub
			Log.d("Jas","doUpdateVisitedHistory");
			super.doUpdateVisitedHistory(view, url, isReload);
		}

		@Override
		public void onFormResubmission(WebView view, Message dontResend,
				Message resend) {
			// TODO Auto-generated method stub
			Log.d("Jas","onFormResubmission");
			Toast.makeText(Html5DemoActivity.this, "²»¿ÉÖØžŽÌáœ»£¬°ŽBack»ØµœÉÏŒ¶ÍøÒ³",
                    Toast.LENGTH_SHORT).show();
			//super.onFormResubmission(view, dontResend, resend);
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			// TODO Auto-generated method stub
			Log.d("Jas","onLoadResource");
			super.onLoadResource(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			Log.d("Jas","onPageFinished");
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			Log.d("Jas","onPageStarted");
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			Log.d("Jas","onReceivedError");
			Toast.makeText(Html5DemoActivity.this, "ÍøÒ³ŽíÎó: "+errorCode+" ÍøÒ³²»¿ÉÓÃ",
                    Toast.LENGTH_LONG).show();
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onReceivedHttpAuthRequest(WebView view,
				HttpAuthHandler handler, String host, String realm) {
			// TODO Auto-generated method stub
			Log.d("Jas","onReceivedHttpAuthRequest");
			super.onReceivedHttpAuthRequest(view, handler, host, realm);
		}

		@Override
		public void onReceivedLoginRequest(WebView view, String realm,
				String account, String args) {
			// TODO Auto-generated method stub
			Log.d("Jas","onReceivedLoginRequest");
			super.onReceivedLoginRequest(view, realm, account, args);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			// TODO Auto-generated method stub
			Log.d("Jas","onReceivedSslError");
			super.onReceivedSslError(view, handler, error);
		}

		@Override
		public void onScaleChanged(WebView view, float oldScale, float newScale) {
			// TODO Auto-generated method stub
			Log.d("Jas","onScaleChanged");
			super.onScaleChanged(view, oldScale, newScale);
		}

		@Override
		@Deprecated
		public void onTooManyRedirects(WebView view, Message cancelMsg,
				Message continueMsg) {
			// TODO Auto-generated method stub
			Log.d("Jas","onTooManyRedirects");
			super.onTooManyRedirects(view, cancelMsg, continueMsg);
		}

		@Override
		public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
			// TODO Auto-generated method stub
			Log.d("Jas","onUnhandledKeyEvent");
			super.onUnhandledKeyEvent(view, event);
		}

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view,
				String url) {
			// TODO Auto-generated method stub
			Log.d("Jas","WebResourceResponse");
			return super.shouldInterceptRequest(view, url);
		}

		@Override
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			// TODO Auto-generated method stub
			Log.d("Jas","shouldOverrideKeyEvent");
			return super.shouldOverrideKeyEvent(view, event);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			Log.d("Jas","shouldOverrideUrlLoading url-->"+url);
			view.loadUrl(url);
			return true;
			//return super.shouldOverrideUrlLoading(view, url);
		}
	}
    
	private class CustomWebChromeClient extends WebChromeClient{
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			Log.d("Jas","onProgressChanged-->"+newProgress);
			SetProgess(newProgress);
			super.onProgressChanged(view, newProgress);
		}
		@Override  
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {  
            Log.d("Jas", "onJsAlert-->"+message);  
            Toast.makeText(Html5DemoActivity.this, "--"+message, Toast.LENGTH_LONG).show();
            result.confirm();   
            return true;  
        }  
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d("Jas","onKeyDown -->"+keyCode+","+KeyEvent.keyCodeToString(keyCode));
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			if(mWebView != null && mWebView.canGoBack()){
				mWebView.goBack();
				return true;
			}
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	private Handler mHandler = new Handler(){};
	final class DemoJavaScriptInterface {  
        DemoJavaScriptInterface() {  
        }  
        public void clickOnAndroid(final String aaq) {        // ×¢ÒâÕâÀïµÄÃû³Æ¡£ËüÎªclickOnAndroid(),×¢Òâ£¬×¢Òâ£¬ÑÏÖØ×¢Òâ  
            mHandler.post(new Runnable() {  
                public void run() {  
                    // ŽËŽŠµ÷ÓÃ HTML ÖÐµÄjavaScript º¯Êý  
                	Log.d("Jas","clickOnAndroid========="+aaq);
                    mWebView.loadUrl("javascript:wave()"); 
                    StartYUE();
                }   
            });  
        }  
	}
	
	private void StartYUE(){
		Intent intent = new Intent();
		intent.setPackage("com.joyplus.tv");
		intent.setClassName("com.joyplus.tv", "com.joyplus.tv.Main");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
		try{
		    this.startActivity(intent);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}