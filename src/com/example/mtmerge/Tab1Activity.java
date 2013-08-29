package com.example.mtmerge;

import java.util.ArrayList;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Tab1Activity extends Activity {

	private ListView mListView = null;
	// Data를 관리해주는 Adapter
	private CustomAdapter mCustomAdapter = null;
	// 제네릭(String)을 사용한 ArrayList
	private ArrayList<String> mArrayList = new ArrayList<String>();
	private ArrayList<Boolean> mIsCheckedList = new ArrayList<Boolean>();

	int group_position;	
	int boyNum = 0;
	int girlNum = 0;
	int checkedNum = 0;

	String group_id;
	String memTbName;
	
	SQLiteDatabase db_mt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab1);
		
		Intent myintent = getIntent();
		group_position = myintent.getExtras().getInt("group_position");
		
		db_mt = openOrCreateDatabase("db_mt", MODE_PRIVATE, null);
		
		initBoyGirlNumTbName();

		createifnotexistMemberTable();


		initList();
		
		mListView = (ListView) findViewById(R.id.lv_tab1_member);
		mCustomAdapter = new CustomAdapter(Tab1Activity.this, mArrayList, mIsCheckedList);
		mListView.setAdapter(mCustomAdapter);
//		mListView.setOnItemClickListener(new mItemClickListener());
//		mListView.setOnItemLongClickListener( new ListViewItemLongClickListener() );
	}

	protected void onDestroy() {
		db_mt.close();
		super.onDestroy();
	}
	
	private void setTotalNum(){
		((TextView)findViewById(R.id.tv_tab1_allnum)).setText(String.valueOf(checkedNum) + "/" + String.valueOf(boyNum + girlNum));
	}

	// 남자 여자 수만큼 추가
	private void initBoyGirlNumTbName() {
		group_id = getGroupId();
		memTbName = "tb_member_" + group_id;
		
		String boyStr = getDataGr(1);
		boyNum = Integer.parseInt(boyStr);
		String girlStr = getDataGr(2);
		girlNum = Integer.parseInt(girlStr);
	}
	
	private void initList() {
        String sql = "select mem_name, money from " + memTbName;
		Cursor cursor = db_mt.rawQuery(sql, null);
		cursor.moveToFirst();
		for(int i = 0; i < cursor.getCount(); i++){
			mArrayList.add(cursor.getString(0));
			if(cursor.getInt(1) == 0){
				mIsCheckedList.add(false);
			}
			else{
				mIsCheckedList.add(true);
				checkedNum++;
			}
			cursor.moveToNext();
		}
		cursor.close();
		
		setTotalNum();
	}

	public void addMember(View v) {
		AlertDialog alertDlg = new AlertDialog.Builder(Tab1Activity.this)
				.create();
		alertDlg.setTitle("Add New Member");
		alertDlg.setMessage("새로운 맴버의 성별은?");

		alertDlg.setButton(DialogInterface.BUTTON_POSITIVE, "남자",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						insertDataMem("새로운 남자 이름 ", false, false);
						updateBoyGirlGr(false, true); // boy, up
						mCustomAdapter.add("새로운 남자 이름 ");
						mCustomAdapter.notifyDataSetChanged();
						boyNum++;
						setTotalNum();
						return;
					}
				});

		alertDlg.setButton(DialogInterface.BUTTON_NEUTRAL, "여자",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						insertDataMem("새로운 여자 이름 ", false, true);
						updateBoyGirlGr(true, true); // girl, up
						mCustomAdapter.add("새로운 여자 이름 ");
						mCustomAdapter.notifyDataSetChanged();
						girlNum++;
						setTotalNum();
						return;
					}
				});
		
		alertDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "취소",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});

		alertDlg.show();
	}

	// ListView 안에 Item을 클릭시에 호출되는 Listener
