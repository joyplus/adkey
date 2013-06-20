package com.joyplus.adkey.video;

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;

import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.joyplus.adkey.AdManager;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.download.Downloader;
import com.joyplus.adkey.video.InterstitialController.OnResetAutocloseListener;
import com.joyplus.adkey.video.MediaController.OnPauseListener;
import com.joyplus.adkey.video.MediaController.OnReplayListener;
import com.joyplus.adkey.video.MediaController.OnUnpauseListener;
import com.joyplus.adkey.video.SDKVideoView.OnStartListener;
import com.joyplus.adkey.video.SDKVideoView.OnTimeEventListener;
import com.joyplus.adkey.video.WebViewClient.OnPageLoadedListener;
import com.joyplus.adkey.widget.Log;

public class RichMediaActivity extends Activity
{
	
	class CanSkipTask extends TimerTask
	{
		
		private final RichMediaActivity mActivity;
		
		public CanSkipTask(final RichMediaActivity activity)
		{
			this.mActivity = activity;
		}
		
		@Override
		public void run()
		{
			
			this.mActivity.mCanClose = true;
			if (this.mActivity.mSkipButton != null)
				this.mActivity.runOnUiThread(new Runnable()
				{
					
					@Override
					public void run()
					{
						CanSkipTask.this.mActivity.mSkipButton
								.setVisibility(View.VISIBLE);
					}
				});
		}
	}
	
	class InterstitialAutocloseTask extends TimerTask
	{
		
		private final Activity mActivity;
		
		public InterstitialAutocloseTask(final Activity activity)
		{
			this.mActivity = activity;
		}
		
		@Override
		public void run()
		{
			
			RichMediaActivity.this.mResult = true;
			this.mActivity.runOnUiThread(new Runnable()
			{
				
				@Override
				public void run()
				{
					
					RichMediaActivity.this.setResult(Activity.RESULT_OK);
					RichMediaActivity.this.finish();
				}
			});
			
		}
	}
	
	class InterstitialLoadingTimeoutTask extends TimerTask
	{
		
		@Override
		public void run()
		{
			
			RichMediaActivity.this.mCanClose = true;
			RichMediaActivity.this.mInterstitialController.pageLoaded();
		}
	}
	
	class VideoTimeoutTask extends TimerTask
	{
		
		private final Activity mActivity;
		
		public VideoTimeoutTask(final Activity activity)
		{
			this.mActivity = activity;
		}
		
		@Override
		public void run()
		{
			
			this.mActivity.runOnUiThread(new Runnable()
			{
				
				@Override
				public void run()
				{
					VideoTimeoutTask.this.mActivity.finish();
				}
			});
		}
	}
	
	public static final int TYPE_UNKNOWN = -1;
	
	public static final int TYPE_BROWSER = 0;
	public static final int TYPE_VIDEO = 1;
	public static final int TYPE_INTERSTITIAL = 2;
	
	// public static final int TYPE_LOCALVIDEO = 3;//add by yyc
	
	public static void setActivityAnimation(final Activity activity,
			final int in, final int out)
	{
		try
		{
			activity.overridePendingTransition(in, out);
			
		} catch (final Exception e)
		{
			
		}
	}
	
	private ResourceManager mResourceManager;
	private FrameLayout mRootLayout;
	private FrameLayout mVideoLayout;
	private FrameLayout mLoadingView;
	private FrameLayout mCustomView;
	private VideoView mCustomVideoView;
	private WebChromeClient.CustomViewCallback mCustomViewCallback;
	private SDKVideoView mVideoView;
	private WebFrame mOverlayView;
	private WebFrame mInterstitialView;
	private WebFrame mWebBrowserView;
	private MediaController mMediaController;
	private ImageView mSkipButton;
	private InterstitialController mInterstitialController;
	private RichMediaAd mAd;
	private VideoData mVideoData;
	private InterstitialData mInterstitialData;
	
	private Uri uri;
	private Timer mInterstitialLoadingTimer;
	private Timer mInterstitialAutocloseTimer;
	private Timer mInterstitialCanCloseTimer;
	private Timer mVideoTimeoutTimer;
	private int mWindowWidth;
	private int mWindowHeight;
	private int mVideoLastPosition;
	private int mVideoWidth;
	private int mVideoHeight;
	private boolean mCanClose;
	protected boolean mInterstitialAutocloseReset;
	private boolean mPageLoaded = false;
	private int mType;
	private int mEnterAnim;
	
	private int mExitAnim;
	private boolean mResult;
	
	DisplayMetrics metrics;
	
	int paddingArg = 5;
	
	int marginArg = 8;
	
	int skipButtonSizeLand = 50;
	
	int skipButtonSizePort = 40;
	
	static class ResourceHandler extends Handler
	{
		
		WeakReference<RichMediaActivity> richMediaActivity;
		
		public ResourceHandler(RichMediaActivity activity)
		{
			richMediaActivity = new WeakReference<RichMediaActivity>(activity);
		}
		
		@Override
		public void handleMessage(final Message msg)
		{
			RichMediaActivity wRichMediaActivity = richMediaActivity.get();
			if (wRichMediaActivity != null)
			{
				wRichMediaActivity.handleMessage(msg);
			}
		}
	};
	
