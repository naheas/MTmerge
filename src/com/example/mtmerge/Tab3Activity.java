package com.example.mtmerge;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Tab3Activity extends Activity {

	private ListView listview;

	DataAdapter adapter;
	ArrayList<Alcohol> alist;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tab3);


		listview = (ListView) findViewById(R.id.lv_tab3_alcohol);

		alist = new ArrayList<Alcohol>();

		adapter = new DataAdapter(this, alist);

		// ����Ʈ�信 ����� ����
		listview.setAdapter(adapter);
		
		//data Alcohol( getAppCon(), ���� , ������ַ� , ����);
		adapter.add(new Alcohol(getApplicationContext(), 0, 0.25, "����"));
		adapter.add(new Alcohol(getApplicationContext(), 0, 1, "����"));
		adapter.add(new Alcohol(getApplicationContext(), 0, 4, "�ۼַ�Ʈ����ī"));
		adapter.add(new Alcohol(getApplicationContext(), 0, 3.5, "���Ÿ��̽���"));
		

	}

	private class DataAdapter extends ArrayAdapter<Alcohol> {

		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<Alcohol> object) {
			super(context, 0, object);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}


		@Override
		public View getView(int position, View v, ViewGroup parent) {
		
			View view = null;

			if (v == null) 
				view = mInflater.inflate(R.layout.tab3_listviewitem, null);
			else 
				view = v;

			final Alcohol data = this.getItem(position);

			if (data != null) {
				// ȭ�� ���
				TextView tv = (TextView) view.findViewById(R.id.tv_tab3_alc_name);
				TextView tv2 = (TextView) view.findViewById(R.id.tv_tab3_alc_bottle);
				// �ؽ�Ʈ��1�� getLabel()�� ��� �� ù��° �μ���
				tv.setText(data.getalc_name());
				// �ؽ�Ʈ��2�� getData()�� ��� �� �ι�° �μ���
				
				tv2.setText(String.valueOf(data.getbottle()));
				adapter.notifyDataSetChanged();
			}
			return view;
		}
	}


	class Alcohol{

		private int bottle;
		private double absolute_alc;
		private String alcname;

		public Alcohol(Context context, int bot, double ab_alc, String a_name) {

			bottle = bot;
			absolute_alc = ab_alc;
			alcname = a_name;
		}

		public int getbottle(){
			return bottle;
		}

		public double getabsolute_alc(){
			return absolute_alc;
		}

		public String getalc_name(){
			return alcname;
		}

		public void up(){
			bottle++;
		
			Button btn_up = (Button)findViewById(R.id.btn_tab3_alcohol_up);
			btn_up.setOnClickListener(new ImageButton.OnClickListener(){

				@Override
				public void onClick(View v) {
					
					TextView total_alc = (TextView)findViewById(R.id.tv_tab3_6);
					Double total = Double.parseDouble(total_alc.getText().toString())+getabsolute_alc();
					total_alc.setText(total.toString());
					
					    		
				}	
				
			}
			);

			return;
		}
		
		public void down(){
			if(bottle==0)
				return;
			else{
				bottle--;
				
				Button btn_down = (Button)findViewById(R.id.btn_tab3_alcohol_down);
				btn_down.setOnClickListener(new ImageButton.OnClickListener(){

					@Override
					public void onClick(View v) {
						
						TextView total_alc = (TextView)findViewById(R.id.tv_tab3_6);
						Double total = Double.parseDouble(total_alc.getText().toString())-getabsolute_alc();
						total_alc.setText(total.toString());
						
						    		
					}	
					
				}
				);

			}
			
			return;
		}
        

		
        
	}

}
