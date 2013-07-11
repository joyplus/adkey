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

public class DownloaderSmallVideo {
	private String urlstr;//
	private String localfile;//
	private int fileSize = 0;//
	private int compeleteSize = 0;//
	private Context context;
	private static final int INIT = 1;//
	private static final int DOWNLOADING = 2;//
	private static final int PAUSE = 3;//
	private static final int STOP = 4;//
	private static final int FAILED = 5;//
	private int state = INIT;

	public DownloaderSmallVideo(String urlstr, Context context) {
		this.urlstr = urlstr;
		this.context = context;
		/*
		 * if the Const.DOWNLOAD_PATH doesn't exists,mkdirs this folder
		 */
		File cacheDir = new File(Const.DOWNLOAD_PATH + Util.VideoFileDir);
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	/**
	 * judge is downloading
	 */
	public boolean isdownloading() {
		return state == DOWNLOADING;
	}

	/**
	 * init the state
	 */
	private void init() {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(urlstr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			fileSize = connection.getContentLength();
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			if (connection != null)
				connection.disconnect();
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}

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

		// localfile
		@SuppressWarnings("resource")
		@Override
		public void run() {
			// true
			localfile = Const.DOWNLOAD_PATH + Util.VideoFileDir
					+ Const.DOWNLOADING_SMALLVIDEO;
			File tempFile = new File(localfile);
			if (tempFile.exists()) {
				tempFile.delete();
			}
			HttpURLConnection connection = null;
			RandomAccessFile randomAccessFile = null;
			InputStream inputstream = null;
			try {
				URL url = new URL(urlstr);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(60 * 1000);
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
						randomAccessFile.close();
						/*
						 * set adv_temp to adv_temp.mp4
						 */
						if (Util.PlayingSmallVideoName == null
								|| Util.PlayingSmallVideoName.contains("http")) {
							renameFile(tempFile, Const.DOWNLOAD_PATH
									+ Util.VideoFileDir
									+ Const.DOWNLOAD_SMALLVIDEO
									+ Util.ExternalName);
						} else {
							if (Util.PlayingSmallVideoName.contains("_ts")) {
								renameFile(tempFile, Const.DOWNLOAD_PATH
										+ Util.VideoFileDir
										+ Const.DOWNLOAD_SMALLVIDEO
										+ Util.ExternalName);
							} else {
								renameFile(tempFile, Const.DOWNLOAD_PATH
										+ Util.VideoFileDir
										+ Const.DOWNLOAD_SMALLVIDEO + "_ts"
										+ Util.ExternalName);
							}
						}
					}
					if (state == PAUSE || state == STOP) {
						return;
					}
				}
			} catch (Exception e) {
				state = STOP;
				e.printStackTrace();
				if (tempFile.exists()) {
					tempFile.delete();
				}
				if (connection != null)
					connection.disconnect();
			} finally {
				state = STOP;
				if (connection != null)
					connection.disconnect();
			}
		}
	}

	private void renameFile(File file, String path) {
		File filedone_ts = new File(path);
		if (filedone_ts.exists()) {
			filedone_ts.delete();
		}
		file.renameTo(filedone_ts);
	}

	// delete
	public void delete(String urlstr) {

	}

	// setPause，the download will stop
	public void pause() {
		state = PAUSE;
	}

	// re init
	public void reset() {
		state = INIT;
	}

	public int getstate() {
		return state;
	}
}