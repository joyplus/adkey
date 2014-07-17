package com.joyplus.adkey.request;

import java.io.Serializable;
import android.text.TextUtils;


public class Click implements Serializable{
	 
	public enum TYPE{
		UNKNOW			("unknow"),
		OPENURL			("openurl"),
		OPENVIDEO		("openvideo"),
		OPENIMAGE		("openimage"),
		OPENAPP			("openapp"),
		SENDBROADCAST	("broadcast"),
		OPENSELF		("openself");
		private String type;
		TYPE(String Type ){
			type = Type; 
		}
		public String toString(){
			return type;
		}
		public static TYPE toTYPE(String type){
			if(type==null || TextUtils.isEmpty(type)){
				return DefaultTYPE;
			}else if(TYPE.OPENAPP.toString().equals(type)){
				return TYPE.OPENAPP;
			}else if(TYPE.OPENIMAGE.toString().equals(type)){
				return TYPE.OPENIMAGE;
			}else if(TYPE.OPENURL.toString().equals(type)){
				return TYPE.OPENURL;
			}else if(TYPE.SENDBROADCAST.toString().equals(type)){
				return TYPE.SENDBROADCAST;
			}else if(TYPE.OPENVIDEO.toString().equals(type)){
				return TYPE.OPENVIDEO;
			}//can't use TYPE.Valueof(typr)
			return DefaultTYPE;
		}
	}
	public static final TYPE DefaultTYPE = TYPE.OPENURL;
	///////////////////////////////////////////
	//Value
	public TYPE   mTYPE     = DefaultTYPE;
	public String mRes      = "";
	public String mClickURL = "";
	public Click(){
		mTYPE     = DefaultTYPE;
		mRes      = "";
		mClickURL = "";
	}
	public Click(Click cl){
	    if(cl!=null){
	    	mTYPE     =	cl.mTYPE;
	    	mRes      = cl.mRes;
	    	mClickURL = cl.mClickURL;
	    }
	}
	public Click CreateNew(){
		return new Click(this);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer ap = new StringBuffer();
		ap.append("click{")
		  .append("mTYPE="+(mTYPE==null?"":mTYPE.toString()))
		  .append(",mRes="+mRes)
		  .append(",mClickURL="+mClickURL)
		  .append("}");
		return ap.toString();
	}
}
