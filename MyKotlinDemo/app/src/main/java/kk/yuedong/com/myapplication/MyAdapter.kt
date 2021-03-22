

package kk.yuedong.com.myapplication

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHold>{

    private var context: Context? = null
    private var list: ArrayList<String>? = null

    constructor(context: Context, list: ArrayList<String>) {
        this.context = context
        this.list = list
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHold {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return MyViewHold(LayoutInflater.from(context).inflate(R.layout.item_title_popup, null))
    }


    override fun getItemCount(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return 50
    }

    override fun onBindViewHolder(holder: MyViewHold, position: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        holder.name.text="peng"+position
    }


    class MyViewHold : RecyclerView.ViewHolder {

        constructor(view: View) : super(view) {
            name = view.findViewById(R.id.item_title_popup_tv) as TextView

        }

        var name: TextView

    }

}

