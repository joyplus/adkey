package com.joyplus.ad.application;

import java.io.File;

import com.joyplus.ad.config.Log;

import android.os.Parcel;
import android.os.Parcelable;

public class AdBootInfo implements Parcelable{
  
	private String FirstSource       = "";
	private String SecondSource      = "";
	private String ThirdSource       = "";
	
	public AdBootInfo(){
		FirstSource       = "";//default path is unknow.
		SecondSource      = "";
		ThirdSource = "";
	}
	public AdBootInfo(AdBootInfo info){
		if(info != null){
			FirstSource       = info.FirstSource;
			SecondSource      = info.SecondSource;
			ThirdSource = info.ThirdSource;
		}
	}
	public AdBootInfo CreateNew(){
		return new AdBootInfo(this);
	}
	public void SetFirstSource(String firstsource){
		FirstSource = firstsource;
	}
	public String GetFirstSource(){
		return FirstSource;
	}
	
	public void SetSecondSource(String secondsource){
		SecondSource = secondsource;
	}
	public String GetSecondSource(){
		return SecondSource;
	}
	
	public void SetThirdSource(String thirdsource){
		ThirdSource = thirdsource;
	}
	public String GetThirdSource(){
		return ThirdSource;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(FirstSource);
		dest.writeString(SecondSource);
		dest.writeString(ThirdSource);
	}
   
	public boolean CheckFirstImageUsable(){
		if(FirstSource == null || "".equals(FirstSource))return false;
		File first = new File(FirstSource);
		if(first.exists()){
			if(first.canRead() && first.canWrite())return true;
		    else return false;}
		return true;
	}
	
	public boolean CheckSecondImageUsable(){
		if(SecondSource == null || "".equals(SecondSource))return false;
		File first = new File(SecondSource);
		if(first.exists()){
			if(first.canRead() && first.canWrite())return true;
		    else return false;}
		return true;
	}
	
	public boolean CheckBootAnimationZipUsable(){
		if(ThirdSource == null || "".equals(ThirdSource))return false;
		File first = new File(ThirdSource);
		if(first.exists()){
			if(first.canRead() && first.canWrite())return true;
		    else return false;}
		return true;
	}
}
