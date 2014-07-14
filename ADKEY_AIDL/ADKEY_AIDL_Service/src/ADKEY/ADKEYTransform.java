package ADKEY;


import android.net.Uri;
import android.text.TextUtils;

import com.joyplus.adkey.Ad;
import com.joyplus.adkey.BannerAd;
import com.joyplus.adkey.request.Click;
import com.joyplus.adkey.request.Click.TYPE;
import com.joyplus.adkey.video.RichMediaAd;

public class ADKEYTransform {
	
	public synchronized static AD ThransformAD(Ad ad){
		AD   mAD= null;
		if(ad == null)return mAD;
		if(ad instanceof BannerAd)
			mAD = new AD(ADModel.AD_BANNER);
		else if(ad instanceof RichMediaAd)
			mAD = new AD(ADModel.AD_RICHMEDIA);
		else return null;
		mAD.setAdClickedContent(getAdClickedContent(ad));
		mAD.setAdClickedSourceVerify(getAdClickedSourceVerify(ad));
		mAD.setAdClickedSourceVerifyType(getAdClickedSourceVerifyType(ad));
		mAD.setAdClickType(getAdClickType(ad));
		mAD.setAdDisplayedSourceType(getAdDisplayedSourceType(ad));
		mAD.setAdDisplayedSourceUrl(getAdDisplayedSourceUrl(ad));
		mAD.setAdDisplayedSourceVerify(getAdDisplayedSourceVerify(ad));
		mAD.setAdDisplayedSourceVerifyType(getAdDisplayedSourceVerifyType(ad));
		return mAD;
	}
    /*  0  无展示资�?
     *  1  图片
     *  2  ZIP �?
     *  3  视频
	*/
	public static int getAdDisplayedSourceType(Ad ad){
		if(ad == null)return 0;
		String type = "";
		if(ad instanceof BannerAd)
			type = ((BannerAd) ad).GetCreative_res_sourcetype();
		else if(ad instanceof RichMediaAd)
			type = ((RichMediaAd) ad).GetCreative_res_sourcetype();
		else return 0;
		//---- 1 图片--- 2 视频--- 3 富媒�?-- 4 ZIP ---5 视频+ZIP
		if("1".equals(type))return 1;
		else if("4".equals(type))return 2;
		else if("2".equals(type))return 3;
		return 0;
	}
	/*  null
	 *  展示的url地址
	 * */
	public static String getAdDisplayedSourceUrl(Ad ad){
		if(ad == null)return null;
		if(ad instanceof BannerAd)
			return ((BannerAd) ad).GetCreative_res_url();
		else if(ad instanceof RichMediaAd)
			return ((RichMediaAd) ad).GetCreative_res_url();
		return null;
	}
    /*  false 无展示内�?     *  true  有展示内�?/
	public static boolean getAdDisplayedContentFlag(Ad ad){ 
		//return (!TextUtils.isEmpty(getAdDisplayedSourceUrl(ad)));
		return false;
	}
	/* null
	 * 展示内容字符�?
	 * */
	public static String getAdDisplayedContent(Ad ad){
		return null;
	}
	
	/* 0 无需�?��
	 * 1 MD5�?��
	 * 2 Hash�?��
	 * */
	public static int getAdDisplayedSourceVerifyType(Ad ad){
		return (TextUtils.isEmpty(getAdDisplayedSourceVerify(ad))?0:2);
	}
	
	/* null 
	 * MD5校验�?
	 * Hash校验�?
	 * */
	public static String getAdDisplayedSourceVerify(Ad ad){
		if(ad == null)return null;
		if(ad instanceof BannerAd)
			return ((BannerAd) ad).GetCreative_res_hash();
		else if(ad instanceof RichMediaAd)
			return ((RichMediaAd) ad).GetCreative_res_Hash();
		return null;
	}
	
	
	/* null
	 * 统计URL
	 * */
	public static String getAdDisplayedStaticUrl(Ad ad){
		if(ad ==null)return null;
		if(ad instanceof BannerAd)
			return ((BannerAd) ad).getmImpressionUrl();
		else if(ad instanceof RichMediaAd)
			return ((RichMediaAd) ad).getmImpressionUrl();
		return null;
	}
	
