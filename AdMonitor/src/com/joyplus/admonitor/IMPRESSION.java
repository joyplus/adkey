package com.joyplus.admonitor;

import com.joyplus.admonitor.data.ImpressionInfo;
import com.joyplus.admonitor.data.ImpressionType;

public class IMPRESSION {
  
	public  ImpressionType mImpressionType = ImpressionInfo.DefaultType;
	public  String         mImpressionURL  = "";
	
	public  boolean        mMonitored      = false;
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer ap = new StringBuffer();
		ap.append("IMPRESSION={")
		  .append(" mImpressionType="+((mImpressionType==null)?"null":mImpressionType.toString()))
		  .append(" ,mImpressionURL="+((mImpressionURL==null)?"null":mImpressionURL))
		  .append(" ,mMonitored="+mMonitored)
		  .append("}");
		return ap.toString();
	}
	
	
	
	
	
	
}
