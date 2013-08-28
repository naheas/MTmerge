package com.example.mtmerge;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class Tab3Activity extends Activity {

	private ListView listview;

	DataAdapter adapter;
	ArrayList<Alcohol> alist;
	SQLiteDatabase db_mt;

	int group_position;	
	int boyNum = 0;
	int girlNum = 0;

	String group_id;
	String alcTbName;

	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tab3);


		listview = (ListView) findViewById(R.id.lv_tab3_alcohol);

		alist = new ArrayList<Alcohol>();

		adapter = new DataAdapter(this, alist);

		// 리스트뷰에 어댑터 연결
		listview.setAdapter(adapter);

		Intent myintent = getIntent();
		group_position = myintent.getExtras().getInt("group_position");
		
		db_mt = openOrCreateDatabase("db_mt", MODE_PRIVATE, null);

		initBoyGirlNumTbName();
		createifnotexistMemberTable();

		Cursor cursor =	db_mt.rawQuery("SELECT * FROM "+alcTbName, null);
		cursor.moveToFirst();
		Double total=0.0;		
		for(int i = 0; i < cursor.getCount(); i++){
			total+=((double)cursor.getInt(1))*cursor.getFloat(2);
			cursor.moveToNext();
		}
		TextView total_alc = (TextView)findViewById(R.id.tv_tab3_5);
		String total_str = String.format("%.2f", total);
		total_alc.setText(total_str);
		
		
		TextView appro_alc = (TextView)findViewById(R.id.tv_tab3_2);
		appro_alc.setText(Integer.toString(boyNum*2+girlNum));
		
		//data Alcohol( getAppCon(), 병수 , 병당소주량 , 주종);
		
		insertDataFood(0, 0.26, "맥주 330ml 캔");
		insertDataFood(0, 1.12, "맥주 1.5L PET");
		insertDataFood(0, 1, "소주 1병");
		insertDataFood(0, 0.67, "막걸리 1병");
		insertDataFood(0, 4, "앱솔루트보드카 700ml");
		insertDataFood(0, 4, "스미노프 700ml");
		insertDataFood(0, 3.5, "예거마이스터 700ml");
		

	}

	private class DataAdapter extends ArrayAdapter<Alcohol> {

		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<Alcohol> object) {
			super(context, 0, object);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}


		@Override
		public View getView(final int position, View v, ViewGroup parent) {
		
			View view = null;

			if (v == null) 
				view = mInflater.inflate(R.layout.tab3_listviewitem, null);
			else 
				view = v;

			final Alcohol data = this.getItem(position);


			if (data != null) {
				// 화면 출력
				TextView tv = (TextView) view.findViewById(R.id.tv_tab3_alc_name);
				final TextView tv2 = (TextView) view.findViewById(R.id.tv_tab3_alc_bottle);
				// 텍스트뷰1에 getLabel()을 출력 즉 첫번째 인수값
				tv.setText(data.getalc_name());
				// 텍스트뷰2에 getData()을 출력 즉 두번째 인수값
				
				Cursor mycursor =	db_mt.rawQuery("SELECT * FROM "+ alcTbName, null);
				mycursor.moveToPosition(position);
				
				tv2.setText(String.valueOf(mycursor.getInt(1)));
				adapter.notifyDataSetChanged();

	
				
				Button btn_up = (Button)view.findViewById(R.id.btn_tab3_alcohol_up);
				btn_up.setOnClickListener(new ImageButton.OnClickListener(){

					@Override
					public void onClick(View v) {
						
						Cursor cursor =	db_mt.rawQuery("SELECT * FROM "+alcTbName, null);
						cursor.moveToPosition(position);
						
						db_mt.execSQL("UPDATE "+ alcTbName + " SET bot = bot+1 WHERE alc_id = " + cursor.getInt(0));
						tv2.setText(String.valueOf(cursor.getInt(1) + 1));
						
						TextView total_alc = (TextView)findViewById(R.id.tv_tab3_5);
						cursor = db_mt.rawQuery("SELECT * FROM "+alcTbName, null);
						cursor.moveToFirst();
						Double total=0.0;
						
						for(;;){
							total+=((double)cursor.getInt(1))*cursor.getFloat(2);
							if(cursor.isLast()) break;
							cursor.moveToNext();
						}
						
						String total_str = String.format("%.2f", total);
						total_alc.setText(total_str);
						
						    		
					}	
					
				}
				);
				
					
				Button btn_down = (Button)view.findViewById(R.id.btn_tab3_alcohol_down);
				btn_down.setOnClickListener(new ImageButton.OnClickListener(){

					@Override
					public void onClick(View v) {
						
						Cursor cursor =	db_mt.rawQuery("SELECT * FROM "+alcTbName, null);
						cursor.moveToPosition(position);
						
						if(cursor.getInt(1)==0)
							return;
						
						db_mt.execSQL("UPDATE "+ alcTbName + " SET bot = bot-1 WHERE alc_id = " + cursor.getInt(0));
						tv2.setText(String.valueOf(cursor.getInt(1) - 1));
						
						TextView total_alc = (TextView)findViewById(R.id.tv_tab3_5);
						cursor = db_mt.rawQuery("SELECT * FROM "+alcTbName, null);
						cursor.moveToFirst();
						Double total=0.0;
						
						for(;;){
							total+=((double)cursor.getInt(1))*cursor.getFloat(2);
							if(cursor.isLast()) break;
							cursor.moveToNext();
						}
						
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
		
				
	}
	
	
	private void initBoyGirlNumTbName() {
		group_id = getGroupId();
		alcTbName = "tb_alc_" + group_id;
		
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
			if(cursor.getString(0).equalsIgnoreCase(alcTbName)){
				break;
			}
			if (!cursor.moveToNext()) {
				String sql = "create table if not exists " + alcTbName + 
						"(alc_id INTEGER PRIMARY KEY AUTOINCREMENT, bot INTEGER, ab_alc REAL, name TEXT)";
				db_mt.execSQL(sql);

				break;
			}
		}
		cursor.close();
	}

	

	private void insertDataFood(int bottle, double abs_alc, String alc_name) {
		
		String sql = "INSERT INTO " + alcTbName + "(bot, ab_alc, name) values('"
				+ Integer.toString(bottle) + "', '" + Double.toString(abs_alc) + "', '" + alc_name + "')";
		// 마찬가지로, 정의한 쿼리를 보냅니다.
		db_mt.execSQL(sql);
		
		adapter.add(new Alcohol(getApplicationContext(), bottle, abs_alc, alc_name));

	}
	

	
	private String getDataMem(int position, int which) {// 쿼리로 값 받아오는 메소드
		// GPS라는 테이블로부터 id,x,y값을 받아오겠다고 정의합니다.
		String sql = "SELECT food_name, quantity, sosu FROM " + alcTbName;
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