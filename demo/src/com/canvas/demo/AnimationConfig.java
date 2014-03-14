package com.canvas.demo;

public class AnimationConfig {

	public final static int   SHOW_ITEM_COUNT = 11;  //必须是奇数
	public final static float ITEM_ANGLE      = 180.0f / (SHOW_ITEM_COUNT - 1);  //每个item直接的夹角
	public final static float RADIUS          = 200.0f;                          //Item围绕圆的半径
	
	//public final static float DEVICE_HEIGHT   = 800.0f;  //屏幕分辨率高度
	//public final static float DEVICE_WIDTH    = 1280.0f; //屏幕分辨率宽度
	public final static float DEVICE_HEIGHT   = 480.0f;  //屏幕分辨率高度
	public final static float DEVICE_WIDTH    = 320.0f; //屏幕分辨率宽度
	
	public final static float TITLE_LEFT      = 450.0f;  //导航标题左端端坐标
	public final static float TITLE_FONT_SIZE = 36.0f;   //导航标题字体大小
}
