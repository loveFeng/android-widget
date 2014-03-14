package com.example.myapp_xlistview;

import java.util.ArrayList;
import java.util.List;


import com.example.demo_highlights.R;
import com.example.myapp_xlistview.XListView.IXListViewListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class XListViewMainActivity extends Activity implements IXListViewListener{

	private String[] mStrings = new String[]{"1","2","3","4","5","2","3","4","5","2","3","4","5","2","3","4","5"};
	
	private List<NewsContentItem> items_list = new ArrayList<NewsContentItem>();
	
	private XListView listview;
	private ArrayAdapter mAdapter;
	
	private MyAdapter mMyAdapter;
	
	private LayoutInflater mInflater;
	
	private int count = 0;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
//				more_url = loadMoreEntity.getMore_url();
				getData(count);
				mMyAdapter.appendToList(getData(count));
				count += 10;
				break;
			}
			onLoad();
		}

	};
	
	public List<NewsContentItem> getData(int j) {
		List<NewsContentItem> items_list = new ArrayList<NewsContentItem>();
		for (int i = j; i < j + 10; i++) {
			NewsContentItem item = new NewsContentItem("title:" + i, "thr:" + i, i,
					"Item简介:" + i + "....." ,// Item简介
					"url:" + i,// 详情url
					1000 + i);
			items_list.add(item);
		}
		return items_list;
	}
	
	protected void onLoad() {
		listview.stopRefresh();
		listview.stopLoadMore();
		listview.setRefreshTime("刚刚");
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlist_view_main);
        mInflater = getLayoutInflater();
        listview = (XListView) this.findViewById(R.id.list);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(false);
		listview.setRefreshTime("刚刚");
		listview.setXListViewListener(this);
		listview.setOverScrollMode(View.OVER_SCROLL_NEVER);
		mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mStrings);
		
		mMyAdapter = new MyAdapter();
		mMyAdapter.appendToList(getData(count));
		count += 10;
		
        listview.setAdapter(mMyAdapter);
    }

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		onLoad();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (/*more_url.equals(null) || more_url.equals("")*/false) {
			mHandler.sendEmptyMessage(1);
			return;
		} else {

			new Thread() {

				@Override
				public void run() {
//					NewsMoreResponse response = new NewsDao(mActivity)
//							.getMore(more_url);
//					if (response != null) {
//						loadMoreEntity = response.getResponse();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						mHandler.sendEmptyMessage(0);
//					}
					super.run();
				}
			}.start();

		}
	}
	
	class MyAdapter extends BaseAdapter {
		List<NewsContentItem> mList = new ArrayList<NewsContentItem>();

		public MyAdapter() {
		}

		public void appendToList(List<NewsContentItem> lists) {
			if (lists == null) {
				return;
			}
			mList.addAll(lists);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			NewsContentItem item = mList.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater
						.inflate(R.layout.news_item_layout, null);
				holder.title_ = (TextView) convertView
						.findViewById(R.id.news_title);
				holder.short_ = (TextView) convertView
						.findViewById(R.id.news_short_content);
				holder.img_thu = (ImageView) convertView
						.findViewById(R.id.img_thu);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

			}
			holder.title_.setText(item.getTitle());
			holder.short_.setText(item.getShort_content());
			String img_url = item.getThumbnail_url();
			if (img_url.equals(null) || img_url.equals("")) {
				holder.img_thu.setVisibility(View.GONE);
			} else {
				holder.img_thu.setVisibility(View.VISIBLE);
//				ImageUtil.setThumbnailView(img_url, holder.img_thu, mActivity,
//						callback1, false);
			}
			return convertView;
		}
	}

	static class ViewHolder {
		public TextView title_;
		public TextView short_;
		public ImageView img_thu;
	}
}
