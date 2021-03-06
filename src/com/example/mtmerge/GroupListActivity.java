package com.example.mtmerge;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GroupListActivity extends Activity {
	private static final int NewGr_ACTIVITY = 1;

	private ArrayAdapter<String> _arrAdapter ;

	SQLiteDatabase db_mt;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window win = getWindow();
        win.requestFeature(Window.FEATURE_CUSTOM_TITLE);	
		setContentView(R.layout.activity_grouplist);
        win.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.grouplist_titlebar);

		db_mt = openOrCreateDatabase("db_mt", MODE_PRIVATE, null);
		createifnotexistTable(); //테이블 생성 메소드 불러오기
		settingListView();
	}

	protected void onDestroy() {
        db_mt.close();
        super.onDestroy();
    }

	private void settingListView() {  
        _arrAdapter= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1)
        {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);

                /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(Color.BLACK);

                return view;
            }
        };
        
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
        	String tempStr = _arrAdapter.getItem(position);
        	intent_tabwidget.putExtra("group_name", tempStr);        	
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
    		if(groupnameStr.isEmpty()) groupnameStr = "";
    		if(placeStr.isEmpty()) placeStr = "";
    		
    		insertData(groupnameStr, boyStr, girlStr, placeStr);
    		refresh(groupnameStr);
        }
    }


	private void refresh( String inputValue ) {  
        _arrAdapter.add( inputValue );  
        _arrAdapter.notifyDataSetChanged();  
    }



    
   /*
    * ListView의 item을 길게 클릭했을 경우.
    * 클릭된 item을 삭제한다.
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
           // '예' 버튼이 클릭되면
           alertDlg.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener()
           {
                @Override
                public void onClick( DialogInterface dialog, int which ) 
                {
                	deleteMemTable(position);
                	deleteOutcomeTable(position);
                	deleteFoodTable(position);
                	deleteAlcTable(position);
                	deleteData(position);
               	 	_arrAdapter.remove(selectedStr);
                    // 아래 method를 호출하지 않을 경우, 삭제된 item이 화면에 계속 보여진다.
                    _arrAdapter.notifyDataSetChanged();       
                    
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
   

	private void createifnotexistTable() { // 테이블 생성 메소드
		// 테이블 생성 쿼리를 정의합니다. id값과 x y 를 텍스트형태로 만듭니다.
		String sql = "create table if not exists tb_group(group_id INTEGER PRIMARY KEY AUTOINCREMENT, groupname text, boy text, girl text, place text)";
		// 위의 정의한 쿼리를 보냅니다.
		db_mt.execSQL(sql);
	}

	private void deleteMemTable(int position) { // 테이블 생성 메소드
		// 테이블 생성 쿼리를 정의합니다. id값과 x y 를 텍스트형태로 만듭니다.		
		String sql = "select group_id from tb_group";
		// 정의한 쿼리를 보내기 전에, Cousor라는 친구에게 넣어줍니다.
		Cursor cursor = db_mt.rawQuery(sql, null);
		cursor.moveToPosition(position);
		int tempn = cursor.getInt(0);
		cursor.close();
		String tempStr = String.valueOf(tempn);
		db_mt.execSQL("drop table if exists tb_member_" + tempStr);
	}
	
	private void deleteOutcomeTable(int position) { // 테이블 생성 메소드
		// 테이블 생성 쿼리를 정의합니다. id값과 x y 를 텍스트형태로 만듭니다.		
		String sql = "select group_id from tb_group";
		// 정의한 쿼리를 보내기 전에, Cousor라는 친구에게 넣어줍니다.
		Cursor cursor = db_mt.rawQuery(sql, null);
		cursor.moveToPosition(position);
		int tempn = cursor.getInt(0);
		cursor.close();
		String tempStr = String.valueOf(tempn);
		db_mt.execSQL("drop table if exists tb_outcome_" + tempStr);
	}
	
	private void deleteFoodTable(int position) { // 테이블 생성 메소드
		// 테이블 생성 쿼리를 정의합니다. id값과 x y 를 텍스트형태로 만듭니다.		
		String sql = "select group_id from tb_group";
		// 정의한 쿼리를 보내기 전에, Cousor라는 친구에게 넣어줍니다.
		Cursor cursor = db_mt.rawQuery(sql, null);
		cursor.moveToPosition(position);
		int tempn = cursor.getInt(0);
		cursor.close();
		String tempStr = String.valueOf(tempn);
		db_mt.execSQL("drop table if exists tb_food_" + tempStr);
	}
	
	private void deleteAlcTable(int position) { // 테이블 생성 메소드
		// 테이블 생성 쿼리를 정의합니다. id값과 x y 를 텍스트형태로 만듭니다.		
		String sql = "select group_id from tb_group";
		// 정의한 쿼리를 보내기 전에, Cousor라는 친구에게 넣어줍니다.
		Cursor cursor = db_mt.rawQuery(sql, null);
		cursor.moveToPosition(position);
		int tempn = cursor.getInt(0);
		cursor.close();
		String tempStr = String.valueOf(tempn);
		db_mt.execSQL("drop table if exists tb_alc_" + tempStr);
	}


	private void insertData(String groupname, String boy, String girl, String place) {// 데이터
																					// 삽입
																					// 메소드
		/*
		 * /삽입할 데이터를 명령해주는 쿼리를 정의합니다. 앞서 버튼정의에서 보셨겠지만, 각각의 값을 메서드에 넣어준겁니다. /
		 */
		String sql = "insert into tb_group(groupname, boy, girl, place) values('"
				+ groupname + "', '" + boy + "', '" + girl + "', '" + place + "')";
		// 마찬가지로, 정의한 쿼리를 보냅니다.
		db_mt.execSQL(sql);
	}
	
	private void deleteData(int position) {// 쿼리로 값 받아오는 메소드
		// GPS라는 테이블로부터 id,x,y값을 받아오겠다고 정의합니다.
		String sql = "select group_id from tb_group";
		// 정의한 쿼리를 보내기 전에, Cousor라는 친구에게 넣어줍니다.
		Cursor cursor = db_mt.rawQuery(sql, null);
		cursor.moveToPosition(position);
		int tempn = cursor.getInt(0);
		cursor.close();
		db_mt.execSQL("delete from tb_group where group_id = '" + String.valueOf(tempn) + "'");
	}

	private String getData(int position, int which) {// 쿼리로 값 받아오는 메소드
		// GPS라는 테이블로부터 id,x,y값을 받아오겠다고 정의합니다.
		String sql = "select groupname, boy, girl, place from tb_group";
		// 정의한 쿼리를 보내기 전에, Cousor라는 친구에게 넣어줍니다.
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