package com.joyplus.ad.data;

import java.security.MessageDigest;

public class MD5Util {
	
	public final static String MD5(String s) {      
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            return new String(md);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	
} 
