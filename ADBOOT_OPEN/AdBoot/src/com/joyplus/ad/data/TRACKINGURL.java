package com.joyplus.ad.data;

import java.io.Serializable;

public class TRACKINGURL implements Serializable{
     
	 public final static String tag = "trackingurl";
	 
	 public String URL = "";
	 
	 
	public String toString() {
		StringBuffer ap = new StringBuffer();
		ap.append(" TRACKINGURL={")
		  .append(" ,tag="+tag)
		  .append(" ,URL="+URL)
		  .append(" }");
		return ap.toString();
	}
}
