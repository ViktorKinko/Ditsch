package com.bytepace.ditsch.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.bytepace.ditsch.R
import com.bytepace.ditsch.utils.AnimUtils
import com.bytepace.ditsch.utils.RandomTranslator
import java.util.*


/**
 * Created by Viktor on 16.03.2018.
 */
class DetailActivity : AppCompatActivity() {

    companion object {
        const val ArgSourceText = "arg_source_text"
        const val ArgChainSize = "arg_chain_size"
        fun getIntent(ctx: Context, source_text: String, chain_size: Int): Intent {
            val intent = Intent(ctx, DetailActivity::class.java)
            intent.putExtra(ArgSourceText, source_text)
            intent.putExtra(ArgChainSize, chain_size)
            return intent
        }
    }

    val currentLocale = Locale.getDefault().language

    lateinit var translatedText: TextView
    lateinit var sourceText: TextView
    lateinit var progress: ProgressBar
    lateinit var frame: FrameLayout
    lateinit var swipeToRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        translatedText = findViewById(R.id.translated_text)
        progress = findViewById(R.id.progress)
        frame = findViewById(R.id.frame)

        setupSourceText()
        setupSwipeToRefresh()

        translateStringAndShowIt(intent.getStringExtra(ArgSourceText), intent.getIntExtra(ArgChainSize, 4))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupSwipeToRefresh() {
        swipeToRefresh = findViewById(R.id.swipe_to_refresh)
        swipeToRefresh.setOnRefreshListener {
            swipeToRefresh.isRefreshing = true
            progress.visibility = View.VISIBLE
            AnimUtils().animateViewFadeOut(translatedText, 300)
            updateTranslation()
        }
        swipeToRefresh.setOnChildScrollUpCallback({ parent, child ->
            child?.canScrollVertically(-1) ?: false
        })
    }

    private fun setupSourceText() {
        sourceText = findViewById(R.id.source_text)
        sourceText.text = intent.getStringExtra(ArgSourceText)
        AnimUtils().animateViewFadeIn(sourceText)
    }

    private fun updateTranslation(): Boolean {
        translateStringAndShowIt(intent.getStringExtra(ArgSourceText), intent.getIntExtra(ArgChainSize, 4))
        return true
    }

    private fun translateStringAndShowIt(toTranslate: String, chain_size: Int) {
        progress.visibility = View.VISIBLE
        val handler = Handler()
        Thread({
            val result = RandomTranslator().makeRandomTranslateChain(toTranslate, chain_size, currentLocale)
            handler.post {
                translatedText.text = result
                AnimUtils().animateViewFadeIn(translatedText)
                progress.visibility = View.INVISIBLE
                swipeToRefresh.isRefreshing = false
            }
        }).start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}