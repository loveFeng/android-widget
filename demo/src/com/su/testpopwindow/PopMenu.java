package com.su.testpopwindow;

import java.util.ArrayList;

import com.example.demo_highlights.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PopMenu
{

    private Context context;
    private PopupWindow popupWindow;
    private ViewPager viewPager;
    private ArrayList<View> listViews;
    private int screenwidth;

    private int currentView = 0;// ��ǰ��ͼ
    private int viewOffset;// ����ͼƬƫ����
    private int imgWidth;// ͼƬ���
    private ImageView iv_cursor;// ����ͼƬ
    private TextView tv_main;
    private TextView tv_utils;
    private TextView tv_set;
    private RelativeLayout.LayoutParams params;

    public PopMenu(Context context)
    {

        this.context = context;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.popmenu, null);

        tv_main = (TextView) view.findViewById(R.id.tv_main);
        tv_utils = (TextView) view.findViewById(R.id.tv_utils);
        tv_set = (TextView) view.findViewById(R.id.tv_set);
        this.tv_main.setOnClickListener(new myOnClick(0));
        this.tv_utils.setOnClickListener(new myOnClick(1));
        this.tv_set.setOnClickListener(new myOnClick(2));

        iv_cursor = (ImageView) view.findViewById(R.id.iv_cursor);
        setCursorWidth();
        params = (RelativeLayout.LayoutParams) iv_cursor.getLayoutParams();
        
        
        viewPager = (ViewPager) view.findViewById(R.id.viewPagerw);
        viewPager.setFocusableInTouchMode(true);
        viewPager.setFocusable(true);

        listViews = new ArrayList<View>();
        listViews.add(inflater.inflate(R.layout.grid_menu, null));
        listViews.add(inflater.inflate(R.layout.grid_menu, null));
        listViews.add(inflater.inflate(R.layout.grid_menu, null));
        viewPager.setAdapter(new myPagerAdapter());
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelSize(R.dimen.popmenu_h));
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    public void setCursorWidth()
    {
        screenwidth = getScreenWidth();
        // imgWidth = screenwidth / 3 - 40;// 40 ��Ϊ����ָʾ�� ��΢С����Ļ��С��1/3
        // viewOffset = 20; // ��ָʾ����ʾ���м�
        imgWidth = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_cursor).getWidth();// ��ȡͼƬ���
        viewOffset = (screenwidth / 3 - imgWidth) / 2;
        Matrix matrix = new Matrix();
        matrix.postTranslate(viewOffset, 0);
        iv_cursor.setImageMatrix(matrix);
        Log.e("TAG", screenwidth + "");

    }

    public int getScreenWidth()
    {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        return screenW;

    }

    // ����ʽ ���� pop�˵� parent ���½�
    public void show(View parent)
    {
        // popupWindow.showAsDropDown(parent, -1000, context.getResources()
        // .getDimensionPixelSize(R.dimen.popmenu_yoff));

        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 70);// ����ײ���λ��
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }

    public void dismiss()
    {
        popupWindow.dismiss();
    }

    public class myPagerAdapter extends PagerAdapter
    {

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2)
        {

            ((ViewPager) arg0).removeView(listViews.get(arg1));

        }

        @Override
        public int getCount()
        {

            return listViews.size();

        }

        public Object instantiateItem(View arg0, int arg1)
        {

            if (arg1 < 3)
            {
                ((ViewPager) arg0).addView(listViews.get(arg1 % 3), 0);

            }
            // ������Ӳ˵���ʱ�� �½�һ��gridviewadapter Ȼ��new��gridview ��ӵ�����Ϳ���
            GirdViewAdapter girdViewAdapter = new GirdViewAdapter(context);
            switch (arg1)
            {
                case 0:// ѡ�1

                    GridView gridView = (GridView) arg0.findViewById(R.id.myGridView);

                    gridView.setAdapter(girdViewAdapter);

                    gridView.setOnItemClickListener(new OnItemClickListener()
                    {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                        {

                            switch (arg2)
                            {

                                default:

                                    Toast.makeText(context, "�����GridView+ViewPager��UC�˵���" + arg2, Toast.LENGTH_LONG).show();
                                    break;
                            }

                        }
                    });

                    break;
                case 1:// ѡ�2
                    GridView gridView2 = (GridView) arg0.findViewById(R.id.myGridView);

                    gridView2.setAdapter(girdViewAdapter);

                    gridView2.setOnItemClickListener(new OnItemClickListener()
                    {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                        {

                            switch (arg2)
                            {

                                default:

                                    Toast.makeText(context, "�����GridView+ViewPager��UC�˵���" + arg2, Toast.LENGTH_LONG).show();
                                    break;
                            }

                        }
                    });
                    break;
                case 2:// ѡ�3
                    GridView gridView3 = (GridView) arg0.findViewById(R.id.myGridView);

                    gridView3.setAdapter(girdViewAdapter);

                    gridView3.setOnItemClickListener(new OnItemClickListener()
                    {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                        {

                            switch (arg2)
                            {

                                default:

                                    Toast.makeText(context, "�����GridView+ViewPager��UC�˵���" + arg2, Toast.LENGTH_LONG).show();
                                    break;
                            }

                        }
                    });
                    break;
            }

            return listViews.get(arg1);

        }

        public boolean isViewFromObject(View arg0, Object arg1)
        {

            return arg0 == (arg1);

        }

    }

    public class MyOnPageChangeListener implements OnPageChangeListener
    {

        int one = viewOffset * 2 + imgWidth;// ҳ��1 -> ҳ��2 ƫ����

        int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����

        @Override
        public void onPageSelected(int arg0)
        {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2)
        {
            params.leftMargin = (int) ((arg0+arg1)*one);
            Log.i("123", " params.leftMargin:"+ params.leftMargin);
            iv_cursor.setLayoutParams(params);
        }

        @Override
        public void onPageScrollStateChanged(int arg0)
        {

        }

    }

    /*
     * 
     * ��ѡ����������ʵ�ַ���
     */
    public class myOnClick implements View.OnClickListener
    {

        int index = 0;

        public myOnClick(int currentIndex)
        {

            index = currentIndex;
        }

        public void onClick(View v)
        {

            viewPager.setCurrentItem(index);

        }

    }

}
