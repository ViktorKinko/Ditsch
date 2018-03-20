package com.bytepace.ditsch.utils

import android.animation.ValueAnimator
import android.view.View

/**
 * Created by Viktor on 19.03.2018.
 */
class AnimUtils {
    fun animateViewFadeIn(view: View, duration: Long = 1000, onCompleteCallback: () -> Unit = {}) {
        animateViewAlpha(view, 0f, 1f, duration, onCompleteCallback)
    }

    fun animateViewFadeOut(view: View, duration: Long = 1000, onCompleteCallback: () -> Unit = {}) {
        animateViewAlpha(view, 1f, 0f, duration, onCompleteCallback)
    }

    private fun animateViewAlpha(view: View, alphaStart: Float, alphaEnd: Float, duration: Long, onCompleteCallback: () -> Unit = {}) {
        view.alpha = alphaStart
        val animator = ValueAnimator.ofFloat(alphaStart, alphaEnd)
        animator.addUpdateListener {
            view.alpha = it.animatedValue as Float
            if (view.alpha == alphaEnd) {
                onCompleteCallback.invoke()
            }
        }
        animator.duration = duration
        animator.start()
    }
}