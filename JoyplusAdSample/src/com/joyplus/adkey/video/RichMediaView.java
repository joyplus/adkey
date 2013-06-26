package com.joyplus.adkey.video;

import static com.joyplus.adkey.Const.AD_EXTRA;

import java.io.File;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.AdManager;
import com.joyplus.adkey.AdRequest;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.RequestRichMediaAd;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.download.Downloader;
import com.joyplus.adkey.video.InterstitialController.OnResetAutocloseListener;
import com.joyplus.adkey.video.MediaController.OnPauseListener;
import com.joyplus.adkey.video.MediaController.OnReplayListener;
import com.joyplus.adkey.video.MediaController.OnUnpauseListener;
import com.joyplus.adkey.video.RichMediaActivity.CanSkipTask;
import com.joyplus.adkey.video.RichMediaActivity.InterstitialAutocloseTask;
import com.joyplus.adkey.video.SDKVideoView.OnStartListener;
import com.joyplus.adkey.video.SDKVideoView.OnTimeEventListener;
import com.joyplus.adkey.video.WebViewClient.OnPageLoadedListener;
import com.joyplus.adkey.widget.Log;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.Location;
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
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class RichMediaView extends FrameLayout
{
	
	private Context mContext = null;
	
	public static final int TYPE_UNKNOWN = -1;
	
	public static final int TYPE_BROWSER = 0;
	public static final int TYPE_VIDEO = 1;
	public static final int TYPE_INTERSTITIAL = 2;
	
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
	
	public RichMediaView(Context context, RichMediaAd ad,FrameLayout layout)
	{
		super(context);
		mContext = context;
		mAd = ad;
		initVideo(layout);
	}
	
	public RichMediaView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	public RichMediaView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	public RichMediaView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	public void initVideo(FrameLayout layout)
	{
		mVideoLayout = layout;
		this.mResult = false;
		this.mPageLoaded = false;
		
		this.mWindowWidth = layout.getWidth();
		this.mWindowHeight = layout.getHeight();
		this.mType = RichMediaActivity.TYPE_UNKNOWN;
		
		this.mEnterAnim = Util.getEnterAnimation(this.mAd.getAnimation());
		this.mExitAnim = Util.getExitAnimation(this.mAd.getAnimation());
		
		this.mCanClose = false;
		this.mType = -1;
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
		}
	}
	
	private void initVideoView()
	{
		this.mVideoData = this.mAd.getVideo();
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
		}
		
		this.mVideoView = new SDKVideoView(mContext, this.mVideoWidth,
				this.mVideoHeight, this.mVideoData.display);
		this.mVideoLayout.addView(mVideoView,
		 new FrameLayout.LayoutParams(this.mVideoWidth,
				 this.mVideoHeight, Gravity.CENTER));//在这个位置可以进行画面的改动
		
		this.mMediaController = new MediaController(mContext, mVideoData);
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
			
//			this.mSkipButton = new ImageView(this);
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
			
