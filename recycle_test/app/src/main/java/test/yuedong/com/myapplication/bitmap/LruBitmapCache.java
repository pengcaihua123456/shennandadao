package test.yuedong.com.myapplication.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: virl
 * Date: 14-5-30
 * Time: 上午12:43
 * Email: nervending@gmail.com
 */
public class LruBitmapCache <T> implements NEBitmap.SDBitmapReleaseListener{
    private HashMap<T, NEBitmap> thumbnailSDBitmapHashMap;
    private LinkedList<NEBitmap> thumbnailSubstitutionQueue;

    private int cacheSize;
    private BitmapFactory.Options _options;
    public LruBitmapCache(int cacheSize) {
        this._config = Bitmap.Config.RGB_565;
        _options = new BitmapFactory.Options();
        _options.inPreferredConfig = _config;

        this.cacheSize = cacheSize;
        thumbnailSubstitutionQueue = new LinkedList<NEBitmap>();
        thumbnailSDBitmapHashMap = new HashMap<T, NEBitmap>();
    }

    private Bitmap.Config _config;
    public void setLoadBmpConfig(Bitmap.Config config) {
        _config = config;
    }

    public void put(T key, NEBitmap bmp) {
        thumbnailSDBitmapHashMap.put(key, bmp);
        bmp.key = key;
        bmp.setReleaseListener(this);
    }

//    public NEBitmap put(T key, String path) {
//        _options.inPreferredConfig = _config;
//        Bitmap bmp = BitmapFactory.decodeFile(path, _options);
//        NEBitmap neBitmap = new NEBitmap(bmp);
//        put(key, neBitmap);
//        return neBitmap;
//    }

//    public NEBitmap get(T key, String path) {
//        NEBitmap bmp = get(key);
//        return bmp;
//    }

    public boolean removeByKey(T key, boolean release) {
        NEBitmap bitmap = thumbnailSDBitmapHashMap.remove(key);
        if(bitmap!=null && release) {
            bitmap.bitmap.recycle();
            return true;
        }
        return false;
    }

    public NEBitmap get(T key) {
        NEBitmap bmp = thumbnailSDBitmapHashMap.get(key);
        if(null == bmp) {
            return null;
        }
        if(bmp.count == 0) {
            thumbnailSubstitutionQueue.remove(bmp);
        }
        return bmp;
    }

    private static String kTag = "bmpCache";
    public void printStatus() {
//        Log.d(kTag, "mp:" + thumbnailSDBitmapHashMap.size() + ", queue:" + thumbnailSubstitutionQueue.size());
    }

    @Override
    public void onReleaseBitmap(NEBitmap bitmap) {
        if(!thumbnailSDBitmapHashMap.containsKey(bitmap.key)) {
            return;
        }
        if(bitmap.count == 0) {
            thumbnailSubstitutionQueue.add(bitmap);
        }

        tryReleaseBmp();
        printStatus();
    }

    private void tryReleaseBmp() {
        while (thumbnailSubstitutionQueue.size() >= cacheSize) {
            NEBitmap bmp = thumbnailSubstitutionQueue.removeFirst();
            thumbnailSDBitmapHashMap.remove(bmp.key);
            bmp.bitmap().recycle();
        }
    }

    public void clear() {
        Collection<NEBitmap> bmps = thumbnailSDBitmapHashMap.values();
        for(NEBitmap bmp : bmps) {
            bmp.bitmap().recycle();
        }
        thumbnailSDBitmapHashMap.clear();
        thumbnailSubstitutionQueue.clear();
    }

}
