package flip.example.mac.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    Mylistview listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


//        listView = (Mylistview) findViewById(R.id.listview);
//        listView.setAdapter(new ArrayAdapter<String>(this,
//                android.R.layout.simple_expandable_list_item_1, getData()));
    }

    private List<String> getData() {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            data.add("测试" + i);
        }

        return data;

    }

}
