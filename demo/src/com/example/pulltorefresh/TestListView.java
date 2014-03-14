package com.example.pulltorefresh;

import java.util.Date;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.demo_highlights.R;
import com.example.pulltorefresh.PullToRefreshView.OnFooterRefreshListener;
import com.example.pulltorefresh.PullToRefreshView.OnHeaderRefreshListener;

public class TestListView extends ListActivity implements OnHeaderRefreshListener,OnFooterRefreshListener{
	PullToRefreshView mPullToRefreshView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pull_refresh_test_listview);
		mPullToRefreshView = (PullToRefreshView)findViewById(R.id.main_pull_refresh_view);
        
		setListAdapter(new DataAdapter(this));
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mPullToRefreshView.setLastUpdated(new Date().toLocaleString());
        
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Toast.makeText(this, "positon = "+position, Toast.LENGTH_SHORT).show();
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
