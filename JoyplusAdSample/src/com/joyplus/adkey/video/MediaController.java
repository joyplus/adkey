package com.joyplus.adkey.video;import java.lang.ref.WeakReference;
import java.util.Formatter;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.TextView;

import com.joyplus.adkey.Const;
import com.joyplus.adkey.Util;

public class MediaController extends FrameLayout {

	private static final int DEFAULT_TIMEOUT = 5000;
	private static final int FADE_OUT = 1;
	private static final int SHOW_PROGRESS = 2;

	private double buttonWidthPercent = 0.09;

	private android.widget.MediaController.MediaPlayerControl mPlayer;
	private Context mContext;
	private ResourceManager mResourceManager;

	private LinearLayout mTopBar;
	private LinearLayout mBottomBar;

	private AspectRatioImageViewWidth mPauseButton;
	private AspectRatioImageViewWidth mReplayButton;
	private TextView mLeftTime;
	private VideoData mVideoData;
	StringBuilder mFormatBuilder;
	Formatter mFormatter;
	private boolean mShowing;
	private boolean mFixed;
	private OnUnpauseListener mOnUnpauseListener;
	private OnPauseListener mOnPauseListener;
	private OnReplayListener mOnReplayListener;

	public MediaController(Context context, VideoData videoData) {
		super(context);
		this.setVisibility(View.GONE);
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		mShowing = false;
		mFixed = false;
		mContext = context;
		mVideoData = videoData;
		if (mVideoData == null) {
			throw new IllegalArgumentException("Video info cannot be null");
		}
		mFormatBuilder = new StringBuilder();
		mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
		mResourceManager = new ResourceManager(mContext, mHandler);
		buildNavigationBarView(metrics);
	}

	public void setMediaPlayer(MediaPlayerControl player) {
		mPlayer = player;
		updatePausePlay();
	}

