package com.joyplus.adkey.mini;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.Util.TranslateAnimationType;
import com.joyplus.adkey.Monitorer.AdMonitorManager;
import com.joyplus.adkey.download.ImpressionThread;
import com.joyplus.adkey.video.InterstitialData;
import com.joyplus.adkey.video.RichMediaAd;
import com.joyplus.adkey.video.TrackEvent;
import com.joyplus.adkey.video.TrackerService;
import com.joyplus.adkey.video.VideoData;
import com.joyplus.adkey.video.WebFrame;
import com.joyplus.adkey.video.SDKVideoView.OnStartListener;
import com.joyplus.adkey.video.SDKVideoView.OnTimeEventListener;
import com.joyplus.adkey.video.WebViewClient.OnPageLoadedListener;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class AdMiniView extends RelativeLayout{

	private boolean    animation  = true;
	private Context    mContext   = null;
	private AdListener adListener = null;
	private Handler    mHandler   = new Handler();
	
	public static final int TYPE_UNKNOWN      = -1;
	public static final int TYPE_BROWSER      = 0;
	public static final int TYPE_VIDEO        = 1;
	public static final int TYPE_INTERSTITIAL = 2;
	
	private FrameLayout mRootLayout;
	private FrameLayout mVideoLayout;
	private FrameLayout mLoadingView;
	
	private VideoView mVideoView;
	private WebFrame mInterstitialView;
	private WebView  mInterstitialImageView;
	
	private RichMediaAd mAd;
	private VideoData mVideoData;
	private InterstitialData mInterstitialData;
	
	private Timer mInterstitialLoadingTimer;
	private Timer mVideoTimeoutTimer;
	
	private boolean Completed = false;
	
	public AdMiniView(final Context context ,final RichMediaAd response, final AdListener adListener) {
		this(context, response, true, adListener);
	}
	public AdMiniView(final Context context, final RichMediaAd response, final boolean animation, final AdListener adListener) {
		super(context);
		this.mAd = response;
		mContext = context;
		this.animation = animation;
		this.adListener = adListener;
		InitResource();
	}
	private void InitResource(){
		if(mAd == null){
			notifyfinish(false);
			return;
		}
		AdMiniView.this.setBackgroundColor(Color.TRANSPARENT);
		if(mAd.getType() == Const.INTERSTITIAL){
			InitInterstitialView();
		} else if(mAd.getType() == Const.VIDEO){
			InitVideoView();
		} else {
			notifyfinish(false);
			return;
		}
	}
	
	private void notifyfinish(boolean completed){
		Completed = completed;
		mHandler.removeCallbacks(Close);
		mHandler.post(Close);
	}
	private Runnable Close = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(mVideoView != null){
				try{
					mVideoView.stopPlayback();
				}catch(Exception e){
				}
				mVideoView = null;
			}
			AdMiniView.this.removeAllViews();
			if(adListener != null){
				adListener.adClosed(mAd, Completed);
			}
		}
	};
	//////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	// VideoView Demo for simple. remove most function. 
    private void InitVideoView() {
		// TODO Auto-generated method stub
    	if(mAd.getVideo() == null 
    			|| mAd.getVideo().videoUrl == null
    			|| "".equals(mAd.getVideo().videoUrl)){
            notifyfinish(false);
    		return;
    	}
    	initVideoView();
	}
	private void initVideoView(){
		mVideoData   = this.mAd.getVideo();
		mVideoLayout = new FrameLayout(mContext);//add to this RelativeLayout
		mVideoLayout.setBackgroundColor(Color.TRANSPARENT);
		mVideoLayout.setFocusable(false);
		AdMiniView.this.addView(mVideoLayout, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						   LayoutParams.MATCH_PARENT, Gravity.CENTER));
		
		InitVideoViewUI();
		InitLoadingUI();
		
		new ImpressionThread(mContext, mAd.getmImpressionUrl(), Util.PublisherId,Util.AD_TYPE.FULL_SCREEN_VIDEO).start();
		if(mAd.getmTrackingUrl().size()>0){
			AdMonitorManager.getInstance(mContext).AddTRACKINGURL(mAd.getmTrackingUrl());
		}
		
		mVideoView.setVideoPath(mVideoData.videoUrl);
		mVideoView.start();
		InitVideoViewTimeOut();
	}
	private void InitVideoViewTimeOut(){
		if(mVideoTimeoutTimer != null){
			mVideoTimeoutTimer.cancel();
			mVideoTimeoutTimer = null;
		}
		mVideoTimeoutTimer = new Timer();
		TimerTask autocloseTask = new TimerTask(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				notifyfinish(false);
			}
		};
		mVideoTimeoutTimer.schedule(autocloseTask,
				Const.VIDEO_LOAD_TIMEOUT);
	}
	private void InitVideoViewUI(){
		mVideoView   = new VideoView(mContext);
		mVideoView.setOnPreparedListener(this.mOnVideoPreparedListener);
		mVideoView.setOnCompletionListener(this.mOnVideoCompletionListener);
		mVideoView.setOnErrorListener(this.mOnVideoErrorListener);
		mVideoView.setFocusable(false);
		mVideoView.setFocusableInTouchMode(false);
		//mVideoView.setOnKeyListener(this.mOnVideoKeyListener);
		//mVideoView.setOnInfoListener(this.mOnVideoInfoListener);
		mVideoLayout.addView(mVideoView,
				   new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
						   LayoutParams.WRAP_CONTENT, Gravity.CENTER));//
	}
	private void InitLoadingUI(){
		mLoadingView = new FrameLayout(mContext);
		mLoadingView.setFocusable(false);  
		mLoadingView.setBackgroundColor(Color.TRANSPARENT);
		TextView loadingText = new TextView(mContext);
		loadingText.setText(Const.LOADING);
		loadingText.setBackgroundColor(Color.TRANSPARENT); 
		loadingText.setFocusable(false);
		mLoadingView.addView(loadingText, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, Gravity.CENTER));
		mVideoLayout.addView(this.mLoadingView,
				new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT, Gravity.CENTER));// fill_parent
	}
	
	OnTimeEventListener mOnVideoTimeEventListener = new OnTimeEventListener(){
		@Override
		public void onTimeEvent(final int time){
			final Vector<String> trackers = mVideoData.timeTrackingEvents.get(time);
			if (trackers != null)
				for (int i = 0; i < trackers.size(); i++){
					final TrackEvent event = new TrackEvent();
					event.url = trackers.get(i);
					event.timestamp = System.currentTimeMillis();
					TrackerService.requestTrack(event);
				}
		}
	};
	OnKeyListener mOnVideoKeyListener = new OnKeyListener(){
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			Log.d("Jas","mOnVideoKeyListener-->"+KeyEvent.keyCodeToString(keyCode));
			return true;
		}
	};
	OnErrorListener mOnVideoErrorListener = new OnErrorListener(){
		@Override
		public boolean onError(final MediaPlayer mp, final int what,
				final int extra){
			notifyfinish(false);
			return true;
		}
	};
	
	protected int mTimeTest;
	
	OnInfoListener mOnVideoInfoListener = new OnInfoListener(){
		@Override
		public boolean onInfo(final MediaPlayer mp, final int what,
				final int extra){
			if (what == 703){
				mTimeTest = mVideoView.getCurrentPosition();
				new Handler().postDelayed(mCheckProgressTask, 5000);
			}
			return true;
		}
	};
	private Runnable mCheckProgressTask = new Runnable(){
		public void run(){
			int test = mVideoView.getCurrentPosition();
			if (test - mTimeTest <= 1){
				notifyfinish(false);
			}
		}
	};
	OnPreparedListener mOnVideoPreparedListener = new OnPreparedListener(){
		@Override
		public void onPrepared(final MediaPlayer mp){
			if (mVideoTimeoutTimer != null){
				mVideoTimeoutTimer.cancel();
				mVideoTimeoutTimer = null;
			}
			if (mLoadingView != null)
				mLoadingView.setVisibility(View.GONE);
			mVideoView.requestFocus();
		}
	};
	
	OnCompletionListener mOnVideoCompletionListener = new OnCompletionListener(){
		@Override
		public void onCompletion(final MediaPlayer mp){
			final Vector<String> trackers = mVideoData.completeEvents;
			for (int i = 0; i < trackers.size(); i++){
				final TrackEvent event = new TrackEvent();
				event.url = trackers.get(i);
				event.timestamp = System.currentTimeMillis();
				TrackerService.requestTrack(event);
			}
			notifyfinish(true);
		}
	};
	
	OnStartListener mOnVideoStartListener = new OnStartListener(){
		@Override
		public void onVideoStart(){
			final Vector<String> trackers = mVideoData.startEvents;
			for (int i = 0; i < trackers.size(); i++){
				final TrackEvent event = new TrackEvent();
				event.url = trackers.get(i);
				event.timestamp = System.currentTimeMillis();
				TrackerService.requestTrack(event);
			}
		}
	};
	////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////
	//for img
	private void InitInterstitialView(){
		if(mAd.getInterstitial()==null){
			notifyfinish(false);
			return;
		}
		initInterstitialView();
	}
	private void initInterstitialView(){
		mInterstitialData = this.mAd.getInterstitial();
		
		mRootLayout = new FrameLayout(mContext);
		AdMiniView.this.addView(mRootLayout, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				   LayoutParams.MATCH_PARENT, Gravity.CENTER));
		
		mInterstitialView = new WebFrame((Activity)mContext, true, false, false);
		mInterstitialView.setOnPageLoadedListener(mOnInterstitialLoadedListener);
		mInterstitialView.setOnClickListener(mInterstitialClickListener);
		mRootLayout.addView(mInterstitialView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				   LayoutParams.MATCH_PARENT, Gravity.CENTER));
		
		new ImpressionThread(mContext, mAd.getmImpressionUrl(), Util.PublisherId,Util.AD_TYPE.FULL_SCREEN_VIDEO).start();
		if(mAd.getmTrackingUrl().size()>0){
			AdMonitorManager.getInstance(mContext).AddTRACKINGURL(mAd.getmTrackingUrl());
		}
		
		switch (mInterstitialData.interstitialType){
			case InterstitialData.INTERSTITIAL_MARKUP:
				if(mAd.GetCreative_res_url() !=null && mAd.GetCreative_res_url().length()>0){
					ShowImage(mAd.GetCreative_res_url());//show image oline.
				}else if(Util.isCacheLoaded()){
					String textData = this.mInterstitialData.interstitialMarkup;
					if(textData!=null){
						int startInd = textData.indexOf("<img")+10;
						int endInd = textData.indexOf(">", startInd)-1;
						String thisImageText = textData.substring(startInd, endInd);
						URL url = null;
						try{
							url = new URL(thisImageText);
						} catch (MalformedURLException e){
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (url != null){
							Util.ExternalName = "." + Util.getExtensionName(url.getPath());
						} else {
							Util.ExternalName = ".jpg";
						}
						this.mInterstitialView.loadUrl(textData);
					}
					mInterstitialView.setMarkup(mInterstitialData.interstitialMarkup);
				}else{
					notifyfinish(false);
				}
				break;
			case InterstitialData.INTERSTITIAL_URL:
				this.mInterstitialView.loadUrl(mInterstitialData.interstitialUrl);
				break;
				default: {
					notifyfinish(false);
					return;
				}
		}
		InitViewTimeOut();
	}
	private void ShowImage(String getCreative_res_url) {
		 //TODO Auto-generated method stub
		if(mInterstitialImageView != null){
			if(animation){
				if(mTranslateAnimationType==null || mTranslateAnimationType == TranslateAnimationType.NOAN){
				}else{
					mInterstitialImageView.startAnimation(Util.GetTranslateAnimation(mTranslateAnimationType));
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
		String text = MessageFormat.format(Const.IMAGE_BODY,getCreative_res_url,null,null);
		text = Uri.encode(Const.HIDE_BORDER + text);
		mInterstitialImageView.loadData(text, "text/html", Const.ENCODING);
		if (this.animation) {
			if(mTranslateAnimationType==null || mTranslateAnimationType == TranslateAnimationType.NOAN){
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
		if(mAd.GetRefresh()<=0)
		    mInterstitialLoadingTimer.schedule(autocloseTask,Const.CONNECTION_TIMEOUT);
		else
			mInterstitialLoadingTimer.schedule(autocloseTask,mAd.GetRefresh()*1000);
	}
	private final OnClickListener mInterstitialClickListener = new OnClickListener(){
		@Override
		public void onClick(final View arg0){
			//here has be onclicked.
			
		}
	};
	//add by Jas for animation
	private TranslateAnimationType mTranslateAnimationType = TranslateAnimationType.RANDOM;
	public  void SetAnimation(TranslateAnimationType type){
		if(type != null){
			mTranslateAnimationType = type;
		}
	}
}
