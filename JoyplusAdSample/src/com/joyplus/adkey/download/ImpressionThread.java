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

import com.joyplus.adkey.Const;
import com.joyplus.adkey.RequestException;
import com.joyplus.adkey.Util;

public class ImpressionThread extends Thread{
	
	public ImpressionThread(){
		
	}
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		//impression参数
		String url = Util.mImpressionUrl+"&ds="+Util.GetDeviceName();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setSoTimeout(client.getParams(),
				Const.SOCKET_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(client.getParams(),
				Const.CONNECTION_TIMEOUT);
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(get);
			int responseCode = response.getStatusLine().getStatusCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
			} else {
				try
				{
					throw new RequestException("Server Error. Response code:"
							+ responseCode);
				} catch (RequestException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
		}
	}
}