package com.white.tooth;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ToothShadeActivity extends BaseActivity {
	private LinearLayout ll_table_pic_btn, ll_camera, ll_shade, ll_shade_sample;
	private Button btn_take_pic, btn_yes, btn_no, btn_save_tooth_shade;
	private int callFrom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tooth_shade);
		ll_table_pic_btn = (LinearLayout) findViewById(R.id.ll_table_pic_btn);
		ll_shade = (LinearLayout) findViewById(R.id.ll_shade);
		ll_camera = (LinearLayout) findViewById(R.id.ll_camera);
		btn_take_pic = (Button) findViewById(R.id.btn_take_pic);
		btn_take_pic.setOnClickListener(this);
		btn_yes = (Button) findViewById(R.id.btn_yes);
		btn_yes.setOnClickListener(this);
		btn_no = (Button) findViewById(R.id.btn_no);
		btn_no.setOnClickListener(this);
		btn_save_tooth_shade = (Button) findViewById(R.id.btn_save_tooth_shade);
		btn_save_tooth_shade.setOnClickListener(this);
		ll_shade_sample = (LinearLayout) findViewById(R.id.ll_shade_sample);
		onLayoutVisibility(1);
		for(int i=0;i<20; i++){
			ll_shade_sample.addView(addtoothShade(i+1));
		}
		
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			callFrom = bundle.getInt("val");
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_take_pic:
			callCamera();
			break;
		case R.id.btn_yes:
			onLayoutVisibility(3);
			break;
		case R.id.btn_no:
			callCamera();
			break;
		case R.id.btn_save_tooth_shade:
			if(callFrom==1){
				if(app.getBeforeSessionShadeValue()!=0){
					Intent returnIntent = new Intent();
			        setResult(RESULT_OK,returnIntent);     
			         finish();
				}	
			}
			
			if(callFrom==2){
				if(app.getAfterSessionShadeValue()!=0){
					Intent returnIntent = new Intent();
			        setResult(RESULT_OK,returnIntent);     
			         finish();
				}	
			}
			
			
			break;

		}
	}

	public void callCamera() {

		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/.wt/" + "img.jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 1) {
			final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/.wt/" + "img.jpg");
			cropCapturedImage(Uri.fromFile(file));

		}
		if (requestCode == 2) {
			onLayoutVisibility(2);
			Bundle extras = data.getExtras();
			Bitmap thePic = extras.getParcelable("data");
			((ImageView) findViewById(R.id.iv_capture_image)).setImageBitmap(thePic);
			((ImageView) findViewById(R.id.iv_shade_image)).setImageBitmap(thePic);
			String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/.wt/";
			File newDir = new File(root);
			newDir.mkdirs();
			String fotoname = "img.jpg";
			File file = new File(newDir, fotoname);
			if (file.exists())
				file.delete();
			try {
				FileOutputStream out = new FileOutputStream(file);
				thePic.compress(Bitmap.CompressFormat.JPEG, 100, out);
				out.flush();
				out.close();

				Log.e("Image Path", Uri.fromFile(file).toString());
				Log.e("Image Path", Uri.fromFile(file).toString());
			} catch (Exception e) {
			}
		}
	}

	public void cropCapturedImage(Uri picUri) {
		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		cropIntent.setDataAndType(picUri, "image/*");
		cropIntent.putExtra("crop", "true");
		cropIntent.putExtra("aspectX", 1);
		cropIntent.putExtra("aspectY", 1);
		cropIntent.putExtra("outputX", 256);
		cropIntent.putExtra("outputY", 256);
		cropIntent.putExtra("return-data", true);
		startActivityForResult(cropIntent, 2);
	}

	public void onLayoutVisibility(int val) {
		switch (val) {
		case 1:
			ll_table_pic_btn.setVisibility(View.VISIBLE);
			ll_camera.setVisibility(View.GONE);
			ll_shade.setVisibility(View.GONE);
			break;
		case 2:
			ll_table_pic_btn.setVisibility(View.GONE);
			ll_camera.setVisibility(View.VISIBLE);
			ll_shade.setVisibility(View.GONE);
			break;
		case 3:
			ll_table_pic_btn.setVisibility(View.GONE);
			ll_camera.setVisibility(View.GONE);
			ll_shade.setVisibility(View.VISIBLE);
			break;
		}
	}

	public View v;
	public View addtoothShade(int i) {
		
		v = View.inflate(getApplicationContext(), R.layout.row_shade, null);
		
		ImageView imageView  = (ImageView)v.findViewById(R.id.iv_shade_sample);
		final TextView tv  = (TextView)v.findViewById(R.id.tv_shade_number);
				
		if(i==0){
			imageView.setImageResource(R.drawable.shade_1);	
			tv.setText("01");
		}else if(i==1){
			imageView.setImageResource(R.drawable.shade_2);	
			tv.setText("02");
		}else if(i==2){
			imageView.setImageResource(R.drawable.shade_3);	
			tv.setText("03");
		}else if(i==3){
			imageView.setImageResource(R.drawable.shade_4);	
			tv.setText("04");
		}else if(i==4){
			imageView.setImageResource(R.drawable.shade_5);	
			tv.setText("05");
		}else if(i==5){
			imageView.setImageResource(R.drawable.shade_6);	
			tv.setText("06");
		}else if(i==6){
			imageView.setImageResource(R.drawable.shade_7);
			tv.setText("07");
		}else if(i==7){
			imageView.setImageResource(R.drawable.shade_8);	
			tv.setText("08");
		}else if(i==8){
			imageView.setImageResource(R.drawable.shade_9);	
			tv.setText("09");
		}else if(i==9){
			imageView.setImageResource(R.drawable.shade_10);
			tv.setText("10");
		}else if(i==10){
			imageView.setImageResource(R.drawable.shade_11);
			tv.setText("11");
		}else if(i==11){
			imageView.setImageResource(R.drawable.shade_12);
			tv.setText("12");
		}else if(i==12){
			imageView.setImageResource(R.drawable.shade_13);
			tv.setText("13");
		}else if(i==13){
			imageView.setImageResource(R.drawable.shade_14);
			tv.setText("14");
		}else if(i==14){
			imageView.setImageResource(R.drawable.shade_15);
			tv.setText("15");
		}else if(i==15){
			imageView.setImageResource(R.drawable.shade_16);
			tv.setText("16");
		}else if(i==16){
			imageView.setImageResource(R.drawable.shade_17);
			tv.setText("17");
		}else if(i==17){
			imageView.setImageResource(R.drawable.shade_18);
			tv.setText("18");
		}else if(i==18){
			imageView.setImageResource(R.drawable.shade_19);
			tv.setText("19");
		}else if(i==19){
			imageView.setImageResource(R.drawable.shade_20);
			tv.setText("20");
		}
		
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				String val = tv.getText().toString().trim();
				System.out.println("!!! "+val);
				app.setBeforeSessionShadeValue(Integer.parseInt(val));
			}
		});
		return v;
		
	}

}
