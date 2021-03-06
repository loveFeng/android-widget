package com.hhj.appListDemo;

import java.util.Random;

import com.hhj.appListDemo.AppAdapter.AppItem;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Scroller;

public class ScrollLayout extends ViewGroup {

    private boolean type;
    
    /** 沿Y轴正方向看，数值减1时动画逆时针旋转。 */
    public static final boolean ROTATE_DECREASE = true;
    /** 沿Y轴正方向看，数值减1时动画顺时针旋转。 */
    public static final boolean ROTATE_INCREASE = false;
    
    /** 值为true时可明确查看动画的旋转方向。 */
    public static final boolean DEBUG = false;
    
    private static final String TAG = "ScrollLayout";
    // 用于滑动的类
    private Scroller mScroller;
    // 用来跟踪触摸速度的类
    private VelocityTracker mVelocityTracker;
    // 当前的屏幕视图
    private int mCurScreen;
    // 默认的显示视图
    private int mDefaultScreen = 0;
    // 无事件的状态
    private static final int TOUCH_STATE_REST = 0;
    // 处于拖动的状态
    private static final int TOUCH_STATE_SCROLLING = 1;
    // 滑动的速度
    private static final int SNAP_VELOCITY = 600;

    private static int mNum;
    
    private int mTouchState = TOUCH_STATE_REST;
    private int mTouchSlop;
    private float mLastMotionX;
    // 用来处理立体效果的类
    private Camera mCamera;
    private Matrix mMatrix;
    /**旋转的角度*/
    private float angle = 180;

    public ScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    // 在构造器中初始化
    public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        mScroller = new Scroller(context);

        mCurScreen = mDefaultScreen;
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        mCamera = new Camera();
        mMatrix = new Matrix();
    }

    /*
     * 
     * 为子View指定位置
     */
    protected void onLayout(boolean changed, int left, int top, int right,
            int bottom) {
        // TODO Auto-generated method stub
        Log.e(TAG, "onLayout");

        if (changed) {
            int childLeft = 0;
            final int childCount = getChildCount();

            for (int i = 0; i < childCount; i++) {
                final View childView = getChildAt(i);
                if (childView.getVisibility() != View.GONE) {
                    final int childWidth = childView.getMeasuredWidth();
                    childView.layout(childLeft, 0, childLeft + childWidth,
                            childView.getMeasuredHeight());
                    childLeft += childWidth;
                }
            }
        }
    }

    // 重写此方法用来计算高度和宽度
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // Exactly：width代表的是精确的尺寸
        // AT_MOST：width代表的是最大可获得的空间
        if (widthMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException(
                    "ScrollLayout only canmCurScreen run at EXACTLY mode!");
        }

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException(
                    "ScrollLayout only can run at EXACTLY mode!");
        }

        // The children are given the same width and height as the scrollLayout
        // 得到多少页(子View)并设置他们的宽和高
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
        // Log.e(TAG, "moving to screen "+mCurScreen);
        scrollTo(mCurScreen * width, 0);
    }
    
    
    
    /**
     * 当进行View滑动时，会导致当前的View无效，该函数的作用是对View进行重新绘制 调用drawScreen函数
     */
    protected void dispatchDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        final long drawingTime = getDrawingTime();
        final int count = getChildCount();
        //Log.i("HHJ", "125"+"   drawingTime:"+drawingTime+"           count:"+count);
        for (int i = 0; i < count; i++) {
            drawScreenCube(canvas, i, drawingTime,mNum);//mNum
        }
    }
    
    class BigStone {
        //图片
        Bitmap bitmap;
        //角度
        int angle;
        //x坐标
        float x;
        //y坐标
        float y;
        //是否可见
        String text;
        boolean isVisible = true;
    }
    
    /**
     * 计算每个点的坐标
     */
    private void computeCoordinates() {
        BigStone stone;
        for(int index=0; index<STONE_COUNT; index++) {
            stone = mStones[index];
            stone.x = mPointX+ (float)(mRadius * Math.cos(stone.angle*Math.PI/90));//stone.angle*Math.PI/180(弧度=角度*3.14)
            stone.y = mPointY+ (float)(mRadius * Math.sin(stone.angle*Math.PI/90));
        }
    }
    //stone列表
    private BigStone[] mStones;
    //数目
    private int STONE_COUNT ;
    
    /**圆心坐标*/
    private int mPointX=0, mPointY=0;
    /**半径*/
    private int mRadius = 120;
    /**每两个点间隔的角度*/
    private int mDegreeDelta;
    private Context context;
    
    private Paint mPaint = new Paint();
