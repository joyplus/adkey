package com.joyplus.adkey;

public interface AdListener {

	public void adClicked();

	public void adClosed(Ad ad, boolean completed);

	public void adLoadSucceeded(Ad ad);

	public void adShown(Ad ad, boolean succeeded);

	public void noAdFound();

}
