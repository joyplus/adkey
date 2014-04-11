package com.joyplus.ad.Monitor;

import com.joyplus.ad.Monitor.Monitorer.MonitorerState;
import com.joyplus.ad.data.TRACKINGURL;

public interface MonitorListener {
  
	void MonitorerStateChange(MonitorerState state, Monitor m);
	
	void MonitorStateChange(MonitorerState state, Monitor m,TRACKINGURL url);
	
}
