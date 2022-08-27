package com.sr01.swipepanel

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import kotlin.math.abs
import kotlin.math.max

class SwipeBehavior @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    CoordinatorLayout.Behavior<NestedScrollBridgeView>(context, attrs) {
        var draggableView: View? = null

        private var isDragging = false
        private var isSettling = false
        var dismissPosition: Float = 0.3F
        var dismissVelocity: Float = 10F
        private var settlingAnimator: ViewPropertyAnimator? = null
        private lateinit var velocityTracker: VelocityTracker

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: NestedScrollBridgeView,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: NestedScrollBridgeView,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (isDragging) {
            onDrag(dy, consumed)
        } else {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        }
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: NestedScrollBridgeView,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (dyConsumed == 0 && dyUnconsumed < 0 && !isDragging) {
            isDragging = true
        }

        if (isDragging) {
            onDrag(dyUnconsumed, consumed)
        }
    }

    private fun onDrag(dy: Int, consumed: IntArray) {
        if (isSettling) {
            isSettling = false
            settlingAnimator?.cancel()
        }

        draggableView?.apply {
            val preTY = translationY
            val targetY = max(0F, translationY - dy)
            translationY = targetY
            consumed[1] = (preTY - targetY).toInt()
        }
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: NestedScrollBridgeView,
        target: View,
        type: Int
    ) {
        if (isDragging) {
            isDragging = false

            draggableView?.let { draggableView ->
                isSettling = true
                velocityTracker.computeCurrentVelocity(1)
                val vY = velocityTracker.yVelocity
                if (vY > dismissVelocity) {
                    dismiss()
                } else if (-vY < ViewConfiguration.getMinimumFlingVelocity() && draggableView.translationY >= draggableView.measuredHeight * dismissPosition) {
                    dismiss()
                } else {
                    resume()
                }
            }
        }
    }

    override fun onNestedFling(
        coordinatorLayout: CoordinatorLayout,
        child: NestedScrollBridgeView,
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        if (!isSettling && !consumed && velocityY > dismissVelocity) {
            isSettling = true
            dismiss()
        }
        return true
    }

    private fun dismiss() {
        draggableView?.apply {
            settle(measuredHeight)
        }
    }

    private fun resume() {
        draggableView?.apply {
            settle(0)
        }
    }

    private fun settle(targetPosition: Int) {
        draggableView?.apply {
            settlingAnimator = animate()
            settlingAnimator!!.translationY(targetPosition.toFloat())
                .setDuration((abs(targetPosition - translationY) / dismissVelocity * 10).toLong())
                .setListener(object : SimpleAnimatorListener() {
                    override fun onAnimationEnd(animation: Animator?) {
                        isSettling = false
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        isSettling = false
                    }
                }).start()
        }
    }

    override fun onAttachedToLayoutParams(params: CoordinatorLayout.LayoutParams) {
        super.onAttachedToLayoutParams(params)
        velocityTracker = VelocityTracker.obtain()
    }

    override fun onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams()
        velocityTracker.recycle()
    }

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: NestedScrollBridgeView,
        ev: MotionEvent
    ): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear()
        }

        velocityTracker.addMovement(ev)

        return super.onInterceptTouchEvent(parent, child, ev)
    }
}

private open class SimpleAnimatorListener: Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator?) {

    }

    override fun onAnimationEnd(animation: Animator?) {

    }

    override fun onAnimationCancel(animation: Animator?) {

    }

    override fun onAnimationRepeat(animation: Animator?) {

    }
}