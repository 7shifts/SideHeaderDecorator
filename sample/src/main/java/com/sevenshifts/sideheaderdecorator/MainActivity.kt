package com.sevenshifts.sideheaderdecorator

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val names = NamesProvider().getNames()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        names_list.layoutManager = LinearLayoutManager(this)
        names_list.adapter = MyAdapter()

        val namesHeaderProvider = object : SideHeaderDecorator.HeaderProvider<Char> {
            override fun getHeader(position: Int) = names[position].first()
        }

        names_list.addItemDecoration(object : SideHeaderDecorator<Char>(namesHeaderProvider) {
            override fun getHeaderView(header: Char, parent: RecyclerView): View {
                val textView = LayoutInflater.from(parent.context).inflate(R.layout.header_view, parent, false) as TextView
                textView.text = header.toString()
                return textView
            }
        })
    }

    class MyViewHolder(private val view: TextView) : RecyclerView.ViewHolder(view) {

        fun setText(s: String) {
            view.text = s
        }
    }

    inner class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {

        override fun getItemCount() = names.size

        override fun onCreateViewHolder(parent: ViewGroup, position: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            return MyViewHolder(view as TextView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val name = names[position]
            holder.setText(name)
        }
    }
}
