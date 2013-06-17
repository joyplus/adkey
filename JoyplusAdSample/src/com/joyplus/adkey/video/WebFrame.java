package com.joyplus.adkey.video;

import static com.joyplus.adkey.Const.ENCODING;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.joyplus.adkey.video.InterstitialController.BrowserControl;
import com.joyplus.adkey.video.WebViewClient.OnPageLoadedListener;

public class WebFrame extends FrameLayout implements BrowserControl {

	private WebView mWebView;
	private WebViewClient mWebViewClient;
	private WebChromeClient mWebChromeClient;
	private Activity mActivity;
	private InterstitialController mController;
	private ImageView mExitButton;
	private boolean enableZoom=true;

	private static Method mWebView_SetLayerType;
	private static Field mWebView_LAYER_TYPE_SOFTWARE;

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

//			mWebView_LAYER_TYPE_SOFTWARE = WebView.class.getField("LAYER_TYPE_SOFTWARE");
			mWebView_LAYER_TYPE_SOFTWARE = WebView.class.getField("LAYER_TYPE_NONE");

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
	}

	public WebFrame(Activity context, boolean allowNavigation,
			boolean scroll, boolean showExit) {
		super(context);
		initCompatibility();
		mActivity = context;
		mWebView = new WebView(context);
		mWebView.setVerticalScrollBarEnabled(scroll);
		mWebView.setHorizontalScrollBarEnabled(scroll);
		mWebView.setBackgroundColor(Color.TRANSPARENT);
		setLayer(mWebView);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setPluginsEnabled(true);
		webSettings.setSupportZoom(enableZoom);
		webSettings.setBuiltInZoomControls(enableZoom);
//		webSettings.setLoadWithOverviewMode(true);

		/*
		 * setup for video of html
		 * @author yyc
		 */
		mWebChromeClient = new WebChromeClient(mActivity);
		mWebView.setWebChromeClient(mWebChromeClient);
		
		mWebViewClient = new WebViewClient(mActivity, allowNavigation);
		mWebView.setWebViewClient(mWebViewClient);
		final Activity localContext = context;
		if(showExit){
			ImageView bg = new ImageView(context);
			bg.setBackgroundColor(Color.TRANSPARENT);
			addView(bg, new FrameLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT,
					android.view.ViewGroup.LayoutParams.FILL_PARENT, Gravity.CENTER));
			addView(mWebView, new FrameLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT,
					android.view.ViewGroup.LayoutParams.FILL_PARENT, Gravity.CENTER));
			mExitButton = new ImageView(context);
			mExitButton.setAdjustViewBounds(false);
			mExitButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					localContext.finish();
				}
			});
			int buttonSize = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 35, getResources()
					.getDisplayMetrics());

			mExitButton.setImageDrawable(ResourceManager
					.getStaticResource(context, ResourceManager.DEFAULT_SKIP_IMAGE_RESOURCE_ID));
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					buttonSize,
					buttonSize, Gravity.TOP
					| Gravity.RIGHT);
			int margin = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 6, getResources()
					.getDisplayMetrics());
			params.topMargin = margin;
			params.rightMargin = margin;
			addView(mExitButton, params);
		}
		else{
			addView(mWebView, new FrameLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT,
					android.view.ViewGroup.LayoutParams.FILL_PARENT, Gravity.CENTER));
		}

	}

	public void loadUrl(String url) {
		new LoadUrlTask().execute(url);
	}

	public void setMarkup(String htmlMarkup) {
		String data = Uri.encode(htmlMarkup);
		this.mWebViewClient.setAllowedUrl(null);
		this.mWebView.loadData(data, "text/html", ENCODING);
	}

	@Override
	public void setBackgroundColor(int color) {
		super.setBackgroundColor(color);
		this.mWebView.setBackgroundColor(color);
	}

	private class LoadUrlTask extends AsyncTask<String, Void, String> {

		String userAgent;

		public LoadUrlTask(){
			userAgent = getUserAgentString();
		}

		@Override
		protected String doInBackground(String... urls) {
			String loadingUrl = urls[0];
			URL url = null;
			try {
				url = new URL(loadingUrl);
			} catch (MalformedURLException e) {
				return (loadingUrl != null) ? loadingUrl : "";
			}

			int statusCode = -1;
			HttpURLConnection connection = null;
			String nextLocation = url.toString();

			Set<String> redirectLocations = new HashSet<String>();
			redirectLocations.add(nextLocation);

			try {
				do {
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestProperty("User-Agent",
							userAgent);
					connection.setInstanceFollowRedirects(false);

					statusCode = connection.getResponseCode();
					if (statusCode == HttpStatus.SC_OK) {
						connection.disconnect();
						break;
					} else {
						nextLocation = connection.getHeaderField("location");
						connection.disconnect();
						if (!redirectLocations.add(nextLocation)) {
							return "";
						}

						url = new URL(nextLocation);
					}
				} while (statusCode == HttpStatus.SC_MOVED_TEMPORARILY
						|| statusCode == HttpStatus.SC_MOVED_PERMANENTLY
						|| statusCode == HttpStatus.SC_TEMPORARY_REDIRECT
						|| statusCode == HttpStatus.SC_SEE_OTHER);
			} catch (IOException e) {
				return (nextLocation != null) ? nextLocation : "";
			} finally {
				if (connection != null)
					connection.disconnect();
			}

			return nextLocation;
		}

		@Override
		protected void onPostExecute(String url) {
			if (url == null || url.equals("")) {
				url = "about:blank";
			}
			mWebViewClient.setAllowedUrl(url);
			mWebView.loadUrl(url);
			requestLayout();

		}
	}

	private String getUserAgentString() {
		return mWebView.getSettings().getUserAgentString();
	}

	public WebView getWebView() {
		return mWebView;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		this.onTouchEvent(ev);
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		return true;
	}

	@Override
	public boolean canGoBack() {
		return mWebView.canGoBack();
	}

	@Override
	public void goBack() {
		mWebView.goBack();
	}

	@Override
	public boolean canGoForward() {
		return mWebView.canGoForward();
	}

	@Override
	public void goForward() {
		mWebView.goForward();
	}

	@Override
	public void reload() {
		mWebView.reload();
	}

	public void setBrowserController(InterstitialController controller) {
		if (mController != null) {
			mController.hide();
		}
		mController = controller;
		attachController();
	}

	private void attachController() {
		if (mController != null) {
			mController.setBrowser(this);
		}
	}

	@Override
	public int getTime() {
		int time = 0;
		long finishedLoadedTime = mWebViewClient.getFinishedLoadingTime();
		if (finishedLoadedTime > 0) {
			return (int) (System.currentTimeMillis() - finishedLoadedTime);
		}
		return time;
	}

	@Override
	public void launchExternalBrowser() {

		String url = mWebViewClient.getAllowedUrl();
		if ((url == null) || (url.length() == 0)) {
			url = "about:blank";
		}
		Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
		mActivity.startActivity(intent);
	}

	@Override
	public String getPageTitle() {
		return mWebView.getTitle();
	}

	public void setOnPageLoadedListener(OnPageLoadedListener l) {
		this.mWebViewClient.setOnPageLoadedListener(l);
	}

	public boolean isEnableZoom() {
		return enableZoom;
	}

	public void setEnableZoom(boolean enableZoom) {
		this.enableZoom = enableZoom;
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setSupportZoom(enableZoom);
		webSettings.setBuiltInZoomControls(enableZoom);
	}

}
