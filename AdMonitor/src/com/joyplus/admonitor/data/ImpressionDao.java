package com.joyplus.admonitor.data;

import java.util.ArrayList;
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
	
    private String GetString(String s){
    	if(s == null)return "";
    	return s.trim();
    }
    private boolean GetNumber(String s){
    	if(s == null || "".equals(s))return false;
    	try{
    		Integer.parseInt(s);
    		return true;
    	} catch( NumberFormatException e){
    		return false;
    	}
    }
    
	public synchronized void InsertOneInfo(ImpressionInfo info) {
		if(info == null || info.getImpressionUrl() == null || "".equals(info.getImpressionUrl()))return;
		if(info.getImpressionType() == null || info.getImpressionType() == ImpressionType.Unknow || GetNumber(info.getDisplay_num()))return;
		SQLiteDatabase database = getConnection();
		try {
			String sql = "insert into impression_info(mImpressionType,mDisdlay_num, mImpressionUrl,mColumn1,mColumn2,mColumn3) values (?,?,?,?,?,?)";
			Object[] bindArgs = {GetString(info.getImpressionType().toString()),
					             info.getDisplay_num(),info.getImpressionUrl(),GetString(info.getColumn1())
					             ,GetString(info.getColumn2()),GetString(info.getColumn3())};
			database.execSQL(sql, bindArgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}
	
	public synchronized ImpressionInfo getOneInfo(String ImpressionType,String ImpressionUrl) {
		if(ImpressionType == null || "".equals(ImpressionType))return null;
		if(ImpressionUrl == null || "".equals(ImpressionUrl))return null;
		ImpressionInfo info = null;
		SQLiteDatabase database = getConnection();
		Cursor cursor = null;
		try {
			String sql = "select mImpressionType,mDisdlay_num, mImpressionUrl,mColumn1,mColumn2,mColumn3,create_date from impression_info where mImpressionType=? and mImpressionUrl=?";
			cursor = database.rawQuery(sql, new String[] { ImpressionType,ImpressionUrl });
			while (cursor.moveToNext()) {
				info = new ImpressionInfo();
				info.setImpressionType(Type.toImpressionType(cursor.getString(0)));
				info.setDisplay_num(cursor.getString(1));
				info.setImpressionUrl(cursor.getString(2));
				info.setColumn1(cursor.getString(3));
				info.setColumn2(cursor.getString(4));
				info.setColumn3(cursor.getString(5));
				return info;
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
		ArrayList<ImpressionInfo> infos = new ArrayList<ImpressionInfo>();
		SQLiteDatabase database = getConnection();
		Cursor cursor = null;
		try {
			String sql = "select * from impression_info ";
			cursor = database.rawQuery(sql, new String[] {});
			while (cursor.moveToNext()) {
				ImpressionInfo info = new ImpressionInfo();
				info.setImpressionType(Type.toImpressionType(cursor.getString(1)));
				info.setDisplay_num(cursor.getString(2));
				info.setImpressionUrl(cursor.getString(3));
				info.setColumn1(cursor.getString(4));
				info.setColumn2(cursor.getString(5));
				info.setColumn3(cursor.getString(6));
				infos.add(info);
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
		return infos;
	}
	
	public synchronized void updataInfos(String mImpressionType,String mImpressionUrl,
			int display_num) {
		if(mImpressionType == null || "".equals(mImpressionType.toString()))return;
		if(mImpressionUrl == null || "".equals(mImpressionUrl))return;
		SQLiteDatabase database = getConnection();
		try {
			String sql = "update impression_info set mDisdlay_num=? where mImpressionType=? and mImpressionUrl=?";
			Object[] bindArgs = { display_num+"", mImpressionType,mImpressionUrl};
			database.execSQL(sql, bindArgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}
	

	public synchronized void delete(String mImpressionType, String mImpressionUrl) {
		if(mImpressionType == null || "".equals(mImpressionType))return;
		if(mImpressionUrl == null || "".equals(mImpressionUrl))return;
		SQLiteDatabase database = getConnection();
		try {
			database.delete("impression_info", "mImpressionType=? and mImpressionUrl=?",
					new String[] { mImpressionType, mImpressionUrl });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}
}