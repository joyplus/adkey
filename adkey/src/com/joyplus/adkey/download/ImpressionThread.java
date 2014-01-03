package com.joyplus.adkey.download;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import android.content.Context;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.Util;
import com.joyplus.adkey.data.ImpressionInfo;
import com.joyplus.adkey.db.ImpressionDao;

public class ImpressionThread extends Thread{
	private final static int MAXNUM = 5;
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
		String url = mImpressionUrl;
		if(url==null || "".equals(url))return;
		int NUMBER = 1,ReportCount = 0;
		ImpressionInfo existingInfo = ImpressionDao.getInstance(context).getOneInfo(publisherId, ad_id);
		if(existingInfo != null){
			NUMBER += Integer.valueOf(existingInfo.getDisplay_num());
		}
		NUMBER = (NUMBER>MAXNUM)?MAXNUM:NUMBER;
	    while((NUMBER--)>0){
			if(REPORT(url))ReportCount++;
		}
		if((NUMBER-ReportCount)<=0){
			if(ad_id != null){
				ImpressionDao.getInstance(context).delete(publisherId, ad_id);
			}
		}else {
			if(existingInfo != null){
				ImpressionDao.getInstance(context).updataInfos(publisherId, ad_id, NUMBER-ReportCount);
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
	
	
	private boolean REPORT(String url){
		if(url == null || "".equals(url))return true;
		int responseCode = 0;
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setSoTimeout(client.getParams(),
					Const.SOCKET_TIMEOUT);
			HttpConnectionParams.setConnectionTimeout(client.getParams(),
					Const.CONNECTION_TIMEOUT);
			HttpGet get = new HttpGet(url);
			HttpResponse response;
			response = client.execute(get);
			responseCode = response.getStatusLine().getStatusCode();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			responseCode = 0;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			responseCode = 0;
			e.printStackTrace();
		} catch (IllegalArgumentException e){
			responseCode = 0;
			e.printStackTrace();
		} catch (IllegalStateException e){
			responseCode =0;
			e.printStackTrace();
		}
		if(responseCode == HttpURLConnection.HTTP_OK){
			return true;
		}
		return false;
	}
}