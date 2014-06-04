package com.example.listdemo;

import java.util.List;

import com.joyplus.konka.ADRequest;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleWindow extends StandOutWindow {
	
    public static boolean SHOW = false;
    public static final int Time = 6;
    private int CountTime = 5;
    private TextView  mTextView;
    private ImageView mImageView;
	@Override
	public String getAppName() {
		return "SimpleWindow"; 
	}
    
	@Override
	public int getAppIcon() {
		return android.R.drawable.ic_menu_close_clear_cancel;
	}

	@SuppressLint("NewApi")
	@Override
	public void createAndAttachView(int id, FrameLayout frame) {
		// create a new layout from body.xml
		SHOW = true;
		CountTime = Time;
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.simple, frame, true);
		mTextView  = (TextView) v.findViewById(R.id.textView1);
		mImageView = (ImageView) v.findViewById(R.id.View1);
		mHandler.sendEmptyMessage(MSG_UPDATEAD);
		v.requestFocus();
	}
    private final static int MSG_UPDATETIME = 0;
    private final static int MSG_UPDATEAD   = 1;
	// the window will be centered
	@Override
	public StandOutLayoutParams getParams(int id, Window window) {
		DisplayMetrics metrics = this.getApplicationContext().getResources().getDisplayMetrics();
		return new StandOutLayoutParams(id, (int)(metrics.widthPixels+0.5f), (int)(metrics.heightPixels+0.5f)+100,
				StandOutLayoutParams.CENTER, StandOutLayoutParams.CENTER);
	}

	// move the window by dragging the view
	@Override
	public int getFlags(int id) {
		return super.getFlags(id) | StandOutFlags.FLAG_BODY_MOVE_ENABLE
				| StandOutFlags.FLAG_WINDOW_FOCUSABLE_DISABLE
				| StandOutFlags.FLAG_WINDOW_FOCUS_INDICATOR_DISABLE;
	}

	@Override
	public String getPersistentNotificationMessage(int id) {
		return "";
	}

	@Override
	public boolean onKeyEvent(int id, Window window, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d("Jas","SimpleWindow onKeyEvent");
		return true;
	}
    
	@Override
	public Intent getPersistentNotificationIntent(int id) {
		return StandOutWindow.getCloseIntent(this, SimpleWindow.class, id);
	}
	private Handler mHandler = new Handler(){
		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case MSG_UPDATEAD:
				List<Drawable> ad = ADRequest.getPicturesDrawble(0);
				if(ad!=null && ad.size()>0){
					mImageView.setBackground(ad.get(0));
					mHandler.sendEmptyMessage(MSG_UPDATETIME);
				}else{
					StandOutWindow.closeAll(getApplicationContext(), SimpleWindow.class);
					SHOW = false;
				}
				break;
			case MSG_UPDATETIME:
				if(--CountTime<=0){
					StandOutWindow.closeAll(getApplicationContext(), SimpleWindow.class);
					SHOW = false;
				}else{
					mTextView.setText(""+CountTime);
					mHandler.sendEmptyMessageDelayed(0, 1000);
				}
				break;
			}
		}
	};
	
}
