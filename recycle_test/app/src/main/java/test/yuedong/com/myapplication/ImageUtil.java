/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.yuedong.com.myapplication;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

/**
 * Collection of utility functions used in this package.
 */
public class ImageUtil {
    private static final String TAG = "ImageUtil";
    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_UP = 2;
    public static final int DIRECTION_DOWN = 3;

    private ImageUtil() {
    }

    public static Bitmap decodeFileUnthrow(String pathName, Options opts) {
        try {
            return BitmapFactory.decodeFile(pathName, opts);
        } catch (Throwable e) {
            LogEx.e(e.toString());
        }

        return null;
    }

    public static Bitmap decodeByteArrayUnthrow(byte[] data, Options opts) {
        try {
            return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        } catch (Throwable e) {
            LogEx.e(e.toString());
        }

        return null;
    }

    public static Bitmap loadImage565(String path, int maxSideLen) {
        if (null == path) {
            return null;
        }

        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int degree = getPictureDegree(path);
        options.inJustDecodeBounds = false;
        options.inSampleSize = Math.max(options.outWidth / maxSideLen, options.outHeight / maxSideLen);
        if (options.inSampleSize < 1) {
            options.inSampleSize = 1;
        }
        options.inPreferredConfig = Config.RGB_565;
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            Bitmap newBmp = ImageUtil.rotateAndScale(bitmap, degree, maxSideLen);
            if (newBmp != bitmap) {
                bitmap.recycle();
            }
            return newBmp;
        } catch (Throwable e) {
            LogEx.e(e.toString());
        }

