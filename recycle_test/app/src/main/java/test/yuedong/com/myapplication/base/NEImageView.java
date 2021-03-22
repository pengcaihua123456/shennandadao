package test.yuedong.com.myapplication.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import test.yuedong.com.myapplication.R;
import test.yuedong.com.myapplication.bitmap.NEBitmap;
import test.yuedong.com.myapplication.bitmap.ResBitmapCache;


/**
 * Created by virl on 15/6/2.
 */
public class NEImageView extends ImageView implements ReleaseAble{
    public NEImageView(Context context) {
        super(context);
        init(context, null);
    }
    public NEImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public NEImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NEImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NEImageView);
        try{
            if(a.hasValue(R.styleable.NEImageView_res_image)) {
                NEBitmap bitmap = new NEBitmap(BitmapFactory.decodeResource(getResources(), a.getResourceId(R.styleable.NEImageView_res_image, 0)));
                setNeBitmap(bitmap);
            } else if(a.hasValue(R.styleable.NEImageView_cache_res_image)) {
                int res = a.getResourceId(R.styleable.NEImageView_res_image, 0);
                NEBitmap bitmap = ResBitmapCache.instance().findRes(res);
                if(bitmap == null) {
                    bitmap = new NEBitmap(BitmapFactory.decodeResource(getResources(), res));
                    ResBitmapCache.instance().put(res, bitmap);
                }
                setNeBitmap(bitmap);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
        a.recycle();
    }

    protected NEBitmap neBitmap;

    public void setSrcCacheRes(int resId) {
        NEBitmap bitmap = ResBitmapCache.instance().loadRes(getResources(), resId);
        setNeBitmap(bitmap);
    }

    public void setResAsNeBitmap(int resId) {
        try {
            NEBitmap bitmap = new NEBitmap(BitmapFactory.decodeResource(getResources(), resId));
            setNeBitmap(bitmap);
        } catch (Throwable e){}
    }

    public void setNeBitmap(NEBitmap bitmap) {
        release();
        neBitmap = bitmap;
        if(neBitmap!=null) {
            setImageBitmap(bitmap.bitmap());
        }
    }

    public NEBitmap getNeBitmap() {
        return neBitmap;
    }

    @Override
    public void release() {
        if(neBitmap!=null) {
            setImageBitmap(null);
            neBitmap.release();
            neBitmap = null;
        }
    }
}
