package com.joyplus.ad.test.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HorizontalScrollView extends android.widget.HorizontalScrollView {

	private OnScrollChangeListener mScrollChangeListener = null;
	
	public HorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public HorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HorizontalScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
//		return super.onTouchEvent(ev);
		return false;
	}
	
	public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener){
		this.mScrollChangeListener = onScrollChangeListener;
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		if(mScrollChangeListener != null){
			mScrollChangeListener.onScrollChanged(this, l, t, oldl, oldt);
		}
	}
	
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
//		if(event.getAction() == KeyEvent.ACTION_DOWN 
//				&& (event.getKeyCode()==KeyEvent.KEYCODE_DPAD_LEFT || event.getKeyCode()==KeyEvent.KEYCODE_DPAD_RIGHT)){
//			return true;
//		}else{
//			return super.dispatchKeyEvent(event);
//		}
//	}
}
