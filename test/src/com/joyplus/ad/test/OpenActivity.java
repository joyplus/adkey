package com.joyplus.ad.test;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.VideoView;


public class OpenActivity extends Activity implements OnCompletionListener {

	private VideoView mVideoView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mVideoView = new VideoView(this);
		mVideoView.setOnCompletionListener(this);
		setContentView(mVideoView);
		mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.test));
		mVideoView.start();
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		finish();
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
}
