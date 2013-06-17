package com.joyplus.adkey.video;

import java.io.Serializable;

public class NavIconData implements Serializable {

	private static final long serialVersionUID = -6812948324043252699L;
	public final static int TYPE_INAPP = 0;
	public final static int TYPE_EXTERNAL = 1;

	String title;
	String iconUrl;
	int openType;
	String clickUrl;

	@Override
	public String toString() {
		return "NavIconData [title=" + title + ", iconUrl=" + iconUrl
				+ ", openType=" + openType + ", clickUrl=" + clickUrl + "]";
	}

}
