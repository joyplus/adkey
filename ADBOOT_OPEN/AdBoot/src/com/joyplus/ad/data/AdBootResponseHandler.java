package com.joyplus.ad.data;

import java.io.CharArrayWriter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.joyplus.ad.config.Log;

import android.webkit.URLUtil;

public class AdBootResponseHandler extends DefaultHandler{
     
	private ADBOOT  mADBOOT  = null;
	private final static boolean Debug = false;
	private CharArrayWriter contents = new CharArrayWriter();
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		contents.write(ch, start, length);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		  if(Debug)Log.d("Jas","endElement() localName="+localName);
		  if (CREATIVE.tag.equals(localName)){
			  if(mADBOOT.video != null && mADBOOT.video.creative != null)
				  mADBOOT.video.creative.URL = contents.toString().trim();
		  } else if (CREATIVE2.tag.equals(localName)){
			  if(mADBOOT.video != null && mADBOOT.video.creative2 != null)
				  mADBOOT.video.creative2.URL = contents.toString().trim();
		  }else if (CREATIVE3.tag.equals(localName)){
			  if(mADBOOT.video != null && mADBOOT.video.creative3 != null)
				  mADBOOT.video.creative3.URL = contents.toString().trim();
		  } else if(IMPRESSIONURL.tag.equals(localName)){
			  if(mADBOOT.video != null && mADBOOT.video.impressionurl != null)
				  throw new SAXException("impressionurl");
			  String impressionurl = contents.toString().trim();
			  if(URLUtil.isHttpsUrl(impressionurl)||URLUtil.isHttpUrl(impressionurl)){
				  mADBOOT.video.impressionurl     = new IMPRESSIONURL();
				  mADBOOT.video.impressionurl.URL = impressionurl;
			  }
		  } else if(TRACKINGURL.tag.equals(localName)){
			  if(mADBOOT.video != null && mADBOOT.video.trackingurl != null)
				  throw new SAXException("trackingevents");
			  		mADBOOT.video.trackingurl = new TRACKINGURL();
			  		mADBOOT.video.trackingurl.URL = contents.toString().trim();
		  } else if(DURATION.tag.equals(localName)){
			  if(mADBOOT.video != null && mADBOOT.video.duration != null)
				  throw new SAXException("duration");
			  mADBOOT.video.duration = new DURATION();
		  } else if(TRACKINGEVENTS.tag.equals(localName)){
			  if(mADBOOT.video != null && mADBOOT.video.trackingevents != null)
				  throw new SAXException("trackingevents");
			  mADBOOT.video.trackingevents = new TRACKINGEVENTS();
		  }
	}

	@Override
	public void startDocument() throws SAXException {
		   mADBOOT = new ADBOOT();
		   if(Debug)Log.d("Jas","startDocument()");
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		  if(Debug)Log.d("Jas","startElement() localName="+localName+" attributes=" +
				  attributes.toString());
		  contents.reset();
		  if (ADBOOT.tag.equals(localName)){
			  String TYPE        = attributes.getValue("type");
			  if(!ADBOOT.type.equals(TYPE))throw new SAXException("Type("+TYPE+") is Unaviliable !!!!!");
			  mADBOOT.animation  = attributes.getValue("animation");
		  } else if (ABDOOTVIDEO.tag.equals(localName)){
			  if(mADBOOT.video != null)throw new SAXException("Video Tag more than one!!!");
			  mADBOOT.video = new ABDOOTVIDEO();
			  mADBOOT.video.orientation = attributes.getValue("orientation");
			  mADBOOT.video.expiration  = attributes.getValue("expiration");
		  } else if (CREATIVE.tag.equals(localName)){
			  if(mADBOOT.video == null || mADBOOT.video.creative != null)
				  throw new SAXException("creative");
			  mADBOOT.video.creative = new CREATIVE();
			  mADBOOT.video.creative.display   = attributes.getValue("display");
			  mADBOOT.video.creative.delivery  = attributes.getValue("delivery");
			  mADBOOT.video.creative.type      = attributes.getValue("type");
			  mADBOOT.video.creative.bitrate   = attributes.getValue("bitrate");
			  mADBOOT.video.creative.width     = attributes.getValue("width");
			  mADBOOT.video.creative.height    = attributes.getValue("height");
		  } else if (CREATIVE2.tag.equals(localName)){
			  if(mADBOOT.video == null || mADBOOT.video.creative2 != null)
				  throw new SAXException("creative2");
			  mADBOOT.video.creative2 = new CREATIVE2();
			  mADBOOT.video.creative2.display   = attributes.getValue("display");
			  mADBOOT.video.creative2.delivery  = attributes.getValue("delivery");
			  mADBOOT.video.creative2.type      = attributes.getValue("type");
			  mADBOOT.video.creative2.bitrate   = attributes.getValue("bitrate");
			  mADBOOT.video.creative2.width     = attributes.getValue("width");
			  mADBOOT.video.creative2.height    = attributes.getValue("height");
		  } else if (CREATIVE3.tag.equals(localName)){
			  if(mADBOOT.video == null || mADBOOT.video.creative3 != null)
				  throw new SAXException("creative3");
			  mADBOOT.video.creative3 = new CREATIVE3();
			  mADBOOT.video.creative3.display   = attributes.getValue("display");
			  mADBOOT.video.creative3.delivery  = attributes.getValue("delivery");
			  mADBOOT.video.creative3.type      = attributes.getValue("type");
			  mADBOOT.video.creative3.bitrate   = attributes.getValue("bitrate");
			  mADBOOT.video.creative3.width     = attributes.getValue("width");
			  mADBOOT.video.creative3.height    = attributes.getValue("height");
		  } else if(SKIPBUTTON.tag.equals(localName)){
			  if(mADBOOT.video == null || mADBOOT.video.skipbutton != null)
				  throw new SAXException("skipbutton");
			  mADBOOT.video.skipbutton = new SKIPBUTTON();
			  mADBOOT.video.skipbutton.show = attributes.getValue("show");
			  mADBOOT.video.skipbutton.showafter = attributes.getValue("showafter");
		  } else if(NAVIGATION.tag.equals(localName)){
			  if(mADBOOT.video == null || mADBOOT.video.navigation != null)
				  throw new SAXException("navigation");
			  mADBOOT.video.navigation = new NAVIGATION();
			  mADBOOT.video.navigation.show     = attributes.getValue("show");
			  mADBOOT.video.navigation.allowtap = attributes.getValue("allowtap");
		  } else if(TOPBAR.tag.equals(localName)){
			  if(mADBOOT.video == null || mADBOOT.video.navigation == null || mADBOOT.video.navigation.topbar != null)
				  throw new SAXException("topbar");
			  mADBOOT.video.navigation.topbar = new TOPBAR();
			  mADBOOT.video.navigation.topbar.custombackgroundurl  = attributes.getValue("custombackgroundurl");
			  mADBOOT.video.navigation.topbar.show                 = attributes.getValue("show");
		  } else if(BOTTOMBAR.tag.equals(localName)){
			  if(mADBOOT.video == null || mADBOOT.video.navigation == null || mADBOOT.video.navigation.bottombar != null)
				  throw new SAXException("bottombar");
			  mADBOOT.video.navigation.bottombar = new BOTTOMBAR();
			  mADBOOT.video.navigation.bottombar.custombackgroundurl = attributes.getValue("custombackgroundurl");
			  mADBOOT.video.navigation.bottombar.show                = attributes.getValue("show");
			  mADBOOT.video.navigation.bottombar.pausebutton         = attributes.getValue("pausebutton");
			  mADBOOT.video.navigation.bottombar.replaybutton        = attributes.getValue("replaybutton");
			  mADBOOT.video.navigation.bottombar.timer               = attributes.getValue("timer");
		  }
	}

//	private int getInteger(String text) {
//		if (text == null) {
//			return -1;
//		}
//		try {
//			return Integer.parseInt(text);
//		} catch (NumberFormatException ex) {
//
//		}
//		return -1;
//	}
//
//	private long getLong(String text) {
//		if (text == null) {
//			return -1;
//		}
//		try {
//			return Long.parseLong(text);
//		} catch (NumberFormatException ex) {
//
//		}
//		return -1;
//	}
//
//	private boolean getBoolean(String text) {
//		if (text == null) {
//			return false;
//		}
//		try {
//			return (Integer.parseInt(text) > 0);
//		} catch (NumberFormatException ex) {
//
//		}
//		return false;
//	}

	public ADBOOT getADBOOT() {
		// TODO Auto-generated method stub
		return mADBOOT;
	}
	

}
