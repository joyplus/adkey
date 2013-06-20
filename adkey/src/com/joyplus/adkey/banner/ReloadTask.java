package com.joyplus.adkey.banner;

import java.util.TimerTask;

class ReloadTask extends TimerTask {

	private final AdView mWebView;

	public ReloadTask(final AdView WebView) {
		this.mWebView = WebView;
	}

	@Override
	public void run() {
		this.mWebView.loadNextAd();
	}
}