package com.joyplus.ad.db;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AdBootTempDao {

	private final static boolean DEBUG = false;
	private static AdBootTempDao dao = null;
	private Context context;
	private AdBootTempDao(Context context) {
		this.context = context;
	}
	public static AdBootTempDao getInstance(Context context) {
		if (dao == null) {
			dao = new AdBootTempDao(context);
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
	public synchronized void Remove(String publisherId){
		if(DEBUG)Log.d("Jas","Remove-->"+publisherId);
		if(publisherId == null || "".equals(publisherId))return;
		SQLiteDatabase database = getConnection();
		try {
			database.delete("adbootTemp_info", "publisher_id=?", new String[]{publisherId});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}
	public synchronized boolean UpdateOneInfo(AdBootImpressionInfo info) {
		if(DEBUG)Log.d("Jas","UpdateOneInfo -->"+(info==null?"":info.toString()));
		if(info==null || !info.IsAviable())return false;
		SQLiteDatabase database = getConnection();
		try {
			ContentValues Value = AdBootImpressionInfo.GetContentValues(info);
			if(info != null){
				return database.update("adbootTemp_info", Value, null, null)>0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
		return false;
	}
	public synchronized boolean InsertOneInfo(AdBootImpressionInfo info) {
		if(DEBUG)Log.d("Jas","InsertOneInfo -->"+(info==null?"":info.toString()));
		if(info==null || !info.IsAviable())return false;
		SQLiteDatabase database = getConnection();
		try {
			database.delete("adbootTemp_info", "publisher_id=?", new String[]{info.publisher_id});
			ContentValues Value = AdBootImpressionInfo.GetContentValues(info);
			if(info != null){
				return database.insert("adbootTemp_info", null, Value)>0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
		return false;
	}
	
	public synchronized ArrayList<AdBootImpressionInfo> GetAllTemp(){
		if(DEBUG)Log.d("Jas","GetAllTemp");
		SQLiteDatabase database = getConnection();
		Cursor cursor = null;
		try{
			cursor = database.rawQuery("select * from adbootTemp_info order by _id ASC", null);
			ArrayList<AdBootImpressionInfo> Info = new ArrayList<AdBootImpressionInfo>();
			if(cursor.moveToFirst()){
				Info.add(AdBootImpressionInfo.GetAdBootImpressionInfo(cursor));
				while(cursor.moveToNext()){
					Info.add(AdBootImpressionInfo.GetAdBootImpressionInfo(cursor));
				}
				return Info;
			}
		}catch(Exception e){}
		finally{
			if(cursor != null)cursor.close();
			if(database!=null)database.close();
		}
		return null;
	}
	 
	public void delAll(){
		if(DEBUG)Log.d("Jas","delAll");
		SQLiteDatabase database = getConnection();
		try{
			database.execSQL("delete from adbootTemp_info");
		}catch(Exception e){}
		finally{
			if(database != null)database.close();
		}
	}
	
	
}
