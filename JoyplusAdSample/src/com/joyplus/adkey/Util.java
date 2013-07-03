package com.joyplus.adkey;

import static com.joyplus.adkey.Const.PREFS_DEVICE_ID;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.joyplus.adkey.data.ScreenSaverInfo;
import com.joyplus.adkey.video.RichMediaAd;

public class Util {
	public enum AD_TYPE {
		FULL_SCREEN_VIDEO, SMALL_WINDOW_VIDEO, BANNER_IMAGE, SCREEN_SAVER
	};
	
	private static int sFadeInAnimationId = 0;
	private static int sFadeOutAnimationId = 0;
	private static int sSlideInRightAnimationId = 0;
	private static int sSlideOutRightAnimationId = 0;
	private static int sSlideInLeftAnimationId = 0;
	private static int sSlideOutLeftAnimationId = 0;
	private static int sSlideInTopAnimationId = 0;
	private static int sSlideOutTopAnimationId = 0;
	private static int sSlideInBottomAnimationId = 0;
	private static int sSlideOutBottomAnimationId = 0;
	public static String ExternalName = null;
	//if DEBUE_MODE= false,log'll not printf
	public static boolean DEBUE_MODE = true;
	//if CACHE_MODE is true,adkey support VideoAd's cacheDisplay
	public static boolean CACHE_MODE = true;
	//video PublisherId
	public static String PublisherId = null;
	public static String VideoFileDir = null;
	
	//pic download key-value
	public static Map<Integer, String> pic_downloaders = new HashMap<Integer, String>();
	public static Map<Integer, ScreenSaverInfo> pic_info = new HashMap<Integer, ScreenSaverInfo>();
	
	//pic for loop's number
	public static int PicNum = 0;
	//pic for download number
	public static int PicDownloadNum = 0;
	//miaozhen sdk is supported
	public static boolean MIAOZHENFLAG = true;
	public static boolean isNetworkAvailable(Context ctx) {
		int networkStatePermission = ctx
				.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);

		if (networkStatePermission == PackageManager.PERMISSION_GRANTED) {

			ConnectivityManager mConnectivity = (ConnectivityManager) ctx
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			// Skip if no connection, or background data disabled
			NetworkInfo info = mConnectivity.getActiveNetworkInfo();
			if (info == null) {
				return false;
			}
			// Only update if WiFi
			int netType = info.getType();
			// int netSubtype = info.getSubtype();
			if ((netType == ConnectivityManager.TYPE_WIFI)
					|| (netType == ConnectivityManager.TYPE_MOBILE)) {
				return info.isConnected();
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	public static String getConnectionType(Context context) {
		int networkStatePermission = context
				.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);

		if (networkStatePermission == PackageManager.PERMISSION_GRANTED) {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info == null) {
				return Const.CONNECTION_TYPE_UNKNOWN;
			}
			int netType = info.getType();
			int netSubtype = info.getSubtype();
			if (netType == ConnectivityManager.TYPE_WIFI) {
				return Const.CONNECTION_TYPE_WIFI;
			} else if (netType == 6) {
				return Const.CONNECTION_TYPE_WIMAX;
			} else if (netType == ConnectivityManager.TYPE_MOBILE) {
				switch (netSubtype) {
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					return Const.CONNECTION_TYPE_MOBILE_1xRTT;
				case TelephonyManager.NETWORK_TYPE_CDMA:
					return Const.CONNECTION_TYPE_MOBILE_CDMA;
				case TelephonyManager.NETWORK_TYPE_EDGE:
					return Const.CONNECTION_TYPE_MOBILE_EDGE;
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					return Const.CONNECTION_TYPE_MOBILE_EVDO_0;
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					return Const.CONNECTION_TYPE_MOBILE_EVDO_A;
				case TelephonyManager.NETWORK_TYPE_GPRS:
					return Const.CONNECTION_TYPE_MOBILE_GPRS;
				case TelephonyManager.NETWORK_TYPE_UMTS:
					return Const.CONNECTION_TYPE_MOBILE_UMTS;
				case 14:
					return Const.CONNECTION_TYPE_MOBILE_EHRPD;
				case 12:
					return Const.CONNECTION_TYPE_MOBILE_EVDO_B;
				case 8:
					return Const.CONNECTION_TYPE_MOBILE_HSDPA;
				case 10:
					return Const.CONNECTION_TYPE_MOBILE_HSPA;
				case 15:
					return Const.CONNECTION_TYPE_MOBILE_HSPAP;
				case 9:
					return Const.CONNECTION_TYPE_MOBILE_HSUPA;
				case 11:
					return Const.CONNECTION_TYPE_MOBILE_IDEN;
				case 13:
					return Const.CONNECTION_TYPE_MOBILE_LTE;
				default:
					return Const.CONNECTION_TYPE_MOBILE_UNKNOWN;
				}
			} else {
				return Const.CONNECTION_TYPE_UNKNOWN;
			}
		} else {
			return Const.CONNECTION_TYPE_UNKNOWN;
		}
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return null;
	}

