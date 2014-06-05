package com.joyplus.adkey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;


public class AdKeyHash {
	
	private final static int MAX_SIZE = 4*1024*1024;//4M
	
    public static String getFileHash(String file){
    	if(file ==null || "".equals(file.trim()))return "";
    	return getFileHash(new File(file));
    }
    public static String getFileHash(File file){
    	if(file == null || !file.exists() || file.isDirectory()||file.length()<=0)return "";
    	byte[] sda1 = getSha1(file);
    	return URLSafeBase64Encode(cat(file.length()<=MAX_SIZE?new byte[]{(byte)0x16}:new byte[]{(byte) 0x96},
    			                       file.length()<=MAX_SIZE?sda1:getSha1(sda1))).trim(); 
    }
    private static String URLSafeBase64Encode(byte[] input){
    	if(input==null || input.length<=0)return "";
	    return new String((new Base64()).encode(input, Base64.URL_SAFE));
	}
    private static byte[] cat(byte[] src,byte[] src1){
    	byte[] result = new byte[(src==null?0:src.length)+(src1==null?0:src1.length)];
    	if(src!=null&&src.length>0)System.arraycopy(src,0,result,0,src.length);
    	if(src1!=null&&src1.length>0)System.arraycopy(src1,0,result,(src==null?0:src.length), src1.length);
    	return result;
    }
    private static byte[] getSha1(byte[] sha1BlockBuf) {
    	if(sha1BlockBuf==null || sha1BlockBuf.length<=0)return null;
	    try {
	    	MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
	        digest.update(sha1BlockBuf);
	        return digest.digest();	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return null;
	}
    
    private static byte[] getSha1(File file){
    	FileInputStream fin = null;
    	byte[] result = null;
	    try {
	    	fin = new FileInputStream(file);
	    	int length = -1;
	    	int Count  = 0;
	    	byte[] buffer = new byte[512*1024];//512KB*8=4096KB=4M
	    	MessageDigest digest = null;
	    	while((length=fin.read(buffer))!=-1){
	    		if((Count++)%8==0){
	    			if(digest!=null){
	    				result = cat(result,digest.digest());
	    				digest.reset();
	    			}
	    			digest = java.security.MessageDigest.getInstance("SHA-1");
	    		}
	    		digest.update(buffer, 0, length);
	    	}
	    	return digest==null?null:cat(result,digest.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(fin!=null)fin.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		    }
		}
	    return null;
    }
}
