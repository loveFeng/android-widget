package com.example.myapp_iphonelistview;

import com.example.demo_highlights.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class IphoneMainActivity extends Activity {

	private String[] mStrings = new String[]{"1","2","3","4","5"};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_iphone_main);
        
        IphoneListView listView = (IphoneListView) findViewById(R.id.list);
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mStrings));

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mStrings);
        listView.setAdapter(adapter);
    }

}
