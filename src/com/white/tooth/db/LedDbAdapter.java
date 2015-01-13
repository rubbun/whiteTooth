package com.white.tooth.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class LedDbAdapter {
	
	public static Context sContext;
	public static SQLiteDatabase sDb;
	public static LedHelper mHelper;
	public static LedDbAdapter sInstance;
	
	
	private LedDbAdapter(Context sContext) {
		LedDbAdapter.sContext = sContext;
	}
	
	public static LedDbAdapter createInstance(Context sContext){
		if(sInstance == null){
			sInstance = new LedDbAdapter(sContext);
			open();
		}
		
		return sInstance;
	}

	private static void open() {
		mHelper  = new LedHelper(sContext);
		sDb = mHelper.getWritableDatabase();
		
	}
	private  void close() {
		mHelper.close();
	}
	
	public long inserValue(String leddate, String ledtime, String ledcounter){
	  ContentValues values = new ContentValues();
	  values.put(LedConstant.TIMER_DATE, leddate);
	  values.put(LedConstant.TIMER_TIME, ledtime);
	  values.put(LedConstant.TIMER_COUNTER, ledcounter);
	  
	  try{
		  sDb.beginTransaction();
		  final long state   = sDb.insert(LedConstant.TABLE_NAME, null, values);
		  sDb.setTransactionSuccessful();
		  return state;
	  }catch(SQLException e){
		  throw e;
	  }finally{
		  sDb.endTransaction();
	  }
	  
	}
	
	public ArrayList<String> getInfo(){
		ArrayList<String> arr = new  ArrayList<String>();
		
		Cursor cursor = sDb.rawQuery("select * from " +LedConstant.TABLE_NAME , null);		
		if(cursor.getCount()>0){
			Log.e("reach here", "reach here");
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				arr.add(cursor.getString(1)+"~"+cursor.getString(2)+"~"+cursor.getString(3)+"~"+cursor.getString(0));
				cursor.moveToNext();
			}
		}
		cursor.close();
		return arr;
	}
	
	public boolean deleteRow(String id) 
	{
	    return sDb.delete(LedConstant.TABLE_NAME, LedConstant.ID + "=" + id, null) > 0;
	}

}
