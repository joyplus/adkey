package com.joyplus.adkey.Monitorer;

import java.io.Serializable;

public class TRACKINGURL implements Serializable{
     
	 public final static String tag = "trackingurl";
	 
	 public String  URL       = "";
	 public TYPE    Type      = TYPE.MIAOZHEN;//default.
	 public boolean Monitored = false;
	 public enum TYPE{
		 UNKNOW    ("unknow"),
		 MIAOZHEN  ("miaozhen"), //秒针
		 IRESEARCH ("iresearch"),//艾瑞
		 ADMASTER  ("admaster"), //ADMsater
		 NIELSEN   ("nielsen"),  //尼尔森
		 JOYPLUS   ("joyplus");
		 private String Type;
		 TYPE(String type){
			 Type = type;
		 }
		 public String toString(){
			 return Type;
		 }
		 public static TYPE toTYPE(String type){
			 if("miaozhen".equals(type)){
				 return TYPE.MIAOZHEN;
			 }else if("iresearch".equals(type)){
				 return TYPE.IRESEARCH;
			 }else if("admaster".equals(type)){
				 return TYPE.ADMASTER;
			 }else if("nielsen".equals(type)){
				 return TYPE.NIELSEN;
			 }else if("joyplus".equals(type)){
				 return TYPE.JOYPLUS;
			 }else{
				 return TYPE.UNKNOW;
			 }
		 }
	 }
	 
	 public String toString() {
		StringBuffer ap = new StringBuffer();
		ap.append(" TRACKINGURL={")
		  .append(" ,tag="+tag)
		  .append(" ,TYPE="+Type.toString())
		  .append(" ,URL="+URL)
		  .append(" }");
		return ap.toString();
	}
}
