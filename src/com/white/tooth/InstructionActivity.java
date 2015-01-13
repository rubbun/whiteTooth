package com.white.tooth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class InstructionActivity extends BaseActivity{
	
	private CheckBox checkbox_meat;

	private TextView tv_general,tv_general_detail,tv_risk,tv_risk_detail;
	private TextView tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inst);
		
		
		tv3 = (TextView)findViewById(R.id.tv3);
		tv3.setTypeface(agecyTypeface());
		
		tv4 = (TextView)findViewById(R.id.tv4);
		tv4.setTypeface(agecyTypeface());
		
		tv5 = (TextView)findViewById(R.id.tv5);
		tv5.setTypeface(agecyTypeface());
		
		tv6 = (TextView)findViewById(R.id.tv6);
		tv6.setTypeface(agecyTypeface());
		
		tv7 = (TextView)findViewById(R.id.tv7);
		tv7.setTypeface(agecyTypeface());
		
		tv8 = (TextView)findViewById(R.id.tv8);
		tv8.setTypeface(agecyTypeface());
		
		tv9 = (TextView)findViewById(R.id.tv9);
		tv9 = (TextView)findViewById(R.id.tv9);
		
		tv10 = (TextView)findViewById(R.id.tv10);
		tv10.setTypeface(agecyTypeface());
		
		tv11 = (TextView)findViewById(R.id.tv11);
		tv11.setTypeface(agecyTypeface());
		
		tv_general = (TextView)findViewById(R.id.tv_general);
		tv_general.setTypeface(agecyTypeface());
		
		tv_general_detail = (TextView)findViewById(R.id.tv_general_details);
		tv_general_detail.setTypeface(agecyTypeface());
		
		tv_risk = (TextView)findViewById(R.id.tv_risk);
		tv_risk.setTypeface(agecyTypeface());
		
		tv_risk_detail = (TextView)findViewById(R.id.tv_risk_detail);
		tv_risk_detail.setTypeface(agecyTypeface());
		
		checkbox_meat = (CheckBox)findViewById(R.id.checkbox_meat);
		checkbox_meat.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					
							Intent i = new Intent(InstructionActivity.this,DsahBoardScreen.class);
							startActivity(i);
							finish();
						}
					
					
				
			}
		});
	}
}
