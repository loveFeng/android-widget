package com.example.demo_highlights;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.myapp_menusetting.DemoSettingActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.graphics.drawable.Drawable;

public class DEMOMainActivity extends ListActivity implements OnItemClickListener{
	private static final String SHORT_CUT_EXTRAS = "com.terry.extra.short";
	
    @TargetApi(11)
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Intent.ACTION_CREATE_SHORTCUT.equals(getIntent().getAction())) {
            createShortCut();
            finish();
            return;
        }
        
        setListAdapter(new SimpleAdapter(this, getData(),
                android.R.layout.simple_list_item_1, new String[] { "title" },
                new int[] { android.R.id.text1 }));
        getListView().setOnItemClickListener(this);
        
        // add layoutAnimation
/*        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50 * 2);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(100 * 3);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);*/
        ListView listView = getListView();      
        listView.setDivider(getResources().getDrawable(R.drawable.jblineshape));
        listView.setDividerHeight(2);
        listView.setLayoutAnimation(/*controller*/getListAnim());
        listView.setFastScrollEnabled(true);
        //add layoutAnimation
        
        changeListViewScrollbar();
        //add virtual menu
//        try {  
//            getWindow().addFlags(WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));  
//        }catch (NoSuchFieldException e) {  
//            // Ignore since this field won't exist in most versions of Android  
//        }catch (IllegalAccessException e) {  
//            Log.w("feelyou.info", "Could not access FLAG_NEEDS_MENU_KEY in addLegacyOverflowButton()", e);  
//        }  
      //add end virtual menu
        //change virtual key to point
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;
//        getWindow().setAttributes(params);
        //
        
        //hide virtual key
        WindowManager.LayoutParams params2 = getWindow().getAttributes();
        params2.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().setAttributes(params2);
        //

    }
    
    
  //修改快速滑动滑块的图片  
    private void changeListViewScrollbar() {  
        try {   
            Field f = AbsListView.class.getDeclaredField("mFastScroller");   
            f.setAccessible(true);   
            Object o=f.get(getListView());   
            f=f.getType().getDeclaredField("mThumbDrawable");   
            f.setAccessible(true);   
            Drawable drawable=(Drawable) f.get(o);   
            drawable=getResources().getDrawable(R.drawable.listview_fast_slider);   
            f.set(o,drawable);   
           // Toast.makeText(this, f.getType().getName(), 1000).show();   
        } catch (Exception e) {   
            throw new RuntimeException(e);   
        }  
          
    }  
    
