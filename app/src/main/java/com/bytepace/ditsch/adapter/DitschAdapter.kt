package com.bytepace.ditsch.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bytepace.ditsch.R

/**
 * Created by Viktor on 15.03.2018.
 */
class DitschAdapter(private val callback: (String) -> Unit) : RecyclerView.Adapter<DitschAdapter.ViewHolder>() {
    private val strings: java.util.ArrayList<String> = ArrayList()

    fun setStrings(strings: java.util.ArrayList<String>) {
        this.strings.addAll(strings)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view, callback)
    }

    override fun getItemCount(): Int {
        return strings.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(strings[position])
    }

    class ViewHolder(itemView: View?, private val callback: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView!!.findViewById(R.id.text)

        fun bind(text: String) {
            textView.text = text
            itemView.setOnClickListener({
                callback.invoke(text)
            })
        }
    }
}