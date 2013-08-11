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

public class Tab3Activity extends BaseActivity {

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

		// 리스트뷰에 어댑터 연결
		listview.setAdapter(adapter);
		
		//data Alcohol( getAppCon(), 병수 , 병당소주량 , 주종);
		adapter.add(new Alcohol(getApplicationContext(), 0, 1.12, "맥주"));
		adapter.add(new Alcohol(getApplicationContext(), 0, 1, "소주"));
		adapter.add(new Alcohol(getApplicationContext(), 0, 4, "앱솔루트보드카"));
		adapter.add(new Alcohol(getApplicationContext(), 0, 3.5, "예거마이스터"));
		

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
				// 화면 출력
				TextView tv = (TextView) view.findViewById(R.id.tv_tab3_alc_name);
				TextView tv2 = (TextView) view.findViewById(R.id.tv_tab3_alc_bottle);
				// 텍스트뷰1에 getLabel()을 출력 즉 첫번째 인수값
				tv.setText(data.getalc_name());
				// 텍스트뷰2에 getData()을 출력 즉 두번째 인수값
				
				tv2.setText(String.valueOf(data.getbottle()));
				adapter.notifyDataSetChanged();

				Button btn_up = (Button)view.findViewById(R.id.btn_tab3_alcohol_up);
				btn_up.setOnClickListener(new ImageButton.OnClickListener(){

					@Override
					public void onClick(View v) {
						data.bottle++;
								
						TextView total_alc = (TextView)findViewById(R.id.tv_tab3_5);
						Double total = Double.parseDouble(total_alc.getText().toString())+data.getabsolute_alc();
						String total_str = String.format("%.2f", total);
						total_alc.setText(total_str);
						
						    		
					}	
					
				}
				);
				
					
				Button btn_down = (Button)view.findViewById(R.id.btn_tab3_alcohol_down);
				btn_down.setOnClickListener(new ImageButton.OnClickListener(){

					@Override
					public void onClick(View v) {
						if(data.bottle==0)
							return;
						data.bottle--;
						
						TextView total_alc = (TextView)findViewById(R.id.tv_tab3_5);
						Double total = Double.parseDouble(total_alc.getText().toString())-data.getabsolute_alc();
						String total_str = String.format("%.2f", total);
						total_alc.setText(total_str);
						
						    		
					}	
					
				}
				);

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
			
			Button btn_up = (Button)findViewById(R.id.btn_tab3_alcohol_up);
			btn_up.setOnClickListener(new ImageButton.OnClickListener(){

				@Override
				public void onClick(View v) {
					bottle++;
							
					TextView total_alc = (TextView)findViewById(R.id.tv_tab3_6);
					Double total = Double.parseDouble(total_alc.getText().toString())+getabsolute_alc();
					total_alc.setText(total.toString());
					
					    		
				}	
				
			}
			);

			return;
		}
		
		public void down(){
			
			
			return;
		}
        

		
        
		
	}

}