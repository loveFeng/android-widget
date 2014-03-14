package com.canvas.demo.anim;

import com.canvas.demo.AnimationConfig;

public class PrevJitterAnimation extends RendererAnimation {

	private int[] ROTATE_ANIMATION  = {1,2,1,0,0};
	
	public PrevJitterAnimation()
    {
        mStep = 0;
        mMaxStep = ROTATE_ANIMATION.length - 1;
    }
    
    public void readyFrame(int position) {
    	mRotate = position * AnimationConfig.ITEM_ANGLE - ROTATE_ANIMATION[mStep];
        mAlpha = (int) (255 *  (1.0f - Math.abs(position * AnimationConfig.ITEM_ANGLE - 90.0f) / 90.0f));
        mScale = RendererAnimation.getScale(position * AnimationConfig.ITEM_ANGLE);
        
        if(position == AnimationConfig.SHOW_ITEM_COUNT / 2){
    		mTitleVcenter = 0;
    		mTitleAlpha = 255;
    	}else{
    		mTitleVcenter = 0;
    		mTitleAlpha   = 0;
    	}
    }

}