	public static String getTelephonyDeviceId(Context context) {
		int telephonyPermission = context
				.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE);

		if (telephonyPermission == PackageManager.PERMISSION_GRANTED) {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			return tm.getDeviceId();
		}
		return "";
	}

	public static String getDeviceId(Context context) {
		String androidId = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		if ((androidId == null) || (androidId.equals("9774d56d682e549c"))
				|| (androidId.equals("0000000000000000"))) {
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(context);
			androidId = prefs.getString(PREFS_DEVICE_ID, null);
			if (androidId == null) {
				try {
					String uuid = UUID.randomUUID().toString();
					MessageDigest digest = MessageDigest.getInstance("MD5");
					digest.update(uuid.getBytes(), 0, uuid.length());
					androidId = String
							.format("%032X",
									new Object[] { new BigInteger(1, digest
											.digest()) }).substring(0, 16);
				} catch (Exception e) {
					androidId = "9774d56d682e549c";
				}
				prefs.edit().putString(PREFS_DEVICE_ID, androidId).commit();
			}
		}
		return androidId;
	}

	public static Location getLocation(Context context) {
		int isAccessFineLocation = context
				.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
		int isAccessCoarseLocation = context
				.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
		if ((isAccessFineLocation == PackageManager.PERMISSION_GRANTED)
				|| (isAccessCoarseLocation == PackageManager.PERMISSION_GRANTED)) {
			LocationManager locationManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
			if (locationManager != null) {
				if (isAccessFineLocation == PackageManager.PERMISSION_GRANTED) {
					boolean isGpsEnabled = locationManager
							.isProviderEnabled(LocationManager.GPS_PROVIDER);
					if (isGpsEnabled) {
						return locationManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					}
				}
				if (isAccessCoarseLocation == PackageManager.PERMISSION_GRANTED) {
					boolean isNetworkEnabled = locationManager
							.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

					if (isNetworkEnabled) {
						return locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					}
				}
			}
		}
		return null;
	}

	public static String getDefaultUserAgentString(Context context) {
//		String userAgent = System.getProperty("http.agent");
		try {
			Constructor<WebSettings> constructor = WebSettings.class
					.getDeclaredConstructor(Context.class, WebView.class);
			constructor.setAccessible(true);
			try {
				WebSettings settings = constructor.newInstance(context, null);
				return settings.getUserAgentString();
			} finally {
				constructor.setAccessible(false);
	}
		} catch (Exception e) {
			return new WebView(context).getSettings().getUserAgentString();
		}
	}

	public static String buildUserAgent() {
		String androidVersion = Build.VERSION.RELEASE;
		String model = Build.MODEL;
		String androidBuild = Build.ID;
		final Locale l = Locale.getDefault();
		final String language = l.getLanguage();
		String locale = "en";
		if (language != null) {
			locale = language.toLowerCase();
			final String country = l.getCountry();
			if (country != null) {
				locale += "-" + country.toLowerCase();
			}
		}

		String userAgent = String.format(Const.USER_AGENT_PATTERN,
				androidVersion, locale, model, androidBuild);
		return userAgent;
	}

	public static int getMemoryClass(Context context) {
		try {
			Method getMemoryClassMethod = ActivityManager.class
					.getMethod("getMemoryClass");
			ActivityManager ac = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			return (Integer) getMemoryClassMethod.invoke(ac, new Object[] {});
		} catch (Exception ex) {
			return 16;
		}
	}

	public static void initializeAnimations(Context ctx) {
		Resources r = ctx.getResources();
		sFadeInAnimationId = r.getIdentifier("fade_in", "anim",
				ctx.getPackageName());
		sFadeOutAnimationId = r.getIdentifier("fade_out", "anim",
				ctx.getPackageName());
		sSlideInBottomAnimationId = r.getIdentifier("slide_bottom_in",
				"anim", ctx.getPackageName());
		sSlideOutBottomAnimationId = r.getIdentifier("slide_bottom_out",
				"anim", ctx.getPackageName());
		sSlideInTopAnimationId = r.getIdentifier("slide_top_in", "anim",
				ctx.getPackageName());
		sSlideOutTopAnimationId = r.getIdentifier("slide_top_out",
				"anim", ctx.getPackageName());
		sSlideInLeftAnimationId = r.getIdentifier("slide_left_in",
				"anim", ctx.getPackageName());
		sSlideOutLeftAnimationId = r.getIdentifier("slide_left_out",
				"anim", ctx.getPackageName());
		sSlideInRightAnimationId = r.getIdentifier("slide_right_in",
				"anim", ctx.getPackageName());
		sSlideOutRightAnimationId = r.getIdentifier("slide_right_out",
				"anim", ctx.getPackageName());

	}

	public static AnimationSet getEnterAnimationSet(int animation) {
		AnimationSet set = new AnimationSet(false);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f,1.0f);
		alphaAnimation.setDuration(3000);
		set.addAnimation(alphaAnimation);
		TranslateAnimation translateAnimation;
		//    	TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
		//    	translateAnimation.setDuration(3000);
		switch (animation) {
		case RichMediaAd.ANIMATION_FADE_IN:
			return set;
		case RichMediaAd.ANIMATION_FLIP_IN:
			return set;
		case RichMediaAd.ANIMATION_SLIDE_IN_BOTTOM:
			translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF,0.0f);
			translateAnimation.setDuration(1000);
			set.addAnimation(translateAnimation);
			return set;
		case RichMediaAd.ANIMATION_SLIDE_IN_LEFT:
			translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f);
			translateAnimation.setDuration(1000);
			set.addAnimation(translateAnimation);
			return set;
		case RichMediaAd.ANIMATION_SLIDE_IN_RIGHT:
			translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f);
			translateAnimation.setDuration(1000);
			set.addAnimation(translateAnimation);
			return set;
		case RichMediaAd.ANIMATION_SLIDE_IN_TOP:
			translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,-1.0f, Animation.RELATIVE_TO_SELF,0.0f);
			translateAnimation.setDuration(1000);
			set.addAnimation(translateAnimation);
			return set;
		default:
			return null;
		}
	}

	public static AnimationSet getExitAnimationSet(int animation) {
		AnimationSet set = new AnimationSet(false);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
		alphaAnimation.setDuration(3000);
		set.addAnimation(alphaAnimation);
		TranslateAnimation translateAnimation;
		//    	TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
		//    	translateAnimation.setDuration(3000);
		switch (animation) {
		case RichMediaAd.ANIMATION_FADE_IN:
			return set;
		case RichMediaAd.ANIMATION_FLIP_IN:
			return set;
		case RichMediaAd.ANIMATION_SLIDE_IN_BOTTOM:
			translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,1.0f);
			translateAnimation.setDuration(1000);
			set.addAnimation(translateAnimation);
			return set;
		case RichMediaAd.ANIMATION_SLIDE_IN_LEFT:
			translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,-1.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f);
			translateAnimation.setDuration(1000);
			set.addAnimation(translateAnimation);
			return set;
		case RichMediaAd.ANIMATION_SLIDE_IN_RIGHT:
			translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f);
			translateAnimation.setDuration(1000);
			set.addAnimation(translateAnimation);
			return set;
		case RichMediaAd.ANIMATION_SLIDE_IN_TOP:
			translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,-1.0f);
			translateAnimation.setDuration(1000);
			set.addAnimation(translateAnimation);
			return set;
		default:
			return null;
		}
	}

	public static int getEnterAnimation(int animation) {
		switch (animation) {
		case RichMediaAd.ANIMATION_FADE_IN:
			return sFadeInAnimationId;
		case RichMediaAd.ANIMATION_FLIP_IN:
			return sFadeInAnimationId;
		case RichMediaAd.ANIMATION_SLIDE_IN_BOTTOM:
			return sSlideInBottomAnimationId;
		case RichMediaAd.ANIMATION_SLIDE_IN_LEFT:
			return sSlideInLeftAnimationId;
		case RichMediaAd.ANIMATION_SLIDE_IN_RIGHT:
			return sSlideInRightAnimationId;
		case RichMediaAd.ANIMATION_SLIDE_IN_TOP:
			return sSlideInTopAnimationId;
		default:
			return 0;
		}

	}

	public static int getExitAnimation(int animation) {
		switch (animation) {
		case RichMediaAd.ANIMATION_FADE_IN:
			return sFadeOutAnimationId;
		case RichMediaAd.ANIMATION_FLIP_IN:
			return sFadeOutAnimationId;
		case RichMediaAd.ANIMATION_SLIDE_IN_BOTTOM:
			return sSlideOutBottomAnimationId;
		case RichMediaAd.ANIMATION_SLIDE_IN_LEFT:
			return sSlideOutLeftAnimationId;
		case RichMediaAd.ANIMATION_SLIDE_IN_RIGHT:
			return sSlideOutRightAnimationId;
		case RichMediaAd.ANIMATION_SLIDE_IN_TOP:
			return sSlideOutTopAnimationId;
		default:
			return 0;
		}
	}
	
	/*
	 * get httpUrl‘s Ex name
	 */
	public static String getExtensionName(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('.');   
            if ((dot >-1) && (dot < (filename.length() - 1))) {   
                return filename.substring(dot + 1);   
            }   
        }   
        return filename;   
    }
	
	/*
	 * By application's package name Get Download path
	 */
	public static void GetPackage(Context context){
		if(context!=null)
			VideoFileDir = context.getPackageName()+"/"+PublisherId+"/";
	}
	/*
	 * get device name 
	 */
	@SuppressWarnings("static-access")
	public static String GetDeviceName(){
		return new Build().MODEL;
	}
	
	/*
	 * save a class by Serialie
	 */
	public void saveClassToSeriale(java.io.ObjectOutputStream out)throws IOException {
//		out.
	}
	/*
	 * get a class from SerialieData
	 */
	public void getClassFromSeriale(){
		
	}
	
}
