package com.canvas.demo.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.canvas.demo.AnimationConfig;
import com.canvas.demo.anim.CloseAppAnimation;
import com.canvas.demo.anim.NextAnimation;
import com.canvas.demo.anim.NextJitterAnimation;
import com.canvas.demo.anim.OpenAppAnimation;
import com.canvas.demo.anim.PrevAnimation;
import com.canvas.demo.anim.PrevJitterAnimation;
import com.canvas.demo.anim.RendererAnimation;
import com.canvas.demo.anim.RendererAnimation.AnimationNotification;
import com.canvas.demo.surface.RendererView;
import com.canvas.demo.util.CanvasUtil;
import com.example.demo_highlights.R;

public class NavigationRendererView extends RendererView implements AnimationNotification {
	

	private RendererAnimation mAnimation = null;
	private int mFirstItemIndex = 0;
	private int mCurrentIndex = 0;
	private int mLastItemIndex = 0;
	private List<NavigationItemView> mShowItems = new ArrayList<NavigationItemView>(AnimationConfig.SHOW_ITEM_COUNT) ;
	private List<NavigationItemView> mAllItems = new ArrayList<NavigationItemView>() ;
	
	private Bitmap mNavigationTitleBackground = null;
	
	public NavigationRendererView(Context context) {
		super(context);
		mNavigationTitleBackground = BitmapFactory.decodeResource(getResources(),R.drawable.navigation_title_bg);
	}
	
	public void addNavigationItem(NavigationItem item)
	{
		mAllItems.add(new NavigationItemView(item));
		
	}
	
	public void doAnimation(RendererAnimation animation)
	{
	    if(mShowItems.size() < 11)
        {
	        mShowItems.clear();
	        for(int i=0;i< 11;i++)
	        {
	            int index = i % mAllItems.size();
	            NavigationItemView item = mAllItems.get(index);
	            item.setPosition(index);
	            mShowItems.add(item);
	        }
	        mFirstItemIndex = 0;
	        mCurrentIndex = mShowItems.size() / 2;
	        mLastItemIndex = mShowItems.size() - 1;
        }
	    
	    if(animation.getClass() == OpenAppAnimation.class) {
	    	NavigationItemView item = mAllItems.get(mCurrentIndex);
	    	((OpenAppAnimation)animation).setItemSize(item.getWidth(), item.getHeight());
	    }else if(animation.getClass() == CloseAppAnimation.class){
	    	NavigationItemView item = mAllItems.get(mCurrentIndex);
	    	((CloseAppAnimation)animation).setItemSize(item.getWidth(), item.getHeight());
	    }

	    mAnimation = animation;
	    mAnimation.setAnimationNotification(this);
        this.startRenderer();
	}

	protected void onRenderer(Canvas canvas) {

		if(mAnimation == null) return;
		int saveCount = canvas.save();
		//ת�����
		canvas.translate(0, getHeight() / 2.0f);
		//���Ʊ��ⱳ��
		drawTitleBackground(canvas);
		//�����ϰ벿��
		for(int index = 0;index < mCurrentIndex;index++)
        {
		    mShowItems.get(index).draw(canvas, mAnimation);
        }
		//�����°벿��
		for(int index = mShowItems.size() - 1;index > mCurrentIndex;index--)
        {
            mShowItems.get(index).draw(canvas, mAnimation);
           
        }
		//����ѡ�в���
		mShowItems.get(mCurrentIndex).draw(canvas, mAnimation);
		//��ԭCanvas
		canvas.restoreToCount(saveCount);
	
		if(mAnimation.isEnd())
		{
			this.stopRenderer();
		}else{
		    mAnimation.nextFrame();
		}
	}
	//----------------------���Ʊ���----------------------------------
	private Paint mTitleBackgroundPaint = new Paint();
	private void drawTitleBackground(Canvas canvas)
	{
		int saveCount = canvas.save();
		mTitleBackgroundPaint.setAlpha(mAnimation.getTitleBackgroundAlpha());
		canvas.translate(AnimationConfig.TITLE_LEFT, 0);
		CanvasUtil.drawImage(canvas, mNavigationTitleBackground, 0, 0,
				CanvasUtil.LEFT | CanvasUtil.VCENTER, mTitleBackgroundPaint);
		canvas.restoreToCount(saveCount);
	}
	//-------------------------End------------------------------------
	
    public void doAnimation(RendererAnimation animation, int step, int maxStep) {
        if(animation.getClass() == NextAnimation.class){
            if(step == maxStep / 2 + 1)
            {
                mShowItems.remove(mShowItems.size() - 1);
                mCurrentIndex --;
            }else if(step == maxStep){
                mFirstItemIndex = ( mFirstItemIndex -1 + mAllItems.size() ) % mAllItems.size();
                mLastItemIndex = ( mLastItemIndex -1 + mAllItems.size() ) % mAllItems.size();
                mShowItems.add(0, mAllItems.get(mFirstItemIndex));
                mCurrentIndex = mShowItems.size() / 2;
                for(int i=0;i< mShowItems.size();i++)
                {
                    mShowItems.get(i).setPosition(i);
                }
                
                this.doAnimation(new NextJitterAnimation());
            }
        }else if(animation.getClass() == PrevAnimation.class){
            if(step == maxStep / 2)
            {
                mShowItems.remove(0);
            }else if(step == maxStep){
                mFirstItemIndex = ( mFirstItemIndex + 1 ) % mAllItems.size();
                mLastItemIndex = ( mLastItemIndex + 1 ) % mAllItems.size();
                mShowItems.add(mAllItems.get(mLastItemIndex));
                mCurrentIndex = mShowItems.size() / 2;
                for(int i=0;i< mShowItems.size();i++)
                {
                    mShowItems.get(i).setPosition(i);
                }
                this.doAnimation(new PrevJitterAnimation());
            }
           
        }
        
    }
}