	public void handleMessage(final Message msg)
	{
		switch (msg.what)
		{
			case ResourceManager.RESOURCE_LOADED_MSG:
				switch (msg.arg1)
				{
					case ResourceManager.DEFAULT_SKIP_IMAGE_RESOURCE_ID:
						if (RichMediaActivity.this.mSkipButton != null)
							if (mResourceManager
									.containsResource(ResourceManager.DEFAULT_SKIP_IMAGE_RESOURCE_ID))
							{
								RichMediaActivity.this.mSkipButton
										.setImageDrawable(mResourceManager
												.getResource(
														this,
														ResourceManager.DEFAULT_SKIP_IMAGE_RESOURCE_ID));
							} else
							{
								RichMediaActivity.this.mSkipButton
										.setImageDrawable(mResourceManager
												.getResource(
														this,
														ResourceManager.DEFAULT_SKIP_IMAGE_RESOURCE_ID));
							}
						
						break;
				}
				break;
		
		}
	}
	
	private final OnTimeEventListener mOverlayShowListener = new OnTimeEventListener()
	{
		
		@Override
		public void onTimeEvent(final int time)
		{
			
			if (RichMediaActivity.this.mOverlayView != null)
			{
				RichMediaActivity.this.mOverlayView.setVisibility(View.VISIBLE);
				RichMediaActivity.this.mOverlayView.requestLayout();
			}
		}
	};
	
