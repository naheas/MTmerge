package com.example.mtmerge;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Tab2Activity extends Activity {

	private ListView listview;

	DataAdapter adapter;
	ArrayList<food> alist;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tab2);


		listview = (ListView) findViewById(R.id.lv_tab2_food);

		alist = new ArrayList<food>();

		adapter = new DataAdapter(this, alist);

		// 리스트뷰에 어댑터 연결
		listview.setAdapter(adapter);
		
		//data Alcohol( getAppCon(), 병수 , 병당소주량 , 주종);
		adapter.add(new food(getApplicationContext(), 1,"밥"));
		adapter.add(new food(getApplicationContext(), 2,"고기"));
		adapter.add(new food(getApplicationContext(), 5,"물"));
		adapter.add(new food(getApplicationContext(), 10,"상추"));
		

	}

	private class DataAdapter extends ArrayAdapter<food> {

		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<food> object) {
			super(context, 0, object);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}


		@Override
		public View getView(int position, View v, ViewGroup parent) {
		
			View view = null;

			if (v == null) 
				view = mInflater.inflate(R.layout.tab2_listviewitem, null);
			else 
				view = v;

			final food data = this.getItem(position);


			if (data != null) {
				
				final TextView tv = (TextView) view.findViewById(R.id.tv_tab2_food_name);
				final TextView tv2 = (TextView) view.findViewById(R.id.tv_tab2_food_quantity);
				// 텍스트뷰1에 getLabel()을 출력 즉 첫번째 인수값
				tv.setText(data.get_foodname());
				// 텍스트뷰2에 getData()을 출력 즉 두번째 인수값
				
				tv2.setText(String.valueOf(data.get_quantity()));
		
				adapter.notifyDataSetChanged();

				Button btn_no = (Button)view.findViewById(R.id.btn_tab2_food_0);
				btn_no.setOnClickListener(new ImageButton.OnClickListener(){

					@Override
					public void onClick(View v) {
								
						TextView quantity = (TextView)findViewById(R.id.tv_tab2_food_quantity);
						Double total = data.get_quantity()*0;
						tv2.setText(total.toString());
						data.quantity=data.real*0;
						
						    		
					}	
					
				}
				);
				
				Button btn_little = (Button)view.findViewById(R.id.btn_tab2_food_1);
				btn_little.setOnClickListener(new ImageButton.OnClickListener(){

					@Override
					public void onClick(View v) {
								
						TextView quantity = (TextView)findViewById(R.id.tv_tab2_food_quantity);
						Double total = data.get_quantity()*0.75;
						tv2.setText(total.toString());
						data.quantity=data.real*0.75;
						
						    		
					}	
					
				}
				);
				
				Button btn_default = (Button)view.findViewById(R.id.btn_tab2_food_2);
				btn_default.setOnClickListener(new ImageButton.OnClickListener(){

					@Override
					public void onClick(View v) {
								
						TextView quantity = (TextView)findViewById(R.id.tv_tab2_food_quantity);
						Double total = data.get_quantity();
						tv2.setText(total.toString());
						data.quantity=data.real*1;
						
						    		
					}	
					
				}
				);
				
				Button btn_lot = (Button)view.findViewById(R.id.btn_tab2_food_3);
				btn_lot.setOnClickListener(new ImageButton.OnClickListener(){

					@Override
					public void onClick(View v) {
								
						TextView quantity = (TextView)findViewById(R.id.tv_tab2_food_quantity);
						Double total = data.get_quantity()*1.25;
						tv2.setText(total.toString());
						data.quantity=data.real*1.25;
						
						    		
					}	
					
				}
				);
				

			}
			return view;
		}
	}


	class food{

		private double quantity;
		private String foodname;
		private double real;
		public food(Context context, double q, String f_name) {

			quantity = q;
			foodname = f_name;
			real= q;
		}

		public String get_foodname(){
			return foodname;
		}

		public double get_quantity(){
			return quantity;
		}



		
        
		
	}

}
