package com.example.myapplication.myapplication;

import android.util.Log;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class MyReference {

    public void weakRefereceTest() {

        ReferenceQueue referenceQueue = new ReferenceQueue<Object>();
        Object object = new Object();
        WeakReference<Object> weakReference = new WeakReference<Object>(object, referenceQueue);

        Log.d("MyReference", "weakReference" + weakReference);
//        object = null;

        Runtime.getRuntime().gc();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //从队列中取弱引用，如果不为空，说明有回收了,手动让object=null;
        //如果为空，说明没有什么可以回收的
        WeakReference<Object> weakReference1 = (WeakReference) referenceQueue.poll();
        Log.d("MyReference", "after weakReference1" + weakReference1);


        Object object1 = null;

        if (referenceQueue.poll() != null) {
            object1 = referenceQueue.poll().get();
        }

        Log.d("MyReference", "after object1" + object1);


    }


}
