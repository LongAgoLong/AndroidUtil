package com.leo.androidutil.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.leo.androidutil.R
import com.leo.recyclerview_help.slide.slideslip.XItemSideslipViewHold
import com.leo.system.util.WindowUtils

class RvItemDeleteAdapter
constructor(private val context: Context,
            private val list: ArrayList<String>?)
    : RecyclerView.Adapter<RvItemDeleteAdapter.ItemHoldI>() {

    class ItemHoldI(itemView: View) : XItemSideslipViewHold(itemView) {
        var tv: TextView = itemView.findViewById(R.id.tv)
        var slideItemCl: ConstraintLayout = itemView.findViewById(R.id.slideItemCl)

        override fun getActionWidth(): Float {
            return WindowUtils.dp2px(dpValue = 100f).toFloat()
        }

        override fun getSlideView(): View {
            return slideItemCl
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHoldI {
        return ItemHoldI(LayoutInflater.from(context).inflate(R.layout.item_rv_delete, parent, false))
    }

    override fun getItemCount(): Int {
        list ?: return 0
        return list.size
    }

    override fun onBindViewHolder(holder: ItemHoldI, position: Int) {
        holder.tv.text = list!![position]
    }
}