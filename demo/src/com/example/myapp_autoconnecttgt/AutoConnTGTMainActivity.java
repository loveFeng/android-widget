package com.example.myapp_autoconnecttgt;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.example.demo_highlights.R;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AutoConnTGTMainActivity extends Activity implements OnClickListener{



	private String mStrSSID = null;
	private WifiManager mWifiManager;
	private ConnectivityManager mCManager;
	private WifiCipherType mType;
	private ListView mListView;
	private TextView mNotifyInfo;
	private ArrayList<String> mTGTwifiList = new ArrayList<String>();
	
	private String mNeedToConnWifi = null;
    public enum WifiCipherType {
        /*WIFICIPHER_WEP,*/ WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID, WIFICIPHER_WPA2
    }
    
	//接受广播，弹出网络状态消息
	private BroadcastReceiver mNetStatusReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			
			NetworkInfo WifiInfo;
			NetworkInfo MobileInfo;
			
			WifiInfo = mCManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			MobileInfo = mCManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			
			if (WifiInfo.isConnected()) {
				setTitle(getString(R.string.alreadyconned) + mWifiManager.getConnectionInfo().getSSID().toString());
				Toast.makeText(AutoConnTGTMainActivity.this, getString(R.string.alreadyconned) + mWifiManager.getConnectionInfo().getSSID().toString(), Toast.LENGTH_SHORT).show();
				if (isTGTwifi(mWifiManager.getConnectionInfo().getSSID())) {
					Intent webintent = new Intent();
					webintent.setAction("android.intent.action.VIEW");
					Uri uri = Uri.parse("http://192.168.43.1:8080/mifi"
							/*"http://192.168.43.1:8080/mifi/robot/main?method=update&setting=reportDev"*/);
					webintent.setData(uri);
					startActivity(webintent);
				} else {
					Toast.makeText(AutoConnTGTMainActivity.this, getString(R.string.not_tgt_wifi) , Toast.LENGTH_SHORT).show();
				}
			} else {
				setTitle(getString(R.string.no_conn_wifi));
			}
			if (MobileInfo.isConnected()) {
			}
		}
	};
    
	private boolean isTGTwifi(String ssid) {
		String strSSID = ssid.toLowerCase();
		if (strSSID.contains("tgt") && strSSID.length() >= 14) {
			return true;
		}
		return false;
	}
	
	private void setReceiver(Context context) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mNetStatusReceiver, filter);
	}
	
	private void unsetReceiver() {
		unregisterReceiver(mNetStatusReceiver);
	}
	
	public OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if (mTGTwifiList.isEmpty()) {
				Toast.makeText(AutoConnTGTMainActivity.this, getString(R.string.need_refresh_wifi), Toast.LENGTH_SHORT).show();
				return;
			}
			mNeedToConnWifi = mTGTwifiList.get(arg2);
	        if (mNeedToConnWifi == null) {
	        	mNotifyInfo.setText(getString(R.string.noChoiceToConnFirst));
	        } else {
	        	mNotifyInfo.setText(getString(R.string.conn_to) + mNeedToConnWifi);
	        }
		}
		
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_conn_tgtmain);
        setReceiver(this);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mCManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        mListView = (ListView) findViewById(R.id.listview);
        mListView.setOnItemClickListener(mItemClickListener);
        mNotifyInfo = (TextView) findViewById(R.id.notify_info);
        Button b = (Button) findViewById(R.id.conn);
        b.setOnClickListener(this);
        
        b = (Button) findViewById(R.id.scan);
        b.setOnClickListener(this);
        
        b = (Button) findViewById(R.id.jumpweb);
        b.setOnClickListener(this);
        
        b = (Button) findViewById(R.id.upload);
        b.setOnClickListener(this);
        
        targetWifiCanScanForlist();
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unsetReceiver();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	private boolean targetWifiCanScanForlist(){
		mTGTwifiList.clear();
		mWifiManager.startScan();
		List<ScanResult> scanResultList = mWifiManager.getScanResults();
		if (scanResultList != null && scanResultList.size() > 0) {
			for (int i = 0; i < scanResultList.size(); i++) {
				ScanResult scanResult = scanResultList.get(i);
				
			StringBuffer str = new StringBuffer()
			                   .append("SSID: " + scanResult.SSID).append("\n")
			                   .append("BSSID: " + scanResult.BSSID).append("\n")
			                   .append("capabilities: " + scanResult.capabilities).append("\n")
			                   .append("level: " + scanResult.level).append("\n")
			                   .append("frequency: " + scanResult.frequency);
			Log.d("zhengpanyong", str.toString());
				String strSSID = scanResult.SSID.toLowerCase();
				if (strSSID.contains("tgt") && strSSID.length() == 14) {
					mStrSSID = scanResult.SSID;
					
					mTGTwifiList.add(scanResult.SSID);
//					String strType = scanResult.capabilities;
//					if (strType.contains("WPA2")) {
//						mType = WifiCipherType.WIFICIPHER_WPA2;
//					} else if (strType.contains("WPA")) {
//						mType = WifiCipherType.WIFICIPHER_WPA;
//					} else {
//						mType = WifiCipherType.WIFICIPHER_NOPASS;
//					}
//					return true;
				}
			}
		}
//		Log.d("zhengpanyong", "为扫描到指定tgt开头的wifi热点！");
		
		mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,mTGTwifiList));
		Toast.makeText(AutoConnTGTMainActivity.this, getString(R.string.wifilist_alreadyrefresh), Toast.LENGTH_SHORT).show();
		return false;
	}
	
	private boolean targetWifiCanScan(){
		List<ScanResult> scanResultList = mWifiManager.getScanResults();
		if (scanResultList != null && scanResultList.size() > 0) {
			for (int i = 0; i < scanResultList.size(); i++) {
				ScanResult scanResult = scanResultList.get(i);
				
			StringBuffer str = new StringBuffer()
			                   .append("SSID: " + scanResult.SSID).append("\n")
			                   .append("BSSID: " + scanResult.BSSID).append("\n")
			                   .append("capabilities: " + scanResult.capabilities).append("\n")
			                   .append("level: " + scanResult.level).append("\n")
			                   .append("frequency: " + scanResult.frequency);
			Log.d("zhengpanyong", str.toString());
				String strSSID = scanResult.SSID.toLowerCase();
				if (strSSID.contains("tgt") && strSSID.length() == 14 && (mNeedToConnWifi == null || scanResult.SSID.equals(mNeedToConnWifi))) {
					mStrSSID = scanResult.SSID;
					String strType = scanResult.capabilities;
					if (strType.contains("WPA2")) {
						mType = WifiCipherType.WIFICIPHER_WPA2;
					} else if (strType.contains("WPA")) {
						mType = WifiCipherType.WIFICIPHER_WPA;
					} else {
						mType = WifiCipherType.WIFICIPHER_NOPASS;
					}
					return true;
				}
			}
		}
		Log.d("zhengpanyong", getString(R.string.noScanningWifi));
		return false;
	}
    
    private WifiConfiguration CreateWifiInfo(String SSID, String Password,
            WifiCipherType Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        if (Type == WifiCipherType.WIFICIPHER_NOPASS) {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
//        if (Type == WifiCipherType.WIFICIPHER_WEP) {
//            config.preSharedKey = "\"" + Password + "\"";
//            config.hiddenSSID = true;
//            config.allowedAuthAlgorithms
//                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
//            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
//            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
//            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
//            config.allowedGroupCiphers
//                    .set(WifiConfiguration.GroupCipher.WEP104);
//            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            config.wepTxKeyIndex = 0;
//        }
        if (Type == WifiCipherType.WIFICIPHER_WPA) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.status = WifiConfiguration.Status.ENABLED;
        } 
        if (Type == WifiCipherType.WIFICIPHER_WPA2) {

	        config.preSharedKey = "\"" + Password + "\"";
	        config.hiddenSSID = true;
	        
            //[WPA2-PSK-TKIP]
            config.preSharedKey = "\""+Password+"\"";    
            //config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);    
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40); 
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);                          
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);                     
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA); 
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP); 
            config.status = WifiConfiguration.Status.ENABLED;    
          

        } 
        WifiConfiguration tempConfig = config;
        if (tempConfig != null) {
        Log.d("zhengpanyong2","BSSID:" + tempConfig.BSSID + 
        		",\n networkId:" + tempConfig.networkId +
        		",\n preSharedKey:" + tempConfig.preSharedKey +
        		",\n priority:" + tempConfig.priority +
        		",\n SSID:" + tempConfig.SSID +
        		",\n status:" + tempConfig.status +
        		",\n wepTxKeyIndex:" + tempConfig.wepTxKeyIndex +
        		",\n allowedAuthAlgorithms:" + tempConfig.allowedAuthAlgorithms +
        		",\n allowedGroupCiphers:" + tempConfig.allowedGroupCiphers +
        		",\n allowedKeyManagement:" + tempConfig.allowedKeyManagement +
        		",\n allowedProtocols:" + tempConfig.allowedProtocols +
        		",\n allowedPairwiseCiphers:" + tempConfig.allowedPairwiseCiphers +
        		",\n wepKeys:" + tempConfig.wepKeys +
        		",\n hiddenSSID:" + tempConfig.hiddenSSID );
        }
        return config;
    }
    
	   private WifiConfiguration IsExsits(String SSID) {
	        List<WifiConfiguration> existingConfigs = mWifiManager
	                .getConfiguredNetworks();
	        for (WifiConfiguration existingConfig : existingConfigs) {
	            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
	                return existingConfig;
	            }
	        }
	        return null;
	    }
	
		private boolean OpenWifi(Context context) {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			boolean bRet = true;
			if (!wifiManager.isWifiEnabled()) {
				bRet = wifiManager.setWifiEnabled(true);
			}
			return bRet;
		}
	   
    public boolean Connect(String SSID, String Password, WifiCipherType Type) {
//      if (!this.OpenWifi()) {
//          return false;
//      }
      // 开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
      // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
      while (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
          try {
              // 为了避免程序一直while循环，让它睡个100毫秒在检测……
              Thread.currentThread();
              Thread.sleep(500);
          } catch (InterruptedException ie) {
          }
      }

      WifiConfiguration wifiConfig = CreateWifiInfo(SSID, Password, Type);

      if (wifiConfig == null) {
          return false;
      }

      WifiConfiguration tempConfig = this.IsExsits(SSID);

      
      if (tempConfig != null) {
          Log.d("zhengpanyong","BSSID:" + tempConfig.BSSID + 
          		",\n networkId:" + tempConfig.networkId +
          		",\n preSharedKey:" + tempConfig.preSharedKey +
          		",\n priority:" + tempConfig.priority +
          		",\n SSID:" + tempConfig.SSID +
          		",\n status:" + tempConfig.status +
          		",\n wepTxKeyIndex:" + tempConfig.wepTxKeyIndex +
          		",\n allowedAuthAlgorithms:" + tempConfig.allowedAuthAlgorithms +
          		",\n allowedGroupCiphers:" + tempConfig.allowedGroupCiphers +
          		",\n allowedKeyManagement:" + tempConfig.allowedKeyManagement +
          		",\n allowedProtocols:" + tempConfig.allowedProtocols +
          		",\n allowedPairwiseCiphers:" + tempConfig.allowedPairwiseCiphers +
          		",\n wepKeys:" + tempConfig.wepKeys +
          		",\n hiddenSSID:" + tempConfig.hiddenSSID );
      	mWifiManager.removeNetwork(tempConfig.networkId);
      }

      int netID = mWifiManager.addNetwork(wifiConfig);
      boolean bRet = mWifiManager.enableNetwork(netID, true);
      return bRet;
  }
    public void autoUpLoad() {
    	  new Thread(new Runnable() {
  			
  			@Override
  			public void run() {
  		    	HttpURLConnection urlConnection = null;  
  		    	try {  
  		    	    URL url = new URL("http://192.168.43.1:8080/mifi/robot/main?method=update&setting=reportDev");  
  		    	    urlConnection = (HttpURLConnection)url.openConnection();  
  		    	    InputStream in = new BufferedInputStream(urlConnection.getInputStream());  
  		    	    String result = readInStream(in);  
  		    	    Log.d("zhengpanyong", "result:" + result);
  		    	} catch (MalformedURLException e) {  
  		    	    e.printStackTrace();  
  		    	} catch (IOException e) {  
  		    	    e.printStackTrace();  
  		    	} finally {  
  		    	    urlConnection.disconnect();  
  		    	} 
  			}
  		}).start();
    }
    
    private String readInStream(InputStream in) {  
        Scanner scanner = new Scanner(in).useDelimiter("\\A");  
        return scanner.hasNext() ? scanner.next() : "";  
    } 
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.conn:
			
			if(targetWifiCanScan()) {
//				setWifiDormancy();
				if (Connect(mStrSSID, (Md5.getMD5(mStrSSID)).substring(0, 8), mType)) {
					setTitle(getString(R.string.conn_wifi_to) + mStrSSID);
				};
				
			} else {
				Toast.makeText(AutoConnTGTMainActivity.this, getString(R.string.noScanningWifi), Toast.LENGTH_LONG).show();
				Log.d("zhengpanyong", getString(R.string.noScanningWifi));
			}
			break;
		case R.id.scan:
			targetWifiCanScanForlist();
			break;
		case R.id.jumpweb:
			NetworkInfo WifiInfo;
			
			WifiInfo = mCManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			
			if (WifiInfo.isConnected()) {
				setTitle(getString(R.string.alreadyconned) + mWifiManager.getConnectionInfo().getSSID());
				Toast.makeText(AutoConnTGTMainActivity.this, getString(R.string.alreadyconned) + mWifiManager.getConnectionInfo().getSSID().toString(), Toast.LENGTH_SHORT).show();
				if (isTGTwifi(mWifiManager.getConnectionInfo().getSSID().toString())) {
					Intent webintent = new Intent();
					webintent.setAction("android.intent.action.VIEW");
					Uri uri = Uri.parse("http://192.168.43.1:8080/mifi"
							/*"http://192.168.43.1:8080/mifi/robot/main?method=update&setting=reportDev"*/);
					webintent.setData(uri);
					startActivity(webintent);
				} else {
					Toast.makeText(AutoConnTGTMainActivity.this, getString(R.string.not_tgt_wifi) , Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(AutoConnTGTMainActivity.this, getString(R.string.no_conn_wifi), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.upload:
			NetworkInfo WifiInfoConn;
			
			WifiInfoConn = mCManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			
			if (WifiInfoConn.isConnected()) {
				setTitle(getString(R.string.alreadyconned) + mWifiManager.getConnectionInfo().getSSID());
				Toast.makeText(AutoConnTGTMainActivity.this, getString(R.string.alreadyconned) + mWifiManager.getConnectionInfo().getSSID().toString(), Toast.LENGTH_SHORT).show();
				if (isTGTwifi(mWifiManager.getConnectionInfo().getSSID().toString())) {
					autoUpLoad();
				} else {
					Toast.makeText(AutoConnTGTMainActivity.this, getString(R.string.not_tgt_wifi_ul) , Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(AutoConnTGTMainActivity.this, getString(R.string.no_conn_wifi), Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
}
