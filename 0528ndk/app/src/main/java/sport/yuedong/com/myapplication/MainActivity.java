package sport.yuedong.com.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    MyException myException;
    TextView tv;

    @Override
    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
        Log.d("peng","onNewIntent");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myException = new MyException();

        // Example of a call to a native method
        tv = (TextView) findViewById(R.id.sample_text);


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv.setText(myException.stringFromJNI());

//                try {
//                    myException.nativeThrowException();
//                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
//                }

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);


            }
        });


    }


}
