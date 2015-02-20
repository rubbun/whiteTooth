package com.white.tooth;

import java.io.File;
import java.io.FileOutputStream;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.white.tooth.dlg.DlgToothShadeCompare;
import com.white.tooth.dlg.DlgToothShadeCompare.OnToothShadeCompareDialogClickListener;

public class ToothShadeActivity extends BaseActivity implements OnToothShadeCompareDialogClickListener {
	private LinearLayout ll_table_pic_btn, ll_camera, ll_shade, ll_shade_sample,ll_shade_row;
	private Button btn_take_pic, btn_yes, btn_no, btn_save_tooth_shade,btn_toggle;
	private int callFrom;
	private int whichLayout;
	private boolean flag = false;
	private HorizontalScrollView hs_view;
	private ImageView iv_drag_image;
	private RelativeLayout rl_drag;
	int mLayoutLeft,mLayoutTop,mLayoutRight,mLayoutBottom; 
	int mLayoutWidth,mLayoutHeight;
	boolean TOUCH_STATUS=false;
	 private static final String IMAGEVIEW_TAG = "Android Logo";
	 String msg="";
	 private int status=0;

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
		btn_toggle = (Button)findViewById(R.id.btn_toggle);
		ll_shade_row = (LinearLayout)findViewById(R.id.ll_shade_row);
		btn_toggle.setOnClickListener(this);
		hs_view = (HorizontalScrollView)findViewById(R.id.hs_view);
		onLayoutVisibility(1);
		whichLayout = 1;
		for(int i=0;i<15; i++){
			ll_shade_sample.addView(addtoothShade(i));
		}
		
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			callFrom = bundle.getInt("val");
		}
		iv_drag_image = (ImageView)findViewById(R.id.iv_drag_image);
		iv_drag_image.setTag(IMAGEVIEW_TAG);
		rl_drag =(RelativeLayout)findViewById(R.id.rl_drag);
		
	
		


	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_take_pic:
			callCamera();
			break;
		case R.id.btn_yes:
			onLayoutVisibility(3);
			whichLayout = 3;
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
				}else{
					Toast.makeText(getApplicationContext(), "Please select a shade", Toast.LENGTH_LONG).show();
				}
			}
			
			if(callFrom==2){
				if(app.getAfterSessionShadeValue()!=0){
					
					if(app.getBeforeSessionShadeValue()>app.getAfterSessionShadeValue()){
						new DlgToothShadeCompare(this, this).show();
					}
					
					
					/*Intent returnIntent = new Intent();
			        setResult(RESULT_OK,returnIntent);     
			         finish();*/
				}else{
					Toast.makeText(getApplicationContext(), "Please select a shade", Toast.LENGTH_LONG).show();
				}	
			}
			
			
			break;
		case R.id.btn_toggle:
			toggleShade();
			break;

		}
	}

	public void callCamera() {

		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/" + "img.jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 1) {
			final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/" + "img.jpg");
			cropCapturedImage(Uri.fromFile(file));

		}
		if (requestCode == 2 && data!=null) {
			onLayoutVisibility(2);
			whichLayout = 2;
			Bundle extras = data.getExtras();
			Bitmap thePic = extras.getParcelable("data");
			((ImageView) findViewById(R.id.iv_capture_image)).setImageBitmap(thePic);
			((ImageView) findViewById(R.id.iv_shade_image)).setImageBitmap(thePic);
			String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/";
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
		
		final ImageView imageView  = (ImageView)v.findViewById(R.id.iv_shade_sample);
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
		}
		
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
				
				int val = Integer.parseInt(tv.getText().toString().trim());
				System.out.println("!!! "+val);
				if(callFrom == 1){
					app.setBeforeSessionShadeValue(val);
				}else{
					app.setAfterSessionShadeValue(val);
				}
				
				val = val-1;
				
				if(val==0){
					iv_drag_image.setImageResource(R.drawable.shade_1);	
					
				}else if(val==1){
					iv_drag_image.setImageResource(R.drawable.shade_2);	
					
				}else if(val==2){
					iv_drag_image.setImageResource(R.drawable.shade_3);	
					
				}else if(val==3){
					iv_drag_image.setImageResource(R.drawable.shade_4);	
					
				}else if(val==4){
					iv_drag_image.setImageResource(R.drawable.shade_5);	
					
				}else if(val==5){
					iv_drag_image.setImageResource(R.drawable.shade_6);	
					
				}else if(val==6){
					iv_drag_image.setImageResource(R.drawable.shade_7);
					
				}else if(val==7){
					iv_drag_image.setImageResource(R.drawable.shade_8);	
					
				}else if(val==8){
					iv_drag_image.setImageResource(R.drawable.shade_9);	
					
				}else if(val==9){
					iv_drag_image.setImageResource(R.drawable.shade_10);
					
				}else if(val==10){
					iv_drag_image.setImageResource(R.drawable.shade_11);
					
				}else if(val==11){
					iv_drag_image.setImageResource(R.drawable.shade_12);
					
				}else if(val==12){
					iv_drag_image.setImageResource(R.drawable.shade_13);
					
				}else if(val==13){
					iv_drag_image.setImageResource(R.drawable.shade_14);
					
				}else if(val==14){
					iv_drag_image.setImageResource(R.drawable.shade_15);
					
				}
				String m = "";
				if(val+1 > 5){
					m = "You should whiten teeth";
					
				}else {
					m = "No whiten needed";
				}
				 new AlertDialog.Builder(ToothShadeActivity.this)
					.setMessage(m)
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							arg0.dismiss();
							
						}
					})
					.show();
				
			}
		});
		return v;
		
	}

	@Override
	public void onOkButtonClick() {
		Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);     
         finish();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		onLayoutVisibility(whichLayout);
		
	}
	
	public void toggleShade(){
		/*if(!flag){
			System.out.println("!! here1");
			flag = true;
			hs_view.setVisibility(View.GONE);
			btn_toggle.setText("Show");
		}else{
			System.out.println("!! here2");
			flag = false;
			hs_view.setVisibility(View.VISIBLE);
			btn_toggle.setText("Hide");
		}*/
	}
	

}
