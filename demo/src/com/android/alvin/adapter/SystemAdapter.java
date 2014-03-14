package com.android.alvin.adapter;

import java.util.List;

import com.example.demo_highlights.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SystemAdapter extends BaseAdapter {
	private List<Drawable> icons;
	private List<String> labels;
	private Context context;
	private LayoutInflater inflater;
	
	public SystemAdapter(Context context, List<Drawable> icons, List<String> labels){
		this.context = context;
		this.icons = icons;
		this.labels = labels;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return labels.size();
	}

	@Override
	public Object getItem(int position) {
		return labels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		SystemViewHolder holder;
		if(view==null){
			view = inflater.inflate(R.layout.system_list_item, null);
			holder = new SystemViewHolder();
			holder.iconView = (ImageView)view.findViewById(R.id.system_app_icon);
			holder.labelView = (TextView)view.findViewById(R.id.system_app_name);
			view.setTag(holder);
		}else{
			holder = (SystemViewHolder)view.getTag();
		}
		holder.iconView.setImageDrawable(icons.get(position));
		holder.labelView.setText(labels.get(position));
		return view;
	}
	
	class SystemViewHolder{
		ImageView iconView;
		TextView labelView;
	}

}
