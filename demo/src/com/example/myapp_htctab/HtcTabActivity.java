package com.example.myapp_htctab;

import com.example.demo_highlights.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class HtcTabActivity extends Activity {
private TextView mTextView;
private EditText mEditText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_htc_tab);
//        mTextView = (TextView) findViewById(R.id.textview);
//        mEditText = (EditText) findViewById(R.id.edittext);
//        mEditText.clearFocus();
//        
//        mTextView.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//			mEditText.requestFocus();	
//			InputMethodManager imm = (InputMethodManager)
//					  getSystemService(Context.INPUT_METHOD_SERVICE);
//					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//			}
//		});
    }
}
