package ru.adminmk.myawesomestore.view.custom_view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import ru.adminmk.myawesomestore.R
import android.graphics.Outline
import android.os.Build
import android.view.ViewOutlineProvider

class BottomHanger
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val mainPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val path = Path()
    private var circleRadius: Int = 0

    init {

        context.withStyledAttributes(attrs, R.styleable.BottomHanger) {
            mainPaint.color = getColor(R.styleable.BottomHanger_mainPaint, 0)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            with(it) {
                drawCircle(
                    2 * circleRadius.toFloat(),
                    circleRadius.toFloat(),
                    circleRadius.toFloat(),
                    mainPaint
                )
                drawCircle(
                    (width - 2 * circleRadius).toFloat(),
                    circleRadius.toFloat(),
                    circleRadius.toFloat(),
                    mainPaint
                )
                drawRect(
                    2 * circleRadius.toFloat(),
                    0f, (width - 2 * circleRadius).toFloat(),
                    circleRadius.toFloat(),
                    mainPaint
                )

                save()

                clipRect(
                    0f,
                    circleRadius.toFloat(),
                    circleRadius.toFloat(),
                    2 * circleRadius.toFloat()
                )
                path.rewind()
                path.addCircle(
                    0f, circleRadius.toFloat(),
                    circleRadius.toFloat(), Path.Direction.CCW
                )
                clipOutPath(path)
                drawPaint(mainPaint)
                restore()

                drawRect(
                    circleRadius.toFloat(),
                    circleRadius.toFloat(), (width - circleRadius).toFloat(),
                    2 * circleRadius.toFloat(),
                    mainPaint
                )

                save()
                clipRect(
                    (width - circleRadius).toFloat(),
                    circleRadius.toFloat(),
                    width.toFloat(),
                    2 * circleRadius.toFloat()
                )
                path.rewind()
                path.addCircle(
                    width.toFloat(), circleRadius.toFloat(),
                    circleRadius.toFloat(), Path.Direction.CCW
                )
                clipOutPath(path)
                drawPaint(mainPaint)
                restore()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        circleRadius = Math.min(width / 4, height / 2)

        setOutlineProvider(CustomOutline(width, height, circleRadius))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width: Int
        val height: Int

        // Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            // Must be this size
            width = widthSize
        } else if (widthMode == MeasureSpec.AT_MOST) {
            // Can't be bigger than...
            width = 0
        } else {
            // Be whatever you want
            width = 0
        }

        // Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            // Must be this size
            height = heightSize
        } else if (heightMode == MeasureSpec.AT_MOST) {
            // Can't be bigger than...
            height = 0
        } else {
            // Be whatever you want
            height = 0
        }

        setMeasuredDimension(width, height)
    }
}

private class CustomOutline constructor(var width: Int, var height: Int, val circleRadius: Int) :
    ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val pathOutline = Path()
            pathOutline.addArc(0f - circleRadius.toFloat(), 0f, circleRadius.toFloat(), 2 * circleRadius.toFloat(), 0f, 90f)
            pathOutline.addArc(circleRadius.toFloat(), 0f, 3 * circleRadius.toFloat(), 2 * circleRadius.toFloat(), 180f, 90f)

            pathOutline.moveTo(2 * circleRadius.toFloat(), 0f)
            pathOutline.lineTo((width - 2 * circleRadius).toFloat(), 0f)

            pathOutline.addArc((width - 3 * circleRadius).toFloat(), 0f, (width - circleRadius).toFloat(), 2 * circleRadius.toFloat(), 90f, 90f)
            pathOutline.addArc((width - circleRadius).toFloat(), 0f, (width + circleRadius).toFloat(), 2 * circleRadius.toFloat(), 270f, 90f)
            outline.setPath(pathOutline)
        } else {
//            @Suppress("DEPRECATION")
//            outline.setConvexPath(pathOutline)
            outline.setRoundRect(
                circleRadius,
                0,
                width - circleRadius,
                2 * circleRadius,
                circleRadius.toFloat()
            )
        }
    }
}
