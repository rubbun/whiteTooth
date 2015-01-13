package com.white.tooth;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Tutorial extends BaseActivity {
	private LinearLayout ll_bg, ll_next;
	private TextView tv_text,tv_next;
	private int i = 2;
	//You’ll want to use a room where you can dim the lights. It is important you don’t drown out the blue light from your phone during the session.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);
		ll_bg = (LinearLayout) findViewById(R.id.ll_bg);
		tv_text = (TextView) findViewById(R.id.tv_text);
		
		tv_next = (TextView)findViewById(R.id.tv_next);
		tv_next.setTypeface(agecyTypeface());

		ll_next = (LinearLayout) findViewById(R.id.ll_next);
		ll_next.setVisibility(View.VISIBLE);
		ll_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(tv_next.getText().equals("Exit")){
					finish();
				}
				i = i +1;
				updateUi(i);
			}

		});
		
		updateUi(i);
	}
	private void updateUi(int i) {
		if(i == 1){
			ll_bg.setBackgroundResource(R.drawable.new_tutorial_1);
			String styledText = "<font color='#189bf6'>Things you'll need.</font><br/>Few cups of water to rinse and spit (unless you are nearby a sink) and napkins or paper towels to wipe..";
			tv_text.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
			
		}else if(i == 2){
			ll_bg.setBackgroundResource(R.drawable.very_first);
			tv_text.setText(R.string.new_tutorial_2);
		}else if(i == 3){
			ll_bg.setBackgroundResource(R.drawable.tutorial_1);
			tv_text.setText(R.string.tutorial1);
		}else if(i == 4){
			ll_bg.setBackgroundResource(R.drawable.tutorial_2);
			tv_text.setText(R.string.tutorial2);
		}else if(i == 5){
			ll_bg.setBackgroundResource(R.drawable.tutorial_3);
			tv_text.setText(R.string.tutorial3);
		}else if(i == 6){
			ll_bg.setBackgroundResource(R.drawable.tutorial_4);
			tv_text.setText(R.string.tutorial4);
		}else if(i == 7){
			ll_bg.setBackgroundResource(R.drawable.tutorial_5);
			tv_text.setText(R.string.tutorial5);
		}else if(i == 8){
			ll_bg.setBackgroundResource(R.drawable.new_tutorial_3);
			//String nodata="&#8226;Hold the phone about 1/4 inch away from your teeth for the duration of this session.<br/>&#8226;If your arms become tired just swap arms.<br/>&#8226;Make sure you are comfortable and enjoy your new white smile!.";
			String nodata="Touch the Begin Timed Session tab while holding the phone about ¼ inch from your teeth for the duration of the 60 minute session.  If your arm becomes tired, swap arms. Make sure you are comfortable and enjoy your new SMILE!";
	
			//TextView nodata= ((TextView) findViewById(R.id.nodata));
					//nodata.setText(Html.fromHtml(nodatafound));
			tv_text.setText(Html.fromHtml(nodata), TextView.BufferType.SPANNABLE);
			tv_next.setText("Exit");
		}
	}

}
