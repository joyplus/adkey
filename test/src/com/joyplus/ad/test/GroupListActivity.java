package com.joyplus.ad.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.Toast;

public class GroupListActivity extends Activity implements OnItemSelectedListener, OnItemClickListener{

	private GridView mGridView;
	private List<GroupInfo> mGroups = new ArrayList<GroupInfo>(); 
	private String[] resorce_urls = {"http://www.tvptv.com/UpNewImg/42%28146%29.jpg",
			"http://p.ganyou.com/attachment/image/2011/04/24/121323605.jpg",
			"http://a3.att.hudong.com/06/48/01300000931713128019481868712.jpg",
			"http://photocdn.sohu.com/20100612/Img272757506.jpg"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);
		
		for (int i = 1; i <= 200; i++) {
			GroupInfo info = new GroupInfo();
			info.setId(i);
			info.setName("悦单"+i);
			info.setPictureUrl(resorce_urls[i%4]);
			mGroups.add(info);
		}
		
		mGridView = (GridView) findViewById(R.id.grid);
		mGridView.setAdapter(new GroupAdapter(mGroups, this));
		mGridView.setOnItemClickListener(this);
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
		startActivity(new Intent(this, MovieListActivity.class));
		Toast.makeText(this, mGroups.get(arg2).getName() + " Click", Toast.LENGTH_SHORT).show();
	}
}
