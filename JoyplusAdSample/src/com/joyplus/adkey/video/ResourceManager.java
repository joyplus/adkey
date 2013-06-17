package com.joyplus.adkey.video;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;

public class ResourceManager {

	public static final int RESOURCE_LOADED_MSG = 100;
	public static final int TYPE_UNKNOWN = -1;
	public static final int TYPE_FILE = 0;
	public static final int TYPE_ZIP = 1;
	public static boolean sDownloading = false;
	public static boolean sCancel = false;
	public static HttpGet sDownloadGet;

	public static final String VERSION = "version.txt";
	public static final String TOPBAR_BG = "bar.png";
	public static final String BOTTOMBAR_BG = "bar.png";

	public static final String PLAY_ICON = "video_play.png";
	public static final String PAUSE_ICON = "video_pause.png";
	public static final String REPLAY_ICON = "video_replay.png";
	public static final String BACK_ICON = "browser_back.png";
	public static final String FORWARD_ICON = "browser_forward.png";
	public static final String RELOAD_ICON = "video_replay.png";
	public static final String EXTERNAL_ICON = "browser_external.png";
	public static final String SKIP_ICON = "skip.png";
	public static final String CLOSE_BUTTON_NORMAL = "close_button_normal.png";
	public static final String CLOSE_BUTTON_PRESSED = "close_button_pressed.png";

	public static final int DEFAULT_TOPBAR_BG_RESOURCE_ID = -1;
	public static final int DEFAULT_BOTTOMBAR_BG_RESOURCE_ID = -2;

	public static final int DEFAULT_PLAY_IMAGE_RESOURCE_ID = -11;
	public static final int DEFAULT_PAUSE_IMAGE_RESOURCE_ID = -12;
	public static final int DEFAULT_REPLAY_IMAGE_RESOURCE_ID = -13;
	public static final int DEFAULT_BACK_IMAGE_RESOURCE_ID = -14;
	public static final int DEFAULT_FORWARD_IMAGE_RESOURCE_ID = -15;
	public static final int DEFAULT_RELOAD_IMAGE_RESOURCE_ID = -16;
	public static final int DEFAULT_EXTERNAL_IMAGE_RESOURCE_ID = -17;
	public static final int DEFAULT_SKIP_IMAGE_RESOURCE_ID = -18;

	public static final int DEFAULT_CLOSE_BUTTON_NORMAL_RESOURCE_ID = -29;
	public static final int DEFAULT_CLOSE_BUTTON_PRESSED_RESOURCE_ID = -30;

	private static HashMap<Integer, Drawable> sResources = new HashMap<Integer,Drawable>();

	private Handler mHandler;
	private HashMap<Integer, Drawable> mResources = new HashMap<Integer,Drawable>();

	public static Drawable getDefaultResource(int resId) {
		return sResources.get(resId);
	}

	public static Drawable getDefaultSkipButton(Context ctx){
		return buildDrawable(ctx, SKIP_ICON);
	}
	
	public static boolean resourcesInstalled(Context ctx) {
		boolean result = false;
		String[] files = ctx.fileList();
		for (int i = 0; i < files.length; i++) {
			if (VERSION.equals(files[i])) {
				return true;
			}
		}
		return result;
	}