	private final OnClickListener mOverlayClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(final View arg0)
		{
			
			if (RichMediaActivity.this.mMediaController != null)
				RichMediaActivity.this.mMediaController.toggle();
		}
	};
	
	OnErrorListener mOnVideoErrorListener = new OnErrorListener()
	{
		
		@Override
		public boolean onError(final MediaPlayer mp, final int what,
				final int extra)
		{
			finish();
			return false;
		}
	};
	
	protected int mTimeTest;
	
	OnInfoListener mOnVideoInfoListener = new OnInfoListener()
	{
		
		@Override
		public boolean onInfo(final MediaPlayer mp, final int what,
				final int extra)
		{
			if (what == 703)
			{
				mTimeTest = mVideoView.getCurrentPosition();
				new Handler().postDelayed(mCheckProgressTask, 5000);
			}
			return false;
		}
	};
	
	private Runnable mCheckProgressTask = new Runnable()
	{
		
		public void run()
		{
			int test = mVideoView.getCurrentPosition();
			if (test - mTimeTest <= 1)
			{
				finish();
			}
		}
	};
	
	OnPreparedListener mOnVideoPreparedListener = new OnPreparedListener()
	{
		
		@Override
		public void onPrepared(final MediaPlayer mp)
		{
			
			if (RichMediaActivity.this.mVideoTimeoutTimer != null)
			{
				RichMediaActivity.this.mVideoTimeoutTimer.cancel();
				RichMediaActivity.this.mVideoTimeoutTimer = null;
			}
			if (RichMediaActivity.this.mLoadingView != null)
				RichMediaActivity.this.mLoadingView.setVisibility(View.GONE);
			if (mVideoData.showNavigationBars)
				mMediaController.setVisibility(View.VISIBLE);
			RichMediaActivity.this.mVideoView.requestFocus();
		}
	};
	
	OnCompletionListener mOnVideoCompletionListener = new OnCompletionListener()
	{
		
		@Override
		public void onCompletion(final MediaPlayer mp)
		{
			
			final Vector<String> trackers = RichMediaActivity.this.mVideoData.completeEvents;
			for (int i = 0; i < trackers.size(); i++)
			{
				
				final TrackEvent event = new TrackEvent();
				event.url = trackers.get(i);
				event.timestamp = System.currentTimeMillis();
				TrackerService.requestTrack(event);
			}
			if (RichMediaActivity.this.mType == RichMediaActivity.TYPE_VIDEO
					&& RichMediaActivity.this.mAd.getType() == Const.VIDEO_TO_INTERSTITIAL)
			{
				final Intent intent = new Intent(RichMediaActivity.this,
						RichMediaActivity.class);
				intent.putExtra(Const.AD_EXTRA, RichMediaActivity.this.mAd);
				intent.putExtra(Const.AD_TYPE_EXTRA,
						RichMediaActivity.TYPE_INTERSTITIAL);
				try
				{
					RichMediaActivity.this.startActivity(intent);
					RichMediaActivity.setActivityAnimation(
							RichMediaActivity.this,
							RichMediaActivity.this.mEnterAnim,
							RichMediaActivity.this.mExitAnim);
				} catch (final Exception e)
				{
				}
			}
			RichMediaActivity.this.mResult = true;
			RichMediaActivity.this.setResult(Activity.RESULT_OK);
			RichMediaActivity.this.finish();
		}
	};
	
	OnStartListener mOnVideoStartListener = new OnStartListener()
	{
		
		@Override
		public void onVideoStart()
		{
			
			final Vector<String> trackers = RichMediaActivity.this.mVideoData.startEvents;
			for (int i = 0; i < trackers.size(); i++)
			{
				
				final TrackEvent event = new TrackEvent();
				event.url = trackers.get(i);
				event.timestamp = System.currentTimeMillis();
				TrackerService.requestTrack(event);
			}
		}
	};
	
	OnPauseListener mOnVideoPauseListener = new OnPauseListener()
	{
		
		@Override
		public void onVideoPause()
		{
			
			final Vector<String> trackers = RichMediaActivity.this.mVideoData.pauseEvents;
			for (int i = 0; i < trackers.size(); i++)
			{
				
				final TrackEvent event = new TrackEvent();
				event.url = trackers.get(i);
				event.timestamp = System.currentTimeMillis();
				TrackerService.requestTrack(event);
			}
		}
	};
	
	OnUnpauseListener mOnVideoUnpauseListener = new OnUnpauseListener()
	{
		
		@Override
		public void onVideoUnpause()
		{
			
			final Vector<String> trackers = RichMediaActivity.this.mVideoData.unpauseEvents;
			for (int i = 0; i < trackers.size(); i++)
			{
				
				final TrackEvent event = new TrackEvent();
				event.url = trackers.get(i);
				event.timestamp = System.currentTimeMillis();
				TrackerService.requestTrack(event);
			}
		}
	};
	
	OnTimeEventListener mOnVideoTimeEventListener = new OnTimeEventListener()
	{
		
		@Override
		public void onTimeEvent(final int time)
		{
			
			final Vector<String> trackers = RichMediaActivity.this.mVideoData.timeTrackingEvents
					.get(time);
			if (trackers != null)
				for (int i = 0; i < trackers.size(); i++)
				{
					final TrackEvent event = new TrackEvent();
					event.url = trackers.get(i);
					event.timestamp = System.currentTimeMillis();
					TrackerService.requestTrack(event);
				}
		}
	};
	
	OnTimeEventListener mOnVideoCanCloseEventListener = new OnTimeEventListener()
	{
		
		@Override
		public void onTimeEvent(final int time)
		{
			RichMediaActivity.this.mCanClose = true;
			if (RichMediaActivity.this.mVideoData.showSkipButton
					&& RichMediaActivity.this.mSkipButton != null)
			{
				
				RichMediaActivity.this.mSkipButton
						.setImageDrawable(mResourceManager.getResource(
								RichMediaActivity.this,
								ResourceManager.DEFAULT_SKIP_IMAGE_RESOURCE_ID));
				
				RichMediaActivity.this.mSkipButton.setVisibility(View.VISIBLE);
			}
		}
	};
	
	OnClickListener mOnVideoSkipListener = new OnClickListener()
	{
		
		@Override
		public void onClick(final View v)
		{
			
			final Vector<String> trackers = RichMediaActivity.this.mVideoData.skipEvents;
			for (int i = 0; i < trackers.size(); i++)
			{
				
				final TrackEvent event = new TrackEvent();
				event.url = trackers.get(i);
				event.timestamp = System.currentTimeMillis();
				TrackerService.requestTrack(event);
			}
			
			RichMediaActivity.this.mResult = true;
			RichMediaActivity.this.setResult(Activity.RESULT_OK);
			RichMediaActivity.this.finish();
		}
	};
	
	OnReplayListener mOnVideoReplayListener = new OnReplayListener()
	{
		
		@Override
		public void onVideoReplay()
		{
			
			final Vector<String> trackers = RichMediaActivity.this.mVideoData.replayEvents;
			for (int i = 0; i < trackers.size(); i++)
			{
				
				final TrackEvent event = new TrackEvent();
				event.url = trackers.get(i);
				event.timestamp = System.currentTimeMillis();
				TrackerService.requestTrack(event);
			}
		}
	};
	
	private final OnClickListener mInterstitialClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(final View arg0)
		{
			
			if (RichMediaActivity.this.mInterstitialController != null)
			{
				RichMediaActivity.this.mInterstitialController.toggle();
				RichMediaActivity.this.mInterstitialController.resetAutoclose();
			}
		}
	};
	
	OnClickListener mOnInterstitialSkipListener = new OnClickListener()
	{
		
		@Override
		public void onClick(final View v)
		{
			
			RichMediaActivity.this.mResult = true;
			RichMediaActivity.this.setResult(Activity.RESULT_OK);
			RichMediaActivity.this.finish();
		}
	};
	
	OnResetAutocloseListener mOnResetAutocloseListener = new OnResetAutocloseListener()
	{
		
		@Override
		public void onResetAutoclose()
		{
			
			RichMediaActivity.this.mInterstitialAutocloseReset = true;
			if (RichMediaActivity.this.mInterstitialAutocloseTimer != null)
			{
				RichMediaActivity.this.mInterstitialAutocloseTimer.cancel();
				RichMediaActivity.this.mInterstitialAutocloseTimer = null;
			}
		}
	};
	
	OnPageLoadedListener mOnInterstitialLoadedListener = new OnPageLoadedListener()
	{
		
		@Override
		public void onPageLoaded()
		{
			if (RichMediaActivity.this.mInterstitialData != null
					&& RichMediaActivity.this.mInterstitialData.autoclose > 0)
				if (RichMediaActivity.this.mInterstitialAutocloseTimer == null
						&& !RichMediaActivity.this.mInterstitialAutocloseReset)
				{
					final InterstitialAutocloseTask autocloseTask = new InterstitialAutocloseTask(
							RichMediaActivity.this);
					RichMediaActivity.this.mInterstitialAutocloseTimer = new Timer();
					RichMediaActivity.this.mInterstitialAutocloseTimer
							.schedule(
									autocloseTask,
									RichMediaActivity.this.mInterstitialData.autoclose * 1000);
				}
			if (RichMediaActivity.this.mInterstitialData != null
					&& RichMediaActivity.this.mInterstitialData.showSkipButtonAfter > 0)
			{
				if (RichMediaActivity.this.mInterstitialCanCloseTimer == null)
				{
					final CanSkipTask skipTask = new CanSkipTask(
							RichMediaActivity.this);
					RichMediaActivity.this.mInterstitialCanCloseTimer = new Timer();
					RichMediaActivity.this.mInterstitialCanCloseTimer
							.schedule(
									skipTask,
									RichMediaActivity.this.mInterstitialData.showSkipButtonAfter * 1000);
				}
			} else
				RichMediaActivity.this.mCanClose = true;
			if (RichMediaActivity.this.mInterstitialLoadingTimer != null)
			{
				RichMediaActivity.this.mInterstitialLoadingTimer.cancel();
				RichMediaActivity.this.mInterstitialLoadingTimer = null;
			}
			RichMediaActivity.this.mPageLoaded = true;
			RichMediaActivity.this.mInterstitialController.pageLoaded();
		}
	};
	
	OnPageLoadedListener mOnWebBrowserLoadedListener = new OnPageLoadedListener()
	{
		
		@Override
		public void onPageLoaded()
		{
			RichMediaActivity.this.mPageLoaded = true;
			
		}
	};
	
	private ResourceHandler mHandler;
	
	@Override
	public void finish()
	{
		
		if (this.mAd != null)
		{
			switch (this.mType)
			{
				case TYPE_VIDEO:
					if (this.mAd.getType() == Const.VIDEO)
						AdManager.closeRunningAd(this.mAd, this.mResult);
					else if (this.mAd.getType() == Const.VIDEO_TO_INTERSTITIAL
							&& !this.mResult)
						AdManager.closeRunningAd(this.mAd, this.mResult);
					break;
				case TYPE_INTERSTITIAL:
					if (this.mAd.getType() == Const.INTERSTITIAL
							|| this.mAd.getType() == Const.VIDEO_TO_INTERSTITIAL
							|| this.mAd.getType() == Const.INTERSTITIAL_TO_VIDEO)
						AdManager.closeRunningAd(this.mAd, this.mResult);
					break;
			}
		}
		super.finish();
		
		RichMediaActivity.setActivityAnimation(this, this.mEnterAnim,
				this.mExitAnim);
	}
	
	public int getDipSize(final int argSize)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				argSize, this.getResources().getDisplayMetrics());
	}
	
	public FrameLayout getRootLayout()
	{
		return this.mRootLayout;
	}
	
	public void goBack()
	{
		if (this.mCustomView != null)
		{
			
			this.onHideCustomView();
			return;
		}
		switch (this.mType)
		{
			case TYPE_VIDEO:
				if (this.mCanClose)
					this.finish();
				break;
			case TYPE_INTERSTITIAL:
				if (this.mInterstitialView.canGoBack())
					this.mInterstitialView.goBack();
				else if (this.mCanClose)
				{
					this.mResult = true;
					this.setResult(Activity.RESULT_OK);
					this.finish();
				}
				break;
			case TYPE_BROWSER:
				if (this.mWebBrowserView.canGoBack())
					this.mWebBrowserView.goBack();
				else
					this.finish();
				break;
		}
		
	}
	
	private void initInterstitialView()
	{
		this.mInterstitialData = this.mAd.getInterstitial();
		this.mInterstitialAutocloseReset = false;
		
		this.setRequestedOrientation(this.mInterstitialData.orientation);
		final FrameLayout layout = new FrameLayout(this);
		this.mInterstitialView = new WebFrame(this, true, false, false);
		this.mInterstitialView.setBackgroundColor(Color.TRANSPARENT);
		this.mInterstitialView
				.setOnPageLoadedListener(this.mOnInterstitialLoadedListener);
		this.mInterstitialController = new InterstitialController(this,
				this.mInterstitialData);
		this.mInterstitialController.setBrowser(this.mInterstitialView);
		this.mInterstitialController.setBrowserView(this.mInterstitialView);
		this.mInterstitialController
				.setOnResetAutocloseListener(this.mOnResetAutocloseListener);
		layout.addView(this.mInterstitialController,
				new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT, Gravity.CENTER));
		if (this.mInterstitialData.showNavigationBars)
			this.mInterstitialController.show(0);
		if (this.mInterstitialData.showSkipButton)
		{
			
			this.mSkipButton = new ImageView(this);
			this.mSkipButton.setAdjustViewBounds(false);
			FrameLayout.LayoutParams params = null;
			
			int buttonSize = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, this.skipButtonSizeLand, this
							.getResources().getDisplayMetrics());
			
			int size = Math.min(
					this.getResources().getDisplayMetrics().widthPixels, this
							.getResources().getDisplayMetrics().heightPixels);
			buttonSize = (int) (size * 0.1);
			
			params = new FrameLayout.LayoutParams(buttonSize, buttonSize,
					Gravity.TOP | Gravity.RIGHT);
			
			if (this.mInterstitialData.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
			{
				final int margin = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 8, this.getResources()
								.getDisplayMetrics());
				params.topMargin = margin;
				params.rightMargin = margin;
			} else
			{
				final int margin = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources()
								.getDisplayMetrics());
				params.topMargin = margin;
				params.rightMargin = margin;
			}
			
			if (this.mInterstitialData.skipButtonImage != null
					&& this.mInterstitialData.skipButtonImage.length() > 0)
			{
				this.mSkipButton.setBackgroundDrawable(null);
				this.mResourceManager.fetchResource(this,
						this.mInterstitialData.skipButtonImage,
						ResourceManager.DEFAULT_SKIP_IMAGE_RESOURCE_ID);
			} else
				this.mSkipButton.setImageDrawable(mResourceManager.getResource(
						this, ResourceManager.DEFAULT_SKIP_IMAGE_RESOURCE_ID));
			this.mSkipButton
					.setOnClickListener(this.mOnInterstitialSkipListener);
			if (this.mInterstitialData.showSkipButtonAfter > 0)
			{
				this.mCanClose = false;
				this.mSkipButton.setVisibility(View.GONE);
				if (this.mInterstitialLoadingTimer == null)
				{
					final InterstitialLoadingTimeoutTask loadingTimeoutTask = new InterstitialLoadingTimeoutTask();
					this.mInterstitialLoadingTimer = new Timer();
					this.mInterstitialLoadingTimer.schedule(loadingTimeoutTask,
							Const.CONNECTION_TIMEOUT);
				}
				
			} else
			{
				this.mCanClose = true;
				this.mSkipButton.setVisibility(View.VISIBLE);
			}
			layout.addView(this.mSkipButton, params);
		} else
			this.mCanClose = false;
		this.mInterstitialView
				.setOnClickListener(this.mInterstitialClickListener);
		this.mRootLayout.addView(layout);
		switch (this.mInterstitialData.interstitialType)
		{
			case InterstitialData.INTERSTITIAL_MARKUP:
				this.mInterstitialView
						.setMarkup(this.mInterstitialData.interstitialMarkup);
				break;
			case InterstitialData.INTERSTITIAL_URL:
				this.mInterstitialView
						.loadUrl(this.mInterstitialData.interstitialUrl);
				break;
		}
	}
	
	private void initRootLayout()
	{
		this.mRootLayout = new FrameLayout(this);
		this.mRootLayout.setBackgroundColor(Color.BLACK);
	}
	
	private void initVideoView()
	{
		
		this.mVideoData = this.mAd.getVideo();
		this.setRequestedOrientation(this.mVideoData.orientation);
		if (this.mVideoData.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
		{
			if (this.mWindowWidth < this.mWindowHeight)
			{
				final int size = this.mWindowWidth;
				this.mWindowWidth = this.mWindowHeight;
				this.mWindowHeight = size;
			}
		} else if (this.mWindowHeight < this.mWindowWidth)
		{
			final int size = this.mWindowHeight;
			this.mWindowHeight = this.mWindowWidth;
			this.mWindowWidth = size;
		}
		this.mVideoWidth = this.mVideoData.width;
		this.mVideoHeight = this.mVideoData.height;
		if (this.mVideoWidth <= 0)
		{
			this.mVideoWidth = this.mWindowWidth;
			this.mVideoHeight = this.mWindowHeight;
		} else
		{
			final DisplayMetrics m = this.getResources().getDisplayMetrics();
			this.mVideoWidth = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, this.mVideoWidth, m);
			this.mVideoHeight = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, this.mVideoHeight, m);
			
			if (this.mVideoWidth > this.mWindowWidth)
				this.mVideoWidth = this.mWindowWidth;
			if (this.mVideoHeight > this.mWindowHeight)
				this.mVideoHeight = this.mWindowHeight;
		}
		
		this.mVideoLayout = new FrameLayout(this);
		this.mVideoView = new SDKVideoView(this, this.mVideoWidth,
				this.mVideoHeight, this.mVideoData.display);
		this.mVideoLayout.addView(this.mVideoView,
				new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT / 2,
						LayoutParams.WRAP_CONTENT / 2, Gravity.CENTER));// MATCH_PARENT
		// new FrameLayout.LayoutParams(this.mVideoWidth*3,
		// this.mVideoHeight*3, Gravity.CENTER));//在这个位置可以进行画面的改动
		if (this.mVideoData.showHtmlOverlay)
		{
			this.mOverlayView = new WebFrame(this, false, false, false);
			this.mOverlayView.setEnableZoom(false);
			this.mOverlayView.setOnClickListener(this.mOverlayClickListener);
			this.mOverlayView.setBackgroundColor(Color.TRANSPARENT);
			
			if (this.mVideoData.showHtmlOverlayAfter > 0)
			{
				this.mOverlayView.setVisibility(View.GONE);
				this.mVideoView.setOnTimeEventListener(
						this.mVideoData.showHtmlOverlayAfter,
						this.mOverlayShowListener);
			}
			if (this.mVideoData.htmlOverlayType == VideoData.OVERLAY_URL)
				this.mOverlayView.loadUrl(this.mVideoData.htmlOverlayUrl);
			else
				this.mOverlayView.setMarkup(this.mVideoData.htmlOverlayMarkup);
			final FrameLayout.LayoutParams overlayParams = new FrameLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			if (this.mVideoData.showBottomNavigationBar
					&& this.mVideoData.showTopNavigationBar)
			{
				overlayParams.bottomMargin = (int) (this.mWindowWidth * 0.11875);
				overlayParams.topMargin = (int) (this.mWindowWidth * 0.11875);
				overlayParams.gravity = Gravity.CENTER;
			} else if (this.mVideoData.showBottomNavigationBar
					&& !this.mVideoData.showTopNavigationBar)
			{
				overlayParams.bottomMargin = (int) (this.mWindowWidth * 0.11875);
				overlayParams.gravity = Gravity.TOP;
				
			} else if (this.mVideoData.showTopNavigationBar
					&& !this.mVideoData.showBottomNavigationBar)
			{
				
				overlayParams.topMargin = (int) (this.mWindowWidth * 0.11875);
				overlayParams.gravity = Gravity.BOTTOM;
			}
			this.mVideoLayout.addView(this.mOverlayView, overlayParams);
		}
		this.mMediaController = new MediaController(this, this.mVideoData);
		this.mVideoView.setMediaController(this.mMediaController);
		if (this.mVideoData.showNavigationBars)
			mMediaController.toggle();
		if (!this.mVideoData.pauseEvents.isEmpty())
			this.mMediaController
					.setOnPauseListener(this.mOnVideoPauseListener);
		if (!this.mVideoData.unpauseEvents.isEmpty())
			this.mMediaController
					.setOnUnpauseListener(this.mOnVideoUnpauseListener);
		if (!this.mVideoData.replayEvents.isEmpty())
			this.mMediaController
					.setOnReplayListener(this.mOnVideoReplayListener);
		this.mVideoLayout.addView(this.mMediaController,
				new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT, Gravity.FILL_HORIZONTAL));// fill_parent
		if (this.mVideoData.showSkipButton)
		{
			
			this.mSkipButton = new ImageView(this);
			this.mSkipButton.setAdjustViewBounds(false);
			FrameLayout.LayoutParams params = null;
			
			int buttonSize = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, this.skipButtonSizeLand, this
							.getResources().getDisplayMetrics());
			
			int size = Math.min(
					this.getResources().getDisplayMetrics().widthPixels, this
							.getResources().getDisplayMetrics().heightPixels);
			buttonSize = (int) (size * 0.09);
			
			params = new FrameLayout.LayoutParams(buttonSize, buttonSize,
					Gravity.TOP | Gravity.RIGHT);
			if (this.mVideoData.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
			{
				final int margin = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 8, this.getResources()
								.getDisplayMetrics());
				params.topMargin = margin;
				params.rightMargin = margin;
			} else
			{
				final int margin = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources()
								.getDisplayMetrics());
				params.topMargin = margin;
				params.rightMargin = margin;
			}
			
			if (this.mVideoData.skipButtonImage != null
					&& this.mVideoData.skipButtonImage.length() > 0)
				this.mResourceManager.fetchResource(this,
						this.mVideoData.skipButtonImage,
						ResourceManager.DEFAULT_SKIP_IMAGE_RESOURCE_ID);
			else
				this.mSkipButton.setImageDrawable(mResourceManager.getResource(
						this, ResourceManager.DEFAULT_SKIP_IMAGE_RESOURCE_ID));
			this.mSkipButton.setOnClickListener(this.mOnVideoSkipListener);
			if (this.mVideoData.showSkipButtonAfter > 0)
			{
				this.mCanClose = false;
				this.mSkipButton.setVisibility(View.GONE);
			} else
			{
				this.mCanClose = true;
				this.mSkipButton.setVisibility(View.VISIBLE);
			}
			this.mVideoLayout.addView(this.mSkipButton, params);
		} else
			this.mCanClose = false;
		if (this.mVideoData.showSkipButtonAfter > 0)
			this.mVideoView.setOnTimeEventListener(
					this.mVideoData.showSkipButtonAfter,
					this.mOnVideoCanCloseEventListener);
		final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				Gravity.CENTER);
		this.mLoadingView = new FrameLayout(this);
		final TextView loadingText = new TextView(this);
		loadingText.setText(Const.LOADING);
		this.mLoadingView.addView(loadingText, params);
		this.mVideoLayout.addView(this.mLoadingView,
				new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT, Gravity.CENTER));// fill_parent
		
		this.mVideoView.setOnPreparedListener(this.mOnVideoPreparedListener);
		this.mVideoView
				.setOnCompletionListener(this.mOnVideoCompletionListener);
		this.mVideoView.setOnErrorListener(this.mOnVideoErrorListener);
		this.mVideoView.setOnInfoListener(this.mOnVideoInfoListener);
		if (!this.mVideoData.startEvents.isEmpty())
			this.mVideoView.setOnStartListener(this.mOnVideoStartListener);
		if (!this.mVideoData.timeTrackingEvents.isEmpty())
		{
			final Set<Integer> keys = this.mVideoData.timeTrackingEvents
					.keySet();
			for (final Iterator<Integer> it = keys.iterator(); it.hasNext();)
			{
				final int key = it.next();
				this.mVideoView.setOnTimeEventListener(key,
						this.mOnVideoTimeEventListener);
			}
		}
		this.mVideoLastPosition = 0;
		String path = this.mVideoData.videoUrl;
		if (Util.CACHE_MODE)
		{
			URL url = null;
			try
			{
				url = new URL(this.mVideoData.videoUrl);
			} catch (MalformedURLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (url != null)
			{
				Util.ExternalName = "." + Util.getExtensionName(url.getPath());
			} else
			{
				Util.ExternalName = ".mp4";
			}
			
			Downloader downloader = new Downloader(path, RichMediaActivity.this);
			if (path.startsWith("http:") || path.startsWith("https:"))
			{
				downloader.download();
				Log.i(Const.TAG, "download starting");
			}
			File file = new File(Const.DOWNLOAD_PATH + Const.DOWNLOAD_PLAY_FILE
					+ Util.ExternalName);
			if (!file.exists())
			{
				Log.i(Const.TAG, "Play OnLine");
			} else
			{
				Log.i(Const.TAG, "Play LocalCacheFile");
				path = Const.DOWNLOAD_PATH + Const.DOWNLOAD_PLAY_FILE
						+ Util.ExternalName;
			}
		}
		
		this.mVideoView.setVideoPath(path);
		
	}
	
	private void initWebBrowserView(final boolean showExit)
	{
		this.mWebBrowserView = new WebFrame(this, true, true, showExit);
		this.mWebBrowserView
				.setOnPageLoadedListener(this.mOnWebBrowserLoadedListener);
		
		this.mRootLayout.addView(this.mWebBrowserView);
	}
	
	public void navigate(final String clickUrl)
	{
		switch (this.mType)
		{
			case TYPE_BROWSER:
				this.mWebBrowserView.loadUrl(clickUrl);
				break;
			case TYPE_INTERSTITIAL:
				this.mInterstitialView.loadUrl(clickUrl);
				break;
			default:
				final Intent intent = new Intent(this, RichMediaActivity.class);
				intent.setData(Uri.parse(clickUrl));
				this.startActivity(intent);
		}
	}
	
	@Override
	public void onConfigurationChanged(final Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		
	}
	
	@Override
	public void onCreate(final Bundle icicle)
	{
		
		super.onCreate(icicle);
		this.mResult = false;
		this.mPageLoaded = false;
		this.setResult(Activity.RESULT_CANCELED);
		final Window win = this.getWindow();
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		win.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		final Display display = this.getWindowManager().getDefaultDisplay();
		this.metrics = new DisplayMetrics();
		final WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(this.metrics);
		this.mWindowWidth = display.getWidth();
		this.mWindowHeight = display.getHeight();
		win.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		this.mType = RichMediaActivity.TYPE_UNKNOWN;
		final Intent intent = this.getIntent();
		final Bundle extras = intent.getExtras();
		if (extras == null || extras.getSerializable(Const.AD_EXTRA) == null)
		{
			this.uri = intent.getData();
			if (this.uri == null)
			{
				
				this.finish();
				return;
			}
			this.mType = RichMediaActivity.TYPE_BROWSER;
		} else
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		mHandler = new ResourceHandler(this);
		
		this.mResourceManager = new ResourceManager(this, this.mHandler);
		this.initRootLayout();
		
		if (this.mType == RichMediaActivity.TYPE_BROWSER)
		{
			this.initWebBrowserView(true);
			this.mWebBrowserView.loadUrl(this.uri.toString());
			this.mEnterAnim = Util
					.getEnterAnimation(RichMediaAd.ANIMATION_FADE_IN);
			this.mExitAnim = Util
					.getExitAnimation(RichMediaAd.ANIMATION_FADE_IN);
		} else
		{
			this.mAd = (RichMediaAd) extras.getSerializable(Const.AD_EXTRA);
			this.mEnterAnim = Util.getEnterAnimation(this.mAd.getAnimation());
			this.mExitAnim = Util.getExitAnimation(this.mAd.getAnimation());
			
			this.mCanClose = false;
			this.mType = extras.getInt(Const.AD_TYPE_EXTRA, -1);
			if (this.mType == -1)
				switch (this.mAd.getType())
				{
					case Const.VIDEO:
					case Const.VIDEO_TO_INTERSTITIAL:
						this.mType = RichMediaActivity.TYPE_VIDEO;
						break;
					case Const.INTERSTITIAL:
					case Const.INTERSTITIAL_TO_VIDEO:
						this.mType = RichMediaActivity.TYPE_INTERSTITIAL;
						break;
				}
			switch (this.mType)
			{
				case TYPE_VIDEO:
					this.initVideoView();
					break;
				case TYPE_INTERSTITIAL:
					this.initInterstitialView();
					break;
			}
		}
		
		this.setContentView(this.mRootLayout);
	}
	
	@Override
	protected void onDestroy()
	{
		mMediaController = null;
		mResourceManager.releaseInstance();
		if (this.mVideoView != null)
			this.mVideoView.destroy();
		super.onDestroy();
	}
	
	public void onHideCustomView()
	{
		
		if (this.mCustomView != null)
		{
			
			this.mCustomView.setVisibility(View.GONE);
			this.mCustomView = null;
			if (this.mCustomVideoView != null)
			{
				try
				{
					
					this.mCustomVideoView.stopPlayback();
				} catch (final Exception e)
				{
				}
				this.mCustomVideoView = null;
			}
		}
		
		this.mCustomViewCallback.onCustomViewHidden();
		this.mRootLayout.setVisibility(View.VISIBLE);
		this.setContentView(this.mRootLayout);
	}
	
	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			this.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onPause()
	{
		
		super.onPause();
		switch (this.mType)
		{
			case TYPE_VIDEO:
				
				this.mVideoLastPosition = this.mVideoView.getCurrentPosition();
				this.mVideoView.stopPlayback();
				this.mRootLayout.removeView(this.mVideoLayout);
				if (this.mVideoTimeoutTimer != null)
				{
					this.mVideoTimeoutTimer.cancel();
					this.mVideoTimeoutTimer = null;
				}
				break;
			case TYPE_INTERSTITIAL:
				if (this.mInterstitialLoadingTimer != null)
				{
					this.mInterstitialLoadingTimer.cancel();
					this.mInterstitialLoadingTimer = null;
				}
				if (this.mInterstitialAutocloseTimer != null)
				{
					this.mInterstitialAutocloseTimer.cancel();
					this.mInterstitialAutocloseTimer = null;
				}
				if (this.mInterstitialCanCloseTimer != null)
				{
					this.mInterstitialCanCloseTimer.cancel();
					this.mInterstitialCanCloseTimer = null;
				}
				/*
				 * pause the video of the html tag
				 * 
				 * @author yyc
				 */
				if (mCustomVideoView != null)
					mCustomVideoView.pause();
				break;
		}
		
	}
	
	@Override
	protected void onResume()
	{
		
		super.onResume();
		switch (this.mType)
		{
			case TYPE_VIDEO:
				
				this.mRootLayout.addView(this.mVideoLayout);
				this.mVideoView.seekTo(this.mVideoLastPosition);
				this.mVideoView.start();
				if (this.mVideoTimeoutTimer == null)
				{
					final VideoTimeoutTask autocloseTask = new VideoTimeoutTask(
							RichMediaActivity.this);
					this.mVideoTimeoutTimer = new Timer();
					this.mVideoTimeoutTimer.schedule(autocloseTask,
							Const.VIDEO_LOAD_TIMEOUT);
				}
				
				break;
			case TYPE_INTERSTITIAL:
				switch (this.mInterstitialData.interstitialType)
				{
					case InterstitialData.INTERSTITIAL_MARKUP:
						if (!this.mPageLoaded)
							this.mInterstitialView
									.setMarkup(this.mInterstitialData.interstitialMarkup);
						break;
					case InterstitialData.INTERSTITIAL_URL:
						if (!this.mPageLoaded)
							this.mInterstitialView
									.loadUrl(this.mInterstitialData.interstitialUrl);
						break;
				}
				break;
			case TYPE_BROWSER:
				break;
		}
		
	}
	
	public void onShowCustomView(final View view,
			final CustomViewCallback callback)
	{
		
		if (view instanceof FrameLayout)
		{
			
			this.mCustomView = (FrameLayout) view;
			this.mCustomViewCallback = callback;
			if (this.mCustomView.getFocusedChild() instanceof VideoView)
			{
				
				this.mCustomVideoView = (VideoView) this.mCustomView
						.getFocusedChild();
				this.mCustomVideoView
						.setOnCompletionListener(new OnCompletionListener()
						{
							
							@Override
							public void onCompletion(final MediaPlayer mp)
							{
								
								RichMediaActivity.this.onHideCustomView();
							}
						});
				this.mCustomVideoView.start();
			}
			this.mRootLayout.setVisibility(View.GONE);
			this.mCustomView.setVisibility(View.VISIBLE);
			this.setContentView(this.mCustomView);
		}
	}
	
	public void playVideo()
	{
		
		switch (this.mType)
		{
			case TYPE_VIDEO:
				if (this.mMediaController != null)
					this.mMediaController.replay();
				break;
			case TYPE_INTERSTITIAL:
				if (this.mAd.getType() == Const.INTERSTITIAL_TO_VIDEO)
				{
					
					final Intent intent = new Intent(this,
							RichMediaActivity.class);
					intent.putExtra(Const.AD_EXTRA, this.mAd);
					intent.putExtra(Const.AD_TYPE_EXTRA,
							RichMediaActivity.TYPE_VIDEO);
					try
					{
						this.startActivity(intent);
						RichMediaActivity.setActivityAnimation(this,
								this.mEnterAnim, this.mExitAnim);
						this.mResult = true;
						this.setResult(Activity.RESULT_OK);
					} catch (final Exception e)
					{
					}
				}
				break;
		}
	}
	
	public void replayVideo()
	{
		if (this.mMediaController != null)
			this.mMediaController.replay();
	}
	
}
