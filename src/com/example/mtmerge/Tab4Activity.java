package com.example.mtmerge;

import java.util.ArrayList;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

public class Tab4Activity extends Activity {
	
	private ListView listview;
	DataAdapter adapter;
	ArrayList<OutcomeUnit> alist;
	int total_income = 0;
	int total_outcome = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab4);
		
		listview = (ListView) findViewById(R.id.lv_tab4_outcome);
		alist = new ArrayList<OutcomeUnit>();
		adapter = new DataAdapter(this, alist);
		// 리스트뷰에 어댑터 연결
		listview.setAdapter(adapter);		
		listview.setOnItemLongClickListener( new ListViewItemLongClickListener() );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tab4, menu);
		return true;
	}
	
	public void setRemainMoney(){
		((TextView) findViewById(R.id.tv_tab4_remain)).setText(String.valueOf(total_income - total_outcome) + " 원");
	}
	
	public void go_btn_tab4_addoutcome(View v) {
		// TODO Auto-generated method stub

		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View layout = inflater.inflate(R.layout.tab4_dialog_addoutcome, null);

		AlertDialog.Builder aDialog = new AlertDialog.Builder(Tab4Activity.this);
		aDialog.setView(layout);
		aDialog.setTitle("새로운 지출 추가");

		aDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				EditText et_name = (EditText) layout.findViewById(R.id.et_tab4_outcome_name);
				String name = et_name.getText().toString();
				EditText et_money = (EditText) layout.findViewById(R.id.et_tab4_outcome_money);
				String moneyStr = et_money.getText().toString();
				if(moneyStr.equalsIgnoreCase("")) moneyStr = "0";
				int money = Integer.parseInt(moneyStr); // do we need to check if number?

				adapter.add(new OutcomeUnit(getApplicationContext(), name, money));
				// db.execSQL("INSERT INTO db_mtpeople(name) VALUES ('" + name +
				// "')");
				total_outcome += money;
				((TextView) findViewById(R.id.tv_tab4_outcome)).setText(String.valueOf(total_outcome) + " 원");
				setRemainMoney();

			}
		});

		aDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});

		AlertDialog ad = aDialog.create();
		ad.show();
	}
	
	public void go_btn_tab4_setincome(View v) {
		// TODO Auto-generated method stub

		Context mContext = getApplicationContext();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View layout = inflater.inflate(R.layout.tab4_dialog_setincome, null);

		AlertDialog.Builder aDialog = new AlertDialog.Builder(Tab4Activity.this);
		aDialog.setView(layout);
		aDialog.setTitle("총 걷을 돈 입력");

		aDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				EditText et_income = (EditText) layout.findViewById(R.id.et_tab4_income);
				String incomeStr = et_income.getText().toString();
				if(incomeStr.equalsIgnoreCase("")) incomeStr = "0";
				int money = Integer.parseInt(incomeStr); // do we need to check if number?

				total_income = money;
				((TextView) findViewById(R.id.tv_tab4_income)).setText(String.valueOf(money) + " 원");
				setRemainMoney();

			}
		});

		aDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});

		AlertDialog ad = aDialog.create();
		ad.show();
	}
	
	private class ListViewItemLongClickListener implements OnItemLongClickListener
	   {
	       @Override
	       public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) 
	       {
	    	   final String selectedStr = ((OutcomeUnit) parent.getItemAtPosition(position)).getName();
	           AlertDialog alertDlg = new AlertDialog.Builder(Tab4Activity.this).create();
	           alertDlg.setTitle("Delete?");
	           alertDlg.setMessage("Do you want to delete " + selectedStr + "?");
	           // '예' 버튼이 클릭되면
	           alertDlg.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener()
	           {
	                @Override
	                public void onClick( DialogInterface dialog, int which ) 
	                {
	                	int money = adapter.getItem(position).getMoney();
	                	adapter.remove(position);
	                    adapter.notifyDataSetChanged();
	                    
	                    total_outcome -= money;
	                    ((TextView) findViewById(R.id.tv_tab4_outcome)).setText(String.valueOf(total_outcome) + " 원");
	    				setRemainMoney();
	    				
	                	return;
	                }
	           });
	           
	           // '아니오' 버튼이 클릭되면
	           alertDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener()
	           {
	                @Override
	                public void onClick( DialogInterface dialog, int which ) {
	                	return;
	                }
	           });
	           
	           
	           alertDlg.show();
	           return true;
	       }
	    
	   }
	
	
	private class DataAdapter extends ArrayAdapter<OutcomeUnit> {

		private LayoutInflater mInflater;

		public DataAdapter(Context context, ArrayList<OutcomeUnit> object) {
			super(context, 0, object);
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}
		
		public void remove(int position){
			alist.remove(position);
		}


		@Override
		public View getView(int position, View v, ViewGroup parent) {
		
			View view = null;

			if (v == null) 
				view = mInflater.inflate(R.layout.tab4_outcomelist_row, null);
			else 
				view = v;

			final OutcomeUnit data = this.getItem(position);


			if (data != null) {
				// 화면 출력
				TextView tv_name = (TextView) view.findViewById(R.id.tv_tab4_lv_outcome_name);
				TextView tv_money = (TextView) view.findViewById(R.id.tv_tab4_lv_outcome_money);
				tv_name.setText(data.getName());
				tv_money.setText(String.valueOf(data.getMoney()) + " 원");				
				adapter.notifyDataSetChanged();
			}
			return view;
		}
	}
	
	
	class OutcomeUnit{

		private String name;
		private int money;

		public OutcomeUnit(Context context, String o_name, int o_money) {
			name = o_name;
			money = o_money;
		}

		public String getName(){
			return name;
		}

		public int getMoney(){
			return money;
		}		
        
		public void putUnit(String o_name, int o_money){
			name = o_name;
			money = o_money;			
		}
		
	}

}
