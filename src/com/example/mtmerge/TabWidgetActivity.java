package com.example.mtmerge;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;

public class TabWidgetActivity extends TabActivity{
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window win = getWindow();
        win.requestFeature(Window.FEATURE_CUSTOM_TITLE);	
		setContentView(R.layout.activity_tabwidget);
        win.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.tabwidget_titlebar);
        
        Resources res = getResources();  //리소스 객체 생성
        TabHost tabHost = getTabHost();  //TabHost 객체 생성
        TabHost.TabSpec spec;    //TabHost.TabSpec 선언
        Intent intent_tab;      //Intent 선언
        
        Intent intent_temp = getIntent();
		int group_position = intent_temp.getExtras().getInt("group_position");
		String group_name = intent_temp.getExtras().getString("group_name");
		
		((TextView) findViewById(R.id.tv_tabwidget_title_left)).setText(group_name);
        
        intent_tab = new Intent(this, Tab1Activity.class);
        intent_tab.putExtra("group_position", group_position);
        spec = tabHost.newTabSpec("tab1")
             .setIndicator("인원 체크")
             .setContent(intent_tab);
        tabHost.addTab(spec);
        
        intent_tab = new Intent(this, Tab2Activity.class);
        intent_tab.putExtra("group_position", group_position);
        spec = tabHost.newTabSpec("tab2")
             .setIndicator("식량 예측")
             .setContent(intent_tab);
        tabHost.addTab(spec);
        
        intent_tab = new Intent(this, Tab3Activity.class);
        intent_tab.putExtra("group_position", group_position);
        spec = tabHost.newTabSpec("tab3")
             .setIndicator("알콜 예측")
             .setContent(intent_tab);
        tabHost.addTab(spec);
        
        intent_tab = new Intent(this, Tab4Activity.class);
        intent_tab.putExtra("group_position", group_position);
        spec = tabHost.newTabSpec("tab4")
             .setIndicator("가계부")
             .setContent(intent_tab);
        tabHost.addTab(spec);
        
        tabHost.setCurrentTab(0); //초기 선택 탭 설정
    }
}
