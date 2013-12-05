package com.joyplus.ad.data;

import java.io.Serializable;


public class ABDOOTVIDEO implements Serializable{
	public final  static String  tag            = "video";
	public        String         orientation    = "";
	public        String         expiration     = "";
	
	public        CREATIVE       creative       = null;
	public        CREATIVE2      creative2      = null;
	public        CREATIVE3      creative3      = null;
	public        IMPRESSIONURL  impressionurl  = null;
	public        TRACKINGURL    trackingurl    = null;
	public        DURATION       duration       = null;
	public        SKIPBUTTON     skipbutton     = null;
	public        NAVIGATION     navigation     = null;
	public        TRACKINGEVENTS trackingevents = null;
	
	public String toString() {
		StringBuffer ap = new StringBuffer();
		ap.append(" ABDOOTVIDEO={")
		  .append(" ,tag="+tag)
		  .append(" ,orientation="+orientation)
		  .append(" ,expiration="+expiration);
		if(creative != null)
		  ap.append(" ,creative="+creative.toString());
		else
		  ap.append(" ,creative=null");
		if(creative2 != null)
	  	  ap.append(" ,creative2="+creative2.toString());
			else
		  ap.append(" ,creative2=null");
		if(creative3 != null)
		  ap.append(" ,creative3="+creative3.toString());
		else
		  ap.append(" ,creative3=null");
		if(impressionurl != null)
		  ap.append(" ,impressionurl="+impressionurl.toString());
		else
		  ap.append(" ,impressionurl=null");
		if(trackingurl != null)
		  ap.append(" ,trackingurl="+trackingurl.toString());
		else
		  ap.append(" ,trackingurl=null");
		if(duration != null)
		  ap.append(" ,duration="+duration.toString());
		else
		  ap.append(" ,duration=null");
		if(skipbutton != null)
		  ap.append(" ,skipbutton="+skipbutton.toString());
		else
		  ap.append(" ,skipbutton=null");
		if(navigation != null)
		  ap.append(" ,navigation="+navigation.toString());
		else
		  ap.append(" ,navigation=null");
		if(trackingevents != null)
		  ap.append(" ,trackingevents="+trackingevents.toString());
		else
		  ap.append(" ,trackingevents=null");
		ap.append(" }");
		return ap.toString();
	}
	
	
}
