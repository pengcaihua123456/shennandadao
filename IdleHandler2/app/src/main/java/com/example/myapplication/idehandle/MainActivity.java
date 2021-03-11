package com.example.myapplication.idehandle;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="SNDD";

    DelayInitDispatcher delayInitDispatcher = new DelayInitDispatcher();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        delayInitDispatcher.addTask(new Task() {
            @Override
            public void run() {
                Log.i(TAG, "run: delay task begin");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "run: delay task end");
            }
        });
        delayInitDispatcher.start();
    }
}