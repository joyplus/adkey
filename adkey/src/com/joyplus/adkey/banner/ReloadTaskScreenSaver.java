package com.joyplus.adkey.banner;

import java.util.TimerTask;

class ReloadTaskScreenSaver extends TimerTask {

	private final AdViewScreenSaver mWebView;

	public ReloadTaskScreenSaver(final AdViewScreenSaver WebView) {
		this.mWebView = WebView;
	}

	@Override
	public void run() {
		this.mWebView.loadNextAd();
	}
}