	protected void buildNavigationBarView(DisplayMetrics metrics) {
		int barHeight = metrics.widthPixels;
		this.setLayoutParams(new FrameLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT));
		mTopBar = new LinearLayout(mContext);
		mTopBar.setOrientation(LinearLayout.HORIZONTAL);
		mTopBar.setWeightSum(1);
		mTopBar.setBackgroundColor(Color.TRANSPARENT);
		FrameLayout.LayoutParams paramsFrame = new FrameLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				(int)(barHeight*0.119));
		paramsFrame.gravity = Gravity.TOP|Gravity.FILL_HORIZONTAL;
		mTopBar.setGravity(Gravity.CENTER_VERTICAL);
		int padding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
				.getDisplayMetrics());
		this.addView(mTopBar, paramsFrame);

		mBottomBar = new LinearLayout(mContext);
		mBottomBar.setOrientation(LinearLayout.HORIZONTAL);
		mBottomBar.setGravity(Gravity.CENTER_VERTICAL);
		paramsFrame = new FrameLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				(int)(barHeight*0.119));
		paramsFrame.gravity = Gravity.BOTTOM;
		mBottomBar.setWeightSum(1);
		mBottomBar.setPadding(padding, 0, padding, 0);
		mBottomBar.setBackgroundColor(Color.TRANSPARENT);

		this.addView(mBottomBar, paramsFrame);

		LinearLayout buttonPanel = new LinearLayout(mContext);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		params.gravity = Gravity.LEFT;
		buttonPanel.setOrientation(LinearLayout.HORIZONTAL);
		buttonPanel.setGravity(Gravity.CENTER_VERTICAL);
		buttonPanel.setBackgroundColor(Color.GREEN);

		mReplayButton = new AspectRatioImageViewWidth(mContext);
		params = new LinearLayout.LayoutParams(
				(int) (barHeight*buttonWidthPercent),
				(int) (barHeight*buttonWidthPercent));
		params.gravity = Gravity.CENTER_VERTICAL;
		mReplayButton.setAdjustViewBounds(true);
		mReplayButton.setPadding(padding, padding, padding, padding);
		mBottomBar.addView(mReplayButton,params);

		mPauseButton = new AspectRatioImageViewWidth(mContext);
		params = new LinearLayout.LayoutParams(
				(int) (barHeight*buttonWidthPercent),
				(int) (barHeight*buttonWidthPercent));
		params.gravity = Gravity.CENTER_VERTICAL;
		mPauseButton.setPadding(padding, padding, padding, padding);
		mPauseButton.setAdjustViewBounds(true);
		mBottomBar.addView(mPauseButton,params);

		mLeftTime = new AutoResizeTextView(mContext);
		params = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_VERTICAL;
		mLeftTime.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		mLeftTime.setPadding(padding, padding, padding, padding);
		mLeftTime.setGravity(Gravity.CENTER_VERTICAL);
		mLeftTime.setTextSize(23);
		mBottomBar.addView(mLeftTime, params);
		View view = new View(mContext);
		params = new LinearLayout.LayoutParams(
				0,
				0);
		params.weight = 1;
		params.gravity = Gravity.CENTER_VERTICAL;
		mBottomBar.addView(view, params);
		
		initNavigationBarControllerView(padding,metrics);
	}

	private void initNavigationBarControllerView(int padding,DisplayMetrics metrics) {
		int barHeight = metrics.widthPixels;
		if (!mVideoData.showBottomNavigationBar) {
			mBottomBar.setVisibility(View.GONE);
		} else {
			mBottomBar.setVisibility(View.VISIBLE);
			if ((mVideoData.bottomNavigationBarBackground != null)
					&& (mVideoData.bottomNavigationBarBackground.length() > 0)) {
				mResourceManager.fetchResource(mContext,
						mVideoData.bottomNavigationBarBackground,
						ResourceManager.DEFAULT_BOTTOMBAR_BG_RESOURCE_ID);
			} else {
				mBottomBar.setBackgroundDrawable(
				mResourceManager
						.getResource(mContext,ResourceManager.DEFAULT_BOTTOMBAR_BG_RESOURCE_ID));

			}
			if (mPauseButton != null) {
				if ((mVideoData.pauseButtonImage != null)
						&& (mVideoData.pauseButtonImage.length() > 0)) {
					mPauseButton.setBackgroundDrawable(null);
					mResourceManager.fetchResource(mContext,mVideoData.pauseButtonImage,
							ResourceManager.DEFAULT_PAUSE_IMAGE_RESOURCE_ID);
				} else {
					mPauseButton
					.setImageDrawable(mResourceManager
							.getResource(mContext,ResourceManager.DEFAULT_PAUSE_IMAGE_RESOURCE_ID));
				}
				if ((mVideoData.playButtonImage != null)
						&& (mVideoData.playButtonImage.length() > 0)) {
					mResourceManager.fetchResource(mContext,mVideoData.playButtonImage,
							ResourceManager.DEFAULT_PLAY_IMAGE_RESOURCE_ID);
				}
				mPauseButton.setOnClickListener(mPauseListener);
				if (mVideoData.showPauseButton) {
					mPauseButton.setVisibility(View.VISIBLE);
				} else {
					mPauseButton.setVisibility(View.GONE);
				}
			}
			if (mReplayButton != null) {
				if ((mVideoData.replayButtonImage != null)
						&& (mVideoData.replayButtonImage.length() > 0))
				{
					mReplayButton.setImageDrawable(null);
					mResourceManager.fetchResource(mContext,
							mVideoData.replayButtonImage,
							ResourceManager.DEFAULT_REPLAY_IMAGE_RESOURCE_ID);
				} else {
					mReplayButton
					.setImageDrawable(mResourceManager
							.getResource(mContext,ResourceManager.DEFAULT_REPLAY_IMAGE_RESOURCE_ID));
				}
				mReplayButton.setOnClickListener(mReplayListener);
				if (mVideoData.showReplayButton) {
					mReplayButton.setVisibility(View.VISIBLE);
				} else {
					mReplayButton.setVisibility(View.GONE);
				}
			}
			if (mLeftTime != null) {
				if (mVideoData.showTimer) {
					mLeftTime.setVisibility(View.VISIBLE);
				} else {
					mLeftTime.setVisibility(View.GONE);
				}
			}
			if (!mVideoData.icons.isEmpty()) {
				for (int i = 0; i < mVideoData.icons.size(); i++) {
					NavIconData iconData = mVideoData.icons.get(i);
					NavIcon icon = new NavIcon(mContext, iconData);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							(int) (barHeight*buttonWidthPercent),
							(int) (barHeight*buttonWidthPercent));
					mBottomBar.addView(icon, params);
				}
			}
		}
		if (!mVideoData.showTopNavigationBar) {
			mTopBar.setVisibility(View.GONE);
		} else {
			mTopBar.setVisibility(View.VISIBLE);
			if ((mVideoData.topNavigationBarBackground != null)
					&& (mVideoData.topNavigationBarBackground.length() > 0)) {
				mResourceManager.fetchResource(mContext,
						mVideoData.topNavigationBarBackground,
						ResourceManager.DEFAULT_TOPBAR_BG_RESOURCE_ID);
			} else {
				mTopBar.setBackgroundDrawable(
						mResourceManager
						.getResource(mContext,ResourceManager.DEFAULT_TOPBAR_BG_RESOURCE_ID));
			}
		}
		if (!mVideoData.showNavigationBars) {
			this.setVisibility(View.GONE);
		}
	}

	public void show() {
		show(DEFAULT_TIMEOUT);
	}

	public void show(int timeout) {
		if (timeout == 0) {
			mFixed = true;
		}
		if (!mShowing) {
			this.setVisibility(View.VISIBLE);
			mShowing = true;
		}
		refreshProgress();
		mHandler.removeMessages(FADE_OUT);
		if ((timeout != 0) && (!mFixed)) {
			Message msg = mHandler.obtainMessage(FADE_OUT);
			mHandler.sendMessageDelayed(msg, timeout);
		}
	}

	public boolean isShowing() {
		return mShowing;
	}	public void hide() {
		mFixed = false;
		if (canToggle()) {
			if (mShowing) {
				mHandler.removeMessages(SHOW_PROGRESS);
				this.setVisibility(View.GONE);
				mShowing = false;
			}
		}
	}

	public void resizeTopBar(int bottom) {
		if (bottom <= 0)
			return;
		int padding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		if (this.mTopBar != null) {
			ViewGroup.LayoutParams params = this.mTopBar.getLayoutParams();
			params.height = bottom + padding;
			this.mTopBar.setLayoutParams(params);
		}
	}

	public void replay() {
		if (mPlayer != null) {
			mPlayer.seekTo(0);
			mPlayer.start();
		}
		refreshProgress();
		if (mOnReplayListener != null) {
			mOnReplayListener.onVideoReplay();
		}
	}

	private static class ResourceHandler extends Handler {

		private final WeakReference<MediaController> mController;

		public ResourceHandler(MediaController controller) {
			mController = new WeakReference<MediaController>(controller);
		}

		@Override
		public void handleMessage(Message msg) {
			MediaController wController = mController.get();
			if(wController != null){
				wController.handleMessage(msg);
			}
		}
	};

	private ResourceHandler mHandler = new ResourceHandler(this);

	private void handleMessage(Message msg) {
		switch (msg.what) {
		case FADE_OUT:
			hide();
			break;
		case SHOW_PROGRESS:
			refreshProgress();
			break;
		case ResourceManager.RESOURCE_LOADED_MSG:
			switch (msg.arg1) {
			case ResourceManager.DEFAULT_TOPBAR_BG_RESOURCE_ID:
				if (mTopBar != null) {
					Drawable d = mResourceManager
							.getResource(mContext,ResourceManager.DEFAULT_TOPBAR_BG_RESOURCE_ID);
					if (d != null) {
						mTopBar.setBackgroundDrawable(d);
					}
				}
				break;
			case ResourceManager.DEFAULT_BOTTOMBAR_BG_RESOURCE_ID:
				if (mBottomBar != null) {
					Drawable d = mResourceManager
							.getResource(mContext,ResourceManager.DEFAULT_BOTTOMBAR_BG_RESOURCE_ID);
					if (d != null) {
						mBottomBar.setBackgroundDrawable(d);
					}
				}
				break;
			case ResourceManager.DEFAULT_PLAY_IMAGE_RESOURCE_ID:
				if (mPauseButton != null) {
					updatePausePlay();
				}
				break;
			case ResourceManager.DEFAULT_PAUSE_IMAGE_RESOURCE_ID:
				if (mPauseButton != null) {
					updatePausePlay();
				}
				break;
			case ResourceManager.DEFAULT_REPLAY_IMAGE_RESOURCE_ID:
				if (mReplayButton != null) {
					updateReplay();
				}
				break;
			}
			requestLayout();
			break;
		}
	}

	private String stringForTime(int timeMs) {
		int totalSeconds = timeMs / 1000;

		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		mFormatBuilder.setLength(0);
		if (hours > 0) {
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
					.toString();
		} else if (minutes > 0) {
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		} else {
			return mFormatter.format("0:%02d", seconds).toString();
		}
	}

	private int setProgress() {
		if (mPlayer == null) {
			return 0;
		}
		int position = mPlayer.getCurrentPosition();
		int duration = mPlayer.getDuration();
		int timeLeft = duration - position;
		if (mLeftTime != null) {
			mLeftTime.setText(stringForTime(timeLeft));
		}
		return position;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode != KeyEvent.KEYCODE_BACK
				&& keyCode != KeyEvent.KEYCODE_VOLUME_UP
				&& keyCode != KeyEvent.KEYCODE_VOLUME_DOWN
				&& keyCode != KeyEvent.KEYCODE_MENU
				&& keyCode != KeyEvent.KEYCODE_CALL
				&& keyCode != KeyEvent.KEYCODE_ENDCALL) {
			if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
					|| keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
				doPauseResume();
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
					&& (mPlayer != null) && mPlayer.isPlaying()) {
				mPlayer.pause();
				if (mOnPauseListener != null) {
					mOnPauseListener.onVideoPause();
				}
			} else {
				toggle();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private View.OnClickListener mPauseListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			doPauseResume();
		}
	};

	private void updateReplay() {
		if (mReplayButton == null)
			return;
		if (mResourceManager.containsResource(ResourceManager.DEFAULT_REPLAY_IMAGE_RESOURCE_ID)) {
			Drawable d = mResourceManager
					.getResource(mContext,ResourceManager.DEFAULT_REPLAY_IMAGE_RESOURCE_ID);
			mReplayButton.setImageDrawable(d);
		} else {
			Drawable d = mResourceManager
					.getResource(mContext,ResourceManager.DEFAULT_REPLAY_IMAGE_RESOURCE_ID);
			mReplayButton.setImageDrawable(d);
		}
	}

	private void updatePausePlay() {
		if (mPauseButton == null)
			return;

		if ((mPlayer != null) && (mPlayer.isPlaying())) {
			if (mResourceManager.containsResource(ResourceManager.DEFAULT_PAUSE_IMAGE_RESOURCE_ID)) {
				Drawable d = mResourceManager
						.getResource(mContext,ResourceManager.DEFAULT_PAUSE_IMAGE_RESOURCE_ID);
				mPauseButton.setImageDrawable(d);
			} else {
				Drawable d = mResourceManager
						.getResource(mContext,ResourceManager.DEFAULT_PAUSE_IMAGE_RESOURCE_ID);
				mPauseButton.setImageDrawable(d);
			}
		} else {
			if (mResourceManager.containsResource(ResourceManager.DEFAULT_PLAY_IMAGE_RESOURCE_ID)) {
				Drawable d = mResourceManager
						.getResource(mContext,ResourceManager.DEFAULT_PLAY_IMAGE_RESOURCE_ID);
				mPauseButton.setImageDrawable(d);
			} else {
				Drawable d = mResourceManager
						.getResource(mContext,ResourceManager.DEFAULT_PLAY_IMAGE_RESOURCE_ID);
				mPauseButton.setImageDrawable(d);
			}
		}
	}

	private void doPauseResume() {
		if (mPlayer == null)
			return;
		if (mPlayer.isPlaying()) {
			mPlayer.pause();
			if (mOnPauseListener != null) {
				mOnPauseListener.onVideoPause();
			}
		} else {
			mPlayer.start();
			if (mOnUnpauseListener != null) {
				mOnUnpauseListener.onVideoUnpause();
			}
		}
		updatePausePlay();
	}

	public boolean canToggle() {
		return mVideoData.allowTapNavigationBars;
	}

	public void toggle() {
		if (canToggle()) {
			if (mShowing) {
				hide();
			} else {
				show();
			}
		}
	}

	public void onStart() {
		refreshProgress();
	}

	private void refreshProgress() {
		if (mShowing) {
			updatePausePlay();
			int pos = setProgress();
			if ((mPlayer != null) && (mPlayer.isPlaying())) {
				mHandler.removeMessages(SHOW_PROGRESS);
				Message msg = mHandler.obtainMessage(SHOW_PROGRESS);
				mHandler.sendMessageDelayed(msg, 1000 - (pos % 1000));
			}
		}
	}

	public void onPause() {
		show(0);
	}

	private View.OnClickListener mReplayListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			replay();
		}
	};

	public void setOnPauseListener(OnPauseListener l) {
		mOnPauseListener = l;
	}

	public void setOnUnpauseListener(OnUnpauseListener l) {
		mOnUnpauseListener = l;
	}

	public void setOnReplayListener(OnReplayListener l) {
		mOnReplayListener = l;
	}

	public interface OnPauseListener {
		public void onVideoPause();
	}

	public interface OnUnpauseListener {
		public void onVideoUnpause();
	}

	public interface OnReplayListener {
		public void onVideoReplay();
	}

}