        return null;
    }

    public static Bitmap loadImage(String path, int maxSideLen) {

        if (null == path) {
            return null;
        }

        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int degree = getPictureDegree(path);
        options.inJustDecodeBounds = false;
        options.inSampleSize = Math.max(options.outWidth / maxSideLen, options.outHeight / maxSideLen);
        if (options.inSampleSize < 1) {
            options.inSampleSize = 1;
        }
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            Bitmap newBmp = ImageUtil.rotateAndScale(bitmap, degree, maxSideLen);
            if (newBmp != bitmap) {
                bitmap.recycle();
            }
            return newBmp;
        } catch (Throwable e) {
            LogEx.e(e.toString());
        }

        return null;
    }

    public static int getPictureDegree(String path) {
        try {
            ExifInterface exif = new ExifInterface(path);
            return getExifDegree(exif);
        } catch (Throwable e) {
        }

        return 0;
    }

    /**
     * 修复图片旋转
     */
    public static void fixPictureOrientation(String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
            exifInterface.saveAttributes();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static int getExifDegree(ExifInterface exif) {
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
        if (orientation != -1) {
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }
        }

        return 0;
    }

    private static final long kYearMillis = 365L * 86400 * 1000;
    private static final SimpleDateFormat sExifDateFormater = new SimpleDateFormat("yyyyMMddkkmmss");

    public static long getExifDateMillis(ExifInterface exif) {
        String dateStr = exif.getAttribute(ExifInterface.TAG_DATETIME);
        if (TextUtils.isEmpty(dateStr)) {
            return 0;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dateStr.length(); i++) {
            char ch = dateStr.charAt(i);
            if (ch >= '0' && ch <= '9') {
                sb.append(ch);
            }
        }

        String formatDateStr = sb.toString();
        if (formatDateStr.length() != 14) {
            LogEx.w("unrecognized exif date: " + dateStr);
            return 0;
        }

        long millis = StrUtil.getTimeMillis(sExifDateFormater, formatDateStr);
        long curMillis = System.currentTimeMillis();
        if (millis <= 0 || millis > curMillis || curMillis > millis + 10 * kYearMillis) {
            LogEx.w("invalid exif date: " + dateStr);
            return 0;
        }

        return millis;
    }

    /**
     * 正方形bitmap 裁剪成圆形bitmap
     * */
    public static Bitmap createCircleBitmap(Bitmap source, int width) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        int sWidth = source.getWidth();
        int sHeight = source.getHeight();
        if (sWidth != 0 && sHeight != 0) {
            float scaleX = (float) width / sWidth;
            float scaleY = (float) width / sHeight;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleX, scaleY);
            canvas.drawBitmap(source, matrix, paint);
        }
        return target;
    }

    public static boolean compressPicture(File srcFile, File destFile, int quality, int maxSideLength) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        decodeFileUnthrow(srcFile.getPath(), options);
        LogEx.v("src width: " + options.outWidth + ", height: " + options.outHeight);

        options.inJustDecodeBounds = false;
        options.inSampleSize = Math.max(options.outWidth / maxSideLength, options.outHeight / maxSideLength);
        LogEx.v("inSampleSize: " + options.inSampleSize);

        Bitmap bmp = decodeFileUnthrow(srcFile.getPath(), options);
        if (null == bmp) {
            LogEx.w("decodeFile failed");
            return false;
        }

        bmp = rotateAndScale(bmp, getPictureDegree(srcFile.getPath()), maxSideLength);

        boolean ret = ImageUtil.saveBitmap2file(bmp, destFile, CompressFormat.JPEG, quality);
        LogEx.i("dst width: " + bmp.getWidth() + ", height: " + bmp.getHeight() + ", size: " + destFile.length());
        bmp.recycle();

        return ret;
    }

    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels, boolean releaseSrc) {

        int size = bitmap.getWidth() < bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(size,
                size, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffFFFFFF;
        final Paint paint = new Paint();
        final Rect rect = new Rect();
        rect.left = (bitmap.getWidth() - size) / 2;
        rect.right = rect.left + size;
        rect.top = (bitmap.getHeight() - size) / 2;
        rect.bottom = rect.top + size;
        final RectF rectF = new RectF(0, 0, size, size);
        final float roundPx = pixels;

        try{
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rectF, paint);
        }catch (Throwable e){e.printStackTrace();}

        if (releaseSrc) {
            bitmap.recycle();
        }

        return output;
    }

    public static Bitmap createRoundRecFImage(Bitmap bitmap, float round, boolean recycle) {
        if (null == bitmap) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        canvas.drawRoundRect(rectF, round, round, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rect, paint);
        if (recycle) {
            bitmap.recycle();
        }
        return target;
    }

    public static Bitmap getPhotoThumbnail(Bitmap bmp, int expectWidth, int expectHeight) {

        if (null == bmp) {
            return null;
        }

        float wRatio = 1.0f * expectWidth / bmp.getWidth();
        float hRatio = 1.0f * expectHeight / bmp.getHeight();
        Options options = new Options();
        options.inJustDecodeBounds = true;
        options.inJustDecodeBounds = false;

        Matrix m = new Matrix();

        Bitmap bitmap = null;
        try{
            if (1 > wRatio || 1 > hRatio) {
                if (wRatio < hRatio) {
                    m.postScale(wRatio, wRatio);
                } else {
                    m.postScale(hRatio, hRatio);
                }
                bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, false);
            } else {
                bitmap = bmp;
            }
        }catch (Throwable e){e.printStackTrace();}
        return bitmap;
    }

    public static Bitmap getPhotoFileThumbnail(String filePath, int expectWidth, int expectHeight) {

        if (null == filePath) {
            return null;
        }

        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inJustDecodeBounds = false;
        int wRatio = options.outWidth / expectWidth;
        int hRatio = options.outHeight / expectHeight;

        if (1 < wRatio || 1 < hRatio) {
            if (wRatio > hRatio) {
                options.inSampleSize = wRatio;
            } else {
                options.inSampleSize = hRatio;
            }
        }

        InputStream inStream;
        try {
            inStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(inStream, new Rect(-1, -1, -1, -1), options);
            inStream.close();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }

        return rotate(bmp, getPictureDegree(filePath));
    }

    public static Bitmap getPhotoFileRectangleThumbnail(FileDescriptor fd, float thumbnailSize) {

        if (null == fd) {
            return null;
        }

        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);

        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) Math.max(options.outWidth / thumbnailSize, options.outHeight / thumbnailSize);
        Bitmap bmp = BitmapFactory.decodeFileDescriptor(fd, null, options);
        if (null == bmp)
            return null;

        Bitmap thumbnail = null;
        try {
            thumbnail = getBitmapRectangleThumbnail(bmp, thumbnailSize);
            if (thumbnail != bmp) {
                bmp.recycle();
            }
        }catch (Throwable e){
            e.printStackTrace();
        }

        return thumbnail;
    }

    public static Bitmap getPhotoFileRectangleThumbnail(String filePath, float thumbnailSize) {

        if (null == filePath) {
            return null;
        }

        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) Math.max(options.outWidth / thumbnailSize, options.outHeight / thumbnailSize);
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        if (null == bmp)
            return null;

        Bitmap thumbnail = getBitmapRectangleThumbnail(bmp, thumbnailSize);
        if (thumbnail != bmp) {
            bmp.recycle();
        }

        return rotate(thumbnail, getPictureDegree(filePath));
    }

    public static Bitmap getBitmapRectangleThumbnail(Bitmap bmp, float thumbnailSize) {
        if (null == bmp){
            return null;
        }
        int x = 0;
        int y = 0;
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        if (width > height) {
            x = (width - height) / 2;
            width = height;
        } else {
            y = (height - width) / 2;
            height = width;
        }

        Bitmap bitmap = null;

        try{
            float scale = thumbnailSize / width;
            if (scale < 1.0) {
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                bitmap = Bitmap.createBitmap(bmp, x, y, width, height, matrix, true);
            }

            bitmap = Bitmap.createBitmap(bmp, x, y, width, height);
        }catch (Throwable e){e.printStackTrace();}
        return bitmap;
    }

    public static boolean saveBitmap2file(Bitmap bmp, File file, CompressFormat format, int quality) {
        if(bmp == null) {
            return false;
        }
        boolean bRet = false;
        OutputStream stream = null;
        try{
            if (file.isFile())
                file.delete();
            try {
                stream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                LogEx.w(e.toString());
                return false;
            }

            bRet = bmp.compress(format, quality, stream);
        }catch (Throwable e){
            e.printStackTrace();
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stream = null;
            }
        }
        return bRet;
    }

    // Rotates the bitmap by the specified degree.
    // If a new bitmap is created, the original bitmap is recycled.
    public static Bitmap rotate(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees);
            try {
                Bitmap b2 = Bitmap.createBitmap(
                        b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (null != b2 && b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (Throwable ex) {
                // We have no memory to rotate. Return the original bitmap.
            }
        }
        return b;
    }

    public static Bitmap rotateNotRecycleOriginal(Bitmap b, int degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees);
            try {
                return Bitmap.createBitmap(
                        b, 0, 0, b.getWidth(), b.getHeight(), m, true);
            } catch (Throwable ex) {
                // We have no memory to rotate. Return the original bitmap.
            }
        }
        return b;
    }

    public static Bitmap rotateAndScale(Bitmap b, int degrees, float maxSideLen) {

        return rotateAndScale(b, degrees, maxSideLen, true);
    }

    public static Bitmap rotateAndScale(Bitmap b, int degrees, float maxSideLen, boolean recycle) {

        if (null == b || degrees == 0 && b.getWidth() <= maxSideLen + 10 && b.getHeight() <= maxSideLen + 10) {
            return b;
        }

        Matrix m = new Matrix();
        if (degrees != 0) {
            m.setRotate(degrees);
        }

        float scale = Math.min(maxSideLen / b.getWidth(), maxSideLen / b.getHeight());
        if (scale < 1) {
            m.postScale(scale, scale);
        }
        LogEx.i("degrees: " + degrees + ", scale: " + scale);

        try {
            Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
            if (null != b2 && b != b2) {
                if (recycle) {
                    b.recycle();
                }
                b = b2;
            }
        } catch (Throwable e) {
        }

        return b;
    }

    // Whether we should recycle the input (unless the output is the input).
    public static final boolean RECYCLE_INPUT = true;
    public static final boolean NO_RECYCLE_INPUT = false;

    public static Bitmap transform(Matrix scaler,
                                   Bitmap source,
                                   int targetWidth,
                                   int targetHeight,
                                   boolean scaleUp,
                                   boolean recycle) {
        int deltaX = source.getWidth() - targetWidth;
        int deltaY = source.getHeight() - targetHeight;
        if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
            /*
			 * In this case the bitmap is smaller, at least in one dimension,
             * than the target.  Transform it by placing as much of the image
             * as possible into the target and leaving the top/bottom or
             * left/right (or both) black.
             */
            Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight,
                    Config.ARGB_8888);
            Canvas c = new Canvas(b2);

            int deltaXHalf = Math.max(0, deltaX / 2);
            int deltaYHalf = Math.max(0, deltaY / 2);
            Rect src = new Rect(
                    deltaXHalf,
                    deltaYHalf,
                    deltaXHalf + Math.min(targetWidth, source.getWidth()),
                    deltaYHalf + Math.min(targetHeight, source.getHeight()));
            int dstX = (targetWidth - src.width()) / 2;
            int dstY = (targetHeight - src.height()) / 2;
            try{
                Rect dst = new Rect(
                        dstX,
                        dstY,
                        targetWidth - dstX,
                        targetHeight - dstY);
                c.drawBitmap(source, src, dst, null);
            }catch (Throwable e){e.printStackTrace();}
            if (recycle) {
                source.recycle();
            }
            return b2;
        }
        float bitmapWidthF = source.getWidth();
        float bitmapHeightF = source.getHeight();

        float bitmapAspect = bitmapWidthF / bitmapHeightF;
        float viewAspect = (float) targetWidth / targetHeight;

        if (bitmapAspect > viewAspect) {
            float scale = targetHeight / bitmapHeightF;
            if (scale < .9F || scale > 1F) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        } else {
            float scale = targetWidth / bitmapWidthF;
            if (scale < .9F || scale > 1F) {
                scaler.setScale(scale, scale);
            } else {
                scaler = null;
            }
        }

        Bitmap b1;
        if (scaler != null) {
            // this is used for minithumb and crop, so we want to filter here.
            b1 = Bitmap.createBitmap(source, 0, 0,
                    source.getWidth(), source.getHeight(), scaler, true);
        } else {
            b1 = source;
        }

        if (recycle && b1 != source) {
            source.recycle();
        }

        int dx1 = Math.max(0, b1.getWidth() - targetWidth);
        int dy1 = Math.max(0, b1.getHeight() - targetHeight);

        Bitmap b2 = Bitmap.createBitmap(
                b1,
                dx1 / 2,
                dy1 / 2,
                targetWidth,
                targetHeight);

        if (b2 != b1) {
            if (recycle || b1 != source) {
                b1.recycle();
            }
        }

        return b2;
    }

    public static void setExifRotation(String path, int degrees) {

        try {

            degrees %= 360;
            if (degrees < 0) degrees += 360;

            int orientation = ExifInterface.ORIENTATION_NORMAL;
            switch (degrees) {
                case 0:
                    orientation = ExifInterface.ORIENTATION_NORMAL;
                    break;
                case 90:
                    orientation = ExifInterface.ORIENTATION_ROTATE_90;
                    break;
                case 180:
                    orientation = ExifInterface.ORIENTATION_ROTATE_180;
                    break;
                case 270:
                    orientation = ExifInterface.ORIENTATION_ROTATE_270;
                    break;
            }

            ExifInterface exif = new ExifInterface(path);
            exif.setAttribute(ExifInterface.TAG_ORIENTATION, Integer.toString(orientation));
            exif.saveAttributes();

        } catch (Exception ex) {
            LogEx.e("unable to save exif data with new orientation, path: " + path);
        }
    }

    public static Bitmap toRoundBitmap(Bitmap bitmap, boolean recycle) {
        if (null == bitmap) {
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width,
                height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        try{
            paint.setAntiAlias(true);

            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, src, dst, paint);
        }catch (Throwable e){e.printStackTrace();}

        if (recycle) {
            bitmap.recycle();
        }
        return output;
    }

    public static String getPicturePathFromUri(Uri uri, ContentResolver contentResolver) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = contentResolver.query(uri, filePathColumn, null, null, null);
        if (null == cursor) {
            return null;
        }

        String picturePath = null;
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
        }
        cursor.close();

        return picturePath;
    }

    private static final int kThumbnailSize = 100;
    public static byte[] bmpToByteArrayByMaxSize(final Bitmap bmp, final boolean needRecycle, int maxSizeK) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        if (null != bmp){
            if(bmp.getWidth() > kThumbnailSize || bmp.getHeight() > kThumbnailSize) {
                Bitmap smallBmp = ImageUtil.getPhotoThumbnail(bmp, kThumbnailSize, kThumbnailSize);
                bmpCompressToOSByMaxSize(smallBmp, Bitmap.CompressFormat.JPEG, 75, output, maxSizeK);
            } else {
                bmpCompressToOSByMaxSize(bmp, Bitmap.CompressFormat.JPEG, 75, output, maxSizeK);
            }
            if (needRecycle) {
                bmp.recycle();
            }
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void bmpCompressToOSByMaxSize(Bitmap bitmap , Bitmap.CompressFormat format, int quality,
                                                ByteArrayOutputStream outputStream, int maxSizeK){
        //微信缩略图最大支持32kb
        if (null != bitmap && null != outputStream){
            if(bitmap.compress(format , quality , outputStream)){
                while (outputStream.toByteArray().length >> 10 > maxSizeK && quality > 1){
                    outputStream.reset();
                    quality -= 1;
                    if (!bitmap.compress(format , quality , outputStream)){
                        break;
                    }
                }
            }
        }
    }
}
