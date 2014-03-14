package hq.memFloat.model;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.Log;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 
 * @author user
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	
	public static final String TAG = "CrashHandler";
	
	private static final String RESTART_ACTION = "com.realfame.mifi.restart";
	private static final String SDCARD_LOG_FOLDER_NAME = "mifi_crash";
	//系统默认的UncaughtException处理类 
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	//CrashHandler实例
	private static CrashHandler INSTANCE = new CrashHandler();
	//程序的Context对象
	private Context mContext;
	//用来存储设备信息和异常信息
	//private Map<String, String> infos = new HashMap<String, String>();

	//用于格式化日期,作为日志文件名的一部分
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		//获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		//设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
		Log.e(TAG, "CrashHandler init");
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		handleException(ex);
/*		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			Log.e(TAG, "error : ", e);
		}*/
		//退出程序
		//关闭数据通道，关闭wifi
		//重启应用
		Log.e(TAG, "demo uncaughtException System.exit");
/*		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);*/
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		//使用Toast来显示异常信息
		/*new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();*/

		//保存日志文件 
		saveCrashInfo2File(ex);
		return true;
	}
	
	/**
	 * 收集设备参数信息
	 * @param ctx
	 */
//	public void collectDeviceInfo(Context ctx) {
//		try {
//			PackageManager pm = ctx.getPackageManager();
//			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
//			if (pi != null) {
//				String versionName = pi.versionName == null ? "null" : pi.versionName;
//				String versionCode = pi.versionCode + "";
//				infos.put("versionName", versionName);
//				infos.put("versionCode", versionCode);
//			}
//		} catch (NameNotFoundException e) {
//			Log.e(TAG, "an error occured when collect package info", e);
//		}
//		Field[] fields = Build.class.getDeclaredFields();
//		for (Field field : fields) {
//			try {
//				field.setAccessible(true);
//				infos.put(field.getName(), field.get(null).toString());
//				Log.e(TAG, field.getName() + " : " + field.get(null));
//			} catch (Exception e) {
//				Log.e(TAG, "an error occured when collect crash info", e);
//			}
//		}
//		
//	}
	
	private String getCrashReport(Context context, Throwable ex) {
		StringBuffer exceptionStr = new StringBuffer();
		
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(), PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
			if (pinfo != null) {
				exceptionStr.append("Version: " + pinfo.versionName + "(" + pinfo.versionCode + ")\n");
				exceptionStr.append("packageName: " + pinfo.packageName + "\n");
				exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE + "(" + android.os.Build.MODEL + ")\n");
				exceptionStr.append("Build data utc: " + android.os.Build.TIME + "(" + formatter.format(new Date(android.os.Build.TIME)) + ")\n");
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		exceptionStr.append("Exception: "+ex.getMessage()+"\n");
		StackTraceElement[] elements = ex.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			exceptionStr.append(elements[i].toString()+"\n");
		}
		return exceptionStr.toString();
	}
	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return	返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {
		
		String crashLog = getCrashReport(mContext, ex);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".log";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
						+	SDCARD_LOG_FOLDER_NAME + File.separator;
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				fos.write(crashLog.getBytes());
				fos.close();
			}
			Log.e(TAG, crashLog);
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}
}
