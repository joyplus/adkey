package com.joyplus.admonitor.data;

public class Type {

	public static ImpressionType toImpressionType(String s){
		 if(s == null || "".equals(s))return ImpressionType.Unknow;
		 if(ImpressionType.Joyplus.toString().equals(s)){
			 return ImpressionType.Joyplus;
		 }else if(ImpressionType.miaozhen.toString().equals(s)){
			 return ImpressionType.miaozhen;
		 }else {
			 return ImpressionType.Unknow;
		 }
	}
	
	
}
