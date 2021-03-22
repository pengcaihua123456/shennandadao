package com.example.mvvm;

import android.util.Log;

public class Account {

    public String name="pengcaihua";
    public int level;

    public String getName() {
        Log.d("peng", "name:" + name);
        return name;
    }
}
