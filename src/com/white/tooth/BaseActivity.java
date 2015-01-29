package com.white.tooth;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.flurry.android.FlurryAgent;
import com.white.tooth.ImageShadeDialogTimerEnd.OnShadeClickListenerEnd;
import com.white.tooth.ImageShadeDialogTimerStart.OnShadeClickListenerStart;
import com.white.tooth.application.WhiteToothApp;
import com.white.tooth.db.LedDbAdapter;

public class BaseActivity extends Activity implements OnShadeClickListenerStart,OnShadeClickListenerEnd, OnClickListener {
	public LedDbAdapter dbAdapter;
	public Runnable runnable;
	public Handler handler = new Handler();
	public WhiteToothApp app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbAdapter = LedDbAdapter.createInstance(getApplicationContext());
		app = (WhiteToothApp) getApplication();
		Map<String, String> articleParams = new HashMap<String, String>();
		FlurryAgent.init(this, "7FNYZXT968K4XQ2V37DV");
		FlurryAgent.logEvent("BaseActivity", articleParams, true);
		FlurryAgent.endTimedEvent("BaseActivity");
	}

	public Typeface agecyTypeface() {
		String fontPath = "fonts/3913467301.ttf";
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		return tf;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, "7FNYZXT968K4XQ2V37DV");
		FlurryAgent.setLogEnabled(true);
		FlurryAgent.setLogEvents(true);
		FlurryAgent.setLogLevel(Log.VERBOSE);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	
	public void onShadeClickStart(int val) {}
	public void onShadeClickEnd(int val) {}	
	public void onClick(View v) {}

}
