package com.joyplus.admonitor.Application;

public class AdMonitorSDKException extends Exception{
	
	//serial Version UID for Android System
    private static final long serialVersionUID = 816136015813043499L;

    public AdMonitorSDKException() {
        super();
    }

    public AdMonitorSDKException(String msg) {
        super(msg);
    }

    public AdMonitorSDKException(Exception cause) {
        super(cause);
    }

}
