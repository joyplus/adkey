package com.joyplus.adkey.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.Util;
import android.content.Context;

public class Downloader {
	private String urlstr;	
	private String localfile;
	private int fileSize = 0;	
	private int compeleteSize = 0;
	private Context context;
	private static final int INIT = 1;
	private static final int DOWNLOADING = 2;
	private static final int PAUSE = 3;
	private static final int STOP = 4;	
	private static final int FAILED = 5;
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


	public boolean isdownloading() {
		return state == DOWNLOADING;
	}

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

		@SuppressWarnings("resource")
		@Override
		public void run() {
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

	public void delete(String urlstr) {
		
	}

	
	public void pause() {
		state = PAUSE;
	}

	public void reset() {
		state = INIT;
	}

	public int getstate() {
		return state;
	}
}