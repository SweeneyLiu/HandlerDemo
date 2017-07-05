package com.lsw.demo;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int UPDATE_TEXT = 1;
    private static final String TAG = "MainActivity";
    private TextView text;
    private Button changeTextBtn;

    //handler使用方式一
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

    //handler使用方式二
    /*private Handler handler2 = new Handler(new Handler.Callback(){

        @Override
        public boolean handleMessage(Message msg) {
            return false;//1.返回false，下面的handleMessage可以得到执行 2.返回true,下面的handleMessage不能得到执行
        }
    }){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };*/

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
                        //更新UI方法一
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //新建message三种方式
//                                Message message = handler.obtainMessage();
//                                Message message = Message.obtain();
                                Message message = new Message();
                                message.what = UPDATE_TEXT;
                                handler.sendMessage(message); // 将Message对象发送出去
                            }
                        }).start();
                        //更新UI方法二
                        //先调用handler.post(Runnable r),最终还是会调用到handler.sendMessageDelayed(Message msg, long delayMillis)方法
                        /*runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText("更新UI方法二");
                            }
                        });*/

                        //更新UI方法三
                        //最终还是会调用到handler.sendMessageDelayed(Message msg, long delayMillis)方法
                        /*handler.post(new Runnable() {
                            @Override
                            public void run() {
                                text.setText("更新UI方法三");
                            }
                        });*/

                        //更新UI方法四
                        //先调用handler.post(Runnable r),最终还是会调用到handler.sendMessageDelayed(Message msg, long delayMillis)方法
                        text.post(new Runnable() {
                            @Override
                            public void run() {
                                text.setText("更新UI方法四");
                            }
                        });

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
                Log.i(TAG, "run: threadHandler");
                Looper.loop();
                Log.i(TAG, "run: threadHandler");//不会被调用，因为Looper.loop()为阻塞函数*/
                //子线程弹toast
                //Toast.makeText(MainActivity.this, "子线程弹toast", Toast.LENGTH_SHORT).show();

                Handler handler = new Handler(Looper.getMainLooper());
            }
        }).start();


        //HandlerThread的用法
        /*HandlerThread handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());*/

    }
}
