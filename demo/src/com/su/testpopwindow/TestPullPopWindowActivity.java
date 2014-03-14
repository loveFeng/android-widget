package com.su.testpopwindow;

import com.example.demo_highlights.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

public class TestPullPopWindowActivity extends Activity {
	private PopMenu popMenu;
	private Context context;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_pop);
		context = TestPullPopWindowActivity.this;
		popMenu = new PopMenu(context);

		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				popMenu.show(v);
			}
		});

	}

}