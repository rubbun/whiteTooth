package com.white.tooth;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.white.tooth.adapter.HistoryAdapter;
import com.white.tooth.adapter.HistoryAdapter.OnHistoryItemDeleteListener;

public class History extends BaseActivity implements OnClickListener,OnHistoryItemDeleteListener {
	private ArrayList<String> list = new ArrayList<String>();
	public HistoryAdapter adapter; 
	private ListView lv_history_list;
	private LinearLayout ll_back;
	private TextView tv_main_menu;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		
		tv_main_menu = (TextView)findViewById(R.id.tv_main_menu);
		tv_main_menu.setTypeface(agecyTypeface());
		
		lv_history_list = (ListView)findViewById(R.id.lv_history_list);
		
		ll_back = (LinearLayout)findViewById(R.id.ll_back);
		ll_back.setOnClickListener(this);

		list = dbAdapter.getInfo();
		adapter = new HistoryAdapter(History.this, R.layout.history_row, list);
		lv_history_list.setAdapter(adapter);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_back:
			finish();
			break;
		}
	}

	@Override
	public void onDelete() {
		list = dbAdapter.getInfo();
		adapter = new HistoryAdapter(History.this, R.layout.history_row, list);
		lv_history_list.setAdapter(adapter);
		
	}
}
