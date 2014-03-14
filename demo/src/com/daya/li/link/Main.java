package com.daya.li.link;

import com.example.demo_highlights.R;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Main extends Activity {
    private TextView persent;
    private ImageView img_progress;
    /** 当前百分数 */
    private int num = 0;
    /** 是否当前为最小 */
    private boolean minus = false;
    private Button button;
    private Bitmap bmp;
    private Bitmap bmp1;
    private Bitmap bmp2;
    private TextView text;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bili);
        img_progress = (ImageView) findViewById(R.id.img_progress);
        persent = (TextView) this.findViewById(R.id.persent);
        Resources res = getResources();
        // 这里是3个不同状态的时候的图片资源
        bmp = BitmapFactory.decodeResource(res, R.drawable.taskmanager_circle_min);
        bmp1 = BitmapFactory.decodeResource(res, R.drawable.taskmanager_circle_mid);
        bmp2 = BitmapFactory.decodeResource(res, R.drawable.taskmanager_circle_full);
        text = (TextView) findViewById(R.id.text);
        text.setText(num + "%");
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                minus = !minus;// true则表示当前时最小，需要往最多的变，false表示当前最多
                persent.post(new LoopRunnable());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 1:
                if (num < 50) {
                    img_progress.setImageBitmap(getRoundedCornerBitmap(bmp, num));
                } else if (num >= 50 && num < 80) {
                    img_progress.setImageBitmap(getRoundedCornerBitmap(bmp1, num));
                } else {
                    img_progress.setImageBitmap(getRoundedCornerBitmap(bmp2, num));
                }
                text.setText((int) (num) + "%");
                break;
            }
        }
    };
    // 控制时间，对每一次刷新进行延时
    class LoopRunnable implements Runnable {

        @Override
        public void run() {
            if (num == 0 || minus) {// 一开始,从少到多
                num++;
            } else if (num == 100 || !minus) { // 从多到少
                num--;
            }
            if (0 <= num && num <= 100) {
                handler.sendEmptyMessage(1);
                if (num != 100 && num != 0) {// 到当低端或最高端的时候就不在更改了
                    persent.post(this);
                } else {
                    button.setEnabled(true);// 并且将按钮设置可用
                }
            }
        }
    }

    // 这里把bitmap图片截取出来pr是指截取比例
    public static Bitmap getRoundedCornerBitmap(Bitmap bit, float pr) {
        Bitmap bitmap = Bitmap.createBitmap(bit.getWidth(), bit.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(0, 0, 0, 0);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        // 把图片按中心旋转180度，去掉的话就是从上往下显示
        canvas.rotate(-180, bit.getWidth() / 2, bit.getHeight() / 2);
        // 截取的区域，从左上角开始（0，0），根据传入比例得出显示高度
        canvas.clipRect(0, 0, bit.getWidth(), pr * bit.getHeight() / 100);
        canvas.drawBitmap(bit, 0, 0, paint);
        return bitmap;

    }
}
