package com.joyplus.adkey.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.joyplus.adkey.data.ImpressionInfo;

/**
 * 
 * 一个业务类
 */
public class ImpressionDao {
	private static ImpressionDao dao = null;
	private Context context;

	private ImpressionDao(Context context) {
		this.context = context;
	}

	public static ImpressionDao getInstance(Context context) {
		if (dao == null) {
			dao = new ImpressionDao(context);
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
	
	/*
	 * 插入一条记录
	 */
	public synchronized void InsertOneInfo(ImpressionInfo info) {
		SQLiteDatabase database = getConnection();
		try {
			String sql = "insert into impression_info(publisher_id,ad_id, ad_type,display_num,column1,column2,column3) values (?,?,?,?,?,?,?)";
			Object[] bindArgs = {info.getPublisher_id(),info.getAd_id(),info.getAd_type()
					,info.getDisplay_num(),info.getColumn1(),info.getColumn2(),info.getColumn3()};
			database.execSQL(sql, bindArgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}
	
	public synchronized ImpressionInfo getOneInfo(String publisher_id, String ad_id) {
		ImpressionInfo info = null;
		SQLiteDatabase database = getConnection();
		Cursor cursor = null;
		try {
			String sql = "select publisher_id,ad_id, ad_type,display_num,column1,column2,column3,create_date from impression_info where publisher_id=? and ad_id=?";
			cursor = database.rawQuery(sql, new String[] { publisher_id, ad_id });
			while (cursor.moveToNext()) {
				info = new ImpressionInfo(cursor.getString(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
			if (null != cursor) {
				cursor.close();
			}
		}
		return info;
	}

	/*
	 * 更新某一个下载记录下载了多少
	 */
	public synchronized void updataInfos(String publisher_id, String ad_id,
			int display_num) {
		SQLiteDatabase database = getConnection();
		try {
			String sql = "update impression_info set display_num=? where publisher_id=? and ad_id=?";
			Object[] bindArgs = { display_num+"", publisher_id, ad_id};
			database.execSQL(sql, bindArgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}
	

	/*
	 * 删除某一个记录
	 */
	public synchronized void delete(String publisher_id, String ad_id) {
		SQLiteDatabase database = getConnection();
		try {
			database.delete("impression_info", "publisher_id=? and ad_id=?",
					new String[] { publisher_id, ad_id });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}
}