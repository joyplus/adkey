package com.joyplus.adkey.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import com.joyplus.adkey.Const;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.data.ScreenSaverInfo;

import android.content.Context;

public class PicDownloader {
	private String urlstr;// 下载的地址
	private String localfile;// 保存路径
	private int fileSize = 0;//文件大小
	private int compeleteSize = 0;//文件下载完成大小
	private Context context;
	private static final int INIT = 1;// 定义三种下载的状态：初始化状态，正在下载状态，暂停状态
	private static final int DOWNLOADING = 2;//正在下载中
	private static final int PAUSE = 3;//暂停
	private static final int STOP = 4;//停止
	private static final int FAILED = 5;//失败
	private int state = INIT;

	public PicDownloader(String urlstr, Context context) {
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
	 * 判断是否正在下载
	 */
	public boolean isdownloading() {
		return state == DOWNLOADING;
	}

	/**
	 * 利用线程开始下载数据
	 */
	public void download() {
		//前面一个为path
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

		// localfile的值是什么呢
		@SuppressWarnings("resource")
		@Override
		public void run() {
			// 标记此线程为true
			ScreenSaverInfo screenSaverInfo = new ScreenSaverInfo();
			screenSaverInfo.setBaseurl(Const.DOWNLOAD_PATH+Util.VideoFileDir);
			screenSaverInfo.setUrl(urlstr);
			screenSaverInfo.setPublishid(Util.PublisherId);
			
			localfile = Const.DOWNLOAD_PATH+Util.VideoFileDir+(Util.ScreenSaverAdNum%3)+Util.ExternalName;
			File file = new File(localfile);
			if(file.exists())
				file.delete();
			screenSaverInfo.setFilename(Util.ScreenSaverAdNum%3+Util.ExternalName);
			
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
				// 将要下载的文件写到保存在保存路径下的文件
				byte[] buffer = new byte[1024 * 50];
				int length = -1;
				while ((length = inputstream.read(buffer)) != -1) {
					randomAccessFile.write(buffer, 0, length);
					compeleteSize += length;
					if (compeleteSize == fileSize) {
						state = STOP;
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

	// 删除数据库中urlstr对应的下载器信息
	public void delete(String urlstr) {
		
	}

	// 设置暂停
	public void pause() {
		state = PAUSE;
	}

	// 重置下载状态
	public void reset() {
		state = INIT;
	}

	public int getstate() {
		return state;
	}
}