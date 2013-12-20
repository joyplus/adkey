package com.joyplus.ad.data;

import java.io.Serializable;



public class ADBOOT implements Serializable{
	 private static final long serialVersionUID          = 6443573739926220979L;
	 
	 public final  static String tag         = "ad";
	 public final  static String type        = "open";
	 public               String animation   = "";
	 public               ABDOOTVIDEO  video = null;
	 //define for respone S 
	 public               CODE   code        = null;
	 public String toString() {
		 StringBuffer ap = new StringBuffer();
		 ap.append("ADBOOT { ")
		   .append(" tag="+tag) 
		   .append(" ,type="+type)
		   .append(" ,animation="+animation);
		 if(code != null){
			 ap.append(" ,code="+video.toString());
		 }else{
			 ap.append(" ,code == null");
		 } 
		 if(video !=null ){
			 ap.append(" ,video="+video.toString());
		 }else{
			 ap.append(" ,video==null");
		 }
		 ap.append(" }");
		 return ap.toString();
	 }
}
