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
	// Data�� �������ִ� Adapter
	private CustomAdapter mCustomAdapter = null;
	// ���׸�(String)�� ����� ArrayList
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

	// ���� ���� ����ŭ �߰�
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
		alertDlg.setMessage("���ο� �ɹ��� ������?");

		alertDlg.setButton(DialogInterface.BUTTON_POSITIVE, "����",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						insertDataMem("���ο� ���� �̸� ", false, false);
						updateBoyGirlGr(false, true); // boy, up
						mCustomAdapter.add("���ο� ���� �̸� ");
						mCustomAdapter.notifyDataSetChanged();
						boyNum++;
						setTotalNum();
						return;
					}
				});

		alertDlg.setButton(DialogInterface.BUTTON_NEUTRAL, "����",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						insertDataMem("���ο� ���� �̸� ", false, true);
						updateBoyGirlGr(true, true); // girl, up
						mCustomAdapter.add("���ο� ���� �̸� ");
						mCustomAdapter.notifyDataSetChanged();
						girlNum++;
						setTotalNum();
						return;
					}
				});
		
		alertDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "���",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});

		alertDlg.show();
	}

	// ListView �ȿ� Item�� Ŭ���ÿ� ȣ��Ǵ� Listener
/*	private class mItemClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			mCustomAdapter.setChecked(position);
			// Data ����� ȣ�� Adapter�� Data ���� ����� �˷��༭ Update ��.
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
	           // '��' ��ư�� Ŭ���Ǹ�
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
	           
	           // '�ƴϿ�' ��ư�� Ŭ���Ǹ�
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
		// �並 ���� ����� ���� Inflater
		private LayoutInflater inflater = null;
		private ArrayList<String> sArrayList = new ArrayList<String>();
		private ArrayList<Boolean> isCheckedConfrim = new ArrayList<Boolean>();

		public CustomAdapter(Context c, ArrayList<String> mList, ArrayList<Boolean> mBoolList) {
			inflater = LayoutInflater.from(c);
			this.sArrayList = mList;
			// ArrayList Size ��ŭ�� boolean �迭�� �����.
			// CheckBox�� true/false�� ���� �ϱ� ����
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
														// = ischeked; üũ�ϱ�
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

			// ConvertView�� null �� ���
			View v = convertView;

			if (v == null) {
				viewHolder = new ViewHolder();
				// View�� inflater �����ش�.
				v = inflater.inflate(R.layout.tab1_memberlist_row, null);
				viewHolder.tView = (TextView) v.findViewById(R.id.tv_tab1_memberlist_row);
				viewHolder.cBox = (CheckBox) v.findViewById(R.id.cb_tab1_memberlist_row);
				v.setTag(viewHolder);
			}

			else {
				viewHolder = (ViewHolder) v.getTag();
			}

			// CheckBox�� �⺻������ �̺�Ʈ�� ������ �ֱ� ������ ListView�� ������
			// Ŭ������ʸ� ����ϱ� ���ؼ��� CheckBox�� �̺�Ʈ�� ���� �־�� �Ѵ�.
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
	    			// Data ����� ȣ�� Adapter�� Data ���� ����� �˷��༭ Update ��.
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
	        		aDialog.setTitle("�̸� �����ϱ�");
	        		
	        		final EditText et_name = (EditText) layout.findViewById(R.id.et_tab1_memnewname);
	        		et_name.setText(getDataMem(position, 0));	        		
	        		et_name.selectAll();
	        		
	        		aDialog.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
	        			public void onClick(DialogInterface dialog, int which) {
	        				String name = et_name.getText().toString();
	        				updateNameDataMem(position, name);
	        				mCustomAdapter.updateName(position, name);
	        				mCustomAdapter.notifyDataSetChanged();
	        			}
	        		});

	        		aDialog.setNegativeButton("���", new DialogInterface.OnClickListener() {
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
                	// '��' ��ư�� Ŭ���Ǹ�
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
     	           
     	           // '�ƴϿ�' ��ư�� Ŭ���Ǹ�
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
			// isCheckedConfrim �迭�� �ʱ�ȭ�� ��� false�� �ʱ�ȭ �Ǳ⶧����
			// �⺻������ false�� �ʱ�ȭ ��ų �� �ִ�.
			viewHolder.cBox.setChecked(isCheckedConfrim.get(position));

			return v;
		}
	}

	class ViewHolder {
		private TextView tView = null;
		// ���ο� Row�� �� CheckBox
		private CheckBox cBox = null;
	}

	private void createifnotexistMemberTable() { // ���̺� ���� �޼ҵ�
		// ���̺� ���� ������ �����մϴ�. id���� x y �� �ؽ�Ʈ���·� ����ϴ�.
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
					insertDataMem("���� �̸� " + i, false, true);
				}
				for (int i = 0; i < boyNum; i++) {
					insertDataMem("���� �̸� " + i, false, false);
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
		// ����������, ������ ������ �����ϴ�.
		db_mt.execSQL(sql);
	}
	
	private void deleteDataMem(int position) {// ������ �� �޾ƿ��� �޼ҵ�
		// GPS��� ���̺�κ��� id,x,y���� �޾ƿ��ڴٰ� �����մϴ�.
		String sql = "select mem_id from " + memTbName;
		// ������ ������ ������ ����, Cousor��� ģ������ �־��ݴϴ�.
		Cursor cursor = db_mt.rawQuery(sql, null);
		cursor.moveToPosition(position);
		int tempn = cursor.getInt(0);
		cursor.close();
		db_mt.execSQL("delete from " + memTbName + " where mem_id = '" + tempn + "'");
	}
	
	private void updateNameDataMem(int position, String memNewName){		
		String sql1 = "select mem_id from " + memTbName;
		// ������ ������ ������ ����, Cousor��� ģ������ �־��ݴϴ�.
		Cursor cursor = db_mt.rawQuery(sql1, null);
		cursor.moveToPosition(position);
		int tempn = cursor.getInt(0);
		cursor.close();
				
		String sql2 = "UPDATE " + memTbName + " SET mem_name = '" + memNewName + "' WHERE mem_id = '" + tempn + "'";
		db_mt.execSQL(sql2);
	}
	
	private void updateMoneyDataMem(int position, boolean newMoney){		
		String sql1 = "select mem_id from " + memTbName;
		// ������ ������ ������ ����, Cousor��� ģ������ �־��ݴϴ�.
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

	
	private String getDataMem(int position, int which) {// ������ �� �޾ƿ��� �޼ҵ�
		// GPS��� ���̺�κ��� id,x,y���� �޾ƿ��ڴٰ� �����մϴ�.
		String sql = "select mem_name, money, sex from " + memTbName;
		// ������ ������ ������ ����, Cousor��� ģ������ �־��ݴϴ�.
		Cursor cursor = db_mt.rawQuery(sql, null);
		cursor.moveToPosition(position);
		String temps;
		if(which == 0) temps = cursor.getString(which);
		else temps = String.valueOf(cursor.getInt(which));
		cursor.close();
		return temps;
	}

	private String getDataGr(int which) {// ������ �� �޾ƿ��� �޼ҵ�
		// GPS��� ���̺�κ��� id,x,y���� �޾ƿ��ڴٰ� �����մϴ�.
		String sql = "select groupname, boy, girl, place from tb_group";
		// ������ ������ ������ ����, Cousor��� ģ������ �־��ݴϴ�.
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

