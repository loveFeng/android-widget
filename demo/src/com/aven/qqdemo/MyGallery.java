package com.aven.qqdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class MyGallery extends Gallery {

	public MyGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public MyGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    //这个方法返回false，则在滑动Gallery的时候，每次都只会滑动一页
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {
 
        int kEvent;
        if (isScrollingLeft(e1, e2)) {
            kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        onKeyDown(kEvent, null);
 
        if (this.getSelectedItemPosition() == 0) {// 实现后退功能
            //this.setSelection(TestFragment.gallery_data.size() - 1);
        }
        return true;
 
    }
 
    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
        return e2.getX() > e1.getX();
    }
 
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
            float distanceY) {
        // TODO Auto-generated method stub
        return super.onScroll(e1, e2, distanceX, distanceY);
    }


	
}
