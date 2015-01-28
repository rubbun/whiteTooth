package com.white.tooth.dlg;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.white.tooth.BaseActivity;
import com.white.tooth.R;



public class DlgToothShade extends Dialog implements OnClickListener{
	
	private BaseActivity base;
	private Button btn_yes,btn_no;
	public OnToothShadeDialogClickListener listener;
	public interface OnToothShadeDialogClickListener{
		public void onYesButtonClick();
		public void onNoButtonClick();
	}

	public DlgToothShade(BaseActivity base, OnToothShadeDialogClickListener l) {
		super(base);
		this.base = base;
		listener = l;
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dlg_tooth_shade);
		btn_yes = (Button)findViewById(R.id.btn_yes);
		btn_no = (Button)findViewById(R.id.btn_no);
		btn_yes.setOnClickListener(this);
		btn_no.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_yes:
			dismiss();
			listener.onYesButtonClick();
			
			break;

		case R.id.btn_no:
			dismiss();
			listener.onNoButtonClick();
			break;
		}
		
	}

}

