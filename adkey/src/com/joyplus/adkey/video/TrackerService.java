package com.joyplus.adkey.video;

import static com.joyplus.adkey.Const.MAX_NUMBER_OF_TRACKING_RETRIES;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import com.joyplus.adkey.Const;

public class TrackerService {

	private static Object sLock = new Object();

	private static boolean sThreadRunning = false;

	private static Queue<TrackEvent> sTrackEvents = new LinkedList<TrackEvent>();

	private static Queue<TrackEvent> sRetryTrackEvents = new LinkedList<TrackEvent>();

	private static Thread sThread;

	private static boolean sStopped;

	public static void requestTrack(TrackEvent[] trackEvents) {
		synchronized (sLock) {
			for (TrackEvent trackEvent : trackEvents) {
				if (!sTrackEvents.contains(trackEvent)) {
					sTrackEvents.add(trackEvent);
				}
			}
		}
		if (!sThreadRunning) {
			startTracking();
		}
	}

	public static void requestTrack(TrackEvent trackEvent) {
		synchronized (sLock) {
			if (!sTrackEvents.contains(trackEvent)) {
				sTrackEvents.add(trackEvent);
			}
		}
		if (!sThreadRunning) {
			startTracking();
		}
	}

	public static void requestRetry(TrackEvent trackEvent) {
		synchronized (sLock) {
			if (!sRetryTrackEvents.contains(trackEvent)) {
				trackEvent.retries++;
				if (trackEvent.retries <= MAX_NUMBER_OF_TRACKING_RETRIES) {
					sRetryTrackEvents.add(trackEvent);
				}
			}
		}
	}

	private static boolean hasMoreUpdates() {
		synchronized (sLock) {
			boolean hasMore = !sTrackEvents.isEmpty();
			return hasMore;
		}
	}

	private static TrackEvent getNextUpdate() {
		synchronized (sLock) {
			if (sTrackEvents.peek() == null) {
				return null;
			} else {
				TrackEvent nextTrackEvent = sTrackEvents.poll();
				return nextTrackEvent;
			}
		}
	}

	public static void startTracking() {

		synchronized (sLock) {
			if (!sThreadRunning) {
				sThreadRunning = true;
				sThread = new Thread(new Runnable() {

					@Override
					public void run() {
						sStopped = false;
						while (!sStopped) {
							while (hasMoreUpdates() && !sStopped) {
								TrackEvent event = getNextUpdate();
								if (event == null)
									continue;
								URL u = null;
								try {
									u = new URL(event.url);
								} catch (MalformedURLException e) {
									continue;
								}

								DefaultHttpClient client = new DefaultHttpClient();
								HttpConnectionParams.setSoTimeout(
										client.getParams(),
										Const.SOCKET_TIMEOUT);
								HttpConnectionParams.setConnectionTimeout(
										client.getParams(),
										Const.CONNECTION_TIMEOUT);
								HttpGet get = new HttpGet(u.toString());
								HttpResponse response;
								try {
									response = client.execute(get);
									if (response.getStatusLine()
											.getStatusCode() != HttpURLConnection.HTTP_OK) {
										requestRetry(event);
									}
								} catch (Throwable t) {
									requestRetry(event);
								}

							}
							if ((!sStopped) && (!sRetryTrackEvents.isEmpty())) {
								try {
									Thread.sleep(30000);
								} catch (Exception e) {

								}
								synchronized (sLock) {
									sTrackEvents.addAll(sRetryTrackEvents);
									sRetryTrackEvents.clear();
								}
							} else {
								sStopped = true;
							}
						}
						sStopped = false;
						sThreadRunning = false;
						sThread = null;
					}
				});
				sThread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

					@Override
					public void uncaughtException(Thread thread, Throwable ex) {
						sThreadRunning = false;
						sThread = null;
						startTracking();
					}
				});
				sThread.start();
			}
		}
	}

	public static void release() {
		if (sThread != null) {
			sStopped = true;
		}
	}

}
