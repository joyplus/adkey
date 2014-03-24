package com.joyplus.ad.test;

import greendroid.widget.AsyncImageView;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joyplus.ad.test.entity.GroupInfo;

public class GroupAdapter extends BaseAdapter {

	private List<GroupInfo> data;
	private Context c;
	private float mDensity;
	
	
	public GroupAdapter(List<GroupInfo> data, Context c) {
		super();
		this.data = data;
		this.c = c;
		Display display = ((Activity)c).getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        mDensity = displayMetrics.density;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View contentView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(contentView == null){
			holder = new ViewHolder();
			contentView = LayoutInflater.from(c).inflate(R.layout.layout_item_grid_group, null);
			holder.image = (AsyncImageView) contentView.findViewById(R.id.item_image);
			holder.text = (TextView) contentView.findViewById(R.id.group_name);
			contentView.setTag(holder);
		}else{
			holder = (ViewHolder) contentView.getTag();
		}
//		holder.image.setImageResource(MovieListActivity.resouce[position%3]);
		holder.image.setUrl(data.get(position).getPictureUrl());
		holder.text.setText(data.get(position).getName());
		int width = (int) (parent.getWidth()-80*mDensity)/3;
		contentView.setLayoutParams(new AbsListView.LayoutParams(width, (width*4)/3));
		return contentView;
	}
	
	class ViewHolder{
		AsyncImageView image;
		TextView text;
	}
}
