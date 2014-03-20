package com.joyplus.ad.test;

import greendroid.widget.AsyncImageView;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.HorizontalScrollView;

public class MovieListActivity extends Activity{

//	private GridView mGridView;
	private LinearLayout mLinearLayout;
	private HorizontalScrollView mScrollView;
	private HorizontalScrollView mScrollViewBackground;
	private ImageView mBackgroundImage;
	private int mScreenWidth;
	private float mDensity;
	private String[] resorce_urls = {"http://www.tvptv.com/UpNewImg/42%28146%29.jpg",
			"http://p.ganyou.com/attachment/image/2011/04/24/121323605.jpg",
			"http://a3.att.hudong.com/06/48/01300000931713128019481868712.jpg",
			"http://photocdn.sohu.com/20100612/Img272757506.jpg"};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mDensity = displayMetrics.density;
        mLinearLayout = (LinearLayout) findViewById(R.id.layout);
        mScrollView = (HorizontalScrollView) findViewById(R.id.hscroll);
        mScrollViewBackground = (HorizontalScrollView) findViewById(R.id.hscroll_back);
        mBackgroundImage = (ImageView) findViewById(R.id.bg);
//        mBackgroundImage.setLayoutParams(new LinearLayout.LayoutParams((int) (mScreenWidth*1.3), LinearLayout.LayoutParams.MATCH_PARENT));
        int width = (int) (mScreenWidth-40*mDensity)/5;
        int height = (int) (width*4/3 + 40*mDensity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        for (int i = 1; i <= 20; i++) {
        	View view = LayoutInflater.from(MovieListActivity.this).inflate(R.layout.layout_item_grid, null);
        	AsyncImageView image = (AsyncImageView) view.findViewById(R.id.item_image);
        	TextView text = (TextView) view.findViewById(R.id.movie_name);
        	text.setText("电影"+i);
        	image.setUrl(resorce_urls[i%4]);
        	final int posistion  = i;
        	image.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(MovieListActivity.this, "click item " + posistion, Toast.LENGTH_SHORT).show();
				}
			});
        	
			view.setLayoutParams(params);
			mLinearLayout.addView(view);
		}
        mScrollView.setOnScrollChangeListener(new MyOnScrollChangeListener());
        mLinearLayout.requestFocus();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    	public boolean onKeyDown(int keyCode, KeyEvent event) {
    		// TODO Auto-generated method stub
    		Log.d("Main", mScrollView.getScrollX()+"");
    		return super.onKeyDown(keyCode, event);
    	}
    
    class MyOnScrollChangeListener extends OnScrollChangeListener{

    	private int mLastX = 0;
    	private int mLastOldx = 0;
		@Override
		void onScrollChanged(HorizontalScrollView scrollView, int x, int y,
				int oldx, int oldy) {
			// TODO Auto-generated method stub
			int dx = x*(mBackgroundImage.getWidth() - mScreenWidth)/(mLinearLayout.getWidth() - mScreenWidth);
			if(x>mLastX){
				if(mLastOldx<=oldx){
					mScrollViewBackground.smoothScrollTo(dx, mScrollViewBackground.getScrollY());
					Log.d("Main", "x =" + x + "\toldx = " + oldx + "\tdx = " + dx + "\tmLastX = " + mLastX + "\tmLastOldx" + mLastOldx);
					mLastX = x;
					mLastOldx = oldx;
				}
			}else if(x<mLastX){
				if(mLastOldx>=oldx){
					mScrollViewBackground.smoothScrollTo(dx, mScrollViewBackground.getScrollY());
					Log.d("Main", "x =" + x + "\toldx = " + oldx + "\tdx = " + dx + "\tmLastX = " + mLastX + "\tmLastOldx" + mLastOldx);
					mLastX = x;
					mLastOldx = oldx;
				}
			}
		}
    }
    
}
