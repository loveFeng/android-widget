package com.android.alvin.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.demo_highlights.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskAdapter extends BaseAdapter {
	private Context context;
	private List<Drawable> icons;
	private List<String> lables;
	private List<String> packageList;
	private List<String> excludeList;
	private LayoutInflater inflater;
	private List<String> selectedList = new ArrayList<String>();
	
	public TaskAdapter(Context context,List<Drawable> icons, List<String> labels,List<String> packageList, List<String> excludeList){
		this.context = context;
		this.icons = icons;
		this.lables = labels;
		this.packageList = packageList;
		this.excludeList = excludeList;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return icons.size();
	}

	@Override
	public Object getItem(int position) {
		return lables.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void addSelectedItem(String position){
		selectedList.add(position);
	}
	
	public void removeSelectedItem(String position){
		selectedList.remove(position);
	}
	
	public void removeAllItems(){
		selectedList.clear();
	}
	
	public List<String> getSelectedList(){
		return selectedList;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if(view==null){
			view = inflater.inflate(R.layout.app_list_item, null);
			holder = new ViewHolder();
			//holder.cb = (CheckBox)view.findViewById(R.id.app_checkbox);
			holder.cbImageView = (ImageView)view.findViewById(R.id.checkbox_icon);
			holder.imageView = (ImageView)view.findViewById(R.id.app_icon);
			holder.text = (TextView)view.findViewById(R.id.app_name);
			view.setTag(holder);
		}else{
			holder = (ViewHolder)view.getTag();
		}
		if(selectedList.contains(String.valueOf(position)))
			holder.cbImageView.setImageResource(R.drawable.checkbox4);
		else if(excludeList.contains(packageList.get(position))){
			holder.cbImageView.setImageResource(R.drawable.exclude);
		}else
			holder.cbImageView.setImageResource(R.drawable.checkbox3);
		holder.imageView.setImageDrawable(icons.get(position));
		holder.text.setText(lables.get(position));
		return view;
	}
	
	class ViewHolder{
		ImageView cbImageView;
		ImageView imageView;
		TextView text;
	}

}
