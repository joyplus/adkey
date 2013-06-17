package com.joyplus.adkey.video;

import java.io.Serializable;

public class TrackerData implements Serializable {

	private static final long serialVersionUID = -8986291192914098710L;
	public static final int TYPE_START = 0;
	public static final int TYPE_COMPLETE = 1;
	public static final int TYPE_MIDPOINT = 2;
	public static final int TYPE_FIRSTQUARTILE = 3;
	public static final int TYPE_THIRDQUARTILE = 4;
	public static final int TYPE_TIME = 5;
	public static final int TYPE_PAUSE = 6;
	public static final int TYPE_UNPAUSE = 7;
	public static final int TYPE_MUTE = 8;
	public static final int TYPE_UNMUTE = 9;
	public static final int TYPE_SKIP = 10;
	public static final int TYPE_REPLAY = 11;

	int type;
	String url;
	int time;

	public void reset() {
		url = null;
		type = -1;
		time = -1;
	}
}
