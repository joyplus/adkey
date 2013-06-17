package com.joyplus.adkey.video;

import java.io.Serializable;
import java.util.Vector;

public class InterstitialData implements Serializable {

	private static final long serialVersionUID         = 8764230341946345345L;
	public static final int   INTERSTITIAL_URL         = 0;
	public static final int   INTERSTITIAL_MARKUP      = 1;

	public static final int   INTERSTITIAL_TITLE_FIXED = 0;
	public static final int   INTERSTITIAL_TITLE_HTML  = 1;
	public static final int   INTERSTITIAL_TITLE_HIDDEN  = 2;
	int                       autoclose;
	int                       orientation;
	int                       interstitialType;
	String                    interstitialUrl;
	String                    interstitialMarkup;
	boolean                   showSkipButton;
	int                       showSkipButtonAfter;
	String                    skipButtonImage;

	boolean                   showNavigationBars;
	boolean                   allowTapNavigationBars;
	boolean                   showTopNavigationBar;
	String                    topNavigationBarBackground;
	int                       topNavigationBarTitleType;
	String                    topNavigationBarTitle;
	boolean                   showBottomNavigationBar;
	String                    bottomNavigationBarBackground;
	boolean                   showBackButton;
	boolean                   showForwardButton;
	boolean                   showReloadButton;
	boolean                   showExternalButton;
	boolean                   showTimer;
	String                    backButtonImage;
	String                    forwardButtonImage;
	String                    reloadButtonImage;
	String                    externalButtonImage;
	Vector<NavIconData>       icons                    = new Vector<NavIconData>();

	@Override
	public String toString() {
		return "InterstitialData \n[\nautoclose=" + autoclose
				+ ",\norientation=" + orientation + ",\ninterstitialType="
				+ interstitialType + ",\ninterstitialUrl=" + interstitialUrl
				+ ",\ninterstitialMarkup=" + interstitialMarkup
				+ ",\nshowSkipButton=" + showSkipButton
				+ ",\nshowSkipButtonAfter=" + showSkipButtonAfter
				+ ",\nskipButtonImage=" + skipButtonImage
				+ ",\nshowNavigationBars=" + showNavigationBars
				+ ",\nallowTapNavigationBars=" + allowTapNavigationBars
				+ ",\nshowTopNavigationBar=" + showTopNavigationBar
				+ ",\ntopNavigationBarBackground=" + topNavigationBarBackground
				+ ",\ntopNavigationBarTitleType=" + topNavigationBarTitleType
				+ ",\ntopNavigationBarTitle=" + topNavigationBarTitle
				+ ",\nshowBottomNavigationBar=" + showBottomNavigationBar
				+ ",\nbottomNavigationBarBackground="
				+ bottomNavigationBarBackground + ",\nshowBackButton="
				+ showBackButton + ",\nbackButtonImage=" + backButtonImage
				+ ",\nshowForwardButton=" + showForwardButton
				+ ",\nforwardButtonImage=" + forwardButtonImage
				+ ",\nshowReloadButton=" + showReloadButton
				+ ",\nreloadButtonImage=" + reloadButtonImage
				+ ",\nshowExternalButton=" + showExternalButton
				+ ",\nexternalButtonImage=" + externalButtonImage
				+ ",\nshowTimer=" + showTimer + ",\nicons=" + icons + "\n]";
	}

}
