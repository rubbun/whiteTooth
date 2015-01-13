package com.white.tooth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SmsActivity extends BaseActivity implements OnClickListener {

	// private ImageView iv_preview,iv_attach;
	private EditText et_message;
	// private TextView tv_hint_text;
	private LinearLayout ll_send;
	public Bitmap scaleBitmap;
	public String imagepath = null;
	public Uri selectedImage;
	public String number = "1-978-728-8480";
	public static final int INVITE_COMPLETED = 0x3;
	public static final int REQUEST_CODE_TAKE_PICTURE = 0x4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms);

		et_message = (EditText) findViewById(R.id.et_message);

		ll_send = (LinearLayout) findViewById(R.id.ll_send);
		ll_send.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_send:
			if (et_message.getText().toString().trim().length() > 0) {
				Intent smsIntent = new Intent(Intent.ACTION_VIEW);
				smsIntent.setData(Uri.parse("smsto:"));
				smsIntent.setType("vnd.android-dir/mms-sms");

				//smsIntent.putExtra(Intent.EXTRA_STREAM, selectedImage);
				// smsIntent.setType("image/*");
				smsIntent.putExtra("address", new String(number));
				smsIntent.putExtra("sms_body", et_message.getText().toString());
				try {
					startActivity(smsIntent);

					Log.i("Finished sending SMS...", "");
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(getApplicationContext(),
							"SMS faild, please try again later.",
							Toast.LENGTH_SHORT).show();
				}
			}

			break;

		}
	}

}
