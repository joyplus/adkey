package com.joyplus.adkey.floatlayout;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.BannerAd;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.Util.TranslateAnimationType;
import com.joyplus.adkey.banner.InAppWebView;
import com.joyplus.adkey.data.ClickType;

public class FloatView extends RelativeLayout {

	private   boolean       mAnimation;
	private   BannerAd      mResponse;
	private   WebSettings   mWebSettings;
	private   Context       mContext = null;
	private   WebView       mfirstWebView;
	private   AdListener    mAdListener;
	private   static Method mWebView_SetLayerType;
	private   static Field  mWebView_LAYER_TYPE_SOFTWARE;

	private   Handler mHandler = new Handler();

	public FloatView(final Context context, final BannerAd response){
		this(context,response,null);
	}
	public FloatView(final Context context, final BannerAd response, final AdListener adListener) {
		this(context, response, true, adListener);
	}

	public FloatView(final Context context, final BannerAd response, final boolean animation, final AdListener adListener) {
		super(context);
		mResponse = response;
		mContext = context;
		this.mAnimation = animation;
		this.mAdListener = adListener;
		//this.initialize(response);
	}

	private WebView createWebView(final Context context) {
		final WebView webView = new WebView(this.getContext()) {
			@Override
			public void draw(final Canvas canvas) {
				if (this.getWidth() > 0 && this.getHeight() > 0)
					super.draw(canvas);
			}
		};
		mWebSettings = webView.getSettings();
		mWebSettings.setJavaScriptEnabled(true);
		webView.setBackgroundColor(Color.TRANSPARENT);
		setLayer(webView);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				if(mResponse.getSkipOverlay()==1){
					doOpenUrl(url);
					return true;
				}
				openLink();
				return true;
			}
		});
		webView.setVerticalScrollBarEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);
		return webView;
	}

	private void doOpenUrl(final String url) {
		notifyAdClicked();
		if (mResponse.getClickType() != null
				&& mResponse.getClickType().equals(ClickType.INAPP) && (url.startsWith("http://") || url.startsWith("https://"))) {
			if(url.endsWith(".mp4")){
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setDataAndType(Uri.parse(url), "video/mp4");
				getContext().startActivity(i);
			}else{
			    Intent intent = new Intent(getContext(),InAppWebView.class);
				intent.putExtra(Const.REDIRECT_URI, url);
				getContext().startActivity(intent);
			}
		} else {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			getContext().startActivity(intent);
		}
	}

	static {
		initCompatibility();
	};

	private static void initCompatibility() {
		try {
			for(Method m:WebView.class.getMethods()){
				if(m.getName().equals("setLayerType")){
					mWebView_SetLayerType = m;
					break;
				}
			}
			mWebView_LAYER_TYPE_SOFTWARE = WebView.class.getField("LAYER_TYPE_SOFTWARE");
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		}
	}

	private void setLayer(WebView webView){
		if (mWebView_SetLayerType != null && mWebView_LAYER_TYPE_SOFTWARE !=null) {
			try {
				mWebView_SetLayerType.invoke(webView, mWebView_LAYER_TYPE_SOFTWARE.getInt(WebView.class), null);
			} catch (InvocationTargetException ite) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
		}
	}

	private void buildBannerView(){
		if(mfirstWebView != null){
			mfirstWebView.startAnimation(Util.GetExitTranslateAnimation(mTranslateAnimationType));
			FloatView.this.removeView(mfirstWebView);
			mfirstWebView = null;
		}
		mfirstWebView = createWebView(mContext);
		setLayoutParams(new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT
				,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		this.addView(mfirstWebView, params);
	}
	public void Show(){
		Show(mResponse);
	}
	public void Show(final BannerAd ad) {
		if(ad == null)return;
        mResponse  = ad; 
		initCompatibility();
		buildBannerView();
		showContent();
	}

	private void notifyAdClicked() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mAdListener != null){
					mAdListener.adClicked();
				}
			}
		});
	}

	private void notifyLoadAdSucceeded() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (mAdListener != null){
					mAdListener.adLoadSucceeded(null);
				}
			}
		});
	}

	private void openLink() {
		if (mResponse != null && mResponse.getClickUrl() != null)
			doOpenUrl(mResponse.getClickUrl());
	}

	public void setAdListener(AdListener bannerListener) {
		mAdListener = bannerListener;
	}


	private void showContent() {
		  try {
			if (mResponse.getType() == Const.IMAGE) {
				String text = MessageFormat.format(Const.IMAGE_BODY,
						mResponse.getImageUrl(),
						mResponse.getBannerWidth(),
						mResponse.getBannerHeight());
				text = Uri.encode(Const.HIDE_BORDER + text);
				mfirstWebView.loadData(text, "text/html", Const.ENCODING);
				notifyLoadAdSucceeded();
			} else if (mResponse.getType() == Const.TEXT) {
                if(((new File(Const.DOWNLOAD_PATH+Util.VideoFileDir
    					+(Const.DOWNLOAD_DISPLAY_IMG)+Util.ExternalName==null?"":Util.ExternalName)).exists())){
                	String baseUrl = "file://";
        			baseUrl = baseUrl+Const.DOWNLOAD_PATH+Util.VideoFileDir;
        			String textPath =  Const.HIDE_BORDER + "<img src='"
        					+ (Const.DOWNLOAD_DISPLAY_IMG)+Util.ExternalName + "'/>";
        			Log.d("Jas","show-->"+baseUrl+","+textPath);
        			mfirstWebView.clearCache(false);
        			mfirstWebView.loadDataWithBaseURL(baseUrl, textPath, "text/html","utf-8", null);
        			notifyLoadAdSucceeded();
                }else{
                	mfirstWebView.clearCache(false);
					String text = mResponse.getText();
					text = Uri.encode(Const.HIDE_BORDER + text);
					mfirstWebView.loadData(text, "text/html", Const.ENCODING);
				    notifyLoadAdSucceeded();
                }
			} 
            if(mAnimation){
            	mfirstWebView.startAnimation(Util.GetTranslateAnimation(mTranslateAnimationType));
            }
		} catch (Throwable t) {
		}
	}
	
	//add b yJas foe animation
	public void SetAnimation(TranslateAnimationType type){
		mTranslateAnimationType = type;
	}
	private TranslateAnimationType mTranslateAnimationType = TranslateAnimationType.RANDOM;
}
