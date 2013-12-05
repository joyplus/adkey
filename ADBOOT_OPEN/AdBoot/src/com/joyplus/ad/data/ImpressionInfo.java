package com.joyplus.ad.data;


public class ImpressionInfo{
	private String publisher_id;
	private String ad_id;
	private String ad_type;
	private String display_num;
	private String mImpressionUrl;
	private String column1;
	private String column2;
	
	public ImpressionInfo(){
		
	}
	
	public ImpressionInfo(String publisher_id,String ad_id,String ad_type,String display_num,String ImpressionUrl){
		this.publisher_id = publisher_id;
		this.ad_id = ad_id;
		this.ad_type = ad_type;
		this.display_num = display_num;
		this.mImpressionUrl = ImpressionUrl;
	}
	
	public String getPublisher_id()
	{
		return publisher_id;
	}

	public void setPublisher_id(String publisher_id)
	{
		this.publisher_id = publisher_id;
	}

	public String getAd_id()
	{
		return ad_id;
	}

	public void setAd_id(String ad_id)
	{
		this.ad_id = ad_id;
	}

	public String getAd_type()
	{
		return ad_type;
	}

	public void setAd_type(String ad_type)
	{
		this.ad_type = ad_type;
	}

	public String getDisplay_num()
	{
		return display_num;
	}

	public void setDisplay_num(String display_num)
	{
		this.display_num = display_num;
	}

	public String getImpressionUrl()
	{
		return mImpressionUrl;
	}

	public void setImpressionUrl(String ImpressionUrl)
	{
		this.mImpressionUrl = ImpressionUrl;
	}

	public String getColumn1()
	{
		return column1;
	}

	public void setColumn1(String column1)
	{
		this.column1 = column1;
	}

	public String getColumn2()
	{
		return column2;
	}

	public void setColumn2(String column2)
	{
		this.column2 = column2;
	}

	@Override
	public String toString()
	{
		return "ImpressionInfo [publisher_id=" + publisher_id + ", ad_id="
				+ ad_id + ", ad_type=" + ad_type + ", display_num="
				+ display_num + ", mImpressionUrl=" + mImpressionUrl + ", column1=" + column1
				+ ", column2=" + column2 + "]";
	}
	
}