	public static long getInstalledVersion(Context ctx) {
		long result = -1;
		FileInputStream in = null;
		try {
			in = ctx.openFileInput(VERSION);
			InputStreamReader isr = new InputStreamReader(in, "UTF-8");
			BufferedReader reader = new BufferedReader(isr);
			String version = reader.readLine();
			result = Long.valueOf(version).longValue();
		} catch (Exception e) {

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {

				}
			}
		}
		return result;
	}

	public static void saveInstalledVersion(Context ctx, long version) {
		FileOutputStream out = null;
		try {
			out = ctx.openFileOutput(VERSION, Context.MODE_PRIVATE);
			OutputStreamWriter osr = new OutputStreamWriter(out, "UTF-8");
			osr.write(String.valueOf(version));
			osr.flush();
		} catch (Exception e) {

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {

				}
			}
		}
	}

	public void releaseInstance(){
		Iterator<Entry<Integer, Drawable>> it = mResources.entrySet().iterator();
		while(it.hasNext()) {
			Entry<Integer, Drawable> pairsEntry = (Entry<Integer, Drawable>)it.next();
			it.remove();
			BitmapDrawable d = (BitmapDrawable) pairsEntry.getValue();

		}
		assert(mResources.size()==0);
		System.gc();
	}

	private static void initDefaultResource(Context ctx, int resource) {
		switch (resource) {
		case DEFAULT_PLAY_IMAGE_RESOURCE_ID:
			registerImageResource(ctx, DEFAULT_PLAY_IMAGE_RESOURCE_ID,
					PLAY_ICON);
			break;
		case DEFAULT_PAUSE_IMAGE_RESOURCE_ID:
			registerImageResource(ctx, DEFAULT_PAUSE_IMAGE_RESOURCE_ID,
					PAUSE_ICON);
			break;
		case DEFAULT_REPLAY_IMAGE_RESOURCE_ID:
			registerImageResource(ctx, DEFAULT_REPLAY_IMAGE_RESOURCE_ID,
					REPLAY_ICON);
			break;
		case DEFAULT_BACK_IMAGE_RESOURCE_ID:
			registerImageResource(ctx, DEFAULT_BACK_IMAGE_RESOURCE_ID,
					BACK_ICON);
			break;
		case DEFAULT_FORWARD_IMAGE_RESOURCE_ID:
			registerImageResource(ctx, DEFAULT_FORWARD_IMAGE_RESOURCE_ID,
					FORWARD_ICON);
			break;
		case DEFAULT_RELOAD_IMAGE_RESOURCE_ID:
			registerImageResource(ctx, DEFAULT_RELOAD_IMAGE_RESOURCE_ID,
					RELOAD_ICON);
			break;
		case DEFAULT_EXTERNAL_IMAGE_RESOURCE_ID:
			registerImageResource(ctx, DEFAULT_EXTERNAL_IMAGE_RESOURCE_ID,
					EXTERNAL_ICON);
			break;
		case DEFAULT_SKIP_IMAGE_RESOURCE_ID:
			registerImageResource(ctx, DEFAULT_SKIP_IMAGE_RESOURCE_ID,
					SKIP_ICON);
			break;
		case DEFAULT_TOPBAR_BG_RESOURCE_ID:
			registerImageResource(ctx, DEFAULT_TOPBAR_BG_RESOURCE_ID, TOPBAR_BG);
			break;
		case DEFAULT_BOTTOMBAR_BG_RESOURCE_ID:
			registerImageResource(ctx, DEFAULT_BOTTOMBAR_BG_RESOURCE_ID,
					BOTTOMBAR_BG);
			break;
		case DEFAULT_CLOSE_BUTTON_NORMAL_RESOURCE_ID:
			registerImageResource(ctx, DEFAULT_CLOSE_BUTTON_NORMAL_RESOURCE_ID,
					CLOSE_BUTTON_NORMAL);
			break;
		case DEFAULT_CLOSE_BUTTON_PRESSED_RESOURCE_ID:
			registerImageResource(ctx, DEFAULT_CLOSE_BUTTON_PRESSED_RESOURCE_ID,
					CLOSE_BUTTON_PRESSED);
			break;
		}
	}

	private static void registerImageResource(Context ctx, int resId,
			String name) {
		Drawable d = buildDrawable(ctx, name);
		if (d != null) {
			sResources.put(resId, d);
		}
	}

	private static Drawable buildDrawable(Context ctx, String name) {

		InputStream in = null;
		try {
			in = ctx.getClass().getClassLoader()
					.getResourceAsStream("defaultresources/" + name);

			Bitmap b = BitmapFactory.decodeStream(in);
			if (b != null) {

				DisplayMetrics m = ctx.getResources().getDisplayMetrics();
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
				return new BitmapDrawable(ctx.getResources(), b);
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
		return null;
	}

	public static boolean isDownloading() {
		return sDownloading;
	}

	public static void cancel() {
		sCancel = true;
		if (sDownloadGet != null) {
			sDownloadGet.abort();
			sDownloadGet = null;
		}

		sResources.clear();
	}

	public ResourceManager(Context ctx, Handler h) {

		mHandler = h;
	}

	public void fetchResource(Context ctx, String url, int resourceId) {
		if (sResources.get(resourceId) == null) {
			new FetchImageTask(ctx, url, resourceId).execute();
		}
	}

	public boolean containsResource(int resourceId) {
		return (mResources.get(resourceId) != null || mResources
				.get(resourceId) != null);
	}

	public Drawable getResource(Context ctx, int resourceId) {
		BitmapDrawable d;
		d = (BitmapDrawable) mResources.get(resourceId);
		if(d!=null){
			return d;
		}
		return ResourceManager.getStaticResource(ctx, resourceId);
	}

	public static Drawable getStaticResource(Context ctx, int resourceId) {
		BitmapDrawable d = (BitmapDrawable) sResources.get(resourceId);
		if (d == null || d.getBitmap().isRecycled()) {

			initDefaultResource(ctx, resourceId);

			d = (BitmapDrawable) sResources.get(resourceId);
		}
		return d;
	}
	
	private class FetchImageTask extends AsyncTask<Void, Void, Boolean> {
		String mUrl;
		int mResourceId;
		Context mContext;

		public FetchImageTask(Context ctx, String url, int resId) {
			mContext = ctx;
			mUrl = url;
			mResourceId = resId;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			Message msg = mHandler.obtainMessage(RESOURCE_LOADED_MSG,
					mResourceId, 0);
			mHandler.sendMessage(msg);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Drawable d = null;
			if ((mUrl != null) && (mUrl.length() > 0)) {
				d = fetchImage(mUrl);
			}
			if (d != null) {
				mResources.put(mResourceId, d);
				return true;
			}
			return false;
		}

		private Drawable fetchImage(String urlString) {
			try {
				URL url = new URL(urlString);
				InputStream is = (InputStream) url.getContent();
				Bitmap b = BitmapFactory.decodeStream(is);
				if (b != null) {
					DisplayMetrics m = mContext.getResources()
							.getDisplayMetrics();
					int w = b.getWidth();
					int h = b.getHeight();
					int imageWidth = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, w, m);
					int imageHeight = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, h, m);
					if ((imageWidth != w) || (imageHeight != h)) {
						b = Bitmap.createScaledBitmap(b, imageWidth,
								imageHeight, false);
					}
					return new BitmapDrawable(mContext.getResources(), b);
				}
			} catch (Exception e) {
			}
			return null;
		}

	}

}
