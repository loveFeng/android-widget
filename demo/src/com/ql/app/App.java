package com.ql.app;

import com.example.demo_highlights.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ql.view.TabSwitcher;
import com.ql.view.TabSwitcher.OnItemClickLisener;

public class App extends Activity{
	private Context context;
	private TabSwitcher tabSwitcher;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tabswitcher);
        context=this;
        tabSwitcher=(TabSwitcher)findViewById(R.id.tabSwitcher);
        
        tabSwitcher.setOnItemClickLisener(onItemClickLisener);
//        tabSwitcher.setTexts(new String[]{"1","2","3"});
    }
    
	
    OnItemClickLisener onItemClickLisener=new OnItemClickLisener(){

		@Override
		public void onItemClickLisener(View view, int position) {
			// TODO Auto-generated method stub
			//
			switch (position) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				Log.i("App", "position:"+position);
//				Toast.makeText(context, "position clicked:"+position, Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
    	
    };
}