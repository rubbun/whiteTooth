package com.white.tooth.adapter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.white.tooth.History;
import com.white.tooth.R;



public class HistoryAdapter  extends ArrayAdapter<String>{
	
	public interface OnHistoryItemDeleteListener{
		public void onDelete();
	}
	private ArrayList<String> arr= new ArrayList<String>();
	private ViewHolder mHolder;
	private History mAct;
	private String[] part;
	public OnHistoryItemDeleteListener listener;
	
	public HistoryAdapter(History act,int textViewResourceId , ArrayList<String> items) {
		super(act, textViewResourceId,items);		
		this.mAct = act;
		this.arr = items;
		listener = (OnHistoryItemDeleteListener) mAct;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v == null){
			LayoutInflater vi = (LayoutInflater) mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=vi.inflate(R.layout.history_row, null);
			mHolder=new ViewHolder();
			
			mHolder.tv_date=(TextView)v.findViewById(R.id.tv_date);
			mHolder.tv_time=(TextView)v.findViewById(R.id.tv_time);
			mHolder.tv_minutes=(TextView)v.findViewById(R.id.tv_minutes);
			mHolder.ll_row = (LinearLayout)v.findViewById(R.id.ll_row);
			
			v.setTag(mHolder);
			
		}else{
			mHolder=(ViewHolder)v.getTag();
		}
		
		part = arr.get(position).split("~");
		mHolder.tv_time.setText(part[1]);
		mHolder.tv_minutes.setText(part[2]);
		mHolder.tv_date.setText(part[0]);
		
		mHolder.ll_row.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(mAct)
			    .setTitle("Alert")
			    .setMessage("Do you want to delete this session from history?")
			    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        	mAct.dbAdapter.deleteRow(part[3]);
			        	//notifyDataSetChanged();
			        	listener.onDelete();
			        }
			     })
			    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            
			        }
			     })
			    .setIcon(android.R.drawable.ic_dialog_alert)
			     .show();
			}
		});
		
		return v;
	}
	
	
	public class ViewHolder {		
		
		public TextView tv_date;
		public TextView tv_time,tv_minutes;
		public LinearLayout ll_row;
		
	}
}
