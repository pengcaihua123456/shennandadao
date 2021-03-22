package kk.yuedong.com.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView

 public class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var tv_test=findViewById<TextView>(R.id.peng_tv)
        tv_test.text="871"


        tv_test.visibility= View.GONE;

        tv_test.setOnClickListener {

            tv_test.text="pengpeng"
        }

        var recyclerView=findViewById<RecyclerView>(R.id.recycler_view);

        recyclerView.layoutManager = LinearLayoutManager(this)
        var adapter=MyAdapter(this,initData()!!);

        recyclerView.adapter=adapter;

        var testBean=TestBean();
        var height=testBean.getHeight();
        Log.d("peng","height="+height);
        println("$height start paly")

    }

    private fun initData(): ArrayList<String>? {
        var list=ArrayList<String>()
        for (i in 0..50){
            list.add(i.toString());
        }
        Log.d("tag","list.size------"+ list.size)
        return list
    }



     fun main(args: Array<String>) {
         val s = "runoob"
         val str = "$s.length is ${s.length}" // 求值结果为 "runoob.length is 6"
         println(str)
     }

}
