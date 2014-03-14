package com.krislq.floating2;

//import com.dream.myqiyi.BaseApp;
import com.way.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
/**
 * 
 * @author <a href="mailto:kris@krislq.com">Kris.lee</a>
 * @website www.krislq.com
 * @date Nov 29, 2012
 * @version 1.0.0
 *
 */
public class FloatView extends ImageView{
	private float mTouchX;
	private float mTouchY;
	private float x;
	private float y;
	private float mStartX;
	private float mStartY;
	private OnClickListener mClickListener;
	private SharedPreferences mShare;

	private WindowManager windowManager = (WindowManager) getContext()
			.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
	// ��windowManagerParams����Ϊ��ȡ��ȫ�ֱ��������Ա�����ڵ�����
	private WindowManager.LayoutParams windowManagerParams = ((Application) getContext()
			.getApplicationContext()).getMywmParams();

	public FloatView(Context context) {
		super(context);
		mShare = context.getSharedPreferences("Setting", 0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//��ȡ��״̬���ĸ߶�
		Rect frame =  new  Rect();  
		getWindowVisibleDisplayFrame(frame);
		int  statusBarHeight = frame.top; 
//		System.out.println("statusBarHeight:"+statusBarHeight);
		// ��ȡ�����Ļ����꣬������Ļ���Ͻ�Ϊԭ��
		x = event.getRawX();
		y = event.getRawY() - statusBarHeight; // statusBarHeight��ϵͳ״̬���ĸ߶�
//		Log.i("tag", "currX" + x + "====currY" + y);
//		Log.e("zhengpanyong", "event.getRawX():" + x + ",getRawY()" + event.getRawY() + ",statusBarHeight:" + statusBarHeight);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // ������ָ�������¶���
			// ��ȡ���View����꣬���Դ�View���Ͻ�Ϊԭ��
			mTouchX = event.getX();
			mTouchY = event.getY();
			mStartX = x;
			mStartY = y;
//			Log.i("tag", "startX" + mTouchX + "====startY"
//					+ mTouchY);
//			Log.e("zhengpanyong", "ACTION_DOWN:" + x + "," + y);
//			setBackgroundColor(0xFF808080);
			setAlpha(255);
			break;

		case MotionEvent.ACTION_MOVE: // ������ָ�����ƶ�����
			updateViewPosition();
//			Log.e("zhengpanyong", "ACTION_MOVE");
			break;

		case MotionEvent.ACTION_UP: // ������ָ�����뿪����
			updateViewPosition();
//			Log.e("zhengpanyong", "ACTION_UP:" + x + "," + y + ",mStartX:" + mStartX + "," + mStartY);
			mTouchX = mTouchY = 0;
			float subX = x - mStartX;
			float subY = y - mStartY;
			subX = subX > 0? subX: -1 * subX;
			subY = subY > 0? subY: -1 * subY;
			if ((/*x - mStartX*/subX) < /*5*/3 && (/*y - mStartY*/subY) < /*5*/3) {
//				Log.e("zhengpanyong", "onClick");
				if(mClickListener!=null) {
					mClickListener.onClick(this);
				}
			} else {
				mShare.edit().putInt("x", windowManagerParams.x).commit();
				mShare.edit().putInt("y", windowManagerParams.y).commit();
			}
//			setBackgroundColor(0x33FFFFFF);
			setAlpha(80);
			break;
		}
		return true;
	}
	@Override
	public void setOnClickListener(OnClickListener l) {
		this.mClickListener = l;
	}
	private void updateViewPosition() {
		// ���¸�������λ�ò���
		windowManagerParams.x = (int) (x - mTouchX);
		windowManagerParams.y = (int) (y - mTouchY);
		windowManager.updateViewLayout(this, windowManagerParams); // ˢ����ʾ

	}


}
