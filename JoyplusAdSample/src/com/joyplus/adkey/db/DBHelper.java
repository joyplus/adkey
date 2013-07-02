package com.joyplus.adkey.db;



import com.joyplus.adkey.widget.Log;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 建立一个数据库帮助类
 */
public class DBHelper extends SQLiteOpenHelper {
	private static final int version = 1; //数据库版本,默认为1
	public DBHelper(Context context) {
		super(context, "download.db", null, version);
	}

	/**
	 * 在download.db数据库下创建一个download_info表存储下载信息
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
//		db.execSQL("create table if not exists impression(_id integer PRIMARY KEY AUTOINCREMENT,"
//				+"publisher_id char,ad_id char,ad_type char,display_num char,column1 char,column2 char,column3 char,create_date TimeStamp NOT NULL DEFAULT (datetime('now','localtime')))");
//		db.execSQL("create table if not exists screen_info(_id integer PRIMARY KEY AUTOINCREMENT,  "
//				+ "url char,publishid char,type char,number1 char, number2 char, number 3,create_date TimeStamp NOT NULL DEFAULT (datetime('now','localtime')))");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		  try {
//			  db.execSQL("alter table download_info add column file_path char");
//			  db.execSQL("alter table video_cache add column comments char");
		  } catch (Exception e) {
			  Log.i("info","异常——————————>"+e.getMessage());
		  } 
	}
}