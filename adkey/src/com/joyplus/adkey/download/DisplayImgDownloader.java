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
	private String urlstr;// 濞戞挸顑堝ù鍥儍閸曨偅鍕鹃柛褝鎷�	
	private String localfile;// 濞ｅ洦绻傞悺銊ф崉椤栨氨绐�
	private int fileSize = 0;//闁哄倸娲ｅ▎銏″緞瑜嶉惃锟�	
	private int compeleteSize = 0;//闁哄倸娲ｅ▎銏＄▔鐎ｎ厽绁伴悗鐟版湰閸ㄦ碍寰勮閻拷
	private Context context;
	private static final int INIT = 1;// 閻庤鐭粻鐔哥▔婢跺矈鐎稿☉鎾愁儓濞村洭鎯冮崟顓炐﹂柟顑跨筏缁变即宕氬┑鍡╃�闁告牗鐗滄慨鎼佸箑娓氬﹦绀夋慨婵撶到濠�亝绋夌�顓熺グ闁绘鍩栭敓浠嬫晬鐏炵偓鐣柛瀣矌婵悂骞�敓锟�private static final int DOWNLOADING = 2;//婵繐绲藉﹢顏呯▔鐎ｎ厽绁板☉鎿勬嫹
	private static final int DOWNLOADING = 2;
	private static final int PAUSE = 3;//闁哄棗鍊告禒锟�	
	private static final int STOP = 4;//闁稿绮嶉锟�	
	private static final int FAILED = 5;//濠㈡儼绮剧憴锟�	
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
	 * 闁告帇鍊栭弻鍥及椤栨碍鍎婃慨婵撶到濠�亝绋夌�顓熺グ
	 */
	public boolean isdownloading() {
		return state == DOWNLOADING;
	}

	/**
	 * 闁告帗绻傞‖濠囧礌閿燂拷	 */
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
	 * 闁告巻鏅濋弫銈囩棯鐠恒劉鏌ょ�顕嗘嫹椤﹥绋夌�顓熺グ闁轰胶澧楀畵锟�	 */
	public void download() {
		//闁告挸绉瑰鐗堢▔閿熶粙鍤嬪☉鎾剁殺ath
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

	// 闁告帞濞�▍搴ㄥ极閻楀牆绁﹂幖瀛樻尫閼垫唹rlstr閻庣數鎳撶花鏌ユ儍閸曨亞鐟撻弶鐐舵濞呮帗绌遍埄鍐х礀
	public void delete(String urlstr) {
		
	}

	// 閻犱礁澧介悿鍡涘汲閸屾矮绮�
	public void pause() {
		state = PAUSE;
	}

	// 闂佹彃绉堕悿鍡樼▔鐎ｎ厽绁伴柣妯垮煐閿燂拷
	public void reset() {
		state = INIT;
	}

	public int getstate() {
		return state;
	}
}