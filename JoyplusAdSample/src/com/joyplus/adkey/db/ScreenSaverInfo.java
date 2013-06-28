package com.joyplus.adkey.db;

/**
 * @author yyc
 *
 */
public class ScreenSaverInfo{

	private String url;
	private String baseurl;
	private String filename;
	private String publishid;
	private String type;
	private String time;
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getBaseurl()
	{
		return baseurl;
	}
	public void setBaseurl(String baseurl)
	{
		this.baseurl = baseurl;
	}
	public String getFilename()
	{
		return filename;
	}
	public void setFilename(String filename)
	{
		this.filename = filename;
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
	public String getTime()
	{
		return time;
	}
	public void setTime(String time)
	{
		this.time = time;
	}
	public ScreenSaverInfo(String url, String baseurl, String filename,
			String publishid, String type, String time)
	{
		super();
		this.url = url;
		this.baseurl = baseurl;
		this.filename = filename;
		this.publishid = publishid;
		this.type = type;
		this.time = time;
	}
	public ScreenSaverInfo(){
		super();
	}
	@Override
	public String toString()
	{
		return "ScreenSaverInfo [url=" + url + ", baseurl=" + baseurl
				+ ", filename=" + filename + ", publishid=" + publishid
				+ ", type=" + type + ", time=" + time + "]";
	}
	
}