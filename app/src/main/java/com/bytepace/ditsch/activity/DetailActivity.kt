package com.bytepace.ditsch.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.bytepace.ditsch.R
import com.bytepace.ditsch.service.OverlayService
import com.bytepace.ditsch.utils.AnimUtils
import com.bytepace.ditsch.utils.RandomTranslator
import java.util.*


/**
 * Created by Viktor on 16.03.2018.
 */

const val CODE_DRAW_OVER_OTHER_APP_PERMISSION = 100

class DetailActivity : AppCompatActivity() {

    companion object {
        private const val ARG_SOURCE_TEXT = "arg_source_text"
        private const val ARG_CHAIN_SIZE = "arg_chain_size"
        fun getIntent(ctx: Context, source_text: String, chain_size: Int): Intent {
            val intent = Intent(ctx, DetailActivity::class.java)
            intent.putExtra(ARG_SOURCE_TEXT, source_text)
            intent.putExtra(ARG_CHAIN_SIZE, chain_size)
            return intent
        }
    }

    private val currentLocale = Locale.getDefault().language

    private lateinit var translatedText: TextView
    private lateinit var sourceText: TextView
    private lateinit var progress: ProgressBar
    private lateinit var frame: FrameLayout
    private lateinit var swipeToRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        translatedText = findViewById(R.id.translated_text)
        progress = findViewById(R.id.progress)
        frame = findViewById(R.id.frame)

        setupSourceText()
        setupSwipeToRefresh()
        setupServiceButtons()

        OverlayService.startService(this)

        translateStringAndShowIt(intent.getStringExtra(ARG_SOURCE_TEXT), intent.getIntExtra(ARG_CHAIN_SIZE, 4))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDestroy() {
        OverlayService.stopService(this)
        super.onDestroy()
    }

    private fun setupSwipeToRefresh() {
        swipeToRefresh = findViewById(R.id.swipe_to_refresh)
        swipeToRefresh.setOnRefreshListener {
            refreshTranslation()
        }
        swipeToRefresh.setOnChildScrollUpCallback({ parent, child ->
            child?.canScrollVertically(-1) ?: false
        })
    }

    private fun refreshTranslation() {
        swipeToRefresh.isRefreshing = true
        progress.visibility = View.VISIBLE
        AnimUtils().animateViewFadeOut(translatedText, 300)
        updateTranslation()
    }

    private fun setupSourceText() {
        sourceText = findViewById(R.id.source_text)
        sourceText.text = intent.getStringExtra(ARG_SOURCE_TEXT)
        AnimUtils().animateViewFadeIn(sourceText)
    }

    private fun updateTranslation(): Boolean {
        translateStringAndShowIt(intent.getStringExtra(ARG_SOURCE_TEXT), intent.getIntExtra(ARG_CHAIN_SIZE, 4))
        return true
    }

    private fun translateStringAndShowIt(toTranslate: String, chain_size: Int) {
        progress.visibility = View.VISIBLE
        val handler = Handler()
        Thread({
            val result = RandomTranslator().makeRandomTranslateChain(toTranslate, chain_size, currentLocale)
            handler.post {
                showText(result)
            }
        }).start()
    }

    private fun showText(text: String) {
        translatedText.text = text
        AnimUtils().animateViewFadeIn(translatedText)
        progress.visibility = View.INVISIBLE
        swipeToRefresh.isRefreshing = false
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

    private fun setupServiceButtons() {
        findViewById<Button>(R.id.btn_show).setOnClickListener({
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                askPermission()
            } else {
                OverlayService.showBubble(this, translatedText.text.toString())
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun askPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
        startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            if (Settings.canDrawOverlays(this)) {
                OverlayService.showBubble(this, translatedText.text.toString())
            } else {
                Toast.makeText(this, "Draw over other app permission not available. Closing the application", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}