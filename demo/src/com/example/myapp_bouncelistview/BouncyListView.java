package com.example.myapp_bouncelistview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

public class BouncyListView extends ListView {
	
	 private Context context;  
	    /* 
	     * Slide over so that cross-border 
	     */  
	    private boolean outBound = false;  
	    private int distance;  
	    private int firstOut;  
	  
	    public BouncyListView(Context context) {  
	        super(context);  
	        this.context = context;  
	    }  
	  
	    public BouncyListView(Context context, AttributeSet attrs) {  
	        super(context, attrs);  
	        this.context = context;  
	    }  
	  
	    public BouncyListView(Context context, AttributeSet attrs, int defStyle) {  
	        super(context, attrs, defStyle);  
	        this.context = context;  
	    }  
	    	
	    }  


