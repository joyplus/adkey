package com.joyplus.adkey.video;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class NavIcon extends AspectRatioImageViewWidth implements OnClickListener {

	private Context mContext;
	private Handler mHandler;
	private NavIconData mIcon;

	public NavIcon(Context context, NavIconData icon) {
		super(context);

		int padding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		mContext = context;
		this.mIcon = icon;

		this.setPadding(padding, 0, padding, 0);
		mHandler = new Handler();
		setVisibility(View.GONE);
		setImageDrawable(icon.iconUrl);
		this.setOnClickListener(this);
	}

	private void setImageDrawable(final String url) {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				final Drawable image = fetchImage(url);
				if (image != null) {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							setImageDrawable(image);
							setVisibility(View.VISIBLE);
							requestLayout();
						}
					});
				}
			}
		});
		t.start();
	}

	private Drawable fetchImage(String urlString) {
		InputStream in = null;
		try {
			URL url = new URL(urlString);
			in = (InputStream) url.getContent();
			Bitmap b = BitmapFactory.decodeStream(in);
			if (b != null) {

				DisplayMetrics m = mContext.getResources().getDisplayMetrics();
				int w = b.getWidth();
				int h = b.getHeight();
				int imageWidth = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, w, m);
				int imageHeight = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, h, m);
				if ((imageWidth != w) || (imageHeight != h)) {
					b = Bitmap.createScaledBitmap(b, imageWidth, imageHeight,
							false);
				}
				return new BitmapDrawable(mContext.getResources(), b);
			}
		} catch (Exception e) {
		} finally {
			if (in != null) {
				try {
					in.close();
					in = null;
				} catch (Exception e) {
				}
			}
		}

		try {
			URL url = new URL(urlString);
			InputStream is = (InputStream) url.getContent();
			Drawable d = Drawable.createFromStream(is, "src");
			return d;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void onClick(View v) {
		try {
			if (mContext instanceof RichMediaActivity) {
				RichMediaActivity activity = (RichMediaActivity) mContext;
				if (mIcon.openType == NavIconData.TYPE_EXTERNAL) {
					Intent intent = new Intent("android.intent.action.VIEW",
							Uri.parse(mIcon.clickUrl));
					activity.startActivity(intent);
				} else {
					String url = mIcon.clickUrl;
					if (url.startsWith("market:")
							|| url.startsWith("http://market.android.com")
							|| url.startsWith("sms:") || url.startsWith("tel:")
							|| url.startsWith("mailto:")
							|| url.startsWith("voicemail:")
							|| url.startsWith("geo:")
							|| url.startsWith("google.streetview:")) {
						Intent intent = new Intent(Intent.ACTION_VIEW,
								Uri.parse(url));
						activity.startActivity(intent);
						return;
					}

					if (url.startsWith("mfox:external:")) {
						url = url.substring(16);
						Intent intent = new Intent(
								"android.intent.action.VIEW", Uri.parse(url));
						activity.startActivity(intent);
						return;
					}
					if (url.startsWith("mfox:replayvideo")) {
						try {
							Class<? extends Activity> c = activity.getClass();
							Method method = c.getMethod("replayVideo");
							method.invoke(activity);
						} catch (NoSuchMethodException e) {
						} catch (Exception e) {
						}
						return;
					}
					if (url.startsWith("mfox:playvideo")) {
						try {
							Class<? extends Activity> c = activity.getClass();
							Method method = c.getMethod("playVideo");
							method.invoke(activity);
						} catch (NoSuchMethodException e) {
						} catch (Exception e) {
						}
						return;
					}
					if (url.startsWith("mfox:skip")) {
						activity.finish();
						return;
					}
					activity.navigate(mIcon.clickUrl);
				}
			}
		} catch (Exception e) {
		}
	}

}
