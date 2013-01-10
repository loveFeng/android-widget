/*
 * Created by rancaihe, 20121119
 * Modified by rancaihe, 20121211
 */
package android.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.AdapterView.OnItemClickListener;
import com.android.inputmethod.pinyin.R;

/** @hide */
public class GroupListAdapter extends BaseAdapter{
	public static final int TAG_TYPE_SMALL = 0;
	public static final int TAG_TYPE_NORMAL = 1;
	public static final int TAG_TYPE_TEXT = 2;
	public static final int ITEM_TYPE_TEXT_ICON = 3;
	public static final int ITEM_TYPE_TOGGLE = 4;
	public static final int ITEM_TYPE_TEXT_ONLY = 5;
	public static final int ITEM_TYPE_TEXT_TWO_LINE = 6;
	public static final int ITEM_TYPE_SEEKBAR = 7;
	public static final int ITEM_TYPE_CHECKBOX = 8;
	public static final int ITEM_TYPE_COUNT = ITEM_TYPE_CHECKBOX + 1;
	
	public static final int ITEM_BACKGROUND_TOP = 0;
	public static final int ITEM_BACKGROUND_MIDDLE = 1;
	public static final int ITEM_BACKGROUND_BOTTOM = 2;
	public static final int ITEM_BACKGROUND_SINGLE = 3;
	public static final int ITEM_BACKGROUND_NONE = 4;
	
	private List<Item> mItems = new ArrayList<Item>();
	
	Context mContext;
	LayoutInflater mInflater = null;
	Resources mResources;
	int mTotalHeight = 0;
	
    private int mCustomCount = 0;
    private boolean mFirstEnter;
    private boolean mCanScroll = true;
    private int mSelectItem = -1;
    private String mSelectText;
    
