package top.daxianwill.greendaodemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

public class                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        SetttingDBHelperOrgin extends SQLiteOpenHelper {
//	TODO 需要更新新的 这个效率太差

    public final static String name = "setting";

    private final static String tableName = "expandSetting";

    private final static int version = 3;

    private SetttingDBHelperOrgin(Context context) {
        super(context, name, null, version);
    }

    private static Object object = new Object();
    private static SetttingDBHelperOrgin dbHelper = null;

    public static SetttingDBHelperOrgin getIntance(Context context) {
        if (null == dbHelper) {
//			synchronized (object) {
//				if (null == dbHelper) {
            dbHelper = new SetttingDBHelperOrgin(context);
//				}
//			}
        }
        return dbHelper;
    }

    private SQLiteDatabase mDefaultWritableDatabase = null;
    private SQLiteDatabase mDefaultReadableDatabase = null;


    private final static String USER_ID = "user_id";
    private final static String ISLOGIN = "islogin";
    private final static String USER_NAME = "user_name";
    private final static String USER_RANK = "user_rank";
    private final static String USER_PASSWORD = "user_password";
    private final static String USER_PHONE = "user_phone";
    private final static String HX_USER = "hx_phone"; //0表示未知, 1表示非环信用户 , 2表示环信用户
    private final static String KEY = "key";
    private final static String VALUE = "value";

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String tableName = name;
        final String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " ( " + USER_ID + " INTEGER NOT NULL DEFAULT '-1', "
                + ISLOGIN + " INTEGER NOT NULL DEFAULT '0', " + USER_RANK + " INTEGER NOT NULL  DEFAULT '0', " + HX_USER + " INTEGER NOT NULL  DEFAULT '0', " + USER_NAME + " TEXT, " +
                USER_PASSWORD + " TEXT, " + USER_PHONE + " TEXT );";
        db.execSQL(sql);
    }

    private boolean hasCheck = false;

    public void checkTable() {
        try {
            if (!hasCheck) {
                final String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " ( "
                        + KEY + " TEXT, " + VALUE + " TEXT );";
                this.getWritableDatabase().execSQL(sql);
                hasCheck = true;
            }
        } catch (Throwable e) {

        }
    }

    public void saveKeyValue(String key, String value) {
        checkTable();
        Cursor cursor = null;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY, key);
            contentValues.put(VALUE, value);
            cursor = getReadableDatabase().rawQuery("select * from " + tableName + " where " + KEY + " = \"" + key + "\";", null);
            if (cursor != null && cursor.getCount() > 0) {
                getWritableDatabase().update(tableName, contentValues, KEY + " = ? ", new String[]{key});
            } else {
                getWritableDatabase().insert(tableName, null, contentValues);
            }
            /*getWritableDatabase().replace(tableName , null , contentValues);*///replace在荣耀6上不生效

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public /*synchronized*/ String getKeyValue(String key) {
        checkTable();
        String value = "";
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().rawQuery("select " + VALUE + " from " + tableName + " where " + KEY + " = \"" + key + "\";", null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                value = cursor.getString(cursor.getColumnIndex(VALUE));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return value;
    }

    public long getUserId() {
        long user_id = -1;
        try {
            String value = getValue(USER_ID);
            if (!TextUtils.isEmpty(value))
                user_id = Long.parseLong(getValue(USER_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user_id;
    }


    private /*synchronized*/ String getValue(String column) {
        String value = "";
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(name, new String[]{column}, null, null, null, null, null, null);
            if (cursor != null) {
                cursor.moveToLast();
                value = cursor.getString(cursor.getColumnIndex(column));
            }
        } catch (Throwable e) {

        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return value;
    }

    public /*synchronized*/ void saveUserId(long user_id) {
        ContentValues values = new ContentValues();
        values.put(USER_ID, user_id);
        saveValue(values);
    }

    private /*synchronized*/ void saveValue(ContentValues values) {
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().rawQuery("select * from " + name, null);
            if (null != cursor && cursor.getCount() == 0) {
                getWritableDatabase().insert(name, null, values);
            } else {
                getWritableDatabase().update(name, values, null, null);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
    }

    /**
     * 0 未登录 false 1登录 true
     *
     * @param status
     */
    public void setLogin(int status) {
        ContentValues values = new ContentValues();
        values.put(ISLOGIN, status);
        saveValue(values);
    }

    /**
     * 0 未登录 false 1登录 true
     *
     * @return
     */
    public boolean getLogin() {
        int status = 0;
        try {
            status = Integer.parseInt(getValue(ISLOGIN));
        } catch (Throwable e) {

        }
        return status == 0 ? false : true;
    }

    public void setPhoneId(String phone) {
        ContentValues values = new ContentValues();
        values.put(USER_PHONE, phone);
        saveValue(values);
    }

    public String getPhone() {
        return getValue(USER_PHONE);
    }

    public void setName(String name) {
        ContentValues values = new ContentValues();
        values.put(USER_NAME, name);
        saveValue(values);
    }

    public String getName() {
        return getValue(USER_NAME);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {

        final SQLiteDatabase db;
        if (mDefaultWritableDatabase != null) {
            db = mDefaultWritableDatabase;
        } else {
            db = super.getWritableDatabase();
        }
        return db;
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        final SQLiteDatabase db;
        if (mDefaultReadableDatabase != null) {
            db = mDefaultReadableDatabase;
        } else {
            db = super.getReadableDatabase();
            this.mDefaultWritableDatabase = db;
            this.mDefaultReadableDatabase = db;
        }
        return db;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ContentValues values = null;
        Cursor cursor = null;
        try {//兼容老版本的数据库
            cursor = db.rawQuery("select * from " + name + ";", null);
            if (null != cursor) {
                values = new ContentValues();
                cursor.moveToLast();
                long user_id = cursor.getLong(cursor.getColumnIndex(USER_ID));
                int isLogin = cursor.getInt(cursor.getColumnIndex(ISLOGIN));
                String user_name = cursor.getString(cursor.getColumnIndex(USER_NAME));
                String user_pase = cursor.getString(cursor.getColumnIndex(USER_PASSWORD));
                String user_phone = cursor.getString(cursor.getColumnIndex(USER_PHONE));
                values.put(USER_ID, user_id);
                values.put(ISLOGIN, isLogin);
                values.put(USER_NAME, user_name);
                values.put(USER_PASSWORD, user_pase);
                values.put(USER_PHONE, user_phone);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        db.execSQL("DROP TABLE IF EXISTS " + name);
        this.onCreate(db);
        if (null != values) {
            saveValue(values);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
