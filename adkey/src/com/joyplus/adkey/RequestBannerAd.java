package com.joyplus.adkey;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import android.webkit.URLUtil;
import com.joyplus.adkey.Monitorer.TRACKINGURL;
import com.joyplus.adkey.Monitorer.TRACKINGURL.TYPE;
import com.joyplus.adkey.data.ClickType;
import com.joyplus.adkey.widget.Log;

public class RequestBannerAd extends RequestAd<BannerAd> {

	public RequestBannerAd() {
	}

	public RequestBannerAd(InputStream xmlArg) {
		is = xmlArg;
	}

	private int getInteger(final String text) {
		if (text == null)
			return 0;
		try {
			return Integer.parseInt(text);
		} catch (final NumberFormatException ex) {
			// do nothing, 0 is returned
		}
		return 0;
	}

	private String getAttribute(final Document document, final String elementName, final String attributeName) {

		NodeList nodeList = document.getElementsByTagName(elementName);
		final Element element = (Element) nodeList.item(0);
		if (element != null) {
			String attribute = element.getAttribute(attributeName);
			if(attribute.length() !=0){
				return attribute;
			}
		}
		return null;
	}
	
	private String getValue(final Document document, final String name) {

		NodeList nodeList = document.getElementsByTagName(name);
		final Element element = (Element) nodeList.item(0);
		if (element != null) {
			nodeList = element.getChildNodes();
			if (nodeList.getLength() > 0)
				return nodeList.item(0).getNodeValue();
		}
		return null;
	}

	private boolean getValueAsBoolean(final Document document, final String name) {
		return "yes".equalsIgnoreCase(this.getValue(document, name));
	}

	private int getValueAsInt(final Document document, final String name) {
		return this.getInteger(this.getValue(document, name));
	}

