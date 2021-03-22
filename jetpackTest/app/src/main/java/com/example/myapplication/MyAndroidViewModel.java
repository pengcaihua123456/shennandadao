package com.example.myapplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

public class MyAndroidViewModel extends AndroidViewModel {

    private SavedStateHandle saveStateHanle;

    public MyAndroidViewModel(@NonNull Application application, MutableLiveData<Integer> number, SavedStateHandle saveStateHanle) {
        super(application);
        this.saveStateHanle=saveStateHanle;

    }

    public static final String KEY="dddsfdsafsfds";

    /**
     * 不能为私有
     * @return
     */
    public MutableLiveData<Integer> getNumber() {
        if(!saveStateHanle.contains(KEY)){
            saveStateHanle.set(KEY,0);
        }
        return saveStateHanle.getLiveData(KEY);
    }

    public void add() {
        getNumber().setValue(getNumber().getValue()+1);
    }


    public MyAndroidViewModel(@NonNull Application application) {
        super(application);
    }
}
