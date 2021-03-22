package accd.health.yuedong.com.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //PathClassLoader
        ClassLoader PathClassLoader = this.getClassLoader();

        //BootClassLoader
        ClassLoader bootClassLoader = Activity.class.getClassLoader();


        Log.d(TAG,"PathClassLoader getClassLoader:" + PathClassLoader);
        Log.d(TAG,"PathClassLoader getClassLoader 的父亲 :" + PathClassLoader.getParent());
        Log.d(TAG,"BootClassLoader Activity.class :" + bootClassLoader);

    }
}
