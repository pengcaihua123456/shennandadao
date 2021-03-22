package top.daxianwill.greendaodemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import top.daxianwill.yuedong.ExpandSettingDbUtil;
import top.daxianwill.yuedong.SettingDbUtil;


/**
 * 数据库操作类
 */
public class MainActivity extends AppCompatActivity {

    private ListView mListView;//列表组件对象

    private List<User> mUserList = new ArrayList<>();//存放列表数据的集合

    private MyAdapter mAdapter;//列表适配器


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.list_view);

        mAdapter = new MyAdapter(this, mUserList);

        mListView.setAdapter(mAdapter);
    }





    private static Long count = 1l;

    /**
     * 增
     */
    public void insert(View view) {



        int insertInt=Integer.parseInt(String.valueOf(111l));
        Log.d("peng","insert insert"+insertInt);


        notifyListView();


//        SettingDbUtil.getsInstance().clear();

         SettingDbUtil.getsInstance().saveValue(SettingDbUtil.string_user_id, 123456);

//        SettingDbUtil.getsInstance().saveValue(SettingDbUtil.string_user_id, 47419973l);
//        SettingDbUtil.getsInstance().saveValue(SettingDbUtil.string_user_name,"username"+count);


//        ExpandSettingDbUtil.getsInstance().saveKeyValue("name", "peng" + count);
//        ExpandSettingDbUtil.getsInstance().saveKeyValue("VOICE_TYPE","10"+count);
//        ExpandSettingDbUtil.getsInstance().getKeyValue("VOICE_TYPE");

        count++;
    }

    /**
     * 删
     */
    public void delete(View view) {
        notifyListView();
    }

    /**
     * 改
     */
    public void update(View view) {

        notifyListView();
        ExpandSettingDbUtil.getsInstance().saveKeyValue("name", "peng" + count);
        ExpandSettingDbUtil.getsInstance().getKeyValue("name");
    }

    /**
     * 查
     */
    public void loadAll(View view) {

        notifyListView();

        SettingDbUtil.getsInstance().query(1);
        ExpandSettingDbUtil.getsInstance().query();

    }

    /**
     * 更新ListView
     */
    public void notifyListView() {

        mUserList.clear();
        mAdapter = new MyAdapter(MainActivity.this, mUserList);
        mListView.setAdapter(mAdapter);
    }


}
