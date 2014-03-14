package com.example.myapp_location;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.GeoPoint;
import com.example.demo_highlights.R;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends Activity implements LocationListener, OnClickListener{

    /**
     * 百度MapAPI的管理类
     */
    public BMapManager mMapManager = null;
    
    /**
     * 当前位置对象
     */
    private GeoPoint locationGeoPoint;
    
    /**
     * 位置管理对象
     */
    private MKLocationManager mLocationManager;
    
    /**
     * 百度地图KEY
     */
    public String mapKey = "333C0A15D54AC6775A0646E1980CABE6B7096029";
    
    public static boolean m_bKeyRight = true; // 授权Key正确，验证通过
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        
        mMapManager = new BMapManager(this);
        mMapManager.init(mapKey, new MyGeneralListener());
        mMapManager.getLocationManager().setNotifyInternal(10, 5);
        
		mMapManager.start();
		mLocationManager = mMapManager.getLocationManager();
        
        Button b = (Button) findViewById(R.id.getlocation);
        b.setOnClickListener(this);
    }

    /**
     * 常用事件监听，用来处理通常的网络错误，授权验证错误等<BR>
     * [功能详细描述]
     * @author 黄广府
     * @version [WalkTour Client V100R001C03, 2012-5-29]
     */
    public static class MyGeneralListener implements MKGeneralListener {
        @Override
        public void onGetNetworkState(int iError) {
            Log.d("MyGeneralListener", "onGetNetworkState error is " + iError);
//            Toast.makeText(LocationActivity.this,
//                    "您的网络出错啦！",
//                    Toast.LENGTH_LONG).show();
            Log.d("zhengpanyong", "您的网络出错啦！");
        }
        
        @Override
        public void onGetPermissionState(int iError) {
            Log.d("MyGeneralListener", "onGetPermissionState error is "
                    + iError);
            if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
                // 授权Key错误：
//                Toast.makeText(LocationActivity.this,
//                        "请在BMapApiDemoApp.java文件输入正确的授权Key！",
//                        Toast.LENGTH_LONG).show();
            	Log.d("zhengpanyong", "输入正确的授权Key！");
               m_bKeyRight = false;
            }
        }
    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mLocationManager.removeUpdates(this);
		mMapManager.stop();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mLocationManager != null) {
			mLocationManager.requestLocationUpdates(this);
		}
		if (mMapManager != null) {
			mMapManager.start();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
        if (location != null) {
            String strLog = String.format("您当前的位置:\r\n" + "纬度:%f\r\n" + "经度:%f",
                    location.getLongitude(),
                    location.getLatitude());
            if (locationGeoPoint == null) {
                locationGeoPoint = new GeoPoint(
                        (int) (location.getLatitude() * 1e6),
                        (int) (location.getLongitude() * 1e6));
            } else {
                locationGeoPoint.setLatitudeE6((int) (location.getLatitude() * 1e6));
                locationGeoPoint.setLongitudeE6((int) (location.getLongitude() * 1e6));
            }
        }
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.getlocation:
            if (locationGeoPoint == null) {
                Toast.makeText(LocationActivity.this,
                        getString(R.string.location_wait),
                        Toast.LENGTH_LONG).show();
            } else {
            	TextView tv = (TextView) findViewById(R.id.location);
            	tv.setText(getString(R.string.place) + "（" + locationGeoPoint.getLatitudeE6() + ", " + locationGeoPoint.getLongitudeE6() + "）");
            	Toast.makeText(LocationActivity.this, tv.getText(), Toast.LENGTH_SHORT).show();
            }
			break;

		default:
			break;
		}
	}
}