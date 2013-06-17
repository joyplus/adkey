package com.joyplus.adkey.video;

import java.io.CharArrayWriter;
import java.util.HashMap;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.pm.ActivityInfo;

import com.joyplus.adkey.Const;

public class ResponseHandler extends DefaultHandler {

	private RichMediaAd richMediaAd = null;

	HashMap<String, Long> videoList = null;

	private CharArrayWriter contents = new CharArrayWriter();
	private TrackerData currentTracker = new TrackerData();
	private long currentExpiration;

	private boolean insideMarkup = false;
	private boolean insideVideo = false;
	private boolean insideInterstitial = false;
	private boolean insideVideoList = false;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		contents.write(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equals("creative")) {
			if ((getRichMediaAd() == null) || (getRichMediaAd().getVideo() == null)) {
				throw new SAXException("Creative tag found outside video node");
			}
			VideoData video = getRichMediaAd().getVideo();
			video.videoUrl = contents.toString().trim();
		} else if (localName.equals("duration")) {
			if ((getRichMediaAd() == null) || (getRichMediaAd().getVideo() == null)) {
				throw new SAXException("Duration tag found outside video node");
			}
			VideoData video = getRichMediaAd().getVideo();
			video.duration = getInteger(contents.toString().trim());
		} else if (localName.equals("tracker")) {
			if ((getRichMediaAd() == null) || (getRichMediaAd().getVideo() == null)) {
				throw new SAXException("Tracker tag found outside video node");
			}
			VideoData video = getRichMediaAd().getVideo();
			currentTracker.url = contents.toString().trim();
			Vector<String> trackers = null;
			switch (currentTracker.type) {
			case TrackerData.TYPE_MIDPOINT:
			case TrackerData.TYPE_FIRSTQUARTILE:
			case TrackerData.TYPE_THIRDQUARTILE:
			case TrackerData.TYPE_TIME:
				trackers = video.timeTrackingEvents
				.get(currentTracker.time);
				if (trackers == null) {
					trackers = new Vector<String>();
					video.timeTrackingEvents.put(currentTracker.time,
							trackers);
				}
				break;
			case TrackerData.TYPE_START:
				trackers = video.getStartEvents();
				break;
			case TrackerData.TYPE_COMPLETE:
				trackers = video.getCompleteEvents();
				break;
			case TrackerData.TYPE_PAUSE:
				trackers = video.pauseEvents;
				break;
			case TrackerData.TYPE_UNPAUSE:
				trackers = video.unpauseEvents;
				break;
			case TrackerData.TYPE_MUTE:
				trackers = video.muteEvents;
				break;
			case TrackerData.TYPE_UNMUTE:
				trackers = video.unmuteEvents;
				break;
			case TrackerData.TYPE_REPLAY:
				trackers = video.replayEvents;
				break;
			case TrackerData.TYPE_SKIP:
				trackers = video.skipEvents;
				break;
			}
			if (trackers != null) {
				trackers.add(currentTracker.url);
			}
		} else if (localName.equals("htmloverlay")) {
			if ((getRichMediaAd() == null) || (getRichMediaAd().getVideo() == null)) {
				throw new SAXException(
						"htmloverlay tag found outside video node");
			}
			VideoData video = getRichMediaAd().getVideo();
			video.htmlOverlayMarkup = contents.toString().trim();
			insideMarkup = false;
		} else if (localName.equals("video")) {
			if (insideVideoList) {
				String url = contents.toString().trim();
				videoList.put(url, currentExpiration);
			}
			insideVideo = false;
		} else if (localName.equals("interstitial")) {
			insideInterstitial = false;
		} else if (localName.equals("markup")) {
			if ((getRichMediaAd() == null)
					|| (getRichMediaAd().getInterstitial() == null)) {
				throw new SAXException(
						"markup tag found outside interstitial node");
			}
			insideMarkup = false;
			InterstitialData inter = getRichMediaAd().getInterstitial();
			inter.interstitialMarkup = contents.toString().trim();
		} else if (localName.equals("error")) {
			getRichMediaAd().setType(Const.NO_AD);
		}
	}

	@Override
	public void startDocument() throws SAXException {
		setRichMediaAd(new RichMediaAd());
		insideVideoList = false;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (!insideMarkup) {
			contents.reset();
			if (localName.equals("activevideolist")) {
				videoList = new HashMap<String, Long>();
				insideVideoList = true;
			} else if (localName.equals("ad")) {
				String type = attributes.getValue("type");
				if ("video-to-interstitial".equalsIgnoreCase(type)) {
					getRichMediaAd().setType(Const.VIDEO_TO_INTERSTITIAL);
				} else if ("interstitial-to-video".equalsIgnoreCase(type)) {
					getRichMediaAd().setType(Const.INTERSTITIAL_TO_VIDEO);
				} else if ("video".equalsIgnoreCase(type)) {
					getRichMediaAd().setType(Const.VIDEO);
				} else if ("interstitial".equalsIgnoreCase(type)) {
					getRichMediaAd().setType(Const.INTERSTITIAL);
				} else if ("noAd".equalsIgnoreCase(type)) {
					getRichMediaAd().setType(Const.NO_AD);
				} else {
					throw new SAXException("Unknown response type " + type);
				}
				String animation = attributes.getValue("animation");
				if ("fade-in".equalsIgnoreCase(animation)) {
					getRichMediaAd().setAnimation(RichMediaAd.ANIMATION_FADE_IN);
				} else if ("slide-in-top".equalsIgnoreCase(animation)) {
					getRichMediaAd()
					.setAnimation(RichMediaAd.ANIMATION_SLIDE_IN_TOP);
				} else if ("slide-in-bottom".equalsIgnoreCase(animation)) {
					getRichMediaAd()
					.setAnimation(RichMediaAd.ANIMATION_SLIDE_IN_BOTTOM);
				} else if ("slide-in-left".equalsIgnoreCase(animation)) {
					getRichMediaAd()
					.setAnimation(RichMediaAd.ANIMATION_SLIDE_IN_LEFT);
				} else if ("slide-in-right".equalsIgnoreCase(animation)) {
					getRichMediaAd()
					.setAnimation(RichMediaAd.ANIMATION_SLIDE_IN_RIGHT);
				} else if ("flip-in".equalsIgnoreCase(animation)) {
					getRichMediaAd().setAnimation(RichMediaAd.ANIMATION_FLIP_IN);
				} else {
					getRichMediaAd().setAnimation(RichMediaAd.ANIMATION_NONE);
				}
			} else if (localName.equals("video")) {
				if (insideVideoList) {
					currentExpiration = getLong(attributes
							.getValue("expiration")) * 1000;
				} else {
					insideVideo = true;
					VideoData video = new VideoData();
					String orientation = attributes.getValue("orientation");
					if ("landscape".equalsIgnoreCase(orientation)) {
						video.orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
					} else if ("portrait".equalsIgnoreCase(orientation)) {
						video.orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
					} else {
						video.orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
					}
					if (getRichMediaAd() != null) {
						if ((getRichMediaAd().getType() == Const.INTERSTITIAL)
								&& (getRichMediaAd().getType() != Const.INTERSTITIAL_TO_VIDEO)
								&& (getRichMediaAd().getType() != Const.VIDEO_TO_INTERSTITIAL)) {
							throw new SAXException(
									"Found Video tag in an interstitial ad:"
											+ getRichMediaAd().getType());
						}
						getRichMediaAd().setVideo(video);
					} else {
						throw new SAXException(
								"Video tag found outside document root");
					}
				}
			} else if (localName.equals("interstitial")) {
				insideInterstitial = true;
				InterstitialData inter = new InterstitialData();
				inter.autoclose = getInteger(attributes.getValue("autoclose"));
				String type = attributes.getValue("type");
				if ("url".equalsIgnoreCase(type)) {
					inter.interstitialType = InterstitialData.INTERSTITIAL_URL;
					String url = attributes.getValue("url");
					if ((url == null) || (url.length() == 0)) {
						throw new SAXException(
								"Empty url for interstitial type " + type);
					}
					inter.interstitialUrl = url;
				} else if ("markup".equalsIgnoreCase(type)) {
					inter.interstitialType = InterstitialData.INTERSTITIAL_MARKUP;
					insideMarkup = true;
				} else {
					inter.interstitialType = InterstitialData.INTERSTITIAL_URL;
					String url = attributes.getValue("url");
					if ((url == null) || (url.length() == 0)) {
						throw new SAXException(
								"Empty url for interstitial type " + type);
					}
					inter.interstitialUrl = url;

				}
				String orientation = attributes.getValue("orientation");
				if ("landscape".equalsIgnoreCase(orientation)) {
					inter.orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				} else if ("portrait".equalsIgnoreCase(orientation)) {
					inter.orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				} else {
					inter.orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				}
				if (getRichMediaAd() != null) {
					if ((getRichMediaAd().getType() == Const.VIDEO)
							&& (getRichMediaAd().getType() != Const.INTERSTITIAL_TO_VIDEO)
							&& (getRichMediaAd().getType() != Const.VIDEO_TO_INTERSTITIAL)) {
						throw new SAXException(
								"Found Interstitial tag in a video ad:"
										+ getRichMediaAd().getType());
					}
					getRichMediaAd().setInterstitial(inter);
				} else {
					throw new SAXException(
							"Interstitial tag found outside document root");
				}
			} else if (localName.equals("creative")) {
				if ((getRichMediaAd() == null) || (getRichMediaAd().getVideo() == null)) {
					throw new SAXException(
							"Creative tag found outside video node");
				}
				VideoData video = getRichMediaAd().getVideo();
				String delivery = attributes.getValue("delivery");
				if ("progressive".equalsIgnoreCase(delivery)) {
					video.delivery = VideoData.DELIVERY_PROGRESSIVE;
				} else if ("streaming".equalsIgnoreCase(delivery)) {
					video.delivery = VideoData.DELIVERY_STREAMING;
				} else {
					video.delivery = VideoData.DELIVERY_STREAMING;
				}
				String type = attributes.getValue("type");
				if ((type == null) || (type.length() == 0)) {
					type = "application/mp4";
				}
				String display = attributes.getValue("display");
				if ("fullscreen".equalsIgnoreCase(display)) {
					video.display = VideoData.DISPLAY_FULLSCREEN;
				} else if ("normal".equalsIgnoreCase(display)) {

					video.display = VideoData.DISPLAY_FULLSCREEN;
				} else {
					video.display = VideoData.DISPLAY_FULLSCREEN;
				}
				video.type = type;
				video.width = getInteger(attributes.getValue("width"));
				video.height = getInteger(attributes.getValue("height"));
				video.bitrate = getInteger(attributes.getValue("bitrate"));
			} else if (localName.equals("skipbutton")) {
				if (insideVideo) {
					if ((getRichMediaAd() == null)
							|| (getRichMediaAd().getVideo() == null)) {
						throw new SAXException(
								"skipbutton tag found inside wrong video node");
					}
					VideoData video = getRichMediaAd().getVideo();
					video.showSkipButton = getBoolean(attributes
							.getValue("show"));
					video.showSkipButtonAfter = getInteger(attributes
							.getValue("showafter"));
					video.skipButtonImage = attributes.getValue("graphic");

				} else if (insideInterstitial) {
					if ((getRichMediaAd() == null)
							|| (getRichMediaAd().getInterstitial() == null)) {
						throw new SAXException(
								"skipbutton tag found inside wrong interstitial node");
					}
					InterstitialData inter = getRichMediaAd().getInterstitial();
					inter.showSkipButton = getBoolean(attributes
							.getValue("show"));
					inter.showSkipButtonAfter = getInteger(attributes
							.getValue("showafter"));
					inter.skipButtonImage = attributes.getValue("graphic");
				}
			} else if (localName.equals("navigation")) {
				if (insideVideo) {
					if ((getRichMediaAd() == null)
							|| (getRichMediaAd().getVideo() == null)) {
						throw new SAXException(
								"navigation tag found inside wrong video node");
					}
					VideoData video = getRichMediaAd().getVideo();
					video.showNavigationBars = getBoolean(attributes
							.getValue("show"));
					video.allowTapNavigationBars = getBoolean(attributes
							.getValue("allowtap"));

				} else if (insideInterstitial) {
					if ((getRichMediaAd() == null)
							|| (getRichMediaAd().getInterstitial() == null)) {
						throw new SAXException(
								"navigation tag found inside wrong interstitial node");
					}
					InterstitialData inter = getRichMediaAd().getInterstitial();
					inter.showNavigationBars = getBoolean(attributes
							.getValue("show"));
					inter.allowTapNavigationBars = getBoolean(attributes
							.getValue("allowtap"));
				}
			} else if (localName.equals("topbar")) {
				if (insideVideo) {
					if ((getRichMediaAd() == null)
							|| (getRichMediaAd().getVideo() == null)) {
						throw new SAXException(
								"topbar tag found inside wrong video node");
					}
					VideoData video = getRichMediaAd().getVideo();
					video.showTopNavigationBar = getBoolean(attributes
							.getValue("show"));
					video.topNavigationBarBackground = attributes
							.getValue("custombackgroundurl");
				} else if (insideInterstitial) {
					if ((getRichMediaAd() == null)
							|| (getRichMediaAd().getInterstitial() == null)) {
						throw new SAXException(
								"topbar tag found inside wrong interstitial node");
					}
					InterstitialData inter = getRichMediaAd().getInterstitial();
					inter.showTopNavigationBar = getBoolean(attributes
							.getValue("show"));
					inter.topNavigationBarBackground = attributes
							.getValue("custombackgroundurl");
					String titleType = attributes.getValue("title");
					if ("fixed".equalsIgnoreCase(titleType)) {
						inter.topNavigationBarTitleType = InterstitialData.INTERSTITIAL_TITLE_FIXED;
						inter.topNavigationBarTitle = attributes
								.getValue("titlecontent");
					} else if("variable".equalsIgnoreCase(titleType)) {
						inter.topNavigationBarTitleType = InterstitialData.INTERSTITIAL_TITLE_HTML;
					}
					else{
						inter.topNavigationBarTitleType = InterstitialData.INTERSTITIAL_TITLE_HIDDEN;
					}
				}
			} else if (localName.equals("bottombar")) {
				if (insideVideo) {
					if ((getRichMediaAd() == null)
							|| (getRichMediaAd().getVideo() == null)) {
						throw new SAXException(
								"bottombar tag found inside wrong video node");
					}
					VideoData video = getRichMediaAd().getVideo();
					video.showBottomNavigationBar = getBoolean(attributes
							.getValue("show"));
					video.bottomNavigationBarBackground = attributes
							.getValue("custombackgroundurl");
					video.showPauseButton = getBoolean(attributes
							.getValue("pausebutton"));
					video.showReplayButton = getBoolean(attributes
							.getValue("replaybutton"));
					video.showTimer = getBoolean(attributes.getValue("timer"));
					video.pauseButtonImage = attributes
							.getValue("pausebuttonurl");
					video.playButtonImage = attributes
							.getValue("playbuttonurl");
					video.replayButtonImage = attributes
							.getValue("replaybuttonurl");
				} else if (insideInterstitial) {
					if ((getRichMediaAd() == null)
							|| (getRichMediaAd().getInterstitial() == null)) {
						throw new SAXException(
								"bottombar tag found inside wrong interstitial node");
					}
					InterstitialData inter = getRichMediaAd().getInterstitial();
					inter.showBottomNavigationBar = getBoolean(attributes
							.getValue("show"));
					inter.bottomNavigationBarBackground = attributes
							.getValue("custombackgroundurl");
					inter.showBackButton = getBoolean(attributes
							.getValue("backbutton"));
					inter.showForwardButton = getBoolean(attributes
							.getValue("forwardbutton"));
					inter.showReloadButton = getBoolean(attributes
							.getValue("reloadbutton"));
					inter.showExternalButton = getBoolean(attributes
							.getValue("externalbutton"));
					inter.showTimer = getBoolean(attributes.getValue("timer"));
					inter.backButtonImage = attributes
							.getValue("backbuttonurl");
					inter.forwardButtonImage = attributes
							.getValue("forwardbuttonurl");
					inter.reloadButtonImage = attributes
							.getValue("reloadbuttonurl");
					inter.externalButtonImage = attributes
							.getValue("externalbuttonurl");
				}
			} else if (localName.equals("navicon")) {
				if (insideVideo) {
					if ((getRichMediaAd() == null)
							|| (getRichMediaAd().getVideo() == null)) {
						throw new SAXException(
								"navicon tag found inside wrong video node");
					}
					VideoData video = getRichMediaAd().getVideo();
					NavIconData icon = new NavIconData();
					icon.title = attributes.getValue("title");
					icon.clickUrl = attributes.getValue("clickurl");
					icon.iconUrl = attributes.getValue("iconurl");
					String type = attributes.getValue("opentype");
					if ("inapp".equalsIgnoreCase(type)) {
						icon.openType = NavIconData.TYPE_INAPP;
					} else {
						icon.openType = NavIconData.TYPE_EXTERNAL;
					}
					video.icons.add(icon);
				} else if (insideInterstitial) {
					if ((getRichMediaAd() == null)
							|| (getRichMediaAd().getInterstitial() == null)) {
						throw new SAXException(
								"navicon tag found inside wrong interstitial node");
					}
					InterstitialData inter = getRichMediaAd().getInterstitial();
					NavIconData icon = new NavIconData();
					icon.title = attributes.getValue("title");
					icon.clickUrl = attributes.getValue("clickurl");
					icon.iconUrl = attributes.getValue("iconurl");
					String type = attributes.getValue("opentype");
					if ("inapp".equalsIgnoreCase(type)) {
						icon.openType = NavIconData.TYPE_INAPP;
					} else {
						icon.openType = NavIconData.TYPE_EXTERNAL;
					}
					inter.icons.add(icon);
				}
			} else if (localName.equals("tracker")) {
				if (insideVideo) {
					if ((getRichMediaAd() == null)
							|| (getRichMediaAd().getVideo() == null)) {
						throw new SAXException(
								"tracker tag found inside wrong video node");
					}
					VideoData video = getRichMediaAd().getVideo();
					currentTracker.reset();
					String type = attributes.getValue("type");
					if ("start".equalsIgnoreCase(type)) {
						currentTracker.type = TrackerData.TYPE_START;
					} else if ("complete".equalsIgnoreCase(type)) {
						currentTracker.type = TrackerData.TYPE_COMPLETE;
					} else if ("midpoint".equalsIgnoreCase(type)) {
						currentTracker.type = TrackerData.TYPE_MIDPOINT;
						currentTracker.time = video.duration / 2;
					} else if ("firstquartile".equalsIgnoreCase(type)) {
						currentTracker.type = TrackerData.TYPE_FIRSTQUARTILE;
						currentTracker.time = video.duration / 4;
					} else if ("thirdquartile".equalsIgnoreCase(type)) {
						currentTracker.type = TrackerData.TYPE_THIRDQUARTILE;
						currentTracker.time = 3 * video.duration / 4;
					} else if ("pause".equalsIgnoreCase(type)) {
						currentTracker.type = TrackerData.TYPE_PAUSE;
					} else if ("unpause".equalsIgnoreCase(type)) {
						currentTracker.type = TrackerData.TYPE_UNPAUSE;
					} else if ("mute".equalsIgnoreCase(type)) {
						currentTracker.type = TrackerData.TYPE_MUTE;
					} else if ("unmute".equalsIgnoreCase(type)) {
						currentTracker.type = TrackerData.TYPE_UNMUTE;
					} else if ("replay".equalsIgnoreCase(type)) {
						currentTracker.type = TrackerData.TYPE_REPLAY;
					} else if ("skip".equalsIgnoreCase(type)) {
						currentTracker.type = TrackerData.TYPE_SKIP;
					} else if ((type != null) && (type.startsWith("sec:"))) {
						currentTracker.type = TrackerData.TYPE_TIME;
						currentTracker.time = getInteger(type.substring(4));
					}
				}
			} else if (localName.equals("htmloverlay")) {
				if (insideVideo) {
					if ((getRichMediaAd() == null)
							|| (getRichMediaAd().getVideo() == null)) {
						throw new SAXException(
								"htmloverlay tag found inside wrong video node");
					}
					VideoData video = getRichMediaAd().getVideo();
					insideMarkup = true;
					String type = attributes.getValue("type");
					if ("url".equalsIgnoreCase(type)) {
						video.htmlOverlayType = VideoData.OVERLAY_URL;
						String url = attributes.getValue("url");
						if ((url == null) || (url.length() == 0)) {
							throw new SAXException(
									"Empty url for overlay type " + type);
						}
						video.htmlOverlayUrl = url;
					} else if ("markup".equalsIgnoreCase(type)) {
						video.htmlOverlayType = VideoData.OVERLAY_MARKUP;
						insideMarkup = true;
					} else {
						video.htmlOverlayType = VideoData.OVERLAY_URL;
						String url = attributes.getValue("url");
						if ((url == null) || (url.length() == 0)) {
							throw new SAXException(
									"Empty url for overlay type " + type);
						}
						video.htmlOverlayUrl = url;

					}
					video.showHtmlOverlayAfter = getInteger(attributes
							.getValue("showafter"));
					video.showHtmlOverlay = getBoolean(attributes
							.getValue("show"));
				}
			}
		}
	}

	private int getInteger(String text) {
		if (text == null) {
			return -1;
		}
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException ex) {

		}
		return -1;
	}

	private long getLong(String text) {
		if (text == null) {
			return -1;
		}
		try {
			return Long.parseLong(text);
		} catch (NumberFormatException ex) {

		}
		return -1;
	}

	private boolean getBoolean(String text) {
		if (text == null) {
			return false;
		}
		try {
			return (Integer.parseInt(text) > 0);
		} catch (NumberFormatException ex) {

		}
		return false;
	}

	public RichMediaAd getRichMediaAd() {
		return richMediaAd;
	}

	public void setRichMediaAd(RichMediaAd richMediaAd) {
		this.richMediaAd = richMediaAd;
	}

}
