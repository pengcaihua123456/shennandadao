package com.example.myapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

class PengViewModel extends ViewModel {

    private MutableLiveData<String> data;

    public MutableLiveData<String> getData() {
        if(data==null){
            data=new MutableLiveData<>();
            data.postValue("peng");
        }
        return data;
    }
}
