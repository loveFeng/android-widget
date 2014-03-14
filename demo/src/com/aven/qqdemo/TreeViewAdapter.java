package com.aven.qqdemo;

import java.util.List;

import com.example.demo_highlights.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TreeViewAdapter extends ArrayAdapter {
	private LayoutInflater mInflater;
	private List<DotaElement> mfilelist;
	private Bitmap mIconCollapse;
	private Bitmap mIconExpand;

	public TreeViewAdapter(Context context, int textViewResourceId,
			List mfilelist) {
		super(context, textViewResourceId, mfilelist);
		this.mInflater = LayoutInflater.from(context);
		this.mfilelist = mfilelist;
		//��ԭʼͼƬֱ�ӽ�����Bitmap���ͣ�����������״̬��չ��״̬����ͼƬ��
		this.mIconCollapse = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.outline_list_collapse);
		this.mIconExpand = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.outline_list_expand);

	}



	public int getCount() {
		return mfilelist.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	class ViewHolder {
		TextView text;
		ImageView icon;
		
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		/*if (convertView == null) {*/
			convertView = mInflater.inflate(R.layout.outline, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		/*} else {
			holder = (ViewHolder) convertView.getTag();
		}*/
		//����Ķ༶��ʵ�Ǹ��ÿһ��item��ļ���Level��һ�����þ�����߾���Ĳ�ͬ��ʵ�ֵ�
		//�����չ��������������onListItemClick�¼���ʵ�ֵ�
		int level = mfilelist.get(position).getLevel();
			//levelԽ�ߣ�������ߵľ����ԽԶ������������ֲ�ͬ����item
			holder.icon.setPadding(25 * (level + 1), holder.icon
				.getPaddingTop(), 0, holder.icon.getPaddingBottom());
		holder.text.setText(mfilelist.get(position).getOutlineTitle());
		if (mfilelist.get(position).isMhasChild()
				&& (mfilelist.get(position).isExpanded() == false)) {
			holder.icon.setImageBitmap(mIconCollapse);
		} else if (mfilelist.get(position).isMhasChild()
				&& (mfilelist.get(position).isExpanded() == true)) {
			holder.icon.setImageBitmap(mIconExpand);
		} else if (!mfilelist.get(position).isMhasChild()){
			holder.icon.setImageBitmap(mIconCollapse);
			//���ﲻҪ����ΪGONE����ΪGONE����ʾ���Ҳ�ռλ�õģ���INVISIBLE�ǲ���ʾ����ռλ�õ�
			holder.icon.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
}
