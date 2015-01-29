package com.white.tooth.dlg;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.white.tooth.BaseActivity;
import com.white.tooth.R;



public class DlgToothShadeCompare extends Dialog implements OnClickListener{
	
	private BaseActivity base;
	private Button btn_ok;
	public OnToothShadeCompareDialogClickListener listener;
	public interface OnToothShadeCompareDialogClickListener{
		public void onOkButtonClick();
		
	}

	public DlgToothShadeCompare(BaseActivity base, OnToothShadeCompareDialogClickListener l) {
		super(base);
		this.base = base;
		listener = l;
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dlg_tooth_shade_compare);
		((TextView)findViewById(R.id.tv_before_number)).setText(""+base.app.getBeforeSessionShadeValue());
		((TextView)findViewById(R.id.tv_after_number)).setText(""+base.app.getAfterSessionShadeValue());
		
		String st = ""+(base.app.getAfterSessionShadeValue()- base.app.getBeforeSessionShadeValue());
		st = "You improved by  "+st+" Shades";
		((TextView)findViewById(R.id.tv_diff)).setText(st);
		
		
		btn_ok = (Button)findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			dismiss();
			listener.onOkButtonClick();
			
			break;

		
		}
		
	}

}

