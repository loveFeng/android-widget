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
import com.example.demo_highlights.slidingmenu.fragment.TabSwitcher.OnItemClickLisener;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


public class PageFragment2 extends Fragment implements OnItemClickListener{

	GridView mGrid;
	List<Map<String, Object>> myData;
	List<Drawable> myIcon;
	LayoutInflater layoutInflater;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.page2, null);
		layoutInflater = LayoutInflater.from(getActivity());
        mGrid = (GridView) view.findViewById(R.id.gridview);
        myData = getData();
        myIcon = getDateImage();
        mGrid.setAdapter(new AppsAdapter());
        mGrid.setOnItemClickListener((OnItemClickListener) this);
		return view;
	}
	
	public void update() {
		((AppsAdapter)mGrid.getAdapter()).notifyDataSetChanged();
	}
	
    protected List<Drawable> getDateImage() {
    	List<Drawable> myIcon = new ArrayList<Drawable>();
    	String[] titles = getActivity().getResources().getStringArray(R.array.demo_names);
    	for (int i = 0; i < titles.length; ++i) {
    		myIcon.add(getDrawableFromInt(i));
    	}
    	
    	return myIcon;
    }
    
    public Drawable getDrawableFromInt(int i) {
    	switch (i % 8) {
    	case 0:
    		return getActivity().getResources().getDrawable(R.drawable.s1);
    	case 1:
    		return getActivity().getResources().getDrawable(R.drawable.s2);
    	case 2:
    		return getActivity().getResources().getDrawable(R.drawable.s3);
    	case 3:
    		return getActivity().getResources().getDrawable(R.drawable.s4);
    	case 4:
    		return getActivity().getResources().getDrawable(R.drawable.s5);
    	case 5:
    		return getActivity().getResources().getDrawable(R.drawable.s6);
    	case 6:
    		return getActivity().getResources().getDrawable(R.drawable.s7);
    	case 7:
    		return getActivity().getResources().getDrawable(R.drawable.s8);
    	default:
    		return getActivity().getResources().getDrawable(R.drawable.s1);
    		
    	}
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
	
	static class ViewHolder{
		ImageView imageView;
		TextView textView;
	}
    
	 public class AppsAdapter extends BaseAdapter {
	        public AppsAdapter() {
	        }

	        public View getView(int position, View convertView, ViewGroup parent) {
	            ImageView i;

	            if (convertView == null) {
	            	convertView = layoutInflater.inflate(R.layout.lay1_item, null);
	            	ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_imageView);
	            	TextView textView = (TextView) convertView.findViewById(R.id.grid_textView);
//	                i = new ImageView(getActivity());
//	                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//	                imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
	            	imageView.setImageDrawable(myIcon.get(position));
	            	textView.setText(myData.get(position).get("title").toString());
	            } else {
	            	ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_imageView);
	            	TextView textView = (TextView) convertView.findViewById(R.id.grid_textView);
//	                i = new ImageView(getActivity());
//	                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//	                imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
	            	imageView.setImageDrawable(myIcon.get(position));
	            	textView.setText(myData.get(position).get("title").toString());
	            }

//	            ResolveInfo info = mApps.get(position);
	            
//	            i.setImageDrawable(myIcon.get(position)/*getActivity().getResources().getDrawable(R.drawable.ic_launcher)*//*info.activityInfo.loadIcon(getPackageManager())*/);

	            return convertView;
	        }


	        public final int getCount() {
	            return myData.size();
	        }

	        public final Object getItem(int position) {
	            return myData.get(position);
//	        	return null;
	        }

	        public final long getItemId(int position) {
	            return position;
	        }
	    }

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onItemClick(AdapterView<?> Gridview, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
        Map<String, Object> map = (Map<String, Object>)Gridview.getItemAtPosition(position);

        Intent intent = (Intent) map.get("intent");
        startActivity(intent);
	}

}

