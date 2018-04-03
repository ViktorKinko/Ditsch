package com.bytepace.ditsch.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import com.bytepace.ditsch.R

/**
 * Created by Viktor on 19.03.2018.
 */
class TranslateHistoryAdapter(private var strings: ArrayList<String>, private val callback: Callback) : RecyclerView.Adapter<TranslateHistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item, parent, false)
        return TranslateHistoryAdapter.ViewHolder(view, callback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    var list: List<String> = strings

    override fun getItemCount(): Int {
        return list.size
    }

    fun refreshItems(strings: ArrayList<String>) {
        this.strings.clear()
        this.strings.addAll(strings)
        list = strings
    }

    fun filter(filter: String) {
        list = strings.filter {
            it.contains(filter)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View?, private val callback: Callback) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView!!.findViewById(R.id.text)

        fun bind(text: String) {
            textView.text = text
            itemView.setOnClickListener {
                callback.onOpen(text)
            }
            itemView.setOnLongClickListener {
                createPopupMenu(itemView, text)
                true
            }
        }

        private fun createPopupMenu(v: View, text: String) {
            val popupMenu = PopupMenu(v.context, v)
            popupMenu.inflate(R.menu.popup_history)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_delete -> {
                        callback.onRemove(text)
                        return@setOnMenuItemClickListener true
                    }
                }
                false
            }
            popupMenu.show()
        }
    }

    interface Callback {
        fun onOpen(string: String)
        fun onRemove(string: String)
    }
}