package com.bytepace.ditsch.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.bytepace.ditsch.R
import com.bytepace.ditsch.adapter.DitschAdapter
import com.bytepace.ditsch.utils.RandStr

class ListActivity : AppCompatActivity() {

    private lateinit var dichList: RecyclerView
    private var ditschAdapter: DitschAdapter = DitschAdapter({
        openDetailActivity(it)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        dichList = findViewById(R.id.list)
        initiateDichList()
    }

    private fun initiateDichList() {
        dichList.layoutManager = LinearLayoutManager(this)
        dichList.adapter = ditschAdapter
        ditschAdapter.setStrings(generateStrings())
        ditschAdapter.notifyDataSetChanged()
    }

    private fun openDetailActivity(text: String) {
        val i = DetailActivity.getIntent(this, text, 4)
        startActivity(i)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    private fun generateStrings(): ArrayList<String> {
        val strings: ArrayList<String> = ArrayList()
        for (i in 0..100) {
            strings.add(RandStr.getSentence(false))
        }
        return strings
    }
}