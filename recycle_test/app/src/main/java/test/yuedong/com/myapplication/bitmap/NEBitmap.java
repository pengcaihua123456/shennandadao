package test.yuedong.com.myapplication.bitmap;

import android.graphics.Bitmap;

import test.yuedong.com.myapplication.ImageUtil;


/**
 * Created with IntelliJ IDEA.
 * User: virl
 * Date: 14-4-25
 * Time: 上午10:44
 * Email: nervending@gmail.com
 */
public class NEBitmap {
    public enum Type {
        kRes,
        kCommon,
    }

    Object key;

    NEBitmap(Type type, Bitmap bitmap, int resId) {
        super();
        count = 1;
        this.type = type;
        this.bitmap = bitmap;
        this.resId = resId;
    }

    public NEBitmap(Bitmap bitmap) {
        super();
        count = 1;
        type = Type.kCommon;
        this.bitmap = bitmap;
    }

    public Bitmap bitmap() {
        return bitmap;
    }

    public boolean released() {
        return count <= 0;
    }

    public NEBitmap retain() {
        ++count;
        return this;
    }

    public void release() {
        if(null!=l) {
            --count;
            l.onReleaseBitmap(this);
            return;
        }
        if(type == Type.kCommon) {
            --count;
            if(count == 0) {
                if(bitmap!=null) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }
            return;
        }
    }

    Type type;
    int count;
    Integer resId;
    Bitmap bitmap;

    private SDBitmapReleaseListener l;
    void setReleaseListener(SDBitmapReleaseListener l) {
        this.l = l;
    }

    interface SDBitmapReleaseListener {
        void onReleaseBitmap(NEBitmap bitmap);
    }

    public NEBitmap rotateAndScale(int degress, int maxSideLen) {
        Bitmap newBmp = ImageUtil.rotateAndScale(bitmap, degress, maxSideLen, false);
        if(newBmp == bitmap) {
            return retain();
        }
        return new NEBitmap(newBmp);
    }

    public NEBitmap scale(int maxSideLen) {
        return rotateAndScale(0, maxSideLen);
    }

    public NEBitmap toSquare(int width) {
        return new NEBitmap(ImageUtil.getBitmapRectangleThumbnail(bitmap, width));
    }


    public NEBitmap rotate(int degress) {
        if(degress == 0) {
            return this.retain();
        }
        return new NEBitmap(ImageUtil.rotateNotRecycleOriginal(bitmap, degress));
    }

    public NEBitmap roundCorner(int roundPiexls) {
        return new NEBitmap(ImageUtil.toRoundCorner(bitmap, roundPiexls, false));
    }

    public NEBitmap toRound() {
        return new NEBitmap(ImageUtil.toRoundBitmap(bitmap, false));
    }
}
