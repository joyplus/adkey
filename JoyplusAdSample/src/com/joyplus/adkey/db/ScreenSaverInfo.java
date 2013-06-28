package com.joyplus.adkey.db;

public class ScreenSaverInfo{

	private String url;
	private String time;
	private String publishid;
	private String type;

	public ScreenSaverInfo(String url, String time, String publishid,
			String type)
	{
		super();
		this.url = url;
		this.time = time;
		this.publishid = publishid;
		this.type = type;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getTime()
	{
		return time;
	}
	public void setTime(String time)
	{
		this.time = time;
	}
	public String getPublishid()
	{
		return publishid;
	}
	public void setPublishid(String publishid)
	{
		this.publishid = publishid;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	@Override
	public String toString()
	{
		return "ScreenSaverCache [url=" + url + ", time=" + time
				+ ", publishid=" + publishid + ", type=" + type + "]";
	}
}