package com.example.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class SexangleViewGroup extends ViewGroup {

	private static final int SPACE = 10;// view与view之间的间隔

	public SexangleViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		int lenght = (int) (getWidth() / 2.5) - SPACE;// 每个子VIEW的长度

		double radian30 = 30 * Math.PI / 180;
		float h = (float) (lenght * Math.cos(radian30));
		int bottomSpace = (int) ((lenght - 2 * h) / 2);// 六边形离底部的间隔

		int offsetX = lenght * 3 / 4 + SPACE;// X轴每次偏移的长度
		int offsetY = lenght / 2;// Y轴每次偏移的长度

		int rowIndex = 0;//行下标  
		int childCount = 3;
		int tempCount = 3;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if(i == childCount){
				rowIndex++;

				if(tempCount - 1 <= 0){
					tempCount = 3;
				}else{
					tempCount --;
				}
				childCount += tempCount;
			}

			int startL = i % 3 * offsetX;
			int startT = i % 3 * offsetY;
			if(tempCount == 1){
				startL -= offsetX;
				startT -= offsetY;
			}
			//随机生成
			if(Math.random()>0.6){
				startT = -startT;
			}
			child.layout(startL, startT + rowIndex * lenght,
					startL + lenght, startT + lenght
							+ rowIndex * lenght - bottomSpace);
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);

		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			child.measure((int) (getWidth() / 2.5), (int) (getWidth() / 2.5));
		}
	}

}
