package com.joyplus.ad.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.joyplus.ad.application.AdBoot;
import com.joyplus.ad.application.AdBootInfo;
import com.joyplus.ad.data.ADBOOT;
import com.joyplus.ad.data.TRACKINGURL;
import com.joyplus.ad.data.TRACKINGURL.TYPE;

public class AdBootImpressionInfo {
    public int    _ID            = -1;
	public String publisher_id   = "";
	public String mImpressionUrl = "";
	
	public String FirstSource    = "";
	public String SecondSource   = "";
	public String ThirdSource    = "";
	
	public String miaozhen       = "";
	public String iresearch      = "";
	public String admaster       = "";
	public String nielsen        = "";
	
	public int    Count          = 0;
	
	public boolean IsAviable(){
		return IsAviable(publisher_id)&&IsAviable(mImpressionUrl)
				&&(IsAviable(FirstSource)||IsAviable(SecondSource)||IsAviable(ThirdSource));
	}
	private boolean IsAviable(String value){
		return (!(value==null || "".equals(value)));
	}
	
	public AdBootImpressionInfo(){
		
	}
	public AdBootImpressionInfo(AdBoot ad,ADBOOT AD){
		if(ad != null && ad.GetPublisherId().CheckId()){
			publisher_id    = ad.GetPublisherId().GetPublisherId();
			AdBootInfo Info = ad.GetAdBootInfo();
			if(Info!=null){
				FirstSource  = Info.GetFirstSource();
				SecondSource = Info.GetSecondSource();
				ThirdSource  = Info.GetThirdSource();
			}
		}
		if(AD != null && AD.video != null){
			if(AD.video.impressionurl != null){
			    mImpressionUrl = AD.video.impressionurl.URL;
			}
			if(AD.video.trackingurl != null){
				for(TRACKINGURL URL : AD.video.trackingurl){
					if(URL.Type == TYPE.ADMASTER){
						admaster  = URL.URL;
					}else if(URL.Type == TYPE.IRESEARCH){
						iresearch = URL.URL;
					}else if(URL.Type == TYPE.MIAOZHEN){
						miaozhen  = URL.URL;
					}else if(URL.Type == TYPE.NIELSEN){
						nielsen   = URL.URL;
					}
				}
			}
		}
		Count = 0;
	}
	
	
	
	public static ContentValues GetContentValues(AdBootImpressionInfo info){
		if(info == null || !info.IsAviable())return null;
		ContentValues Values = new ContentValues();
		Values.put("publisher_id",   info.publisher_id);
		Values.put("mImpressionUrl", info.mImpressionUrl);
		Values.put("FirstSource",    info.FirstSource);
		Values.put("SecondSource",   info.SecondSource);
		Values.put("ThirdSource",    info.ThirdSource);
		Values.put("miaozhen",       info.miaozhen);
		Values.put("iresearch",      info.iresearch);
		Values.put("admaster",       info.admaster);
		Values.put("nielsen",        info.nielsen);
		Values.put("Count",          info.Count);
		return Values;
	}
	public static AdBootImpressionInfo GetAdBootImpressionInfo(Cursor c){
		if(c == null)return null;
		AdBootImpressionInfo Info = new AdBootImpressionInfo();
		Info._ID            = c.getInt(c.getColumnIndex("_id"));
		Info.publisher_id   = c.getString(c.getColumnIndex("publisher_id"));
		Info.mImpressionUrl = c.getString(c.getColumnIndex("mImpressionUrl"));
		Info.FirstSource    = c.getString(c.getColumnIndex("FirstSource"));
		Info.SecondSource   = c.getString(c.getColumnIndex("SecondSource"));
		Info.ThirdSource    = c.getString(c.getColumnIndex("ThirdSource"));
		Info.miaozhen       = c.getString(c.getColumnIndex("miaozhen"));
		Info.iresearch      = c.getString(c.getColumnIndex("iresearch"));
		Info.admaster       = c.getString(c.getColumnIndex("admaster"));
		Info.nielsen        = c.getString(c.getColumnIndex("nielsen"));
		Info.Count          = c.getInt(c.getColumnIndex("Count"));
		return Info;
	}
	
	public String toString(){
		StringBuffer ap = new StringBuffer();
		ap.append("AdBootImpressionInfo{")
		  .append("_ID="+_ID)
		  .append(",publisher_id="+publisher_id)
		  .append(",mImpressionUrl="+mImpressionUrl)
		  .append(",FirstSource="+FirstSource)
		  .append(",SecondSource="+SecondSource)
		  .append(",ThirdSource="+ThirdSource)
		  .append(",miaozhen="+miaozhen)
		  .append(",iresearch="+iresearch)
		  .append(",admaster="+admaster)
		  .append(",nielsen="+nielsen)
		  .append(",Count="+Count)
		  .append("}");
		return ap.toString();
	}
}
