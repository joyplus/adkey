package com.joyplus.ad.db;

import com.joyplus.ad.config.Log;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* Define by Jas@20131126 
 * It use to AdBoot DataBase*/
public class DBHelper extends SQLiteOpenHelper {
	private static final int version = 1;
	public DBHelper(Context context) {
		super(context, "download.db", null, version);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists impression_info(_id integer PRIMARY KEY AUTOINCREMENT,"
				+"publisher_id char,ad_id char,ad_type char,display_num char,mImpressionUrl char,column1 char,column2 char,create_date TimeStamp NOT NULL DEFAULT (datetime('now','localtime')))");		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		  try {
//			  db.execSQL("alter table download_info add column file_path char");
//			  db.execSQL("alter table video_cache add column comments char");
		  } catch (Exception e) {
			  Log.i("info","onUpgrade fail:"+e.getMessage());
		  } 
	}
}