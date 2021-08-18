package ru.adminmk.myawesomestore.view.categories

import android.graphics.Canvas
import android.widget.EdgeEffect
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView

private const val OVERSCROLL_TRANSLATION_MAGNITUDE = 0.02f
private const val FLING_TRANSLATION_MAGNITUDE = 0.05f

class RecyclerViewEdgeEffectFactory : RecyclerView.EdgeEffectFactory() {
    override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect {
        return object : EdgeEffect(view.context) {
            var anim: SpringAnimation? = null

            override fun isFinished(): Boolean {
                // Without this, will skip future calls to onAbsorb()
                return anim?.isRunning?.not() ?: true
            }

            override fun onPull(deltaDistance: Float) {
                super.onPull(deltaDistance)
                handlePull(deltaDistance)
            }

            override fun onPull(deltaDistance: Float, displacement: Float) {
                super.onPull(deltaDistance, displacement)
                handlePull(deltaDistance)
            }

            private fun handlePull(deltaDistance: Float) {
                // Translate the recyclerView with the distance
                val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                val translationYDelta =
                    sign * view.width * deltaDistance * OVERSCROLL_TRANSLATION_MAGNITUDE
                view.translationY += translationYDelta

                anim?.cancel()
            }

            override fun onRelease() {
                super.onRelease()
                // The finger is lifted. Start the animation to bring translation back to the resting state.
                if (view.translationY != 0f) {
                    anim = createAnim()?.also { it.start() }
                }
            }

            override fun onAbsorb(velocity: Int) {
                super.onAbsorb(velocity)
                // The list has reached the edge on fling.
                val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                val translationVelocity = sign * velocity * FLING_TRANSLATION_MAGNITUDE
                anim?.cancel()
                anim = createAnim().setStartVelocity(translationVelocity)
                    .also { it.start() }
            }

            private fun createAnim() =
                SpringAnimation(view, SpringAnimation.TRANSLATION_Y)
                    .setSpring(
                        SpringForce()
                            .setFinalPosition(0f)
                            .setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY)
                            .setStiffness(SpringForce.STIFFNESS_LOW)
                    )

            override fun draw(canvas: Canvas?): Boolean {
                // don't paint the usual edge effect
                return false
            }
        }
    }
}
