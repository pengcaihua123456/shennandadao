package yuedong.peng.com.yuedongplugin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
    }


    public void testIntent(Context context) {

        Intent intent = new Intent(context, PluginActivity.class);
        context.startActivity(intent);
    }
}
