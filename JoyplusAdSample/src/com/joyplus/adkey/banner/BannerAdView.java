package com.joyplus.adkey.banner;

import static com.joyplus.adkey.Const.TAG;

import java.io.InputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Timer;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.joyplus.adkey.AdListener;
import com.joyplus.adkey.AdRequest;
import com.joyplus.adkey.BannerAd;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.RequestBannerAd;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.data.ClickType;

public class BannerAdView extends RelativeLayout {

	public static final int LIVE = 0;
	public static final int TEST = 1;

	private boolean animation;

	private boolean isInternalBrowser = false;

	private BannerAd response;
	private Animation fadeInAnimation = null;
	private Animation fadeOutAnimation = null;
	private WebSettings webSettings;

	private Context mContext = null;
	protected boolean mIsInForeground;

	private WebView firstWebView;
	private WebView secondWebView;

	private ViewFlipper viewFlipper;

	private AdListener adListener;

	private boolean touchMove;

	private InputStream xml;

	private static Method mWebView_SetLayerType;
	private static Field mWebView_LAYER_TYPE_SOFTWARE;

	private final Handler updateHandler = new Handler();

	public void setWidth(int width){
		;
	}
	public void setHeight(int width){
		;
	}

	public BannerAdView(final Context context, final BannerAd response, final AdListener adListener) {
		this(context, response, false, adListener);
	}

	public BannerAdView(final Context context, final InputStream xml, final boolean animation){
		super(context);
		this.xml = xml;
		mContext = context;
		this.animation = animation;
		this.initialize(context);
	}

	public BannerAdView(final Context context, final BannerAd response, final boolean animation, final AdListener adListener) {
		super(context);
		this.response = response;
		mContext = context;
		this.animation = animation;
		this.adListener = adListener;
		this.initialize(context);
	}

	private WebView createWebView(final Context context) {
		final WebView webView = new WebView(this.getContext()) {

			@Override
			public void draw(final Canvas canvas) {
				if (this.getWidth() > 0 && this.getHeight() > 0)
					super.draw(canvas);
			}
		};

		this.webSettings = webView.getSettings();
		this.webSettings.setJavaScriptEnabled(true);
		webView.setBackgroundColor(Color.TRANSPARENT);
		setLayer(webView);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				if(response.getSkipOverlay()==1){
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
		if (this.response.getClickType() != null
				&& this.response.getClickType().equals(ClickType.INAPP) && (url.startsWith("http://") || url.startsWith("https://"))) {
			if(url.endsWith(".mp4")){
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setDataAndType(Uri.parse(url), "video/mp4");
				this.getContext().startActivity(i);
			}
			else{
				final Intent intent = new Intent(this.getContext(),
						InAppWebView.class);
				intent.putExtra(Const.REDIRECT_URI, url);
				this.getContext().startActivity(intent);
			}
		} else {
			final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			this.getContext().startActivity(intent);
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

	private static void setLayer(WebView webView){
		if (mWebView_SetLayerType != null && mWebView_LAYER_TYPE_SOFTWARE !=null) {
			try {
				mWebView_SetLayerType.invoke(webView, mWebView_LAYER_TYPE_SOFTWARE.getInt(WebView.class), null);
			} catch (InvocationTargetException ite) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
		}
		else{
		}
	}

	private void buildBannerView(){
		this.firstWebView = this.createWebView(mContext);
		this.secondWebView = this.createWebView(mContext);
		this.viewFlipper = new ViewFlipper(this.getContext()) {

			@Override
			protected void onDetachedFromWindow() {
				try {
					super.onDetachedFromWindow();
				} catch (final IllegalArgumentException e) {
					this.stopFlipping();
				}
			}
		};
		final float scale = mContext.getResources().getDisplayMetrics().density;
		this.setLayoutParams(new RelativeLayout.LayoutParams((int)(300*scale+0.5f), (int)(50*scale+0.5f)));
		final FrameLayout.LayoutParams webViewParams = new FrameLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		this.viewFlipper.addView(this.firstWebView, webViewParams);
		this.viewFlipper.addView(this.secondWebView, webViewParams);

		final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		this.addView(this.viewFlipper, params);

		if (this.animation) {

			this.fadeInAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, +1.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
			this.fadeInAnimation.setDuration(1000);

			this.fadeOutAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, -1.0f);
			this.fadeOutAnimation.setDuration(1000);
			this.viewFlipper.setInAnimation(this.fadeInAnimation);
			this.viewFlipper.setOutAnimation(this.fadeOutAnimation);
		}
	}

	private void initialize(final Context context) {
		initCompatibility();
		buildBannerView();
		showContent();
	}

	public boolean isInternalBrowser() {
		return this.isInternalBrowser;
	}

	private void notifyAdClicked() {
		this.updateHandler.post(new Runnable() {

			@Override
			public void run() {
				if (adListener != null){
					adListener.adClicked();
				}
			}
		});
	}

	private void notifyLoadAdSucceeded() {
		this.updateHandler.post(new Runnable() {

			@Override
			public void run() {
				if (adListener != null){
					adListener.adLoadSucceeded(null);
				}
			}
		});
	}

	private void openLink() {

		if (this.response != null && this.response.getClickUrl() != null)
			this.doOpenUrl(this.response.getClickUrl());

	}

	public void setAdListener(final AdListener bannerListener) {
		this.adListener = bannerListener;
	}

	public void setInternalBrowser(final boolean isInternalBrowser) {
		this.isInternalBrowser = isInternalBrowser;
	}

	private void showContent() {

		try {

			WebView webView;
			if (this.viewFlipper.getCurrentView() == this.firstWebView)
				webView = this.secondWebView;
			else
				webView = this.firstWebView;
			if (this.response.getType() == Const.IMAGE) {

				String text = MessageFormat.format(Const.IMAGE_BODY,
						this.response.getImageUrl(),
						this.response.getBannerWidth(),
						this.response.getBannerHeight());
				text = Uri.encode(Const.HIDE_BORDER + text);
				webView.loadData(text, "text/html", Const.ENCODING);
				this.notifyLoadAdSucceeded();
			} else if (this.response.getType() == Const.TEXT) {
				final String text = Uri.encode(Const.HIDE_BORDER
						+ this.response.getText());
				webView.loadData(text, "text/html", Const.ENCODING);
				this.notifyLoadAdSucceeded();
			} 

			if (this.viewFlipper.getCurrentView() == this.firstWebView) {
				this.viewFlipper.showNext();
			} else {
				this.viewFlipper.showPrevious();
			}

		} catch (final Throwable t) {
		}
	}

}
