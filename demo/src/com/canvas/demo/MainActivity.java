package com.canvas.demo;

import com.canvas.demo.anim.CloseAppAnimation;
import com.canvas.demo.anim.NextAnimation;
import com.canvas.demo.anim.OpenAppAnimation;
import com.canvas.demo.anim.PrevAnimation;
import com.canvas.demo.anim.ShowAnimation;
import com.canvas.demo.surface.IRendererInterface;
import com.canvas.demo.surface.RendererView;
import com.canvas.demo.view.BackgroundRendererView;
import com.canvas.demo.view.NavigationItem;
import com.canvas.demo.view.NavigationRendererView;
import com.example.demo_highlights.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

public class MainActivity extends Activity implements IRendererInterface {
	

	private BackgroundRendererView mBackgroundRendererView = null;
	private NavigationRendererView mNavigationRendererView = null;
	
	private int index = 0;
	private boolean doOpen = false;
	private boolean back = false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		mBackgroundRendererView = new BackgroundRendererView(this);
		mNavigationRendererView = new NavigationRendererView(this);
		mBackgroundRendererView.setRendererInterface(this);
		mNavigationRendererView.setRendererInterface(this);
        this.addContentView(mBackgroundRendererView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));   
        this.addContentView(mNavigationRendererView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
		
		for(int i=0; i< 11; i++)
		{
		    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.a0 + i);
			NavigationItem item = new NavigationItem(bitmap,"game");
			mNavigationRendererView.addNavigationItem(item);
		}
		
        //add virtual menu
        try {  
            getWindow().addFlags(WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));  
        }catch (NoSuchFieldException e) {  
            // Ignore since this field won't exist in most versions of Android  
        }catch (IllegalAccessException e) {  
            Log.w("feelyou.info", "Could not access FLAG_NEEDS_MENU_KEY in addLegacyOverflowButton()", e);  
        }  
      //add end virtual menu
    }

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		
	    if(mNavigationRendererView.doingRenderer() || mBackgroundRendererView.doingRenderer())
	    {
	        return true;
	    }
	    if(doOpen)
	    {
	    	mNavigationRendererView.doAnimation(new CloseAppAnimation());
	    	doOpen = false;
	 		return true;
	    }
	   
		if(keyCode == KeyEvent.KEYCODE_MENU)
		{
		    if(index == 0)
		    {
		        mBackgroundRendererView.startRenderer();
		        index ++;
		    }else  if(index >= 1 && back == false){
		        mNavigationRendererView.doAnimation(new NextAnimation());
		        index ++;
		        if(index >= 5)back = true;
		    }
		    else  if(index >= 1 && back){
                mNavigationRendererView.doAnimation(new PrevAnimation());
                index --;
                if(index <= 1){
                    back = false;
                    index = 1;
                }
            }
			
			return true;
		}else if(keyCode == KeyEvent.KEYCODE_ENTER && doOpen == false){
			mNavigationRendererView.doAnimation(new OpenAppAnimation());
			doOpen = true;
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	protected void onResume() {
		super.onResume();
	}
	protected void onStart() {
		super.onStart();
	}


	public void doRendererFinished(RendererView view, int tag) {
		if(view == mBackgroundRendererView)
		{
			mNavigationRendererView.doAnimation(new ShowAnimation());
		}else{
			
		}
		
		
	}
}