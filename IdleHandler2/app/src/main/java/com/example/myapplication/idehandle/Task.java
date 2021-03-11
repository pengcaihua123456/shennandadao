package com.example.myapplication.idehandle;

import android.util.Log;

import java.util.Random;

public class Task implements Runnable{

    public void doWork(){

        int number = new Random().nextInt(7) + 1;

        try {
            Thread.sleep(1000*number);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("SNDD","doWork");
    }

    @Override
    public void run() {
        Log.d("SNDD","run  doWork");
        doWork();
    }
}
