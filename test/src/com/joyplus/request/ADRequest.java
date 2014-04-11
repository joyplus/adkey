package com.joyplus.request;

import greendroid.app.GDApplication;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;

import net.tsz.afinal.FinalHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.joyplus.adkey.downloads.AdFileManager;
import com.joyplus.adkey.downloads.DownLoadManager;
import com.joyplus.adkey.downloads.Download;
import com.joyplus.adkey.widget.Log;
import com.joyplus.adkey.widget.SerializeManager;
import com.joyplus.kkmetrowidget.JoyplusWidet;

public class ADRequest {
    private boolean DEBUG = true;
	private Context mContext;
	private static String ID  = "9a51d0c16fa83008eba3001aa892b901";
	private String REQUESTURL = "http://advapi.joyplus.tv/advapi/v1/topic/get?s=";
	private String DEBUG_PARH = "/mnt/sdcard/Joyplus_video/";
	private String PATH       = "./data/misc/konka/advert/Jas/"+ID+"/";
	
	public static final String html5BaseUrl = "http://download.joyplus.tv/app/item.html?s="+ID;
	public static final String BaseUrl      = "http://advapi.joyplus.tv/advapi/v1/topic/get?s="+ID;
	
	private SerializeManager  mSerializeManager;
	
    private static ADRequest mADRequest;
    public  static void Init(Context context){
    	mADRequest = new ADRequest(context);
    }
    public  static ADRequest GetInstance(){
    	return mADRequest;
    }
	private ADRequest(Context context){
		mContext          = context;
		mSerializeManager = new SerializeManager();
		AdFileManager.getInstance().SetBasePath(DEBUG?DEBUG_PARH:PATH);
		File f = AdFileManager.getInstance().GetBasePath();
		if(!f.exists()){
			f.mkdirs();
		}
	}
	
	private Thread mRequestThread = null;
	public void request(){
		if(mRequestThread != null)return;
		mRequestThread = new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				GDApplication p = GDApplication.GetInstance();
				if(p!=null){
					p.SetClock();
				}
				Log.d(""+AdFileManager.getInstance().GetBasePath());
				AdInfo Info = Request();// for request ad.
				if(Info != null){
					Log.d("Info-->"+Info.toString());
					if(Info.widgetPicUrl != null){
						Log.d("ADD Download --------------------------------------------");
						mSerializeManager.writeSerializableData(AdFileManager.getInstance().GetBasePath()+"/ad", Info);
						Download m   = new Download();
						m.LocalFile  = AdFileManager.getInstance().GetBasePath()+"/Temp";
						m.TargetFile = AdFileManager.getInstance().GetBasePath()+"/ADFILE";
						m.URL        = Info.widgetPicUrl;
						DownLoadManager.getInstance().AddDownload(m);
						Log.d("ADD Download +++++++++++++++++++++++++++++++++++++++++++++");
					}
				}
				mRequestThread = null;
			}
		};
		mRequestThread.setUncaughtExceptionHandler(new UncaughtExceptionHandler(){
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				// TODO Auto-generated method stub
				mRequestThread = null;
			}}
		);
		mRequestThread.start();
    }
	
	private AdInfo Request(){
		FinalHttp fh = new FinalHttp();
		fh.configTimeout(JoyplusWidet.MESSAGE_UPDATA_TIME);
		Object b = fh.getSync(REQUESTURL+ID);
		return ParserAdInfo((String) b);
	}
	
	private AdInfo ParserAdInfo(String result){
		if(result == null)return null;
		try {
			AdInfo Info = new AdInfo();
			JSONObject resultObj;
			resultObj = new JSONObject(result);
			JSONObject _metaObj = resultObj.getJSONObject("_meta");
			if("00000".equals(_metaObj.get("code"))){
				Info.creativeUrl = resultObj.getString("creativeUrl");
				if(resultObj.has("trackingUrl")){
					Info.trackingUrl = resultObj.getString("trackingUrl");
				}
				Info.widgetPicUrl= resultObj.getString("widgetPicUrl");
				JSONArray items = resultObj.getJSONArray("items");
				for(int i=0; i<items.length(); i++){
					JSONObject item = items.getJSONObject(i);
					AdInfoItems info = new AdInfoItems();
					info.id          = Integer.valueOf(item.getString("id"));
					info.name        = (item.getString("name"));
					info.description = item.getString("description");
					info.uri         = item.getString("uri");
					info.column      = item.getString("column");
					info.zone        = item.getString("zone");
					info.picUrl      = item.getString("picUrl");
					info.createTime  = item.getString("createTime");
					Info.items.add(info);
				}
			}
			return Info;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
