package com.ssong_develop.paintview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class RotateView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : View(context) {

    private val MAX_ANGLE: Double = 1e-1
    private val paint = Paint()
    private var previousAngle: Float? = null
    private var mRotation: Float = 0f

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setPaint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val radius = if (width > height) height * 0.666f / 2 else width * 0.666f / 2
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)
        canvas.save()
        canvas.rotate(mRotation, (width / 2).toFloat(), (height / 2).toFloat())
        canvas.drawLine(
            (width / 2).toFloat(),
            height * 0.1f,
            (width / 2).toFloat(),
            height * 0.9f,
            paint
        )
        canvas.restore()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.pointerCount == 2) {
            val currentAngle: Float = angle(event).toFloat()
            previousAngle?.let {
                mRotation -= Math.toDegrees(
                    clamp(
                        (previousAngle!! - currentAngle).toDouble(),
                        -MAX_ANGLE,
                        MAX_ANGLE
                    )
                ).toFloat()

                invalidate()
            }
            previousAngle = currentAngle
        } else {
            previousAngle = null
        }
        return true
    }

    private fun setPaint() {
        paint.apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 10f
            isAntiAlias = true
        }
    }

    private fun angle(event: MotionEvent): Double {
        val deltaX: Double = ((event.getX(0) - event.getX(1)).toDouble())
        val deltaY: Double = ((event.getY(0) - event.getY(1)).toDouble())
        return Math.atan2(deltaY, deltaX)
    }

    private fun clamp(value: Double, min: Double, max: Double): Double {
        if (value < min) {
            return min
        }
        if (value > min) {
            return max
        }
        return value
    }
}