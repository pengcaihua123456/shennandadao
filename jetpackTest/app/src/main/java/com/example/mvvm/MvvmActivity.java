package com.example.mvvm;

import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.databinding.TestMvvmBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

public class MvvmActivity extends AppCompatActivity {


    TestMvvmBinding testMvvmBinding;
    ManagerViewModel managerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testMvvmBinding= DataBindingUtil.setContentView(this,R.layout.test_mvvm);
        managerViewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(getApplication(),this)).get(ManagerViewModel.class);
        testMvvmBinding.setData(managerViewModel);
        testMvvmBinding.setLifecycleOwner(this);
    }
}