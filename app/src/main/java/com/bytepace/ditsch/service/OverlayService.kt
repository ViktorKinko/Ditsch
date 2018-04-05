package com.bytepace.ditsch.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.IBinder
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import com.bytepace.ditsch.R
import java.util.*


class OverlayService : Service() {

    companion object {
        private const val ARG_SOURCE_TEXT = "arg_source_text"
        private const val BROADCAST_START_OVERLAY = "bcast_start_overlay"
        fun startService(ctx: Context) {
            val intent = Intent(ctx, OverlayService::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ctx.startService(intent)
        }

        fun stopService(ctx: Context) {
            val intent = Intent(ctx, OverlayService::class.java)
            ctx.stopService(intent)
        }

        fun showBubble(ctx: Context, text: String) {
            val i = Intent(BROADCAST_START_OVERLAY)
            i.putExtra(ARG_SOURCE_TEXT, text)
            ctx.sendBroadcast(i)
        }
    }

    private lateinit var manager: WindowManager
    private lateinit var view: View
    private var isRegisteredReceiver = false
    private var isShownPopup = false
    private val receiver = object : OverlayReceiver() {
        override fun showOverlay(overlayText: String) {
            if (!isShownPopup) {
                val size = getDisplaySize()
                val height = dpToPx(36f)
                manager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val params = prepareParams(size.x, height.toInt())
                view = prepareAndShowView(manager, params, size.y, overlayText)
                isShownPopup = true
            } else {
                updateViewTextAndColor(view, overlayText)
            }
        }
    }

    abstract class OverlayReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            showOverlay(intent.getStringExtra(ARG_SOURCE_TEXT))
        }

        abstract fun showOverlay(overlayText: String)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isRegisteredReceiver) {
            isRegisteredReceiver = true
            val filter = IntentFilter(BROADCAST_START_OVERLAY)
            registerReceiver(receiver, filter)
        }
        return START_STICKY_COMPATIBILITY
    }

    override fun onDestroy() {
        if (isShownPopup) {
            manager.removeViewImmediate(view)
        }
        if (isRegisteredReceiver) {
            unregisterReceiver(receiver)
        }
        super.onDestroy()
    }

    private fun getDisplaySize(): Point {
        val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val size = Point()
        display.getSize(size)
        return size
    }

    private fun dpToPx(dp: Float): Float {
        val r = resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics)
    }

    private fun prepareParams(screenWidth: Int, height: Int): WindowManager.LayoutParams {
        val params = WindowManager.LayoutParams(
                screenWidth,
                height,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)
        params.x = 0
        params.y = 0
        return params
    }

    private fun prepareAndShowView(manager: WindowManager, params: WindowManager.LayoutParams, screenHeight: Int, text: String): View {
        val rootView = LayoutInflater.from(this).inflate(R.layout.overlay_floating_tip, null)
        rootView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        rootView.setOnTouchListener(View.OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                params.x = event.rawX.toInt()
                params.y = event.rawY.toInt() - screenHeight / 2
                manager.updateViewLayout(rootView, params)
                return@OnTouchListener true
            }
            false
        })
        updateViewTextAndColor(rootView, text)
        manager.addView(rootView, params)
        return rootView
    }

    private fun updateViewTextAndColor(view: View, text: String) {
        view.findViewById<TextView>(R.id.text).text = text
        view.findViewById<TextView>(R.id.text).setBackgroundColor(getRandomColor())
    }

    private fun getRandomColor(): Int {
        val rnd = Random(System.currentTimeMillis())
        return Color.rgb(30 + Math.abs(rnd.nextInt() % 100), 30 + Math.abs(rnd.nextInt() % 100), 30 + Math.abs(rnd.nextInt() % 100))
    }
}