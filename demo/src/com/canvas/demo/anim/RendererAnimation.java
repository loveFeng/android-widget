package com.canvas.demo.anim;

import com.canvas.demo.AnimationConfig;

import android.graphics.RectF;

public abstract class RendererAnimation {
    
    public interface AnimationNotification
    {
        public void doAnimation(RendererAnimation animation,int step,int maxStep);
    }
    
    
    
    protected int    mStep     = 0;  //当前帧数
    protected int    mMaxStep  = 0;  //最大帧数
    protected float  mRotate   = 0f;  //功能图片旋转角度
    protected float  mRadius   = AnimationConfig.RADIUS; //功能图片中心离圆中心的距离
    protected float  mScale    = 0f;                     //功能图片缩小的比例
    protected int    mAlpha    = 0;                      //功能图片的透明度
    protected int    mTitleBackgroundAlpha = 255;        //标题背景图片的透明度
    protected RectF  mRect     = new RectF();            //功能图片的绘制矩形框，在OpenAppAnimation 和 CloseAppAnimation中用到
    protected int    mTitleAlpha   = 0;
	protected float  mTitleVcenter = 0;
    
    protected AnimationNotification mNotification = null;

    public RendererAnimation()
    {
    	mTitleBackgroundAlpha = 255;
    }

    public void setAnimationNotification(AnimationNotification notification)
    {
        mNotification = notification;
    }
    
    public void nextFrame()
    {
        mStep ++;
        if(mStep >= mMaxStep) mStep = mMaxStep;
        if(mNotification != null) {
            mNotification.doAnimation(this,mStep, mMaxStep);
        }
    }
    
    public abstract void readyFrame(int position);

    
    public boolean isEnd()
    {
        return (mStep >= mMaxStep) || (mStep < 0);
    }

    public int getTitleBackgroundAlpha() {
        return mTitleBackgroundAlpha;
    }
    public float getRadius() {
        return mRadius;
    }

    public float getRotate() {
        return mRotate;
    }

    public float getScale() {
        return mScale;
    }

    public int getAlpha() {
        if(mAlpha < 0 || mAlpha > 255) return 0;
        return mAlpha;
    }
    
    public int getTitleAlpha() {
		return mTitleAlpha;
	}

	public float getTitleVcenter() {
		return mTitleVcenter;
	}

    public RectF getRect() {
        return mRect;
    }
    
    
    private static float UNSELECT_MAX_SCALE = 90;
    private static float UNSELECT_MIN_SCALE = 80;
    
    private static float SELECTED_MAX_SCALE = 100;
    private static float SELECTED_MIN_SCALE = 90;
    
    public static float getScale(float rotate)
    {
        if(Math.abs(rotate - 90) < 18)
        {
            float maxValue = 18;
            float curValue = Math.abs(rotate - 90);
            float scale = 1.0f - curValue / maxValue;
            return (SELECTED_MIN_SCALE + (SELECTED_MAX_SCALE - SELECTED_MIN_SCALE) *  scale) / 100.0f;
        }else{
            float maxValue = 90 - 18;
            float curValue = Math.abs(Math.abs(rotate - 90) - 18);
            float scale = 1.0f - curValue / maxValue;
            return (UNSELECT_MIN_SCALE + (UNSELECT_MAX_SCALE - UNSELECT_MIN_SCALE) *  scale) / 100.0f;
        }
    }
}
