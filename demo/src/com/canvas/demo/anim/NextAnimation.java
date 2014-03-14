package com.canvas.demo.anim;

import com.canvas.demo.AnimationConfig;

public class NextAnimation extends RendererAnimation {


    public NextAnimation()
    {
        mStep = 0;
        mMaxStep = 6;
    }
    
    public void readyFrame(int position) {
        
    	if(this.isEnd())
    	{
    		mRotate = position * AnimationConfig.ITEM_ANGLE;
    	}else{
    		mRotate = position * AnimationConfig.ITEM_ANGLE + mStep * (AnimationConfig.ITEM_ANGLE / 6.0f);
    	}
        mAlpha = (int) (255 *  (1.0f - Math.abs(mRotate - 90.0f) / 90.0f));
        mScale = RendererAnimation.getScale(mRotate);
        
        if(position == AnimationConfig.SHOW_ITEM_COUNT / 2){
    		mTitleVcenter = (AnimationConfig.TITLE_FONT_SIZE / mMaxStep) * (mStep - mMaxStep);
    		mTitleAlpha   = 255 * mStep / mMaxStep;
    	}else if(position == AnimationConfig.SHOW_ITEM_COUNT / 2 - 1){
    		mTitleVcenter = (AnimationConfig.TITLE_FONT_SIZE / mMaxStep) * mStep;
    		mTitleAlpha   = 255 * (mMaxStep - mStep) / mMaxStep;
    	}else{
    		mTitleVcenter = 0;
    		mTitleAlpha   = 0;
    	}
    }

}
