package com.joyplus.ad.download;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import com.joyplus.ad.AdBootServer;
import com.joyplus.ad.AdManager;
import com.joyplus.ad.HttpManager;
import com.joyplus.ad.PhoneManager;
import com.joyplus.ad.config.Log;
import com.joyplus.ad.data.ImpressionInfo;
import com.joyplus.ad.db.ImpressionDao;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


@SuppressWarnings("unused")
public class ImpressionThread extends Thread{
	private Context      context;
	private String       publisherId;
	private String       ad_id;
	private AdManager.AD ad_type;
	private String       mImpressionUrl;
	private static boolean REPORTING = false;
	public  final  static int MAX = 5;
	
	public ImpressionThread(Context context ,String mImpressionUrl, String publisherId,AdManager.AD ad_type){
		this.context        = context;
		this.mImpressionUrl = mImpressionUrl;
		this.publisherId    = publisherId;
		this.ad_type        = ad_type;
	}
    public ImpressionThread(Context context){
    	this.context        = context;
    }
    public String toString(){
    	StringBuffer ap = new StringBuffer();
    	ap.append(" ImpressionThread={")
    	  .append(" mImpressionUrl="+mImpressionUrl)
    	  .append(" publisherId="+publisherId)
    	  .append(" ad_type="+ad_type)
    	  .append(" }");
    	return ap.toString();
    }
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		if(this.context == null)return;
		if(!((mImpressionUrl == null || "".equals(mImpressionUrl))
				&& (publisherId == null || "".equals(publisherId)))){
			int NUM = 0;
			ImpressionInfo existingInfo = ImpressionDao.getInstance(context).getOneInfo(publisherId, mImpressionUrl);
			if(existingInfo != null){
				NUM = Integer.valueOf(existingInfo.getDisplay_num());						
			}
			if(++NUM>0){
				NUM = (NUM>MAX)?MAX:NUM;
				if(existingInfo != null){
					ImpressionDao.getInstance(context).updataInfos(publisherId, mImpressionUrl, Integer.valueOf((NUM)));
				} else {
				    ImpressionInfo info = new ImpressionInfo();
					info.setPublisher_id(publisherId);
					info.setAd_id(ad_id);
					info.setAd_type(ad_type+"");
					info.setDisplay_num(NUM+"");
					info.setImpressionUrl(mImpressionUrl);
					ImpressionDao.getInstance(context).InsertOneInfo(info);
				}
			}
		}
		//start report
		Report();
	}
	
	private void Report() {
		if(REPORTING)return;
		REPORTING    = true;
		List<ImpressionInfo> Infos = 
				ImpressionDao.getInstance(context).getAllInfo();
		Iterator<ImpressionInfo> it = Infos.iterator();
		while(it.hasNext()){
			//if(isNetworkAvailable(context))
				Report(it.next());
			it.remove();
		}
		REPORTING = false;
	}
	private void Report(ImpressionInfo info){
		if(info == null)return;
		if(!(info.getImpressionUrl() == null 
				|| "".equals(info.getImpressionUrl())
				|| (Integer.valueOf(info.getDisplay_num()) <= 0))){
			int NUMBER = Integer.valueOf(info.getDisplay_num());
			NUMBER = NUMBER>MAX?MAX:NUMBER;
			int ReportCount = 0;
			while((NUMBER--)>0){
				if(Report(info.getImpressionUrl()))ReportCount++;
			}
			if(ReportCount<NUMBER){
				ImpressionInfo existingInfo = ImpressionDao.getInstance(context).getOneInfo(publisherId, ad_id);
				if(existingInfo != null){
					ImpressionDao.getInstance(context).updataInfos(publisherId, ad_id, Integer.valueOf(existingInfo.getDisplay_num())+1);
				} else {
					ImpressionInfo ninfo = new ImpressionInfo();
					ninfo.setPublisher_id(info.getPublisher_id());
					ninfo.setAd_id(info.getAd_id());
					ninfo.setAd_type(info.getAd_type()+"");
					ninfo.setDisplay_num((NUMBER+ReportCount)+"");
					ninfo.setImpressionUrl(info.getImpressionUrl());
					ImpressionDao.getInstance(context).InsertOneInfo(ninfo);
				}
			}
		}
	}
	
	private boolean Report(String url){
		if(url == null || "".equals(url))return true;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setSoTimeout(client.getParams(),
				HttpManager.SOCKET_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(client.getParams(),
				HttpManager.CONNECTION_TIMEOUT);
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		int responseCode = 0;
		try {
			response        =  client.execute(get);
			responseCode    =  response.getStatusLine().getStatusCode();
			if(responseCode == HttpURLConnection.HTTP_OK)return true;
		} catch (Exception e){
			e.printStackTrace();
		} 
		return false;
	}
	
	public boolean isNetworkAvailable(Context ctx) {
		int networkStatePermission = ctx
				.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);
		if (networkStatePermission == PackageManager.PERMISSION_GRANTED) {
			ConnectivityManager mConnectivity = (ConnectivityManager) ctx
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = mConnectivity.getActiveNetworkInfo();
			if (info == null) {return false;}
			int netType = info.getType();
			if ((netType == ConnectivityManager.TYPE_WIFI)
					|| (netType == ConnectivityManager.TYPE_MOBILE)) {
				return info.isConnected();
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
}