package com.dream.myqiyi.search;

import com.example.demo_highlights.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class SearchActivity extends Activity {
	GridView mHotGridView, mHistoryGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity_qiyi);
		prepareView();
	}

	private void prepareView() {
		mHotGridView = (GridView) findViewById(R.id.hot_search_grid);
		mHotGridView.setAdapter(new GridAdapter(this));
		mHistoryGridView = (GridView) findViewById(R.id.history_search_grid);
		mHistoryGridView.setAdapter(new GridAdapter(this));
	}
}
