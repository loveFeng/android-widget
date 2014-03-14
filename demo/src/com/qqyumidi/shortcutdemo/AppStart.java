package com.qqyumidi.shortcutdemo;

import com.example.demo_highlights.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AppStart extends Activity implements OnClickListener {

	private Button button1;
	private Button button2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_start);

		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);

		button1.setOnClickListener(this);
		button2.setOnClickListener(this);

		Intent intent = getIntent();
		if (intent != null) {
			String tName = intent.getStringExtra("tName");
			if (tName != null) {
				Intent redirectIntent = new Intent();
				redirectIntent.setClass(AppStart.this, TiebaActivity.class);
				redirectIntent.putExtra("tName", tName);
				startActivity(redirectIntent);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(AppStart.this, TiebaActivity.class);
		//传参
		switch (v.getId()) {
		case R.id.button1:
			intent.putExtra("tName", getString(R.string.shutcut_1));
			break;
		case R.id.button2:
			intent.putExtra("tName", getString(R.string.shutcut_2));
			break;
		}
		startActivity(intent);
	}
}
