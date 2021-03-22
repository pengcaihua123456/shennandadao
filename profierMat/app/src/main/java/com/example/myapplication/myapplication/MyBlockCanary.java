package com.example.myapplication.myapplication;

import android.util.Log;
import android.view.Choreographer;


/***
 * 卡顿检测
 * Android系统每隔16ms发出VSYNC信号，触发对UI进行渲染。SDK中包含了一个相关类，以及相关回调。理论上来说两次回调的时间周期应该在16ms，
 * 如果超过了16ms我们则认为发生了卡顿，我们主要就是利用两次回调间的时间周期来判断：
 */
public class MyBlockCanary {

    private static void detect() {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                //检测2次回来的时间
                //第一次的时候开始检测，如果大于阈值则输出相关堆栈信息，否则则移除。
                Log.d("MyBlockCanary","doFrame");
                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }
}
