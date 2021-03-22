package test.yuedong.com.myapplication.bitmap;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: virl
 * Date: 14-4-25
 * Time: 上午10:21
 * Email: nervending@gmail.com
 */
public class ResBitmapCache implements NEBitmap.SDBitmapReleaseListener{

    static ResBitmapCache sInstance;
    public static ResBitmapCache instance(){
        if(sInstance == null) {
            sInstance = new ResBitmapCache();
        }
        return sInstance;
    }

    public static void release() {
        if(sInstance != null) {
            sInstance = null;
        }
    }

    public void logCount() {
//        LogEx.d("log res count begin");
//        for(Integer key: resSDBitmapHashMap.keySet()) {
//            NEBitmap bit = resSDBitmapHashMap.get(key);
//            LogEx.d(StrUtil.linkObjects("id:", String.format("%x", key), ", count:", bit.count));
//        }
//        LogEx.d("log res count end");
    }

    private HashMap<Integer, NEBitmap> resSDBitmapHashMap;

    private ResBitmapCache() {
        resSDBitmapHashMap = new HashMap<Integer, NEBitmap>();
    }

    public NEBitmap loadRes(Resources res, Integer resId) {
        NEBitmap bit = resSDBitmapHashMap.get(resId);
        if(bit == null) {
            bit = new NEBitmap(NEBitmap.Type.kRes, BitmapFactory.decodeResource(res, resId), resId);
            bit.setReleaseListener(this);
            resSDBitmapHashMap.put(resId, bit);
            return bit;
        }
        return bit.retain();
    }

    public NEBitmap findRes(Integer resId) {
        return resSDBitmapHashMap.get(resId);
    }

    public void put(Integer resId, NEBitmap bitmap) {
        resSDBitmapHashMap.put(resId, bitmap);
        bitmap.setReleaseListener(this);
    }

    public void forceReleaseAll() {
        for(NEBitmap bitmap: resSDBitmapHashMap.values()) {
            bitmap.bitmap().recycle();
        }
        resSDBitmapHashMap.clear();
    }

    public void releaseUnUsedBitmap() {
        HashSet<Integer> removed = new HashSet<>();
        for(Integer key: resSDBitmapHashMap.keySet()) {
            NEBitmap bit = resSDBitmapHashMap.get(key);
            if(bit.count <= 0) {
                removed.add(key);
                bit.bitmap().recycle();
            }
        }
        for(Integer key: removed) {
            resSDBitmapHashMap.remove(key);
        }
    }

    @Override
    public void onReleaseBitmap(NEBitmap bitmap) {
        if(bitmap.count < 0) {
        }
    }
}
