package com.joyplus.ad.data;

import java.io.Serializable;

public class BOTTOMBAR implements Serializable{
	 public final static String tag          ="bottombar";
	 public       String custombackgroundurl ="";
	 public       String show                ="";
	 public       String pausebutton         =""; 
	 public       String replaybutton        ="";         
	 public       String timer               ="";
	
	public String toString() {
		StringBuffer ap = new StringBuffer();
		ap.append(" BOTTOMBAR={")
		  .append(" ,custombackgroundurl="+custombackgroundurl)
		  .append(" ,show="+show)
		  .append(" ,pausebutton="+pausebutton)
		  .append(" ,replaybutton="+replaybutton)
		  .append(" ,timer="+timer)
		  .append(" }");
		return ap.toString();
	}
}
