package sport.yuedong.com.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        add(52);


        add(17);

        add(36);


        long localID=getLocalId(1591149638987L);
        Log.d("peng","SecondActivity"+localID);

    }


    public static long getLocalId(long localId) {

        if (localId >= 1591149638 * 1000) {
            return (localId / 1000) * 1000;
        }

        return localId;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private final static int offsetCount = 18;
    private final static int LIMIT_COUNT = 20;


    private void add(int size) {

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }

        limitReport(list);

    }

    private void limitReport(final List<Integer> list) {

        if (list != null && list.size() > 0) {
            if (list.size() >= LIMIT_COUNT) {
                int curIndex = 0;
                int loop = list.size() / offsetCount;
                ArrayList<Integer> tempList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    if (i % offsetCount == 0 && i > 0) {//18 36 54 72
                        curIndex++;
                        ArrayList<Integer> tempUploadList = new ArrayList<>();
                        tempUploadList.addAll(tempList);
                        tempList.clear();
                        Log.d("peng", "upload" + curIndex);
                    } else {
                        tempList.add(list.get(i));
                        if (loop == curIndex && i == list.size() - 1) {
                            Log.d("peng", "upload remain all" + tempList.size() + "loop:" + loop + "i" + i);
                        } else {

                        }
                    }
                }
            } else {
                Log.d("peng", "upload <18 ");
            }
        }
    }
}
