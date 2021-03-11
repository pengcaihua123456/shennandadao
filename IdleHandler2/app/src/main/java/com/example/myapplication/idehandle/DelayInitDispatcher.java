package com.example.myapplication.idehandle;

import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

public class DelayInitDispatcher {

    private Queue<Task> mDelayTasks = new LinkedList<>();

    private MessageQueue.IdleHandler mIdleHandler = new MessageQueue.IdleHandler() {
        @Override
        public boolean queueIdle() {
            if(mDelayTasks.size()>0){
                Log.d("SNDD","queueIdle");
                Task task = mDelayTasks.poll();
                new DispatchRunnable(task).run();
            }
            return !mDelayTasks.isEmpty();
        }
    };

    public DelayInitDispatcher addTask(Task task){
        mDelayTasks.add(task);
        return this;
    }

    public void start(){
        Log.d("SNDD","DelayInitDispatcher start");
        Looper.myQueue().addIdleHandler(mIdleHandler);
    }

}