/*    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            firstDegree =mStones[0].angle;
        }
        return true;
    }*/
    /**
     * 立体效果的实现函数 ,screen为哪一个子View  立方体效果
     * 
     */
    public void drawScreenCube(Canvas canvas, int screen, long drawingTime ,int select) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        
        if(select==1){//圆圈

           super.dispatchDraw(canvas);
        }else if(select==2){//层叠透明变亮
            final int width = getWidth(); 
            final int scrollWidth = screen * width; 
            final int scrollX = this.getScrollX();  
            if(scrollWidth > scrollX + width || scrollWidth + width < scrollX) { 
                return; 
            } 
            final View child;// = getChildAt(screen); 
            
            float scaleX = 1f;/*设置矩阵大小*/
            float scaleY = 1f;
            
            float centerX= (scrollWidth < scrollX)?scrollWidth + width:scrollWidth; //中心位置
            float centerY=getHeight()/2;
            
            if(screen==mCurScreen+1 && getChildAt(mCurScreen+1)!=null){
                child = getChildAt(mCurScreen+1);
                Log.i("HHJ", "child:"+child.toString());
                scaleX = (float) (((getScrollX()-(mCurScreen*getMeasuredWidth()))) * (1.0 / getMeasuredWidth()));
                scaleY = scaleX;
                centerX = (scrollWidth < scrollX)?scrollWidth + width:scrollWidth-getMeasuredWidth()/2; 
                centerY = getHeight()/2; 
            }else if(screen==mCurScreen-1 && getChildAt(mCurScreen-1)!=null){
                child = getChildAt(mCurScreen-1);
            }else {
                child = getChildAt(mCurScreen);
                if(scrollX<mCurScreen*getMeasuredWidth()){
                    scaleX = (float) (Math.abs(getScrollX()-mCurScreen*getMeasuredWidth()) * (1.0 / getMeasuredWidth())-1);
                    scaleY = scaleX;
                    centerX = (scrollWidth < scrollX)?scrollWidth + width:scrollWidth-getMeasuredWidth()/2; 
                    centerY = getHeight()/2; 
                }
            }
            
            final Camera camera = mCamera; 
            final Matrix matrix = mMatrix; 
            canvas.save(); 
            camera.save(); 
            camera.rotateY(-0);
            
            camera.getMatrix(matrix); 
            camera.restore(); 
            
            matrix.preScale(Math.abs(scaleX),Math.abs(scaleY));//大小
            //matrix.postScale(scaleX, scaleY);
            
            matrix.preTranslate(-centerX, -centerY); //中心位置
            matrix.postTranslate(centerX, centerY); 
            
            canvas.concat(matrix);
            drawChild(canvas, child, drawingTime); 
            canvas.restore(); 
        }else if(select==3){//波浪情况
            final int width = getWidth(); 
            final int scrollWidth = screen * width; 
            final int scrollX = this.getScrollX();  
            if(scrollWidth > scrollX + width || scrollWidth + width < scrollX) { 
                return; 
            } 
            final View child;// = getChildAt(screen); 
            float scaleX = 1f;
            float scaleY = 1f;
            if(screen==mCurScreen+1 && getChildAt(mCurScreen+1)!=null){
                child = getChildAt(mCurScreen+1);
                scaleX = (float) ((float) ((getScrollX()-(mCurScreen*getMeasuredWidth()))) * (1.0 / getMeasuredWidth()));
                scaleY = scaleX;
            }else if(screen==mCurScreen-1 && getChildAt(mCurScreen-1)!=null){
                child = getChildAt(mCurScreen-1);
                scaleX = (float) ((float) ((getScrollX()-(mCurScreen*getMeasuredWidth()))) * (1.0 / getMeasuredWidth()));
                scaleY = scaleX;
            }else {
                child = getChildAt(mCurScreen);
            }
            
            final float centerX = (scrollWidth < scrollX)?scrollWidth + width:scrollWidth; 
            final float centerY = getHeight()/2; 
            
            
            final Camera camera = mCamera; 
            final Matrix matrix = mMatrix; 
            canvas.save(); 
            camera.save(); 
            camera.rotateY(-0);
            
            camera.getMatrix(matrix); 
            camera.restore(); 
            
            matrix.preScale(Math.abs(scaleX), Math.abs(scaleY));
            //matrix.postScale(scaleX, scaleY);
            
            matrix.preTranslate(-centerX, -centerY); 
            matrix.postTranslate(centerX, centerY); 
            
            canvas.concat(matrix); 
            drawChild(canvas, child, drawingTime); 
            canvas.restore(); 
        }else if(select==4){//立体翻转
            final int width = getWidth(); 
            final int scrollWidth = screen * width; 
            final int scrollX = this.getScrollX();  
            if(scrollWidth > scrollX + width || scrollWidth + width < scrollX) { 
                return; 
            } 
            final View child = getChildAt(screen); 
            final int faceIndex = screen; 
            final float faceDegree = (this.getScrollX() - faceIndex *480f)*0.1875f;
            final float currentDegree = getScrollX() * (angle / getMeasuredWidth());
            if(faceDegree > 90 || faceDegree < -90) { 
                return; 
            }         
            final float centerX = (scrollWidth < scrollX)?scrollWidth + width:scrollWidth; 
            final float centerY = getHeight()/2; 
            final Camera camera = mCamera; 
            final Matrix matrix = mMatrix; 
            canvas.save(); 
            camera.save(); 
            camera.rotateY(-faceDegree); 
            camera.getMatrix(matrix); 
            camera.restore(); 
            matrix.preTranslate(-centerX, -centerY); 
            matrix.postTranslate(centerX, centerY); 
            canvas.concat(matrix); 
            drawChild(canvas, child, drawingTime); 
            canvas.restore(); 
        }else if (select==5){
            // 得到当前子View的宽度
            final int width = getWidth();
            
            /**当前滑动到view视图在viewgroup里面到位置   scrollWidth = 屏数*该view到宽度  */
            final int scrollWidth = screen * width;
            /**View水平方向的偏移量（像素）*/
            final int scrollX = this.getScrollX();
            if (scrollWidth > scrollX + width || scrollWidth + width < scrollX) {
                return;
            }
            final View child = getChildAt(screen);
            final int faceIndex = screen;
            
            /**
             * 1.解锁下这里到意思  由于当前一屏所充满屏幕宽度（假如所480*800）480    
           * 2. 180/getMeasuredWidth() 当前屏总共分为180度  比上 当前屏总宽度 意思说屏每一个像素到滑动占多少角度    意思所说180/480=0.375 每移动一个像素就所要转动0.375个角度 
           * 3. getScrollX() 由于这个所一个viewgroup 上滑动到偏移量 ,所有等有所将viewgroup翻转了很多个180
             *    等于说所第一屏所0～180度   第二屏所 180 ～ 360 度   第三屏所 360～540度 依次将viewgroup翻转到角度而已
             *  */
            final float currentDegree = getScrollX() * (angle / getMeasuredWidth());
            /**
             * 1.当前翻转到角度   currentDegree-当前屏数*180
             * 2.这里这样一减当前到滑屏度数就控制在-180～0度之间了
             * 3.让后在加上下面一个if判断，是将当前一屏与屏成九十度到时候以后的滑动就注销掉老
             * 
             **/
            final float faceDegree = currentDegree - faceIndex * angle;
            if (faceDegree > 90 || faceDegree < -90) {
                return;
            }
            
            //旋转的x轴中心位置
            final float centerX = (scrollWidth < scrollX) ? scrollWidth + width: scrollWidth;
            Log.i("HHJ", "centerX:"+centerX+"    scrollWidth:"+scrollWidth+"   scrollX:"+scrollX);
            //旋转的y轴中心位置
            final float centerY = getHeight() / 2;
            final Camera camera = mCamera;
            final Matrix matrixX = mMatrix;
            canvas.save();
            camera.save();
            camera.rotateY(-faceDegree);
            camera.getMatrix(matrixX);
            camera.restore();
            matrixX.preTranslate(-centerX, -centerY);//特效处理的中心
            matrixX.postTranslate(centerX, centerY);
            canvas.concat(matrixX);
            drawChild(canvas, child, drawingTime);
            canvas.restore();
        }else {
            super.dispatchDraw(canvas);
        }

    }
    
    /**
     * 把中心点放到中心处
     * @param canvas
     * @param bitmap
     * @param left
     * @param top
     */
    void drawInCenter(Canvas canvas, Bitmap bitmap, float left, float top,String text) {
        canvas.drawPoint(left, top, mPaint);
        Log.i("HHJ","bitmap==null:"+(bitmap==null));
        canvas.drawBitmap(bitmap, left-bitmap.getWidth()/2, top-bitmap.getHeight()/2, null);
        canvas.drawText(text,left-bitmap.getWidth()/2+2, top+bitmap.getHeight()/2+8, mPaint);
        canvas.restore(); 
    }

    /**
     * 根据目前的位置滚动到下一个视图位置.
     */
    public void snapToDestination() {
        final int screenWidth = getWidth();
        // 根据View的宽度以及滑动的值来判断是哪个View
        final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
        snapToScreen(destScreen);
    }

    public void snapToScreen(int whichScreen) {
        // get the valid layout page
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        if (getScrollX() != (whichScreen * getWidth())) {

            final int delta = whichScreen * getWidth() - getScrollX();
            mScroller.startScroll(getScrollX(), 0, delta, 0,
                    Math.abs(delta) * 2);
            mCurScreen = whichScreen;
            invalidate(); // 重新布局
        }
    }

    public void setToScreen(int whichScreen) {
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        mCurScreen = whichScreen;
        scrollTo(whichScreen * getWidth(), 0);
    }

    public int getCurScreen() {
        return mCurScreen;
    }

    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub
        if (mScroller.computeScrollOffset()) {
            //Log.i("HHJ", "209"+mScroller.computeScrollOffset());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    
    
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        if (mVelocityTracker == null) {
            // 使用obtain方法得到VelocityTracker的一个对象
            mVelocityTracker = VelocityTracker.obtain();
        }
        // 将当前的触摸事件传递给VelocityTracker对象
        mVelocityTracker.addMovement(event);
        // 得到触摸事件的类型
        final int action = event.getAction();
        final float x = event.getX();

        switch (action) {
        case MotionEvent.ACTION_DOWN:
            Log.e(TAG, "event down!");
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            mLastMotionX = x;
            break;

        case MotionEvent.ACTION_MOVE:
            int deltaX = (int) (mLastMotionX - x);
            mLastMotionX = x;

            scrollBy(deltaX, 0);
            break;

        case MotionEvent.ACTION_UP:
            Log.e(TAG, "event : up");
            // if (mTouchState == TOUCH_STATE_SCROLLING) {
            final VelocityTracker velocityTracker = mVelocityTracker;
            // 计算当前的速度
            velocityTracker.computeCurrentVelocity(1000);
            // 获得当前的速度
            int velocityX = (int) velocityTracker.getXVelocity();

            if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
                // Fling enough to move left
                snapToScreen(mCurScreen - 1);
            } else if (velocityX < -SNAP_VELOCITY && mCurScreen < getChildCount() - 1) {
                // Fling enough to move right
                snapToScreen(mCurScreen + 1);
            } else {
                snapToDestination();
            }

            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
            // }
            mTouchState = TOUCH_STATE_REST;
            break;
        case MotionEvent.ACTION_CANCEL:
            mTouchState = TOUCH_STATE_REST;
            break;
        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        Log.e(TAG, "onInterceptTouchEvent-slop:" + mTouchSlop);

        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE)
                && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }

        final float x = ev.getX();

        switch (action) {
        case MotionEvent.ACTION_MOVE:
            final int xDiff = (int) Math.abs(mLastMotionX - x);
            if (xDiff > mTouchSlop) {
                mTouchState = TOUCH_STATE_SCROLLING;

            }
            break;

        case MotionEvent.ACTION_DOWN:
            
            mNum = new Random().nextInt(10);
            mLastMotionX = x;
            mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
                    : TOUCH_STATE_SCROLLING;
            break;

        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            mTouchState = TOUCH_STATE_REST;
            break;
        }

        return mTouchState != TOUCH_STATE_REST;
    }


}
