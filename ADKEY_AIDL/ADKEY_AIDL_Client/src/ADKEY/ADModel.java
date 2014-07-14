package ADKEY;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public abstract class ADModel implements Parcelable{
	public  final  static String AD_BANNER    = "banner";
	public  final  static String AD_RICHMEDIA = "richmedia";
	
	private String TYPE;//for type which ad.can be change by other.
	public  ADModel(String type){
		TYPE = type;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(TYPE);
	}
	
	public static boolean CheckADModel(String type){
		return TextUtils.equals(AD_BANNER, type)
				||TextUtils.equals(AD_RICHMEDIA, type);
	}
	public String getTYPE(){
		return TYPE;
	}
}
