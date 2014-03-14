package com.example.getgeminiimsi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import org.apache.http.util.EncodingUtils;

import com.example.demo_highlights.R;

import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GetGeminiIMSIActivity extends Activity {
	private String LOG_PATH_SDCARD_DIR;		//日志文件在sdcard中的路径
	private static final String SDCARD_LOG_FOLDER_NAME = "mifi_log";
	private String logServiceLogName = "Log.log";//本服务输出的日志文件名称
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//日志名称格式
	private SharedPreferences mShare;
	private TelephonyManager tm;
	private Button mButton;
	private TextView mTextView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_gemini_imsi);
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTextView = (TextView) findViewById(R.id.textView_imsi);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str = getIMSI();
				mTextView.setText(str);
				Toast.makeText(GetGeminiIMSIActivity.this, "IMSI:" + str, Toast.LENGTH_SHORT).show();
				Log.d("GetGeminiIMSIActivity", "IMSI:" + str);
//				Log.d("LogService", "IMSI:" + str);
//				Intent it = new Intent();
//				it.setClassName("com.android.settings", "com.android.settings.MasterClear");
//				startActivity(it);
//				getLog();
//	                sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
	                // Intent handling is asynchronous -- assume it will happen soon.
//				GetInetAddress("www.51tgt.com");
				
//                Intent intent = new Intent("com.mediatek.intent.System_Update_Entry");
//                startActivity(intent);
				
//                sendBroadcast(new Intent(Intent.ACTION_BATTERY_LOW));
//                Toast.makeText(GetGeminiIMSIActivity.this, "send ACTION_BATTERY_LOW", Toast.LENGTH_SHORT).show();
			
//				 Toast.makeText(GetGeminiIMSIActivity.this, "hasStorageUsage:" + hasStorageUsage(), Toast.LENGTH_SHORT).show();
//				Intent i = new Intent(Intent.ACTION_REBOOT);
//				i.putExtra("nowait", 1);
//				i.putExtra("interval", 1);
//				i.putExtra("window", 0);
//				sendBroadcast(i);
				
//                String cmd = "su -c reboot";
//                try {
//                        Runtime.getRuntime().exec(cmd);
//                } catch (IOException e) {
//                        // TODO Auto-generated catch block
//
//                        new AlertDialog.Builder(GetGeminiIMSIActivity.this).setTitle("Error").setMessage(
//                                        e.getMessage()).setPositiveButton("OK", null).show();
//                }
				

			 
			 
			 
//			PackageManager pm = /*activity*/GetGeminiIMSIActivity.this.getPackageManager();
//			Class[] arrayOfClass = new Class[2];
//			Class localClass2 = Long.TYPE;
//			arrayOfClass[0] = localClass2;
//			arrayOfClass[1] = IPackageDataObserver.class;
//			Method localMethod = pm.getClass().getMethod("freeStorageAndNotify", arrayOfClass);
//			Long localLong = Long.valueOf(getEnvironmentSize() - 1L);
//			Object[] arrayOfObject = new Object[2];
//			arrayOfObject[0] = localLong;
//			localMethod.invoke(pm,localLong,new IPackageDataObserver.Stub(){
//			  public void onRemoveCompleted(String packageName,boolean succeeded) throws RemoteException {
//			       // TODO Auto-generated method stub
//			}});
				
			}
		});
//        mButton.isEnabled();
    }

	private static long getEnvironmentSize()
    {
      File localFile = Environment.getDataDirectory();
      long l1;
      if (localFile == null)
        l1 = 0L;
      while (true)
      {
         
        String str = localFile.getPath();
        StatFs localStatFs = new StatFs(str);
        long l2 = localStatFs.getBlockSize();
        l1 = localStatFs.getBlockCount() * l2;
        return l1;
      }
    }
    
    public boolean hasStorageUsage() {
        File path = Environment.getExternalStorageDirectory()/*getDataDirectory()*/;
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        float freeStorage = (availableBlocks * blockSize)/(1024 * 1024);
        if (freeStorage >= /*minStorageUsage*/ 15/* * 1024 * 1024*/) {
               return true;
        }
        return false;
    }
    
