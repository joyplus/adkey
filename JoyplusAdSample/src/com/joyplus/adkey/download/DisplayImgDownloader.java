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
	private static final int INIT = 1;// 鐎规矮绠熸稉澶岊瀸娑撳娴囬惃鍕Ц閹緤绱伴崚婵嗩瀶閸栨牜濮搁幀渚婄礉濮濓絽婀稉瀣祰閻樿埖锟介敍灞炬畯閸嬫粎濮搁幀锟�	private static final int DOWNLOADING = 2;//濮濓絽婀稉瀣祰娑擄拷
	private static final int DOWNLOADING = 2;
	private static final int PAUSE = 3;//閺嗗倸浠�
	private static final int STOP = 4;//閸嬫粍顒�
	private static final int FAILED = 5;//婢惰精瑙�
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
	 * 閸掋倖鏌囬弰顖氭儊濮濓絽婀稉瀣祰
	 */
	public boolean isdownloading() {
		return state == DOWNLOADING;
	}

	/**
	 * 閸掓繂顬婇崠锟�	 */
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
	 * 閸掆晝鏁ょ痪璺ㄢ柤瀵拷顬婃稉瀣祰閺佺増宓�
	 */
	public void download() {
		//閸撳秹娼版稉锟介嚋娑撶皢ath
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

	// 閸掔娀娅庨弫鐗堝祦鎼存挷鑵憉rlstr鐎电懓绨查惃鍕瑓鏉炶棄娅掓穱鈩冧紖
	public void delete(String urlstr) {
		
	}

	// 鐠佸墽鐤嗛弳鍌氫粻
	public void pause() {
		state = PAUSE;
	}

	// 闁插秶鐤嗘稉瀣祰閻樿埖锟�
	public void reset() {
		state = INIT;
	}

	public int getstate() {
		return state;
	}
}