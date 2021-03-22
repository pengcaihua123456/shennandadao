package test.yuedong.com.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;

import test.yuedong.com.myapplication.recycleview.RefreshLoadMoreRecyclerView;


/***
 * ActivitySportWatch
 */
public class ActivityMyDevice extends Activity implements View.OnClickListener, RefreshLoadMoreRecyclerView.OnRefeshDataListener {


    private RefreshLoadMoreRecyclerView recyclerView;
    private NewsAdapter mAdapter;
    private LinkedList<MyDeviceBean.DeviceInfo> photosLinkedList = new LinkedList<>();
    private ActivityMyDevice activitySportBase;


    public static void gotoMyDeviceInent(Context context) {
        Intent intent = new Intent(context, ActivityMyDevice.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_recycle);
        setTitle("选择设备");
        initView();
        initData();

    }
    ///hardware/bind_hardware

    private void initView() {

        activitySportBase = this;
        recyclerView = findViewById(R.id.device_list);
        recyclerView.setEnableLoadMore(true);
        recyclerView.setEnableRefresh(true);
        recyclerView.setEnableOverScroll(true);
        recyclerView.setOnRefreshListener(this);
        recyclerView.getRecyclerView().setNestedScrollingEnabled(false);

        mAdapter = new NewsAdapter(ActivityMyDevice.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityMyDevice.this));
        recyclerView.setAdapter(mAdapter);


    }

    private void initData() {
//        loadData();

        addDataTest();
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefeshData() {
        Log.d("peng", "onRefeshData");
    }

    @Override
    public void onLoadMoreData() {
        Log.d("peng", "onLoadMoreData");
        addDataTest();
    }

    private int index;

    private void addDataTest() {
        LinkedList<MyDeviceBean.DeviceInfo> linkedList = new LinkedList<>();

        for (int i = 0; i < 25; i++) {
            MyDeviceBean.DeviceInfo deviceInfo = new MyDeviceBean.DeviceInfo();
            deviceInfo.device_name = "11111" + index;
            index++;
            linkedList.add(deviceInfo);
        }


        photosLinkedList.addAll(linkedList);
        mAdapter.setData(photosLinkedList);
        recyclerView.setVisibility(View.VISIBLE);

    }


}
