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

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;

import com.example.demo_highlights.R;
import com.example.demo_highlights.slidingmenu.activity.SlidingActivity;

public class ViewPageFragment extends Fragment {

	private Button showLeft;
	private Button showRight;
	private MyAdapter mAdapter;
	private ViewPager mPager;
	private TabSwitcher mTabSwitcher;
	private ArrayList<Fragment> pagerItemList = new ArrayList<Fragment>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.view_pager, null);
		
		ViewGroup p = (ViewGroup)mView.getParent();
		
		if (p != null) {
			p.removeAllViews();
		}
		
		showLeft = (Button) mView.findViewById(R.id.showLeft);
		showRight = (Button) mView.findViewById(R.id.showRight);
		mPager = (ViewPager) mView.findViewById(R.id.pager);
		mTabSwitcher = (TabSwitcher) mView.findViewById(R.id.switcher);
		
		PageFragment1 page1 = new PageFragment1();
		PageFragment2 page2 = new PageFragment2();
		PageFragment2 page3 = new PageFragment2();
		PageFragment2 page4 = new PageFragment2();
		pagerItemList.add(page1);
		pagerItemList.add(page2);
		pagerItemList.add(page3);
		pagerItemList.add(page4);
		mAdapter = new MyAdapter(getFragmentManager());
		mPager.setAdapter(mAdapter);
		mPager.setOffscreenPageLimit(4);
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				if (myPageChangeListener != null) {
					myPageChangeListener.onPageSelected(position);
					mTabSwitcher.setPosition(position);
//					mTabSwitcher.setFocusable(true);
//					mTabSwitcher.clearAnimation();
					Log.d("zhengpanyong", "onPageSelected:" + position);
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
				Log.d("zhengpanyong", "int arg0, float arg1, int arg2:" + arg0 +","+ arg1 +","+ arg2);
				
				
//	            imageView.startAnimation(animation);  
				
			}

			@Override
			public void onPageScrollStateChanged(int position) {

//				mTabSwitcher.setPosition(position);

			}
		});
		mTabSwitcher.setPager(mPager);
		return mView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		showLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showLeft();
			}
		});

		showRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((SlidingActivity) getActivity()).showRight();
			}
		});
	}

	public boolean isFirst() {
		if (mPager.getCurrentItem() == 0)
			return true;
		else
			return false;
	}

	public boolean isEnd() {
		if (mPager.getCurrentItem() == pagerItemList.size() - 1)
			return true;
		else
			return false;
	}

	public class MyAdapter extends /*FragmentPagerAdapter*/FragmentStatePagerAdapter {
		List<String> tagList ;
		FragmentManager mFragmentManager;
		
		public MyAdapter(FragmentManager fm) {
			super(fm);
			mFragmentManager = fm;
		}
		
		public void update(int item){  
		    Fragment fragment = mFragmentManager.findFragmentByTag(tagList.get(item));  
		    if(fragment != null){  
		        switch (item) {  
		        case 0:  
		            ((PageFragment1) fragment).update();  
		            break;  
		        case 1:  
		            ((PageFragment2) fragment).update();  
		            break;  
		        case 2:  
		            ((PageFragment2) fragment).update();  
		            break;  
		        case 3:  
		            ((PageFragment2) fragment).update();  
		            break;  
		        default:  
		            break;  
		        }  
		    }  
		}  

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
//			tagList.add(makeFragmentName(container.getId(), /*getItemId(position))*/pagerItemList.get(position).getId()));
			return super.instantiateItem(container, position);
		}

		@Override
		public int getCount() {
			return pagerItemList.size();
		}
		

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
//			return super.getItemPosition(object);
			return POSITION_NONE;
		}

		@Override
		public Fragment getItem(int position) {

			Fragment fragment = null;
			if (position < pagerItemList.size())
				fragment = pagerItemList.get(position);
			else
				fragment = pagerItemList.get(0);

			return fragment;

		}
	}

	private MyPageChangeListener myPageChangeListener;

	public void setMyPageChangeListener(MyPageChangeListener l) {

		myPageChangeListener = l;

	}

	public interface MyPageChangeListener {
		public void onPageSelected(int position);
	}

}
