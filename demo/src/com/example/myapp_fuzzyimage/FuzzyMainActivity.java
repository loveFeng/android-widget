package com.example.myapp_fuzzyimage;

import java.io.IOException;

import com.example.demo_highlights.R;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FuzzyMainActivity extends Activity {

	ImageView mImageView;
	Button mButton;
	
	private int [] originalPixels;
	/**
	 * Current set of pixels from the image (the one that will be exported
	 */
	private int [] currentPixels;
	/**
	 * Original width of the image
	 */
	private int _width = -1;
	/**
	 * Original height of the image
	 */
	private int _height= -1;
	/**
	 * Original image
	 */
	private Bitmap _image;
	
	private Bitmap mBitmap;
    
	private boolean alpha = false;
	
	private static int proc = 5;
	
	private RelativeLayout mRelativeLayout;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuzzy_main);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.layout);
        mImageView = (ImageView) findViewById(R.id.imageView1);
        mButton = (Button) findViewById(R.id.button1); 
        
        mBitmap = ((BitmapDrawable)(this.getResources().getDrawable(R.drawable.image_transparency))).getBitmap();
//        StackBlurManager(mBitmap);
        mButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				 //1.构建Bitmap      
				WindowManager windowManager = getWindowManager();  
				Display display = windowManager.getDefaultDisplay();
				int w = display.getWidth();
				int h = display.getHeight();
				Bitmap Bmp = Bitmap.createBitmap( w, h, Config.ARGB_8888 ); 
				//2.获取屏幕 
				View decorview = getWindow().getDecorView();
				decorview.setDrawingCacheEnabled(true);
				Bmp = decorview.getDrawingCache();    
				
				StackBlurManager(Bmp);
				
				proc += 5;
				proc %= 100;
		        mImageView.setImageBitmap(null);
				mRelativeLayout.setBackgroundDrawable(new BitmapDrawable(process(proc)));
		
			}
		});
        



    }
    
    public void setImage(Bitmap image) {
//    	set
    }

	public void StackBlurManager(Bitmap image) {
		_width=image.getWidth();
		_height=image.getHeight();
		_image = image;
        
		originalPixels= new int[_width*_height];
        
		_image.getPixels(originalPixels, 0, _width, 0, 0, _width, _height);
	}
    
    public Bitmap process(int radius) {
		if (radius < 1 )
			radius = 1;
		currentPixels = originalPixels.clone();
		int wm=_width-1;
		int hm=_height-1;
		int wh=_width*_height;
		int div=radius+radius+1;
        
		int r[]=new int[wh];
		int g[]=new int[wh];
		int b[]=new int[wh];
		int rsum,gsum,bsum,x,y,i,p,yp,yi,yw;
		int vmin[] = new int[Math.max(_width,_height)];
        
		int divsum=(div+1)>>1;
		divsum*=divsum;
		int dv[]=new int[256*divsum];
		for (i=0;i<256*divsum;i++){
			dv[i]=(i/divsum);
		}
        
		yw=yi=0;
        
		int[][] stack=new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1=radius+1;
		int routsum,goutsum,boutsum;
		int rinsum,ginsum,binsum;
        
		for (y=0;y<_height;y++){
			rinsum=ginsum=binsum=routsum=goutsum=boutsum=rsum=gsum=bsum=0;
			for(i=-radius;i<=radius;i++){
				p=currentPixels[yi+Math.min(wm,Math.max(i,0))];
				sir=stack[i+radius];
				sir[0]=(p & 0xff0000)>>16;
                sir[1]=(p & 0x00ff00)>>8;
                sir[2]=(p & 0x0000ff);
                rbs=r1-Math.abs(i);
                rsum+=sir[0]*rbs;
                gsum+=sir[1]*rbs;
                bsum+=sir[2]*rbs;
                if (i>0){
                    rinsum+=sir[0];
                    ginsum+=sir[1];
                    binsum+=sir[2];
                } else {
                    routsum+=sir[0];
                    goutsum+=sir[1];
                    boutsum+=sir[2];
                }
			}
			stackpointer=radius;
            
			for (x=0;x<_width;x++){
				if (!alpha)
					alpha = (int)(Color.alpha(originalPixels[y*_width+x]))  != 255;

				r[yi]=dv[rsum];
				g[yi]=dv[gsum];
				b[yi]=dv[bsum];
                
				rsum-=routsum;
				gsum-=goutsum;
				bsum-=boutsum;
                
				stackstart=stackpointer-radius+div;
				sir=stack[stackstart%div];
                
				routsum-=sir[0];
				goutsum-=sir[1];
				boutsum-=sir[2];
                
				if(y==0){
					vmin[x]=Math.min(x+radius+1,wm);
				}
				p=currentPixels[yw+vmin[x]];
                
				sir[0]=(p & 0xff0000)>>16;
                sir[1]=(p & 0x00ff00)>>8;
                sir[2]=(p & 0x0000ff);
                
                rinsum+=sir[0];
                ginsum+=sir[1];
                binsum+=sir[2];
                
                rsum+=rinsum;
                gsum+=ginsum;
                bsum+=binsum;
                
                stackpointer=(stackpointer+1)%div;
                sir=stack[(stackpointer)%div];
                
                routsum+=sir[0];
                goutsum+=sir[1];
                boutsum+=sir[2];
                
                rinsum-=sir[0];
                ginsum-=sir[1];
                binsum-=sir[2];
                
                yi++;
			}
			yw+=_width;
		}
		for (x=0;x<_width;x++){
			rinsum=ginsum=binsum=routsum=goutsum=boutsum=rsum=gsum=bsum=0;
			yp=-radius*_width;
			for(i=-radius;i<=radius;i++){
				yi=Math.max(0,yp)+x;
                
				sir=stack[i+radius];
                
				sir[0]=r[yi];
				sir[1]=g[yi];
				sir[2]=b[yi];
                
				rbs=r1-Math.abs(i);
                
				rsum+=r[yi]*rbs;
				gsum+=g[yi]*rbs;
				bsum+=b[yi]*rbs;
                
				if (i>0){
					rinsum+=sir[0];
					ginsum+=sir[1];
					binsum+=sir[2];
				} else {
					routsum+=sir[0];
					goutsum+=sir[1];
					boutsum+=sir[2];
				}
                
				if(i<hm){
					yp+=_width;
				}
			}
			yi=x;
			stackpointer=radius;	
			for (y=0;y<_height;y++){
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				if ( alpha )
					currentPixels[yi] = (0xff000000 & currentPixels[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];
				else
					currentPixels[yi]=0xff000000 | (dv[rsum]<<16) | (dv[gsum]<<8) | dv[bsum];
                
				rsum-=routsum;
				gsum-=goutsum;
				bsum-=boutsum;
                
				stackstart=stackpointer-radius+div;
				sir=stack[stackstart%div];
                
				routsum-=sir[0];
				goutsum-=sir[1];
				boutsum-=sir[2];
                
				if(x==0){
					vmin[y]=Math.min(y+r1,hm)*_width;
				}
				p=x+vmin[y];
                
				sir[0]=r[p];
				sir[1]=g[p];
				sir[2]=b[p];
                
				rinsum+=sir[0];
				ginsum+=sir[1];
				binsum+=sir[2];
                
				rsum+=rinsum;
				gsum+=ginsum;
				bsum+=binsum;
                
				stackpointer=(stackpointer+1)%div;
				sir=stack[stackpointer];
                
				routsum+=sir[0];
				goutsum+=sir[1];
				boutsum+=sir[2];
                
				rinsum-=sir[0];
				ginsum-=sir[1];
				binsum-=sir[2];
                
				yi+=_width;
			}
		}
		return returnBlurredImage();
	}
    
	public Bitmap returnBlurredImage() {
		Bitmap newBmp = Bitmap.createBitmap(_image.getWidth(), _image.getHeight(), Config.ARGB_8888);
		Canvas c = new Canvas(newBmp);
        
		c.drawBitmap(_image, 0, 0, new Paint());
		newBmp.setPixels(currentPixels, 0, _width, 0, 0, _width, _height);
		return newBmp;
	}
    
/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_fuzzy_main, menu);
        return true;
    }*/
}
