package com.joyplus.ad.config;

import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.content.Context;
import android.content.res.AssetManager;

public class AdBootAssertExternalConfig {

	private static boolean         Useable   = false;
	private static AdBootAssertExternalConfig mInstance;
	public  static AdBootAssertExternalConfig getInstance(Context context){
		if(mInstance == null){
			mInstance = new AdBootAssertExternalConfig(context);
		}
		return mInstance;
	}
	private  static boolean AdBootDebugEnable  = false;
	private  static String  BaseURL            = null;
	private  static String  mAdBootBasePath    = null;
	private  static int     MAXSIZE            = -1;//default is 5
	private  static boolean COPYALWAYS         = true;
	private  static boolean REQUESTALWAYS      = true;
	
	public   boolean getUseable(){
		return Useable;
	}
	public   boolean getCOPYALWAYS(boolean defineValue){
		return COPYALWAYS;
	}
	public   boolean getREQUESTALWAYS(boolean defineValue){
		return REQUESTALWAYS;
	}
	public   boolean getAdBootDebugEnable(boolean defineValue){
		return AdBootDebugEnable;
	}
	public   String  getBaseURL(String defineValue){
		if(BaseURL==null||"".equals(BaseURL))return defineValue;
		return BaseURL;
	}
	public   String  getAdBootBasePath(String defineValue){
		if(mAdBootBasePath==null||"".equals(mAdBootBasePath))return defineValue;
		return mAdBootBasePath;
	}
	public   int     getMAXSIZE(int defineValue){
		if(MAXSIZE <0)return defineValue;
		return MAXSIZE;
	}
	private AdBootAssertExternalConfig(Context context){
		AssetManager manager     = context.getAssets();
		InputStream       in     = null;
		try{
			in                = manager.open("adbootconfig.xml");
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser  pr = factory.newPullParser();
			pr.setInput(in, "utf-8");
			Useable           = ParserXML(pr);
			return;
		}catch(IOException e){
			android.util.Log.i("AdBootSDK","Assert External Config IOException !!!!");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			android.util.Log.i("AdBootSDK","Assert External Config XmlPullParserException !!!!");
			e.printStackTrace();
		}finally{
			try {
				if(in != null)in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Useable = false;
	}
	private boolean ParserXML(XmlPullParser  parser){
		 try {
	        beginDocument(parser, "adbootconfig");
	        while(true){
                nextElement(parser);
                String tag   = parser.getName();
                if (tag == null)break;
                String name  = parser.getAttributeName(0);
                String value = parser.getAttributeValue(0);
                String text  = null;
                if (parser.next() == XmlPullParser.TEXT) {
                    text = parser.getText();
                }
                if ("name".equalsIgnoreCase(name)) {
                    if ("bool".equals(tag)) {
                        // bool config tags go here
                        if ("AdBootDebugEnable".equalsIgnoreCase(value)) {
                        	AdBootDebugEnable = "true".equalsIgnoreCase(text);
                        }else if("COPYALWAYS".equalsIgnoreCase(value)){
                        	COPYALWAYS = "true".equalsIgnoreCase(text);
                        }else if("REQUESTALWAYS".equalsIgnoreCase(value)){
                        	REQUESTALWAYS = "true".equalsIgnoreCase(text);
                        }
                    } else if ("int".equals(tag)) {
                        // int config tags go here
                        if ("MAXSIZE".equalsIgnoreCase(value)) {
                            MAXSIZE = Integer.parseInt(text);
                        }
                    } else if ("string".equals(tag)) {
                        // string config tags go here
                        if ("BaseURL".equalsIgnoreCase(value)) {
                           BaseURL = text;
                        }else if("mAdBootBasePath".equalsIgnoreCase(value)) {
                           mAdBootBasePath = text;
                        }
                    }
                }
	        }
	        return true;
        } catch (XmlPullParserException e) {
        	android.util.Log.i("AdBootSDK", "ParserXML caught ", e);
        } catch (NumberFormatException e) {
        	android.util.Log.i("AdBootSDK", "ParserXML caught ", e);
        } catch (IOException e) {
        	android.util.Log.i("AdBootSDK", "ParserXML caught ", e);
        } 
        return false;    
	}
	
	private static final void beginDocument(XmlPullParser parser, String firstElementName) throws XmlPullParserException, IOException{
        int type;
        while ((type=parser.next()) != parser.START_TAG&& type != parser.END_DOCUMENT) {
            ;
        }
        if (type != parser.START_TAG) {
            throw new XmlPullParserException("No start tag found");
        }
        if (!parser.getName().equals(firstElementName)) {
            throw new XmlPullParserException("Unexpected start tag: found " + parser.getName() +
                    ", expected " + firstElementName);
        }
    }
	private static final void nextElement(XmlPullParser parser) throws XmlPullParserException, IOException{
	    int type;
	    while ((type=parser.next()) != parser.START_TAG&& type != parser.END_DOCUMENT) {
	         ;
	    }
	}
}
