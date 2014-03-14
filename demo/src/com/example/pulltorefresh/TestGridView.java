package com.example.pulltorefresh;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.example.demo_highlights.R;
import com.example.pulltorefresh.PullToRefreshView.OnFooterRefreshListener;
import com.example.pulltorefresh.PullToRefreshView.OnHeaderRefreshListener;


public class TestGridView extends Activity implements OnHeaderRefreshListener,OnFooterRefreshListener{
	PullToRefreshView mPullToRefreshView;
	GridView mGridView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pull_refresh_test_gridview);
		
		mPullToRefreshView = (PullToRefreshView)findViewById(R.id.main_pull_refresh_view);
		mGridView = (GridView)findViewById(R.id.gridview);
		mGridView.setAdapter(new DataAdapter(this));
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPullToRefreshView.setLastUpdated(new Date().toLocaleString());
        
	}
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mPullToRefreshView.onFooterRefreshComplete();
			}
		},1000);
	}
	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mPullToRefreshView.onHeaderRefreshComplete("更新于:"+new Date().toLocaleString());
//				mPullToRefreshView.onHeaderRefreshComplete();
			}
		},1000);
		
	}
}
