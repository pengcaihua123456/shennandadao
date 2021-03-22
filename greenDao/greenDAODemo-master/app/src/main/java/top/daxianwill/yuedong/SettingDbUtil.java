package top.daxianwill.yuedong;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.greendao.gen.DaoSession;
import com.greendao.gen.SettingBeanDao;

import java.util.List;

import top.daxianwill.greendaodemo.MyApplication;


public class SettingDbUtil extends SQLiteOpenHelper {


    private SettingBeanDao settingBeanDao;


    public static final String string_user_id = "user_id";
    public static final String string_islogin = "islogin";
    public static final String string_user_name = "user_name";
    public static final String string_user_password = "user_password";
    public static final String string_user_phone = "user_phone";

    public static final String string_hx_phone = "hx_phone";
    public static final String string_user_rank = "user_rank";

    private final static int version = 3;


    private SettingDbUtil(Context context) {

        super(context, SetttingDBHelper.name, null, version);
        Log.d("peng", "SettingDbUtil");
        DaoSession daoSession = GreenDaoUtil.getsInstance().getDaoSession();
        settingBeanDao = daoSession.getSettingBeanDao();
    }


    private static SettingDbUtil sInstance = null;

    public static SettingDbUtil getsInstance() {
        if (sInstance == null) {
            synchronized (SettingDbUtil.class) {
                if (sInstance == null) {
                    sInstance = new SettingDbUtil(MyApplication.instances);
                }
            }
        }


        return sInstance;
    }