	private String convertStreamToString(java.io.InputStream is) {
		try {
			return new java.util.Scanner(is).useDelimiter("\\A").next();
		} catch (java.util.NoSuchElementException e) {
			return "";
		}
	}
    public BannerAd parse(InputStream inputStream,boolean f) throws RequestException{
    	return parse(inputStream);
    }
	@Override
	BannerAd parse(final InputStream inputStream)
			throws RequestException {

		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		final BannerAd response = new BannerAd();

		try {
			db = dbf.newDocumentBuilder();
			InputSource src = new InputSource(inputStream);
			src.setEncoding(Const.RESPONSE_ENCODING);
			final Document doc = db.parse(src);

			final Element element = doc.getDocumentElement();

			if (element == null)
				throw new RequestException(
						"Cannot parse Response, document is not an xml");

			final String errorValue = this.getValue(doc, "error");
			if (errorValue != null)
				throw new RequestException("Error Response received: "
						+ errorValue);

			final String type = element.getAttribute("type");
			element.normalize();
			if ("imageAd".equalsIgnoreCase(type)) {
				response.setType(Const.IMAGE);
				response.setBannerWidth(this.getValueAsInt(doc, "bannerwidth"));
				response.setBannerHeight(this
						.getValueAsInt(doc, "bannerheight"));
				final ClickType clickType = ClickType.getValue(this.getValue(
						doc, "clicktype"));
				response.setClickType(clickType);
				response.setClickUrl(this.getValue(doc, "clickurl"));
				response.setImageUrl(this.getValue(doc, "imageurl"));
				response.setRefresh(this.getValueAsInt(doc, "refresh"));
				response.setScale(this.getValueAsBoolean(doc, "scale"));
				response.setSkipPreflight(this.getValueAsBoolean(doc,
						"skippreflight"));
			} else if ("textAd".equalsIgnoreCase(type)) {
				response.setType(Const.TEXT);
				response.setText(this.getValue(doc, "htmlString"));
				String mImpressionUrl = this.getValue(doc, "impressionurl");
				if(URLUtil.isHttpsUrl(mImpressionUrl)||URLUtil.isHttpUrl(mImpressionUrl))
				{
					response.setmImpressionUrl(mImpressionUrl);
				}
				//change be Jas
				String mTrackingUrl = this.getValue(doc, "trackingurl_miaozhen");
				if(URLUtil.isHttpsUrl(mTrackingUrl)||URLUtil.isHttpUrl(mTrackingUrl))
				{
					TRACKINGURL url = new TRACKINGURL();
					url.Type        = TYPE.MIAOZHEN;
					url.URL         = mTrackingUrl;
					response.setmTrackingUrl(url);
				}
				mTrackingUrl = this.getValue(doc, "trackingurl_iresearch");
				if(URLUtil.isHttpsUrl(mTrackingUrl)||URLUtil.isHttpUrl(mTrackingUrl))
				{
					TRACKINGURL url = new TRACKINGURL();
					url.Type        = TYPE.IRESEARCH;
					url.URL         = mTrackingUrl;
					response.setmTrackingUrl(url);
				}
				mTrackingUrl = this.getValue(doc, "trackingurl_admaster");
				if(URLUtil.isHttpsUrl(mTrackingUrl)||URLUtil.isHttpUrl(mTrackingUrl))
				{
					TRACKINGURL url = new TRACKINGURL();
					url.Type        = TYPE.ADMASTER;
					url.URL         = mTrackingUrl;
					response.setmTrackingUrl(url);
				}
				mTrackingUrl = this.getValue(doc, "trackingurl_nielsen");
				if(URLUtil.isHttpsUrl(mTrackingUrl)||URLUtil.isHttpUrl(mTrackingUrl))
				{
					TRACKINGURL url = new TRACKINGURL();
					url.Type        = TYPE.NIELSEN;
					url.URL         = mTrackingUrl;
					response.setmTrackingUrl(url);
				}
				String Creative_res_url = this.getAttribute(doc, "creative_res_url","src");
				response.SetCreative_res_url(Creative_res_url);
				response.SetCreative_res_hash(this.getAttribute(doc, "creative_res_url","hash"));
				//end change by Jas
				String skipOverlay = this.getAttribute(doc, "htmlString", "skipoverlaybutton");
				if (skipOverlay != null){
					response.setSkipOverlay(Integer.parseInt(skipOverlay));
				}
				final ClickType clickType = ClickType.getValue(this.getValue(
						doc, "clicktype"));
				response.setClickType(clickType);
				response.setClickUrl(this.getValue(doc, "clickurl"));
				response.setRefresh(this.getValueAsInt(doc, "refresh"));
				response.setScale(this.getValueAsBoolean(doc, "scale"));
				response.setSkipPreflight(this.getValueAsBoolean(doc,
						"skippreflight"));
			} else if ("mraidAd".equalsIgnoreCase(type)) {
				response.setType(Const.MRAID);
				response.setText(this.getValue(doc, "htmlString"));
				String skipOverlay = this.getAttribute(doc, "htmlString", "skipoverlaybutton");
				if (skipOverlay != null){
					response.setSkipOverlay(Integer.parseInt(skipOverlay));
				}
				//add by Jas
				String mImpressionUrl = this.getValue(doc, "impressionurl");
				if(URLUtil.isHttpsUrl(mImpressionUrl)||URLUtil.isHttpUrl(mImpressionUrl))
				{
					response.setmImpressionUrl(mImpressionUrl);
				}
				//end add by Jas
				final ClickType clickType = ClickType.getValue(this.getValue(
						doc, "clicktype"));
				response.setClickType(clickType);
				response.setClickUrl(this.getValue(doc, "clickurl"));
				response.setUrlType(this.getValue(doc, "urltype"));
				response.setRefresh(0);
				response.setScale(this.getValueAsBoolean(doc, "scale"));
				response.setSkipPreflight(this.getValueAsBoolean(doc,
						"skippreflight"));
			} else if ("noAd".equalsIgnoreCase(type))
				response.setType(Const.NO_AD);
			else
				throw new RequestException("Unknown response type " + type);

		} catch (final ParserConfigurationException e) {
			throw new RequestException("Cannot parse Response", e);
		} catch (final SAXException e) {
			throw new RequestException("Cannot parse Response", e);
		} catch (final IOException e) {
			throw new RequestException("Cannot read Response", e);
		} catch (final Throwable t) {
			throw new RequestException("Cannot read Response", t);
		}

		return response;
	}

	@Override
	BannerAd parseTestString() throws RequestException {
		return parse(is);
	}
}
