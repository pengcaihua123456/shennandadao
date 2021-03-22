package com.example.mvvm;

import androidx.lifecycle.MutableLiveData;

public class ManagerModel {


    public MutableLiveData<Account> result;

    public MutableLiveData<Account> getAccountResult() {

        if(account==null){
            account=new Account();
        }
        if(result==null){
            result=new MutableLiveData<>();
            result.setValue(account);

        }
        account.level++;
        account.name="pengcaihua";
        return result;
    }
    public Account account;






}
