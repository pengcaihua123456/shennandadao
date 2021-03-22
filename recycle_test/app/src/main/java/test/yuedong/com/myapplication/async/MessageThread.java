package test.yuedong.com.myapplication.async;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;

public abstract class MessageThread extends Thread {
    private Looper m_looper;
    private Handler m_handler;
    private ArrayList<Message> m_waiting_msgs = new ArrayList<Message>();

    public void close() {
        if (null != m_looper) {
            m_looper.quit();
            m_looper = null;
        }
        if (m_handler != null) {
            m_handler.removeCallbacksAndMessages(null);
            m_handler = null;
        }
    }

    protected void sendMessage(Message msg) {
        if (null == m_handler) {
            synchronized (this) {
                if (null == m_handler) {
                    m_waiting_msgs.add(msg);
                    return;
                }
            }
        }
        m_handler.sendMessage(msg);
    }

    protected abstract void handleMessage(Message msg);

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {
        public void handleMessage(Message msg) {
            MessageThread.this.handleMessage(msg);
        }
    }

    @Override
    public void run() {
        if (Looper.myLooper() == null){
            Looper.prepare();
        }
        m_looper = Looper.myLooper();

        Handler handler = new MyHandler();
        synchronized (this) {
            for (Message msg : m_waiting_msgs) {
                handler.sendMessage(msg);
            }
            m_handler = handler;
            m_waiting_msgs.clear();
            m_waiting_msgs = null;
        }

        Looper.loop();

    }
}
