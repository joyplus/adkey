package com.joyplus.adkey;

/*
 * Interface of AdBootScreen
 * Jas@20130711
 * 
 * */
public interface AdBootScreenListener {

	//Interface of load advert file successed.
	public void adLoadSucceeded();
    
	//Interface of no Ad found.
	public void noAdFound();
	
	//interface of report count fail.
	public void ReportFail();
	
	//interface of report count success.
	public void ReportSuccessed();
	
	//interface of closed
	public void Closed();
	
}
