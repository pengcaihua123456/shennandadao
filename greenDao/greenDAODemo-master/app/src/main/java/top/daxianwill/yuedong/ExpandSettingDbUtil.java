package top.daxianwill.yuedong;

import android.util.Log;

import com.greendao.gen.DaoSession;
import com.greendao.gen.ExpandSettingBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;


public class ExpandSettingDbUtil {


    ExpandSettingBeanDao expandSettingBeanDao;


    public ExpandSettingDbUtil() {
        Log.d("peng", "ExpandSettingDbUtil");
        DaoSession daoSession = GreenDaoUtil.getsInstance().getDaoSession();
        expandSettingBeanDao = daoSession.getExpandSettingBeanDao();
    }


    private static ExpandSettingDbUtil sInstance = null;

    public static ExpandSettingDbUtil getsInstance() {
        if (sInstance == null) {
            synchronized (ExpandSettingDbUtil.class) {
                if (sInstance == null) {
                    sInstance = new ExpandSettingDbUtil();
                }
            }
        }


        return sInstance;
    }


    /***
     * 把键值对存储在扩展的数据库表里
     * 1.查询是否包含这个键
     * @param key
     * @param value
     */
    public void saveKeyValue(String key, String value) {

        queryBefore();

        QueryBuilder<ExpandSettingBean> qb = expandSettingBeanDao.queryBuilder();//是否用
        QueryBuilder<ExpandSettingBean> studentQueryBuilder = qb.where(ExpandSettingBeanDao.Properties.Key.eq(key));
        List<ExpandSettingBean> expandSettingBeans = studentQueryBuilder.list(); //查出当前对应的数据

        if (expandSettingBeans != null && expandSettingBeans.size() > 0) {//调用更新
//            Log.e("peng", "update:key=" + key + "value:" + value);

            ExpandSettingBean expandSettingBean = expandSettingBeans.get(0);

//            String keyExpand=expandSettingBeanDao.getKey(expandSettingBean);
//            Log.d("peng","keyExpand:"+keyExpand);
            update(key, value, expandSettingBean);
        } else {//insert
//            Log.d("peng", "insert:key=" + key + "  value=" + value);
            insert(key, value);
        }

        query();//test
    }

    public String getKeyValue(String key) {

        String value = "";

        QueryBuilder<ExpandSettingBean> qb = expandSettingBeanDao.queryBuilder();//是否用
        QueryBuilder<ExpandSettingBean> studentQueryBuilder = qb.where(ExpandSettingBeanDao.Properties.Key.eq(key));
        List<ExpandSettingBean> expandSettingBeans = studentQueryBuilder.list(); //查出当前对应的数据
//        Log.d("peng", "getKeyValue expandSettingBeans size:" + expandSettingBeans.size());

        if (expandSettingBeans != null && expandSettingBeans.size() > 0) {//调用更新
            value = expandSettingBeans.get(0).getValue();
        }

//        Log.d("peng", "getKeyValue " + " key:" + key + "   value:" + value);
        return value;
    }

    public void insert(String key, String value) {
        ExpandSettingBean settingBean = new ExpandSettingBean();
        settingBean.setKey(key);
        settingBean.setValue(value);
        expandSettingBeanDao.insert(settingBean);
    }


    /***
     * 会修改所有的值，把初始值也会复制过来
     * 更新必须要有主键
     */
    private void update(String key, String value, ExpandSettingBean expandSettingBean) {
        ExpandSettingBean settingBean = new ExpandSettingBean();

        if (isEquals(value, expandSettingBean.getValue())) {
            return;
        }

        settingBean.setKey(key);
        settingBean.setValue(value);
        expandSettingBeanDao.update(settingBean);
    }


    private boolean isEquals(String value, String goal) {

        if (value != null && goal != null && goal.equals(value)) {
            return true;
        }

        return false;

    }

    /**************************************************************/


    public void add(int count) {
        ExpandSettingBean settingBean = new ExpandSettingBean();
        settingBean.setKey("peng" + count);
        settingBean.setValue("1234" + count);
        expandSettingBeanDao.insert(settingBean);
    }

    public void delete() {
        ExpandSettingBean settingBean = new ExpandSettingBean();
        expandSettingBeanDao.delete(settingBean);
    }


    /***
     * 会修改所有的值，把初始值也会复制过来
     */
    public void update() {
        ExpandSettingBean settingBean = new ExpandSettingBean();
        settingBean.setKey("tang");
        settingBean.setValue("8888");
        expandSettingBeanDao.update(settingBean);
    }

    public void queryBefore() {

        List<ExpandSettingBean> settingBeanList = expandSettingBeanDao.loadAll();
//        Log.e("peng", "before ExpandSettingBean:" + settingBeanList.size());

    }

    public void query() {

        List<ExpandSettingBean> settingBeanList = expandSettingBeanDao.loadAll();

        for (ExpandSettingBean expandSettingBean : settingBeanList) {
//            Log.d("peng", "after ExpandSettingBean:" + expandSettingBean.toString());
        }
    }

}
