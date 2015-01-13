package com.white.tooth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TimerActivity extends BaseActivity implements OnClickListener {
	private LinearLayout ll_plus, ll_minus;
	private Button btn_save;
	private TextView tv_counter_value;
	public int counter_value = 00;
	public static final int CAMERA_CAPTURE = 100;
	private Bitmap bitmap;
	private ImageShadeDialogTimerStart imageShadeDlg;
	private String imagename;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timer);
		imagename = ""+System.currentTimeMillis();
		
		File folder = new File(Environment.getExternalStorageDirectory().toString()+"/DCIM/whitetooth");
		if(!folder.isDirectory()){
			folder.mkdirs();
		}

		btn_save = (Button) findViewById(R.id.btn_save);
		btn_save.setTypeface(agecyTypeface());

		tv_counter_value = (TextView) findViewById(R.id.tv_timer_value);

		ll_plus = (LinearLayout) findViewById(R.id.ll_plus);
		ll_plus.setOnClickListener(this);

		ll_minus = (LinearLayout) findViewById(R.id.ll_minus);
		ll_minus.setOnClickListener(this);

		btn_save.setOnClickListener(this);

		counter_value = Integer.parseInt(tv_counter_value.getText().toString()
				.trim());
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_plus:
			if (counter_value < 20) {
				counter_value = counter_value + 5;
				tv_counter_value.setText("" + counter_value);
			}

			break;

		case R.id.ll_minus:
			if (counter_value > 10) {
				counter_value = counter_value - 5;
				tv_counter_value.setText("" + counter_value);
			}
			break;
		case R.id.btn_save:

			/*new AlertDialog.Builder(TimerActivity.this)
					.setTitle("Capture Image")
					.setMessage("Take a picture of your teeth")
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
									File file = new File(Environment.getExternalStorageDirectory().toString()+"/DCIM/whitetooth/" +imagename+".jpg");
									intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
									startActivityForResult(intent, 1);
								}
							})
					.setNegativeButton("Skip",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									if (counter_value > 0) {
										Intent i = new Intent(TimerActivity.this, BlueledScreen.class);
										i.putExtra("value", counter_value);
										startActivity(i);
										finish();
									} else {
										Toast.makeText(getApplicationContext(),
												"Please select a valid timer value", 5000).show();
									}
								}
							}).setIcon(android.R.drawable.ic_dialog_alert)
					.show();*/
//"for best results set brightness to high"
			new AlertDialog.Builder(TimerActivity.this)
			.setMessage("For best results set brightness to high")
			.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if (counter_value > 0) {
						Intent i = new Intent(TimerActivity.this, BlueledScreen.class);
						i.putExtra("value", counter_value);
						startActivity(i);
						finish();
					} else {
						Toast.makeText(getApplicationContext(),
								"Please select a valid timer value", 5000).show();
					}
				}
			})
			.show();
			break;
		}
	}
	
	public void cropCapturedImage(Uri picUri){
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1){
			File file = new File(Environment.getExternalStorageDirectory().toString()+"/DCIM/whitetooth/" + imagename+".jpg");
			try {
				cropCapturedImage(Uri.fromFile(file));
			}
			catch(ActivityNotFoundException aNFE){
				String errorMessage = "Sorry - your device doesn't support the crop action!";
				Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		if(requestCode==2 && data !=null){
			try {
				Bundle extras = data.getExtras();
				Bitmap thePic = extras.getParcelable("data");
				//imVCature_pic.setImageBitmap(thePic);
				bitmap = thePic;
				String path = Environment.getExternalStorageDirectory().toString();
				OutputStream fOut = null;
				File file = new File(Environment.getExternalStorageDirectory().toString()+"/DCIM/crop_image/" + imagename+".jpg");
				fOut = new FileOutputStream(file);

				thePic.compress(Bitmap.CompressFormat.JPEG, 85, fOut); 
				fOut.flush();
				fOut.close(); 
				MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
				imageShadeDlg = new ImageShadeDialogTimerStart(this, bitmap);
				imageShadeDlg.show();
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	public void onShadeClickStart(int val) {	
		if (counter_value > 0) {
			Intent i = new Intent(TimerActivity.this, BlueledScreen.class);
			i.putExtra("value", counter_value);
			startActivity(i);
			finish();
		} else {
			Toast.makeText(getApplicationContext(),
					"Please select a valid timer value", 5000).show();
		}
	}	
}
