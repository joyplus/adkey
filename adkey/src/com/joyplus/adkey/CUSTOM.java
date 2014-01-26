package com.joyplus.adkey;


public class CUSTOM {
    
	//Joyplus
	public final static int JOYPLUS = 0;
	
	//康佳
	public final static int KONKA   = 1;
	
	//润和
	public final static int RUNHE   = 2;
	
	//海尔
	public final static int HAIER   = 3;
	
	public int   GetCustom(){
		return AdKeyConfig.getInstance().getCUSTOM();
	}
	
}
