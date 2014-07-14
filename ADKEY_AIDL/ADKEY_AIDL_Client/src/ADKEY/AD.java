package ADKEY;

import android.os.Parcel;
import android.os.Parcelable;

public class AD extends ADModel{
	
	private int     AdDisplayedSourceType       = 0;
	private String  AdDisplayedSourceUrl        = null;
	private int     AdDisplayedSourceVerifyType = 0;
	private String  AdDisplayedSourceVerify     = null;
	private int     AdClickType	                = 0;
	private String  AdClickedContent            = null;
	private int		AdClickedSourceVerifyType   = 0;
	private String  AdClickedSourceVerify       = null;
	
	public AD(String type){
		super(type);
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		super.writeToParcel(dest, flags);
		dest.writeInt(AdDisplayedSourceType);
		dest.writeString(AdDisplayedSourceUrl);
		dest.writeInt(AdDisplayedSourceVerifyType);
		dest.writeString(AdDisplayedSourceVerify);
		dest.writeInt(AdClickType);
		dest.writeString(AdClickedContent);
		dest.writeInt(AdClickedSourceVerifyType);
		dest.writeString(AdClickedSourceVerify);
	}
	
	public static final Parcelable.Creator<AD> CREATOR = new Parcelable.Creator<AD>(){
		@Override
		public AD createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			AD ad = new AD(source.readString());
			ad.AdDisplayedSourceType = source.readInt();
			ad.AdDisplayedSourceUrl  = source.readString();
			ad.AdDisplayedSourceVerifyType = source.readInt();
			ad.AdDisplayedSourceVerify     = source.readString();
			ad.AdClickType                 = source.readInt();
			ad.AdClickedContent            = source.readString();
			ad.AdClickedSourceVerifyType   = source.readInt();
			ad.AdClickedSourceVerify       = source.readString();
			return ad;
		}

		@Override
		public AD[] newArray(int size) {
			// TODO Auto-generated method stub
			return new AD[size];
		}
    	
    };
    
    public String toString() {
    	StringBuffer sp = new StringBuffer();
    	sp.append("AD{")
    		.append("TYPE="+getTYPE())
    		.append(",AdDisplayedSourceType="+getAdDisplayedSourceType())
    		.append(",AdDisplayedSourceUrl="+getAdDisplayedSourceUrl())
    		.append(",AdDisplayedSourceVerifyType="+getAdDisplayedSourceVerifyType())
    		.append(",AdDisplayedSourceVerify="+getAdDisplayedSourceVerify())
    		.append(",AdClickType="+getAdClickType())
    		.append(",AdClickedContent="+getAdClickedContent())
    		.append(",AdClickedSourceVerifyType="+getAdClickedSourceVerifyType())
    		.append(",AdClickedSourceVerify="+getAdClickedSourceVerify())
    		.append("}");
    	return sp.toString();
    }

	public int getAdDisplayedSourceType() {
		return AdDisplayedSourceType;
	}

	public String getAdDisplayedSourceUrl() {
		return AdDisplayedSourceUrl;
	}

	public int getAdDisplayedSourceVerifyType() {
		return AdDisplayedSourceVerifyType;
	}

	public String getAdDisplayedSourceVerify() {
		return AdDisplayedSourceVerify;
	}

	public int getAdClickType() {
		return AdClickType;
	}

	public String getAdClickedContent() {
		return AdClickedContent;
	}

	public int getAdClickedSourceVerifyType() {
		return AdClickedSourceVerifyType;
	}

	public String getAdClickedSourceVerify() {
		return AdClickedSourceVerify;
	}

	public void setAdDisplayedSourceType(int adDisplayedSourceType) {
		AdDisplayedSourceType = adDisplayedSourceType;
	}

	public void setAdDisplayedSourceUrl(String adDisplayedSourceUrl) {
		AdDisplayedSourceUrl = adDisplayedSourceUrl;
	}

	public void setAdDisplayedSourceVerifyType(int adDisplayedSourceVerifyType) {
		AdDisplayedSourceVerifyType = adDisplayedSourceVerifyType;
	}

	public void setAdDisplayedSourceVerify(String adDisplayedSourceVerify) {
		AdDisplayedSourceVerify = adDisplayedSourceVerify;
	}

	public void setAdClickType(int adClickType) {
		AdClickType = adClickType;
	}

	public void setAdClickedContent(String adClickedContent) {
		AdClickedContent = adClickedContent;
	}

	public void setAdClickedSourceVerifyType(int adClickedSourceVerifyType) {
		AdClickedSourceVerifyType = adClickedSourceVerifyType;
	}

	public void setAdClickedSourceVerify(String adClickedSourceVerify) {
		AdClickedSourceVerify = adClickedSourceVerify;
	}

}
