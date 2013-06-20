package com.joyplus.adkey.video;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class AutoResizeTextView extends TextView {

	public static final float MIN_TEXT_SIZE = 20;

	public interface OnTextResizeListener {
		public void onTextResize(TextView textView, float oldSize, float newSize);
	}

	private static final Canvas sTextResizeCanvas = new Canvas();

	private static final String mEllipsis = "...";

	private OnTextResizeListener mTextResizeListener;

	private boolean mNeedsResize = false;

	private float mTextSize;

	private float mMaxTextSize = 0;

	private float mMinTextSize = MIN_TEXT_SIZE;

	private float mSpacingMult = 1.0f;

	private float mSpacingAdd = 0.0f;

	private boolean mAddEllipsis = true;

	public AutoResizeTextView(Context context) {
		this(context, null);
	}

	public AutoResizeTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AutoResizeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mTextSize = getTextSize();
	}

	@Override
	protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
		mNeedsResize = true;

		resetTextSize();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (w != oldw || h != oldh) {
			mNeedsResize = true;
		}
	}

	public void setOnResizeListener(OnTextResizeListener listener) {
		mTextResizeListener = listener;
	}

	@Override
	public void setTextSize(float size) {
		super.setTextSize(size);
		mTextSize = getTextSize();
	}

	@Override
	public void setTextSize(int unit, float size) {
		super.setTextSize(unit, size);
		mTextSize = getTextSize();
	}

	@Override
	public void setLineSpacing(float add, float mult) {
		super.setLineSpacing(add, mult);
		mSpacingMult = mult;
		mSpacingAdd = add;
	}

	public void setMaxTextSize(float maxTextSize) {
		mMaxTextSize = maxTextSize;
		requestLayout();
		invalidate();
	}

	public float getMaxTextSize() {
		return mMaxTextSize;
	}

	public void setMinTextSize(float minTextSize) {
		mMinTextSize = minTextSize;
		requestLayout();
		invalidate();
	}

	public float getMinTextSize() {
		return mMinTextSize;
	}

	public void setAddEllipsis(boolean addEllipsis) {
		mAddEllipsis = addEllipsis;
	}

	public boolean getAddEllipsis() {
		return mAddEllipsis;
	}

	public void resetTextSize() {
		super.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
		mMaxTextSize = mTextSize;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		if(changed || mNeedsResize) {
			int widthLimit = (right - left) - getCompoundPaddingLeft() - getCompoundPaddingRight();
			int heightLimit = (bottom - top) - getCompoundPaddingBottom() - getCompoundPaddingTop();
			resizeText(widthLimit, heightLimit);
		}
		super.onLayout(changed, left, top, right, bottom);
	}

	public void resizeText() {
		int heightLimit = getHeight() - getPaddingBottom() - getPaddingTop();
		int widthLimit = getWidth() - getPaddingLeft() - getPaddingRight();
		resizeText(widthLimit, heightLimit);
	}

	public void resizeText(int width, int height) {
		CharSequence text = getText();

		if(text == null || text.length() == 0 || height <= 0 || width <= 0 || mTextSize == 0) {
			return;
		}

		TextPaint textPaint = getPaint();

		float oldTextSize = textPaint.getTextSize();

		float targetTextSize = mMaxTextSize > 0 ? Math.min(mTextSize, mMaxTextSize) : mTextSize;

		int textHeight = getTextHeight(text, textPaint, width, targetTextSize);

		while(textHeight > height && targetTextSize > mMinTextSize) {
			targetTextSize = Math.max(targetTextSize - 2, mMinTextSize);
			textHeight = getTextHeight(text, textPaint, width, targetTextSize);
		}

		if(mAddEllipsis && targetTextSize == mMinTextSize && textHeight > height) {

			StaticLayout layout = new StaticLayout(text, textPaint, width, Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, false);
			layout.draw(sTextResizeCanvas);

			if(layout.getLineCount() > 0) {

				int lastLine = layout.getLineForVertical(height) - 1;

				if(lastLine < 0) {
					setText("");
				}

				else {
					int start = layout.getLineStart(lastLine);
					int end = layout.getLineEnd(lastLine);
					float lineWidth = layout.getLineWidth(lastLine);
					float ellipseWidth = textPaint.measureText(mEllipsis);

					while(width < lineWidth + ellipseWidth) {
						lineWidth = textPaint.measureText(text.subSequence(start, --end + 1).toString());
					}
					setText(text.subSequence(0, end) + mEllipsis);
				}
			}
		}

		textPaint.setTextSize(targetTextSize);
		setLineSpacing(mSpacingAdd, mSpacingMult);

		if(mTextResizeListener != null) {
			mTextResizeListener.onTextResize(this, oldTextSize, targetTextSize);
		}

		mNeedsResize = false;
	}

	private int getTextHeight(CharSequence source, TextPaint paint, int width, float textSize) {

		paint.setTextSize(textSize);

		StaticLayout layout = new StaticLayout(source, paint, width, Alignment.ALIGN_NORMAL, mSpacingMult, mSpacingAdd, true);
		layout.draw(sTextResizeCanvas);
		return layout.getHeight();
	}

}