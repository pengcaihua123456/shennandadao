package yuedong.peng.com.yuedongplugin;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_stat_plugin_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动插件Activity
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("yuedong.peng.com.yuedongplugin", "yuedong.peng.com.yuedongplugin.PluginActivity"));
                startActivity(intent);
            }
        });
    }
}


