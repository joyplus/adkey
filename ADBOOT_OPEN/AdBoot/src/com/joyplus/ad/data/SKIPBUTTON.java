package com.joyplus.ad.data;

import java.io.Serializable;

public class SKIPBUTTON implements Serializable{
	 public final static String tag       = "skipbutton";
	 public       String show      = "";
	 public       String showafter = "";
	 

	public String toString() {
		StringBuffer ap = new StringBuffer();
		ap.append(" SKIPBUTTON={")
		  .append(" ,tag="+tag)
		  .append(" ,show="+show)
		  .append(" ,showafter="+showafter)
		  .append(" }");
		return ap.toString();
	}
}
