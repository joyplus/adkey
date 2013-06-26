package com.joyplus.adkey.video;

import static com.joyplus.adkey.Const.AD_EXTRA;

import java.io.File;
import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;

import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.AdManager;
import com.joyplus.adkey.AdRequest;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.RequestRichMediaAd;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.download.Downloader;
import com.joyplus.adkey.widget.Log;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.Location;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
		
//		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		this.mType = RichMediaActivity.TYPE_UNKNOWN;
		
//		mHandler = new ResourceHandler(this);
		
//		this.mResourceManager = new ResourceManager(this, this.mHandler);
		// this.initRootLayout();
		
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
		this.mVideoView = new SDKVideoView(mContext, this.mVideoWidth,
				this.mVideoHeight, this.mVideoData.display);
		mVideoLayout.addView(mVideoView);
//		this.mVideoLayout.addView(this.mVideoView,
//		 new FrameLayout.LayoutParams(this.mVideoWidth,
//		 this.mVideoHeight, Gravity.CENTER));//在这个位置可以进行画面的改动
		
		this.mMediaController = new MediaController(mContext, mVideoData);
		this.mVideoView.setMediaController(this.mMediaController);
		if (this.mVideoData.showNavigationBars)
			mMediaController.toggle();
//		if (!this.mVideoData.pauseEvents.isEmpty())
//			this.mMediaController
//					.setOnPauseListener(this.mOnVideoPauseListener);
//		if (!this.mVideoData.unpauseEvents.isEmpty())
//			this.mMediaController
//					.setOnUnpauseListener(this.mOnVideoUnpauseListener);
//		if (!this.mVideoData.replayEvents.isEmpty())
//			this.mMediaController
//					.setOnReplayListener(this.mOnVideoReplayListener);
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
//		final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
//				Gravity.CENTER);
//		this.mLoadingView = new FrameLayout(this);
//		final TextView loadingText = new TextView(this);
//		loadingText.setText(Const.LOADING);
//		this.mLoadingView.addView(loadingText, params);
//		this.mVideoLayout.addView(this.mLoadingView,
//				new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
//						LayoutParams.MATCH_PARENT, Gravity.CENTER));// fill_parent
		
//		this.mVideoView.setOnPreparedListener(this.mOnVideoPreparedListener);
//		this.mVideoView
//				.setOnCompletionListener(this.mOnVideoCompletionListener);
//		this.mVideoView.setOnErrorListener(this.mOnVideoErrorListener);
//		this.mVideoView.setOnInfoListener(this.mOnVideoInfoListener);
//		if (!this.mVideoData.startEvents.isEmpty())
//			this.mVideoView.setOnStartListener(this.mOnVideoStartListener);
//		if (!this.mVideoData.timeTrackingEvents.isEmpty())
//		{
//			final Set<Integer> keys = this.mVideoData.timeTrackingEvents
//					.keySet();
//			for (final Iterator<Integer> it = keys.iterator(); it.hasNext();)
//			{
//				final int key = it.next();
//				this.mVideoView.setOnTimeEventListener(key,
//						this.mOnVideoTimeEventListener);
//			}
//		}
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
}