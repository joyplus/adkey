package com.joyplus.admonitor.phone;



import java.util.Locale;
import com.joyplus.admonitor.Application.AdMonitorSDKException;
import com.joyplus.admonitor.Application.AdMonitorSDKManager;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;

/*Define by Jas@20140102
 * this manager was use to manager phone base manager*/
public class PhoneManager {
	
	  private Context      mContext;
	  private PhoneService mPhoneService;
	  public enum CONNECTION_TYPE{
		  UNKNOWN        ("UNKNOW"),
		  WIFI           ("WIFI"),
		  WIMAX          ("WIMAX"),
		  MOBILE_UNKNOWN ("MOBILE"),
		  MOBILE_1xRTT   ("1xRTT"),
		  MOBILE_CDMA    ("CDMA"),
		  MOBILE_EDGE    ("EDGE"),
		  MOBILE_EHRPD   ("EHRPD"),
		  MOBILE_EVDO_0  ("EVDO_0"),
		  MOBILE_EVDO_A  ("EVDO_A"),
		  MOBILE_EVDO_B  ("EVDO_B"),
		  MOBILE_GPRS    ("GPRS"),
		  MOBILE_HSDPA   ("HSDPA"),
		  MOBILE_HSPA    ("HSPA"),
		  MOBILE_HSPAP   ("HSPAP"),
		  MOBILE_HSUPA   ("HSUPA"),
		  MOBILE_IDEN    ("MOBILE_IDEN"),
		  MOBILE_LTE     ("LTE"),
		  MOBILE_UMTS    ("UMTS");
		  private String TYPE ;
		  CONNECTION_TYPE(String type){
			  TYPE = type;
		  } 
		  public String toString(){
			  return TYPE;
		  }
	  }

	  //
      private String mUniqueId1     = "";
  	  private String mUniqueId2     = "";
  	  private String mUserAgent1    = "";
  	  private String mUserAgent2    = "";
  	  private String androidVersion = Build.VERSION.RELEASE;
  	 
  	  
	  private static PhoneManager mPhoneManager;
	  public static void Init(Context context) throws AdMonitorSDKException{
		  if(AdMonitorSDKManager.IsInited())return;
		  if(context == null)throw new AdMonitorSDKException("PhoneManager context is null !!!!!");
		  mPhoneManager = new PhoneManager(context);
	  }
	  public static PhoneManager getInstance(){
		  return mPhoneManager;
	  }
      private PhoneManager(Context context) throws AdMonitorSDKException{
    	  mContext = context.getApplicationContext();
    	  mPhoneService = new PhoneService();
    	  InitResource();
      }
      private void InitResource() throws AdMonitorSDKException {
		  // TODO Auto-generated method stub
    	  mUserAgent1  = mPhoneService.getDefaultUserAgentString(mContext);
    	  mUserAgent2  = mPhoneService.buildUserAgent();
    	  mUniqueId1   = mPhoneService.getTelephonyDeviceId(mContext);
    	  mUniqueId2   = mPhoneService.getDeviceId(mContext);
    	  if ((mUniqueId2 == null) || (mUniqueId2.length() == 0))
  			    throw new AdMonitorSDKException("System Device Id cannot be null or empty");
    	  //mContext.getSystemService(name)
    	  DisplayMetrics ss = mContext.getResources().getDisplayMetrics();
    	  ss.toString();
	  }
      
      public String toString(){
    	  StringBuffer ap = new StringBuffer();
    	  ap.append("PhoneManager{ ")
    	    .append("mUserAgent1="+mUserAgent1)
    	    .append(" ,mUserAgent2="+mUserAgent2)
    	    .append(" ,mUniqueId1="+mUniqueId1)
    	    .append(" ,mUniqueId2="+mUniqueId2)
    	    .append(" }");
    	  return ap.toString();
      }
      
      /*Interface for Application*/
      public String GetDeviceId1(){
    	  if(mUniqueId1 != null)
    		  return mUniqueId1;
    	  return "";
      }
      
      public String GetDeviceId2(){
    	  if(mUniqueId2 != null)
    		  return mUniqueId2;
    	  return "";
      }
      
      public String GetUA1(){
    	  if(mUserAgent1 != null)
    		  return mUserAgent1;
    	  return "";
      }
      
      public String GetUA2(){
    	  if(mUserAgent2 != null)
    		  return mUserAgent2;
    	  return "";
      }
      
      public String GetAndroidVersion(){
    	  if(androidVersion != null)
    	      return androidVersion;
    	  return "4.2.1";
      }
      
      public String GetIp(){
    	  String mIp = mPhoneService.getLocalIpAddress();
    	  if(mIp != null)
    	      return mIp;
    	  return "";
      }
      
      public String GetMac(){
    	  String mMac = mPhoneService.getMacAddress(mContext);
    	  if(!(mMac == null || "".equals(mMac))){
    		  return mMac;
    	  }
    	  return "";
      }
      
      public CONNECTION_TYPE GetConnectType(){
    	  return mPhoneService.getConnectionType(mContext);
      }
      
      public String GetDeviceName(){
  		return new Build().MODEL;
      }
}
