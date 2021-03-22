package top.daxianwill.yuedong;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.greendao.gen.DaoMaster;
import com.greendao.gen.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

public class GreenDaoUtil {


    private static GreenDaoUtil sInstance = null;

    public static GreenDaoUtil getsInstance() {


//        ActivityManager.RunningAppProcessInfo processInfo = AndroidUtils.getCurrentProcessInfo(ShadowApp.context());
//        String thisProcessName = processInfo.processName;
//
//        Log.v("peng", "GreenDaoUtil getsInstance isInit" + isInit+"thisProcessName==="+thisProcessName);

        if (sInstance == null) {
            synchronized (GreenDaoUtil.class) {
                if (sInstance == null) {
                    sInstance = new GreenDaoUtil();
                }
            }
        }
        return sInstance;
    }


    private MySQLiteOpenHelper mHelper;

    private SQLiteDatabase db;

    private DaoMaster mDaoMaster;//Dao中的管理者。它保存了sqlitedatebase对象以及操作DAO

    private DaoSession mDaoSession;//会话层。操作具体的DAO对象（注意：是对象），比如各种getter方法


    private static boolean isInit;


    public static final String SETTING_DBNAME = "setting";

    public void init(Context context) {
        Log.e("peng", "GreenDaoUtil init context" + isInit);
        setDatabase(context);
        isInit = true;
    }


    /**
     * 设置greenDAO
     * 1.创建数据库
     * 2.得到DaoSession，最后操作的类
     */
    private void setDatabase(Context context) {

        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new MySQLiteOpenHelper(context, SETTING_DBNAME, null);

        db = mHelper.getWritableDatabase();
        int version=db.getVersion();
        Log.d("peng","version:"+version);

        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);


        mDaoSession = mDaoMaster.newSession();

        setDebugMode(true);
    }


    //返回DaoSession对象  DaoSession：会话层。操作具体的DAO对象（注意：是对象），比如各种getter方法
    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    //返回SQLiteDatabase对象
    public SQLiteDatabase getDb() {
        return db;
    }


    //是否开启Log
    private void setDebugMode(boolean flag) {
        QueryBuilder.LOG_SQL = flag;
        QueryBuilder.LOG_VALUES = flag;
    }

    public void querySetting() {
        SettingDbUtil.getsInstance().query(1);
    }


}
