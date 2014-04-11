package com.joyplus.ad.data;

import java.io.Serializable;

public class CREATIVE2 implements Serializable{
	 public final static String tag     = "creative2";
	 public       String display  =""; 
	 public       String delivery =""; 
	 public       String type     =""; 
	 public       String bitrate  =""; 
	 public       String width    =""; 
	 public       String height   ="";
	 public       String URL      ="";
	
		public String toString() {
			StringBuffer ap = new StringBuffer();
			ap.append(" CREATIVE2={")
			  .append(" ,tag="+tag)
			  .append(" ,display="+display)
			  .append(" ,delivery="+delivery)
			  .append(" ,type="+type)
			  .append(" ,bitrate="+bitrate)
			  .append(" ,width="+width)
			  .append(" ,height="+height)
			  .append(" ,URL="+URL)
			  .append(" }");
			return ap.toString();
		}
}
