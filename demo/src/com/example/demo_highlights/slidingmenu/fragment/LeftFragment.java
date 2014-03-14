/*
 * Copyright (C) 2012 yueyueniao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demo_highlights.slidingmenu.fragment;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Text;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.demo_highlights.R;
import com.example.demo_highlights.slidingmenu.view.SlidingMenu;


public class LeftFragment extends Fragment {
	private final String LIST_TEXT = "text";
	private final String LIST_IMAGEVIEW = "img";
	
	
	// [start]变量
	/**
	 * 数字代表列表顺序
	 */
	private int mTag = 0;
	private TextView textView1;
	private TextView textView2;
	private ProgressBar progressBar1;
	private ProgressBar progressBar2;
	
	private ListView lvTitle;
	private SimpleAdapter lvAdapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left, null);
		textView1 = (TextView)view.findViewById(R.id.DataTextView);
		textView2 = (TextView)view.findViewById(R.id.SDTextView);
		progressBar1 = (ProgressBar)view.findViewById(R.id.DataProgressBar);
		progressBar2 = (ProgressBar)view.findViewById(R.id.SDProgressBar);
		
		lvTitle = (ListView) view.findViewById(R.id.behind_list_show);
		
		getDataSize();
		getSDSize();
		
		initListView();
		
		return view;
	}
	
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(LIST_TEXT, getResources().getString(R.string.menuGood));
		map.put(LIST_IMAGEVIEW, R.drawable.dis_menu_handpick);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put(LIST_TEXT, getResources().getString(R.string.menuNews));
		map.put(LIST_IMAGEVIEW, R.drawable.dis_menu_news);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put(LIST_TEXT, getResources().getString(R.string.menuStudio));
		map.put(LIST_IMAGEVIEW, R.drawable.dis_menu_studio);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put(LIST_TEXT, getResources().getString(R.string.menuBlog));
		map.put(LIST_IMAGEVIEW, R.drawable.dis_menu_blog);
		list.add(map);
		return list;
	}
	
	private void initListView() {
		lvAdapter = new SimpleAdapter(getActivity(), getData(),
				R.layout.behind_list_show, new String[] { LIST_TEXT,
						LIST_IMAGEVIEW },
				new int[] { R.id.textview_behind_title,
						R.id.imageview_behind_icon }) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub.
				View view = super.getView(position, convertView, parent);
				if (position == mTag) {
					view.setBackgroundResource(R.drawable.back_behind_list);
					lvTitle.setTag(/*view*/String.valueOf(position));
				} else {
					view.setBackgroundColor(Color.TRANSPARENT);
				}
				return view;
			}
		};
		lvTitle.setAdapter(lvAdapter);
		lvTitle.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				NavigationModel navModel = navs.get(position);
