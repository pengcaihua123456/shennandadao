package test.yuedong.com.myapplication.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import test.yuedong.com.myapplication.async.MessageThread;


public class AsyncImageLoader extends MessageThread {

    private static AsyncImageLoader sInstance;

    public static AsyncImageLoader instance() {
//        if(sInstance == null) {
//            sInstance = new AsyncImageLoader();
//            sInstance.start();
//        }
        if (sInstance == null) {
            synchronized (AsyncImageLoader.class) {
                if (sInstance == null) {
                    sInstance = new AsyncImageLoader();
                    sInstance.start();
                }
            }
        }
        return sInstance;
    }

    public interface Listener {
        void imageLoadFinish(Object tag, Bitmap bitmap);
    }

    public void loadImage(Resources resources, int id, Object tag, Listener listener) {
        Info info = new Info();
        info.tag = tag;
        info.listener = listener;
        info.resources = resources;
        info.resId = id;

        Message message = Message.obtain();
        message.obj = info;

        sendMessage(message);
    }

    public void loadImage(String path, Object tag, IImageLoader loader, Listener listener) {
        if (null == path || null == listener) {
            return;
        }

        Info info = new Info();
        info.path = path;
        info.tag = tag;
        info.listener = listener;
        info.loader = loader;

        Message message = Message.obtain();
        message.obj = info;

        sendMessage(message);
    }

    private static class Info {
        String path = null;
        Object tag;
        Bitmap bitmap;
        Listener listener;
        IImageLoader loader;
        Resources resources;
        int resId;
    }

    private static class CallbackHandler extends Handler {

        public void handleMessage(Message msg) {
            Info info = (Info) msg.obj;
            info.listener.imageLoadFinish(info.tag, info.bitmap);
            info.bitmap = null;
        }
    }

    private final CallbackHandler callbackHandler = new CallbackHandler();

    @Override
    protected void handleMessage(Message msg) {
        Info info = (Info) msg.obj;
        try {
            if (info.resources != null) {
                info.bitmap = BitmapFactory.decodeResource(info.resources, info.resId);
            } else {
                if (info.loader == null) {
                    info.bitmap = BitmapFactory.decodeFile(info.path);
                } else {
                    info.bitmap = info.loader.loadBitmap(info.path);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Message message = Message.obtain();
        message.obj = info;
        callbackHandler.sendMessage(message);
    }

}
