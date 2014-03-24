package com.joyplus.ad.test;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.Toast;

import com.joyplus.ad.test.entity.GroupInfo;
import com.joyplus.ad.test.ui.WaitingDialog;
import com.joyplus.ad.test.util.HttpTools;
import com.joyplus.ad.test.util.Log;

public class GroupListActivity extends Activity implements OnItemSelectedListener, OnItemClickListener{

	private static final int DIALOG_WAITING = 0;
	private static final int MESSAGE_GETDATA_SUCESS = 0;
	private static final int MESSAGE_GETDATA_FAILED = MESSAGE_GETDATA_SUCESS + 1;
	private static final String BASE_URL = "http://advapi.yue001.com/advapi/v1/topic/list?bid=zino";
	private static final String TAG = GroupListActivity.class.getName();
	private GridView mGridView;
	private GroupAdapter mAdapter;
	private List<GroupInfo> mGroups = new ArrayList<GroupInfo>(); 
//	private String[] resorce_urls = {"http://www.tvptv.com/UpNewImg/42%28146%29.jpg",
//			"http://p.ganyou.com/attachment/image/2011/04/24/121323605.jpg",
//			"http://a3.att.hudong.com/06/48/01300000931713128019481868712.jpg",
//			"http://photocdn.sohu.com/20100612/Img272757506.jpg"};
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MESSAGE_GETDATA_SUCESS:
				removeDialog(DIALOG_WAITING);
				mAdapter.notifyDataSetChanged();
				break;
			case MESSAGE_GETDATA_FAILED:
				removeDialog(DIALOG_WAITING);
				Toast.makeText(GroupListActivity.this, "get data failed", Toast.LENGTH_SHORT).show();
				break;
			default:
				super.handleMessage(msg);
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);
		mGridView = (GridView) findViewById(R.id.grid);
		mAdapter = new GroupAdapter(mGroups, this);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
		new Thread(new GetDateRunable()).start();
		showDialog(DIALOG_WAITING);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
//		Log.d("TAG", arg2 + "seleted");
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, MovieListActivity.class);
		intent.putExtra("url", mGroups.get(arg2).getMovieUrl());
		startActivity(intent);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DIALOG_WAITING:
			WaitingDialog dlg = new WaitingDialog(this);
			dlg.show();
			dlg.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			dlg.setDialogWindowStyle();
			return dlg;
		default:
			return super.onCreateDialog(id);
		}
	}
	
	class GetDateRunable implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				String result = HttpTools.get(GroupListActivity.this, BASE_URL);
				Log.d(TAG, "bangdan result --->" + result);
				JSONObject jsonObj = new JSONObject(result);
				JSONObject _metaObj = jsonObj.getJSONObject("_meta");
				if("00000".equals(_metaObj.get("code"))){
					JSONArray items = jsonObj.getJSONArray("items");
					for (int i=0; i<items.length(); i++) {
						JSONObject item = items.getJSONObject(i);
						GroupInfo info = new GroupInfo();
						info.setId(Integer.valueOf(item.getString("id")));
						info.setName(item.getString("name"));
						info.setPictureUrl(item.getString("picUrl"));
						info.setMovieUrl(item.getString("url"));
						mGroups.add(info);
					}
					mHandler.sendEmptyMessage(MESSAGE_GETDATA_SUCESS);
				}else{
					mHandler.sendEmptyMessage(MESSAGE_GETDATA_FAILED);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				mHandler.sendEmptyMessage(MESSAGE_GETDATA_FAILED);
			}
		}
		
	}
}
