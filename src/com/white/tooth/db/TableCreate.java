package com.white.tooth.db;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TableCreate {

	public static final String TABLE_LED_CREATE = "CREATE TABLE "
			+ LedConstant.TABLE_NAME + " (" + LedConstant.ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + LedConstant.TIMER_DATE	+ " TEXT, "+LedConstant.TIMER_TIME+" TEXT , "+LedConstant.TIMER_COUNTER+" TEXT)";
	
	public static void  onCreate(SQLiteDatabase db){
		db.beginTransaction();
		Log.i("Table created", "Table created");
		try{
			db.execSQL(TABLE_LED_CREATE);
			db.setTransactionSuccessful();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			db.endTransaction();
		}
		
	}
	
	public static void  onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		Log.w(TableCreate.class.getName(), "Upgrade database from version "+oldVersion+" to "+newVersion+"");
		db.execSQL("DROP TABLE IF EXISTS "+LedConstant.TABLE_NAME);
		onCreate(db);
	}

}
