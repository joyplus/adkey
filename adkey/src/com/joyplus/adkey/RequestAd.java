package com.joyplus.adkey;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

import android.util.Log;

public abstract class RequestAd<T> {

	InputStream is;

	public T sendRequest(AdRequest request)
			throws RequestException {
		if (is == null) {
			String url = request.toString();
			url = url+"&ds="+Util.GetDeviceName();
			DefaultHttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setSoTimeout(client.getParams(),
					Const.SOCKET_TIMEOUT);
			HttpConnectionParams.setConnectionTimeout(client.getParams(),
					Const.CONNECTION_TIMEOUT);
			HttpProtocolParams.setUserAgent(client.getParams(),
					request.getUserAgent());
			HttpGet get = new HttpGet(url);
			HttpResponse response;
			Log.d("AdBootScreen","RequestAd sendRequest() url="+url);
			try {
				Log.d("AdBootScreen","RequestAd sendRequest() 11url="+url);
				response = client.execute(get);
				Log.d("AdBootScreen","RequestAd sendRequest() 22url="+url);
				int responseCode = response.getStatusLine().getStatusCode();
				Log.d("AdBootScreen","RequestAd sendRequest() 33url="+url);
				if (responseCode == HttpURLConnection.HTTP_OK) {
					Log.d("AdBootScreen","RequestAd sendRequest() 44url="+url);
					return parse(response.getEntity().getContent());
				} else {
					Log.d("AdBootScreen","RequestAd sendRequest() 55url="+url);
					throw new RequestException("Server Error. Response code:"
							+ responseCode);
				}
			} catch (RequestException e) {
				Log.d("AdBootScreen","RequestAd sendRequest() 66url="+url);
				throw e;
			} catch (ClientProtocolException e) {
				Log.d("AdBootScreen","RequestAd sendRequest() 77url="+url);
				throw new RequestException("Error in HTTP request", e);
			} catch (IOException e) {
				Log.d("AdBootScreen","RequestAd sendRequest() 88url="+url);
				throw new RequestException("Error in HTTP request", e);
			} catch (Throwable t) {
				Log.d("AdBootScreen","RequestAd sendRequest() 99url="+url);
				throw new RequestException("Error in HTTP request", t);
			}
		} else {
			Log.d("AdBootScreen","RequestAd sendRequest() 1010url");
			return parseTestString();
		}
	}

	abstract T parseTestString() throws RequestException;

	abstract T parse(InputStream inputStream) throws RequestException;

}
