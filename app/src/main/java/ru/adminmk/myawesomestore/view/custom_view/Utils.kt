package ru.adminmk.myawesomestore.view.custom_view

import android.content.res.Resources
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

fun dp2px(dips: Float): Float = dips * Resources.getSystem().displayMetrics.density + 0.5f

class FontSpan(private val font: Typeface?) : MetricAffectingSpan() {

    companion object {
        const val WRONG_TYPEFACE = 0
    }

    override fun updateMeasureState(textPaint: TextPaint) = updateTypeface(textPaint)

    override fun updateDrawState(textPaint: TextPaint) = updateTypeface(textPaint)

    private fun updateTypeface(textPaint: TextPaint) {
        textPaint.apply {
            val oldStyle = getOldStyle(typeface)

            if (oldStyle == WRONG_TYPEFACE) return

            typeface = Typeface.create(font, oldStyle)
        }
    }

    private fun getOldStyle(typeface: Typeface?) = typeface?.style ?: WRONG_TYPEFACE
}
