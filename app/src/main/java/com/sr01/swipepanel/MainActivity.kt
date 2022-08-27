package com.sr01.swipepanel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {
    private lateinit var bv: NestedScrollBridgeView
    private lateinit var vp: ViewPager2
    private lateinit var adapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bv = findViewById(R.id.bv)

        vp = findViewById(R.id.vp)
        vp.adapter = MyAdapter()

        val lp = bv.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = lp.behavior as SwipeBehavior
        behavior.draggableView = vp
    }

    private class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tv: TextView = itemView.findViewById(R.id.tv)

        fun bind(position: Int) {
            var text = ""
            repeat(100) {
                text += "This is Pager $position\n"
            }
            tv.text = text
        }
    }

    private class MyAdapter: RecyclerView.Adapter<MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_pager_content, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bind(position)
        }

        override fun getItemCount(): Int = 3
    }
}