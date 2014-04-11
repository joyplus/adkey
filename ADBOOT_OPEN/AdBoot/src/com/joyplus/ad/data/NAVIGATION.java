package com.joyplus.ad.data;

import java.io.Serializable;


public class NAVIGATION implements Serializable{
	 public final static String    tag       = "navigation";
	 public       String    show      = "";
	 public       String    allowtap  = "";
	 public       TOPBAR    topbar    = null;
	 public       BOTTOMBAR bottombar = null;
	 
	public String toString() {
		StringBuffer ap = new StringBuffer();
		ap.append(" NAVIGATION={")
		  .append(" ,tag="+tag)
		  .append(" ,allowtap="+allowtap);
		if(topbar != null)
		  ap.append(" ,topbar="+topbar.toString());
		else 
		  ap.append(" ,topbar=null");
		if(bottombar != null)
			  ap.append(" ,bottombar="+bottombar.toString());
			else 
			  ap.append(" ,bottombar=null");
		ap.append(" }");
		return ap.toString();
	}
}
