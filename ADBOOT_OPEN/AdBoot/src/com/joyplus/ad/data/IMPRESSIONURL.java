package com.joyplus.ad.data;

import java.io.Serializable;

public class IMPRESSIONURL implements Serializable{
     public static final String tag  = "impressionurl";
     
     public String    URL = "";

	
	public String toString() {
		StringBuffer ap = new StringBuffer();
		ap.append(" IMPRESSIONURL={")
		  .append(" ,tag="+tag)
		  .append(" ,URL="+URL)
		  .append(" }");
		return ap.toString();
	}
     
     
	 
}
