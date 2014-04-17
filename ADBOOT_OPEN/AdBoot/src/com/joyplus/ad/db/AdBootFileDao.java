package com.joyplus.ad.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class AdBootFileDao {
	private final static boolean DEBUG = false;
	private static AdBootFileDao dao = null;
	private Context context;
	private AdBootFileDao(Context context) {
		this.context = context;
	}
	public static AdBootFileDao getInstance(Context context) {
		if (dao == null) {
			dao = new AdBootFileDao(context);
		}
		return dao;
	}
	public SQLiteDatabase getConnection() {
		SQLiteDatabase sqliteDatabase = null;
		try {
			sqliteDatabase = new DBHelper(context).getReadableDatabase();
		} catch (Exception e) {
		}
		return sqliteDatabase;
	}
	
	
	
}
