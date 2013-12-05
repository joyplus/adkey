package com.joyplus.ad.data;

import java.io.Serializable;

public class TRACKINGEVENTS implements Serializable{

	  public final static String tag = "trackingevents";
	  
	  @Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer ap = new StringBuffer();
		ap.append(" TRACKINGEVENTS={")
		  .append(" ,tag="+tag)
		  .append(" }");
		return ap.toString();
	}
}
