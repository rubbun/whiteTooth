package com.white.tooth;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;

import com.white.tooth.ImageShadeDialogTimerEnd.OnShadeClickListenerEnd;
import com.white.tooth.ImageShadeDialogTimerStart.OnShadeClickListenerStart;
import com.white.tooth.db.LedDbAdapter;

public class BaseActivity extends Activity implements OnShadeClickListenerStart,OnShadeClickListenerEnd {
	public LedDbAdapter dbAdapter;
	public Runnable runnable;
	public Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbAdapter = LedDbAdapter.createInstance(getApplicationContext());
	}

	public Typeface agecyTypeface() {
		String fontPath = "fonts/3913467301.ttf";
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		return tf;
	}

	
	public void onShadeClickStart(int val) {}
	public void onShadeClickEnd(int val) {}

}
