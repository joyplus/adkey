package com.joyplus.adkey.mon;

public interface monitor {
	
	public TYPE getType();
	
	public boolean IsAviable();
	
	public enum TYPE{
		VIDEO , APP
	}
}