//    android将域名转化为IP
//
//    我们只需要知道网页的域名，就可以将它转化为我们所需要的IP//将域名转换为IP

    	public void GetInetAddress(final String  host){
    		new Thread() {
    			public void run() {
    		String IPAddress = ""; 
    		InetAddress ReturnStr1 = null;
    		try {
    			InetAddress[] ip = InetAddress.getAllByName(host);
    			Log.d("zhengpangyong1", ip[0].getHostAddress());
//    			Toast.makeText(GetGeminiIMSIActivity.this, "IPAddress:ip[0].getHostAddress()", Toast.LENGTH_SHORT).show();
    			
    			ReturnStr1 = java.net.InetAddress.getByName(host);
    			IPAddress = ReturnStr1.getHostAddress();

    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
//    			Toast.makeText(GetGeminiIMSIActivity.this, "IPAddress:error", Toast.LENGTH_SHORT).show();
//    			return  IPAddress;
    			Log.d("zhengpanyong2", "IPAddress:" + IPAddress);
    		}
//    		Toast.makeText(GetGeminiIMSIActivity.this, "IPAddress:" + IPAddress, Toast.LENGTH_SHORT).show();
//    		return IPAddress;
    		Log.d("zhengpanyong3", "IPAddress:" + IPAddress);
    		if (IPAddress.equals("")	) {
    			Log.d("zhengpanyong4", "Ip为空");
    		}
    		final String strIp = IPAddress;
    		mTextView.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mTextView.setText("IPAddress:" + strIp);
				}
			});
    			}
    		}.start();
			
    	}
    
	public void getLog() {
		new Thread() {
			public void run() {
				LOG_PATH_SDCARD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
						+	SDCARD_LOG_FOLDER_NAME;
				
				String fileName = null;
				   File fileDir = new File(LOG_PATH_SDCARD_DIR);
				      if(fileDir.isDirectory()){
				           File [] allFiles = fileDir.listFiles();
							int len = allFiles.length;
							if (len < 1) {
								return;
							}
				           Arrays.sort(allFiles, new FileComparator());
				           fileName = allFiles[0].getName();

				} else {
					Log.d("zhengpanyong", "error");
					return;
				}
				
				if (/* LastLogFileName */fileName == null) {
					Log.d("zhengpanyong", "fileName == null");
					return;
				}
				String res = "";
				String log = "";
				String logdata = "";

				try {

					File file = new File(LOG_PATH_SDCARD_DIR + File.separator
							+ /* LastLogFileName */fileName);
					FileInputStream fin = new FileInputStream(file);

					int length = fin.available();

					byte[] buffer = new byte[length];

					fin.read(buffer);

					res = EncodingUtils.getString(buffer, "UTF-8");

					fin.close();

				} catch (Exception e) {
					e.printStackTrace();
					Log.d("zhengpanyong", "getLog failed");
				}

				if (res != null) {
					String[] lines = res.split("\n");
					for (String line : lines) {
						if (!line.contains("beginning")) {
							Calendar c = Calendar.getInstance();
							int year = c.get(Calendar.YEAR);
							if (line.contains("MiFiService")) {
								log = log + "SYSE" + ":";
								logdata = line.substring(41);
							} else if (line.contains("LogService")) {
								log = log + "SYSE" + ":";
								logdata = line.substring(40);
							} else if (line.contains("DeviceManagerService")) {
								log = log + "SYSE" + ":";
								logdata = line.substring(50);
							} else if (line.contains("CheckPinService")) {
								log = log + "SYSE" + ":";
								logdata = line.substring(45);
							} else if (line.contains("BootBroadcastReceiver")) {
								log = log + "OPEN" + ":";
								logdata = line.substring(51);
							} else if (line.contains("APN")) {
								log = log + "NETE" + ":";
								logdata = line.substring(34);
							} else if (line.contains("Api")) {
								log = log + "NETE" + ":";
								logdata = line.substring(34);
							} else if (line.contains("MobileDataSet")) {
								log = log + "NETE" + ":";
								logdata = line.substring(43);
							} else if (line.contains("PinSet")) {
								log = log + "SYSE" + ":";
								logdata = line.substring(36);
							} else if (line.contains("WebCheck")) {
								log = log + "WEBS" + ":";
								logdata = line.substring(39);
							} else if (line.contains("WiFiApSet")) {
								log = log + "SYSE" + ":";
								logdata = line.substring(39);
							} else if (line.contains("CheckImeiAndImsi")) {
								log = log + "SYSE" + ":";
								logdata = line.substring(46);
							} else if (line.contains("FlowSet")) {
								log = log + "SYSE" + ":";
								logdata = line.substring(37);
							} else {
								
							}
							String date = line.substring(0, 14);
							date = date.replace(" ", "");
							date = date.replace(":", "");
							date = date.replace("：", "");
							date = date.replace("-", "");
							logdata = logdata.replace(":", " ");
							logdata = logdata.replace("：", " ");
							logdata = logdata.replace(";", ",");
//							logdata = logdata.replace("\n", " ");

							log = log + year + date + ":" + logdata + ";";
						}
					}
				}
				mShare = GetGeminiIMSIActivity.this
						.getSharedPreferences("LOG", 0);
				mShare.edit().putString("LOG", log).commit();
				mShare.edit().putString("LOG_name", fileName).commit();
//				Toast.makeText(GetGeminiIMSIActivity.this, log, Toast.LENGTH_LONG).show();
			}
		}.start();
	}
	
	/**
	 * 去除文件的扩展类型（.log）
	 * @param fileName
	 * @return
	 */
	private String getFileNameWithoutExtension(String fileName){
		return fileName.substring(0, fileName.indexOf("."));
	}
    
	class FileComparator implements Comparator<File>{
		public int compare(File file1, File file2) {
			if(logServiceLogName.equals(file1.getName())){
				return -1;
			}else if(logServiceLogName.equals(file2.getName())){
				return 1;
			}
			
			String createInfo1 = getFileNameWithoutExtension(file1.getName());
			String createInfo2 = getFileNameWithoutExtension(file2.getName());
			
			try {
				Date create1 = sdf.parse(createInfo1);
				Date create2 = sdf.parse(createInfo2);
				if(create1.before(create2)){
					return -1;
				}else{
					return 1;
				}
			} catch (ParseException e) {
				return 0;
			}
		}
	}
	
    public String getIMSI() {
        return tm.getSubscriberId();
    }
}
