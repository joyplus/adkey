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

public class Downloader {
	private String urlstr;// 涓嬭浇鐨勫湴鍧�
	private String localfile;// 淇濆瓨璺緞
	private int fileSize = 0;//鏂囦欢澶у皬
	private int compeleteSize = 0;//鏂囦欢涓嬭浇瀹屾垚澶у皬
	private Context context;
	private static final int INIT = 1;// 瀹氫箟涓夌涓嬭浇鐨勭姸鎬侊細鍒濆鍖栫姸鎬侊紝姝ｅ湪涓嬭浇鐘舵�锛屾殏鍋滅姸鎬�
	private static final int DOWNLOADING = 2;//姝ｅ湪涓嬭浇涓�
	private static final int PAUSE = 3;//鏆傚仠
	private static final int STOP = 4;//鍋滄
	private static final int FAILED = 5;//澶辫触
	private int state = INIT;

	public Downloader(String urlstr, Context context) {
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
		//
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

		// localfile鐨勫�鏄粈涔堝憿
		@SuppressWarnings("resource")
		@Override
		public void run() {
			// 鏍囪姝ょ嚎绋嬩负true
			localfile = Const.DOWNLOAD_PATH+Util.VideoFileDir+Const.DOWNLOADING_FILE;
			
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
					if (compeleteSize == fileSize) {
						state = STOP;
						/*
						 * be sure there hasn't adv_temp.mp4
						 */
						File file = new File(Const.DOWNLOAD_PATH+Util.VideoFileDir+Const.DOWNLOAD_READY_FILE);
						if(file.exists())
						{
							file.delete();
						}
						randomAccessFile.close();
						/*
						 * set adv_temp to adv_temp.mp4
						 */
						File filetemp = new File(Const.DOWNLOAD_PATH+Util.VideoFileDir+Const.DOWNLOADING_FILE);
						if(filetemp.exists())
						{
							File filedone = new File(Const.DOWNLOAD_PATH+Util.VideoFileDir+Const.DOWNLOAD_PLAY_FILE+Util.ExternalName);
							if(filedone.exists()){
								filedone.delete();
							}
							filetemp.renameTo(filedone);
						}
					}
					if (state == PAUSE||state == STOP) {
						return;
					}
				}
			} catch (Exception e) {
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
	// 鐠佸墽鐤嗛弳鍌氫粻
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