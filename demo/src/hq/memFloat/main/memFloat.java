package hq.memFloat.main;

import com.example.demo_highlights.R;

import hq.memFloat.service.FloatService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class memFloat extends Activity {
	Button btnstart;
	 Button btnstop;
	 TextView tv;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_float);
        
        btnstart = (Button) findViewById(R.id.btnstart);
        btnstart.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent service = new Intent();
        		service.setClass(memFloat.this, FloatService.class);		
        		startService(service);
            }
        });
        
        btnstop = (Button) findViewById(R.id.btnstop);
        btnstop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent serviceStop = new Intent();
        		serviceStop.setClass(memFloat.this, FloatService.class);
        		stopService(serviceStop);
            }
        });       
        tv= (TextView) findViewById(R.id.tv);
        
        
        
        String str=new StringBuilder()
        .append("\n")
		.toString();
        tv.setText(str);        
    }
    @Override
    protected void onStop(){
     super.onStop();
     Log.v("stop","stop");
     //createView();
    }
    @Override
    protected void onRestart(){
     super.onRestart();
     Log.v("restart","restart");
     
    }
  
}