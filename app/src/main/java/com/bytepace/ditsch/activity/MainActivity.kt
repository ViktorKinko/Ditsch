package com.bytepace.ditsch.activity

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.SeekBar
import com.bytepace.ditsch.R
import com.bytepace.ditsch.adapter.TranslateHistoryAdapter
import com.bytepace.ditsch.database.HistoryDatabase
import com.bytepace.ditsch.database.SearchRequest
import com.bytepace.ditsch.utils.AnimUtils
import com.bytepace.ditsch.utils.RandStr


class MainActivity : AppCompatActivity() {

    private lateinit var btn_go: Button
    private lateinit var edit_text: EditText
    private lateinit var seek_bar: SeekBar
    private lateinit var frame: FrameLayout
    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: TranslateHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_go = findViewById(R.id.btn_go)
        edit_text = findViewById(R.id.edit_text)
        seek_bar = findViewById(R.id.seek_bar)
        frame = findViewById(R.id.frame)

        btn_go.setOnClickListener({
            onTranslateBtnClick()
        })
        initList()
    }

    private fun initList() {
        recyclerView = findViewById(R.id.list)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        adapter = TranslateHistoryAdapter(loadHistory(), object : TranslateHistoryAdapter.Callback{
            override fun onOpen(string: String) {
                edit_text.setText(string)
            }

            override fun onRemove(string: String) {
                findAndRemove(string)
                adapter.refreshItems(loadHistory())
                adapter.notifyDataSetChanged()
            }
        })

        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun loadHistory(): ArrayList<String> {
        val list = ArrayList<String>()
        val history = HistoryDatabase.getInstance(this).searchRequestDao().getAll()
        for (h: SearchRequest in history) {
            val c: String? = h.content
            c ?: continue
            list.add(c)
        }
        return list
    }

    private fun insertHistory(text: String) {
        val searchRequest = SearchRequest()
        searchRequest.content = text
        findAndRemove(text)
        HistoryDatabase.getInstance(this).searchRequestDao().insert(searchRequest)
    }

    private fun findAndRemove(text: String){
        val sameStuff = HistoryDatabase.getInstance(this).searchRequestDao().getSame(text)
        if (sameStuff.isNotEmpty()) {
            for (sameRequest in sameStuff) {
                HistoryDatabase.getInstance(this).searchRequestDao().remove(sameRequest)
            }
        }
    }

    private fun onTranslateBtnClick() {
        AnimUtils().animateViewFadeOut(edit_text, 300, onCompleteCallback = {
            val text: String = prepareText()
            insertHistory(text)
            startDetailActivity(text, prepareAnimation())
        })
    }

    private fun prepareText(): String {
        return if (edit_text.text.isEmpty()) {
            RandStr.getSentence(false)
        } else {
            edit_text.text.toString()
        }
    }

    @SuppressLint("NewApi")
    private fun prepareAnimation(): Bundle? {
        var bundle: Bundle? = null
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val options = ActivityOptions.makeSceneTransitionAnimation(this, frame, getString(R.string.transition_main_to_detail))
            bundle = options.toBundle()
        }
        return bundle
    }

    private fun startDetailActivity(source: String, bundle: Bundle?) {
        val intent = DetailActivity.getIntent(this, source, seek_bar.progress)
        if (bundle == null) {
            startActivity(intent)
        } else {
            startActivity(intent, bundle)
        }
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    override fun onResume() {
        edit_text.alpha = 1f
        adapter.refreshItems(loadHistory())
        adapter.notifyDataSetChanged()
        super.onResume()
    }
}