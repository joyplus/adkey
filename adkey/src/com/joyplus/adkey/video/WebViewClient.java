package com.joyplus.adkey.video;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

public class WebViewClient extends android.webkit.WebViewClient {
	private boolean mAllowNavigation = false;
	private Activity mActivity;
	private String mAllowedUrl;
	private long mFinishedLoadingTime;
	private OnPageLoadedListener mOnPageLoadedListener;

	public WebViewClient(Activity activity, boolean allowNavigation) {
		mActivity = activity;
		mAllowNavigation = allowNavigation;
		mFinishedLoadingTime = 0;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if (url.startsWith("market:")
				|| url.startsWith("http://market.android.com")
				|| url.startsWith("sms:") || url.startsWith("tel:")
				|| url.startsWith("mailto:") || url.startsWith("voicemail:")
				|| url.startsWith("geo:")
				|| url.startsWith("google.streetview:")) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			try {
				mActivity.startActivity(intent);
			} catch (ActivityNotFoundException e) {
			}
			return true;
		}

		if (url.startsWith("mfox:external:")) {
			url = url.substring(14);
			Intent intent = new Intent("android.intent.action.VIEW",
					Uri.parse(url));
			mActivity.startActivity(intent);
			return true;
		}
		if (url.startsWith("mfox:replayvideo")) {
			try {
				Class<? extends Activity> c = mActivity.getClass();
				Method method = c.getMethod("replayVideo");
				method.invoke(mActivity);
			} catch (NoSuchMethodException e) {
			} catch (Exception e) {
			}
			return true;
		}
		if (url.startsWith("mfox:playvideo")) {
			try {
				Class<? extends Activity> c = mActivity.getClass();
				Method method = c.getMethod("playVideo");
				method.invoke(mActivity);
			} catch (NoSuchMethodException e) {
			} catch (Exception e) {
			}
			return true;
		}
		if (url.startsWith("mfox:skip")) {
			mActivity.finish();
			return true;
		}
		if ((mAllowNavigation) || (url.equals(mAllowedUrl))) {
			view.loadUrl(url);
		} else {
			Intent intent = new Intent(mActivity, RichMediaActivity.class);
			intent.setData(Uri.parse(url));
			mActivity.startActivity(intent);
		}
		return true;
	}

	public void setAllowedUrl(String url) {
		mFinishedLoadingTime = 0;
		this.mAllowedUrl = url;
		if (mAllowedUrl != null) {
			Uri uri = Uri.parse(mAllowedUrl);
			String path = uri.getPath();
			if ((path == null) || (path.length() == 0)) {
				mAllowedUrl += "/";
			}
		}
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		if ((mAllowedUrl == null) || (url.equals(mAllowedUrl))) {

			if (mFinishedLoadingTime == 0) {
				this.mFinishedLoadingTime = System.currentTimeMillis();
			}
			if (this.mOnPageLoadedListener != null) {
				this.mOnPageLoadedListener.onPageLoaded();
			}
		}
	}

	public long getFinishedLoadingTime() {
		return this.mFinishedLoadingTime;
	}

	public String getAllowedUrl() {
		return this.mAllowedUrl;
	}

	public void setOnPageLoadedListener(OnPageLoadedListener l) {
		this.mOnPageLoadedListener = l;
	}

	public interface OnPageLoadedListener {
		public void onPageLoaded();
	}

}
