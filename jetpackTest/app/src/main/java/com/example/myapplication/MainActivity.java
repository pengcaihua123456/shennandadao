package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.nav.NavActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity {


    MyAndroidViewModel viewModel;
    ActivityMainBinding activityMainBinding;
    PengViewModel pengViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding=DataBindingUtil.setContentView(this,R.layout.activity_main);
//        viewModel=ViewModelProviders.of(this,new SavedStateViewModelFactory(this)).get(MyAndroidViewModel.class);
        activityMainBinding.setData(viewModel);//databing绑定ViewModel
        activityMainBinding.setLifecycleOwner(this);//基类实现了这个监听
//        MainActivity.this.startActivity(new Intent(MainActivity.this, MvvmActivity.class));


        MainActivity.this.startActivity(new Intent(MainActivity.this, NavActivity.class));

//        activityMainBinding.setData(pengViewModel);
//        pengViewModel= ViewModelProviders.of(this).get(PengViewModel.class);
//        MutableLiveData<String> mutableLiveData=pengViewModel.getData();
//        mutableLiveData.observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                mutableLiveData.setValue(s);
//                activityMainBinding.tvShow.setText(s);
//            }
//        });
    }
}