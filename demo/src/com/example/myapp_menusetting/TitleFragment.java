package com.example.myapp_menusetting;


import com.example.demo_highlights.R;

import android.app.ListFragment;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;


public class TitleFragment extends ListFragment {
	    

	    @Override
	    public void onCreate(Bundle icicle) {
	        super.onCreate(icicle);
	    }

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        return inflater.inflate(R.layout.device_admin_settings, container, false);
	    }

	    @Override
	    public void onResume() {
	        super.onResume();
	        //updateList();
	    }

	    void updateList() {   
	        getListView().setAdapter(new PolicyListAdapter());
	    }
	    void updateList(long[] list, String[] listTitle, String[] listArtist, Drawable[] listDrawable) {   
	    	PolicyListAdapter Adapter = new PolicyListAdapter();
	    	Adapter.setData(list.length, list, listTitle, listArtist, listDrawable);
	        getListView().setAdapter(Adapter);
	    }

	    @Override
	    public void onListItemClick(ListView l, View v, int position, long id) {

	    }

        static class ViewHolder {
            TextView line1;
            TextView line2;
            TextView duration;
            ImageView play_indicator;
			// add by zhengpanyong 2013.1.21
			ToggleButton toggleButton;
			ProgressBar timeProgressBar;
			ImageView icon;
			CheckBox checkBox;
			// add end
            CharArrayBuffer buffer1;
            char [] buffer2;
            /// M: add the ViewHolder members, DRM icon and Edit icon @{
            ImageView mDrmLock;
            ImageView mEditIcon;
            /// @}
        }
	    
	    class PolicyListAdapter extends BaseAdapter {
	        final LayoutInflater mInflater;
	        private int mCount = 0;
	        private long[] mIdList;
	        private String[] mTitleList;
	        private String[] mArtistList;
	        private Drawable[] mDrawableList;
	        
	        PolicyListAdapter() {
	            mInflater = (LayoutInflater)
	                    getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        }
	        public void setData(int count, long[] list, String[] listTitle, String[] listArtist, Drawable[] listDrawable) {
	        	mCount = count;
	        	mIdList = list;
	        	mTitleList = listTitle;
	        	mArtistList = listArtist;
	        	mDrawableList = listDrawable;
	        }

	        public boolean hasStableIds() {
	            return true;
	        }
	        
	        public int getCount() {
//	            return mAvailableAdmins.size();
	        	return mCount;
	        }

	        public Object getItem(int position) {
//	            return mAvailableAdmins.get(position);
	        	return null;
	        }

	        public long getItemId(int position) {
	            return position;
	        }

	        public boolean areAllItemsEnabled() {
	            return false;
	        }

	        public boolean isEnabled(int position) {
	            return true;
	        }

	        public View getView(int position, View convertView, ViewGroup parent) {
	            View v;
	            if (convertView == null) {
	                v = newView(parent);
	            } else {
	                v = convertView;
	            }
	            bindView(v, position);
	            return v;
	        }
	        
	        public View newView(ViewGroup parent) {
	            View v = mInflater.inflate(R.layout.track_list_item, parent, false);
	            ViewHolder vh = new ViewHolder();
	            vh.line1 = (TextView) v.findViewById(R.id.line1);
	            vh.line2 = (TextView) v.findViewById(R.id.line2);
	            vh.duration = (TextView) v.findViewById(R.id.duration);
	            vh.play_indicator = (ImageView) v.findViewById(R.id.play_indicator);
	            vh.icon = (ImageView)v.findViewById(R.id.icon);
	            vh.checkBox = (CheckBox)v.findViewById(R.id.select);

				  // add by zhengpanyong 2013.1.25
				  vh.timeProgressBar = (ProgressBar) v
						.findViewById(R.id.time_progressBar);
				  vh.toggleButton = (ToggleButton) v.findViewById(R.id.broadcast);
				  // add end         
	            vh.buffer1 = new CharArrayBuffer(100);
	            vh.buffer2 = new char[200];
	            /// M: get the ImageView of drm_lcok and play_indicator.
	            vh.mDrmLock = (ImageView) v.findViewById(R.id.drm_lock);
	            /// M: when on Edit mode,get the ImagerView of edit icon @{
//	            if (mActivity.mEditMode) {
//	                vh.mEditIcon = (ImageView) v.findViewById(R.id.icon);
//	            }
	            /// @}
	            v.setTag(vh);
	            return v;
	        }
	        
	        public void bindView(View view, int position) {
	            ViewHolder vh = (ViewHolder) view.getTag();
	            vh.line1.setVisibility(View.VISIBLE);
	            vh.line2.setVisibility(View.VISIBLE);
	            vh.line1.setText(mTitleList[position]);
	            vh.line2.setText(mArtistList[position]);
	            vh.icon.setImageDrawable(mDrawableList[position]);

				//vh.icon.setImageDrawable();

	        }
	    }
	}
