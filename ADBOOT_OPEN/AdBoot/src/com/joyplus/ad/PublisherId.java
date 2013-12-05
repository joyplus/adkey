package com.joyplus.ad;

public class PublisherId {
     
	private String mPublisherId = null;
	
	public PublisherId(){
		CheckId(mPublisherId);
	}
	public PublisherId(String id){
		CheckId(id);
		mPublisherId = id;
	}
		
	public String GetPublisherId(){
		CheckId(); //make sure it be useable.
		return mPublisherId;
	}
	
	public boolean CheckId(){
		return CheckId(mPublisherId);
	}
	
	private boolean CheckId(String id){
		if(id == null || "".equals(id))
			throw new IllegalArgumentException(
					"PublisherId Id cannot be null or empty");
		return true;
	}
	
}