//			if (this.mVideoData.skipButtonImage != null
//					&& this.mVideoData.skipButtonImage.length() > 0)
//				this.mResourceManager.fetchResource(this,
//						this.mVideoData.skipButtonImage,
//						ResourceManager.DEFAULT_SKIP_IMAGE_RESOURCE_ID);
//			else
//				this.mSkipButton.setImageDrawable(mResourceManager.getResource(
//						this, ResourceManager.DEFAULT_SKIP_IMAGE_RESOURCE_ID));
//			this.mSkipButton.setOnClickListener(this.mOnVideoSkipListener);
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
//		if (this.mVideoData.showSkipButtonAfter > 0)
//			this.mVideoView.setOnTimeEventListener(
//					this.mVideoData.showSkipButtonAfter,
//					this.mOnVideoCanCloseEventListener);
		final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				Gravity.CENTER);
		this.mLoadingView = new FrameLayout(mContext);
		final TextView loadingText = new TextView(mContext);
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
			
			Downloader downloader = new Downloader(path, mContext);
			if (path.startsWith("http:") || path.startsWith("https:"))
			{
				downloader.download();
				Log.i(Const.TAG, "download starting");
			}
			File file = new File(Const.DOWNLOAD_PATH + Util.VideoFileDir+Const.DOWNLOAD_PLAY_FILE
					+ Util.ExternalName);
			if (!file.exists())
			{
				Log.i(Const.TAG, "Play OnLine");
			} else
			{
				Log.i(Const.TAG, "Play LocalCacheFile");
				path = Const.DOWNLOAD_PATH + Util.VideoFileDir+Const.DOWNLOAD_PLAY_FILE
						+ Util.ExternalName;
			}
		}
		
		this.mVideoView.setVideoPath(path);
	}
	
	private final OnTimeEventListener mOverlayShowListener = new OnTimeEventListener()
	{
		
		@Override
		public void onTimeEvent(final int time)
		{
			
			if (mOverlayView != null)
			{
				mOverlayView.setVisibility(View.VISIBLE);
				mOverlayView.requestLayout();
			}
		}
	};
	
	private final OnClickListener mOverlayClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(final View arg0)
		{
			
			if (mMediaController != null)
				mMediaController.toggle();
		}
	};
	
	OnErrorListener mOnVideoErrorListener = new OnErrorListener()
	{
		
		@Override
		public boolean onError(final MediaPlayer mp, final int what,
				final int extra)
		{
			/*
			 * 进行相关处理
			 */
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
//				finish();
			}
		}
	};
	
	OnPreparedListener mOnVideoPreparedListener = new OnPreparedListener()
	{
		
		@Override
		public void onPrepared(final MediaPlayer mp)
		{
			
			if (mVideoTimeoutTimer != null)
			{
				mVideoTimeoutTimer.cancel();
				mVideoTimeoutTimer = null;
			}
			if (mLoadingView != null)
				mLoadingView.setVisibility(View.GONE);
			if (mVideoData.showNavigationBars)
				mMediaController.setVisibility(View.VISIBLE);
			mVideoView.requestFocus();
			/*
			 * cann't invoke the start() function
			 */
			if(mMediaController!=null)
				mMediaController.replay();
		}
	};
	
	OnCompletionListener mOnVideoCompletionListener = new OnCompletionListener()
	{
		
		@Override
		public void onCompletion(final MediaPlayer mp)
		{
			
			final Vector<String> trackers = mVideoData.completeEvents;
			for (int i = 0; i < trackers.size(); i++)
			{
				
				final TrackEvent event = new TrackEvent();
				event.url = trackers.get(i);
				event.timestamp = System.currentTimeMillis();
				TrackerService.requestTrack(event);
			}
			if (mType == RichMediaActivity.TYPE_VIDEO
					&& mAd.getType() == Const.VIDEO_TO_INTERSTITIAL)
			{
				
			}
			if(mVideoData!=null&&mVideoData.width>0)
			{
				replayVideo();
			}else{
				mResult = true;
			}
		}
	};
	
	OnStartListener mOnVideoStartListener = new OnStartListener()
	{
		
		@Override
		public void onVideoStart()
		{
			
			final Vector<String> trackers =mVideoData.startEvents;
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
			
			final Vector<String> trackers = mVideoData.pauseEvents;
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
			
			final Vector<String> trackers = mVideoData.unpauseEvents;
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
			
			final Vector<String> trackers = mVideoData.timeTrackingEvents
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
			mCanClose = true;
			if (mVideoData.showSkipButton
					&& mSkipButton != null)
			{
				
				mSkipButton
						.setImageDrawable(mResourceManager.getResource(
								mContext,
								ResourceManager.DEFAULT_SKIP_IMAGE_RESOURCE_ID));
				
				mSkipButton.setVisibility(View.VISIBLE);
			}
		}
	};
	
	OnClickListener mOnVideoSkipListener = new OnClickListener()
	{
		
		@Override
		public void onClick(final View v)
		{
			
			final Vector<String> trackers = mVideoData.skipEvents;
			for (int i = 0; i < trackers.size(); i++)
			{
				
				final TrackEvent event = new TrackEvent();
				event.url = trackers.get(i);
				event.timestamp = System.currentTimeMillis();
				TrackerService.requestTrack(event);
			}
			
			mResult = true;
		}
	};
	
	OnReplayListener mOnVideoReplayListener = new OnReplayListener()
	{
		
		@Override
		public void onVideoReplay()
		{
			
			final Vector<String> trackers = mVideoData.replayEvents;
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
			
			if (mInterstitialController != null)
			{
				mInterstitialController.toggle();
				mInterstitialController.resetAutoclose();
			}
		}
	};
	
	OnClickListener mOnInterstitialSkipListener = new OnClickListener()
	{
		
		@Override
		public void onClick(final View v)
		{
			
			mResult = true;
		}
	};
	
	OnResetAutocloseListener mOnResetAutocloseListener = new OnResetAutocloseListener()
	{
		
		@Override
		public void onResetAutoclose()
		{
			
			mInterstitialAutocloseReset = true;
			if (mInterstitialAutocloseTimer != null)
			{
				mInterstitialAutocloseTimer.cancel();
				mInterstitialAutocloseTimer = null;
			}
		}
	};
	
	OnPageLoadedListener mOnInterstitialLoadedListener = new OnPageLoadedListener()
	{
		
		@Override
		public void onPageLoaded()
		{
			if (mInterstitialData != null
					&& mInterstitialData.autoclose > 0)
				if (mInterstitialAutocloseTimer == null
						&& !mInterstitialAutocloseReset)
				{
					
				}
		}
	};
	
	OnPageLoadedListener mOnWebBrowserLoadedListener = new OnPageLoadedListener()
	{
		
		@Override
		public void onPageLoaded()
		{
			mPageLoaded = true;
			
		}
	};
	
	public void replayVideo()
	{
		if (this.mMediaController != null)
			this.mMediaController.replay();
	}
}