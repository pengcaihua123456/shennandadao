package com.example.myapplication.idehandle;

public class DispatchRunnable implements Runnable {


    private Task task;

    public DispatchRunnable(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        this.task.run();
    }
}