//    Xlog.d(TAG, String.format("wifi: %s sig=%d act=%d", (mWifiVisible ? "VISIBLE" : "GONE"), mWifiStrengthId,
//            mWifiActivityId));
    
	private LayoutAnimationController getListAnim() {
		AnimationSet set = new AnimationSet(true);
		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(200);
		set.addAnimation(animation);

		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
		Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
		-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(200);
		set.addAnimation(animation);
		
		Animation inAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		inAnimation.setDuration(200);
		inAnimation.setFillAfter(true);
		set.addAnimation(inAnimation);
		
		Animation outAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		outAnimation.setDuration(200);
		outAnimation.setFillAfter(true);
		// 进入动画控制器
		LayoutAnimationController inController = new LayoutAnimationController(inAnimation, 0.3f);
		// 退出动画控制器
		LayoutAnimationController outController = new LayoutAnimationController(outAnimation, 0.3f);
		
		LayoutAnimationController controller = new LayoutAnimationController(
		set, 0.5f);
		return controller;
		}
    
    void createShortCut() {
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setClass(this, this.getClass());
        shortcutIntent.putExtra(SHORT_CUT_EXTRAS, "...");

        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "demo");
        Parcelable shortIcon = Intent.ShortcutIconResource.fromContext(
                this, R.drawable.ic_launcher);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, shortIcon);
        setResult(RESULT_OK, intent);
    }
    
    protected List<Map<String, Object>> getData() {
    	List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();
    	String[] titles = getResources().getStringArray(R.array.demo_names);
    	String[] intents = getResources().getStringArray(R.array.demo_intents);
    	for (int i = 0; i < titles.length; ++i) {
    		addItem(myData, titles[i], new Intent(intents[i].toString()));
    	}
        
    	return myData;
    }
    
    protected void addItem(List<Map<String, Object>> data, String name, Intent intent) {
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("title", name);
        temp.put("intent", intent);
        data.add(temp);
    }

	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
		// TODO Auto-generated method stub
        Map<String, Object> map = (Map<String, Object>)listview.getItemAtPosition(position);

        Intent intent = (Intent) map.get("intent");
        startActivity(intent);
        showAni(position);
        
	}
	
	public void showAni(int position) {
		switch (position%5) {
		case 0:
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout); 
			break;
		case 1:
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			break;
		case 2:
			overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out); 
			break;
		case 3:
			overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
		default:
			break;
		}
	}
	
	

	
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    // TODO Auto-generated method stub  
	    if(keyCode == KeyEvent.KEYCODE_BACK)  
	       {    
	           exitBy2Click();      //调用双击退出函数  
	       }  
	    
	    return false;  
	}  
	/** 
	 * 双击退出函数 
	 */  
	private static Boolean isExit = false;  
	  
	private void exitBy2Click() {  
	    Timer tExit = null;  
	    if (isExit == false) {  
	        isExit = true; // 准备退出  
//	        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();  
	        IJettyToast.showQuickToast(DEMOMainActivity.this, R.string.twice_quit);
	        tExit = new Timer();  
	        tExit.schedule(new TimerTask() {  
	            @Override  
	            public void run() {  
	                isExit = false; // 取消退出  
	            }  
	        }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务  
	  
	    } else {  
	        finish();  
	        System.exit(0);  
	    }  
	}  
	
	protected void setMenuBackground(){
	     
//	    Log.d(TAG, "开始设置菜单的的背景");
	    getLayoutInflater().setFactory( new Factory() {

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		 if ( name.equalsIgnoreCase( "com.android.internal.view.menu.IconMenuItemView" ) ){
			//上面这句Android123提醒大家不能有改动，目前来看对于原生android目前这个packageName还没有变化
				        
			 try {
				 LayoutInflater f = getLayoutInflater();
				 final View view = f.createView( name, null, attrs );  //尝试创建我们自己布局

				 new Handler().post( new Runnable() {
					 public void run () {
						 view.setBackgroundResource( R.drawable.bpush_top_bg);
					 }
				 } );
				 return view;
			 	}
			 	catch ( InflateException e ) {}
			 	catch ( ClassNotFoundException e ) {}
				}
			
		 		return null;
	    		}
	    });
	    }
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_setting_main, menu);
        setMenuBackground();
        return true;
    }
    
	private void addShortCut(String tName) {
		// 瀹夎鐨処ntent  
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

		// 蹇嵎鍚嶇О  
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, tName);
		// 蹇嵎鍥炬爣鏄厑璁搁噸澶�		shortcut.putExtra("duplicate", false);

		Intent shortcutIntent = new Intent(/*Intent.ACTION_MAIN*/"android.intent.action.oneKeyLock");
//		shortcutIntent.putExtra("tName", tName);
//		shortcutIntent.setClassName("com.feng.onekeylockscreen", "com.feng.onekeylockscreen.LockActivity");
		shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

		// 蹇嵎鍥炬爣  
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable./*ic_launcher*//*icon*/one_key_lock_screen);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

		// 鍙戦�骞挎挱  
		sendBroadcast(shortcut);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		

        
        switch (item.getItemId()) {
        case R.id.menu1:
        	
        	addShortCut("一键锁屏");
            return true;
        case R.id.menu2:
            return true;
        case R.id.menu3:
            return true;
        case R.id.menu4:
            return true;
        case R.id.menu5:
            final Intent settings = new Intent(android.provider.Settings.ACTION_SETTINGS);
            settings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(settings);
            return true;
        case R.id.menu6:
            final Intent demo_settings = new Intent(DEMOMainActivity.this, DemoSettingActivity.class);
            startActivity(demo_settings);
            return true;
    }
		return super.onOptionsItemSelected(item);
	}
	
/*  toRoundCorner(bitmap, 10);  
 * public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {  
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),  
                bitmap.getHeight(), Config.ARGB_8888);  
        Canvas canvas = new Canvas(output);  
        final int color = 0xff424242;  
        final Paint paint = new Paint();  
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
        final RectF rectF = new RectF(rect);  
        final float roundPx = pixels;  
        paint.setAntiAlias(true);  
        canvas.drawARGB(0, 0, 0, 0);  
        paint.setColor(color);  
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
        canvas.drawBitmap(bitmap, rect, rect, paint);  
        return output;  
    }  */
    
}
