package com.white.tooth;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class ImageShadeDialogTimerEnd extends Dialog implements android.view.View.OnClickListener{
	
	public interface OnShadeClickListenerEnd{
		public void onShadeClickEnd(int val);
	}
	
	private BaseActivity base;
	private Bitmap bitmap;
	private ImageView shade_1,shade_2,capture_image;
	private OnShadeClickListenerEnd listener;
	public ImageShadeDialogTimerEnd(BaseActivity base, Bitmap bitmap) {
		super(base);
		this.base = base;
		this.bitmap = bitmap;
		listener = (OnShadeClickListenerEnd) base;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.picture_gallery);
		shade_1 = (ImageView)findViewById(R.id.shade_1);
		shade_2= (ImageView)findViewById(R.id.shade_2);
		capture_image = (ImageView)findViewById(R.id.capture_image);
		setCancelable(true);
		
		capture_image.setImageBitmap(bitmap);
		shade_1.setOnClickListener(this);
		shade_2.setOnClickListener(this);
	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shade_1:
			listener.onShadeClickEnd(1);
			dismiss();
			break;
			
		case R.id.shade_2:
			listener.onShadeClickEnd(2);
			dismiss();
			break;
		}	
	}
}
