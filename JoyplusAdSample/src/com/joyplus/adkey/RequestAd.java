package com.joyplus.adkey;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

public abstract class RequestAd<T> {

	InputStream is;

	public T sendRequest(AdRequest request)
			throws RequestException {
		if (is == null) {
			String url = request.toString();
			String device_name = "V8";
			try {
				device_name = URLEncoder.encode(Util.GetDeviceName(), "utf-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			url = url+"&ds="+device_name;
			DefaultHttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setSoTimeout(client.getParams(),
					Const.SOCKET_TIMEOUT);
			HttpConnectionParams.setConnectionTimeout(client.getParams(),
					Const.CONNECTION_TIMEOUT);
			HttpProtocolParams.setUserAgent(client.getParams(),
					request.getUserAgent());
			HttpGet get = new HttpGet(url);
			HttpResponse response;
			try {
				response = client.execute(get);
				int responseCode = response.getStatusLine().getStatusCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					return parse(response.getEntity().getContent());
				} else {
					throw new RequestException("Server Error. Response code:"
							+ responseCode);
				}
			} catch (RequestException e) {
				throw e;
			} catch (ClientProtocolException e) {
				throw new RequestException("Error in HTTP request", e);
			} catch (IOException e) {
				throw new RequestException("Error in HTTP request", e);
			} catch (Throwable t) {
				throw new RequestException("Error in HTTP request", t);
			}
		} else {
			return parseTestString();
		}
	}

	abstract T parseTestString() throws RequestException;

	abstract T parse(InputStream inputStream) throws RequestException;

}
