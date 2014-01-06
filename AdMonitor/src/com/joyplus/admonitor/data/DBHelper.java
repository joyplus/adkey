package com.joyplus.admonitor.data;


import com.joyplus.admonitor.Config.Log;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* Define by Jas@20140103
 * It use to AdMonitor DataBase*/
public class DBHelper extends SQLiteOpenHelper {
	private static final int version = 1;
	public DBHelper(Context context) {
		super(context, "admonitor.db", null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		CreateImpressionDB(db);
	}
    
	private void CreateImpressionDB(SQLiteDatabase db){
		db.execSQL("create table if not exists impression_info(_id integer PRIMARY KEY AUTOINCREMENT,"
				+"mImpressionType char,mDisdlay_num char,mImpressionUrl char,mColumn1 char,mColumn2 char,mColumn3 char,create_date TimeStamp NOT NULL DEFAULT (datetime('now','localtime')))");		
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