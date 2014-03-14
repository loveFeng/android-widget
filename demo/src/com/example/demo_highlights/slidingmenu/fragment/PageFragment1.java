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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo_highlights.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;



public class PageFragment1 extends Fragment implements OnItemClickListener{
	ListView mListView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.page1, null);
		mListView = (ListView) view.findViewById(R.id.listview); 
		mListView.setAdapter(new SimpleAdapter(getActivity(), getData(),
                android.R.layout.simple_list_item_1, new String[] { "title" },
                new int[] { android.R.id.text1 }));
		mListView.setOnItemClickListener(this);
		
		mListView.setDivider(getResources().getDrawable(R.drawable.jblineshape));
		mListView.setDividerHeight(2);
//		mListView.setLayoutAnimation(/*controller*/getListAnim());
		mListView.setFastScrollEnabled(true);
		
		return view;
	}
	
	private LayoutAnimationController getListAnim() {
		AnimationSet set = new AnimationSet(true);
		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(200);
		set.addAnimation(animation);

		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
		Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
		-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(200);
		set.addAnimation(animation);
		
		Animation inAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		inAnimation.setDuration(200);
		inAnimation.setFillAfter(true);
		set.addAnimation(inAnimation);
		
		Animation outAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		outAnimation.setDuration(200);
		outAnimation.setFillAfter(true);
		// 进入动画控制器
		LayoutAnimationController inController = new LayoutAnimationController(inAnimation, 0.3f);
		// 退出动画控制器
		LayoutAnimationController outController = new LayoutAnimationController(outAnimation, 0.3f);
		
		LayoutAnimationController controller = new LayoutAnimationController(
		set, 0.5f);
		return controller;
		}
	
    protected List<Map<String, Object>> getData() {
    	List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();
    	String[] titles = getActivity().getResources().getStringArray(R.array.demo_names);
    	String[] intents = getActivity().getResources().getStringArray(R.array.demo_intents);
    	for (int i = 0; i < titles.length; ++i) {
    		addItem(myData, titles[i], new Intent(intents[i].toString()));
    	}
        
    	return myData;
    }
	
    protected void addItem(List<Map<String, Object>> data, String name, Intent intent) {
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("title", name);
        temp.put("intent", intent);
        data.add(temp);
    }
    

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
		// TODO Auto-generated method stub
        Map<String, Object> map = (Map<String, Object>)listview.getItemAtPosition(position);

        Intent intent = (Intent) map.get("intent");
        startActivity(intent);
        showAni(position);
        
	}
	
	public void update() {
		((SimpleAdapter)mListView.getAdapter()).notifyDataSetChanged();
	}
	
	public void showAni(int position) {
		switch (position%5) {
		case 0:
			getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout); 
			break;
		case 1:
			getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;
		case 2:
			getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out); 
			break;
		case 3:
			getActivity().overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
		default:
			break;
		}
	}


}

