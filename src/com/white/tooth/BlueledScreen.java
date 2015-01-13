package com.white.tooth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class BlueledScreen extends BaseActivity implements OnClickListener, OnSeekBarChangeListener {

	// private RelativeLayout rl_bg;
	private ImageView iv_btn;
	private boolean flag = false;
	// private ProgressBar progressBar;
	private TextView tv_timer_value;

	private long lastUsed = 0;
	private long idle = 0;
	private int countervalue, countervalue_save;
	// private LinearLayout ll_canvas;
	float curBrightnessValue;
	private RelativeLayout ll_blink;
	protected PowerManager.WakeLock mWakeLock;
	int val = 0;
	private VideoView videoView;
	private Uri uri;
	private AlertDialog.Builder builder1;
	private Bitmap bitmap;
	private ImageShadeDialogTimerEnd imageShadeDlg;
	private String imagename;
	private VerticalSeekBar seekbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imagename = "" + System.currentTimeMillis();
		File folder = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/whitetooth");
		if (!folder.isDirectory()) {
			folder.mkdirs();
		}
		final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		this.mWakeLock.acquire();

		try {
			curBrightnessValue = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		seekbar = (VerticalSeekBar) findViewById(R.id.seekbar);
		seekbar.setMax(255);
		seekbar.setProgress(255-(int) curBrightnessValue);
		seekbar.setOnSeekBarChangeListener(this);
		ll_blink = (RelativeLayout) findViewById(R.id.ll_blink);
		countervalue = getIntent().getExtras().getInt("value");
		// countervalue = 1;
		countervalue_save = countervalue;
		tv_timer_value = (TextView) findViewById(R.id.tv_timer_value);
		tv_timer_value.setText("" + countervalue);

		// rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
		iv_btn = (ImageView) findViewById(R.id.iv_btn);
		// progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		iv_btn.setOnClickListener(this);

		// ll_blink.setBackgroundResource(R.drawable.n_on);
		iv_btn.setImageResource(R.drawable.button_off);
		// ll_canvas = (LinearLayout)findViewById(R.id.ll_canvas);
		// progressBar.setVisibility(View.VISIBLE);

		videoView = (VideoView) findViewById(R.id.videoview);
		uri = Uri.parse("android.resource://com.white.tooth/" + R.raw.led_on);

		playvideo();
		lastUsed = System.currentTimeMillis();
		idle = 0;
		runnable = new Runnable() {
			public void run() {
				Log.e("!!reach here", "reach here");
				Constant.isRunning = true;
				handler.postDelayed(runnable, 1000);
				idle = System.currentTimeMillis() - lastUsed;

				if (val == 60) {
					val = 0;
					countervalue = countervalue - 1;
					updateTimer(countervalue);
				}
				val++;
				if (idle >= (countervalue_save * 1000 * 60)) {
					handler.removeCallbacks(runnable);
					if (videoView.isPlaying()) {
						videoView.stopPlayback();
					}
					videoView.setVisibility(View.INVISIBLE);
					iv_btn.setImageResource(R.drawable.button_on);
					android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, (int) curBrightnessValue);
					new AlertDialog.Builder(BlueledScreen.this).setTitle("Alert").setMessage("Do you want to save this session to history?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							insertValue("" + countervalue_save);
							finish();
							/*
							 * Intent i = new Intent(); startActivity(i);
							 */
							//
							// dialogShow();
						}
					}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							//
							dialog.dismiss();
							finish();
							// dialogShow();
						}
					}).setIcon(android.R.drawable.ic_dialog_alert).show();

					// finish();
				}
				/*
				 * if (idle >= ( 1000 * 30)) {
				 * handler.removeCallbacks(runnable);
				 * insertValue(""+countervalue); }
				 */
			}
		};
		handler.postDelayed(runnable, 1000);
		// android.provider.Settings.System.putInt(getContentResolver(),
		// android.provider.Settings.System.SCREEN_BRIGHTNESS, 255);

		videoView.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				playvideo();

			}
		});

	}

	private void updateTimer(final int remaining_time) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				tv_timer_value.setText("" + remaining_time);
			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	public void insertValue(String counter) {
		String st[] = new SimpleDateFormat("dd-MM-yyyy~hh:mm a").format(new Date()).split("~");
		String _date = st[0];
		String _time = st[1];

		Log.e("!!finish here", "finish here");
		dbAdapter.inserValue(_date, _time, counter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_btn:
			if (flag) {

				flag = false;
				videoView.setVisibility(View.VISIBLE);
				playvideo();

				iv_btn.setImageResource(R.drawable.button_off);
				android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 255);

				/*
				 * builder1 = new AlertDialog.Builder(this);
				 * builder1.setMessage("Write your message here.");
				 * builder1.setCancelable(true);
				 * builder1.setPositiveButton("PAUSE ", new
				 * DialogInterface.OnClickListener() { public void
				 * onClick(DialogInterface dialog, int id) { dialog.cancel();
				 * 
				 * flag = false; videoView.setVisibility(View.VISIBLE);
				 * playvideo();
				 * 
				 * iv_btn.setImageResource(R.drawable.button_off);
				 * android.provider.Settings.System
				 * .putInt(getContentResolver(),
				 * android.provider.Settings.System.SCREEN_BRIGHTNESS, 255); }
				 * }); builder1.setNegativeButton("STOP", new
				 * DialogInterface.OnClickListener() { public void
				 * onClick(DialogInterface dialog, int id) { dialog.cancel();
				 * finish(); } });
				 * 
				 * AlertDialog alert11 = builder1.create(); alert11.show();
				 */

			} else {

				builder1 = new AlertDialog.Builder(this);
				// builder1.setMessage("Write your message here.");
				builder1.setCancelable(true);
				builder1.setPositiveButton("PAUSE ", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

						/*
						 * flag = false; videoView.setVisibility(View.VISIBLE);
						 * playvideo();
						 * 
						 * iv_btn.setImageResource(R.drawable.button_off);
						 * android
						 * .provider.Settings.System.putInt(getContentResolver
						 * (),
						 * android.provider.Settings.System.SCREEN_BRIGHTNESS,
						 * 255);
						 */

						flag = true;

						if (videoView.isPlaying()) {
							videoView.stopPlayback();
						}
						videoView.setVisibility(View.INVISIBLE);
						iv_btn.setImageResource(R.drawable.button_on);
						android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, (int) curBrightnessValue);
					}
				});
				builder1.setNegativeButton("STOP", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						finish();
					}
				});

				AlertDialog alert11 = builder1.create();
				alert11.show();

				/*
				 * flag = true;
				 * 
				 * if (videoView.isPlaying()) { videoView.stopPlayback(); }
				 * videoView.setVisibility(View.INVISIBLE);
				 * iv_btn.setImageResource(R.drawable.button_on);
				 * android.provider.Settings.System.putInt(getContentResolver(),
				 * android.provider.Settings.System.SCREEN_BRIGHTNESS, (int)
				 * curBrightnessValue);
				 */
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		handler.removeCallbacks(runnable);
		finish();
	}

	@Override
	public void onDestroy() {
		this.mWakeLock.release();
		handler.removeCallbacks(runnable);
		super.onDestroy();
	}

	public void playvideo() {
		videoView.setVideoURI(uri);
		MediaController mc = new MediaController(BlueledScreen.this);
		videoView.setMediaController(mc);
		videoView.requestFocus();
		videoView.start();
		mc.setVisibility(View.GONE);
	}

	public void dialogShow() {
		new AlertDialog.Builder(BlueledScreen.this).setTitle("Capture Image").setMessage("Take a picture of your teeth").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				File file = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/whitetooth/" + imagename + ".jpg");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				startActivityForResult(intent, 1);
			}
		}).setNegativeButton("Skip", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();

			}
		}).setIcon(android.R.drawable.ic_dialog_alert).show();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			File file = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/crop_image/" + imagename + ".jpg");
			try {
				cropCapturedImage(Uri.fromFile(file));
			} catch (ActivityNotFoundException aNFE) {
				String errorMessage = "Sorry - your device doesn't support the crop action!";
				Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		if (requestCode == 2 && data != null) {
			try {
				Bundle extras = data.getExtras();
				Bitmap thePic = extras.getParcelable("data");
				// imVCature_pic.setImageBitmap(thePic);
				bitmap = thePic;
				String path = Environment.getExternalStorageDirectory().toString();
				OutputStream fOut = null;
				File file = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/crop_image/" + imagename + ".jpg");
				fOut = new FileOutputStream(file);

				thePic.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
				fOut.flush();
				fOut.close();
				MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
				imageShadeDlg = new ImageShadeDialogTimerEnd(this, bitmap);
				imageShadeDlg.show();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void onShadeClickEnd(int val) {
		finish();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

		android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 255-progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		

	}

}