	/* 0 无跳�?
	 * 1 跳转应用
	 * 2 跳转web
	 * 3 跳转图片
	 * 4 跳转视频
	 * */
	public static int getAdClickType(Ad ad){
		if(TextUtils.isEmpty(getClickUrl(ad)))return 0;
		Click click = null;
		if(ad instanceof BannerAd)
			click = ((BannerAd) ad).GetClick();
		else if(ad instanceof RichMediaAd)
			click = ((RichMediaAd) ad).GetClick();
		if(click == null || click.mTYPE == null)return 0;
		if(TYPE.OPENAPP == click.mTYPE){
			return 1;
		}else if(TYPE.OPENURL == click.mTYPE){
			return 2;
		}else if(TYPE.OPENIMAGE == click.mTYPE){
			return 3;
		}else if(TYPE.OPENVIDEO == click.mTYPE){
			return 4;
		}
		return 0;
	}
	/* null
	 * 跳转内容String*/
	public static String getAdClickedContent(Ad ad){
		String clickuri = getClickUrl(ad);
		int type = getAdClickType(ad);
		if(type==0 || TextUtils.isEmpty(clickuri))return null;
		if(type == 3 || type == 4){//image or video
			return transformClickURL(clickuri);
		}
		return clickuri;
	}
	
	/* 0 无需校验
	 * 1 MD5�?��
	 * 2 Hash校验
	 * */
	public static int getAdClickedSourceVerifyType(Ad ad){
		return TextUtils.isEmpty(getAdClickedSourceVerify(ad))?0:2;
	}
	/* null
	 * MD5/Hash校验�?
	 * */
	public static String getAdClickedSourceVerify(Ad ad){
		String uri = getClickUrl(ad);
		if(TextUtils.isEmpty(uri))return null;
		int ClickType = getAdClickType(ad);
		if(3 == ClickType || 4 == ClickType){//image or video
			String hash = getHash(uri);
			return TextUtils.isEmpty(hash)?null:hash.trim();	
		}
		return null;
	}
	/* null
	 * 统计URL
	 * */
	public  static String getClickedStaticUrl(Ad ad){
		if(ad == null)return null;
		Click click = null;
		if(ad instanceof BannerAd)
			click = ((BannerAd) ad).GetClick();
		else if(ad instanceof RichMediaAd)
			click = ((RichMediaAd) ad).GetClick();
		if(click == null || TextUtils.isEmpty(click.mClickURL))return null;
		return click.mClickURL.trim();
	}
	
	
	
	/////////////////////////////////////////////////////////////////////////
	//for transform
	private static String getClickUrl(Ad ad){
		if(ad == null)return null;
		Click  click   = null;
		if(ad instanceof BannerAd)
			click = ((BannerAd) ad).GetClick();
		else if(ad instanceof RichMediaAd)
			click = ((RichMediaAd) ad).GetClick();
		return ((click==null)?null:(TextUtils.isEmpty(click.mRes)?null:click.mRes.trim()));
	}
	private static String transformClickURL(String url){
		if(TextUtils.isEmpty(url))return null;
		int index = url.indexOf("?h=");
		if(index!=-1){
			return url.substring(0, index);
		}
		return url;
	}
	private static String getHash(String uri){
		if(TextUtils.isEmpty(uri))return null;
		try{
		   Uri mUri = Uri.parse(uri);
		   return mUri.getQueryParameter("h");
		}catch(Throwable e){}
		return null;
	}
	///////////////////////////////////////////////////////////////////////////////
	/* for DEBUG*/
	public static String Transform(Ad ad){
		if(ad == null)return "AD{ IS UNLL !!!}";
		StringBuffer sp = new StringBuffer();
		sp.append("AD{")
		  .append("AdDisplayedSourceType="+getAdDisplayedSourceType(ad))
		  .append(" ,AdDisplayedSourceUrl="+getAdDisplayedSourceUrl(ad))
		  .append(" ,AdDisplayedSourceVerifyType="+getAdDisplayedSourceVerifyType(ad))
		  .append(" ,AdDisplayedSourceVerify="+getAdDisplayedSourceVerify(ad))
		  .append(" ,AdDisplayedStaticUrl="+getAdDisplayedStaticUrl(ad))
		  .append(" ,AdClickedType="+getAdClickType(ad))
		  .append(" ,AdClickedContent="+getAdClickedContent(ad))
		  .append(" ,AdClickedSourceVerifyType="+getAdClickedSourceVerifyType(ad))
		  .append(" ,AdClickedSourceVerify="+getAdClickedSourceVerify(ad))
		  .append(" ,AdClickedStaticUrl="+getClickedStaticUrl(ad))
		.append("}");
		return sp.toString();  
	}
}
