package com.joyplus.ad.db;

import java.util.ArrayList;
import com.joyplus.ad.data.ImpressionInfo;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/* Define by Jas@20131126
 * It use to AdBoot DataBase*/
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
	
    
	public synchronized void InsertOneInfo(ImpressionInfo info) {
		SQLiteDatabase database = getConnection();
		try {
			String sql = "insert into impression_info(publisher_id,ad_id, ad_type,display_num,mImpressionUrl,column1,column2) values (?,?,?,?,?,?,?)";
			Object[] bindArgs = {info.getPublisher_id(),info.getAd_id(),info.getAd_type()
					,info.getDisplay_num(),info.getImpressionUrl(),info.getColumn1(),info.getColumn2()};
			database.execSQL(sql, bindArgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}
	
	public synchronized ImpressionInfo getOneInfo(String publisherId,String mImpressionUrl) {
		ImpressionInfo info = null;
		SQLiteDatabase database = getConnection();
		Cursor cursor = null;
		try {
			String sql = "select publisher_id,ad_id, ad_type,display_num,mImpressionUrl,column1,column2,create_date from impression_info where publisher_id=? and mImpressionUrl=?";
			cursor = database.rawQuery(sql, new String[] { publisherId,mImpressionUrl });
			while (cursor.moveToNext()) {
				info = new ImpressionInfo(cursor.getString(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3),cursor.getString(4));
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
    
	public synchronized ArrayList<ImpressionInfo> getAllInfo() {
		ArrayList<ImpressionInfo> info = new ArrayList<ImpressionInfo>();
		SQLiteDatabase database = getConnection();
		Cursor cursor = null;
		try {
			String sql = "select * from impression_info ";
			cursor = database.rawQuery(sql, new String[] {});
			while (cursor.moveToNext()) {
				info.add(new ImpressionInfo(cursor.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4),cursor.getString(5)));
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
	
	public synchronized void updataInfos(String publisher_id,String mImpressionUrl,
			int display_num) {
		SQLiteDatabase database = getConnection();
		try {
			String sql = "update impression_info set display_num=? where publisher_id=? and mImpressionUrl=?";
			Object[] bindArgs = { display_num+"", publisher_id,mImpressionUrl};
			database.execSQL(sql, bindArgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}
	

	public synchronized void delete(String publisher_id, String mImpressionUrl) {
		SQLiteDatabase database = getConnection();
		try {
			database.delete("impression_info", "publisher_id=? and mImpressionUrl=?",
					new String[] { publisher_id, mImpressionUrl });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}
}