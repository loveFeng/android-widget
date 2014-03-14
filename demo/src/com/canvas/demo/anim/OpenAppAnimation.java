package com.canvas.demo.anim;

import com.canvas.demo.AnimationConfig;

public class OpenAppAnimation extends RendererAnimation {

	private float mMinHeight = 0.0f;
	private float mMinWidth  = 0.0f;
	
	private float mMaxHeight = 0.0f;
	private float mMaxWidth  = 0.0f;

	//private float mMinScale = 0.0f;
	//private float mMaxScale = 0.0f;
	public OpenAppAnimation()
    {
        mStep = 0;
        mMaxStep = 8;
        mRadius = AnimationConfig.RADIUS;
    }
	
	public void setItemSize(float width,float height)
	{
		//mMinScale = 1.0f;
		//mMaxScale = AnimationConfig.DEVICE_WIDTH / height;
		
		mMinHeight = height;
		mMinWidth  = width;
		mMaxHeight = AnimationConfig.DEVICE_HEIGHT;
		mMaxWidth  = AnimationConfig.DEVICE_WIDTH;
	}
	
	public void readyFrame(int position) {
		
		mRotate = position * AnimationConfig.ITEM_ANGLE;
        mAlpha = (int) (255 *  (1.0f - Math.abs(position * AnimationConfig.ITEM_ANGLE - 90.0f) / 90.0f));
        mScale = RendererAnimation.getScale(position * AnimationConfig.ITEM_ANGLE);
        
		if(position != AnimationConfig.SHOW_ITEM_COUNT / 2)
		{
			mRadius = AnimationConfig.RADIUS;
		}else{
			mRadius = AnimationConfig.RADIUS + mStep * (AnimationConfig.DEVICE_WIDTH / 2 - AnimationConfig.RADIUS) / mMaxStep;

			float height = mMinHeight + mStep * (mMaxHeight - mMinHeight) / mMaxStep;
			float width = mMinWidth + mStep * (mMaxWidth - mMinWidth) / mMaxStep;
			
			mRect.left = - width / 2.0f;
			mRect.top  = - height / 2.0f;
			mRect.bottom = height / 2.0f;
			mRect.right  = width / 2.0f;
		}
		
		if(position == AnimationConfig.SHOW_ITEM_COUNT / 2){
    		mTitleVcenter = 0;
    		mTitleAlpha = 255;
    	}else{
    		mTitleVcenter = 0;
    		mTitleAlpha   = 0;
    	}
	}

}
