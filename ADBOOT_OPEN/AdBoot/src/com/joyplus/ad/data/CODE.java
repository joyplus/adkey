package com.joyplus.ad.data;

import java.io.Serializable;

public class CODE implements Serializable{
    
	 public final  static String tag = "";
	
	 public String VALUE  = "";
	 
	 public String toString() {
			StringBuffer ap = new StringBuffer();
			ap.append(" CODE={")
			  .append(" ,tag="+tag)
			  .append(" ,VALUE="+VALUE)
			  .append(" }");
			return ap.toString();
	 }
	 
	 public final static String AD_NO = "20001";
}