/*	private class mItemClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			mCustomAdapter.setChecked(position);
			// Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
			mCustomAdapter.notifyDataSetChanged();

		}
	}
	
	private class ListViewItemLongClickListener implements OnItemLongClickListener
	   {
	       @Override
	       public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) 
	       {
	           final String selectedStr = getDataMem(position, 0);
	           AlertDialog alertDlg = new AlertDialog.Builder(Tab1Activity.this).create();
	           alertDlg.setTitle("Delete?");
	           alertDlg.setMessage("Do you want to delete " + selectedStr + "?");
	           // '예' 버튼이 클릭되면
	           alertDlg.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener()
	           {
	                @Override
	                public void onClick( DialogInterface dialog, int which ) 
	                {
	                	if(getDataMem(position, 2).equalsIgnoreCase("1")) girlNum--;
	                	else boyNum--;
	                	setTotalNum();
	                	
	            		if(getDataMem(position, 2).equalsIgnoreCase("1")) updateBoyGirlGr(true, false); // girl, down
	            		else updateBoyGirlGr(false, false); //boy, down
	                    
	            		deleteDataMem(position);
	            		
	                	mCustomAdapter.remove(position);
	                    mCustomAdapter.notifyDataSetChanged();       
	                    
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
*/
	// Custom Adapter
	class CustomAdapter extends BaseAdapter {

		private ViewHolder viewHolder = null;
		// 뷰를 새로 만들기 위한 Inflater
		private LayoutInflater inflater = null;
		private ArrayList<String> sArrayList = new ArrayList<String>();
		private ArrayList<Boolean> isCheckedConfrim = new ArrayList<Boolean>();

		public CustomAdapter(Context c, ArrayList<String> mList, ArrayList<Boolean> mBoolList) {
			inflater = LayoutInflater.from(c);
			this.sArrayList = mList;
			// ArrayList Size 만큼의 boolean 배열을 만든다.
			// CheckBox의 true/false를 구별 하기 위해
			this.isCheckedConfrim = mBoolList;
/*			for (int i = 0; i < sArrayList.size(); i++) {
				this.isCheckedConfrim.add(false);
			}
*/		}

		public void add(String tempStr) {
			sArrayList.add(tempStr);
			isCheckedConfrim.add(false);
		}
		
		public void remove(int position) {
			sArrayList.remove(position);
			isCheckedConfrim.remove(position);
		}

		public void setChecked(int position) {
			boolean tempBool = !isCheckedConfrim.get(position);
			isCheckedConfrim.set(position, tempBool); // isCheckedConfrim[position]
														// = ischeked; 체크하기
		}
		
		public void updateName(int position, String name){
			sArrayList.set(position, name);
		}

		public ArrayList<Integer> getChecked() {
			int tempSize = isCheckedConfrim.size();
			ArrayList<Integer> mArrayList = new ArrayList<Integer>();
			for (int b = 0; b < tempSize; b++) {
				if (isCheckedConfrim.get(b)) {
					mArrayList.add(b);
				}
			}
			return mArrayList;
		}

		@Override
		public int getCount() {
			return sArrayList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			// ConvertView가 null 일 경우
			View v = convertView;

			if (v == null) {
				viewHolder = new ViewHolder();
				// View를 inflater 시켜준다.
				v = inflater.inflate(R.layout.tab1_memberlist_row, null);
				viewHolder.tView = (TextView) v.findViewById(R.id.tv_tab1_memberlist_row);
				viewHolder.cBox = (CheckBox) v.findViewById(R.id.cb_tab1_memberlist_row);
				v.setTag(viewHolder);
			}

			else {
				viewHolder = (ViewHolder) v.getTag();
			}

			// CheckBox는 기본적으로 이벤트를 가지고 있기 때문에 ListView의 아이템
			// 클릭리즈너를 사용하기 위해서는 CheckBox의 이벤트를 없애 주어야 한다.
//			viewHolder.cBox.setClickable(false);
//			viewHolder.cBox.setFocusable(false);

			viewHolder.cBox.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                // TODO Auto-generated method stub
	            	updateMoneyDataMem(position, !isCheckedConfrim.get(position));
	            	
	            	if(isCheckedConfrim.get(position)) checkedNum--;
	            	else checkedNum++;
	            	setTotalNum();
	            	
	            	mCustomAdapter.setChecked(position);
	    			// Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
	    			mCustomAdapter.notifyDataSetChanged();
	            }
	        });
			
			viewHolder.tView.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                // TODO Auto-generated method stub
	            	Context mContext = getApplicationContext();
	        		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
	        		final View layout = inflater.inflate(R.layout.tab1_dialog_changememname, null);

	        		AlertDialog.Builder aDialog = new AlertDialog.Builder(Tab1Activity.this);
	        		aDialog.setView(layout);
	        		aDialog.setTitle("이름 수정하기");
	        		
	        		final EditText et_name = (EditText) layout.findViewById(R.id.et_tab1_memnewname);
	        		et_name.setText(getDataMem(position, 0));	        		
	        		et_name.selectAll();
	        		
	        		aDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
	        			public void onClick(DialogInterface dialog, int which) {
	        				String name = et_name.getText().toString();
	        				updateNameDataMem(position, name);
	        				mCustomAdapter.updateName(position, name);
	        				mCustomAdapter.notifyDataSetChanged();
	        			}
	        		});

	        		aDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
	        			public void onClick(DialogInterface dialog, int which) {
	        				return;
	        			}
	        		});
	        		
	        		AlertDialog ad = aDialog.create();
	        		ad.setOnShowListener(new OnShowListener() {
	        		    @Override
	        		    public void onShow(DialogInterface dialog) {
	        		        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	        		        imm.showSoftInput(et_name, InputMethodManager.SHOW_IMPLICIT);
	        		    }
	        		});
	        		ad.show();
	            }
	        });
			
			viewHolder.tView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                	final String selectedStr = getDataMem(position, 0);
                	AlertDialog alertDlg = new AlertDialog.Builder(Tab1Activity.this).create();
                	alertDlg.setTitle("Delete?");
                	alertDlg.setMessage("Do you want to delete " + selectedStr + "?");
                	// '예' 버튼이 클릭되면
                	alertDlg.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener()
                	{
     	                @Override
     	                public void onClick( DialogInterface dialog, int which ) 
     	                {
     	                	if(getDataMem(position, 2).equalsIgnoreCase("1")) girlNum--;
     	                	else boyNum--;
     	                	if(isCheckedConfrim.get(position)) checkedNum--;     	                	
     	                	setTotalNum();
     	                	
     	            		if(getDataMem(position, 2).equalsIgnoreCase("1")) updateBoyGirlGr(true, false); // girl, down
     	            		else updateBoyGirlGr(false, false); //boy, down
     	                    
     	            		deleteDataMem(position);
     	            		
     	            		mCustomAdapter.remove(position);
     	                    mCustomAdapter.notifyDataSetChanged();       
     	                    
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
            });
			
			
			viewHolder.tView.setText(sArrayList.get(position));
			// isCheckedConfrim 배열은 초기화시 모두 false로 초기화 되기때문에
			// 기본적으로 false로 초기화 시킬 수 있다.
			viewHolder.cBox.setChecked(isCheckedConfrim.get(position));

			return v;
		}
	}

	class ViewHolder {
		private TextView tView = null;
		// 새로운 Row에 들어갈 CheckBox
		private CheckBox cBox = null;
	}

	private void createifnotexistMemberTable() { // 테이블 생성 메소드
		// 테이블 생성 쿼리를 정의합니다. id값과 x y 를 텍스트형태로 만듭니다.
		Cursor cursor = db_mt.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
		cursor.moveToFirst();
		for(;;){
			if(cursor.getString(0).equalsIgnoreCase(memTbName)){
				break;
			}
			if (!cursor.moveToNext()) {
				String sql = "create table if not exists " + memTbName + "(mem_id INTEGER PRIMARY KEY AUTOINCREMENT, mem_name text, money integer, sex integer)";
				db_mt.execSQL(sql);

				for (int i = 0; i < girlNum; i++) {
					insertDataMem("여자 이름 " + i, false, true);
				}
				for (int i = 0; i < boyNum; i++) {
					insertDataMem("남자 이름 " + i, false, false);
				}
				break;
			}
		}
		cursor.close();
	}

	

	private void insertDataMem(String mem_name, boolean moneyBool, boolean sexBool) {
		int moneyNum;
		int sexNum;
		
		if(moneyBool) moneyNum = 1;
		else moneyNum = 0;
		if(sexBool) sexNum = 1; // 1=Girl
		else sexNum = 0;// 0=Boy
		
		String sql = "insert into " + memTbName + "(mem_name, money, sex) values('"
				+ mem_name + "', '" + moneyNum + "', '" + sexNum + "')";
		// 마찬가지로, 정의한 쿼리를 보냅니다.
		db_mt.execSQL(sql);
	}
	
	private void deleteDataMem(int position) {// 쿼리로 값 받아오는 메소드
		// GPS라는 테이블로부터 id,x,y값을 받아오겠다고 정의합니다.
		String sql = "select mem_id from " + memTbName;
		// 정의한 쿼리를 보내기 전에, Cousor라는 친구에게 넣어줍니다.
		Cursor cursor = db_mt.rawQuery(sql, null);
		cursor.moveToPosition(position);
		int tempn = cursor.getInt(0);
		cursor.close();
		db_mt.execSQL("delete from " + memTbName + " where mem_id = '" + tempn + "'");
	}
	
	private void updateNameDataMem(int position, String memNewName){		
		String sql1 = "select mem_id from " + memTbName;
		// 정의한 쿼리를 보내기 전에, Cousor라는 친구에게 넣어줍니다.
		Cursor cursor = db_mt.rawQuery(sql1, null);
		cursor.moveToPosition(position);
		int tempn = cursor.getInt(0);
		cursor.close();
				
		String sql2 = "UPDATE " + memTbName + " SET mem_name = '" + memNewName + "' WHERE mem_id = '" + tempn + "'";
		db_mt.execSQL(sql2);
	}
	
	private void updateMoneyDataMem(int position, boolean newMoney){		
		String sql1 = "select mem_id from " + memTbName;
		// 정의한 쿼리를 보내기 전에, Cousor라는 친구에게 넣어줍니다.
		Cursor cursor = db_mt.rawQuery(sql1, null);
		cursor.moveToPosition(position);
		int tempn = cursor.getInt(0);
		cursor.close();
		
		int tempMoney;		
		if(newMoney) tempMoney = 1;
		else tempMoney = 0;
				
		String sql2 = "UPDATE " + memTbName + " SET money = " + tempMoney + " WHERE mem_id = '" + tempn + "'";
		db_mt.execSQL(sql2);
	}	

	
	private String getDataMem(int position, int which) {// 쿼리로 값 받아오는 메소드
		// GPS라는 테이블로부터 id,x,y값을 받아오겠다고 정의합니다.
		String sql = "select mem_name, money, sex from " + memTbName;
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
	
	private void updateBoyGirlGr(boolean boyOrGirl, boolean downOrUp){ // boy, down = false // girl, up = true
		int tempNum;
		if(boyOrGirl) tempNum = Integer.parseInt(getDataGr(2));
		else tempNum = Integer.parseInt(getDataGr(1));
		if(downOrUp) tempNum++;
		else tempNum--;
		
		String tempStr;
		if(boyOrGirl) tempStr = "girl";
		else tempStr = "boy";
				
		String sql = "UPDATE tb_group SET " + tempStr + " = "+ String.valueOf(tempNum) + " WHERE group_id = " + group_id;
		db_mt.execSQL(sql);
	}

}

