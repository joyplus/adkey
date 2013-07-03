package com.joyplus.adkey.data;

/**
 * @author yyc
 *
 */
public class ImpressionInfo{
	private String publisher_id;
	private String ad_id;
	private String ad_type;
	private String display_num;
	private String column1;
	private String column2;
	private String column3;
	
	public ImpressionInfo(){
		
	}
	
	public ImpressionInfo(String publisher_id,String ad_id,String ad_type,String display_num){
		this.publisher_id = publisher_id;
		this.ad_id = ad_id;
		this.ad_type = ad_type;
		this.display_num = display_num;
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

	public String getColumn3()
	{
		return column3;
	}

	public void setColumn3(String column3)
	{
		this.column3 = column3;
	}

	@Override
	public String toString()
	{
		return "ImpressionInfo [publisher_id=" + publisher_id + ", ad_id="
				+ ad_id + ", ad_type=" + ad_type + ", display_num="
				+ display_num + ", column1=" + column1 + ", column2=" + column2
				+ ", column3=" + column3 + "]";
	}
	
}