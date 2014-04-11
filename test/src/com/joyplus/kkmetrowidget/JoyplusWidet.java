package com.joyplus.kkmetrowidget;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import com.joyplus.ad.test.R;
import com.joyplus.adkey.downloads.AdFileManager;
import com.joyplus.adkey.widget.SerializeManager;
import com.joyplus.request.ADRequest;
import com.joyplus.request.AdInfo;


public class JoyplusWidet extends AppWidgetProvider {

	private static final String TAG =	JoyplusWidet.class.getSimpleName();
	
	private RemoteViews   mRemoteViews;
	private PendingIntent mPendingIntent;
	private int flag = 0;
	private static final int MESSAGE_UPDATE      = 0;
	public  static final int MESSAGE_UPDATA_TIME = 30*1000;
	public  static final int MESSAGE_REQUEST_TIME = 60*1000;
	private Context context;
	private AppWidgetManager appWidgetManager;
	private int[] appWidgetIds;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MESSAGE_UPDATE:
				if(context!=null && appWidgetManager!=null){
					onUpdate(context, appWidgetManager, appWidgetIds);
				}
				sendMessageDelay();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		this.context  = context;
		Log.d(TAG, "----------------onUpdate-----------" + flag);
		AdInfo Info  =  (AdInfo) new SerializeManager().readSerializableData(AdFileManager.getInstance().GetBasePath()+"/ad");
		Bitmap bm    = null;
		if(Info != null){
			
			mPendingIntent    = GetPendingIntent(Info);
//			Log.d("Jas","EXIST="+(bm!=null)+",FIEL-->"+m.toString());
		}else{
			mPendingIntent    = null;
		}
		if(mRemoteViews == null){
			mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
			mRemoteViews.setImageViewResource(R.id.click, R.drawable.w0);
			
			this.appWidgetManager = appWidgetManager;
			this.appWidgetIds = appWidgetIds;
			sendMessageDelay();
		}else{
			RemoteViews subViews = new RemoteViews(context.getPackageName(),R.layout.widget_1);  
			mRemoteViews.removeAllViews(R.id.key);  
			mRemoteViews.addView(R.id.key, subViews); 
			
//			File m            = new File("/mnt/sdcard/abc.png");
			File m            = new File(AdFileManager.getInstance().GetBasePath(),"ADFILE");
			if(m.exists()) bm = BitmapFactory.decodeFile(m.toString());
			Log.d("Jas",""+AdFileManager.getInstance().GetBasePath().toString());
			if(bm != null){	
				mRemoteViews.setBitmap(R.id.click, "setImageBitmap", bm);
			}else{
				mRemoteViews.setImageViewResource(R.id.click, R.drawable.w0);  
			}
		}
		mRemoteViews.setOnClickPendingIntent(R.id.parent, mPendingIntent);
		mRemoteViews.setOnClickPendingIntent(R.id.key, mPendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, mRemoteViews);
	}
	
	private PendingIntent GetPendingIntent(AdInfo info){
		if(info==null)return null;
		try{
//			Intent intent = new Intent(context,JumpActivity.class);
			JSONObject json = new JSONObject();
//			intent.setType("com.joyplus.ad.test.view");
			Intent intent = new Intent("com.joyplus.ad.test.view");
			if(info.mOPENTYPE == null || info.mOPENTYPE==AdInfo.OPENTYPE.ANDROID){
				json.put("type", 2);
				json.put("url", ADRequest.BaseUrl);
			}else{
				json.put("type", 0);
				json.put("url", ADRequest.html5BaseUrl);
			}
			intent.putExtra("data", json.toString());
//			intent.setClassName("com.example.testad", "com.example.testad.MainActivity");
	//		mRemoteViews.setImageViewResource(R.id.click, R.drawable.icon);
			PendingIntent mPending = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//			PendingIntent mPending = PendingIntent.
			return mPending;
		}catch(JSONException e){
			
		}
		return null;
	}
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		Log.d(TAG, "----------------onReceive-----------");
	}
	
	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		Log.d(TAG, "----------------onDisabled-----------");
		mHandler.removeCallbacksAndMessages(null);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
		Log.d(TAG, "----------------onDeleted-----------");
		mHandler.removeCallbacksAndMessages(null);
	}
	
	private void sendMessageDelay(){
		mHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE, MESSAGE_UPDATA_TIME);
	}
	
	
	
	
}
