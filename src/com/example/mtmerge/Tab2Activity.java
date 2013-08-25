package com.example.mtmerge;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
	SQLiteDatabase db_mt;
	
	
	int group_position;	
	int boyNum = 0;
	int girlNum = 0;

	String group_id;
	String foodTbName;

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tab2);


		listview = (ListView) findViewById(R.id.lv_tab2_food);

		alist = new ArrayList<food>();

		adapter = new DataAdapter(this, alist);

		// 리스트뷰에 어댑터 연결
		listview.setAdapter(adapter);
		
		
		Intent myintent = getIntent();
		group_position = myintent.getExtras().getInt("group_position");
		
		db_mt = openOrCreateDatabase("db_mt", MODE_PRIVATE, null);

		initBoyGirlNumTbName();
		createifnotexistMemberTable();


		
		//insertDataFood(food_name, quantity, real)
		insertDataFood("쌀"	,100,1);
		insertDataFood("고기"	,250,1);
		insertDataFood("상추"	,0.2,1);
		insertDataFood("버섯"	,0.2,1);
		insertDataFood("소세지",0.2,1);
		insertDataFood("만두",0.2	,1);
		insertDataFood("물"	,0.25,1);
		insertDataFood("음료수",0.25,1);
		insertDataFood("과자"	,1,1);
		insertDataFood("젓가락",4,1);
		insertDataFood("그릇",1,1);
		insertDataFood("종이컵",5,1);

		

	}


	private class DataAdapter extends ArrayAdapter<food> {

		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<food> object) {
			super(context, 0, object);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}


		@Override
		public View getView(final int position, View v, ViewGroup parent) {
		
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
				
	

				Cursor mycursor =	db_mt.rawQuery("SELECT * FROM "+ foodTbName, null);
				mycursor.moveToPosition(position);
				
				Double mytotal = (double) (mycursor.getFloat(2)*mycursor.getFloat(3));
				tv2.setText(String.format("%.2f", mytotal));

				
				adapter.notifyDataSetChanged();

				Button btn_no = (Button)view.findViewById(R.id.btn_tab2_food_0);
				btn_no.setOnClickListener(new ImageButton.OnClickListener(){

					@Override
					public void onClick(View v) {
								
						
						Cursor cursor =	db_mt.rawQuery("SELECT * FROM "+foodTbName, null);
						cursor.moveToPosition(position);
						
						db_mt.execSQL("UPDATE "+ foodTbName + " SET sosu = 0 WHERE food_id = " + cursor.getInt(0));
						
						cursor = db_mt.rawQuery("SELECT * FROM "+foodTbName, null);
						cursor.moveToPosition(position);
						Double total = (double) (cursor.getFloat(2)*cursor.getFloat(3));
						tv2.setText(total.toString());
						    		
					}	
					
				}
				);
				
				Button btn_little = (Button)view.findViewById(R.id.btn_tab2_food_1);
				btn_little.setOnClickListener(new ImageButton.OnClickListener(){

					@Override
					public void onClick(View v) {
								
						Cursor cursor =	db_mt.rawQuery("SELECT * FROM "+foodTbName, null);
						cursor.moveToPosition(position);
						
						db_mt.execSQL("UPDATE "+ foodTbName + " SET sosu = 0.75 WHERE food_id = " + cursor.getInt(0));

						cursor = db_mt.rawQuery("SELECT * FROM "+foodTbName, null);
						cursor.moveToPosition(position);
						Double total = (double) (cursor.getFloat(2)*cursor.getFloat(3));
						tv2.setText(total.toString());

						    		
					}	
					
				}
				);
				
				Button btn_default = (Button)view.findViewById(R.id.btn_tab2_food_2);
				btn_default.setOnClickListener(new ImageButton.OnClickListener(){

					@Override
					public void onClick(View v) {
								
						Cursor cursor =	db_mt.rawQuery("SELECT * FROM "+foodTbName, null);
						cursor.moveToPosition(position);
						
						db_mt.execSQL("UPDATE "+ foodTbName + " SET sosu = 1.0 WHERE food_id = " + cursor.getInt(0));

						cursor = db_mt.rawQuery("SELECT * FROM "+foodTbName, null);
						cursor.moveToPosition(position);
						Double total = (double) (cursor.getFloat(2)*cursor.getFloat(3));
						tv2.setText(total.toString());
    		
					}	
					
				}
				);
				
				Button btn_lot = (Button)view.findViewById(R.id.btn_tab2_food_3);
				btn_lot.setOnClickListener(new ImageButton.OnClickListener(){

					@Override
					public void onClick(View v) {
								
						Cursor cursor =	db_mt.rawQuery("SELECT * FROM "+foodTbName, null);
						cursor.moveToPosition(position);
						
						db_mt.execSQL("UPDATE "+ foodTbName + " SET sosu = 1.25 WHERE food_id = " + cursor.getInt(0));

						cursor = db_mt.rawQuery("SELECT * FROM "+foodTbName, null);
						cursor.moveToPosition(position);
						Double total = (double) (cursor.getFloat(2)*cursor.getFloat(3));
						tv2.setText(total.toString());
    		
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
			real= 1.0;
		}

		public String get_foodname(){
			return foodname;
		}

		public double get_quantity(){
			return quantity;
		}



		
        
		
	}
	
	private void initBoyGirlNumTbName() {
		group_id = getGroupId();
		foodTbName = "tb_food_" + group_id;
		
		String boyStr = getDataGr(1);
		boyNum = Integer.parseInt(boyStr);
		String girlStr = getDataGr(2);
		girlNum = Integer.parseInt(girlStr);
				
		
	}
	

	private void createifnotexistMemberTable() { // 테이블 생성 메소드
		// 테이블 생성 쿼리를 정의합니다. id값과 x y 를 텍스트형태로 만듭니다.
		Cursor cursor = db_mt.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
		cursor.moveToFirst();
		for(;;){
			if(cursor.getString(0).equalsIgnoreCase(foodTbName)){
				break;
			}
			if (!cursor.moveToNext()) {
				String sql = "create table if not exists " + foodTbName + 
						"(food_id INTEGER PRIMARY KEY AUTOINCREMENT, food_name text, quantity REAL, sosu REAL)";
				db_mt.execSQL(sql);

				break;
			}
		}
		cursor.close();
	}

	

	private void insertDataFood(String food_name, double quantity, double sosu) {
		
		String sql = "INSERT INTO " + foodTbName + "(food_name, quantity, sosu) values('"
				+ food_name + "', '" + Double.toString(quantity*(boyNum+girlNum)) + "', '" + Double.toString(sosu) + "')";
		// 마찬가지로, 정의한 쿼리를 보냅니다.
		db_mt.execSQL(sql);
		
		adapter.add(new food(getApplicationContext(), quantity, food_name));

	}
	

	
	private String getDataMem(int position, int which) {// 쿼리로 값 받아오는 메소드
		// GPS라는 테이블로부터 id,x,y값을 받아오겠다고 정의합니다.
		String sql = "SELECT food_name, quantity, sosu FROM " + foodTbName;
		// 정의한 쿼리를 보내기 전에, Cousor라는 친구에게 넣어줍니다.
		Cursor cursor = db_mt.rawQuery(sql, null);
		cursor.moveToPosition(position);
		String temps;
		if(which == 0) temps = cursor.getString(which);
		else temps = String.valueOf(cursor.getInt(which));
		cursor.close();
		return temps;
	}

	private String getDataGr(int which) {// 쿼리로 값 받아오는 메소드
		// GPS라는 테이블로부터 id,x,y값을 받아오겠다고 정의합니다.
		String sql = "select groupname, boy, girl, place from tb_group";
		// 정의한 쿼리를 보내기 전에, Cousor라는 친구에게 넣어줍니다.
		Cursor cursor = db_mt.rawQuery(sql, null);
		cursor.moveToPosition(group_position);
		String temps = cursor.getString(which);
		cursor.close();
		return temps;
	}
	
	private String getGroupId() {
		String sql = "select group_id from tb_group";
		Cursor cursor = db_mt.rawQuery(sql, null);
		cursor.moveToPosition(group_position);
		int tempn = cursor.getInt(0);
		cursor.close();
		String tempStr = String.valueOf(tempn);
		return tempStr;
	}
	


}
