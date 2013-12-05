package com.joyplus.ad.data;

import java.io.Serializable;

public class TOPBAR implements Serializable{
	 public final static String tag          = "topbar";
	 public       String custombackgroundurl = "";
	 public       String show                = "";
	 
	
	public String toString() {
		StringBuffer ap = new StringBuffer();
		ap.append(" TOPBAR={")
		  .append(" ,custombackgroundurl="+custombackgroundurl)
		  .append(" ,show="+show)
		  .append(" }");
		return ap.toString();
	}
}