	public GroupListAdapter(Context context, List<Item> items) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mResources = context.getResources();
		mItems = items;
		mFirstEnter = true;
		mSelectText = null;
	}
	
	@Override
    public int getItemViewType(int position) {
        Item item = (Item)getItem(position);
        return item.mType;
    }
	
	@Override
    public int getViewTypeCount() {
        return ITEM_TYPE_COUNT + mCustomCount;
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mItems.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view =  convertView;
		ViewHolder holder = null;
		
		if(position == 0){
			mTotalHeight = 0;
		}
		if(mFirstEnter && parent instanceof AbsListView){
			mFirstEnter = false;
			mTotalHeight = 0;
            ((AbsListView)parent).setOnHierarchyChangeListener(mItemChangeListener);
		}
		Item item = (Item)getItem(position);
		int type = item.mType;
		if(convertView == null){
			holder = new ViewHolder();
			switch (type) {
			case TAG_TYPE_SMALL:
			case TAG_TYPE_NORMAL:
			case TAG_TYPE_TEXT:
				view = createTagByType(type);
				holder.title = (TextView)view;
				break;
			case ITEM_TYPE_TEXT_ICON:
				view = mInflater.inflate(R.layout.item_texts, parent, false);
                holder.title = (TextView) view.findViewById(R.id.text1);
                holder.hint = (TextView) view.findViewById(R.id.text2);
				break;
			case ITEM_TYPE_TOGGLE:
				view = mInflater.inflate(R.layout.item_togglebutton, parent, false);
                holder.title = (TextView) view.findViewById(R.id.text1);
                holder.slipButton = (SlipButton)view.findViewById(R.id.toggle);
				break;
			case ITEM_TYPE_TEXT_ONLY:
				view = mInflater.inflate(R.layout.item_centertext, parent, false);
                holder.title = (TextView) view.findViewById(R.id.text1);
				break;
			case ITEM_TYPE_CHECKBOX:
				view = mInflater.inflate(R.layout.item_checkbox, parent, false);
                holder.title = (TextView) view.findViewById(R.id.text1);
                holder.checkBox = (CheckBox)view.findViewById(R.id.checkbox);
                if(parent instanceof AbsListView){
                	((AbsListView)parent).setOnItemClickListener(mItemClickListener);
                }
                break;
			case ITEM_TYPE_SEEKBAR:
				view = mInflater.inflate(R.layout.item_slide, parent, false);
                holder.icon1 = (ImageView) view.findViewById(R.id.icon1);
                holder.icon2 = (ImageView)view.findViewById(R.id.icon2);
                holder.seekBar = (SeekBar)view.findViewById(R.id.progress);
				break;
			case ITEM_TYPE_TEXT_TWO_LINE:
				view = mInflater.inflate(R.layout.item_two_line, parent, false);
				holder.title = (TextView) view.findViewById(R.id.text1);
				holder.hint = (TextView) view.findViewById(R.id.text2);
                holder.icon1 = (ImageView) view.findViewById(R.id.icon);
                holder.icon2 = (ImageView)view.findViewById(R.id.icon2);
				break;
			default:
				if(item instanceof CustomListItem){
					CustomListItem customItem = (CustomListItem)item;
					view = mInflater.inflate(customItem.mResId, parent, false);
					if(customItem.mCallback != null){
						view = customItem.mCallback.newViewCallback(position, view, parent);
					}
				}
				break;
			}
			if(type < ITEM_TYPE_COUNT){
				view.setTag(holder);
			}
		}else{
			if(type < ITEM_TYPE_COUNT){
				holder = (ViewHolder)view.getTag();
			}
		}
		
		switch(type){
		case TAG_TYPE_SMALL:
		case TAG_TYPE_NORMAL:
			holder.title.setText("");
			break;
		case TAG_TYPE_TEXT:
			holder.title.setText(item.mText);
			if(item instanceof TagTextItem){
				TagTextItem tagItem = (TagTextItem)item;
				
				switch(tagItem.mStyleType){
				case TagTextItem.TAG_STYLE_LEFT:
					holder.title.setTextAppearance(mContext, R.style.ListTextTag);
					break;
				case TagTextItem.TAG_STYLE_CENTER:
					holder.title.setTextAppearance(mContext, R.style.ListTextTagGray);
					holder.title.setGravity(Gravity.CENTER|Gravity.TOP);
					break;
				case TagTextItem.TAG_STYLE_CUSTOM:
					if(tagItem.mTextAppearance != 0){
						holder.title.setTextAppearance(mContext, tagItem.mTextAppearance);
					}else{
						holder.title.setTextAppearance(mContext, R.style.ListTextTag);
					}
					break;
				}
			}else{
				holder.title.setTextAppearance(mContext, R.style.ListTextTag);
			}
			break;
		case ITEM_TYPE_TEXT_ICON:
			GroupListItem textIconItem = (GroupListItem)item;
            holder.title.setText(textIconItem.mText);
            if(textIconItem.mIcon != 0){
            	holder.title.setCompoundDrawablesWithIntrinsicBounds(mResources
            			.getDrawable(textIconItem.mIcon),
						null, null, null);
            	holder.title.setPadding(0, 0, 0, 0);
            }else{
            	holder.title.setCompoundDrawablesWithIntrinsicBounds(null,
						null, null, null);
            	holder.title.setPadding(mResources.getDimensionPixelSize(R.dimen.list_item_margin), 0, 0, 0);
            }
            if(textIconItem.mHint == null || textIconItem.mHint == ""){
            	holder.hint.setText("");
            }else{
	            holder.hint.setText(textIconItem.mHint);
	            if(textIconItem.mHintTextAppearance != 0){
	            	holder.hint.setTextAppearance(mContext, textIconItem.mHintTextAppearance);
	            }else{
	            	holder.hint.setTextAppearance(mContext, R.style.ListTextBule);
	            }
	            holder.hint.setVisibility(View.VISIBLE);
            }
            
            switch(textIconItem.mArrowType){
            case GroupListItem.ITEM_ARROW_TYPE_NONE:
            	holder.hint.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            	break;
            case GroupListItem.ITEM_ARROW_TYPE_NORMAL:
            	holder.hint.setCompoundDrawablesWithIntrinsicBounds(null,
						null, mResources
            			.getDrawable(R.drawable.list_item_small_arrow), null);
            	break;
            case GroupListItem.ITEM_ARROW_TYPE_CIRCLE:
            	holder.hint.setCompoundDrawablesWithIntrinsicBounds(null,
						null, mResources
            			.getDrawable(R.drawable.list_item_arrow), null);
            	break;
            }
			break;
		case ITEM_TYPE_TOGGLE:
			SlipListItem slipItem = (SlipListItem)item;
			holder.title.setText(slipItem.mText);
            if(slipItem.mIcon != 0){
            	holder.title.setCompoundDrawablesWithIntrinsicBounds(mResources
            			.getDrawable(slipItem.mIcon),
						null, null, null);
            	holder.title.setPadding(0, 0, 0, 0);
            }else{
            	holder.title.setCompoundDrawablesWithIntrinsicBounds(null,
						null, null, null);
            	holder.title.setPadding(mResources.getDimensionPixelSize(R.dimen.list_item_margin), 0, 0, 0);
            }
            if(slipItem.mOnResId != 0){
            	holder.slipButton.setOnImage(slipItem.mOnResId);
            }
            holder.slipButton.setChecked(slipItem.mIsChecked);
            holder.slipButton.setTag(position);
            holder.slipButton.setOnCheckedChangeListener(slipItem.mCheckListener);
			break;
		case ITEM_TYPE_TEXT_ONLY:
			GroupListItem textItem = (GroupListItem)item;
			holder.title.setText(textItem.mText);
			break;
		case ITEM_TYPE_CHECKBOX:
			CheckBoxListItem checkItem = (CheckBoxListItem)item;
			holder.title.setText(checkItem.mText);
			
			if(checkItem.mIsRadio){
				if(mSelectItem == -1){
					if(mSelectText != null && mSelectText.equals(checkItem.mText)){
						holder.checkBox.setChecked(true);
					}else{
						holder.checkBox.setChecked(false);
					}
				}else if(mSelectItem == position){
					holder.checkBox.setChecked(true);
				}else{
					holder.checkBox.setChecked(false);
				}
			}else{
				holder.checkBox.setChecked(checkItem.mIsChecked);
				holder.checkBox.setOnCheckedChangeListener(checkItem.mCheckListener);
			}
			holder.checkBox.setTag(position);   
			break;
		case ITEM_TYPE_SEEKBAR:
			SeekBarListItem seekBarItem = (SeekBarListItem)item;
			holder.seekBar.setMax(seekBarItem.maxValue);
			holder.seekBar.setProgress(seekBarItem.currentValue);
			holder.seekBar.setOnSeekBarChangeListener(seekBarItem.mListener);
			switch(seekBarItem.mSeekbarType){
			case SeekBarListItem.SEEKBAR_TYPE_SOUND:
				holder.icon1.setImageResource(R.drawable.item_sound_small_icon);
				holder.icon2.setImageResource(R.drawable.item_sound_large_icon);
				break;
			case SeekBarListItem.SEEKBAR_TYPE_LIGHT:
				holder.icon1.setImageResource(R.drawable.item_light_small_icon);
				holder.icon2.setImageResource(R.drawable.item_light_large_icon);
				break;
			case SeekBarListItem.SEEKBAR_TYPE_CUSTOM:
				holder.icon1.setImageResource(seekBarItem.mLeftIcon);
				holder.icon2.setImageResource(seekBarItem.mRightIcon);
				holder.seekBar.setThumb(mResources.getDrawable(seekBarItem.mThumb));
				holder.seekBar.setProgressDrawable(mResources.getDrawable(seekBarItem.mProgressBg));
				break;
			}
			break;
		case ITEM_TYPE_TEXT_TWO_LINE:
			GroupListItem twoLineItem = (GroupListItem)item;
            holder.title.setText(twoLineItem.mText);
            if(twoLineItem.mIcon > 0){
            	holder.icon1.setBackgroundResource(twoLineItem.mIcon);
            }
            if(twoLineItem.mHint == null || twoLineItem.mHint == ""){
            	holder.hint.setVisibility(View.GONE);
            }else{
	            holder.hint.setText(twoLineItem.mHint);
	            if(twoLineItem.mHintTextAppearance != 0){
	            	holder.hint.setTextAppearance(mContext, twoLineItem.mHintTextAppearance);
	            }else{
	            	holder.hint.setTextAppearance(mContext, R.style.ListTextGray);
	            }
	            holder.hint.setVisibility(View.VISIBLE);
            }
            holder.icon2.setBackgroundResource(R.drawable.list_item_small_arrow);
			break;
		default:
			if(item instanceof CustomListItem){
				CustomListItem customItem = (CustomListItem)item;
				if(customItem.mCallback != null){
					view = customItem.mCallback.bindViewCallback(position, view, parent);
				}
				if(!customItem.mDefaultBg){
					return view;
				}
			}
			break;
		}
		
		int background = getBackground(position);
		switch(background){
		case ITEM_BACKGROUND_TOP:
			view.setBackgroundResource(R.drawable.list_item_selector_up);
			break;
		case ITEM_BACKGROUND_MIDDLE:
			view.setBackgroundResource(R.drawable.list_item_selector_middle);
			break;
		case ITEM_BACKGROUND_BOTTOM:
			view.setBackgroundResource(R.drawable.list_item_selector_down);
			break;
		case ITEM_BACKGROUND_SINGLE:
			view.setBackgroundResource(R.drawable.list_item_selector_single);
			break;
		default:
			view.setBackgroundColor(Color.TRANSPARENT);
			break;
		}
		return view;
	}
	
	private OnItemClickListener mItemClickListener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
			// TODO Auto-generated method stub
			Item currItem = (Item)getItem(position);
			if(currItem instanceof CheckBoxListItem){
				ViewHolder vHoler = (ViewHolder)v.getTag();
				CheckBoxListItem item = (CheckBoxListItem)currItem;
				if(!vHoler.checkBox.isChecked()){
					vHoler.checkBox.setChecked(true);
					if(item.mCheckListener != null){
						item.mCheckListener.onCheckedChanged(vHoler.checkBox, true);
					}
				}
				mSelectItem = position;
				notifyDataSetChanged();
			}
		}
		
	};
	
	private OnHierarchyChangeListener mItemChangeListener = new OnHierarchyChangeListener(){

		@Override
		public void onChildViewAdded(View parent, View child) {
			// TODO Auto-generated method stub
			int parentHeight = parent.getMeasuredHeight();
			if(mTotalHeight >= parentHeight){
				return;
			}
			int childHeight = 0;
			LayoutParams lp = child.getLayoutParams();
			if(lp != null){
				childHeight = lp.height;
				if(childHeight < 0){
					child.measure(0, 0);
					childHeight = child.getMeasuredHeight();
				}
			}else{
				child.measure(0, 0);
				childHeight = child.getMeasuredHeight();
			}
			mTotalHeight += childHeight;
			if(mTotalHeight < parentHeight
					&& ((ViewGroup)parent).indexOfChild(child) == mItems.size() - 1
					&& child instanceof TextView ){
				int footerHeight = parentHeight - mTotalHeight + childHeight;
				android.util.Log.d("rancaihe","footerHeight = "+footerHeight);
				((TextView)child).setHeight(footerHeight);
				
				mTotalHeight = parentHeight;
			}
		}

		@Override
		public void onChildViewRemoved(View parent, View child) {
			// TODO Auto-generated method stub
			mTotalHeight -= child.getHeight();
		}
	};
	
	private int getBackground(int position){
		boolean top = false;
		boolean bottom = false;
		Item item = (Item)getItem(position);
		
		if(item.mType < ITEM_TYPE_TEXT_ICON){
			return ITEM_BACKGROUND_NONE;
		}
		if(position == 0){
			top = true;
		}else if (((Item)getItem(position - 1)).mType < ITEM_TYPE_TEXT_ICON) {
			top = true;
		}
		
		if(position == getCount() - 1){
			bottom = true;
		}else if (((Item)getItem(position + 1)).mType < ITEM_TYPE_TEXT_ICON) {
			bottom = true;
		}

		if (top && bottom) {
			return ITEM_BACKGROUND_SINGLE;
		} else if (top) {
			return ITEM_BACKGROUND_TOP;
		} else if (bottom) {
			return ITEM_BACKGROUND_BOTTOM;
		} else {
			return ITEM_BACKGROUND_MIDDLE;
		}
	}
	
	private View createTagByType(int type){
		TextView tag = null;
		
		int tagHeight = 0;
		switch(type){
		case TAG_TYPE_SMALL:
			tag = new TextView(mContext);
			tagHeight = mResources.getDimensionPixelSize(R.dimen.list_tag_height_small);
			tag.setHeight(tagHeight);
			break;
		case TAG_TYPE_NORMAL:
			tag = new TextView(mContext);
			tagHeight = mResources.getDimensionPixelSize(R.dimen.list_tag_height_normal);
			tag.setHeight(tagHeight);
			break;
		case TAG_TYPE_TEXT:
			tag = (TextView)mInflater.inflate(R.layout.item_tag, null, false);
			break;
		default:
			tag = null;
			break;
		}
		
		return tag;
	}
	
	public void setCustomTypeCount(int count){
		mCustomCount = count;
	}
	private class ViewHolder {
        TextView title;
        TextView hint;
        SlipButton slipButton;
        CheckBox checkBox;
        ImageView icon1;
        ImageView icon2;
        SeekBar seekBar;
    }
	
	public static Item createItem(int type){
		return new Item(type);
	}
	
	public static Item createItem(int type, String text){
		return new Item(type, text);
	}
	
	public static GroupListItem createItem(int type, String text, Intent intent){
		return new GroupListItem(type, text, intent);
	}
	
	public static GroupListItem createItem(int type, String text, int icon,
			Intent intent) {
		return new GroupListItem(type, text, icon, intent);
	}

	public static GroupListItem createItem(int type, String text, int icon,
			String hint, int hintTextAppearance, int arrowType, Intent intent) {
		return new GroupListItem(type, text, icon, hint, hintTextAppearance, arrowType,
				intent);
	}
	
	public static TagTextItem createTagItem(String text, int tagStyleType, int textAppearance) {
		return new TagTextItem(text, tagStyleType, textAppearance);
	}

	public static SlipListItem createSlipItem(String text, int icon,
			boolean isChecked, SlipButton.OnCheckedChangeListener listener) {
		return new SlipListItem(text, icon, isChecked, listener);
	}
	
	public static SlipListItem createSlipItem(String text, int icon,
			boolean isChecked, int resId, SlipButton.OnCheckedChangeListener listener) {
		return new SlipListItem(text, icon, isChecked, resId, listener);
	}

	public static CheckBoxListItem createCheckItem(int type, String text,
			boolean isChecked, OnCheckedChangeListener listener) {
		return new CheckBoxListItem(type, text, isChecked, listener);
	}
	
	public static CheckBoxListItem createCheckItem(int type, String text,
			boolean isChecked, boolean isRadio, OnCheckedChangeListener listener) {
		CheckBoxListItem item = new CheckBoxListItem(type, text, isChecked, listener);
		item.mIsRadio = isRadio;
		return item;
	}
	
	public static SeekBarListItem createSeekBarItem(int seekbarType, int min,
			int max, int current, OnSeekBarChangeListener listener) {
		return new SeekBarListItem(seekbarType, min, max, current, listener);
	}
	
	public static SeekBarListItem createSeekBarItem(int min, int max,
			int current, int leftResId, int rightResId, int thumb,
			int progressBg, OnSeekBarChangeListener listener) {
		return new SeekBarListItem(min, max, current, leftResId, rightResId,
				thumb, progressBg, listener);
	}
	
	public static CustomListItem createCustomItem(int type, int resId,
			boolean defaultBg, Intent intent, Callback callback) {
		return new CustomListItem(type, resId, defaultBg, intent, callback);
	}

	public void refresh(List<Item> items) {
		mItems = items;
		mFirstEnter = true;
		notifyDataSetChanged();
	}
	
	public void setCanScroll(boolean canScroll) {
		mCanScroll = canScroll;
	}

	public void setSelectedItem(int select){
		mSelectItem = select;
	}
	
	public void setSelectedText(String text){
		mSelectText = text;
	}

	/** @hide */
	public static class Item {
		public String mText;
		public int mType;
		public int mTextAppearance;

		Item(int type) {
			this(type, "");
		}

		Item(int type, String text) {
			mText = text;
			mType = type;
		}
	}
	
	/** @hide */
	public static class TagTextItem extends Item{
		public static final int TAG_STYLE_LEFT = 0;
		public static final int TAG_STYLE_CENTER = 1;
		public static final int TAG_STYLE_CUSTOM = 2;
		
		public int mStyleType;
		
		TagTextItem(String text, int tagStyleType, int textAppearance) {
			super(TAG_TYPE_TEXT, text);
			mStyleType = tagStyleType;
			mTextAppearance = textAppearance;
		}
	}

	/** @hide */
	public static class GroupListItem extends Item {
		public static final int ITEM_ARROW_TYPE_NORMAL = 0;
		public static final int ITEM_ARROW_TYPE_CIRCLE = 1;
		public static final int ITEM_ARROW_TYPE_NONE = 2;

		public int mIcon;
		public Intent mIntent;
		public String mHint;
		public int mHintTextAppearance;
		public int mArrowType;

		GroupListItem(int type, String text, Intent intent) {
			this(type, text, 0, intent);
		}

		GroupListItem(int type, String text, int icon, Intent intent) {
			super(type, text);
			mIcon = icon;
			mIntent = intent;
			mHint = "";
			mArrowType = ITEM_ARROW_TYPE_NORMAL;
		}

		GroupListItem(int type, String text, int icon, String hint,
				int hintTextAppearance, int arrowType, Intent intent) {
			super(type, text);
			mIcon = icon;
			mIntent = intent;
			if(hint == null){
				mHint = "";
			}else{
				mHint = hint;
			}
			mHintTextAppearance = hintTextAppearance;
			mArrowType = arrowType;
		}
	}

	/** @hide */
	public static class SlipListItem extends Item {
		public int mIcon;
		public boolean mIsChecked = false;
		public int mOnResId;
		public SlipButton.OnCheckedChangeListener mCheckListener;

		SlipListItem(String text, int icon, boolean isChecked,
				SlipButton.OnCheckedChangeListener listener) {
			super(ITEM_TYPE_TOGGLE, text);
			mIcon = icon;
			mIsChecked = isChecked;
			mCheckListener = listener;
			mOnResId = 0;
		}
		
		SlipListItem(String text, int icon, boolean isChecked,
				int resId, SlipButton.OnCheckedChangeListener listener) {
			super(ITEM_TYPE_TOGGLE, text);
			mIcon = icon;
			mIsChecked = isChecked;
			mCheckListener = listener;
			mOnResId = resId;
		}
	}

	/** @hide */
	public static class CheckBoxListItem extends Item {
		public boolean mIsChecked = false;
		public boolean mIsRadio;
		public OnCheckedChangeListener mCheckListener;

		CheckBoxListItem(int type, String text, boolean isChecked,
				OnCheckedChangeListener listener) {
			super(ITEM_TYPE_CHECKBOX, text);
			mIsChecked = isChecked;
			mCheckListener = listener;
			mIsRadio = true;
		}
	}
	
	/** @hide */
	public static class SeekBarListItem extends Item {
		public static final int SEEKBAR_TYPE_SOUND = 0;
		public static final int SEEKBAR_TYPE_LIGHT = 1;
		public static final int SEEKBAR_TYPE_CUSTOM = 2;
		
		public int minValue;
		public int maxValue;
		public int currentValue;
		public int mSeekbarType;
		
		public int mLeftIcon;
		public int mRightIcon;
		public int mThumb;
		public int mProgressBg;
		public OnSeekBarChangeListener mListener;

		SeekBarListItem(int seekbarType, int min, int max, int current,
				OnSeekBarChangeListener listener) {
			super(ITEM_TYPE_SEEKBAR);
			minValue = min;
			maxValue = max;
			currentValue = current;
			mSeekbarType = seekbarType;
			mListener = listener;
		}

		SeekBarListItem(int min, int max, int current, int leftResId,
				int rightResId, int thumb, int progressBg,
				OnSeekBarChangeListener listener) {
			this(SEEKBAR_TYPE_CUSTOM, min, max, current, listener);
			mLeftIcon = leftResId;
			mRightIcon = rightResId;
			mThumb = thumb;
			mProgressBg = progressBg;
		}
	}
	
	/** @hide */
	public static class CustomListItem extends Item {
		public int mResId;
		public Intent mIntent;
		public boolean mDefaultBg;
		
		Callback mCallback;
		
		CustomListItem(int type, int resId, boolean defaultBg, Intent intent, Callback callback) {
			super(type);
			mResId = resId;
			mDefaultBg = defaultBg;
			mIntent = intent;
			mCallback = callback;
		}
	}
	
	public interface Callback{
		View newViewCallback(int position, View convertView, ViewGroup parent);
		View bindViewCallback(int position, View convertView, ViewGroup parent);
	}
}
