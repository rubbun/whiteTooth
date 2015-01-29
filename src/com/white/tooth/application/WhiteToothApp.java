package com.white.tooth.application;

import android.app.Application;

public class WhiteToothApp extends Application{
	
	public int beforeSessionShadeValue=0;
	public int afterSessionShadeValue=0;
	public boolean isShadeActive = false;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	public int getBeforeSessionShadeValue() {
		return beforeSessionShadeValue;
	}

	public void setBeforeSessionShadeValue(int beforeSessionShadeValue) {
		this.beforeSessionShadeValue = beforeSessionShadeValue;
	}

	public int getAfterSessionShadeValue() {
		return afterSessionShadeValue;
	}

	public void setAfterSessionShadeValue(int afterSessionShadeValue) {
		this.afterSessionShadeValue = afterSessionShadeValue;
	}

	public boolean isShadeActive() {
		return isShadeActive;
	}

	public void setShadeActive(boolean isShadeActive) {
		this.isShadeActive = isShadeActive;
	}
	
	

}
