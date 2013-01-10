package android.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Handler;

import com.android.music.R;
/** @hide */
public class DeleteButton extends Button implements EditPlugin.Listener, OnClickListener{
	public static final String TAG = "DeleteButton";
	
	private View mView;
	private int mPosition;
	private EditPlugin.OnEditListener mListener;
	private EditPlugin mEditPlugin;
	/** @hide */
	DeleteButton(Context context){
		this(context, null);
	}
	/** @hide */
	public DeleteButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	/** @hide */
	public DeleteButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	/** @hide */
	private void init(){
		setText(R.string.delete_text);
		setTextColor(Color.WHITE);
		setGravity(Gravity.CENTER);
		setOnClickListener(this);
		setTag(TAG);
		setBackgroundResource(R.drawable.btn_delete);
	}
	/** @hide */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(mListener != null){		
			mListener.onDelete(mView, mPosition);
			SwitchButton.resetState();
		}
	}
  /** @hide */
	@Override
	public void setView(View view) {
		// TODO Auto-generated method stub
		mView = view;
	}
  /** @hide */
	@Override
	public void setPosition(int position) {
		// TODO Auto-generated method stub
		mPosition = position;
	}
  /** @hide */
	@Override
	public void setListener(EditPlugin.OnEditListener listener) {
		// TODO Auto-generated method stub
		mListener = listener;
	}
	/** @hide */
	@Override
	public void setEditPlugin(EditPlugin plugin){
		mEditPlugin = plugin;
	}
}
