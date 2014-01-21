package com.joyplus.admonitor.data;

public enum ImpressionType {
     
	 Unknow   ("unknow"),
	 Joyplus  ("Joyplus"),
	 miaozhen ("miaozhen");
	
	 private String Type;
	 ImpressionType(String type){
		 Type = type;
	 }
	 
	 
	 @Override
	 public String toString() {
		// TODO Auto-generated method stub
		return Type;
	 }
	 
}
