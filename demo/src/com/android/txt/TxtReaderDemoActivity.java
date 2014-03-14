package com.android.txt;

import java.io.InputStream;

import com.example.demo_highlights.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class TxtReaderDemoActivity extends Activity {

	TextView txt;
	TextView textView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_txtreader);

		txt = (TextView) findViewById(R.id.button1);
		textView = (TextView) findViewById(R.id.textView1);

		txt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputStream inputStream = getResources().openRawResource(R.raw.a);
				String string = TxtReader.getString(inputStream);
				textView.setText(string);
			}
		});

	}
}