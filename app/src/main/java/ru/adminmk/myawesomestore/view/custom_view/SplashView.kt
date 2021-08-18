package ru.adminmk.myawesomestore.view.custom_view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.graphics.PathParser
import ru.adminmk.myawesomestore.R

class SplashView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val iconPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    private val logoWidth: Float
    private val logoHeight: Float
    private val viewportWidth: Float
    private val viewportHeight: Float

    private val originalIconPath: Path?
    private val iconPath: Path = Path()
    private val iconMatrix: Matrix = Matrix()

    private var scale = 1f

    private var logoSideHalfWidth: Int = 0
    private var logoSideHalfHeight: Int = 0
    private var scaledLogoWidth: Int = 0
    private var scaledLogoHeight: Int = 0

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        VectorDrawableParser.parsedVectorDrawable(resources, R.drawable.ic_my_awesome_store).run {
            originalIconPath = this?.pathData?.let { PathParser.createPathFromPathData(it) }

            // Assuming vector drawable is square
            logoWidth = dp2px(this?.width ?: 0f)
            logoHeight = dp2px(this?.height ?: 0f)
            viewportWidth = this?.viewportWidth ?: 0f
            viewportHeight = this?.viewportHeight ?: 0f
        }
    }

    fun performAnimation(onFinickCallback: () -> Unit) {

        ValueAnimator.ofFloat(MASK_SCALE_START, MASK_SCALE_END).apply {
            startDelay = SCALE_ANIMATION_START_DELAY
            duration = SCALE_ANIMATION_DURATION
            interpolator = AccelerateInterpolator()

            addUpdateListener {
                scale = it.animatedValue as Float
                requestLayout()
                postInvalidate()
            }

            start()
        }

        ValueAnimator.ofInt(255, 0).apply {
            duration = ALPHA_ANIMATION_DURATION
            interpolator = AccelerateInterpolator()
            addUpdateListener {
                iconPaint.alpha = it.animatedValue as Int

                if (it.animatedValue as Int <= 150) {
                    this@SplashView.alpha = (it.animatedValue as Int).toFloat() / 150 + 0.2f
                }
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    this@SplashView.visibility = GONE
                    onFinickCallback.invoke()
                }

                override fun onAnimationCancel(animation: Animator?) {
                    this@SplashView.visibility = GONE
                }
            })
            start()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        scaledLogoWidth = (scale * logoWidth).toInt()
        scaledLogoHeight = (scale * logoHeight).toInt()
        logoSideHalfWidth = scaledLogoWidth / 2
        logoSideHalfHeight = scaledLogoHeight / 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawPath(
            canvas,
            scaledLogoWidth,
            scaledLogoHeight,
            width / 2 - logoSideHalfWidth,
            height / 2 - logoSideHalfHeight
        )
    }

    private fun drawPath(canvas: Canvas, width: Int, height: Int, dx: Int, dy: Int) {
        originalIconPath ?: return

        val scaleX = width / viewportWidth
        val scaleY = height / viewportHeight

        canvas.save()
        canvas.translate(
            (width - scaleX * viewportWidth) / 2f + dx,
            (height - scaleY * viewportHeight) / 2f + dy
        )
        iconMatrix.reset()
        iconMatrix.setScale(scaleX, scaleY)
        canvas.scale(1.0f, 1.0f)
        iconPath.reset()
        iconPath.addPath(originalIconPath)
        iconPath.transform(iconMatrix)
        canvas.drawPath(iconPath, iconPaint)
        canvas.restore()
    }

    companion object {
        private const val MASK_SCALE_START = 1f
        private const val MASK_SCALE_END = 75f

        private const val SCALE_ANIMATION_START_DELAY = 750L
        private const val SCALE_ANIMATION_DURATION = 1500L

        private const val ALPHA_ANIMATION_DURATION = 2500L
    }
}
