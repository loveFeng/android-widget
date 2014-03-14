package com.krislq.floating2;

import android.app.Application;
import android.view.WindowManager;
/**
 * 
 * @author <a href="mailto:kris@krislq.com">Kris.lee</a>
 * @website www.krislq.com
 * @date Nov 29, 2012
 * @version 1.0.0
 *
 */
public class FloatApplication extends Application {
	private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();

	public WindowManager.LayoutParams getWindowParams() {
		return windowParams;
	}
}
