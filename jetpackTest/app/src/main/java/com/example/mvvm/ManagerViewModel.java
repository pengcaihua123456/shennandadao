package com.example.mvvm;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


/**
 * 1.ViewModel如何拿到Databing对象?在Activity的时候已经相互绑定了。
 * 2.ViewModel拿到Model对象
 *
 * 个人理解VM在取到Model层的数据后，应该是通过LiveData的post或者set方法来通知View层有数据更新，View层调用Adapter去刷新RecyclerView数据。以上只是个人理解
 */
public class ManagerViewModel extends ViewModel {

    private ManagerModel managerModel;
    public MutableLiveData<Account> result;

    public void load(){
          managerModel= new ManagerModel();
          result=managerModel.getAccountResult();
          String name= managerModel.account.getName();
          Log.d("pengpeng","name:"+name);
          //model把值给了ViewModel,ViewModel又和Databing绑定在了一起
      }
}
