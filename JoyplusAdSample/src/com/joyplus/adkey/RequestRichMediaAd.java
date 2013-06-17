package com.joyplus.adkey;

import static com.joyplus.adkey.Const.RESPONSE_ENCODING;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.joyplus.adkey.video.ResponseHandler;
import com.joyplus.adkey.video.RichMediaAd;


public class RequestRichMediaAd extends RequestAd<RichMediaAd> {



	public RequestRichMediaAd() {
		is = null;
	}

	public RequestRichMediaAd(InputStream xmlArg) {
		is = xmlArg;
	}

	@Override
	RichMediaAd parseTestString() throws RequestException {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			ResponseHandler myHandler = new ResponseHandler();
			xr.setContentHandler(myHandler);
			//			byte[] bytes = xml.getBytes(RESPONSE_ENCODING);
			//			InputSource src = new InputSource(new ByteArrayInputStream(bytes));
			Reader reader = new InputStreamReader(is,"UTF-8");
			InputSource src = new InputSource(reader);
			xr.parse(src);
			return myHandler.getRichMediaAd();
		} catch (Exception e) {
			throw new RequestException("Cannot parse Response:"
					+ e.getMessage(), e);
		}
	}

	@Override
	RichMediaAd parse(InputStream inputStream)
			throws RequestException {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			ResponseHandler myHandler = new ResponseHandler();
			xr.setContentHandler(myHandler);
//			InputSource src = new InputSource(inputStream);
			String in = "<ad type=\"video\" animation=\"fade-in\">"+
				     "<video orientation=\"portrait\">"+
				       "<creative delivery=\"streaming\" type=\"application/mp4\" display=\"fullscreen\" width=\"200\" height=\"400\" bitrate=\"100\">"+			         
				        "http://218.76.97.41:80/play/166761ABF893224CD2D42830639A057C804061C0.mp4"+
				       "</creative>"+
				      "<skipbutton show=\"1\" showafter=\"0\"/>"+
				     "<navigation show=\"0\">"+
				        "<topbar custombackgroundurl=\"\" show=\"0\" title=\"fixed\" titlecontent=\"\"/>"+
				        "<bottombar custombackgroundurl=\"\" show=\"0\" "+
				           "backbutton=\"0\" forwardbutton=\"0\" reloadbutton=\"0\"  externalbutton=\"0\" timer=\"0\"/>"+
				       "</navigation>"+
				    "</video>"+
				    "</ad>";
			ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("ISO-8859-1"));
			InputSource src = new InputSource(is);
			src.setEncoding(RESPONSE_ENCODING);
			xr.parse(src);
			return myHandler.getRichMediaAd();

		} catch (Exception e) {
			throw new RequestException("Cannot parse Response:"
					+ e.getMessage(), e);
		}
	}

	private String convertStreamToString(java.io.InputStream is) {
		try {
			return new java.util.Scanner(is).useDelimiter("\\A").next();
		} catch (java.util.NoSuchElementException e) {
			return "";
		}
	}
}