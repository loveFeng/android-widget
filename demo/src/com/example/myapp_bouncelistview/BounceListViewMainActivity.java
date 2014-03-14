package com.example.myapp_bouncelistview;

import com.example.demo_highlights.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class BounceListViewMainActivity extends Activity {

    private String[] mStrings = new String[]{"1","2","3","4","5","2","3","4","5","2","3","4","5","2","3","4","5","2","3","4","5","2","3","4","5","2","3","4","5","2","3","4","5","2","3","4","5","2","3","4","5","2","3","4","5"};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bounce_list_view_main);
        
        BouncyListView bounceListView = (BouncyListView) findViewById(R.id.list);
        bounceListView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mStrings));

		AnimationAdapter animAdapter = new ScaleInAnimationAdapter/*SwingRightInAnimationAdapter*//*AlphaInAnimationAdapter*//*SwingBottomInAnimationAdapter*/(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mStrings));
		animAdapter.setAbsListView(bounceListView);
		bounceListView.setAdapter(animAdapter);
       
    }
}