    /********************数据拷贝*******************/
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("peng", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /***
     * 拷贝数据到greendao
     * 1.查询下老的表是否存在
     * 1.greendao是否存在数据
     *
     *
     *
     */
    private void copyDataToGreenDao() {
        if (isExsitData()) {
            Log.d("peng", "isExsitData  ");
            insert(getOldSettingBean());
        } else {
            Log.d("peng", "not isExsitData");
        }
    }


    private SettingBean getOldSettingBean() {
        SettingBean settingBean = new SettingBean();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(SetttingDBHelper.name, null, null, null, null, null, null, null);
            if (cursor != null) {
                cursor.moveToLast();
                String user_name = cursor.getString(cursor.getColumnIndex(string_user_name));
                int user_id = cursor.getInt(cursor.getColumnIndex(string_user_id));
                int islogin = cursor.getInt(cursor.getColumnIndex(string_islogin));
                String user_password = cursor.getString(cursor.getColumnIndex(string_user_password));
                String user_phone = cursor.getString(cursor.getColumnIndex(string_user_phone));
                int hx_phone = cursor.getInt(cursor.getColumnIndex(string_hx_phone));
                int user_rank = cursor.getInt(cursor.getColumnIndex(string_user_rank));

                settingBean.setUser_id(user_id);
                settingBean.setUser_name(user_name);
                settingBean.setIslogin(islogin);
                settingBean.setUser_password(user_password);
                settingBean.setUser_phone(user_phone);
                settingBean.setHx_phone(hx_phone);
                settingBean.setUser_rank(user_rank);

            }
        } catch (Throwable e) {

        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        Log.d("peng", "getOldSettingBean" + settingBean.toString());
        return settingBean;
    }


    /***
     * 旧的表是否存在数据
     * @return
     */
    private boolean isExsitData() {
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().rawQuery("select * from " + SetttingDBHelper.name, null);
            if (null != cursor && cursor.getCount() == 0) {
                return true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return false;
    }


    /******************************************/


    /***
     * 查询账户表，通过列
     * @param column
     * @return
     */
    public String getValue(String column) {

        String value = "";

//        try {
            List<SettingBean> settingBeans = settingBeanDao.loadAll();

            if (settingBeans != null && settingBeans.size() > 0) {//调用更新
                SettingBean settingBean = settingBeans.get(0);

                if (string_user_name.equals(column)) {
                    value = settingBean.getUser_name();
                } else if (string_user_password.equals(column)) {
                    value = settingBean.getUser_password();
                } else if (string_user_phone.equals(column)) {
                    value = settingBean.getUser_phone();
                }
            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Log.d("peng", "getValue column:" + column + "value:" + value);
        return value;
    }


    /***
     * 把键值对存储在扩展的数据库表里
     * 1.查询是否包含这个键
     * 取后面的
     * moveToLast();
     * @param key
     * @param value
     */
    public void saveValue(String key, Object value) {

        List<SettingBean> settingBeans = null;
//        try {
            settingBeans = settingBeanDao.loadAll();
            Log.d("peng", "settingBeans size:" + settingBeans.size());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if (settingBeans != null && settingBeans.size() > 0) {//调用更新
            Log.e("peng", "saveValue update:key=" + key + "value=" + value);
            SettingBean settingBean = settingBeans.get(settingBeans.size() - 1);
            update(key, value, settingBean);
        } else {//insert
            Log.d("peng", "saveValue insert:key=" + key + "value=" + value);
            insert(key, value);
        }

        query();//test
    }


    private void insert(SettingBean settingBean) {
//        try {
            settingBeanDao.insert(settingBean);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void insert(String column, Object value) {
        SettingBean settingBean = new SettingBean();
        Log.d("peng", "insert settingBean:" + settingBean);
        if (string_user_name.equals(column)) {
            settingBean.setUser_name(getStringValue(value));
        } else if (string_user_phone.equals(column)) {
            settingBean.setUser_phone(getStringValue(value));
        } else if (string_user_id.equals(column)) {

//            if (value instanceof Integer) {
//                Log.d("peng","value instanceof Integer :"+value);
//                value = (Long) value;
//            }
            if ((value instanceof Integer)) {
                settingBean.setUser_id((int)value);
            }else if(value instanceof Long){
                int valueInt=Integer.parseInt(String.valueOf(value));
                settingBean.setUser_id(valueInt);
            }
        } else if (string_islogin.equals(column)) {
            if ((value instanceof String)) {
                return;
            }
            settingBean.setIslogin((int) value);
        }

//        try {
            settingBeanDao.insert(settingBean);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    public static String getStringValue(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else {
            return String.valueOf(value);
        }
    }

    /***
     * 会修改所有的值，把初始值也会复制过来
     * 更新必须要有主键
     */
    private void update(String column, Object value, SettingBean settingBean) {

        Log.d("peng", "update before settingBean:" + settingBean);
        if (string_user_name.equals(column)) {
            if (isEquals(value, settingBean.getUser_name())) {
                return;
            }
            settingBean.setUser_name(getStringValue(value));
        } else if (string_user_phone.equals(column)) {
            if (isEquals(value, settingBean.getUser_phone())) {
                return;
            }
            settingBean.setUser_phone(getStringValue(value));
        } else if (string_user_id.equals(column)) {
//            if (settingBean.getUser_id() == ((Long) value)) {
//                return;
//            }
//            if (!(value instanceof Long)) {
//                return;
//            }
            if ((value instanceof Integer)||value instanceof Long) {
                settingBean.setUser_id((int)value);
            }
        } else if (string_islogin.equals(column)) {
            if (settingBean.getIslogin() == (int) value) {
                return;
            }
            if ((value instanceof String)) {
                return;
            }
            settingBean.setIslogin((int) value);
        }

        //test
//        settingBean.setUser_password("99999999");

        Log.d("peng", "settingBeanDao.update(settingBean):" + settingBean);
//        try {
            settingBeanDao.update(settingBean);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    private boolean isEquals(Object value, String goal) {

        if (!(value instanceof String)) {
            return false;
        }

        if (value != null && goal != null && goal.equals((String) value)) {
            return true;
        }

        return false;

    }


    /****************test method***************************/


    /***
     * 已经存在了就不能再添加
     */
    private void add() {
        SettingBean settingBean = new SettingBean();
        settingBean.setUser_id(123456);
        settingBean.setHx_phone(987654321);
        settingBean.setIslogin(1);
        settingBean.setUser_phone("15814473803");
        settingBeanDao.insert(settingBean);
    }

    private void delete() {
        SettingBean settingBean = new SettingBean();
        settingBeanDao.delete(settingBean);

        settingBeanDao.getKey(settingBean);
    }


    /***
     * 会修改所有的值，把初始值也会复制过来
     */
    private void update() {
        SettingBean settingBean = new SettingBean();
        settingBean.setUser_phone("18898357513");
        settingBean.setUser_id(123456);
        settingBeanDao.update(settingBean);
    }

    public void query() {

//        try {
            List<SettingBean> settingBeanList = settingBeanDao.loadAll();

            for (SettingBean settingBean1 : settingBeanList) {
                Log.e("peng", "after settingBean:" + settingBean1.toString());
            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void clear() {
        try {
            settingBeanDao.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        settingBeanDao.insertInTx();
    }

    public void query(int index) {

//        try {
            List<SettingBean> settingBeanList = settingBeanDao.loadAll();

            for (SettingBean settingBean1 : settingBeanList) {
                Log.e("peng", "mainactiivty settingBean:" + settingBean1.toString());
            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


}
