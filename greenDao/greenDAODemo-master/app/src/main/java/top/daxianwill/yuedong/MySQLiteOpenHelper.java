package top.daxianwill.yuedong;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.greendao.gen.DaoMaster;
import com.greendao.gen.ExpandSettingBeanDao;
import com.greendao.gen.SettingBeanDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Growth on 2016/3/3.
 */
public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.w("peng", "onUpgrade oldVersion===" + oldVersion + "  newVersion==" + newVersion);
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, SettingBeanDao.class, ExpandSettingBeanDao.class);
    }
}
