package com.example.mtmerge;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GroupListActivity extends Activity {
	private static final int NewGr_ACTIVITY = 1;

	private ArrayAdapter<String> _arrAdapter ;

	SQLiteDatabase db_mt;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouplist);

		db_mt = openOrCreateDatabase("db_mt", MODE_PRIVATE, null);
		createifnotexistTable(); //���̺� ���� �޼ҵ� �ҷ�����
		settingListView();
	}

	protected void onDestroy() {
        db_mt.close();
        super.onDestroy();
    }

	private void settingListView() {  
        _arrAdapter = new ArrayAdapter<String>( getApplicationContext(), android.R.layout.simple_list_item_1 ) ;  

        ListView listView = (ListView) findViewById(R.id.lv_grouplist) ;
        listView.setAdapter( _arrAdapter ) ;
        
        String sql = "select groupname, boy, girl, place from tb_group";
		Cursor cursor = db_mt.rawQuery(sql, null);
		cursor.moveToFirst();
		for(int i = 0; i < cursor.getCount(); i++){
			refresh(cursor.getString(0));
			cursor.moveToNext();
		}
		cursor.close();
        
        listView.setOnItemClickListener( new ListViewItemClickListener() );
        listView.setOnItemLongClickListener( new ListViewItemLongClickListener() );
    }

	private class ListViewItemClickListener implements OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
        {
        	Intent intent_tabwidget = new Intent(GroupListActivity.this, TabWidgetActivity.class);
        	intent_tabwidget.putExtra("group_position", position);
        	startActivity(intent_tabwidget);
        	
        }        
    }

 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void go_btn_grouplist_select (View v) {

	    Intent myintent = new Intent(this, NewGroupActivity.class);
	    startActivityForResult(myintent, NewGr_ACTIVITY);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {		
        if(resultCode == RESULT_OK && requestCode == NewGr_ACTIVITY) {        	
        	String groupnameStr = data.getExtras().getString("groupname");
    		String boyStr = data.getExtras().getString("boy");
    		String girlStr = data.getExtras().getString("girl");
    		String placeStr = data.getExtras().getString("place");
    		
    		if(!isStringInt(boyStr)) boyStr = "0";
    		if(!isStringInt(girlStr)) girlStr = "0";
    		
    		insertData(groupnameStr, boyStr, girlStr, placeStr);
    		refresh(groupnameStr);
        }
    }


	private void refresh( String inputValue ) {  
        _arrAdapter.add( inputValue ) ;  
        _arrAdapter.notifyDataSetChanged() ;  
    }



    
   /*
    * ListView�� item�� ��� Ŭ������ ���.
    * Ŭ���� item�� �����Ѵ�.
    * @author stargatex
    */
   private class ListViewItemLongClickListener implements OnItemLongClickListener
   {
       @Override
       public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) 
       {
           final String selectedStr = _arrAdapter.getItem(position);
           AlertDialog alertDlg = new AlertDialog.Builder(GroupListActivity.this).create();
           alertDlg.setTitle("Delete?");
           alertDlg.setMessage("Do you want to delete " + selectedStr + "?");
           // '��' ��ư�� Ŭ���Ǹ�
           alertDlg.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener()
           {
                @Override
                public void onClick( DialogInterface dialog, int which ) 
                {
                	deleteMemTable(position);
                	deleteData(position);
               	 	_arrAdapter.remove(selectedStr);
                    // �Ʒ� method�� ȣ������ ���� ���, ������ item�� ȭ�鿡 ��� ��������.
                    _arrAdapter.notifyDataSetChanged();       
                    
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
   

	private void createifnotexistTable() { // ���̺� ���� �޼ҵ�
		// ���̺� ���� ������ �����մϴ�. id���� x y �� �ؽ�Ʈ���·� ����ϴ�.
		String sql = "create table if not exists tb_group(group_id INTEGER PRIMARY KEY AUTOINCREMENT, groupname text, boy text, girl text, place text)";
		// ���� ������ ������ �����ϴ�.
		db_mt.execSQL(sql);
	}

	private void deleteMemTable(int position) { // ���̺� ���� �޼ҵ�
		// ���̺� ���� ������ �����մϴ�. id���� x y �� �ؽ�Ʈ���·� ����ϴ�.		
		String sql = "select group_id from tb_group";
		// ������ ������ ������ ����, Cousor��� ģ������ �־��ݴϴ�.
		Cursor cursor = db_mt.rawQuery(sql, null);
		cursor.moveToPosition(position);
		int tempn = cursor.getInt(0);
		cursor.close();
		String tempStr = String.valueOf(tempn);
		db_mt.execSQL("drop table tb_member_" + tempStr);
	}

	private void insertData(String groupname, String boy, String girl, String place) {// ������
																					// ����
																					// �޼ҵ�
		/*
		 * /������ �����͸� ������ִ� ������ �����մϴ�. �ռ� ��ư���ǿ��� ���̰�����, ������ ���� �޼��忡 �־��ذ̴ϴ�. /
		 */
		String sql = "insert into tb_group(groupname, boy, girl, place) values('"
				+ groupname + "', '" + boy + "', '" + girl + "', '" + place + "')";
		// ����������, ������ ������ �����ϴ�.
		db_mt.execSQL(sql);
	}
	
	private void deleteData(int position) {// ������ �� �޾ƿ��� �޼ҵ�
		// GPS��� ���̺�κ��� id,x,y���� �޾ƿ��ڴٰ� �����մϴ�.
		String sql = "select group_id from tb_group";
		// ������ ������ ������ ����, Cousor��� ģ������ �־��ݴϴ�.
		Cursor cursor = db_mt.rawQuery(sql, null);
		cursor.moveToPosition(position);
		int tempn = cursor.getInt(0);
		cursor.close();
		db_mt.execSQL("delete from tb_group where group_id = '" + String.valueOf(tempn) + "'");
	}

	private String getData(int position, int which) {// ������ �� �޾ƿ��� �޼ҵ�
		// GPS��� ���̺�κ��� id,x,y���� �޾ƿ��ڴٰ� �����մϴ�.
		String sql = "select groupname, boy, girl, place from tb_group";
		// ������ ������ ������ ����, Cousor��� ģ������ �־��ݴϴ�.
		Cursor cursor = db_mt.rawQuery(sql, null);
		cursor.moveToPosition(position);
		String temps = cursor.getString(which);
		cursor.close();
		return temps;
	}
	
	public static boolean isStringInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}


}