package com.joyplus.ad.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AdBootDao {
    private final static boolean DEBUG = false;
	public  static final int MAX = 5;
	private static AdBootDao dao = null;
	private Context context;
	private AdBootDao(Context context) {
		this.context = context;
	}
	public static AdBootDao getInstance(Context context) {
		if (dao == null) {
			dao = new AdBootDao(context);
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
	
	public synchronized boolean InsertOneInfo(AdBootImpressionInfo info) {
		if(DEBUG)Log.d("Jas","InsertOneInfo-->"+info.toString());
		ArrayList<AdBootImpressionInfo> Info = GetAllReport();
		SQLiteDatabase database = getConnection();
		try {
			if(Info!= null && Info.size()>=MAX){
				database.delete("adbootReport_info", "_id=?", new String[]{""+Info.get(0)._ID});
			}
			ContentValues Value = AdBootImpressionInfo.GetContentValues(info);
			if(info != null){
				return database.insert("adbootReport_info", null, Value)>0;
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
	public synchronized AdBootImpressionInfo GetFirst(){
		SQLiteDatabase database = getConnection();
		Cursor         cursor   = null;
		try{
			cursor = database.rawQuery("select * from adbootReport_info order by _id ASC limit 1", null);
			if(cursor.moveToFirst()){
				if(cursor.getCount()>0){
					database.delete("adbootReport_info", "_id=?", new String[]{cursor.getString(cursor.getColumnIndex("_id"))});
					return AdBootImpressionInfo.GetAdBootImpressionInfo(cursor);
				}
			}
		}catch(Exception e){}
		finally{
			if(cursor != null)cursor.close();
			if(database!=null)database.close();
		}
		delAll();//for nothing return
		return null;
	}
	public synchronized AdBootImpressionInfo GetLast(){
		SQLiteDatabase database = getConnection();
		Cursor         cursor   = null;
		try{
			cursor = database.rawQuery("select * from adbootReport_info order by _id desc limit 1", null);
			if(cursor.moveToFirst()){
				if(cursor.getCount()>0){
					database.delete("adbootReport_info", "_id=?", new String[]{cursor.getString(cursor.getColumnIndex("_id"))});
					return AdBootImpressionInfo.GetAdBootImpressionInfo(cursor);
				}
			}
		}catch(Exception e){}
		finally{
			if(cursor != null)cursor.close();
			if(database!=null)database.close();
		}
		delAll();//for nothing return
		return null;
	}
	public synchronized ArrayList<AdBootImpressionInfo> GetAllReport(){
		if(DEBUG)Log.d("Jas","GetAllReport");
		SQLiteDatabase database = getConnection();
		Cursor         cursor   = null;
		try{
			cursor = database.rawQuery("select * from adbootReport_info order by _id ASC", null);
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
	public synchronized ArrayList<AdBootImpressionInfo> GetAllReport(String publisherID){
		if(DEBUG)Log.d("Jas","GetAllReport publisherID="+publisherID);
		SQLiteDatabase database = getConnection();
		Cursor         cursor   = null;
		try{
			cursor = database.rawQuery("select * from adbootReport_info order by _id ASC", null);
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
			database.execSQL("delete from adbootReport_info");
		}catch(Exception e){}
		finally{
			if(database != null)database.close();
		}
	}
}
