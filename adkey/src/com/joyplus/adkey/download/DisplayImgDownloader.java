package com.joyplus.adkey.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import com.joyplus.adkey.Const;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.widget.Log;

import android.content.Context;

public class DisplayImgDownloader {
	private String urlstr;// 娑撳娴囬惃鍕勾閸э拷
	private String localfile;// 娣囨繂鐡ㄧ捄顖氱窞
	private int fileSize = 0;//閺傚洣娆㈡径褍鐨�
	private int compeleteSize = 0;//閺傚洣娆㈡稉瀣祰鐎瑰本鍨氭径褍鐨�
	private Context context;
	private static final int INIT = 1;// 瀹氫箟涓夌涓嬭浇鐨勭姸鎬侊細鍒濆鍖栫姸鎬侊紝姝ｅ湪涓嬭浇鐘舵�锛屾殏鍋滅姸鎬�
	private static final int DOWNLOADING = 2;//姝ｅ湪涓嬭浇涓�
	private static final int PAUSE = 3;//鏆傚仠
	private static final int STOP = 4;//鍋滄
	private static final int FAILED = 5;//澶辫触
	private int state = INIT;

	public DisplayImgDownloader(String urlstr, Context context) {
		this.urlstr = urlstr;
		this.context = context;
		/*
		 * if the Const.DOWNLOAD_PATH doesn't exists,mkdirs this folder
		 */
		File cacheDir = new File(Const.DOWNLOAD_PATH+Util.VideoFileDir);
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	/**
	 * 鍒ゆ柇鏄惁姝ｅ湪涓嬭浇
	 */
	public boolean isdownloading() {
		return state == DOWNLOADING;
	}

	/**
	 * 鍒濆鍖�
	 */
	private void init() {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(urlstr);
			connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			fileSize = connection.getContentLength();
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			if(connection!=null)
				connection.disconnect();
		} finally{
			if(connection!=null)
				connection.disconnect();
		}
	}

	/**
	 * 鍒╃敤绾跨▼寮�涓嬭浇鏁版嵁
	 */
	public void download() {
		//鍓嶉潰涓�釜涓簆ath
		new MyThread(urlstr, context).start();
	}

	private class MyThread extends Thread {
		private String urlstr;
		private Context context;
		long percent = 0;

		public MyThread(String urlstr, Context context) {
			this.urlstr = urlstr;
			this.context = context;
		}

		// localfile
		@SuppressWarnings("resource")
		@Override
		public void run() {
			//true
			localfile = Const.DOWNLOAD_PATH+Util.VideoFileDir+Const.DOWNLOAD_DISPLAY_IMG+Util.ExternalName;
			File file = new File(localfile);
			if(file.exists())
			{
				file.delete();
			}
			HttpURLConnection connection = null;
			RandomAccessFile randomAccessFile = null;
			InputStream inputstream = null;
			try {
				URL url = new URL(urlstr);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(60*1000);
				connection.setRequestMethod("GET");
				fileSize = connection.getContentLength();
				randomAccessFile = new RandomAccessFile(localfile, "rwd");
				inputstream = connection.getInputStream();
				// 
				byte[] buffer = new byte[1024 * 50];
				int length = -1;
				while ((length = inputstream.read(buffer)) != -1) {
					randomAccessFile.write(buffer, 0, length);
					compeleteSize += length;
					if (compeleteSize == fileSize) {}
					if (state == PAUSE||state == STOP) {
						return;
					}
				}
			} catch (Exception e) {
				if(file!=null&&file.exists())
				{
					file.delete();
				}
				state = STOP;
				e.printStackTrace();
				if(connection!=null)
					connection.disconnect();
			} finally {
				state = STOP;
				if(connection!=null)
					connection.disconnect();
			}
		}
	}

	// 鍒犻櫎鏁版嵁搴撲腑urlstr瀵瑰簲鐨勪笅杞藉櫒淇℃伅
	public void delete(String urlstr) {
		
	}

	// 璁剧疆鏆傚仠
	public void pause() {
		state = PAUSE;
	}

	// 閲嶇疆涓嬭浇鐘舵�
	public void reset() {
		state = INIT;
	}

	public int getstate() {
		return state;
	}
}