//				mAboveTitle.setText(navModel.getName());
//				current_page = navModel.getTags();
				if (mTag != position) {
				switch (position) {
				case 0:
//					imgQuery.setVisibility(View.GONE);
					new MyTask().execute(new BaseDao());
					break;
				case 1:
					new MyTask().execute(new BaseDao(getActivity(), 1));
					break;
				case 2:
					new MyTask().execute(new BaseDao(getActivity(), 2));
					break;
				case 3:
					new MyTask().execute(new BaseDao(getActivity(), 3));
					break;
				}
				}
				
				if (lvTitle.getTag() != null) {
					if (lvTitle.getTag().equals(String.valueOf(position))) {
//						MainActivity.this.showContent();
						view.setBackgroundResource(R.drawable.back_behind_list);
						mTag = position;
						return;
					} else {
/*					((View) lvTitle.getTag())
							.setBackgroundColor(Color.TRANSPARENT);*/
						mTag = position;
						lvTitle.setTag(/*view*/String.valueOf(position));
						view.setBackgroundResource(R.drawable.back_behind_list);
					}
				} else {
					mTag = position;
					lvTitle.setTag(view);
					view.setBackgroundResource(R.drawable.back_behind_list);
				}
				//				imgQuery.setVisibility(View.VISIBLE);

				lvAdapter.notifyDataSetChanged();
			}
		});

	}
	
	public void getSDSize(){
		textView1.setText("");
		progressBar1.setProgress(0);
    	//�ж��Ƿ��в���洢��
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
    		File path =Environment.getExternalStorageDirectory();
    		//ȡ��sdcard�ļ�·��
    		StatFs statfs=new StatFs(path.getPath());
    		//��ȡblock��SIZE
    		long blocSize=statfs.getBlockSize();
    		//��ȡBLOCK����
    		long totalBlocks=statfs.getBlockCount();
    		//��ʹ�õ�Block������
    		long availaBlock=statfs.getAvailableBlocks();
    		
    		String[] total=filesize(totalBlocks*blocSize);
    		String[] availale=filesize(availaBlock*blocSize);
    		//���ý���������ֵ 
    		int maxValue=Integer.parseInt(availale[0].replaceAll(",", ""))
    		*progressBar1.getMax()/Integer.parseInt(total[0].replaceAll(",", ""));
    		progressBar1.setProgress(100 - maxValue);
    		String Text="SD:" + (Integer.parseInt(total[0].replaceAll(",", "")) - Integer.parseInt(availale[0].replaceAll(",", "")))+availale[1]/*availale[0].replaceAll(",", "")+availale[1]*/ + " / " + total[0].replaceAll(",", "")+total[1]; 
    		textView1.setText(Text);
    		
    	}else if(Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED)){
//    		Toast.makeText(getActivity(), "û��sdCard", 1000).show();
    		textView1.setText("SD卡容量未获得");
    	}
    }
	
	public void getDataSize(){
		textView2.setText("未获得内存容量");
		progressBar2.setProgress(0);
    	//�ж��Ƿ��в���洢��
    		File path =Environment.getDataDirectory();
    		//ȡ��sdcard�ļ�·��
    		StatFs statfs=new StatFs(path.getPath());
    		//��ȡblock��SIZE
    		long blocSize=statfs.getBlockSize();
    		//��ȡBLOCK����
    		long totalBlocks=statfs.getBlockCount();
    		//��ʹ�õ�Block������
    		long availaBlock=statfs.getAvailableBlocks();
    		
    		String[] total=filesize(totalBlocks*blocSize);
    		String[] availale=filesize(availaBlock*blocSize);
    		//���ý���������ֵ 
    		int maxValue=Integer.parseInt(availale[0].replaceAll(",", ""))
    		*progressBar2.getMax()/Integer.parseInt(total[0].replaceAll(",", ""));
    		progressBar2.setProgress(100 - maxValue);
//    		String Text="总大小"+total[0].replaceAll(",", "")+total[1]+"\n"
//    		+"已用大小:"+availale[0].replaceAll(",", "")+availale[1]; 
    		String Text="内部存储空间:" + (Integer.parseInt(total[0].replaceAll(",", "")) - Integer.parseInt(availale[0].replaceAll(",", ""))) + availale[1]/*availale[0].replaceAll(",", "")+availale[1]*/ + " / " + total[0].replaceAll(",", "")+total[1];
    		textView2.setText(Text);
    		
    	
    }

    String[] filesize(long size){
    	String str="";
    	if(size>=1024){
    		str="KB";
    		size/=1024;
    		if(size>=1024){
    			str="MB";
    			size/=1024;
    		}
    	}
    	DecimalFormat formatter=new DecimalFormat();
    	formatter.setGroupingSize(3);
    	String result[] =new String[2];
    	result[0]=formatter.format(size);
    	result[1]=str;
    	return result;
    }
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	// [end]
	/**
	 * 加载分类list的task
	 * 
	 * @author wangxin
	 * 
	 */
	public class MyTask extends AsyncTask<BaseDao, String, Map<String, Object>> {

		private boolean mUseCache;

		public MyTask() {
			mUseCache = true;
		}

		public MyTask(boolean useCache) {
			mUseCache = useCache;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			FragmentTransaction t = getActivity().getSupportFragmentManager()
					.beginTransaction();
			t.replace(R.id.center_frame, (Fragment)new PageFragmentLoading());
			t.commit();
//			getActivity().closeSlidingMenu();
/*			imgLeft.setVisibility(View.GONE);
			imgRight.setVisibility(View.GONE);
			loadLayout.setVisibility(View.VISIBLE);
			mViewPager.setVisibility(View.GONE);
			mViewPager.removeAllViews();
			mBasePageAdapter.Clear();
			MainActivity.this.showContent();*/
			super.onPreExecute();
//			isShowPopupWindows = false;
		}

		@Override
		protected Map<String, Object> doInBackground(BaseDao... params) {
			BaseDao dao = params[0];
			Map<String, Object> map = new HashMap<String, Object>();
			
			switch (dao.getFlag()) {
			case 0:
				map.put("fragment", new ViewPageFragment());
				map.put("tab", 0);
				break;
			case 1:
				map.put("fragment", new PageFragment1());
				map.put("tab", 1);
				break;
			case 2:
				map.put("fragment", new PageFragment2());
				map.put("tab", 2);
				break;
			case 3:
				map.put("fragment", new RightFragment());
				map.put("tab", 3);
				break;
			default:
				break;
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return map;
//			return new Map<String, Object>() {dao.getFlag, new PageFragment1()};
			
//			return null;
/*			BaseDao dao = params[0];
			List<CategorysEntity> categorys = new ArrayList<CategorysEntity>();
			Map<String, Object> map = new HashMap<String, Object>();
			if (dao instanceof TopDao) {
				mTag = 0;
				if ((categoryList = topDao.mapperJson(mUseCache)) != null) {

					categorys = topDao.getCategorys();
					map.put("tabs", categorys);
					map.put("list", categoryList);
				}
			} else if (dao instanceof BlogsDao) {
				mTag = 3;
				if ((responseData = blogsDao.mapperJson(mUseCache)) != null) {
					categoryList = (List) responseData.getList();
					categorys = responseData.getCategorys();
					map.put("tabs", categorys);
					map.put("list", categoryList);
				}
			} else if (dao instanceof NewsDao) {
				mTag = 1;
				if ((newsResponseData = newsDao.mapperJson(mUseCache)) != null) {
					categoryList = (List) newsResponseData.getList();
					categorys = newsResponseData.getCategorys();
					map.put("tabs", categorys);
					map.put("list", categoryList);
				}
			} else if (dao instanceof WikiDao) {
				mTag = 2;
				if ((wikiResponseData = wikiDao.mapperJson(mUseCache)) != null) {
					categoryList = (List) wikiResponseData.getList();
					categorys = wikiResponseData.getCategorys();
					map.put("tabs", categorys);
					map.put("list", categoryList);
				}
			} else {
				return null;
			}
			return map;*/
		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if (!result.isEmpty()) {
				FragmentTransaction t = getActivity().getSupportFragmentManager()
						.beginTransaction();
				t.replace(R.id.center_frame, (Fragment)result.get("fragment"));
				t.commit();
			} else {
				
			}
			
	/*		isShowPopupWindows = true;
			mBasePageAdapter.Clear();
			mViewPager.removeAllViews();
			if (!result.isEmpty()) {
				mBasePageAdapter.addFragment((List) result.get("tabs"),
						(List) result.get("list"));
				imgRight.setVisibility(View.VISIBLE);
				loadLayout.setVisibility(View.GONE);
				loadFaillayout.setVisibility(View.GONE);
			} else {
				mBasePageAdapter.addNullFragment();
				loadLayout.setVisibility(View.GONE);
				loadFaillayout.setVisibility(View.VISIBLE);
			}
			mViewPager.setVisibility(View.VISIBLE);
			mBasePageAdapter.notifyDataSetChanged();
			mViewPager.setCurrentItem(0);
			mIndicator.notifyDataSetChanged();

		*/}
	}
	
	public  class BaseDao {
//		ObjectMapper mObjectMapper = new ObjectMapper();

		protected Activity mActivity;
		private int mFlag;
		
		public int getFlag() {
			return mFlag;
		}
		
		public BaseDao(){
			mFlag = 0;
		};
		
		public BaseDao(Activity activity, int flag)
		{
			mActivity=activity;
			mFlag = flag;
		}

		
	}
	
}
