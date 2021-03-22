package glidetest.yuedong.com.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.lang.reflect.Method;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = (ImageView) findViewById(R.id.my_image_view);

        Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView);


        checkReadPermission(Manifest.permission.CALL_PHONE, REQUEST_CALL_PERMISSION);
        checkReadPermission(Manifest.permission.ANSWER_PHONE_CALLS, 444);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // 给bnt1添加点击响应事件
                Intent intent = new Intent(MainActivity.this, ScollerActivity.class);
                //启动
                startActivity(intent);

//                rejectCall();

                clickMe(v);
            }
        });


    }


    public void clickMe(View view) {
        Log.i("TAG", "callMethod ing   ");
    }

    public static final int REQUEST_CALL_PERMISSION = 10111; //拨号请求码


    public boolean checkReadPermission(String string_permission, int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(this, string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(this, new String[]{string_permission}, request_code);
        }
        return flag;
    }

    public void rejectCall() {
        try {
            // 首先拿到TelephonyManager
            TelephonyManager telMag = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            Class<TelephonyManager> c = TelephonyManager.class;

            // 再去反射TelephonyManager里面的私有方法 getITelephony 得到 ITelephony对象
            Method mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);
            //允许访问私有方法
            mthEndCall.setAccessible(true);
            final Object obj = mthEndCall.invoke(telMag, (Object[]) null);

//            // 再通过ITelephony对象去反射里面的call方法，并传入包名和需要拨打的电话号码
//            Method mt = obj.getClass().getMethod("call", new Class[]{String.class, String.class});
//            //允许访问私有方法
//            mt.setAccessible(true);
//            mt.invoke(obj, new Object[]{getPackageName() + "", "10086"});

            Toast.makeText(MainActivity.this, "拨打电话！", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    try {
                        // 延迟5秒后自动挂断电话
                        // 再通过ITelephony对象去反射里面的endCall方法，挂断电话
                        Method mt = obj.getClass().getMethod("endCall");
                        //允许访问私有方法
                        mt.setAccessible(true);
                        mt.invoke(obj);
                        Toast.makeText(MainActivity.this, "挂断电话！", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 5 * 1000);
        } catch (Exception e) {
            Log.e("peng", "e");
            e.printStackTrace();

        }
    }


}



