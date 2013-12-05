package com.joyplus.ad.application;

import java.io.File;

import com.joyplus.ad.config.Log;

import android.os.Parcel;
import android.os.Parcelable;

public class AdBootInfo implements Parcelable{
  
	private String FirstImage       = "";
	private String SecondImage      = "";
	private String BootAnimationZip = "";
	
	public AdBootInfo(){
		FirstImage       = "";//default path is unknow.
		SecondImage      = "";
		BootAnimationZip = "";
	}
	public AdBootInfo(AdBootInfo info){
		if(info != null){
			FirstImage       = info.FirstImage;
			SecondImage      = info.SecondImage;
			BootAnimationZip = info.BootAnimationZip;
		}
	}
	public AdBootInfo CreateNew(){
		return new AdBootInfo(this);
	}
	public void SetFirstImage(String firstimage){
		FirstImage = firstimage;
	}
	public String GetFirstImage(){
		return FirstImage;
	}
	
	public void SetSecondImage(String secondimage){
		SecondImage = secondimage;
	}
	public String GetSecondImage(){
		return SecondImage;
	}
	
	public void SetBootAnimationZip(String bootanimationzip){
		BootAnimationZip = bootanimationzip;
	}
	public String GetBootAnimationZip(){
		return BootAnimationZip;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(FirstImage);
		dest.writeString(SecondImage);
		dest.writeString(BootAnimationZip);
	}
   
	public boolean CheckFirstImageUsable(){
		if(FirstImage == null || "".equals(FirstImage))return false;
		File first = new File(FirstImage);
		if(first.exists())
			if(first.canRead() && first.canWrite())return true;
		    else return false;
		return true;
	}
	
	public boolean CheckSecondImageUsable(){
		if(SecondImage == null || "".equals(SecondImage))return false;
		File first = new File(SecondImage);
		if(first.exists())
			if(first.canRead() && first.canWrite())return true;
		    else return false;
		return true;
	}
	
	public boolean CheckBootAnimationZipUsable(){
		if(BootAnimationZip == null || "".equals(BootAnimationZip))return false;
		File first = new File(BootAnimationZip);
		if(first.exists())
			if(first.canRead() && first.canWrite())return true;
		    else return false;
		return true;
	}
}
