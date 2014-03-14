package com.example.pulltorefresh;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.demo_highlights.R;
import com.example.pulltorefresh.PullToRefreshView.OnFooterRefreshListener;
import com.example.pulltorefresh.PullToRefreshView.OnHeaderRefreshListener;

public class TestScrollView extends Activity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {
	PullToRefreshView mPullToRefreshView;
	TextView textview;
	String text = "http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/http://miloisbadboy.com/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pull_refresh_test_scrollview);

		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		textview = (TextView) findViewById(R.id.textview);
		textview.setText(text);

		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		mPullToRefreshView.setLastUpdated(new Date().toLocaleString());

	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.onFooterRefreshComplete();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				mPullToRefreshView.onHeaderRefreshComplete("更新于:"+new Date().toLocaleString());
//				mPullToRefreshView.onHeaderRefreshComplete();
			}
		}, 1000);

	}
}
