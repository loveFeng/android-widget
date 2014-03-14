package com.dream.myqiyi.account;


import com.example.demo_highlights.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AccountActivity extends Activity {
	TextView mTitleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_activity_qiyi);
		prepareView();
		mTitleView.setText(R.string.category_account);
	}

	private void prepareView() {
		mTitleView = (TextView) findViewById(R.id.title_text);
	}
}
