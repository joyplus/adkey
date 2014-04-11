package com.joyplus.adkey.Monitorer;

import com.joyplus.adkey.Monitorer.Monitorer.MonitorerState;
public interface MonitorListener {
  
	void MonitorerStateChange(MonitorerState state, Monitor m);
	
	//void MonitorStateChange(MonitorerState state, Monitor m,TRACKINGURL url);
	
}
