package com.joyplus.adkey.video;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

public class AspectRatioImageView extends ImageView {

	private boolean mFill = false;
	private int mMinW = -1;
	private int mMaxH = -1;

	public AspectRatioImageView(Context context) {
		super(context);
	}

	public AspectRatioImageView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public AspectRatioImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public void fillParent(boolean fill, int minWidthDip, int maxHeightDip) {
		mFill = fill;
		mMaxH = maxHeightDip;
		mMinW = minWidthDip;

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		if (mFill == false || getDrawable() == null) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		} else {
			int drawableH = getDrawable().getIntrinsicHeight();
			int drawableW = getDrawable().getIntrinsicWidth();

			if (drawableW > drawableH) {
				height = width * drawableH / drawableW;
			} else {
				height = width;
				width = height * drawableW / drawableH;

			}

			ensureConstraintMetAndSet(width, height, drawableW, drawableH);
		}
	}

	protected int getMeasuredHeight(int widthMeasureSpec, int heightMeasureSpec) {

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		if (mFill == false || getDrawable() == null) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			return 0;
		} else {
			int drawableH = getDrawable().getIntrinsicHeight();
			int drawableW = getDrawable().getIntrinsicWidth();

			if (drawableW > drawableH) {
				height = width * drawableH / drawableW;
			} else {
				height = width;
				width = height * drawableW / drawableH;

			}

			return getConstrainedHeight(width, height, drawableW, drawableH);
		}
	}

	void ensureConstraintMetAndSet(int measuredWidth, int measuredHeight,
			int drawableW, int drawableH) {

		boolean portraitImage = (drawableW < drawableH);

		if (portraitImage) {

			if (mMinW > 0) {
				float minW = dip2pixel(mMinW, getContext());

				if (measuredWidth < minW) {
					measuredWidth = (int) minW;
					measuredHeight = drawableH / drawableW * measuredWidth;
				}
			}

			if (mMaxH > 0) {
				float maxH = dip2pixel(mMaxH, getContext());

				if (measuredHeight > maxH) {
					measuredHeight = (int) maxH;
					measuredWidth = measuredHeight * drawableW / drawableH;
				}
			}

		} else {

			if (mMaxH > 0) {
				float maxH = dip2pixel(mMaxH, getContext());

				if (measuredHeight > maxH) {
					measuredHeight = (int) maxH;
					measuredWidth = measuredHeight * drawableW / drawableH;
				}
			}

			if (mMinW > 0) {
				float minW = dip2pixel(mMinW, getContext());

				if (measuredWidth < minW) {
					measuredWidth = (int) minW;
					measuredHeight = drawableH / drawableW * measuredWidth;
				}
			}

		}

		setMeasuredDimension(measuredWidth, measuredHeight);
	}
	int getConstrainedHeight(int measuredWidth, int measuredHeight,
			int drawableW, int drawableH) {

		boolean portraitImage = (drawableW < drawableH);

		if (portraitImage) {

			if (mMinW > 0) {
				float minW = dip2pixel(mMinW, getContext());

				if (measuredWidth < minW) {
					measuredWidth = (int) minW;
					measuredHeight = drawableH / drawableW * measuredWidth;
				}
			}

			if (mMaxH > 0) {
				float maxH = dip2pixel(mMaxH, getContext());

				if (measuredHeight > maxH) {
					measuredHeight = (int) maxH;
					measuredWidth = measuredHeight * drawableW / drawableH;
				}
			}

		} else {

			if (mMaxH > 0) {
				float maxH = dip2pixel(mMaxH, getContext());

				if (measuredHeight > maxH) {
					measuredHeight = (int) maxH;
					measuredWidth = measuredHeight * drawableW / drawableH;
				}
			}

			if (mMinW > 0) {
				float minW = dip2pixel(mMinW, getContext());

				if (measuredWidth < minW) {
					measuredWidth = (int) minW;
					measuredHeight = drawableH / drawableW * measuredWidth;
				}
			}

		}

		return measuredHeight;
	}

	public static float dip2pixel(int dip, Context context) {

		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
				r.getDisplayMetrics());

		return px;
	}

}
