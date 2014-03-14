package com.aven.qqdemo;

import java.util.ArrayList;

import com.example.demo_highlights.R;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 鍙傝�鍘熶綔鑰匘.Winter鍩虹锛� * 
 * @author avenwu
 * iamavenwu@gmail.com
 * 
 */
public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    public static ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    private ImageView ivBottomLine;
    private TextView tvTabMeinv, tvTabLuoli, tvTabQiche, tvTabLuntan;
    private LinearLayout grid_linearLayout;
    public static final int CASE_MEINV = 10;
    public static final int CASE_LUOLI = 11;
    public static final int CASE_QICHE = 12;
    public static final int CASE_LUNTAN = 13;

    private int currIndex = 0;
    private int bottomLineWidth;
    private int offset = 0;
    private int position_one;
    private int position_two;
    private int position_three;
    public static double screenWidth = 0.0;
    public static double screenHeight = 0.0;
    public static double grid_linear_width = 0.0;
    public static double grid_linear_height = 0.0;
    private Resources resources;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_qqdemo);
        resources = getResources();
        
        //初始化宽高，这个宽高是用来服务于界面上的四个标题的点击事件的
        InitWidth();
        //初始化TextView
        InitTextView();
        //初始化ViewPager
        InitViewPager();
        
    }
    
    /**
     * 初始化四个TextView，并为TextView设置点击监听事件
     */
    private void InitTextView() {
    	tvTabMeinv = (TextView) findViewById(R.id.tv_tab_meinv);
    	tvTabLuoli = (TextView) findViewById(R.id.tv_tab_luoli);
    	tvTabQiche = (TextView) findViewById(R.id.tv_tab_qiche);
    	tvTabLuntan = (TextView) findViewById(R.id.tv_tab_luntan);

    	tvTabMeinv.setOnClickListener(new MyOnClickListener(0));
    	tvTabLuoli.setOnClickListener(new MyOnClickListener(1));
    	tvTabQiche.setOnClickListener(new MyOnClickListener(2));
    	tvTabLuntan.setOnClickListener(new MyOnClickListener(3));
    }
    
  
    
    /**
     * 初始化ViewPager，创建4个Fragment，为ViewPager设置适配器，为ViewPager设置监听器
     */
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);
        fragmentsList = new ArrayList<Fragment>();
        //LayoutInflater mInflater = getLayoutInflater();
        // View activityView = mInflater.inflate(R.layout.lay1, null);
        
        //不同的Fragment传入的是不同的值，这个值用来在TestFragment类中的onCreatView()方法中根据这个
        //传进来的int值返回不同的View
        Fragment activityfragment = TestFragment.newInstance(CASE_MEINV);
        Fragment groupFragment = TestFragment.newInstance(CASE_LUOLI);
        Fragment friendsFragment=TestFragment.newInstance(CASE_QICHE);
        Fragment chatFragment=TestFragment.newInstance(CASE_LUNTAN);

        fragmentsList.add(activityfragment);
        fragmentsList.add(groupFragment);
        fragmentsList.add(friendsFragment);
        fragmentsList.add(chatFragment);
        
        //设置ViewPager的适配器（自定义的继承自FragmentPagerAdapter的adapter）
        //参数分别是FragmentManager和装载着Fragment的容器
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
        //设置默认是第一页
        mPager.setCurrentItem(0);
        //设置ViewPager的页面切换监听事件
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }
    
    
    /**
     * onWindowFocusChanged会在onResume事件之后调用，InitViewPager方法选择在这里调用是
     * 这个方法里面需要取得后写线性布局的width,height，只有在控件加载之后才能取得宽和高
     * 所以选择在这个方法里面
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        //通过DisplayMetrics得到屏幕的宽度
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        Log.d("xiaomi", "screenW="+screenWidth+","+"screenH="+screenHeight);
        
        //取得线性布局的宽高
        grid_linearLayout = (LinearLayout) findViewById(R.id.title_linearLayout);
        int grid_linear_width = grid_linearLayout.getWidth();
        int grid_linear_height = grid_linearLayout.getHeight();
        Log.d("xiaomi", "linear_width="+grid_linear_width+","+"linear_height="+grid_linear_height);
        Log.d("xiaomi", "grid_image_width="+grid_linear_width+","+"grid_image_height="+grid_linear_height);
        
    }

    private void InitWidth() {
    	//获得ImageView的引用
        ivBottomLine = (ImageView) findViewById(R.id.iv_bottom_line);
        //得到在布局文件中ImageView的宽度
        bottomLineWidth = ivBottomLine.getLayoutParams().width;
        Log.d(TAG, "cursor imageview width=" + bottomLineWidth);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        //通过DisplayMetrics得到屏幕的宽度
        int screenW = dm.widthPixels;
       
        //offset为ImageView在每一小块中距离左边的距离
        offset = (int) ((screenW / 4.0 - bottomLineWidth) / 2);
        
        position_one = (int) (screenW / 4.0);
        position_two = position_one * 2;
        position_three = position_one * 3;
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    };
    
    //这个是ViewPager的页面切换事件监听器
    public class MyOnPageChangeListener implements OnPageChangeListener {
    	
        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
            //下面的意思是：拿第一个case 0举例，如果切换的是到case 0 
            //下面的if的意思是，如果现在是在序号为1的界面切换过来到序号为0的页面
            //那么执行动画，并把一开始所在界面上的文字颜色设为轻亮，最后设置当前页面的文字为高亮
            //TranslateAnimation的四个参数代表的意思是：动画起始x,终结x，起始y,终结y位置
            case 0:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, 0, 0, 0);
                    tvTabLuoli.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(position_two, 0, 0, 0);
                    tvTabQiche.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(position_three, 0, 0, 0);
                    tvTabLuntan.setTextColor(resources.getColor(R.color.lightwhite));
                }
                tvTabMeinv.setTextColor(resources.getColor(R.color.white));
                break;
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, position_one, 0, 0);
                    tvTabMeinv.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(position_two, position_one, 0, 0);
                    tvTabQiche.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(position_three, position_one, 0, 0);
                    tvTabLuntan.setTextColor(resources.getColor(R.color.lightwhite));
                }
                tvTabLuoli.setTextColor(resources.getColor(R.color.white));
                break;
            case 2:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, position_two, 0, 0);
                    tvTabMeinv.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, position_two, 0, 0);
                    tvTabLuoli.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(position_three, position_two, 0, 0);
                    tvTabLuntan.setTextColor(resources.getColor(R.color.lightwhite));
                }
                tvTabQiche.setTextColor(resources.getColor(R.color.white));
                break;
            case 3:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, position_three, 0, 0);
                    tvTabMeinv.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, position_three, 0, 0);
                    tvTabLuoli.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(position_two, position_three, 0, 0);
                    tvTabQiche.setTextColor(resources.getColor(R.color.lightwhite));
                }
                tvTabLuntan.setTextColor(resources.getColor(R.color.white));
                break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            ivBottomLine.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}