package top.daxianwill.yuedong;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class SetttingDBHelper {
//	TODO 需要更新新的 这个效率太差

    public final static String name = "setting";

    private SetttingDBHelper(Context context) {

    }

    private static SetttingDBHelper dbHelper = null;

    public static SetttingDBHelper getIntance(Context context) {
        if (null == dbHelper) {
            synchronized (SetttingDBHelper.class) {
                if (null == dbHelper) {
                    dbHelper = new SetttingDBHelper(context);
                }
            }
        }
        return dbHelper;
    }

    private final static String USER_ID = "user_id";
    private final static String ISLOGIN = "islogin";
    private final static String USER_NAME = "user_name";
    private final static String USER_RANK = "user_rank";
    private final static String USER_PASSWORD = "user_password";
    private final static String USER_PHONE = "user_phone";
    private final static String HX_USER = "hx_phone"; //0表示未知, 1表示非环信用户 , 2表示环信用户


    /***
     * 把键值对存储在扩展的数据库表里
     * @param key
     * @param value
     */
    public void saveKeyValue(String key, String value) {
        ExpandSettingDbUtil.getsInstance().saveKeyValue(key, value);
    }

    public String getKeyValue(String key) {
        return ExpandSettingDbUtil.getsInstance().getKeyValue(key);
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


    /***
     * 查询账户表，通过列
     * @param column
     * @return
     */
    private /*synchronized*/ String getValue(String column) {
        return SettingDbUtil.getsInstance().getValue(column);
    }

    public /*synchronized*/ void saveUserId(long user_id) {
        Log.w("peng", "saveUserId=" + user_id);
        saveValue(USER_ID, user_id);
    }


    /***
     * 主要用的几个数据
     * 1.用户id
     * 2.是否登入login
     * 3.电话号码
     *
     * @param key
     * @param value
     */
    private /*synchronized*/ void saveValue(String key, Object value) {
        SettingDbUtil.getsInstance().saveValue(key, value);
    }

    /**
     * 0 未登录 false 1登录 true
     *
     * @param status
     */
    public void setLogin(int status) {
        Log.d("peng", "status" + status);
        saveValue(ISLOGIN, status);
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
        saveValue(USER_PHONE, phone);
    }

    public String getPhone() {
        return getValue(USER_PHONE);
    }

    public void setName(String name) {
        saveValue(USER_NAME, name);
    }

    public String getName() {
        return getValue(USER_NAME);
    }




}
