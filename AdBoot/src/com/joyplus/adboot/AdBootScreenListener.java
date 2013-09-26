package com.joyplus.adboot;

/*
 * Interface of AdBootScreenManager
 * Jas@20130711
 * 
 * */
public interface AdBootScreenListener {

	// Interface of load advert file successed.
	public void adLoadSucceeded();

	// Interface of no Ad found.
	public void noAdFound();

	// interface of closed
	public void Closed();

}
