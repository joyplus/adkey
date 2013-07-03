package com.joyplus.adkey.download;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;

import com.joyplus.adkey.Const;
import com.joyplus.adkey.RequestException;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.Util.AD_TYPE;
import com.joyplus.adkey.data.ImpressionInfo;
import com.joyplus.adkey.db.ImpressionDao;

public class ImpressionThread extends Thread{
	private Context context;
	private String publisherId;
	private String ad_id;
	private Util.AD_TYPE ad_type;
	private String mImpressionUrl;
	public ImpressionThread(Context context ,String mImpressionUrl, String publisherId,Util.AD_TYPE ad_type){
		this.context = context;
		this.mImpressionUrl = mImpressionUrl;
		this.publisherId = publisherId;
		this.ad_type = ad_type;
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		//impression参数
		String url = mImpressionUrl+"&ds="+Util.GetDeviceName();
		int startInd = url.indexOf("&ad_id");
		if(startInd > 0){
			int endInd = url.indexOf("&", startInd+1);
			ad_id = url.substring(startInd + 7, endInd);
		}
		ImpressionInfo existingInfo = ImpressionDao.getInstance(context).getOneInfo(publisherId, ad_id);
		if(existingInfo != null){
			url = url + "&impression=" + (Integer.valueOf(existingInfo.getDisplay_num())+1);
		}
		DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setSoTimeout(client.getParams(),
				Const.SOCKET_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(client.getParams(),
				Const.CONNECTION_TIMEOUT);
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		int responseCode = 0;
		try {
			response = client.execute(get);
			responseCode = response.getStatusLine().getStatusCode();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally{
			if (responseCode == HttpURLConnection.HTTP_OK) {
				if(ad_id != null){
					ImpressionDao.getInstance(context).delete(publisherId, ad_id);
				}
			} else {
				if(existingInfo != null){
					ImpressionDao.getInstance(context).updataInfos(publisherId, ad_id, Integer.valueOf(existingInfo.getDisplay_num())+1);
				} else {
					ImpressionInfo info = new ImpressionInfo();
					info.setPublisher_id(publisherId);
					info.setAd_id(ad_id);
					info.setAd_type(ad_type+"");
					info.setDisplay_num(1+"");
					ImpressionDao.getInstance(context).InsertOneInfo(info);
				}
			}
			
		}
	}
}