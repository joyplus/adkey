package com.joyplus.ad.data;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.joyplus.ad.AdManager.AD;
import com.joyplus.ad.AdMode;
import com.joyplus.ad.HttpManager;
import com.joyplus.ad.application.AdBoot;
import com.joyplus.ad.config.Log;


public class AdBootRequest extends RequestAd<ADBOOT>{
	
	private AdRequest mAdRequest;
	
	public AdBootRequest(AdMode ad,AdBoot adboot){
    	if(ad == null)
    		throw new IllegalArgumentException("AdMode cannot be null or empty"); 
    	if(ad.GetAD() != AD.ADBOOT) 
    		throw new IllegalArgumentException("AdMode unavilable !!!");
    	if(ad.GetPublisherId().GetPublisherId() == null)
    		throw new IllegalArgumentException("PublisherId unavilable !!!");
    	mAdRequest = new AdRequest(ad,adboot);
    	mFileName  = "AdBootRequest";
    }
	
	public ADBOOT sendRequest() throws RequestException {
		if(mAdRequest == null)return null;
		return super.sendRequest(mAdRequest);
	}

	@Override
	ADBOOT parseTestString() throws RequestException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	ADBOOT parse(InputStream inputStream) throws RequestException {
		// TODO Auto-generated method stub
		if(inputStream == null)return null;
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser(); 
			XMLReader xr = sp.getXMLReader();
			AdBootResponseHandler AdBootHandler = new AdBootResponseHandler();
			xr.setContentHandler(AdBootHandler);
			InputSource src = new InputSource(inputStream);
			src.setEncoding(HttpManager.RESPONSE_ENCODING);
			xr.parse(src);
			return AdBootHandler.getADBOOT();
		} catch (Exception e) {
			throw new RequestException("Cannot parse Response:" 
					+ e.getMessage(), e);
		}
	}
	public String convertStreamToString(InputStream is) {   
		   BufferedReader reader = new BufferedReader(new InputStreamReader(is));   
		        StringBuilder sb = new StringBuilder();   
		        String line = null;   
		        try {   
		            while ((line = reader.readLine()) != null) {   
		                sb.append(line);   
		            }   
		        } catch (IOException e) {   
		            e.printStackTrace();   
		        } 	    
		        return sb.toString();   
		    }   
}
