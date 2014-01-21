package com.joyplus.admonitor.Application;

public class CustomInfo {

	
	private String mMAC = "";//mac
	private String mDEVICEMOVEMENT = "";//dm
	
	
	public void SetMAC(String mac){
		mMAC  =  mac;
	}
	public String GetMAC(){
		if(mMAC == null)return "";
		return mMAC;
	}
	
	
	
	public void SetDEVICEMOVEMENT(String dm){
		mDEVICEMOVEMENT = dm;
	}
	public String GetDEVICEMOVEMENT(){
		if(mDEVICEMOVEMENT == null)return "";
		return mDEVICEMOVEMENT;
	}
	
	
	
}
