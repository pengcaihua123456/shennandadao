package com.example.nav;

import android.os.Bundle;

import com.example.myapplication.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class NavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        NavController navController= Navigation.findNavController(this,R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this,navController);
    }

    /***
     * 添加返回键跳转
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController= Navigation.findNavController(this,R.id.fragment);
        return navController.navigateUp();
    }
}