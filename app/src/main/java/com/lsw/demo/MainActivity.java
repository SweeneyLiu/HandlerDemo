package com.lsw.demo;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int UPDATE_TEXT = 1;
    private TextView text;
    private Button changeTextBtn;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    // 在这里可以进行UI操作
                    text.setText("Nice to meet you");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView)findViewById(R.id.text_view);
        changeTextBtn = (Button)findViewById(R.id.change_text);
        changeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.change_text:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = UPDATE_TEXT;
                                handler.sendMessage(message); // 将Message对象发送出去
                            }
                        }).start();
                        break;
                    default:
                      break;
                }
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                //子线程new Handler
                /*Looper.prepare();
                Handler threadHandler = new Handler();
                Looper.loop();*/

                //子线程弹toast
                //Toast.makeText(MainActivity.this, "子线程弹toast", Toast.LENGTH_SHORT).show();

                Handler handler = new Handler(Looper.getMainLooper());
            }
        }).start();

    }
}
