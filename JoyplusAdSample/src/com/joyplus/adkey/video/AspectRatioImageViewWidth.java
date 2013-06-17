package com.joyplus.adkey.video;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AspectRatioImageViewWidth extends ImageView {

	public AspectRatioImageViewWidth(Context context) {
		super(context);
	}

	public AspectRatioImageViewWidth(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public AspectRatioImageViewWidth(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

}
