package com.joyplus.admonitor.data;


public class ImpressionInfo{
	
	public  final static ImpressionType DefaultType = ImpressionType.Joyplus;
	
	private ImpressionType mImpressionType;
	private String         mDisdlay_num;
	private String         mImpressionUrl;
	private String         mColumn1;
	private String         mColumn2;
	private String         mColumn3;
	
	public ImpressionInfo(){
		InitData();
	}
	
	public ImpressionInfo(ImpressionInfo info){
		if(info != null){
			mImpressionType = info.mImpressionType;
			mDisdlay_num    = info.mDisdlay_num;
			mImpressionUrl  = info.mImpressionUrl;
			mColumn1        = info.mColumn1;
			mColumn2        = info.mColumn2;
			mColumn3        = info.mColumn3;
		}else{
			InitData();
		}
	}
	
	public ImpressionInfo CreateNew(){
		return new ImpressionInfo(this);
	}
	
	private void InitData(){
		mImpressionType = DefaultType;
		mDisdlay_num    = "";
		mImpressionUrl  = "";
		mColumn1        = "";
		mColumn2        = "";
		mColumn3        = "";
	}
	
	
	public ImpressionType getImpressionType(){
		return mImpressionType;
	}
	public void setImpressionType(ImpressionType type){
		this.mImpressionType = type;
	}

	
	
	public String getDisplay_num(){
		return mDisdlay_num;
	}
	public void setDisplay_num(String number){
		mDisdlay_num = number;
	}

	
	public String getImpressionUrl(){
		return mImpressionUrl;
	}
	public void setImpressionUrl(String number){
		mImpressionUrl = number;
	}
	
	
	public String getColumn1(){
		return mColumn1;
	}
	public void setColumn1(String number){
		mColumn1 = number;
	}
	
	
	public String getColumn2(){
		return mColumn2;
	}
	public void setColumn2(String number){
		mColumn2 = number;
	}
	
	
	public String getColumn3(){
		return mColumn3;
	}
	public void setColumn3(String number){
		mColumn3 = number;
	}
	
	@Override
	public String toString(){
		StringBuffer ap = new StringBuffer();
		ap.append("ImpressionInfo={")
		  .append(" mImpressionType="+((mImpressionType==null)?"null":mImpressionType.toString()))
		  .append(" ,mDisdlay_num="+((mDisdlay_num==null)?"null":mDisdlay_num))
		  .append(" ,mImpressionUrl="+((mImpressionUrl==null)?"null":mImpressionUrl))
		  .append(" ,mColumn1="+((mColumn1==null)?"null":mColumn1))
		  .append(" ,mColumn2="+((mColumn2==null)?"null":mColumn2))
		  .append(" ,mColumn3="+((mColumn3==null)?"null":mColumn3))
		  .append("}");
		return ap.toString();
	}
	
}