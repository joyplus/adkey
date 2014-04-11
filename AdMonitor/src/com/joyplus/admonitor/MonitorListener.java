package com.joyplus.admonitor;

import com.joyplus.admonitor.Monitorer.MonitorerState;


public interface MonitorListener {
  
	void MonitorerStateChange(MonitorerState state, Monitor m);
	
	void MonitorStateChange(MonitorerState state, Monitor m);
	
}
