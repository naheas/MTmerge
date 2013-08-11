package com.example.mtmerge;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Window;
import android.widget.TabHost;

public class TabWidgetActivity extends TabActivity{
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tabwidget);
        
        Resources res = getResources();  //���ҽ� ��ü ����
        TabHost tabHost = getTabHost();  //TabHost ��ü ����
        TabHost.TabSpec spec;    //TabHost.TabSpec ����
        Intent intent_tab;      //Intent ����
        
        Intent intent_temp = getIntent();
		int group_position = intent_temp.getExtras().getInt("group_position");
        
        intent_tab = new Intent(this, Tab1Activity.class);
        intent_tab.putExtra("group_position", group_position);
        spec = tabHost.newTabSpec("tab1")
             .setIndicator("�ο� üũ")
             .setContent(intent_tab);
        tabHost.addTab(spec);
        
        intent_tab = new Intent(this, Tab2Activity.class);
        intent_tab.putExtra("group_position", group_position);
        spec = tabHost.newTabSpec("tab2")
             .setIndicator("�ķ� ����")
             .setContent(intent_tab);
        tabHost.addTab(spec);
        
        intent_tab = new Intent(this, Tab3Activity.class);
        intent_tab.putExtra("group_position", group_position);
        spec = tabHost.newTabSpec("tab3")
             .setIndicator("���� ����")
             .setContent(intent_tab);
        tabHost.addTab(spec);
        
        intent_tab = new Intent(this, Tab4Activity.class);
        intent_tab.putExtra("group_position", group_position);
        spec = tabHost.newTabSpec("tab4")
             .setIndicator("�����", res.getDrawable(android.R.drawable.sym_action_call))
             .setContent(intent_tab);
        tabHost.addTab(spec);
        
        tabHost.setCurrentTab(0); //�ʱ� ���� �� ����
    }
}
