package ru.adminmk.myawesomestore.view.custom_view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.PathParser
import ru.adminmk.myawesomestore.R

class StarRating
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val shapePaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private val fillPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private val shapePath: Path?
    private val shapeMatrix: Matrix = Matrix()
    private val viewportWidth: Float

    private var shapeScale = 1f
    private var fillScale = 1f

    private var numOfStars = 0
    var level = 0f

    private var starSize = 0f
    private var gap = 2f

    init {
        VectorDrawableParser.parsedVectorDrawable(resources, R.drawable.ic_rating_star_empty).run {
            shapePath = this?.pathData?.let { PathParser.createPathFromPathData(it) }
            viewportWidth = this?.viewportWidth ?: 0f
        }

        context.withStyledAttributes(attrs, R.styleable.StarRating) {
            numOfStars = getInt(R.styleable.StarRating_numOfStars, 0)
            shapePaint.strokeWidth = getFloat(R.styleable.StarRating_shapeStrokeWidth, 2f)
            gap = getFloat(R.styleable.StarRating_gap, 2f)
            level = getFloat(R.styleable.StarRating_level, 0f)

            shapePaint.color = getColor(R.styleable.StarRating_shapeColor, 0)
            fillPaint.color = getColor(R.styleable.StarRating_coreColor, 0)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        val maxHorizontalSize = (width - (numOfStars - 1) * gap) / numOfStars
        starSize = Math.min(maxHorizontalSize, height.toFloat())

        shapeScale = starSize / viewportWidth
        fillScale = (starSize - shapePaint.strokeWidth * 2) / viewportWidth

        shapeMatrix.setScale(shapeScale, shapeScale)

        shapePath?.transform(shapeMatrix)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        shapePath ?: return

        canvas?.let {
            with(it) {
                for (i in 0..numOfStars - 1) {
                    save()
                    translate(
                        i * starSize + i * gap,
                        0f
                    )
                    drawPath(shapePath, shapePaint)

                    if (level > i) {
                        clipPath(shapePath)

                        if (level < i + 1) {
                            clipOutRect(
                                shapePaint.strokeWidth * 2 + (starSize - 4 * shapePaint.strokeWidth) * (level - i), 0f,
                                starSize,
                                height.toFloat()
                            )
                        }

                        drawRect(0f, 0f, height.toFloat(), starSize, fillPaint)
                    }
                    restore()
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width: Int
        val height: Int

        val maxHorizontalSize = (widthSize - (numOfStars - 1) * gap) / numOfStars
        starSize = Math.min(maxHorizontalSize, heightSize.toFloat())

        val desiredWidth = (starSize * numOfStars + (numOfStars - 1) * gap).toInt()
//        + paddingLeft + paddingRight
        val desiredHeight = starSize.toInt()
//        + paddingTop + paddingBottom
        // padding is not considered

        // Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            // Must be this size
            width = widthSize
        } else if (widthMode == MeasureSpec.AT_MOST) {
            // Can't be bigger than...
            width = Math.min(desiredWidth, widthSize)
        } else {
            // Be whatever you want
            width = desiredWidth
        }

        // Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            // Must be this size
            height = heightSize
        } else if (heightMode == MeasureSpec.AT_MOST) {
            // Can't be bigger than...
            height = Math.min(desiredHeight, heightSize)
        } else {
            // Be whatever you want
            height = desiredHeight
        }

        setMeasuredDimension(width, height)
    }
}
