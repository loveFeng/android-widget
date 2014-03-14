package com.android.alvin.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.demo_highlights.R;

import android.R.integer;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InstallAdapter extends BaseAdapter {
	private Context context;
	private List<Drawable> iconList;
	private List<String> labelList;
	private LayoutInflater inflater;
	private int currentPosition = -1;
	private List<String> selectedList = new ArrayList<String>();
	InstallViewHolder holder;
	
	public InstallAdapter(Context context, List<Drawable> iconList, List<String> labelList){
		this.context = context;
		this.iconList = iconList;
		this.labelList = labelList;
		inflater = LayoutInflater.from(context);
	}
	
	public int getPosition(){
		return currentPosition;
	}
	
	public List<String> getSelectedList(){
		return selectedList;
	}
	
	@Override
	public int getCount() {
		return labelList.size();
	}

	@Override
	public Object getItem(int position) {
		return labelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		if(view==null){
			holder = new InstallViewHolder();
			view = inflater.inflate(R.layout.app_list_item, null);
			holder.checkboxView = (ImageView)view.findViewById(R.id.checkbox_icon);
			holder.imageView = (ImageView)view.findViewById(R.id.app_icon);
			holder.textView = (TextView)view.findViewById(R.id.app_name);
			view.setTag(holder);
		}else{
			holder = (InstallViewHolder)view.getTag();
		}
		final String posi = String.valueOf(position);
		if(selectedList.contains(posi))
			holder.checkboxView.setImageResource(R.drawable.checkbox4);
		else 
			holder.checkboxView.setImageResource(R.drawable.checkbox3);
		holder.imageView.setImageDrawable(iconList.get(position));
		holder.textView.setText(labelList.get(position));
		
		holder.checkboxView.setOnClickListener(new ImageView.OnClickListener(){
			@Override
			public void onClick(View v) {
				ImageView checkboxView = (ImageView)v.findViewById(R.id.checkbox_icon);
				if(selectedList.contains(posi)){
					selectedList.remove(posi);
					checkboxView.setImageResource(R.drawable.checkbox3);
				}else{
					selectedList.add(posi);
					checkboxView.setImageResource(R.drawable.checkbox4);
				}
			}
		});
		return view;
	}
	
	class InstallViewHolder{
		ImageView checkboxView;
		ImageView imageView;
		TextView textView;
	}

}
