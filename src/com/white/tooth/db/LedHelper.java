package com.white.tooth.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LedHelper extends SQLiteOpenHelper{

	public LedHelper(Context context) {
		super(context, LedConstant.DATABASE_NAME, null, LedConstant.DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		TableCreate.onCreate(db);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		TableCreate.onUpgrade(db, oldVersion, newVersion);	
		
	}

}
