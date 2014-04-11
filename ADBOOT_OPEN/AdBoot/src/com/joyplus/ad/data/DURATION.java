package com.joyplus.ad.data;

import java.io.Serializable;

public class DURATION implements Serializable{
     
	 public final static String tag = "duration";
	 
	
	public String toString() {
		StringBuffer ap = new StringBuffer();
		ap.append(" DURATION={")
		  .append(" ,tag="+tag)
		  .append(" }");
		return ap.toString();
	}
	 
}
