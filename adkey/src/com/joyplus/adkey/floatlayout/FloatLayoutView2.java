package com.joyplus.adkey.floatlayout;

import java.io.File;
import java.text.MessageFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import android.widget.RelativeLayout.LayoutParams;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.BannerAd;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.Monitorer.AdMonitorManager;
import com.joyplus.adkey.Util.TranslateAnimationType;
import com.joyplus.adkey.download.ImpressionThread;
import com.joyplus.adkey.video.InterstitialData;
import com.joyplus.adkey.video.RichMediaAd;
import com.joyplus.adkey.video.WebFrame;
import com.joyplus.adkey.video.WebViewClient.OnPageLoadedListener;

public class FloatLayoutView2 extends RelativeLayout{

	private boolean    animation  = true;
	private Context    mContext   = null;
	private AdListener adListener = null;
	private Handler    mHandler   = new Handler();
	
	public static final int TYPE_UNKNOWN      = -1;
	public static final int TYPE_BROWSER      = 0;
	public static final int TYPE_VIDEO        = 1;
	public static final int TYPE_INTERSTITIAL = 2;
	
	private BannerAd mAd;
	private FrameLayout mRootLayout;
	private WebFrame mInterstitialView;
	private WebView  mInterstitialImageView;
	
	private Timer mInterstitialLoadingTimer;
	
	
	private boolean Completed = false;
	
	public FloatLayoutView2(final Context context ,final BannerAd response, final AdListener adListener) {
		this(context, response, true, adListener);
	}
	public FloatLayoutView2(final Context context, final BannerAd response, final boolean animation, final AdListener adListener) {
		super(context);
		this.mAd = response;
		mContext = context;
		this.animation  = animation;
		this.adListener = adListener;
	}
	//for TranslateAnimation
	public void SetAnimation(TranslateAnimationType type){
		mTranslateAnimationType = type;
	}
	private TranslateAnimationType mTranslateAnimationType = TranslateAnimationType.RANDOM;
	
	public void InitResource(){
		if(mAd == null){
			notifyfinish(false);
			return;
		}
		FloatLayoutView2.this.setBackgroundColor(Color.TRANSPARENT);
		InitInterstitialView();
	}
	
	private void InitInterstitialView(){
		if(mAd.GetCreative_res_url()==null || "".equals(mAd.GetCreative_res_url())){
			notifyfinish(false);
			return;
		}
		initInterstitialView();
	}
	
	private void initInterstitialView(){
		mRootLayout = new FrameLayout(mContext);
		FloatLayoutView2.this.addView(mRootLayout, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				   LayoutParams.MATCH_PARENT, Gravity.CENTER));
		
		mInterstitialView = new WebFrame((Activity)mContext, true, false, false);
		mInterstitialView.setOnPageLoadedListener(mOnInterstitialLoadedListener);
		mInterstitialView.setOnClickListener(mInterstitialClickListener);
		mRootLayout.addView(mInterstitialView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				   LayoutParams.MATCH_PARENT, Gravity.CENTER));
		if(Util.isCacheLoaded()){
			ShowImage(mAd.GetCreative_res_url(),true);//show image oline.
		}else{
			notifyfinish(false);//finish
		}
	}
	private void ShowImage(String getCreative_res_url,boolean location) {
		 //TODO Auto-generated method stub
		new ImpressionThread(mContext, mAd.getmImpressionUrl(), Util.PublisherId,Util.AD_TYPE.BANNER_IMAGE).start();
		if(mAd.getmTrackingUrl().size()>0){
			AdMonitorManager.getInstance(mContext).AddTRACKINGURL(mAd.getmTrackingUrl());
		}
		if(mInterstitialImageView != null){
			if(animation){
				if(mTranslateAnimationType == null || mTranslateAnimationType == TranslateAnimationType.NOAN){
				}else{
					mInterstitialImageView.startAnimation(Util.GetExitTranslateAnimation(mTranslateAnimationType));
				}
			}
			mRootLayout.removeView(mInterstitialImageView);
			mInterstitialImageView = null;
		}
		mInterstitialImageView = new WebView(mContext);
		mInterstitialImageView.setBackgroundColor(Color.TRANSPARENT);
		mInterstitialImageView.getSettings().setJavaScriptEnabled(true);
		mInterstitialImageView.setVerticalScrollBarEnabled(false);
		mInterstitialImageView.setHapticFeedbackEnabled(false);
		 
		mRootLayout.addView(mInterstitialImageView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				   LayoutParams.MATCH_PARENT, Gravity.CENTER));
		if(location){
			String baseUrl = "file://";
			baseUrl = baseUrl+Const.DOWNLOAD_PATH+Util.VideoFileDir;
			String textPath =  Const.HIDE_BORDER + "<img src='"
					+ (Const.DOWNLOAD_DISPLAY_IMG)+Util.ExternalName + "'/>";
			if(!(new File(Const.DOWNLOAD_PATH+Util.VideoFileDir
					+(Const.DOWNLOAD_DISPLAY_IMG)+Util.ExternalName)).exists()){
				return;
			}
			mInterstitialImageView.clearCache(false);
			mInterstitialImageView.loadDataWithBaseURL(baseUrl, textPath, "text/html","utf-8", null);
		}else{
			String text = MessageFormat.format(Const.IMAGE_BODY,getCreative_res_url,null,null);
			text = Uri.encode(Const.HIDE_BORDER + text);
			mInterstitialImageView.loadData(text, "text/html", Const.ENCODING);
		}
		if (this.animation) {
			if(mTranslateAnimationType == null || mTranslateAnimationType == TranslateAnimationType.NOAN){
			}else{
				mInterstitialImageView.startAnimation(Util.GetTranslateAnimation(mTranslateAnimationType));
			}
		}
	}
	
	OnPageLoadedListener mOnInterstitialLoadedListener = new OnPageLoadedListener(){
		@Override
		public void onPageLoaded(){
			if (mInterstitialLoadingTimer != null){
				mInterstitialLoadingTimer.cancel();
				mInterstitialLoadingTimer = null;
			}
		}
	};
	private void InitViewTimeOut(){
		if (mInterstitialLoadingTimer != null){
			mInterstitialLoadingTimer.cancel();
			mInterstitialLoadingTimer = null;
		}
		this.mInterstitialLoadingTimer = new Timer();
		TimerTask autocloseTask = new TimerTask(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				notifyfinish(false);
			}
		};
		if(mAd.getRefresh()<=0)
		    mInterstitialLoadingTimer.schedule(autocloseTask,Const.CONNECTION_TIMEOUT);
		else
			mInterstitialLoadingTimer.schedule(autocloseTask,mAd.getRefresh()*1000);
	}
	private final OnClickListener mInterstitialClickListener = new OnClickListener(){
		@Override
		public void onClick(final View arg0){
			//here has be onclicked.
			
		}
	};
	
	private void notifyfinish(boolean completed){
		Completed = completed;
		mHandler.removeCallbacks(Close);
		mHandler.post(Close);
	}
	
	private Runnable Close = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(mInterstitialImageView != null){//for animation
				if(animation){
					if(mTranslateAnimationType == null || mTranslateAnimationType == TranslateAnimationType.NOAN){
					}else{
					     mInterstitialImageView.startAnimation(Util.GetExitTranslateAnimation(mTranslateAnimationType));
					}
				}
				mRootLayout.removeView(mInterstitialImageView);
				mInterstitialImageView = null;
			}
			FloatLayoutView2.this.removeAllViews();
			if(adListener != null){
				adListener.adClosed(mAd, Completed);
			}
		}
	};
}
