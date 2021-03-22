package glidetest.yuedong.com.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScollerActivity extends Activity {

    private ScrollerLayout scrollerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoller);

        scrollerLayout = (ScrollerLayout) findViewById(R.id.activity_marginlayout_scrollerlayout);

        /**
         * 动态添加view
         */
        scrollerLayout.removeAllViewsInLayout();
        for (int i = 0; i < 3; i++) {
            TextView textView = new TextView(this);
            textView.setBackgroundColor(Color.RED);
            textView.setTextColor(Color.WHITE);
            textView.setClickable(true);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
            textView.setText("Scroller滑动Demo-" + "TextView-" + (i + 1));
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(layoutParams);
            scrollerLayout.addView(textView);
        }


    }
}
