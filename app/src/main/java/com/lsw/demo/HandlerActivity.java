package com.lsw.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by liushuwei on 2017/7/3.
 */

public class HandlerActivity extends AppCompatActivity {

    //错误代码

    /*private TextView mTextMessage;
    private Button button;

    private UpdateHandler updateHandler;



    class UpdateHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                mTextMessage.setText("update====" );
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);

        button = (Button) findViewById(R.id.btn_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextViewContent();
            }
        });
    }


    private void updateTextViewContent() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                updateHandler = new UpdateHandler();
                Message message = new Message();
                message.what = 1;

                updateHandler.sendMessage(message);
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //参数为null,表示移除所有回调和消息
        updateHandler.removeCallbacksAndMessages(null);
    }*/


    /*
    * 1.不能在子线程更新UI；
    * 2.Handler使用不当可能引起的内存泄露；
    * 3.Message的优化！
    * 4.在子线程中创建Handler，需要为这个Handler准备Looper。
    * 5.可能出现异常：在Handler把消息处理完了，但是页面销毁了，这是可能就会出现各种异常；
    * */


    //优化后

    private  TextView mTextMessage;
    private Button button;

    private UpdateHandler updateHandler;

    static class UpdateHandler extends Handler {
        private final WeakReference<HandlerActivity> mActivity;

        public UpdateHandler(HandlerActivity activity) {
            mActivity = new WeakReference<HandlerActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            HandlerActivity instance = mActivity.get();

            if (msg.what == 1) {
                instance.mTextMessage.setText("update====");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);

        updateHandler = new UpdateHandler(HandlerActivity.this);

        button = (Button) findViewById(R.id.btn_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTextViewContent();
            }
        });
    }

    private void updateTextViewContent() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message message = Message.obtain();
                message.what = 1;
                updateHandler.sendMessage(message);
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateHandler.removeCallbacksAndMessages(null);
    }
}
