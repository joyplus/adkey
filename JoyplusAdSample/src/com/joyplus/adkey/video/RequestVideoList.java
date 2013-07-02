package com.joyplus.adkey.video;

import static com.joyplus.adkey.Const.RESPONSE_ENCODING;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.joyplus.adkey.AdRequest;
import com.joyplus.adkey.Const;
import com.joyplus.adkey.RequestException;
import com.joyplus.adkey.Util;


public class RequestVideoList {

	public RequestVideoList() {
	}

	public HashMap<String, Long> sendRequest(AdRequest request)
			throws RequestException {
		String url = request.toString() + "&listads=1"+"&ds="+Util.GetDeviceName();
//		String url = request.toString() + "&listads=1";
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

	}

	private HashMap<String, Long> parse(InputStream inputStream)
			throws RequestException {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			ResponseHandler myHandler = new ResponseHandler();
			xr.setContentHandler(myHandler);
			InputSource src = new InputSource(inputStream);
			src.setEncoding(RESPONSE_ENCODING);
			xr.parse(src);
			return myHandler.videoList;
		} catch (Exception e) {
			throw new RequestException("Cannot parse Response:"
					+ e.getMessage(), e);
		}
	}
}