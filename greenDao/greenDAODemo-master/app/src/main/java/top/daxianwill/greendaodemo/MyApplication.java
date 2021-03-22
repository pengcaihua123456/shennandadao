package top.daxianwill.greendaodemo;

import android.app.Application;

import top.daxianwill.yuedong.GreenDaoUtil;

/**
 * 重写Application实现Greendao各种初始化操作
 */
public class MyApplication extends Application {


    public static MyApplication instances;//Application对象


    @Override
    public void onCreate() {

        super.onCreate();
        instances = this;
        GreenDaoUtil.getsInstance().init(this);


    }

}
