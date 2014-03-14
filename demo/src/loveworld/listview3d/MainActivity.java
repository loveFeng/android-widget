package loveworld.listview3d;

import java.util.ArrayList;
import java.util.List;

import com.example.demo_highlights.R;

import loveworld.listview3d.util.Dynamics;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_listview3d);
        
        ListView3D listView = (ListView3D) findViewById(R.id.my_listview);
        final CustomAdapter customAdapter = new CustomAdapter(this, createTestData());
        listView.setAdapter(customAdapter);
        
        listView.setDynamics(new SimpleDynamics(0.9f, 0.6f));
        
        // item Click
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String itemName = (String) customAdapter.getItem(position);
				Toast.makeText(getBaseContext(), "���   " + itemName, 
						Toast.LENGTH_SHORT).show();
			}
		});
        
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				String itemName = (String) customAdapter.getItem(position);
				Toast.makeText(getBaseContext(), "����  " + itemName, 
						Toast.LENGTH_SHORT).show();
				return true;
			}
		});
    }
	
	// ===========================================================
	// Private Methods
	// ===========================================================

	/**
	 * ListView��ݴ���
	 */
	private List<String> createTestData() {
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < 50; i++) {
			data.add("Love World " + i);
		}
		return data;
	}
    
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================


	private class CustomAdapter extends BaseAdapter {

		private List<String> mData;
		private LayoutInflater mInflater;
		
		public CustomAdapter(Context context, List<String> data) {
			mData = data;
			mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			if (mData == null || mData.size() <= 0) {
				return 0;
			}
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			if (mData == null || mData.size() <= 0
					|| position < 0 || position >= mData.size()) {
				return null;
			}
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_item_listview3d, null);
			}
			
			TextView name = (TextView) convertView.findViewById(R.id.tv_name);
			name.setText( (CharSequence) getItem(position) );
			
			return convertView;
		}
		
	}
	
	class SimpleDynamics extends Dynamics {

        private float mFrictionFactor;
        private float mSnapToFactor;

        public SimpleDynamics(final float frictionFactor, final float snapToFactor) {
            mFrictionFactor = frictionFactor;
            mSnapToFactor = snapToFactor;
        }

        @Override
        protected void onUpdate(final int dt) {
            mVelocity += getDistanceToLimit() * mSnapToFactor;

            // �ٶ� * ʱ����  = ���ʱ����λ��
            mPosition += mVelocity * dt / 1000;
            
            // ���٣� ���´�onUpdateʹ��
            mVelocity *= mFrictionFactor;
        }
	}

}