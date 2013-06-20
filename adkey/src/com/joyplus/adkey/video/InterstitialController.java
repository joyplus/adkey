package com.joyplus.adkey.video;

import java.lang.ref.WeakReference;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InterstitialController extends LinearLayout {

	private static final int DEFAULT_TIMEOUT = 3000;
	private static final int FADE_OUT = 1;
	private static final int SHOW_PROGRESS = 2;

	private BrowserControl mBrowser;
	private InterstitialData mInterstitialData;
	private Context mContext;
	private ResourceManager mResourceManager;
	private FrameLayout mBrowserView;
	private LinearLayout mTopBar;
	private LinearLayout mBottomBar;
	private AspectRatioImageView mBottomBarBackground;
	private AspectRatioImageView mTopBarBackground;
	private LinearLayout mNavIconsLayout;
	private AspectRatioImageViewWidth mBackButton;
	private AspectRatioImageViewWidth mForwardButton;
	private AspectRatioImageViewWidth mReloadButton;
	private AspectRatioImageViewWidth mExternalButton;
	private TextView mLeftTime;
	private TextView mTitleText;
	StringBuilder mFormatBuilder;
	Formatter mFormatter;
	private boolean mShowing;
	private boolean mFixed;
	private boolean mAutoclose;
	private int mDefaultTimeout;
	private String mTitle;
	private OnReloadListener mOnReloadListener;
	private OnResetAutocloseListener mOnResetAutocloseListener;
	private double buttonWidthPercent = 0.09;
	private Handler mHandler;

	public InterstitialController(Context context,
			InterstitialData interstitialData) {
		super(context);
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		mContext = context;
		mInterstitialData = interstitialData;
		if (mInterstitialData == null) {
			throw new IllegalArgumentException(
					"Interstitial info cannot be null");
		}
		mFormatBuilder = new StringBuilder();
		mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
		mFixed = false;
		mAutoclose = (mInterstitialData.autoclose > 0);
		mDefaultTimeout = DEFAULT_TIMEOUT;
		if ((mInterstitialData != null)
				&& (!mInterstitialData.allowTapNavigationBars)) {
			mDefaultTimeout = 0;
		}
		mHandler = new ResourceHandler(this);
		mResourceManager = new ResourceManager(mContext, mHandler);
		buildNavigationBarView(metrics);
	}

	public void setBrowserView(View browserView) {
		mBrowserView.addView(browserView, new FrameLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT, Gravity.CENTER));
	}

	public void setBrowser(BrowserControl browser) {
		mBrowser = browser;
		updateBackForward();
	}

	private void buildNavigationBarView(DisplayMetrics metrics) {

		this.setWeightSum(1);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setLayoutParams(new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT));

		mTopBar = new LinearLayout(mContext);
		mTopBar.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.TOP;
		params.weight = 0;
		mTopBar.setLayoutParams(params);
		mTopBar.setBackgroundColor(Color.TRANSPARENT);
		mTopBar.setGravity(Gravity.CENTER);
		int padding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		mTopBar.setPadding(padding, 0, padding, 0);
		mTopBarBackground = new AspectRatioImageView(mContext);
		mTopBarBackground.setLayoutParams(params);
		mTopBarBackground
		.fillParent(true, this.getWidth(), mTopBar.getHeight());

		this.addView(mTopBarBackground, params);
		this.addView(mTopBar, params);

		mTitleText = new TextView(mContext);
		params = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		params.gravity = Gravity.CENTER;
		mTitleText.setTextAppearance(mContext,
				android.R.style.TextAppearance_Small);
		mTitleText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		mTitleText.setGravity(Gravity.CENTER);
		mTopBar.addView(mTitleText, params);

		mBrowserView = new FrameLayout(mContext);
		LinearLayout.LayoutParams browserParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		browserParams.gravity = Gravity.TOP;
		browserParams.weight = 1;
		mBrowserView.setBackgroundColor(Color.WHITE);
		this.addView(mBrowserView, browserParams);

		mBottomBar = new LinearLayout(mContext);
		mBottomBar.setOrientation(LinearLayout.HORIZONTAL);
		params = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				(int)(metrics.widthPixels*0.119));
		params.gravity = Gravity.BOTTOM;
		params.weight = 0;
		mBottomBar.setLayoutParams(params);
		mBottomBar.setBackgroundColor(Color.TRANSPARENT);
		mBottomBar.setGravity(Gravity.CENTER_VERTICAL);
		mBottomBar.setWeightSum(1);
		mBottomBarBackground = new AspectRatioImageView(mContext);

		mBottomBarBackground
		.fillParent(true, this.getWidth(), mBottomBar.getHeight());

		this.addView(mBottomBarBackground);
		this.addView(mBottomBar, params);

		LinearLayout buttonPanel = new LinearLayout(mContext);
		params = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT, 0);
		params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
		buttonPanel.setOrientation(LinearLayout.HORIZONTAL);

		mBottomBar.addView(buttonPanel, params);

		padding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		mReloadButton = new AspectRatioImageViewWidth(mContext);
		params = new LinearLayout.LayoutParams(
				(int) (metrics.widthPixels*buttonWidthPercent),
				(int) (metrics.widthPixels*buttonWidthPercent));
		params.leftMargin=4;
		params.rightMargin=4;
		params.gravity = Gravity.CENTER_VERTICAL;
		mReloadButton.setAdjustViewBounds(true);
		mReloadButton.setPadding(padding, 0, padding, 0);

		buttonPanel.addView(mReloadButton, params);

		mBackButton = new AspectRatioImageViewWidth(mContext);
		mBackButton.setPadding(padding, 0, padding, 0);
		buttonPanel.addView(mBackButton, params);

		mForwardButton = new AspectRatioImageViewWidth(mContext);
		mForwardButton.setPadding(padding, 0, padding, 0);
		buttonPanel.addView(mForwardButton, params);

		mExternalButton = new AspectRatioImageViewWidth(mContext);
		mExternalButton.setPadding(padding, 0, padding, 0);
		buttonPanel.addView(mExternalButton, params);

		mLeftTime = new AutoResizeTextView(mContext);
		params = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;

		mLeftTime.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		buttonPanel.addView(mLeftTime, params);

		mNavIconsLayout = new LinearLayout(mContext);
		params = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1);
		params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		mNavIconsLayout.setOrientation(LinearLayout.HORIZONTAL);
		mNavIconsLayout.setPadding(0, 0, 0, 0);
		mNavIconsLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		mBottomBar.addView(mNavIconsLayout, params);
		initNavigationBarControllerView(padding,metrics);
	}

	private void initNavigationBarControllerView(int padding, DisplayMetrics metrics) {

		if (!mInterstitialData.showBottomNavigationBar) {
			mBottomBar.setVisibility(View.GONE);
		} else {
			mBottomBar.setVisibility(View.VISIBLE);
			if ((mInterstitialData.bottomNavigationBarBackground != null)
					&& (mInterstitialData.bottomNavigationBarBackground
							.length() > 0)) {
				mResourceManager.fetchResource(mContext,
						mInterstitialData.bottomNavigationBarBackground,
						ResourceManager.DEFAULT_BOTTOMBAR_BG_RESOURCE_ID);
			} else {
				mBottomBar
				.setBackgroundDrawable(mResourceManager
						.getResource(mContext,ResourceManager.DEFAULT_BOTTOMBAR_BG_RESOURCE_ID));
			}

			if (mBackButton != null) {
				if ((mInterstitialData.backButtonImage != null)
						&& (mInterstitialData.backButtonImage.length() > 0)) {
					mBackButton.setBackgroundDrawable(null);
					mResourceManager.fetchResource(mContext,
							mInterstitialData.backButtonImage,
							ResourceManager.DEFAULT_BACK_IMAGE_RESOURCE_ID);
				} else {
					mBackButton.setImageDrawable(mResourceManager
							.getResource(mContext,ResourceManager.DEFAULT_BACK_IMAGE_RESOURCE_ID));
					mBackButton.setEnabled(false);
				}

				mBackButton.setOnClickListener(mBackListener);
				if (mInterstitialData.showBackButton) {
					mBackButton.setVisibility(View.VISIBLE);
				} else {
					mBackButton.setVisibility(View.GONE);
				}
			}

			if (mForwardButton != null) {
				if ((mInterstitialData.forwardButtonImage != null)
						&& (mInterstitialData.forwardButtonImage.length() > 0)) {
					mForwardButton.setBackgroundDrawable(null);
					mResourceManager.fetchResource(mContext,
							mInterstitialData.forwardButtonImage,
							ResourceManager.DEFAULT_FORWARD_IMAGE_RESOURCE_ID);
				} else {
					mForwardButton.setImageDrawable(mResourceManager
							.getResource(mContext,ResourceManager.DEFAULT_FORWARD_IMAGE_RESOURCE_ID));

				}
				mForwardButton.setOnClickListener(mForwardListener);
				if (mInterstitialData.showForwardButton) {
					mForwardButton.setVisibility(View.VISIBLE);
				} else {
					mForwardButton.setVisibility(View.GONE);
				}
			}

			if (mReloadButton != null) {
				if ((mInterstitialData.reloadButtonImage != null)
						&& (mInterstitialData.reloadButtonImage.length() > 0)) {
					mReloadButton.setBackgroundDrawable(null);
					mResourceManager.fetchResource(mContext,
							mInterstitialData.reloadButtonImage,
							ResourceManager.DEFAULT_RELOAD_IMAGE_RESOURCE_ID);
				} else {
					mReloadButton.setImageDrawable(mResourceManager
							.getResource(mContext,ResourceManager.DEFAULT_RELOAD_IMAGE_RESOURCE_ID));

				}
				mReloadButton.setOnClickListener(mReloadListener);
				if (mInterstitialData.showReloadButton) {
					mReloadButton.setVisibility(View.VISIBLE);
				} else {
					mReloadButton.setVisibility(View.GONE);
				}
			}

			if (mExternalButton != null) {
				if ((mInterstitialData.externalButtonImage != null)
						&& (mInterstitialData.externalButtonImage.length() > 0)) {
					mExternalButton.setBackgroundDrawable(null);
					mResourceManager.fetchResource(mContext,
							mInterstitialData.externalButtonImage,
							ResourceManager.DEFAULT_EXTERNAL_IMAGE_RESOURCE_ID);
				} else {
					mExternalButton.setImageDrawable(mResourceManager
							.getResource(mContext,ResourceManager.DEFAULT_EXTERNAL_IMAGE_RESOURCE_ID));

				}
				mExternalButton.setOnClickListener(mExternalListener);
				if (mInterstitialData.showExternalButton) {
					mExternalButton.setVisibility(View.VISIBLE);
				} else {
					mExternalButton.setVisibility(View.GONE);
				}
			}

			mFormatBuilder = new StringBuilder();
			mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
			if (mLeftTime != null) {
				if ((mInterstitialData.showTimer) && (mAutoclose)) {
					mLeftTime.setVisibility(View.VISIBLE);
				} else {
					mLeftTime.setVisibility(View.GONE);
				}
			}
			if (!mInterstitialData.icons.isEmpty()) {
				for (int i = 0; i < mInterstitialData.icons.size(); i++) {
					NavIconData iconData = mInterstitialData.icons.get(i);
					NavIcon icon = new NavIcon(mContext, iconData);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							(int) (metrics.widthPixels*buttonWidthPercent),
							(int) (metrics.widthPixels*buttonWidthPercent));
					mNavIconsLayout.addView(icon, params);
				}
			}
		}
		if (!mInterstitialData.showTopNavigationBar) {
			mTopBar.setVisibility(View.GONE);
		} else {
			mTopBar.setVisibility(View.VISIBLE);
			if ((mInterstitialData.topNavigationBarBackground != null)
					&& (mInterstitialData.topNavigationBarBackground.length() > 0)) {
				mResourceManager.fetchResource(mContext,
						mInterstitialData.topNavigationBarBackground,
						ResourceManager.DEFAULT_TOPBAR_BG_RESOURCE_ID);
			} else {
				mTopBarBackground.setImageDrawable(mResourceManager
						.getResource(mContext,ResourceManager.DEFAULT_TOPBAR_BG_RESOURCE_ID));
			}

			if (mTitleText != null) {
				if (mInterstitialData.topNavigationBarTitleType == InterstitialData.INTERSTITIAL_TITLE_FIXED) {
					mTitleText.setText(mInterstitialData.topNavigationBarTitle);
				}
				else if(mInterstitialData.topNavigationBarTitleType == InterstitialData.INTERSTITIAL_TITLE_HIDDEN) {
					mTitleText.setVisibility(View.GONE);
				}
			}
		}

		if (!mInterstitialData.showNavigationBars) {
			if (mTopBar != null) {
				mTopBar.setVisibility(View.GONE);
			}
			if (mBottomBar != null) {
				mBottomBar.setVisibility(View.GONE);
			}
		}
	}

	public void resetAutoclose() {
		if (mAutoclose) {
			mAutoclose = false;
			if (mOnResetAutocloseListener != null) {
				mOnResetAutocloseListener.onResetAutoclose();
			}
		}
	}

	public void show() {
		show(mDefaultTimeout);
	}

	public void show(int timeout) {

		if (timeout == 0) {
			mFixed = true;
		}
		if (!mShowing) {
			setProgress();
			if ((mTopBar != null) && (mInterstitialData.showTopNavigationBar)) {
				mTopBar.setVisibility(View.VISIBLE);
			}
			if ((mBottomBar != null)
					&& (mInterstitialData.showBottomNavigationBar)) {
				mBottomBar.setVisibility(View.VISIBLE);
			}
			mShowing = true;
		}
		updateBackForward();

		mHandler.removeMessages(FADE_OUT);
		mHandler.sendEmptyMessage(SHOW_PROGRESS);
		if ((timeout != 0) && (!mFixed)) {
			Message msg = mHandler.obtainMessage(FADE_OUT);
			mHandler.sendMessageDelayed(msg, timeout);
		}
	}

	public boolean isShowing() {
		return mShowing;
	}

	public void hide() {

		if (canToggle()) {
			if (mShowing) {
				mHandler.removeMessages(SHOW_PROGRESS);
				if (mTopBar != null) {
					mTopBar.setVisibility(View.GONE);
				}
				if (mBottomBar != null) {
					mBottomBar.setVisibility(View.GONE);
				}
				mShowing = false;
				mFixed = false;
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

	public void toggle() {
		if (canToggle()) {
			if (mShowing) {
				hide();
			} else {
				show();
			}
		}
	}

	public void reload() {
		if (mBrowser != null) {
			mBrowser.reload();
		}
		setProgress();
		show(mDefaultTimeout);
		if (mOnReloadListener != null) {
			mOnReloadListener.onInterstitialReload();
		}
	}

	public void pageLoaded() {
		setProgress();
	}

	private static class ResourceHandler extends Handler{
		WeakReference<InterstitialController> interstitialController;

		public ResourceHandler(InterstitialController i) {
			interstitialController = new WeakReference<InterstitialController>(i);
		}

		@Override
		public void handleMessage(Message msg) {
			InterstitialController wInterstitialController = interstitialController.get();
			if(wInterstitialController!=null){
				switch (msg.what) {
				case SHOW_PROGRESS:
					wInterstitialController.setProgress();
					if (wInterstitialController.mShowing && wInterstitialController.mInterstitialData.showTimer) {
						msg = obtainMessage(SHOW_PROGRESS);
						sendMessageDelayed(msg, 1000);
					}
					break;
				default:
					wInterstitialController.handleMessage(msg);
				}

			}
		}
	}

	private void handleMessage(Message msg) {
		switch (msg.what) {
		case FADE_OUT:
			hide();
			break;
		case ResourceManager.RESOURCE_LOADED_MSG:
			switch (msg.arg1) {
			case ResourceManager.DEFAULT_TOPBAR_BG_RESOURCE_ID:
				if (mTopBar != null) {
					Drawable d = mResourceManager
							.getResource(mContext,ResourceManager.DEFAULT_TOPBAR_BG_RESOURCE_ID);
					if (d != null) {
						mTopBarBackground.setImageDrawable(d);
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
			case ResourceManager.DEFAULT_BACK_IMAGE_RESOURCE_ID:
				if (mBackButton != null) {
					Drawable d = mResourceManager
							.getResource(mContext,ResourceManager.DEFAULT_BACK_IMAGE_RESOURCE_ID);
					if (d != null) {
						mBackButton.setImageDrawable(d);
					}

				}
				break;
			case ResourceManager.DEFAULT_FORWARD_IMAGE_RESOURCE_ID:
				if (mForwardButton != null) {
					Drawable d = mResourceManager
							.getResource(mContext,ResourceManager.DEFAULT_FORWARD_IMAGE_RESOURCE_ID);
					if (d != null) {
						mForwardButton.setImageDrawable(d);
					}

				}
				break;
			case ResourceManager.DEFAULT_RELOAD_IMAGE_RESOURCE_ID:
				if (mReloadButton != null) {
					Drawable d = mResourceManager
							.getResource(mContext,ResourceManager.DEFAULT_RELOAD_IMAGE_RESOURCE_ID);
					if (d != null) {
						mReloadButton.setImageDrawable(d);
					}

				}
				break;
			case ResourceManager.DEFAULT_EXTERNAL_IMAGE_RESOURCE_ID:
				if (mExternalButton != null) {
					Drawable d = mResourceManager
							.getResource(mContext,ResourceManager.DEFAULT_EXTERNAL_IMAGE_RESOURCE_ID);
					if (d != null) {
						mExternalButton.setImageDrawable(d);
					}

				}
				break;

			}
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
		int position = 0;
		if (mBrowser != null) {
			position = mBrowser.getTime();
		}
		int duration = mInterstitialData.autoclose * 1000;
		int timeLeft = duration - position;
		if ((mAutoclose) && (duration > 0) && (timeLeft >= 0)) {
			if (mLeftTime != null) {
				mLeftTime.setText(stringForTime(timeLeft));
			}
		} else {
			if (mLeftTime != null) {
				mLeftTime.setVisibility(View.GONE);
			}
		}
		if (mTitleText != null) {
			if (mInterstitialData.topNavigationBarTitleType == InterstitialData.INTERSTITIAL_TITLE_HTML) {
				if (mBrowser != null) {
					if ((mTitle == null)
							|| (!mTitle.equals(mBrowser.getPageTitle()))) {
						mTitle = mBrowser.getPageTitle();
						mTitleText.setText(mTitle);
					}
				}
			}
		}

		updateBackForward();
		return position;
	}

	private View.OnClickListener mBackListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mBrowser != null) {
				mBrowser.goBack();
			}
			show(mDefaultTimeout);
		}
	};

	private View.OnClickListener mReloadListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			reload();
		}
	};

	private View.OnClickListener mForwardListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mBrowser != null) {
				mBrowser.goForward();
			}
			show(mDefaultTimeout);
		}
	};

	private View.OnClickListener mExternalListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mBrowser != null) {
				mBrowser.launchExternalBrowser();
			}
		}
	};

	private void updateBackForward() {
		if (mBrowser == null)
			return;

		if (mBrowser.canGoBack()) {
			if (mBackButton != null) {
				mBackButton.setEnabled(true);
			}
		} else {
			if (mBackButton != null) {
				mBackButton.setEnabled(false);
			}
		}
		if (mBrowser.canGoForward()) {
			if (mForwardButton != null) {
				mForwardButton.setEnabled(true);
			}
		} else {
			if (mForwardButton != null) {
				mForwardButton.setEnabled(false);
			}
		}
	}

	public boolean canToggle() {
		return mInterstitialData.allowTapNavigationBars;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		this.onTouchEvent(ev);
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		this.resetAutoclose();
		return true;
	}

	public void setOnReloadListener(OnReloadListener l) {
		mOnReloadListener = l;
	}

	public void setOnResetAutocloseListener(OnResetAutocloseListener l) {
		mOnResetAutocloseListener = l;
	}

	public interface OnReloadListener {
		public void onInterstitialReload();
	}

	public interface OnResetAutocloseListener {
		public void onResetAutoclose();
	}

	public interface BrowserControl {
		int getTime();

		boolean canGoBack();

		boolean canGoForward();

		void goBack();

		void goForward();

		void reload();

		void launchExternalBrowser();

		String getPageTitle();
	}

}